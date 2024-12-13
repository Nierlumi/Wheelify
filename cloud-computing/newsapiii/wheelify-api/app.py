from flask import Flask, request, jsonify
import tensorflow as tf
import numpy as np
from PIL import Image
import os
import uuid
from google.cloud import storage
from google.cloud import firestore
import logging

# Konfigurasi logging
logging.basicConfig(level=logging.DEBUG)  #

app = Flask(__name__)

# --- Konfigurasi ---
BUCKET_NAME = "wheelify_user-images" 
PROJECT_ID = "wheelify-441715"  

# --- Load model ---
model_path = 'saved_model'
try:
    loaded_model = tf.saved_model.load(model_path)
    logging.info("Model loaded successfully")
except OSError as e:
    logging.error(f"Error loading model: {e}")
    exit(1)

# Cek signature
print(loaded_model.signatures)

# Input signature
@tf.function(input_signature=[tf.TensorSpec(shape=[None, 224, 224, 3], dtype=tf.float32)])
def predict_function(image):
    predictions = loaded_model.signatures['serving_default'](inputs=image)
    return predictions['output_1']

# Label kelas
try:
    labels = np.load('labels.npy')
    logging.info("Labels loaded successfully")
except FileNotFoundError:
    logging.error("Error: labels file not found")
    exit(1)

# --- Fungsi untuk upload ke Cloud Storage ---
def upload_to_bucket(image, image_id):
    try:
        client = storage.Client()
        logging.debug(f"Storage client created: {client}")
        bucket = client.bucket(BUCKET_NAME)
        logging.debug(f"Bucket object: {bucket}")
        blob = bucket.blob(f"{image_id}.jpg")
        logging.debug(f"Blob object: {blob}")
        response = blob.upload_from_string(image.tobytes(), content_type='image/jpeg')
        logging.debug(f"Upload response: {response}")
        public_url = blob.public_url
        logging.info(f"Image uploaded to {public_url}")
        return public_url
    except Exception as e:
        logging.exception(f"Error uploading to bucket: {e}")
        return jsonify({'error': 'Gagal mengupload gambar ke Cloud Storage.'}), 500  # Pesan error yang lebih informatif


# --- Fungsi untuk menyimpan ke Firestore ---
def save_to_firestore(image_id, predicted_class, confidence):
    try:
        db = firestore.Client(project=PROJECT_ID)
        doc_ref = db.collection('predictions').document(image_id)
        doc_ref.set({
            'class': predicted_class,
            'confidence': float(confidence)  # Konversi ke float
        })
        logging.info(f"Prediction saved to Firestore for image ID: {image_id}")
    except Exception as e:
        logging.exception(f"Error saving to Firestore: {e}")

# --- Endpoint /process ---
@app.route('/process', methods=['POST'])
def process_image():
    if request.method == 'POST':
        logging.info("Received request on /process")
        if 'image' not in request.files:
            return jsonify({'error': 'No image part'}), 400

        file = request.files['image']

        if file.filename == '':
            return jsonify({'error': 'No selected file'}), 400

        try:
            # 1. Generate unique ID
            image_id = str(uuid.uuid4())
            logging.debug(f"Generated image ID: {image_id}")

            # 2. Baca dan proses gambar
            try:
                image = Image.open(file.stream)
                logging.debug("Image opened successfully")
            except PIL.UnidentifiedImageError:
                logging.error("Invalid image file")
                return jsonify({'error': 'Invalid image file'}), 400

            image = image.resize((224, 224))
            image = np.array(image) / 255.0
            image = np.expand_dims(image, axis=0)

            # 3. Prediksi
            try:
                predictions = predict_function(image)
                logging.debug(f"Prediction result: {predictions}")
            except Exception as e:
                logging.exception(f"Error during prediction: {e}")
                return jsonify({'error': f'Error during prediction: {str(e)}'}), 500

            # 4. Format output
            class_index = np.argmax(predictions[0])
            predicted_class = labels[class_index]
            confidence = predictions[0][class_index]
            logging.info(f"Predicted class: {predicted_class} with confidence {confidence}")

            # 5. Upload ke Cloud Storage
            image_url = upload_to_bucket(image, image_id)

            # 6. Simpan ke Firestore
            save_to_firestore(image_id, predicted_class, confidence)

            # 7. Kembalikan hasil
            return jsonify({'class': predicted_class,
                            'confidence': float(confidence),
                            'image_url': image_url})

        except Exception as e:
            logging.exception(f"An unexpected error occurred: {e}")
            return jsonify({'error': f'An unexpected error occurred: {str(e)}'}), 500

if __name__ == '__main__':
    app.run(host="0.0.0.0", port=8080)