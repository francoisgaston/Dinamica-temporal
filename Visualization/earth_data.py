import csv

# Archivo de entrada
planet = "mars"
input_file = '../Simulation/Input/horizons_results_' + planet + '.txt'
output_file = '../Simulation/Input/' + planet + '.csv'

# Lista para guardar los datos
data = []

# Abrir el archivo de entrada y extraer las columnas relevantes
with open(input_file, 'r') as file:
    for line in file:
        # Verificamos si la línea es parte de los datos válidos
        if line.strip().startswith('246'):
            # Dividimos la línea en partes por comas
            parts = line.strip().split(',')
            if len(parts) >= 6:  # Verificamos que tenemos suficientes columnas
                x = parts[2].strip()  # El índice de 'X'
                y = parts[3].strip()  # El índice de 'Y'
                vx = parts[5].strip()  # El índice de 'VX'
                vy = parts[6].strip()  # El índice de 'VY'
                data.append([x, y, vx, vy])  # Añadimos 'X', 'Y', 'VX', 'VY' a la lista

# Escribir los datos en un archivo CSV
with open(output_file, 'w', newline='') as csvfile:
    csvwriter = csv.writer(csvfile)
    csvwriter.writerow(['X', 'Y', 'VX', 'VY'])  # Encabezado para CSV
    csvwriter.writerows(data)  # Escribir los datos