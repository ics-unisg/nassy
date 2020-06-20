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

def checkFixations():

    log = logg.Logger("Requirement8_Fixations_", "./logs/")


    studyName = "TestStudy"

    # Clean database with all artefacts of study
    helper.cleanDatabase(authorisation, studyName)

    # Create Study
    studyId = createStudyAndUser.create_study(authorisation, studyName, log)

    # Create user
    subject_list = ["p1"]
    listOfCreatedSubjects = createStudyAndUser.createSubjects(authorisation, subject_list, studyId, log)

    #upload a file
    file1 = "./testData/p1@TestStudy@fixations.tsv"
    files = [('files', (os.path.basename(file1), open(file1, 'rb'), 'application/octet-stream'))]
    url = "http://"+setting.httpPrefix+"/cheetah/api/user/uploadFile"
    fileId, fileIdsFull = uploadFilesViaNamingConvention.uploadFilesNaming(authorisation, files, url, log)

    #Set variables for measures
    baseline = "grey" #baseline set
    columNames = "[\"PupilLeft\",\"PupilRight\"]"
    fileIDs = str(fileId)
    labelColumn = "Label"
    labels = "[]"
    timeStampColumnNAme = "EyeTrackerTimestamp"

    #calculate measures
    measureTaskIds = measures.calculateMeasures(authorisation, baseline, columNames, fileIDs, labelColumn, labels,
                                                timeStampColumnNAme, log)

    # wait till task is finished
    helper.waitTilfFinished(authorisation, measureTaskIds, log)

    # get id of file (first calculated measure
    measureFileId = tasks.getUserDataFileId(authorisation, measureTaskIds, log)[0]

    #download the measure file
    path=file.downloadFile(authorisation, measureFileId, "measures", log)

    #verify the downloaded statistics, for statistics with baseline
    calcStatisticsWithBaseline.compareStatistics("Label", {}, file1, path, baseline,log)

    #remove downloaded file
    os.remove(path)

#checkFixations()