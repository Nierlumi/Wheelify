# Wheelify API Documentation

## Overview
The **Wheelify API** is designed to accurately classify vehicle types from uploaded images. It processes images using a machine learning model to predict vehicle categories. This API is especially useful for automating tasks like toll payment classification, parking management, and more.

### Features:
- Accepts an image of a vehicle.
- Preprocesses the image for analysis.
- Runs a machine learning model to classify the vehicle type.
- Returns the classification result and confidence score.
- Optionally stores the image and prediction results in Google Cloud Storage and Firestore.

---

## Base URL
https://wheelify-model-api-2-199228555553.asia-southeast2.run.app/


---

## Endpoints

### `/process` (POST)
Processes an uploaded image to classify the vehicle type.

- **URL**:  
https://wheelify-model-api-2-199228555553.asia-southeast2.run.app/process


- **Method**:  
`POST`

- **Content-Type**:  
`multipart/form-data`

- **Function**:  
Accepts a vehicle image, processes it using the ML model, and returns the predicted class and confidence score.

#### Request Body:
| Field | Type    | Description                                       |
|-------|---------|---------------------------------------------------|
| image | `File`  | Vehicle image in JPG, PNG, or other supported formats. |

#### Response:
| Field       | Type     | Description                                              |
|-------------|----------|----------------------------------------------------------|
| class       | `string` | Predicted vehicle class.                                 |
| confidence  | `float`  | Model's confidence score for the prediction.            |
| image_url   | `string` | URL of the uploaded image in Google Cloud Storage.       |

---

### HTTP Status Codes:
- **200 OK**: Request was successfully processed, and the classification result is returned.
- **400 Bad Request**: Invalid request, e.g., no image provided.
- Example response:
  ```json
  {
    "error": "No image part"
  }
  ```
- **500 Internal Server Error**: An error occurred while processing the request.
- Example response:
  ```json
  {
    "error": "Error during prediction: ..."
  }
  ```

---

## Testing the API

### 1. Using Postman
1. Open Postman and create a new request.
2. Set the request method to `POST`.
3. Enter the URL:  https://wheelify-model-api-2-199228555553.asia-southeast2.run.app/process
4. In the **Body** tab, select `form-data`:
- Add a key named `image`, set the type to `File`, and upload a vehicle image.
5. Click **Send**.
6. Check the response for the predicted class and confidence score.

### 2. Using `curl`
Run the following command in your terminal, replacing `<path-to-image>` with the file path to your image:

```bash
curl -X POST \
-F "image=@<path-to-image>" \
https://wheelify-model-api-2-199228555553.asia-southeast2.run.app/process

## Technical Requirements

### Libraries Used

Ensure these libraries are installed in your Python environment:

* Flask
* tensorflow
* Pillow
* opencv-python
* google-cloud-storage
* google-cloud-firestore
* scikit-learn
* scikit-image
* matplotlib
* pandas

### File Requirements

* **Model file:** `saved_model` directory containing the TensorFlow model.
* **Labels file:** `labels.npy` containing class labels.

## Deployment Guide

### Prerequisites

* Google Cloud project with Cloud Run and Cloud Storage enabled.
* Docker installed locally.

### Steps

1. **Create a Dockerfile:** Include instructions to build and run the API container.

2. **Build Docker Image:**

   ```bash
   docker build -t wheelify-api .
