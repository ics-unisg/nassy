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



def checkRequirementSix():
    #create log file
    log = logg.Logger("Requirement6_", "./logs/")

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
    file1= "./testData/max@TestStudy@averageTest.tsv"
    files = [('files',(os.path.basename(file1), open(file1, 'rb'), 'application/octet-stream'))]
    url="http://"+setting.httpPrefix+"/cheetah/api/user/uploadFile"
    fileIds, fileIdsFull= uploadFilesViaNamingConvention.uploadFilesNaming(authorisation, files, url, log)

    jsonStringForTwo = "{  \"name\":  \"AverageFilter\",  \"actualParameters\":  {\"timestampcolumn\":  \"EyeTrackerTimestamp\"},  \"columns\":  { \"one\":  \"columnOne\", \"two\":  \"columnTwo\"}  }"


    #filter files uploaded above. Use average filter setting
    filename = "forTwo"
    forTwo= filterScripts.doFilterData(authorisation, jsonStringForTwo, fileIds, filename, log)


    #wait till filters are applied
    helper.waitTilfFinished(authorisation, forTwo, log)

    #download the filtered file
    fileId = getUserData(authorisation, forTwo, log)
    file.downloadFile(authorisation, fileId, filename, log)

    #Compare the file with a model file
    file.columnCompare(filename, "average", "./testData/confirmAverageTest.tsv", "averageTwo", log)

    #remove downloaded file
    os.remove('./logs/' + filename + '.tsv')

    jsonStringForThree = "{  \"name\":  \"AverageFilter\",  \"actualParameters\":  {\"timestampcolumn\":  \"EyeTrackerTimestamp\"},  \"columns\":  { \"one\":  \"columnOne\", \"two\":  \"columnTwo\", \"three\":  \"columnThreee\"}  }"

    #filter files uploaded above. Use average filter setting
    filename = "forThree"
    forThree = filterScripts.doFilterData(authorisation, jsonStringForThree, fileIds,filename, log)

    # wait till filters are applied
    helper.waitTilfFinished(authorisation, forThree, log)

    #download the filtered file
    fileId = getUserData(authorisation, forThree, log)
    file.downloadFile(authorisation, fileId, filename, log)

    #Compare the file with a model file
    file.columnCompare(filename, "average", "./testData/confirmAverageTest.tsv", "averageThree", log)

    #remove downloaded file
    os.remove('./logs/' + filename + '.tsv')

    #remove study from server
    helper.cleanDatabase(authorisation, studyName)






