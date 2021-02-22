import requests as rq
import json
from help import setting



def calculateMeasures(authorisation, baseline, columNames, fileIDs, labelColumn, labels, timeStampColumnNAme, logFile):
    dataString = "{\"baseline\":\"%s\",\"columnNames\":%s,\"fileIds\":%s,\"labelColumn\":\"%s\",\"labels\":%s,\"timeStampColumnName\":\"%s\"};" % (
        baseline, columNames, fileIDs, labelColumn, labels, timeStampColumnNAme);
    #print(dataString)
    url = "http://"+setting.httpPrefix+"/cheetah/login"
    with rq.Session() as s:
        s.post(url=url, data=authorisation)
        r = s.post("http://"+setting.httpPrefix+"/cheetah/api/user/calculatemeasures", data=dataString, headers={'content-type': 'application/json'})
        decode = r.content.decode('UTF-8')
        loads = json.loads(decode)
        logFile.logHTTPResponse(__file__, "Measure the files " + str(fileIDs), str(loads)   )
        taskIds = loads['resBody']
        #print(taskIds)
        return taskIds



#
# baseline0 = ""
# columNames0 = "[\"PupilLeft\",\"PupilRight\", \"PupilSomething\"]"
# fileIDs0 = "[21113]"
# labelColumn0 = "label"
# labels0 = "[]"
# timeStampColumnNAme0 = "EyeTrackerTimestamp"
# authorisation0 = {
#     'username': 'test.user@test.dcap',
#     'password': 'Test'
#}

#log=Logger()

#calculateMeasures(authorisation0, baseline0, columNames0, fileIDs0, labelColumn0, labels0, timeStampColumnNAme0, log)
