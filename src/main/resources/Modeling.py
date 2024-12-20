import pandas as pd
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import LabelEncoder
from sklearn.impute import SimpleImputer
from sklearn.linear_model import LinearRegression
from sklearn.tree import DecisionTreeRegressor
from sklearn.ensemble import RandomForestRegressor
from sklearn.metrics import r2_score
import pickle

# Load the dataset
file_path = 'Salary_Data.csv'
data = pd.read_csv(file_path)

# Handling missing values
data['Age'].fillna(data['Age'].median(), inplace=True)
data['Years of Experience'].fillna(data['Years of Experience'].median(), inplace=True)
data['Salary'].dropna(inplace=True)
data.dropna(inplace=True)  # Remove rows with any remaining missing values

# Encoding categorical features
label_encoders = {}
categorical_cols = ['Gender', 'Education Level', 'Job Title']

for col in categorical_cols:
    le = LabelEncoder()
    data[col] = le.fit_transform(data[col])
    label_encoders[col] = le

# Splitting data into input and target
X = data.drop('Salary', axis=1)
y = data['Salary']

# Splitting into training and testing sets
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

# Initialize models
models = {
    "Linear Regression": LinearRegression(),
    "Decision Tree": DecisionTreeRegressor(random_state=42),
    "Random Forest": RandomForestRegressor(random_state=42)
}

# Train and evaluate models
results = {}
for name, model in models.items():
    model.fit(X_train, y_train)
    predictions = model.predict(X_test)
    accuracy = r2_score(y_test, predictions)
    results[name] = (model, accuracy)

# Find the best model
best_model_name = max(results, key=lambda name: results[name][1])
best_model, best_accuracy = results[best_model_name]

# Save the best model
model_file_path = 'SalaryData_model.pkl'
with open(model_file_path, 'wb') as file:
    pickle.dump(best_model, file)

# Output results
print(f"Best Model: {best_model_name}")
print(f"Accuracy (R^2): {best_accuracy}")
print(f"Model saved at: {model_file_path}")