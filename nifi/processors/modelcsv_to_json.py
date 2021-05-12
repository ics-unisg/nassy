#!/usr/bin/python3

import pandas as pd
import sys

df = pd.read_csv(sys.stdin).drop(['b_start'], axis=1).rename(columns={'prediction_class': 'prediction', 'e_start': 'timestamp'})
print(df.to_json(orient="records"))
