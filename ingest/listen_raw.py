#!/usr/bin/env python3

import socket
import pandas as pd
import signal
import sys
from time import sleep, time
import curses
from datetime import datetime, timedelta
import requests


HOST = '0.0.0.0' 
PORT = 9999        # Port to listen on (non-privileged ports are > 1023)

stdscr = curses.initscr()
curses.noecho()
stdscr.nodelay(1) # set getch() non-blocking


STAGE = 'B'
TIME = datetime.now()
UPLOAD = TIME + timedelta(seconds=90)
RESULT = None
COUNTER = 0


def signal_handler(sig, frame):
    sys.exit(0)

signal.signal(signal.SIGINT, signal_handler)

def upload(df):
    df = df.rename(columns={
        "ETTimestamp": "ET_TimeSignal",
        "PupilLeft": "ET_PupilLeft",
        "PupilRight": "ET_PupilRight",
        "GazeLeftX": "ET_GazeLeftx",
        "GazelLeftY": "ET_GazeLefty",
        "GazeRightX": "ET_GazeRightx",
        "GazeRightY": "ET_GazeRighty",

    })
    df['subject'] = 'experimenter'
    df['task'] = 'experiment'
    try:
        result = requests.post('http://localhost:8888/data', data=df.to_csv(index=False), headers={'Content-Type': 'text/plain; charset=utf-8'})
    except:
        pass

stdscr.addstr(0,0,"Press \"b\" to end baseline")
try:
    with socket.socket(socket.AF_INET, socket.SOCK_DGRAM) as s:
        s.bind((HOST, PORT))
        while True:
            delta = datetime.now() - TIME
            stdscr.addstr(3,0,f'Tital duration: {delta}')
    

            c = stdscr.getch()
            if c == ord('b') and STAGE == 'B':
                stdscr.addstr(0,0,"Experiment started                  ")
                STAGE = 'M'


            data, addr = s.recvfrom(1024) # buffer size is 1024 bytes
            lines = data.decode('utf-8').split('\r\n')
            for line in lines:
                if len(line) > 0:
                    arr = line.split(';')
                    if arr[1] == 'EyeTracker':
                        stdscr.addstr(2,0,f'Events: {COUNTER}')
                        COUNTER += 1
                        d = pd.DataFrame([arr], columns=['id', 'EventSource', 'SampleName', '1', '2', 'ETTimestamp', 'GazeLeftX', 'GazelLeftY', 'GazeRightX', 'GazeRightY', 'PupilLeft', 'PupilRight', 'DistanceLeft', 'DistanceRight', 'XLeft', 'YLeft', 'XRight', 'XLeft'])
                        d['Timestamp'] = int(datetime.now().timestamp() * 1000)
                        stdscr.addstr(20,0,f'{str(d)}')
                        d['type'] = STAGE

                        upload(d)

            sleep(0.01)


finally:
    curses.endwin()