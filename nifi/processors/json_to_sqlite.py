#!/usr/bin/python3

import json
import sys

import sqlite3

db = sqlite3.connect('/ingres.db')
cursor = db.cursor()
cursor.execute('CREATE TABLE IF NOT EXISTS et (id INTEGER PRIMARY KEY, ingres_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP, timestamp INTEGER, left FLOAT, right FLOAT, type STRING)')

for line in sys.stdin:
    data = json.loads(line)
    
    cursor.execute('INSERT INTO et (timestamp, left, right, type) VALUES (?,?,?,?)', (data['timestamp'], data['diameter']['left'], data['diameter']['right'], data['type']))

db.commit()
cursor.close()
db.close()