from help import setting
import requests as rq
import json


authorisation = {
    'username': 'admin@administrator.dcap',
    'password': 'vjxNIUb'
}


pw ="{\"pwd\":\"Test\"}"
headers = {'content-type': 'application/json'}
with rq.Session() as s:
    s.post("http://"+setting.httpPrefix+"/cheetah/login", data=authorisation)
    r = s.post("http://"+setting.httpPrefix+"/cheetah/api/user/user", headers=headers, data=pw)
    decode = r.content.decode('UTF-8')
    message  = json.loads(decode)
    #print(message)
