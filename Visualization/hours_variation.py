import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import os
import re

# Directorio donde están tus archivos CSV
data_directory = "../Simulation/Output"

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
        min_distance = distances.min()/10

        if float(re.search(regex, filename).group(1)) > 16:
            min_distance -= 2*10**7

        # Guarda el nombre del archivo y la distancia mínima
        min_distances.append((float(re.search(regex, filename).group(1)), min_distance))

data = [
    2.2995638315271583e9, 2.2020126711048427e9, 2.104457496777567e9,
    2.0068983513209558e9, 1.9093352848358634e9, 1.811768338994881e9,
    1.7141975632716e9, 1.616622999927181e9, 1.5190446935944526e9,
    1.42146269637121e9, 1.3238770497811198e9, 1.2262878051851885e9,
    1.1286950027228594e9, 1.0310986891338432e9, 9.334989197072544e8,
    8.358957310014936e8, 7.382891795104407e8, 6.406793072294868e8,
    5.430661607108827e8, 4.4544979707249874e8, 3.4783024770492435e8,
    2.5020758723539102e8, 2.3488666063340068e8,
    2.3493812739105254e8, 2.3490665090276358e8,
    2.3496859072620302e8, 2.3768217951816e8, 3.353223753787688e8,
    4.3296535755459696e8, 5.306110887763763e8, 6.282595068138747e8,
    7.259105618049994e8, 8.235642142027948e8, 9.212204097120516e8,
    1.0188790954910744e9, 1.11654022331844e9, 1.2142037476076734e9,
    1.3118696106463223e9, 1.409537761158348e9, 1.5072081559779325e9,
    1.6048807392629404e9, 1.7025554625612447e9, 1.8002322709846222e9,
    1.8979111159417326e9, 1.9955919465923142e9, 2.093274710620959e9,
    2.190959355564935e9, 2.2886458271024756e9, 2.3863340780895505e9
]

data2 = [
    2.2995638315271583e9, 2.2020126711048427e9, 2.104457496777567e9,
    2.0068983513209558e9, 1.9093352848358634e9, 1.811768338994881e9,
    1.7141975632716e9, 1.616622999927181e9, 1.5190446935944526e9,
    1.42146269637121e9, 1.3238770497811198e9, 1.2262878051851885e9,
    1.1286950027228594e9, 1.0310986891338432e9, 9.334989197072544e8,
    8.358957310014936e8, 7.382891795104407e8, 6.406793072294868e8,
    5.430661607108827e8, 4.4544979707249874e8, 3.4783024770492435e8,
    2.5020758723539102e8, 1.5258183626741037e8, 5.495312558359547e7,
    4.241049856895701e7, 1.4004483771465594e8, 2.3768217951816e8,
    3.353223753787688e8, 4.3296535755459696e8, 5.306110887763763e8,
    6.282595068138747e8, 7.259105618049994e8, 8.235642142027948e8,
    9.212204097120516e8, 1.0188790954910744e9, 1.11654022331844e9,
    1.2142037476076734e9, 1.3118696106463223e9, 1.409537761158348e9,
    1.5072081559779325e9, 1.6048807392629404e9, 1.7025554625612447e9,
    1.8002322709846222e9, 1.8979111159417326e9, 1.9955919465923142e9,
    2.093274710620959e9, 2.190959355564935e9, 2.2886458271024756e9,
    2.3863340780895505e9
]
# Convierte a DataFrame para facilitar el uso
min_distances_df = pd.DataFrame(min_distances, columns=["Filename", "Min Distance"])

# Ordena por nombre del archivo (si es necesario)
min_distances_df.sort_values(by="Filename", inplace=True)

# Crea el gráfico de distancias mínimas
plt.figure(figsize=(10, 6))
#plt.plot(min_distances_df['Filename'], data2, 'r--', label='Min Distance')
#plt.plot(min_distances_df['Filename'], data, 'o-', label='Min Distance')
plt.plot(min_distances_df['Filename'], min_distances_df["Min Distance"], 'o-', label='Min Distance')
plt.xlabel("Tiempo (horas)", fontsize=16)
plt.ylabel("Distancia mínima (m)", fontsize=16)
plt.ticklabel_format(axis="y", style="sci", useMathText=True)
plt.grid(False)
plt.show()