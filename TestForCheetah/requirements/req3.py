from help import helper
from logger import logg
from net import uploadFilesViaNamingConvention
from net import tasks
from net import operateOnSubjects
from net import filterScripts
from net import createStudyAndUser
from net import measures
from net import file
import os
from fileoperations import calcStatisticsWithOutBaseline, fileEquality
from help import setting

#Data for authorisation
from net.tasks import getUserData

authorisation = {
    'username': 'test.user@test.dcap',
    'password': 'Test'
}

def checkFilteredFileEquality(taskId, name, log):
    filename = "download"
    fileId = getUserData(authorisation, taskId, log)
    file.downloadFileAndSearchEyetrackerTimeStamp(authorisation, fileId, filename, log)
    tsv_name = "./testData/max@%s.tsv" % name
    fileEquality.compareColumns("./logs/download.tsv", tsv_name, "PupilLeft", "PupilLeft", log)
    fileEquality.compareColumns("./logs/download.tsv", tsv_name, "PupilRight", "PupilRight", log)

    os.remove('./logs/' + filename + '.tsv')

def checkRequirementThree():
    #create log file
    log = logg.Logger("Requirement3_", "./logs/")

    #Standart name for test study
    studyName = "TestStudy"

    #Clean database with all artefacts of study
    helper.cleanDatabase(authorisation, studyName)

    #Create Study
    studyId= createStudyAndUser.create_study(authorisation, studyName, log)


    #Create List of users
    #subject_list=["p001b",  "p024a"]
    subject_list=["max"]
    listOfCreatedSubjects = createStudyAndUser.createSubjects(authorisation, subject_list, studyId, log)


    #upload a files
    file1= "./testData/max@TestStudy@1.tsv"
    files = [('files',(os.path.basename(file1), open(file1, 'rb'), 'application/octet-stream'))]
    url="http://"+setting.httpPrefix+"/cheetah/api/user/uploadFile"
    fileIds, fileIdsFull= uploadFilesViaNamingConvention.uploadFilesNaming(authorisation, files, url, log)

    # list for wait function
    forWait=""

    #filter files uploaded above. Use standard filter setting
    fullFilteredId= filterScripts.fullFilterData(authorisation, fileIds, log)
    forWait=fullFilteredId

    #filter, user filter substitue pupils
    subPup= filterScripts.filterData(authorisation, fileIds, "subPup", log)
    forWait=forWait+", "+subPup

    # filter, user filter substitute gaze
    subGaze= filterScripts.filterData(authorisation, fileIds, "subGaze", log)
    forWait=forWait+", "+subGaze

    # filter, user filter blink detection
    blink = filterScripts.filterData(authorisation, fileIds, "blink", log)
    forWait=forWait+", "+blink

    # filter, user filter standard deviation
    stdDev = filterScripts.filterData(authorisation, fileIds,"stdDev" , log)
    forWait=forWait+", "+stdDev

    # filter, user filter linear interpolation
    linSub = filterScripts.filterData(authorisation, fileIds, "linIn", log)
    forWait=forWait+", "+linSub

    # filter, user filter butterworth
    butter = filterScripts.filterData(authorisation, fileIds, "butter", log)
    forWait=forWait+", "+butter

    #wait till filters are applied
    helper.waitTilfFinished(authorisation, forWait, log)


    #check full
    checkFilteredFileEquality(fullFilteredId, "full", log)
    # check subst pupil
    checkFilteredFileEquality(subPup, "subPup", log)
    # check substitute gaze
    checkFilteredFileEquality(subGaze, "subGaze", log)
    # check blink
    checkFilteredFileEquality(blink, "blink", log)
    # check stdDev
    checkFilteredFileEquality(stdDev, "stdDev", log)
    # check linear interpolation
    checkFilteredFileEquality(linSub, "linIn", log)
    # check butterworth
    checkFilteredFileEquality(butter, "butter", log)
