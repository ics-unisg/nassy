import pandas as pd
from datetime import datetime
from time import sleep
import requests
import json

settings = {
    "url": 'http://localhost:1112'
}
print('RESET')
response = requests.post(
    url='http://localhost:1114' + "/reset"
)


df = pd.read_csv('./008_t2.csv', low_memory=False)

start = datetime.fromtimestamp(df.iloc[0]['Timestamp']/1000.0)
now = datetime.now()
diff = (now - start)

for index, row in df.iterrows():
    while True:
        if datetime.fromtimestamp(row['Timestamp']/1000.0) - start <= datetime.now() - now:
            break

    type = 'BASELINE' if row['type'] == 'B' else 'EXPERIMENT'
    
    requests.post(
        url=settings.get('url') + "/data", 
        data=json.dumps({
            'timestamp': int(row['Timestamp']),
            'type': type,
            'subject': 'subject01',
            'study': 'simulator',
            'diameter': {
                'left': row['ET_PupilLeft'] if row['ET_PupilLeft'] > 0 else 0,
                'right':  row['ET_PupilRight'] if row['ET_PupilRight'] > 0 else 0,
            }
        })
    )

print('RESET')
response = requests.post(
    url='http://localhost:1114' + "/reset"
)