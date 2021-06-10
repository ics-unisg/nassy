#!/usr/bin/python3

"""Resets the datastore

Deletes all sqlite files. This will not reset nifi but reset lhipa and cl model states
"""

__author__ = "Martin Eigenmann"
__license__ = "unlicence"
__version__ = "0.0.1"
__email__ = "martin.eigenmann@unisg.ch"
__status__ = "Prototpye"

import json
import sys
import os
import sqlite3

for f in os.listdir('/data'):
    if len(f.split('-')) == 1:
        db = sqlite3.connect(f'/data/{f}')
        cursor = db.cursor()
        cursor.execute('DROP TABLE IF EXISTS et')
        db.commit()
        cursor.close()
        db.close()

        os.remove(f'/data/{f}')

        print(json.dumps({ "reset": True }))