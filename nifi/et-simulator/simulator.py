import requests
import json
import numpy as np
from time import time, sleep

settings = {
    "url": 'http://localhost:1112'
}


print('RESET')
requests.post(
    url='http://localhost:1114' + "/reset"
)

print('FREQ: 100HZ')
print('START BAELINE')
record_type = 'BASELINE'

print('[] static')
for t in np.arange(0, 500, 0.1):
    sleep(0.001)
    requests.post(
        url=settings.get('url') + "/data", 
        data=json.dumps({
            'timestamp': int(time() * 10000),
            'type': record_type,
            'diameter': {
                'left': 5,
                'right': 5,
            }
        })
    )

while True:
    print('START EXPERIMENT')
    record_type = 'EXPERIMENT'

    print('[] slow big sin')
    for t in np.arange(0, 100, 0.01):
        sleep(0.001)
        requests.post(
            url=settings.get('url') + "/data", 
            data=json.dumps({
                'timestamp': int(time() * 10000),
                'type': record_type,
                'diameter': {
                    'left': (np.sin(t)+1) * 1 + 4,
                    'right': (np.sin(t)+1) * 1 + 4,
                }
            })
        )

    print('[] slow biger sin')
    for t in np.arange(0, 100, 0.01):
        sleep(0.001)
        requests.post(
            url=settings.get('url') + "/data", 
            data=json.dumps({
                'timestamp': int(time() * 10000),
                'type': record_type,
                'diameter': {
                    'left': (np.sin(t)+1) * 2.5 + 4,
                    'right': (np.sin(t)+1) * 2.5 + 4,
                }
            })
        )

    print('[] fast sin')
    for t in np.arange(0, 1000, 0.5):
        sleep(0.001)
        requests.post(
            url=settings.get('url') + "/data", 
            data=json.dumps({
                'timestamp': int(time() * 10000),
                'type': record_type,
                'diameter': {
                    'left': (np.sin(t)+1)*5,
                    'right': (np.sin(t+1)+1)*5
                }
            })
        )

    print('[] slow sin')
    for t in np.arange(0, 100, 0.01):
        sleep(0.001)
        requests.post(
            url=settings.get('url') + "/data", 
            data=json.dumps({
                'timestamp': int(time() * 10000),
                'type': record_type,
                'diameter': {
                    'left': (np.sin(t)+1)*0.1 + 5,
                    'right': (np.sin(t)+1)*0.1 + 5,
                }
            })
        )