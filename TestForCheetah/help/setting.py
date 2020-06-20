httpPrefix="localhost:8989"
#httpPrefix="localhost:4000"
#httpPrefix="172.18.03:8989"

#hostName = "172.18.02"

#pathTofiles=".."
#pathTofiles="/home/uli/postCheetah/.."




hostName = "localhost"
schemaName= ""
dbUser=""
dbPassword=""

def getPathToFiles(string):
    if(hostName=="localhost"):
        return ".."+string
    else:
        string=string[5:]
        return "/home/uli/postCheetah/"+string
