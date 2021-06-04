import requests
import json
import numpy as np
from time import time, sleep

settings = {
    "url": 'http://localhost:1112'
}


print('RESET')
response = requests.post(
    url='http://localhost:1114' + "/reset"
)
print(response)