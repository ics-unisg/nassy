import  requests as rq
from help import setting

url = "http://"+setting.httpPrefix+"/cheetah/login"
headers = {'content-type': 'application/json'}


with rq.Session() as s:

    default = input("Is \"admin@administrator.dcap\" the default administrator user (y or n): ")
    if default=="y":
        email ="admin@administrator.dcap"
    elif default=="n":
        email = input("Enter user email: ")
    else:
        print("Sorry, no \"y\" or \"n\" as answer")
        exit()
    pwd= input("Enter user password: ")
    authorisationtmp = {'username': email ,'password': pwd}
    s.post(url=url, data=authorisationtmp)

    pwd1= input("Enter new password: ")
    pwd2= input("Re-enter new password: ")
    if pwd1!=pwd2:
        print("Sorry, not same pwd")
        exit()

    message = "{\"pwd\":\"%s\"}" % (pwd1);
    r = s.post("http://" + setting.httpPrefix + "/cheetah/api/user/user", headers=headers, data=message)
    decode = r.content.decode('UTF-8')
    print(decode)