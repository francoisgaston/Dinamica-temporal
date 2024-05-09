import pandas as pd
import matplotlib.pyplot as plt

data = pd.read_csv('../Simulation/Output/c_vel_time/PlanetOutput.csv')
velocity = ((data['svX']**2 + data['svY']**2)**0.5)

plt.figure(figsize=(10, 6))
plt.plot(data['timeFrame'], velocity)
plt.xlabel("Tiempo (s)", fontsize=16)
plt.ylabel("Velocidad (m/s)", fontsize=16)
plt.ticklabel_format(axis="x", style="sci", useMathText=True)
plt.grid(False)
plt.show()
