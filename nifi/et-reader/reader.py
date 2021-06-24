import requests
from datetime import datetime
import socket
import sys
import json


from concurrent.futures import ThreadPoolExecutor
from requests_futures.sessions import FuturesSession

session = FuturesSession(executor=ThreadPoolExecutor(max_workers=20))


def main():
    settings = {
        "url": 'http://localhost:1112'
    }


    SUBJECT = None
    STUDY = None
    STATE = None
    CHUNK_SIZE = 10240


    HOST = '192.168.1.2'  # Standard loopback interface address (localhost)
    PORT = 9999        # Port to listen on (non-privileged ports are > 1023)
    buffer = bytearray()

    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
        s.connect((HOST, PORT))
        print("connected")
        while True:
            while True:
                chunk = s.recv(CHUNK_SIZE)
                buffer.extend(chunk)
                if b'\n' in chunk:
                    break
            
            while b'\n' in buffer:
                index = buffer.find(b'\n')
                line = buffer[:index]

                buffer = buffer[index + 1:]

                [index, source, *params] = line.decode('utf-8').replace('\n\r', '').split(';')
                if source == 'EyeTracker':
                    if SUBJECT and STATE:
                        [_, timestamp, media_time, et_timestamp, glx, gly, grx, gry, lpd, rpd, led, red, lepx, lepy, repx, repy, *rest] = params
                        #print(index, source,  timestamp, media_time, glx, gly, grx, gry, lpd, rpd, led, red, lepx, lepy, repx, repy)

                        left = float(lpd) if float(lpd) > 0 else 0
                        right = float(rpd) if float(rpd) > 0 else 0
                        session.post(
                            url=settings.get('url') + "/data", 
                            data=json.dumps({
                                'timestamp': int(datetime.now().timestamp()),
                                'type': STATE,
                                'subject': SUBJECT,
                                'study': STUDY,
                                'diameter': {
                                    'left': left,
                                    'right': right
                                }
                            })
                        )
                
                elif source == 'Affectiva AFFDEX/Affectiva AFFDEX':
                    pass
                elif source == 'AttentionTool':
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


if __name__ == '__main__':
    main()
