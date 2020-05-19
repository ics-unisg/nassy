import json

import requests as rq
from help import setting


from help.error import RequirementError


def checkListOfUserData(authorisation,name, le, log ):
    url = "http://"+setting.httpPrefix+"/cheetah/login"
    with rq.Session() as s:
        s.post(url=url, data=authorisation)
        r=s.get("http://"+setting.httpPrefix+"/cheetah/api/user/userdataall")
        decode = r.content.decode('UTF-8')
        loads = json.loads(decode)
        response = loads["resBody"]
        if len(response)!=le:
            log.interlog(__file__, "checkListOfUserData", "not same length", "Check")
            log.close()
            raise RequirementError("no correct list of userdata")
        returnArray=[]
        for e in response:
            returnArray.append(e["id"])
            if(name!=""):
                namelist=name.split(",")
                if not e["name"].split(".")[0] in namelist:
                    log.interlog(__file__, "checkListOfUserData", "not correct list of userdata", "Check")#
                    log.close
                    raise RequirementError("no correct list of userdata")
        log.interlog(__file__, "checkListOfUserData", "all userdata there ", "Check")
        return returnArray


def deleteFiles(authorisation,list, log ):
    url = "http://"+setting.httpPrefix+"/cheetah/login"
    with rq.Session() as s:
        s.post(url=url, data=authorisation)
        for e in list:
            r=s.delete("http://"+setting.httpPrefix+"/cheetah/api/user/file/"+str(e))
            decode = r.content.decode('UTF-8')
            loads = json.loads(decode)
            response = loads["resBody"]
            if response!="True":
                log.interlog(__file__, "deleteFile", "Not deleted", "Check")
                log.close
                raise RequirementError("could not delete")
        log.interlog(__file__, "deleteFile", "Deletion done", "Check")
