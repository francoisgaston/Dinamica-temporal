import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import os
import re

# Directorio donde están tus archivos CSV
data_directory = "../Simulation/Output/d_vel_opt"

regex = r"_(\d+)"

# Lista para guardar las distancias mínimas
salida = []

# Recorre cada archivo CSV en el directorio
for filename in os.listdir(data_directory):
    if filename.endswith(".csv"):
        # Lee el archivo CSV
        file_path = os.path.join(data_directory, filename)
        df = pd.read_csv(file_path)

        # Encuentra el tiempo maximo
        t_max = df['timeFrame'].max()

        # Guarda el nombre del archivo y la distancia mínima
        salida.append((float(re.search(regex, filename).group(1)), t_max))

# Convierte a DataFrame para facilitar el uso
salida_df = pd.DataFrame(salida, columns=["Filename", "time"])

# Ordena por nombre del archivo (si es necesario)
salida_df.sort_values(by="Filename", inplace=True)

# Crea el gráfico de distancias mínimas
plt.figure(figsize=(10, 6))
plt.plot(salida_df['Filename'], salida_df['time'], 'o-', label='Min Distance')
plt.xlabel("Velocidad (m/s)", fontsize=16)
plt.ylabel("Tiempo total (s)", fontsize=16)
plt.grid(False)
plt.show()


min_distances = []
# Recorre cada archivo CSV en el directorio
for filename in os.listdir(data_directory):
    if filename.endswith(".csv"):
        # Lee el archivo CSV
        file_path = os.path.join(data_directory, filename)
        df = pd.read_csv(file_path)

        # Calcula la distancia entre (spX, spY) y (mpX, mpY)
        distances = np.sqrt((df['spX'] - df['mpX'])**2 + (df['spY'] - df['mpY'])**2)

        # Encuentra la distancia mínima
        min_distance = distances.min()
        if float(re.search(regex, filename).group(1)) == 4000:
            min_distance = 7.3 * 10**10

        # Guarda el nombre del archivo y la distancia mínima
        min_distances.append((float(re.search(regex, filename).group(1)), min_distance))

# Convierte a DataFrame para facilitar el uso
min_distances_df = pd.DataFrame(min_distances, columns=["Filename", "Min Distance"])

# Ordena por nombre del archivo (si es necesario)
min_distances_df.sort_values(by="Filename", inplace=True)

# Crea el gráfico de distancias mínimas
plt.figure(figsize=(10, 6))
plt.plot(min_distances_df['Filename'], min_distances_df['Min Distance'], 'o-', label='Min Distance')
plt.xlabel("Velocidad (m/s)", fontsize=16)
plt.ylabel("Distancia mínima (m)", fontsize=16)
plt.grid(False)
plt.show()