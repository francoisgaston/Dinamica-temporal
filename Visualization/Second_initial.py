import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import os
import re
import matplotlib.ticker as ticker  # Importa el módulo ticker

# Directorio donde están tus archivos CSV
data_directory = "../Simulation/Output/hours"

regex = r"_(\d+)"

# Lista para guardar las distancias mínimas
min_distances = []

# Recorre cada archivo CSV en el directorio
for filename in os.listdir(data_directory):
    if filename.endswith(".csv"):
        # Lee el archivo CSV
        file_path = os.path.join(data_directory, filename)
        df = pd.read_csv(file_path)

        # Calcula la distancia entre (spX, spY) y (mpX, mpY)
        distances = np.sqrt((df['spX'] - df['epX'])**2 + (df['spY'] - df['epY'])**2)

        # Encuentra la distancia mínima
        min_distance = distances.min()

        # Guarda el nombre del archivo y la distancia mínima
        min_distances.append((int(re.search(regex, filename).group(1)), min_distance))

# Convierte a DataFrame para facilitar el uso
min_distances_df = pd.DataFrame(min_distances, columns=["Filename", "Min Distance"])

# Ordena por nombre del archivo (si es necesario)
min_distances_df.sort_values(by="Filename", inplace=True)

# Crea el gráfico de distancias mínimas
plt.figure(figsize=(10, 6))
plt.plot(min_distances_df['Filename'], min_distances_df['Min Distance'], '-o', label='Min Distance')
plt.xlabel("Días posteriores", fontsize=16)
plt.ylabel("Distancia mínima (m)", fontsize=16)
plt.grid(False)

plt.ticklabel_format(axis="y", style="sci", useMathText=True)

x_labels = min_distances_df['Filename'].values
x_tick_interval = 10
# Establece el formato de las etiquetas para evitar decimales
formatter = ticker.FormatStrFormatter('%d')
plt.gca().xaxis.set_major_formatter(formatter)

plt.show()