import os
from help import helper
from logger import logg
from net import filterScripts, uploadFilesViaNamingConvention, file, createStudyAndUser
from help.helper import cleanDatabase
from net.file import downloadFile
from net.filterScripts import ownFilterScript
from net.tasks import getUserData
from net.user import createNewUser, deleteUser
from help import setting

def checkRequirementFive():

    # file with the python code
    pythonFile="./testData/addMeanScript.py"

    # file with the parameters
    parameterFile= "./testData/addMeanParam.param"

    log = logg.Logger("Requirement5a_", "./logs/")


    # informations required for login, i.e. the username and the password for a http session
    authorisation = {
        'username': 'test.user@test.dcap',
        'password': 'Test'
    }

    #Name for filter
    filterName = "TestPython"

    #data for upload
    job_data = {'name': filterName, 'type': 'custom'}

    #Standart name for test study
    studyName = "TestStudy"

    #clean database
    cleanDatabase(authorisation, studyName)

    #create standard study
    studyId = createStudyAndUser.create_study(authorisation, studyName, log)

    # Create List of users
    subject_list = ["u1"]
    listOfCreatedSubjects = createStudyAndUser.createSubjects(authorisation, subject_list, studyId, log)

    # delete filterscript
    filterScripts.deleteFilter(authorisation, filterName, log)

    #add a filterscript
    filterScripts.addFilter(authorisation, pythonFile, parameterFile, job_data, log)

    # Create a new user
    cred, userId=createNewUser(authorisation, log)

    listOfFilters =  filterScripts.getListOfFilters(cred, log)

    #Check if list contains everything
    helper.listContaining(listOfFilters,[filterName], "addedFilter mean", log )

    # check detail of filters
    details = {'name': 'right_pupil', 'type': 'String', 'message': None}, {'name': 'left_pupil', 'type': 'String', 'message': None}, {'name': 'columns', 'type': 'InParameter', 'message': None}
    filterScripts.checkFilterDetails(authorisation, "TestPython", details, log)

    #upload a file
    ressourceFile= "./testData/u1@TestStudy@d1.tsv"
    files = [('files',(os.path.basename(ressourceFile), open(ressourceFile, 'rb'), 'application/octet-stream'))]
    url="http://"+setting.httpPrefix+"/cheetah/api/user/uploadFile"
    fileId, fileIdsFull = uploadFilesViaNamingConvention.uploadFilesNaming(authorisation, files, url, log)

    #JSON String that should be sent to the server
    jsonString = "{  \"name\":  \""+filterName+"\",  \"actualParameters\":  {\"timestampcolumn\":  \"EyeTrackerTimestamp\",  \"left_pupil\":  \"PupilLeft\",  \"right_pupil\":  \"PupilRight\"},  \"columns\":  { \"left_pupil\":  \"PupilLeft\",  \"right_pupil\":  \"PupilRight\"}  }"

    #start the filter and retrieve the taskID
    taskId = ownFilterScript(authorisation, fileId, jsonString, log)

    # wait till filters are applied
    helper.waitTilfFinished(authorisation, taskId, log)

    #download the filtered file
    filename = "own"
    fileId = getUserData(authorisation, taskId, log)
    adress =downloadFile(authorisation, fileId, filename, log)

    #Compare the file with a model file
    file.compareMean(adress, ressourceFile, log)

    #remove downloaded file
    os.remove('./logs/' + filename + '.tsv')

    #deletes user
    deleteUser(authorisation, userId, log)

    #deletes filter from server
    filterScripts.deleteFilter(authorisation, filterName, log)

