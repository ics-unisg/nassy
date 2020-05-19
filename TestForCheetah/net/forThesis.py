import requests as rq
import json

authorisation = {
    'username': 'test.user@test.dcap',
    'password': 'Test'
}

url ="http://localhost:8989/cheetah/login"

jsonString= "{  " \
"\"files\":[21175],  \"filters\":  [{  \"name\":  \"SubstitutePupilFilter\",  \"actualParameters\":  " \
    "{  \"left_pupil\":  \"PupilLeft\",  \"right_pupil\":  \"PupilRight\",\"timestampcolumn\": \"EyeTrackerTimestamp\"},  " \
    "\"columns\":  {\"left_pupil\":  \"PupilLeft\",  \"right_pupil\":  \"PupilRight\"  }  }  ],  \"decimalSeparator\":  \".\"  }"

taskIds=[]

with rq.Session() as s:
    s.post(url=url, data=authorisation)
    r = s.post("http://localhost:8989/cheetah/api/user/filterrequest", data=jsonString,
               headers={'content-type': "application/json"})
    decode = r.content.decode('UTF-8')
    loads = json.loads(decode)
    taskIds = loads['resBody']

print(taskIds)
