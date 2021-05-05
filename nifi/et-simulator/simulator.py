import requests
import json
import numpy as np

settings = {
    "url": 'http://localhost:1112'
}

while True:
    for t in np.arange(0, 100, 0.1):
        requests.post(
            url=settings.get('url') + "/data", 
            data=json.dumps({
                'diameter': {
                    'left': (np.sin(t)+1)*5,
                    'right': (np.sin(t+1)+1)*5
                }
            })
        )