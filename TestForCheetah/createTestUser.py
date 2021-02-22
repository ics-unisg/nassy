from help import setting
import requests as rq
import json


authorisation = {
    'username': 'admin@administrator.dcap',
    'password': 'VIVCsiL'
}


newUser ="{\"id\":0, \"lastname\":\"User\", \"firstname\":\"Test\", \"email\": \"test.user@test.dcap\", \"password\": \"Test\", \"role\":\"administrator\"}"
headers = {'content-type': 'application/json'}
with rq.Session() as s:
    s.post("http://"+setting.httpPrefix+"/cheetah/login", data=authorisation)
    r = s.post("http://"+setting.httpPrefix+"/cheetah/api/admin/user", headers=headers, data=newUser)
    decode = r.content.decode('UTF-8')
    message  = json.loads(decode)
    #print(message)

