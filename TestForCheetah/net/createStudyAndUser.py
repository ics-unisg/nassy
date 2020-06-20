from net import user
import json
import  requests as rq
from help import setting
from help.error import RequirementError

headers = {'content-type': 'application/json'}

url = "http://"+setting.httpPrefix+"/cheetah/login"


def create_study(authorisation, studyName, logFile):
    job_data = "{\"comment\": \"Study to test\",\"name\": \"" + studyName + "\"}"
    with rq.Session() as s:
        s.post(url=url, data=authorisation)
        r = s.post("http://"+setting.httpPrefix+"/cheetah/api/user/study", headers=headers, data=job_data)

        decode = r.content.decode('UTF-8')

        loads = json.loads(decode)
        if(loads["statusCode"]!=0):

            logFile.logHTTPResponse(__file__, "createStudy failed", loads)
            logFile.close()
            raise RequirementError("Could not create study")
        logFile.logHTTPResponse(__file__, "createStudy", loads)
        return loads['resBody']["id"]


#,  "p764b", "p551a", "p768b", "p057a", "p846a", "p876a", "p786a", "p338b,"p741b""



def createSubjects(authorisation, subject_list, id, logFile):

    with rq.Session() as s:
        s.post(url=url, data=authorisation)
        returnarray=[]
        for subject in subject_list:
            sub = "{  \"comment\": \"Keines\",  \"email\": \"" + subject + "@1234.com\",  \"id\": 0,  \"studyId\": " + \
                  str(id) + ",  \"studyName\": \"string\",  \"subject_id\": \"" + subject + "\",  \"synchronized_from\": 0}"
            r = s.post("http://"+setting.httpPrefix+"/cheetah/api/user/addsubject", headers=headers, data=sub)
            decode = r.content.decode('UTF-8')
            loads = json.loads(decode)
            try:
                logFile.logHTTPResponse(__file__, "createSubjects", loads)
                returnarray.append(loads['resBody']["id"])
            except (KeyError, TypeError):
                logFile.logHTTPResponse(__file__, "createSubjects", decode)
                logFile.close()
                raise RequirementError("could not create subjects")
        return returnarray

studyName0="TestStudy"
subject_list0=["p001b",  "p024a"]

authorisation0 = {
    'username': 'test.user@test.dcap',
    'password': 'Test'
}

#logFile=Logger()

#create_study(authorisation0,studyName0, logFile)
#createUsers(authorisation0, subject_list0,logFile)

def tryToCreateDoubleSubject(authorisation, subject, id, logFile):
    with rq.Session() as s:
        s.post(url=url, data=authorisation)
        returnarray = []
        sub = "{  \"comment\": \"Keines\",  \"email\": \"" + subject + "@1234.com\",  \"id\": 0,  \"studyId\": " + \
                  str(
                      id) + ",  \"studyName\": \"string\",  \"subject_id\": \"" + subject + "\",  \"synchronized_from\": 0}"
        r = s.post("http://"+setting.httpPrefix+"/cheetah/api/user/addsubject", headers=headers, data=sub)
        decode = r.content.decode('UTF-8')
        loads = json.loads(decode)
        try:
            if loads["statusCode"]==1503:
                logFile.logHTTPResponse(__file__, "Great, creation of subject with double name not allowed...", loads)
            else:
                logFile.logHTTPResponse(__file__, "Damn, now there are subjects with the same id...", loads)
                raise RequirementError("Subjects with same id...")
                logFile.close()
        except (KeyError, TypeError):
            logFile.logHTTPResponse(__file__, "creation of double subjects failed because of unexpected reasons...", decode)
            logFile.close()
            raise RequirementError("could not create subjects")

def createUserForTest():
    with rq.Session() as s:
        email = input("Enter user email: ")
        pwd= input("Enter user password: ")
        authorisationtmp = {'username': email,'password': pwd}
        s.post(url=url, data=authorisationtmp)
        message="{ \"email\": \"test.user@test.dcap\", \"firstname\": \"Test\", \"id\": 0, \"lastname\": \"User\", \"password\": \"Test\", \"role\": \"administrator\"}"
        r = s.post("http://" + setting.httpPrefix + "/cheetah/api/admin/user", headers=headers, data=message)
        decode = r.content.decode('UTF-8')
        loads = json.loads(decode)


def deleteUserForTest():
    authorisation = {
        'username': 'test.user@test.dcap',
        'password': 'Test'
    }
    with rq.Session() as s:
        s.post(url=url, data=authorisation)
        r=s.get("http://"+setting.httpPrefix+"/cheetah/api/admin/user")


        decode = r.content.decode('UTF-8')
        loads = json.loads(decode)
        response=loads["resBody"]
        for u in response:
            if u["email"] =="test.user@test.dcap":
                r = s.delete("http://" + setting.httpPrefix + "/cheetah/api/admin/user/" + str(u["id"]))
                decode = r.content.decode('UTF-8')
                message = json.loads(decode)
                print(message)
        return response


