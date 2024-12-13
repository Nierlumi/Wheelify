# Wheelify - Capstone Project Bangkit Academy 2024

## Table of Contents
1. [General Information](#general-information)
2. [Cloud Computing Documentation](#cloud-computing-documentation)
    - [Project Description](#project-description)
    - [System Architecture](#system-architecture)
    - [Deployment Steps](#deployment-steps)
    - [Local Installation Guide](#local-installation-guide)
    - [Technologies Used](#technologies-used)
    - [Repository Structure](#repository-structure)
3. [Mobile Development Documentation](#mobile-development-documentation)
4. [Machine Learning Documentation](#machine-learning-documentation)
5. [Contributors](#contributors)

---

## General Information
Wheelify is a vehicle classification mobile application designed to streamline the process of identifying vehicle types using advanced machine learning models. The project is part of Bangkit Academy 2024 Capstone Program and integrates three learning paths: Cloud Computing, Mobile Development, and Machine Learning.

---

## Cloud Computing Documentation

### Project Description
Wheelify’s cloud infrastructure ensures efficient and scalable deployment of the machine learning model and its integration with the mobile application. Key technical challenges addressed include:
- Building an accurate and efficient machine learning model for image classification.
- Handling variations in image conditions (lighting, angles, backgrounds).
- Integrating the machine learning model with APIs and mobile applications.
- Deploying and managing the API on Google Cloud Run.
- Ensuring security and performance of the API.

### System Architecture
#### Key Cloud Components:
1. **Cloud Run**: Serverless platform for deploying and running containerized APIs.
2. **Container Registry**: Stores Docker images for the Wheelify API.
3. **Cloud Storage**: Object storage for user-uploaded images.
4. **Firestore**: NoSQL database for storing classification results.

#### Workflow:
1. A user uploads an image through the mobile application.
2. The mobile app sends the image to the Wheelify API running on Cloud Run.
3. The API performs the following:
   - Pre-processes the image.
   - Runs the machine learning model for classification.
   - Uploads the image to Cloud Storage.
   - Saves classification results to Firestore.
   - Returns classification results to the mobile application.
4. The mobile app displays the classification results to the user.

**System Architecture Diagram**:
*(To be included in the repository as an image file, e.g., `architecture-diagram.png`)*

### Deployment Steps
1. **API Development**:
   - Create an API in `app.py` that loads the machine learning model and handles image processing, prediction, and integration with Cloud Storage and Firestore.
   - Define endpoints for processing images (e.g., `/process`).
   
2. **Create a Dockerfile**:
   ```dockerfile
   FROM python:3.9-slim
   WORKDIR /app
   COPY requirements.txt .
   RUN pip install -r requirements.txt
   COPY . .
   CMD ["python", "app.py"]
Build and Push Docker Image:

Build the Docker image:
bash
Salin kode
docker build -t gcr.io/<PROJECT_ID>/wheelify-api .
Push the image to Container Registry:
bash
Salin kode
docker push gcr.io/<PROJECT_ID>/wheelify-api
Deploy to Cloud Run:

Deploy the containerized API:
bash
Salin kode
gcloud run deploy wheelify-api \
--image gcr.io/<PROJECT_ID>/wheelify-api \
--region <REGION> \
--platform managed \
--allow-unauthenticated
Testing the API:

Use tools like Postman or the mobile application to test the /process endpoint.
Local Installation Guide
Prerequisites:

Python 3.7 or higher installed.
Google Cloud account with Cloud Storage and Firestore enabled.
Setup Environment:

Clone the repository and navigate to the project folder.
Set up a virtual environment:
bash
Salin kode
python -m venv env
source env/bin/activate  # On Windows: env\Scripts\activate
Install dependencies:
bash
Salin kode
pip install -r requirements.txt
Set Environment Variables:

Set the path to your Google Cloud credentials:
bash
Salin kode
export GOOGLE_APPLICATION_CREDENTIALS="path/to/credentials.json"
Run the API Locally:

Start the API:
bash
Salin kode
python app.py
Test the API:

Use tools like curl or Postman to send requests to the local server (default: http://127.0.0.1:8080).
Technologies Used
Machine Learning: TensorFlow, Python
Cloud Services: Cloud Run, Cloud Storage, Firestore, Container Registry
Development Tools: Docker, Flask
Repository Structure
sql
Salin kode
|-- wheelify-api/
    |-- app.py
    |-- requirements.txt
    |-- Dockerfile
    |-- saved_model/  # Contains the TensorFlow model
    |-- labels.npy    # Contains the label mappings
    |-- README.md
    |-- architecture-diagram.png
Mobile Development Documentation
(To be updated later.)

Machine Learning Documentation
(To be updated later.)

Contributors
M123B4KX4368 – Tsabitah Elysia Krismananda – Politeknik Elektronika Negeri Surabaya
M123B4KX1199 – Dwi Rahayu Wulanida – Politeknik Elektronika Negeri Surabaya
M123B4KX2425 – Masitha Mirzaky Firdaus – Politeknik Elektronika Negeri Surabaya
C296B4KX1954 – Iluh Meiranda Dyah Setyawati – Universitas Pembangunan Nasional Veteran Jawa Timur
C296B4KY2361 – M. Saaduddin Abdillah Yusuf – Universitas Pembangunan Nasional Veteran Jawa Timur
A552B4KY2090 – Jonathan Kaparang – Universitas 45 Surabaya
A552B4KY0148 – Afan Kristianto – Universitas 45 Surabaya
markdown
Salin kode
