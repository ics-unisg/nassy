import requests
from datetime import datetime
import socket
import sys
import json

from requests_futures.sessions import FuturesSession

session = FuturesSession()

settings = {
    "url": 'http://localhost:1112' # nifi server address
}


SUBJECT = None
STUDY = None
STATE = None


HOST = '130.82.27.9'  # Standard loopback interface address (localhost)
PORT = 9999        # Port to listen on (non-privileged ports are > 1023)
with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
    s.connect((HOST, PORT))
    print("connected")
    while True:
        line = s.recv(10240)
        [index, source, *params] = line.decode('utf-8').replace('\n\r', '').split(';')
        #print(".", end="")
        print(source)
        if source == 'EyeTracker':
            if SUBJECT and STATE:
                [_, timestamp, media_time, et_timestamp, glx, gly, grx, gry, lpd, rpd, led, red, lepx, lepy, repx, repy, *rest] = params
                #print(index, source,  timestamp, media_time, glx, gly, grx, gry, lpd, rpd, led, red, lepx, lepy, repx, repy)


                session.post(
                    url=settings.get('url') + "/data", 
                    data=json.dumps({
                        'timestamp': int(datetime.now().timestamp()),
                        'type': STATE,
                        'subject': SUBJECT,
                        'study': STUDY,
                        'diameter': {
                            'left': float(lpd) if float(lpd) > 0 else 0,
                            'right':  float(rpd) if float(rpd) > 0 else 0,
                        }
                    })
                )
        
        elif source == 'Affectiva AFFDEX/Affectiva AFFDEX':
            pass
        elif source == 'AttentionTool':
            print(params)
            if params[0] == 'SlideStart':
                start = params[4].split('-')[0]
                print(start)
                if start == 'baseline':
                    STATE = 'BASELINE'
                elif start == 'experiment':
                    STATE = 'EXPERIMENT'
                else:
                    STATE = None
                print(STATE)
            elif params[0] == 'SlideshowEnd':
                STUDY = None
                SUBJECT = None
                STATE = None
                print('END')
            elif params[0] == 'SlideshowStart':
                print('START')
            elif params[0] == 'GazeCalibrationStart':
                SUBJECT = params[4]
                STUDY = params[7]
                print(SUBJECT, STUDY)
            else:
                pass

        else:
            pass
            #print(source, params)
