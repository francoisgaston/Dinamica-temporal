import csv
import pandas as pd
import numpy as np
import json
import sys
import cv2


# ---------------------------------------------------
# FILES STUFF
# ---------------------------------------------------
CONFIG_FILE = '../Simulation/Output/Status.json'
OPENCV_OUTPUT_FILENAME = 'visualization'
SIMULATION_OUTPUT = '../Simulation/Output/PlanetOutput.csv'
MP4_FORMAT = 'mp4'
EARTH = 'earth.csv'
MARS = 'mars.csv'
# ---------------------------------------------------
# CONSTANTS
# ---------------------------------------------------
FPS = 5.0
VIDEO_RES = 2000
# SCALE_FACTOR = 10**-8
L = 5 * 10**11
SUN_COLOR = (0, 255, 255)
SUN_RADIUS = 69.6500
EARTH_COLOR = (240, 0, 0)
EARTH_RADIUS = 6.378 * 2
EARTH_X = 7
EARTH_Y = 8
MARS_COLOR = (0, 0, 240)
MARS_RADIUS = 3.3895 * 2
MARS_X = 5
MARS_Y = 6
ROCKET_COLOR = (0,0,0)
ROCKET_SIZE = 5
ROCKET_X = 1
ROCKET_Y = 2
# ---------------------------------------------------


def complete_visualization_opencv(lines):
    fourcc = cv2.VideoWriter_fourcc(*'mp4v')
    # SCALED_L = L * SCALE_FACTOR
    video_writer = cv2.VideoWriter(OPENCV_OUTPUT_FILENAME + '.' + MP4_FORMAT, fourcc, FPS, (VIDEO_RES,VIDEO_RES))

    frame = np.full((int(VIDEO_RES),int(VIDEO_RES), 3), 255, dtype=np.uint8)

    for index, row in lines.iterrows():

        # Sun
        sun_pos = [int(VIDEO_RES/2), int(VIDEO_RES/2)]
        cv2.circle(frame, tuple(sun_pos), int(SUN_RADIUS), SUN_COLOR, -1)

        # Mars
        mars_pos = [int( (row['mpX'] * VIDEO_RES / L) + VIDEO_RES/2), int((row['mpY'] * VIDEO_RES / L) + VIDEO_RES/2)]
        cv2.circle(frame, tuple(mars_pos), int(MARS_RADIUS), MARS_COLOR, -1)

        # Earth
        earth_pos = [int((row['epX']* VIDEO_RES / L) + VIDEO_RES/2), int( (row['epY']* VIDEO_RES / L ) + VIDEO_RES/2 )]
        cv2.circle(frame, tuple(earth_pos), int(EARTH_RADIUS), EARTH_COLOR, -1)

        # Rocket
        rocket_pos = [int((row['spX']* VIDEO_RES / L) + VIDEO_RES/2), int( (row['spY']* VIDEO_RES / L ) + VIDEO_RES/2 )]
        cv2.circle(frame, tuple(rocket_pos), int(ROCKET_SIZE), ROCKET_COLOR, -1)

        video_writer.write(frame)

    video_writer.release()
    cv2.destroyAllWindows()


def read_config_file(file_path):
    with open(file_path, 'r') as file:
        config_data = json.load(file)
    return config_data


def main():
    lines = pd.read_csv(SIMULATION_OUTPUT)

    print('Drawing particles with opencv...')
    complete_visualization_opencv(lines)
    print('Done!')


if __name__ == '__main__':
    main()