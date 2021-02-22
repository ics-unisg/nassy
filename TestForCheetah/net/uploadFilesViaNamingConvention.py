import requests as rq
import os
import  json
from help import setting

authorisation = {
    'username': 'test.user@test.dcap',
    'password': 'Test'
}

loginUrl ="http://"+setting.httpPrefix+"/cheetah/login"


path = "does not exist"
    
file1="does not existp001b@TestStudy@p001b.tsv"
file2="does not existp024a@TestStudy@p024a.tsv"

#files = [('files', (os.path.basename(file1), open(file1, 'rb'), 'application/octet-stream')), ('files',(os.path.basename(file2), open(file2, 'rb'), 'application/octet-stream'))]



def uploadFilesNaming(authorisation, files, url, logFile):
    with rq.Session() as s:
        job_data = {'subjectIds': ""}
        s.post(url=loginUrl, data=authorisation)
        r = s.post(url, data= job_data,files=files)
        response = r.content.decode('UTF-8')

        responseAsJSON = json.loads(response)
        mapped = responseAsJSON["resBody"]["easyUserDataListSuccessfultMapped"]
        returnlistFull=[]
        returnlistIds=[]
        for e in mapped:
            returnlistFull.append([e["id"], e["name"]])
            returnlistIds.append(e["id"])
            logStat=str(e["id"]) + "-"+e["name"]
            logFile.logHTTPResponse(__file__, "uploadFilesNaming", logStat)

        return returnlistIds, returnlistFull


def uploadFilesWithoutNaming(authorisation, files, subjects, url, logFile):
    with rq.Session() as s:
        job_data = {'subjectIds': [subjects]}
        s.post(url=loginUrl, data=authorisation)
        r = s.post(url, data=job_data, files=files)
        response = r.content.decode('UTF-8')

        responseAsJSON = json.loads(response)

        mapped = responseAsJSON["resBody"]["easyUserDataListSuccessfultMapped"]
        returnlistFull = []
        returnlistIds = []
        for e in mapped:
            returnlistFull.append([e["id"], e["name"]])
            returnlistIds.append(e["id"])
            logStat = str(e["id"]) + "-" + e["name"]
            logFile.logHTTPResponse(__file__, "uploadFilesNaming", logStat)

        return returnlistIds, returnlistFull