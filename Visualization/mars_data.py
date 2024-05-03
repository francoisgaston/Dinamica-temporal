import csv
import re

# Archivo de entrada con tus datos
input_file = '../Simulation/Input/horizons_results_mars.txt'
output_file = '../Simulation/Input/mars.csv'

# Expresión regular para extraer las columnas X e Y
pattern = re.compile(r'([-\d.E+]+), .* ([\d.E+-]+), ([\d.E+-]+), ([\d.E+-]+), .*')

# Lista para guardar los datos
data = []

# Abrir el archivo de entrada y extraer los datos relevantes
with open(input_file, 'r') as file:
    for line in file:
        if line.startswith('246'):  # Las líneas con datos relevantes
            parts = line.strip().split(',')
            if len(parts) >= 3:  # Asegurar que tenemos suficientes columnas
                x = parts[2].strip()  # El índice correcto para 'X'
                y = parts[3].strip()  # El índice correcto para 'Y'
                data.append([x, y])  # Añadimos las columnas 'X' y 'Y' a la lista

# Escribir los datos en un archivo CSV
with open(output_file, 'w', newline='') as csvfile:
    csvwriter = csv.writer(csvfile)
    csvwriter.writerow(['X', 'Y'])  # Encabezado
    csvwriter.writerows(data)  # Datos