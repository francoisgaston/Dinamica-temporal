import pandas as pd
import matplotlib.pyplot as plt
import math


# ---------------------------------------------------
# DATOS A CAMBIAR SEGÚN EL CASO DE ESTUDIO
# ---------------------------------------------------
OUTPUT_PATH = '../Simulation/Output/'
DEFAULT_INPUT_PATH = '../Simulation/Input/'
AVG_PATH = '../output/'
DELTA_T = 0.001
# ---------------------------------------------------

# ---------------------------------------------------
# CONSTANTES
# ---------------------------------------------------
# ---------------------------------------------------


def main():
    if DELTA_T == 10**-3:
        particle_coords = pd.read_csv(OUTPUT_PATH + 'VerletOutput_' + str(DELTA_T)[2:] + '.csv')
    else:
        particle_coords = pd.read_csv(OUTPUT_PATH + 'VerletOutput_' + '0E' + str(math.log10(DELTA_T))[:2] + '.csv')

    plt.figure(figsize=(10, 6))
    plt.plot(particle_coords['timeFrame'], particle_coords['position'], label=f'Borde')

    plt.xlabel('Tiempo (s)', fontsize=16)
    plt.ylabel('Posición (m)', fontsize=16)
    plt.grid(False)
    plt.legend(bbox_to_anchor=(0.5, 1.1), loc='upper center', borderaxespad=0, fontsize=12, ncol=4)
    plt.show()


if __name__ == '__main__':
    main()
