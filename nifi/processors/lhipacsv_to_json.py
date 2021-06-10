#!/usr/bin/python3

"""Converts csv to json

This is used for the lhipa pipeline
"""

__author__ = "Martin Eigenmann"
__license__ = "unlicence"
__version__ = "0.0.1"
__email__ = "martin.eigenmann@unisg.ch"
__status__ = "Prototpye"

import pandas as pd
import sys

print(pd.read_csv(sys.stdin).to_json(orient="records"))
