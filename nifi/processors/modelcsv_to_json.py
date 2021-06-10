#!/usr/bin/python3

"""Converts csv to json

This is used for the cl pipeline
"""

__author__ = "Martin Eigenmann"
__license__ = "unlicence"
__version__ = "0.0.1"
__email__ = "martin.eigenmann@unisg.ch"
__status__ = "Prototpye"

import pandas as pd
import sys

df = pd.read_csv(sys.stdin).drop(['b_start'], axis=1).rename(columns={'prediction_class': 'prediction', 'e_start': 'timestamp'})
print(df.to_json(orient="records"))
