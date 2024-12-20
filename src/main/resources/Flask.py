import pandas as pd
from flask import Flask, jsonify
from sqlalchemy import create_engine, inspect

app = Flask(__name__)
DATABASE_URI = "mysql+pymysql://root:@127.0.0.1/job_salary"

engine = create_engine(DATABASE_URI)

@app.route('/', methods=['GET'])
def get_salary_data():
    filepath = "src/main/resources/Salary_Data.csv"
    data_csv = pd.read_csv(filepath)
    data_csv = data_csv.dropna()
    # Periksa apakah tabel salary_datas ada
    inspector = inspect(engine)
    if 'salary_datas' in inspector.get_table_names():
        with engine.connect() as conn:
            data_db = pd.read_sql("SELECT * FROM salary_datas", conn)

            # Jika tabel kosong, hanya gunakan data dari CSV
            if data_db.empty:
                merged_data = data_csv
            else:
                # Gabungkan data dari database dengan data CSV
                merged_data = pd.concat([data_csv, data_db], ignore_index=True)

            # Hilangkan kolom duplikat
            merged_data = merged_data.loc[:, ~merged_data.columns.duplicated()]
    else:
        # Jika tabel tidak ada, gunakan hanya data CSV
        merged_data = data_csv

    # Hapus kolom 'id' jika ada
    if 'id' in merged_data.columns:
        merged_data = merged_data.drop(columns=['id'])

    # Mengembalikan data gabungan dalam format JSON
    return jsonify(merged_data.to_dict(orient='records'))

if __name__ == '__main__':
    app.run(port=5000)
