#!/usr/bin/python3
 
import pandas as pd
import sys


df = pd.read_csv(sys.stdin)
baseline = df[df['type'] == 'B']
experiment = df[df['type'] == 'M']

with open('/code/storage/baseline.csv', 'a') as f:
    baseline.to_csv(f, mode='a', header=False)

with open('/code/storage/experiment.csv', 'a') as f:
    experiment.to_csv(f, mode='a', header=False)
