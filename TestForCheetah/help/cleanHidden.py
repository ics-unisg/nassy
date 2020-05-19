import json
import requests as rq
from help import setting



authorisation = {
    'username': 'test.user@test.dcap',
    'password': 'Test'
}

with rq.Session() as s:
    s.post("http://"+setting.httpPrefix+"/cheetah/login", data=authorisation)
    response = s.get("http://"+setting.httpPrefix+"/cheetah/api/admin/clean/yes")
    decode = response.content.decode('UTF-8')
    loads = json.loads(decode)
    print(loads)