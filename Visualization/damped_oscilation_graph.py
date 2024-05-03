import pandas as pd
import matplotlib.pyplot as plt
import math
import numpy as np


# ---------------------------------------------------
# DATOS A CAMBIAR SEGÚN EL CASO DE ESTUDIO
# ---------------------------------------------------
OUTPUT_PATH = '../Simulation/Output/'
DELTA_T = 0.001
TIME_STEPS = ['01', '001', '0E-4', '0E-5', '0E-6']
METHODS = ["Verlet", "Beeman", "Gear", "Analitica"]
# ---------------------------------------------------


def plots():
    plt.figure(figsize=(10, 6))

    for method in METHODS:
        if DELTA_T <= 10**-3:
            particle_coords = pd.read_csv(OUTPUT_PATH + method +  'Output_' + str(DELTA_T)[2:] + '.csv')
        else:
            particle_coords = pd.read_csv(OUTPUT_PATH + method + 'Output_' + '0E' + str(math.log10(DELTA_T))[:2] + '.csv')

        plt.plot(particle_coords['timeFrame'], particle_coords['position'], label=method)

    plt.xlabel('Tiempo (s)', fontsize=16)
    plt.ylabel('Posición (m)', fontsize=16)
    plt.grid(False)
    plt.legend(bbox_to_anchor=(0.5, 1.1), loc='upper center', borderaxespad=0, fontsize=12, ncol=4)
    plt.show()


def cuadratic_error():
    plt.figure(figsize=(10, 6))
    time_steps = []
    for i in range(2,7): # [ x , y )
        time_steps.append(10 ** -i)

    for method in METHODS:
        if method == 'Analitica':
            continue
        ecm = []
        i=0
        for time_step in TIME_STEPS:
            analytical_coords = pd.read_csv(OUTPUT_PATH + 'Analitica' +  'Output_' + time_step + '.csv')
            method_coords = pd.read_csv(OUTPUT_PATH + method + 'Output_' + time_step + '.csv')

            if method == 'Verlet':
                ecm.append(calculate_ecm_verlet(analytical_coords['position'], method_coords['position']))
            else:
                ecm.append(calculate_ecm(analytical_coords['position'], method_coords['position'], 5 / time_steps[i]))

            i += 1

        print('Length ecm: ', len(ecm))
        print('Length time: ', len(time_steps))
        plt.scatter(time_steps, ecm, label=method)

    plt.xlabel('Tiempo (s)', fontsize=16)
    plt.ylabel('ECM', fontsize=16)
    plt.grid(False)
    plt.legend(bbox_to_anchor=(0.5, 1.1), loc='upper center', borderaxespad=0, fontsize=12, ncol=4)
    plt.xscale("log")
    plt.yscale("log")
    plt.show()


def calculate_ecm_verlet(analytical_method, aprox_method):
    square_diff = []
    for analytical, method in zip(analytical_method, aprox_method):
        square_diff.append((float(analytical) - float(method)) ** 2)
    return np.mean(square_diff)

def calculate_ecm(analytical_method, aprox_method, iterations):
    square_diff = []
    for i in range(1,int(iterations)):
        square_diff.append((float(analytical_method[i]) - float(aprox_method[i-1])) ** 2)
    return np.mean(square_diff)


def main():
    plots()
    cuadratic_error()



if __name__ == '__main__':
    main()
