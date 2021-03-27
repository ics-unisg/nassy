#!/usr/bin/python3
 
import pandas as pd
import sys
import os

header_list = ["idx","id","EventSource","SampleName","1","2","ET_TimeSignal","ET_GazeLeftx","ET_GazeLefty","ET_GazeRightx","ET_GazeRighty","ET_PupilLeft","ET_PupilRight","DistanceLeft","DistanceRight","XLeft","YLeft","XRight","XLeft.1","Timestamp","type","subject","task"]
try:
    baseline = pd.read_csv('/code/storage/baseline.csv', names=header_list)
    experiment = pd.read_csv('/code/storage/experiment.csv', names=header_list)

    if len(experiment) > 100:
        os.remove("/code/storage/experiment.csv")

        df = baseline.append(experiment)
        print(df.sort_values(by=['Timestamp']).to_csv(index=False))
except FileNotFoundError:
    pass