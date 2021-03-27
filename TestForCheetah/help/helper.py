import time

import requests as rq
import json
from help import setting

from help.error import RequirementError
from net.filterScripts import url, checkIfFinished


def cleanDatabase(authorisation, studyname):
    with rq.Session() as s:
        s.post("http://"+setting.httpPrefix+"/cheetah/login", data=authorisation)
        url="http://"+setting.httpPrefix+"/cheetah/api/user/studies"
        r=s.get(url=url)
        decode = r.content.decode('UTF-8')

        loads = json.loads(decode)

        for e in loads["resBody"]:
            if e["name"] == studyname:
                idToDel = "http://"+setting.httpPrefix+"/cheetah/api/admin/study/" + str(e["id"])
                r1=s.delete(idToDel)
                #print(r1)
                if r1.status_code==200:
                    pass
                    #print("Old study deleted")
                else:
                    raise RequirementError("error while deleting")


def listContaining(list0, list1, message, log):
    for e in list1:
        if not e in list0:
            log.interlog(__file__, "in check subjects", "First list has NOT all elements of second list: " + message ,
                         "Check")
            log.close()
            raise RequirementError("Not in list")
    log.interlog(__file__, "in check subjects", "First list has all elements of second list: " + message,
                 "Check")
    return True


authorisation0 = {
    'username': 'test.user@test.dcap',
    'password': 'Test'
}
#cleanDatabase(authorisation0, "TestStudy")


def waitTilfFinished(authorisation, taskIds, logFile):
    with rq.Session() as s:
        s.post(url=url, data=authorisation)

        index = 0
        idList = taskIds.split(", ")

        while (len(idList) != 0):
            for entry in idList:
                if (checkIfFinished(s, entry)):
                    idList.remove(entry)
            time.sleep(1)
            #print(index)
            index = index + 1
        logFile.interlog(__file__, "tasks finished", "Tasks  "+taskIds + " are finished.",
                     "Check")
        return True