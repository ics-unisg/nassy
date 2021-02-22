#!/usr/bin/python3
 
import pandas as pd
import sys

df = pd.read_csv(sys.stdin, sep=";")

df['ET_PubilAvg'] = df.apply(lambda x: (x['ET_PupilLeft'] + x['ET_PupilRight']) / 2, axis=1)


baseline = df[df['type'] == 'B']
baseline['ET_PubilAvg'].agg([pd.np.min, pd.np.max, pd.np.mean, pd.np.median, pd.np.std])


experiment = df[df['type'] == 'M']
raise Exception(
experiment['ET_PubilAvg'].agg([pd.np.min, pd.np.max, pd.np.mean, pd.np.median, pd.np.std])
)

df.to_csv(sys.stdout)