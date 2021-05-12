import requests
import json
import numpy as np
from time import time, sleep

settings = {
    "url": 'http://localhost:1112'
}

requests.post(
    url='http://localhost:1114' + "/reset"
)
record_type = 'BASELINE'

while True:
    for t in np.arange(0, 100, 0.1):
        sleep(0.01)
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
    record_type = 'EXPERIMENT'