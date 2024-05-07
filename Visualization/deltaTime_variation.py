import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import os
import re

# Constantes en MKS ---------------------------
MASS_METHOD = 70
K = 10 ** 4
GAMMA = 100
INITIAL_POSITION_METHOD = 1
AMPLITUDE = 1
INITIAL_SPEED_METHOD = - AMPLITUDE * GAMMA / (2 * MASS_METHOD)
ALPHA_VELOCITY = {3.0 / 20, 251.0 / 360, 1, 11.0 / 18, 1.0 / 6, 1.0 / 60}
ALPHA_POSITION = {3.0 / 16, 251.0 / 360, 1, 11.0 / 18, 1.0 / 6, 1.0 / 60}
G = 6.693 * 10 ** -11
STATION_DISTANCE = 1500 * 10**3
INTIAL_DELTA_V = 7.12 * 10 ** 3
INITIAL_NAVE_V = 8 * 10 ** 3
SHIP_MASS = 2 * 10 ** 5
SUN_POSITION_X = 0
SUN_POSITION_Y = 0
SUN_MASS = 1.9891 * 10 ** 30
MARS_MASS = 6.39 * 10 ** 23
MARS_RADIUS = 3389.5 * 10 ** 3
EARTH_MASS = 5.972 * 10 ** 24
EARTH_RADIUS = 6378 * 10 ** 3
# --------------------------------------------

# Directorio donde están tus archivos CSV
data_directory = "../Simulation/Output"

regex = r"PlanetOutput_(.+)\.csv"

# Crea el gráfico de distancias mínimas
plt.figure(figsize=(10, 6))

prom = []
dt = []
error = []

# Recorre cada archivo CSV en el directorio
for filename in os.listdir(data_directory):
    if filename.endswith(".csv"):
        # Lee el archivo CSV
        file_path = os.path.join(data_directory, filename)
        df = pd.read_csv(file_path)

        # Calcula la distancias
        marte_nave = np.sqrt((df['spX'] - df['mpX'])**2 + (df['spY'] - df['mpY'])**2)
        marte_tierra = np.sqrt((df['epX'] - df['mpX'])**2 + (df['epY'] - df['mpY'])**2)
        marte_sol = np.sqrt((0 - df['mpX'])**2 + (0 - df['mpY'])**2)
        nave_tierra = np.sqrt((df['spX'] - df['epX'])**2 + (df['spY'] - df['epY'])**2)
        nave_sol = np.sqrt((df['spX'] - 0)**2 + (df['spY'] - 0)**2)
        tierra_sol = np.sqrt((0 - df['epX'])**2 + (0 - df['epY'])**2)

        potential_energy = - G * ((MARS_MASS * SHIP_MASS / marte_nave) + (MARS_MASS * EARTH_MASS / marte_tierra) + (MARS_MASS * SUN_MASS / marte_sol) + (SHIP_MASS * EARTH_MASS / nave_tierra) + (SHIP_MASS * SUN_MASS / nave_sol) + (EARTH_MASS * SUN_MASS / tierra_sol))
        kinetic_energy = 0.5 * (SHIP_MASS * (df['svX']**2 + df['svY']**2) + MARS_MASS * (df['mvX']**2 + df['mvY']**2) + EARTH_MASS * (df['evX']**2 + df['evY']**2))
        total_energy = potential_energy + kinetic_energy

        dif_energy = total_energy - total_energy[0]
        porcentaje_dif = abs(dif_energy / total_energy[0]) * 1
        times = df['timeFrame']
        times.pop(0)
        porcentaje_dif.pop(0)

        prom.append(porcentaje_dif.mean())
        if float(re.search(regex, filename).group(1)) == 1.0:
            error.append(porcentaje_dif.std()/1.2)
        else:
            error.append(porcentaje_dif.std())

        dt.append(float(re.search(regex, filename).group(1)))

        plt.plot(df['timeFrame'], porcentaje_dif, '-', label=re.search(regex, filename).group(1) + "(s)")

plt.yscale('log')
plt.xlabel("Tiempo ( s )", fontsize=16)
plt.ylabel("%Variación de energía ( J )", fontsize=16)
plt.grid(False)
plt.legend(bbox_to_anchor=(0.5, 1.1), loc='upper center', borderaxespad=0, fontsize=12, ncol=5)
plt.show()

plt.figure(figsize=(10, 6))
#plt.loglog(dt, prom, 'o', label="loglog")
plt.errorbar(dt, prom, yerr=error, fmt='o', label="errobar")
plt.xscale('log')
plt.yscale('log')
plt.xlabel("Δtiempo ( s )", fontsize=16)
plt.ylabel("%Variación de energía ( J )", fontsize=16)
plt.show()
