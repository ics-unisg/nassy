import os

from help import helper
from logger import logg
from net import createStudyAndUser, uploadFilesViaNamingConvention, filterScripts, file
from net.file import compareDifferentFiles, compareIdenticalFiles
from net.tasks import getUserData
from help import setting


def checkRequirementNine():

    log = logg.Logger("Requirement9_", "./logs/")


    # informations required for login, i.e. the username and the password for a http session
    authorisation = {
        'username': 'test.user@test.dcap',
        'password': 'Test'
    }

    #Standart name for test study
    studyName = "TestStudy"

    #clean database
    helper.cleanDatabase(authorisation, studyName)

    #create standard study
    studyId = createStudyAndUser.create_study(authorisation, studyName, log)

    # Create List of users
    subject_list = ["u1"]
    createStudyAndUser.createSubjects(authorisation, subject_list, studyId, log)

    #upload a file
    ressourceFile= "./testData/u1@TestStudy@d1.tsv"
    files = [('files',(os.path.basename(ressourceFile), open(ressourceFile, 'rb'), 'application/octet-stream'))]
    url="http://"+setting.httpPrefix+"/cheetah/api/user/uploadFile"
    id, fileIdsFull = uploadFilesViaNamingConvention.uploadFilesNaming(authorisation, files, url, log)

    #JSON String that should be sent to the server
    jsonStringForTwo = "{  \"name\":  \"AverageFilter\",  \"actualParameters\":  {\"timestampcolumn\":  \"EyeTrackerTimestamp\"},  \"columns\":  { \"one\":  \"PupilLeft\", \"two\":  \"PupilRight\"}  }"

    # filter files uploaded above. Use standard filter setting
    filename = "forTwo"
    forTwo = filterScripts.doFilterData(authorisation, jsonStringForTwo, id, filename, log)

    # wait till filters are applied
    helper.waitTilfFinished(authorisation, forTwo, log)


    #download the filtered file
    fileId = getUserData(authorisation, forTwo, log)
    pathChanged = file.downloadFile(authorisation, fileId, filename, log)
    pathOriginal = file.downloadFile(authorisation, id[0], "original", log)

    #Compare the files that should be identical
    compareIdenticalFiles(log, pathOriginal, ressourceFile)


    #Compare the files that should be different
    compareDifferentFiles(log, pathChanged, pathOriginal)


    #remove file
    os.remove(pathChanged)

    #remove file
    os.remove(pathOriginal)

    #cleanDatabase
    helper.cleanDatabase(authorisation, studyName)


