import pandas as pd
import matplotlib.pyplot as plt

data = pd.read_csv('../Simulation/Output/PlanetOutput.csv')
velocity = ((data['svX']**2 + data['svY']**2)**0.5)

plt.figure(figsize=(10, 6))
plt.plot(data['timeFrame'], velocity)
plt.xlabel("Tiempo (s)", fontsize=16)
plt.ylabel("velocidad (m/s)", fontsize=16)
plt.grid(False)
plt.show()
