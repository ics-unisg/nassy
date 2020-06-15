import os

from help import helper
from logger import logg
from net import createStudyAndUser, uploadFilesViaNamingConvention, filterScripts, file, tasks
import pandas as pd
from help import setting
import requests as rq
import json

authorisation = {
    'username': 'test.user@test.dcap',
    'password': 'Test'
}

def checkFixations():

    log = logg.Logger("TEPR", "./logs/")


    studyName = "TestStudy"

    # Clean database with all artefacts of study
    helper.cleanDatabase(authorisation, studyName)

    # Create Study
    studyId = createStudyAndUser.create_study(authorisation, studyName, log)

    # Create user
    subject_list = ["p001b"]
    listOfCreatedSubjects = createStudyAndUser.createSubjects(authorisation, subject_list, studyId, log)

    #upload a file
    file1 = "./testData/p001b@TestStudy@TEPR.tsv"
    files = [('files', (os.path.basename(file1), open(file1, 'rb'), 'application/octet-stream'))]
    url = "http://"+setting.httpPrefix+"/cheetah/api/user/uploadFile"
    fileId, fileIdsFull = uploadFilesViaNamingConvention.uploadFilesNaming(authorisation, files, url, log)

    #Set variables for measures
    period=10000
    columNames = "[\"PupilLeft\",\"PupilRight\"]"
    fileIDs = str(fileId)
    labelColumn = "mylab"
    labels = "[]"
    timeStampColumnNAme = "EyeTrackerTimestamp"

    #calculate measures
    measureTaskIds = calculateTEPR(authorisation,log, fileId, period)

    # wait till task is finished
    helper.waitTilfFinished(authorisation, measureTaskIds, log)

    # get id of file (first calculated measure
    measureFileId = tasks.getUserDataFileId(authorisation, measureTaskIds, log)[0]

    #download the measure file
    path=file.downloadFile(authorisation, measureFileId, "teprValues", log)

    method_name(path, log)



    #remove downloaded file
    os.remove(path)


def method_name(path, log: logg.Logger, statistics="baseline;mean;mean;min;max;standDev;standErr"):
    statsToCheck = statistics.split(";")

    checkfile = pd.read_csv(path, delimiter=";")

    if checkfile["TEPR_baseline_a"][0] != 3.5:
        log.interlog(__file__,"checkparameter","Wrong base", "CHECK")
        log.close()
        raise Exception("wrong value")
    if ("mean" in statsToCheck):
        if checkfile["TEPR_Mean_a"][0] != 5.0:
            log.interlog(__file__,"checkparameter","Wrong mean", "CHECK")
            log.close()
            raise Exception("wrong value")
    if ("mean" in statsToCheck):
        if checkfile["TEPR_Median_a"][0] != 5.0:
            log.interlog(__file__,"checkparameter","Wrong median", "CHECK")
            log.close()
            raise Exception("wrong value")
    if ("min" in statsToCheck):
        if checkfile["TEPR_Min_a"][0] != 5.0:
            log.interlog(__file__,"checkparameter","Wrong min", "CHECK")
            log.close()
            raise Exception("wrong value")
    if ("max" in statsToCheck):
        if checkfile["TEPR_Max_a"][0] != 5.0:
            log.interlog(__file__,"checkparameter","Wrong max", "CHECK")
            log.close()
            raise Exception("wrong value")
    if ("standDev" in statsToCheck):
        if checkfile["TEPR_StandardDeviation_a"][0] != 0.0:
            log.interlog(__file__,"checkparameter","Wrong stddev", "CHECK")
            log.close()
            raise Exception("wrong value")
    if ("standErr" in statsToCheck):
        if checkfile["TEPR_StandardError_a"][0] != 0.0:
            log.interlog(__file__,"checkparameter","Wrong stderr", "CHECK")
            log.close()
            raise Exception("wrong value")
    log.interlog(__file__,"checkparameter","parameters are okay", "CHECK")

def calculateTEPR(authorisation, logFile, ids, period, label="mylab", statistics="mean;median;min;max;standDev;standErr"):

    tepr = "{  \"name\":  \"TEPRFilter\",  \"actualParameters\":  {\"timestampcolumn\":  \"EyeTrackerTimestamp\",  \"labelColumn\": \"%s\", \"nameColumn\": \"myname\",\"period\":  %d,  \"statistics\": \"%s\"}, \"columns\":  { \"left_pupil\":  \"PupilLeft\",  \"right_pupil\":  \"PupilRight\"  } }"%(label, period, statistics)
    with rq.Session() as s:
        s.post(url=filterScripts.url, data=authorisation)
        dataString = filterScripts.s3 + str(ids) + filterScripts.s4 + tepr + filterScripts.s5
        r = s.post("http://" + setting.httpPrefix + "/cheetah/api/user/filterrequest", data=dataString,
                   headers={'content-type': 'application/json'})
        decode = r.content.decode('UTF-8')
        loads = json.loads(decode)
        logFile.logHTTPResponse(__file__, "trim files", loads)
        taskIds = loads['resBody']
        print(taskIds)
        return taskIds

checkFixations()

