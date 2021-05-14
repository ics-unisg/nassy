#!/usr/bin/python3

import sqlite3

db = sqlite3.connect('/ingres.db')
cursor = db.cursor()

cursor.execute("SELECT count(name) FROM sqlite_master WHERE type='table' AND name='et'")

if cursor.fetchone()[0]==1:
    cursor.execute("SELECT timestamp from et ORDER BY timestamp DESC LIMIT 1")

    timestamp = cursor.fetchone()[0]
    lower_bound = timestamp - 1 * 60 * 1000

    cursor.execute("SELECT timestamp, left, right from et where timestamp >= ? AND timestamp <= ? ORDER BY timestamp asc", (lower_bound, timestamp))
    data = [entry for entry in cursor.fetchall()]


    if len(data) > 2:
        print('timestamp,diameter_left,diameter_right')
        for (timestamp, left, right) in data:
            print(f'{timestamp},{left},{right}')
        
    db.close()