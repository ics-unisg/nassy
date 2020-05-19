import json
from help import setting


import requests as rq

def getUserDataFileId(authorisation, taskIds, logFile):
    with rq.Session() as s:
        s.post(url="http://"+setting.httpPrefix+"/cheetah/login", data=authorisation)
        idList = taskIds.split(", ")

        fileIds=[]
        for entry in idList:
            dataUrl = str("http://"+setting.httpPrefix+"/cheetah/api/user/taskid/" + str(entry))
            r=s.get(dataUrl)

            decode = r.content.decode('UTF-8')
            load = json.loads(decode)
            logFile.logHTTPResponse(__file__, "getTaskData", load)
            fileIds.append(load["resBody"]["id"])
        return fileIds

def getUserDataFileIdAndParent(authorisation, taskIds, logFile):
    with rq.Session() as s:
        s.post(url="http://"+setting.httpPrefix+"/cheetah/login", data=authorisation)
        idList = taskIds.split(", ")

        fileIds=[]
        for entry in idList:
            dataUrl = str("http://"+setting.httpPrefix+"/cheetah/api/user/taskid/" + str(entry))
            r=s.get(dataUrl)

            decode = r.content.decode('UTF-8')
            load = json.loads(decode)
            logFile.logHTTPResponse(__file__, "getTaskData", load)
            fileIds.append([load["resBody"]["id"], load["resBody"]["derived"]])
        return fileIds


def getUserDataOfGivenFile(authorisation, taskIds, logFile, fileName):
    with rq.Session() as s:
        s.post(url="http://"+setting.httpPrefix+"/cheetah/login", data=authorisation)
        idList = taskIds.split(", ")
        filNames= fileName.split("; ")
        fileIds=[]
        for entry in idList:
            dataUrl = str("http://"+setting.httpPrefix+"/cheetah/api/user/taskid/" + str(entry))
            r=s.get(dataUrl)

            decode = r.content.decode('UTF-8')
            load = json.loads(decode)
            logFile.logHTTPResponse(__file__, "getTaskData", load)
            for name in filNames:
                if name in load["resBody"]["name"]:
                    fileIds.append(load["resBody"]["id"])
        return fileIds

def getUserData(authorisation, taskId, logFile):
    with rq.Session() as s:
        s.post(url="http://"+setting.httpPrefix+"/cheetah/login", data=authorisation)
        dataUrl = str("http://"+setting.httpPrefix+"/cheetah/api/user/taskid/" + str(taskId))
        r = s.get(dataUrl)

        decode = r.content.decode('UTF-8')
        load = json.loads(decode)
        logFile.logHTTPResponse(__file__, "getTaskData", load)
        return load["resBody"]["id"]

def  getDataForColumn(authorisation, id, timestamp, column, log):
    with rq.Session() as s:
        s.post(url="http://"+setting.httpPrefix+"/cheetah/login", data=authorisation)
        dataUrl = str("http://"+setting.httpPrefix+"/cheetah/api/user/data/" + str(id)+"/"+timestamp+"/"+column)
        r = s.get(dataUrl)

        decode = r.content.decode('UTF-8')
        load = json.loads(decode)
        log.logHTTPResponse(__file__, "getData", load)
        result = load["resBody"]
        new_results = list(map(lambda x:x["value"], result))
        return new_results

def  tryToGetDataForColumn(authorisation, id, timestamp, column, log):
    with rq.Session() as s:
        s.post(url="http://"+setting.httpPrefix+"/cheetah/login", data=authorisation)
        dataUrl = str("http://"+setting.httpPrefix+"/cheetah/api/user/data/" + str(id)+"/"+timestamp+"/"+column)
        r = s.get(dataUrl)

        decode = r.content.decode('UTF-8')
        load = json.loads(decode)
        if load["statusCode"]!=0:
            log.logHTTPResponse(__file__, "Unauthorized user cannot access data", load)
        else:
            log.logHTTPResponse(__file__, "Unauthorized user CAN access data", load)
            log.close()
            raise RecursionError("Unauthorized user CAN access data")



def  getDataForColumnAndTimeslot(authorisation, id, timestamp, start, end, column, log):
    with rq.Session() as s:
        s.post(url="http://"+setting.httpPrefix+"/cheetah/login", data=authorisation)
        dataUrl = str("http://"+setting.httpPrefix+"/cheetah/api/user/data/" + str(id)+"/"+timestamp+"/"+column+"/"+str(start)+"/"+str(end))
        r = s.get(dataUrl)

        decode = r.content.decode('UTF-8')
        load = json.loads(decode)
        log.logHTTPResponse(__file__, "getData", load)
        result = load["resBody"]
        starting=result[0]["timestamp"]
        ending = result[len(result)-1]["timestamp"]
        new_results = list(map(lambda x:x["value"], result))
        return new_results, starting, ending

