#!/usr/bin/python3

"""Reads the sqlite data for cl model

For every user it will read the past two minute of 
eye tracker data form sqlite and pass it forward to the cl model.
It will also read the baseline data.

Data has to be send the following way:
Baseline - Experiment

To the model all baseline data will be sent and the last 2 min of experiment data but only if there is any experiment data.
"""

__author__ = "Martin Eigenmann"
__license__ = "unlicence"
__version__ = "0.0.1"
__email__ = "martin.eigenmann@unisg.ch"
__status__ = "Prototpye"

import sqlite3
import os


first = True
for f in os.listdir('/data'):
    if len(f.split('-')) == 1:
        db = sqlite3.connect(f'/data/{f}')
        cursor = db.cursor()

        cursor.execute("SELECT count(name) FROM sqlite_master WHERE type='table' AND name='et'")

        if cursor.fetchone()[0]==1:
            cursor.execute("SELECT timestamp from et where type = 'EXPERIMENT' ORDER BY timestamp DESC LIMIT 1")
            result = cursor.fetchone()
            if result:
                experiment_upper_bound = result[0]
                experiment_lower_bound = experiment_upper_bound - 2 * 60 * 1000 # The past 120000 milliseconds

                cursor.execute("SELECT timestamp from et where type = 'BASELINE' ORDER BY timestamp DESC LIMIT 1")
                baseline_upper_bound = cursor.fetchone()[0]

                if baseline_upper_bound > experiment_upper_bound:
                    experiment_upper_bound = baseline_upper_bound
                    experiment_lower_bound = baseline_upper_bound

                cursor.execute("SELECT timestamp, left, right, type, study, subject from et where timestamp > ? AND timestamp < ? AND type = 'EXPERIMENT' AND left > 0 and right > 0 ORDER BY timestamp asc", (experiment_lower_bound, experiment_upper_bound))
                experiment = [entry for entry in cursor.fetchall()]

                cursor.execute("SELECT timestamp, left, right, type, study, subject from et where type = 'BASELINE' ORDER BY timestamp asc")
                baseline = [entry for entry in cursor.fetchall()]

                if len(experiment) > 0 and len(baseline) > 0:
                    if first:
                        first = False
                    else:
                        print("---split---")
                    print('timestamp,type,diameter_left,diameter_right,study,subject')
                    for (timestamp, left, right, record_type, study, subject) in baseline + experiment:
                        print(f'{timestamp},{record_type},{left},{right},{study},{subject}')

                db.close()