#!/usr/bin/python3

import pandas as pd
import sys

print(pd.read_csv(sys.stdin).to_json(orient="records"))
