import  datetime
import os
from termcolor import colored

class Logger:



    def __init__(self, name, path="./logs/"):
        self.logfile= self.create_file(name, path)
        print(colored(name, "green"))

    def getFileName(self, file):
        return os.path.basename(file)

    def logString(self, file, where, message, logType):
        try:
            message_type = type(message['resBody'])
        except TypeError:
            return str(self.getFileName(file) + "; " + str(datetime.datetime.now()) + "; " + where + "; " + str(message)+ "; " + logType)
        if(message_type is not None):
            message=str(message['resBody'])
        else:
            message = str(message)
        logString = str(self.getFileName(file) + "; " + str(datetime.datetime.now()) + "; " + where + "; " + message + "; " + logType)
        return logString


    def logHTTPResponse(self, file, where, message):
        self.logfile.write("\n"+self.logString(file, where, message, "HTTP"))

    def interlog(self, file, where, message, type):
        self.logfile.write("\n" + self.logString(file, where, message, type))

    def create_file(self,name, path):
        file = path+(name) + str(datetime.date.today())
        ind = 1
        fileToCreate = file + ".csv"

        while (os.path.exists(fileToCreate)):
            fileToCreate = file + "(" + str(ind) + ").csv"
            ind = ind + 1

        logFile = open(fileToCreate, "w+")
        logFile.write("Class; Time; Method; Message; Type")
        return logFile

    def close(self):
        self.logfile.close()
