import json

import requests as rq
from help import setting


from help.error import RequirementError
from net import operateOnStudy

headers = {'content-type': 'application/json'}


def createNewUser(authorisation, log):
    newUser ="{\"id\":0, \"lastname\":\"User\", \"firstname\":\"Test\", \"email\": \"123@yahoo.com\", \"password\": \"123\", \"role\":\"user\"}"
    with rq.Session() as s:
        s.post("http://"+setting.httpPrefix+"/cheetah/login", data=authorisation)
        r = s.post("http://"+setting.httpPrefix+"/cheetah/api/admin/user", headers=headers, data=newUser)
        decode = r.content.decode('UTF-8')
        message  = json.loads(decode)
        if message["statusCode"]==0:
            log.logHTTPResponse(__file__, "createNewUser", "User with mailadress 123@yahoo.com created.")
            return ({'username': '123@yahoo.com','password': '123'}, message["resBody"]["id"])

        else:
            #print(message)
            log.logHTTPResponse(__file__, "createNewUser", "Could not create User.")
            log.close()
            raise RequirementError("no user created.")

def createNewUser2(authorisation, log):
    newUser ="{\"id\":0, \"lastname\":\"User2\", \"firstname\":\"Test2\", \"email\": \"1234@yahoo.com\", \"password\": \"123\", \"role\":\"user\"}"
    with rq.Session() as s:
        s.post("http://"+setting.httpPrefix+"/cheetah/login", data=authorisation)
        r = s.post("http://"+setting.httpPrefix+"/cheetah/api/admin/user", headers=headers, data=newUser)
        decode = r.content.decode('UTF-8')
        message  = json.loads(decode)
        #print(message)
        if message["statusCode"]==0:
            return ({'username': '1234@yahoo.com','password': '123'}, message["resBody"]["id"])
            log.logHTTPResponse(__file__, "createNewUser", "User2 with mailadress 1234@yahoo.com created.")

        else:
            log.logHTTPResponse(__file__, "createNewUser", "Could not create User2.")
            log.close()
            raise RequirementError("no user created.")

def deleteUser(authorisation, id, log):
    with rq.Session() as s:
        s.post("http://"+setting.httpPrefix+"/cheetah/login", data=authorisation)
        r = s.delete("http://"+setting.httpPrefix+"/cheetah/api/admin/user/"+str(id))
        decode = r.content.decode('UTF-8')
        message = json.loads(decode)
        #print(message)
        return
        if message["resBody"] == True:
            log.logHTTPResponse(__file__, "deleteUser", "User deleted.")
        else:
            log.logHTTPResponse(__file__, "deleteUser", "Could not delete user.")
            log.close()
            raise RequirementError("no user deleted")



authorisation1 = {
    'username': 'test.user@test.dcap',
    'password': 'Test'
}

