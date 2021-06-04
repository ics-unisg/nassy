#!/usr/bin/python3

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