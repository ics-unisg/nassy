import filecmp
import os
import pandas as pd
import requests as rq
from help import setting
from help import error
from help.error import RequirementError


def downloadFileAndSearchEyetrackerTimeStamp(authorisation, id, filename, logFile):
    with rq.Session() as s:
        s.post("http://"+setting.httpPrefix+"/cheetah/login", data=authorisation)
        response = s.get("http://"+setting.httpPrefix+"/cheetah/api/user/download/"+str(id))
        with open('./logs/'+filename+'.tsv', 'wb') as f:
            f.write(response.content)
        with open('./logs/' + filename + '.tsv', 'r') as f:
            firstString= f.readline()
            logFile.logHTTPResponse(__file__, "in Download, ", "file with id " + str(id) + " was downloaded as " + filename+".tsv.")
            if("EyeTrackerTimestamp" in firstString):
                logFile.interlog(__file__, "in Download, ",
                                        "\"EyeTrackerTimestamp\" was found in " + filename + ".tsv.", "Check")
            else:
                logFile.interlog(__file__, "in Download, ",
                                        "\"EyeTrackerTimestamp\" NOT found in " + filename + ".tsv.", "Check")
                logFile.close()
                raise RequirementError("\"EyeTrackerTimestamp\" NOT found in " + filename + ".tsv.")

def downloadFile(authorisation, id, filename, logFile):
    with rq.Session() as s:
        s.post("http://"+setting.httpPrefix+"/cheetah/login", data=authorisation)
        response = s.get("http://"+setting.httpPrefix+"/cheetah/api/user/download/"+str(id))
        with open('./logs/'+filename+'.tsv', 'wb') as f:
            f.write(response.content)
            logFile.logHTTPResponse(__file__, "in Download, ", "file with id " + str(id) + " was downloaded as " + filename+".tsv.")
            return './logs/'+filename+'.tsv'

def downloadAndReadFile(authorisation, id, filename, logFile):
    with rq.Session() as s:
        s.post("http://"+setting.httpPrefix+"/cheetah/login", data=authorisation)
        response = s.get("http://"+setting.httpPrefix+"/cheetah/api/user/download/"+str(id))
        with open('./logs/'+filename+'.tsv', 'wb') as f:
            return response.content

def tryToDownloadFile(authorisation, id, logFile):
    with rq.Session() as s:
        s.post("http://"+setting.httpPrefix+"/cheetah/login", data=authorisation)
        response = s.get("http://"+setting.httpPrefix+"/cheetah/api/user/download/" + str(id))
        if response.status_code == 403:
            logFile.logHTTPResponse(__file__, "TryToDownload, ", "Files from other users protected")
        else:
            logFile.logHTTPResponse(__file__, "TryToDownload, ", "Not files from other users protected")
            logFile.close()
            raise RequirementError("Other user could  download foreign data")

def columnCompare(filename, columnOne, path, columnTwo, log):
    fileOne = pd.read_csv("./logs/"+filename+".tsv", delimiter="\t")
    fileTwo = pd.read_csv(path, delimiter="\t")
    columnValuesOne = fileOne[columnOne]
    columnValuesTwo = fileTwo[columnTwo]
    if len(columnValuesOne)!=len(columnValuesTwo):
        log.interlog(__file__, "columnComp, ", "columns do not have same length.", "Inter")
        raise RequirementError("columns do not have same length.")
    for i in range(len(columnTwo)):
        if abs(columnValuesOne[i]-columnValuesTwo[i])>0.000001:
            log.interlog(__file__, "columnComp, ", "column values are not similar.", "Inter")
            log.close()
            raise RequirementError("column values are not similar.", i, columnValuesOne[i], columnValuesTwo[i])
    log.interlog(__file__, "columnComp, ", "column values are similar.", "Inter")




def fileCompare(filename, path, logfile):
    similar=filecmp.cmp(path, './logs/'+filename+'.tsv')
    if similar:
        logfile.interlog(__file__, "fileComp, ", "files are similar.", "Inter")
    else:
        logfile.interlog(__file__, "fileComp, ", "files are NOT similar.", "Inter")
        logfile.close()
        raise RequirementError("Files NOT equal")

def compareMean(adressResult, adressRessource, log):
    myReader = pd.read_csv(adressRessource, delimiter="\t")
    result = pd.read_csv(adressResult, delimiter="\t")
    left = myReader["PupilLeft"]
    right = myReader["PupilRight"]
    meanAdded = result["Mean"]
    for i in range(len(left)):
        mean= (float(right[i].replace(",",".")) + float(left[i].replace(",",".")))/2
        if(abs(mean-float(meanAdded[i])>0.0001)):
            log.interlog(__file__, "fileComp, ", "Mean NOT to file added.", "Inter")
            log.close()
            raise RequirementError("Not mean")
    log.interlog(__file__, "fileComp, ", "Mean to file added.", "Inter")


def compareMinusTen(adressResult, adressRessource, log):
    myReader = pd.read_csv(adressRessource, delimiter="\t")
    result = pd.read_csv(adressResult, delimiter="\t")
    leftOld = myReader["PupilLeft"]
    leftNew = result["PupilLeft"]
    for i in range(len(leftOld)):
        l_n = leftNew[i].replace(",", ".")
        l_o = float(leftOld[i].replace(",", ".")) - 10
        if (abs(l_o-float(l_n))>0.00001):
            log.interlog(__file__, "fileComp, ", "Minus ten NOT done.", "Inter")
            log.close()
            raise RequirementError("No minus ten", str(l_o), str(float(leftOld[i].replace(",", "."))), l_n)
    log.interlog(__file__, "fileComp, ", "Minus ten done.", "Inter")



def deletFile(filename, log):
    os.remove('./logs/'+filename+'.tsv')
    log.interlog(__file__, "delete file, ", "file  " + filename + " was deleted.", "Inter")


def compareDifferentFiles(log, pathChanged, pathOriginal):
    second = filecmp.cmp(pathChanged, pathOriginal)
    if (second):
        log.close()
        log.interlog(__file__, "compareFiles", "Original file and changed downlaoded are NOT identical", "Check")
        raise error.RequirementError("Original file and changed downlaoded are NOT identical")
    else:
        log.interlog(__file__, "compareFiles", "Original file and changed downlaoded not are identical", "Check")


def compareIdenticalFiles(log, pathOriginal, ressourceFile):
    first = filecmp.cmp(ressourceFile, pathOriginal)
    if (first):
        log.interlog(__file__, "compareFiles", "Original file and not changed downlaoded are identical", "Check")
    else:
        log.interlog(__file__, "compareFiles", "Original file and not changed downlaoded are NOT identical", "Check")
        log.close()
        raise error.RequirementError("Original file and not changed downlaoded are NOT identical")