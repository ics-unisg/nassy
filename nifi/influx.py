#!/usr/bin/python3
 
import pandas as pd
import sys
import torch
import numpy as np


df = pd.read_csv(sys.stdin)

for index, row in df.iterrows():
    print(f'cl,subject={row["subject"]} prediction={row["prediction_class"]} {int(row["e_start"]*10000000)}')
