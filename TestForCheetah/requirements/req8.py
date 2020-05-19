import os

from help import helper
from logger import logg
from net import createStudyAndUser, uploadFilesViaNamingConvention, filterScripts, file, tasks, measures
from fileoperations import calcStatisticsWithBaseline, calcStatisticsWithOutBaseline
from help import setting

authorisation = {
    'username': 'test.user@test.dcap',
    'password': 'Test'
}

def checkRequirementEight():

    log = logg.Logger("Requirement8_", "./logs/")


    studyName = "TestStudy"

    # Clean database with all artefacts of study
    helper.cleanDatabase(authorisation, studyName)

    # Create Study
    studyId = createStudyAndUser.create_study(authorisation, studyName, log)

    # Create user
    subject_list = ["p024"]
    listOfCreatedSubjects = createStudyAndUser.createSubjects(authorisation, subject_list, studyId, log)

    #upload a file
    file1 = "./testData/p024@TestStudy@fullFiltered.tsv"
    files = [('files', (os.path.basename(file1), open(file1, 'rb'), 'application/octet-stream'))]
    url = "http://"+setting.httpPrefix+"/cheetah/api/user/uploadFile"
    fileId, fileIdsFull = uploadFilesViaNamingConvention.uploadFilesNaming(authorisation, files, url, log)

    #Set variables for measures
    baseline0 = "" # no baseline
    columNames0 = "[\"PupilLeft\",\"PupilRight\"]"
    fileIDs0 = str(fileId)
    labelColumn0 = "MediaName"
    labels0 = "[]"
    timeStampColumnNAme0 = "EyeTrackerTimestamp"

    #calculate measures
    measureTaskIds = measures.calculateMeasures(authorisation, baseline0, columNames0, fileIDs0, labelColumn0, labels0,
                                                timeStampColumnNAme0, log)

    # wait till task is finished
    helper.waitTilfFinished(authorisation, measureTaskIds, log)

    # get id of file (first calculated measure
    measureFileId = tasks.getUserDataFileId(authorisation, measureTaskIds, log)[0]

    #download the measure file
    path = file.downloadFile(authorisation, measureFileId, "measures", log)

    #verify the downloaded statistics, for statistics without baseline
    calcStatisticsWithOutBaseline.compareStatistics("MediaName", {}, file1, path, log)

    #remove downloaded file
    os.remove(path)
###########

    #Set variables for measures
    baseline0 = "M1A-Q01.png" #baseline set
    columNames0 = "[\"PupilLeft\",\"PupilRight\"]"
    fileIDs0 = str(fileId)
    labelColumn0 = "MediaName"
    labels0 = "[]"
    timeStampColumnNAme0 = "EyeTrackerTimestamp"

    #calculate measures
    measureTaskIds = measures.calculateMeasures(authorisation, baseline0, columNames0, fileIDs0, labelColumn0, labels0,
                                                timeStampColumnNAme0, log)

    # wait till task is finished
    helper.waitTilfFinished(authorisation, measureTaskIds, log)

    # get id of file (first calculated measure
    measureFileId = tasks.getUserDataFileId(authorisation, measureTaskIds, log)[0]

    #download the measure file
    path=file.downloadFile(authorisation, measureFileId, "measures", log)

    #verify the downloaded statistics, for statistics with baseline
    calcStatisticsWithBaseline.compareStatistics("MediaName", {}, file1, path, baseline0,log)

    #remove downloaded file
    os.remove(path)

