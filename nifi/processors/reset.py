#!/usr/bin/python3

import json
import sys

import sqlite3

db = sqlite3.connect('/ingres.db')
cursor = db.cursor()
cursor.execute('DROP TABLE IF EXISTS et')
db.commit()
cursor.close()
db.close()

print(json.dumps({ "reset": True }))