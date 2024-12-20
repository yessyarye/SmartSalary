from flask import Flask, request, jsonify
import joblib
import pandas as pd
import os

app = Flask(__name__)


model_path = os.path.join(os.path.dirname(__file__), 'best_salary_model.pkl')
model = joblib.load(model_path)


@app.route('/', methods=['POST'])
def predict():
    try:
        # Ambil data dari permintaan
        data = request.get_json()

        # Ambil input dari JSON
        age = int(data.get('age'))
        gender = data.get('gender')
        education_level = data.get('educationLevel')
        job_title = data.get('jobTitle')
        years_experience = int(data.get('yearsExperience'))

        # Buat DataFrame untuk prediksi
        input_data = pd.DataFrame({
            'Age': [age],
            'Gender': [gender],
            'Education Level': [education_level],
            'Job Title': [job_title],
            'Years of Experience': [years_experience]
        })

        # Prediksi dengan model
        prediction = model.predict(input_data)

        # Kirim hasil prediksi sebagai respons
        return jsonify({'predictedSalary': float(prediction[0])})

    except Exception as e:
        return jsonify({'error': str(e)}), 400


if __name__ == '__main__':
    app.run(port=6000)
