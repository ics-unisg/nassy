import json
import copy
import requests as rq

from help.error import RequirementError
from help import setting





def checkAllSubjects(authorisation, list, studyname, log):
    with rq.Session() as s:
        s.post("http://"+setting.httpPrefix+"/cheetah/login", data=authorisation)
        r = s.get("http://"+setting.httpPrefix+"/cheetah/api/user/subjects", data=authorisation)
        decode = r.content.decode('UTF-8')

        #print(decode)

        loads = json.loads(decode)
        testArray = []
        for e in loads["resBody"]:
            if e["studyName"] == studyname:
                testArray.append(e["subject_id"])

        if len(list)!=len(testArray):
            log.interlog(__file__, "in check subjects", "Test failed, because not same number of subjects in database as created",
                         "Check")

            log.close()
            raise RequirementError("Test failed, because not same number of subjects in database as created")

        for item in list:
            if not item in testArray:
                log.interlog(__file__, "in check subjects", "Test failed, because Subject " + str(item) + " is not here", "Check")
                log.close()
                raise RequirementError("Test failed, because Subject " + str(item) + " is not here")
            log.interlog(__file__, "in check subjects", str("Subject "+str(item) + " is here"), "Check")


        return True


def changeSubjectName(authorisation, sub_id, name, log):
    with rq.Session() as s:
        s.post("http://"+setting.httpPrefix+"/cheetah/login", data=authorisation)
        subjectUrl = str("http://"+setting.httpPrefix+"/cheetah/api/user/getsubject/" + str(sub_id))
        r = s.get(subjectUrl)

        decode = r.content.decode('UTF-8')
        load = json.loads(decode)
        log.interlog(__file__, "renameSubject", "Subject befor change " + str(load["resBody"]),
                     "Check")
        subject1 = load["resBody"]
        subject2 = copy.deepcopy(subject1)
        subject2["subject_id"] =  name
        subject2["email"] =  name + "@1234.com"

        headers = {'content-type': 'application/json'}
        #print(subject2)
        r = s.put("http://"+setting.httpPrefix+"/cheetah/api/user/updatesubject", headers=headers, json=subject2)
        log.interlog(__file__, "change Subject", str("Subject changed"), "Check")


def changeSubject(authorisation, sub_id, log):
    with rq.Session() as s:
        s.post("http://"+setting.httpPrefix+"/cheetah/login", data=authorisation)
        subjectUrl = str("http://"+setting.httpPrefix+"/cheetah/api/user/getsubject/"+str(sub_id))
        r = s.get(subjectUrl)

        decode = r.content.decode('UTF-8')
        load=json.loads(decode)
        log.interlog(__file__, "renameSubject", "Subject befor change "  + str(load["resBody"]),
                     "Check")
        subject1=load["resBody"]
        subject2=copy.deepcopy(subject1)
        subject2["comment"]="CHANGED"
        headers = {'content-type': 'application/json'}
        #print(subject2)
        r = s.put("http://"+setting.httpPrefix+"/cheetah/api/user/updatesubject", headers=headers, json=subject2)
        decode = r.content.decode('UTF-8')

        load = json.loads(decode)
        #print(decode)
        r = s.get(subjectUrl)
        subject3=json.loads(r.content.decode('UTF-8'))["resBody"]

        log.interlog(__file__, "change Subject", str("Subject successful changed"), "Check")


def getSubjectIdByName(authorisation, subject_name, log):
    with rq.Session() as s:
        s.post("http://"+setting.httpPrefix+"/cheetah/login", data=authorisation)
        r = s.get("http://"+setting.httpPrefix+"/cheetah/api/user/subjects", data=authorisation)
        decode = r.content.decode('UTF-8')

        #print(decode)

        loads = json.loads(decode)
        testArray = []
        for e in loads["resBody"]:
            if e["subject_id"] == subject_name:
                log.logHTTPResponse(__file__, "getSubjectIdByName", "Subject with name "+subject_name + " found.")
                return e["id"]
        log.logHTTPResponse(__file__, "getSubjectIdByName", "Subject with name " + subject_name + "NOT found.")
        log.close()
        raise RequirementError("SubjectNotFound")


def deleteSubject(authorisation, subjectToChangeId, log):
    with rq.Session() as s:
        s.post("http://"+setting.httpPrefix+"/cheetah/login", data=authorisation)
        r = s.delete("http://"+setting.httpPrefix+"/cheetah/api/user/deletesubject/"+str(subjectToChangeId), data=authorisation)
        decode = r.content.decode('UTF-8')
        loads = json.loads(decode)
        if(loads["statusCode"]==0):
            log.logHTTPResponse(__file__, "deleteSubject", "Subject with id " + str(subjectToChangeId) + " deleted.")
        else:
            log.logHTTPResponse(__file__, "deleteSubject", "Subject with id " + str(subjectToChangeId) + " NOT deleted.")
            log.close()
            raise RequirementError("Could not delete subejct")


