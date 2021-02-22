import datetime
import os


class Logger:


    def getFileName(self, file):
        return os.path.basename(file)

    def logString(self, file, where, message, messageType):
        if(type(message) is not str):
            message="Ojeh"
        msg = self.getFileName(file) + "; " + str(
            datetime.datetime.now()) + "; " + where + "; " + message + "; " + messageType
        ##print(msg)
        return msg

    def logHTTPResponse(self, file, where, message):
        self.log(file, where, message, "HTTP")

    def interlog(self, file, where, message, messageType):
        if (type(message) is not str):
            message = "Ojeh"
        msg = self.getFileName(file) + "; " + str(
            datetime.datetime.now()) + "; " + where + "; " + message + "; " + messageType
        ##print(msg)
        return msg


    def log(self, file, where, message, messageType):
        self.logString(file, where, message, messageType)

    def close(self):
        ##print("Error")
        pass