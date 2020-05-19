import json
import copy
import requests as rq

from help.error import RequirementError
from help import setting


def changeStudy(authorisation, stud_id, new_name, log):
    with rq.Session() as s:
        s.post("http://"+setting.httpPrefix+"/cheetah/login", data=authorisation)
        subjectUrl = str("http://"+setting.httpPrefix+"/cheetah/api/user/study/"+str(stud_id))
        r = s.get(subjectUrl)
        decode = r.content.decode('UTF-8')
        load=json.loads(decode)
        log.interlog(__file__, "renameStudy", "Study befor change "  + str(load["resBody"]),
                     "Check")
        study1=load["resBody"]
        study2=copy.deepcopy(study1)
        study2["comment"]="CHANGED"
        study2["name"]=new_name
        headers = {'content-type': 'application/json'}
        r = s.put("http://"+setting.httpPrefix+"/cheetah/api/user/study", headers=headers, json=study2)



        loads = s.get(subjectUrl)
        lo = loads.content.decode('UTF-8')
        load = json.loads(lo)
        if (load["statusCode"] == 0):
            log.interlog(__file__, "change Subject", str("Subject successful changed"), "Check")
        else:
            log.interlog(__file__, "change Subject", str("Subject NOT successful changed"), "Check")
            log.close()
            raise RequirementError("Could not rename Study")

def checkStudyName(authorisation, stud_id, stud_name, log):
    with rq.Session() as s:
        s.post("http://"+setting.httpPrefix+"/cheetah/login", data=authorisation)
        studyUrl = str("http://"+setting.httpPrefix+"/cheetah/api/user/study/"+str(stud_id))
        r = s.get(studyUrl)
        decode = r.content.decode('UTF-8')
        load=json.loads(decode)
        name=load["resBody"]["name"]
        if stud_name == name:
            log.interlog(__file__, "checkName", "Study has name "+stud_name, "Check")
        else:
            log.interlog(__file__, "checkName", "Study has NOT name "+stud_name, "Check")
            log.close()
            raise RequirementError("Study hast not expected name.")


def tryToGetAlienStudy(authorisation, stud_id, log):
    with rq.Session() as s:
        s.post("http://"+setting.httpPrefix+"/cheetah/login", data=authorisation)
        studyUrl = str("http://"+setting.httpPrefix+"/cheetah/api/user/study/"+str(stud_id))
        r = s.get(studyUrl)
        decode = r.content.decode('UTF-8')
        load=json.loads(decode)
        code=load["statusCode"]
        if code == 1403:
            log.logHTTPResponse(__file__, "tryToGetAlienStudy", "Perfect. Could not get other studies")
        else:
            log.logHTTPResponse(__file__, "tryToGetAlienStudy", "Oh no. Could get other studies")
            log.close()
            raise RequirementError("Could get a study that does not belong to me.")
