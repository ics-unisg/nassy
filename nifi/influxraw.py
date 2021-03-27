#!/usr/bin/python3
 
import pandas as pd
import sys
import torch
import numpy as np


df = pd.read_csv(sys.stdin)

for index, row in df.iterrows():
    print(f'et,subject={row["subject"]} pupil_left={row["ET_PupilLeft"]} {int(row["Timestamp"]*1000000)}')
    print(f'et,subject={row["subject"]} pupil_right={row["ET_PupilRight"]} {int(row["Timestamp"]*1000000)}')
    print(f'et,subject={row["subject"]} gaze_righty={row["ET_GazeRighty"]} {int(row["Timestamp"]*1000000)}')
    print(f'et,subject={row["subject"]} gaze_rightx={row["ET_GazeRightx"]} {int(row["Timestamp"]*1000000)}')
    print(f'et,subject={row["subject"]} gaze_lefty={row["ET_GazeLefty"]} {int(row["Timestamp"]*1000000)}')
    print(f'et,subject={row["subject"]} gaze_leftx={row["ET_GazeLeftx"]} {int(row["Timestamp"]*1000000)}')
