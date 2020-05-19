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

urlFilterList="http://"+setting.httpPrefix+"/cheetah/api/user/listoffilters"


# file with the python code
pythonFile="./testData/trimFilter.py"
# file with the parameters
parameterFile= "./testData/trimFilterParam.param"

def checkRequirementFive():

    log = logg.Logger("Requirement5c_", "./logs/")


    # informations required for login, i.e. the username and the password for a http session
    authorisation = {
        'username': 'test.user@test.dcap',
        'password': 'Test'
    }

    #Name for filter
    filterName = "pythonTrimFilter"

    #data for upload
    job_data = {'name': filterName, 'type': 'trim'}

    #Standart name for test study
    studyName = "TestStudy"

    #clean database
    cleanDatabase(authorisation, studyName)

    #create standard study
    studyId = createStudyAndUser.create_study(authorisation, studyName, log)

    # Create List of users
    subject_list = ["max"]
    createStudyAndUser.createSubjects(authorisation, subject_list, studyId, log)

    # delete filterscript
    filterScripts.deleteFilter(authorisation, filterName, log)

    #add a filterscript
    filterScripts.addFilter(authorisation, pythonFile, parameterFile, job_data, log)

    # Create a new user
    cred, userId=createNewUser(authorisation, log)

    #Get a list of Filters
    listOfFilters =  filterScripts.getListOfFilters(cred, log)

    #Check if list contains everything
    helper.listContaining(listOfFilters,[filterName], "addedFilter mean", log )

    #upload a file
    ressourceFile= "./testData/max@TestStudy@forCustomizedTrim.tsv"
    files = [('files',(os.path.basename(ressourceFile), open(ressourceFile, 'rb'), 'application/octet-stream'))]
    url="http://"+setting.httpPrefix+"/cheetah/api/user/uploadFile"
    fileId, fileIdsFull = uploadFilesViaNamingConvention.uploadFilesNaming(authorisation, files, url, log)

    #JSON String that should be sent to the server
    jsonString = "{  \"name\":  \""+filterName+"\",  \"actualParameters\":  {\"timestampcolumn\":  \"EyeTrackerTimestamp\",  \"label\":  \"mylab\"},  \"columns\":  { \"label\":  \"mylab\"}  }"

    #start the filter and retrieve the taskID
    taskId = ownFilterScript(authorisation, fileId, jsonString, log)

    # wait till filters are applied
    helper.waitTilfFinished(authorisation, taskId, log)


    #download the filtered file
    filename = "trimmed"
    fileId = getUserData(authorisation, taskId, log)
    file.downloadFile(authorisation, fileId, filename, log)

    #Compare the file with a model file
    file.fileCompare(filename, "./testData/trimLabelConfirm.tsv", log)

    #remove downloaded file
    os.remove('./logs/' + filename + '.tsv')

    #deletes user
    deleteUser(authorisation, userId, log)

    #deletes filter from server
    filterScripts.deleteFilter(authorisation, filterName, log)

