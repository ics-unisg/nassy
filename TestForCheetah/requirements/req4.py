import os

from fileoperations import fileEquality
from help import helper
from logger import logg
from net import createStudyAndUser
from net import file
from net import filterScripts
from net import uploadFilesViaNamingConvention
# Data for authorisation
from net.tasks import getUserData
from help import setting

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

    #os.remove('./logs/' + filename + '.tsv')

def checkRequirementFour():
    #create log file
    log = logg.Logger("Requirement4_", "./logs/")

    #Standart name for test study
    studyName = "TestStudy"

    #Clean database with all artefacts of study
    helper.cleanDatabase(authorisation, studyName)

    #Create Study
    studyId= createStudyAndUser.create_study(authorisation, studyName, log)


    #Create List of users
    subject_list=["max"]
    listOfCreatedSubjects = createStudyAndUser.createSubjects(authorisation, subject_list, studyId, log)


    #upload a files
    file1= "./testData/max@TestStudy@1.tsv"
    files = [('files',(os.path.basename(file1), open(file1, 'rb'), 'application/octet-stream'))]
    url="http://"+setting.httpPrefix+"/cheetah/api/user/uploadFile"
    fileIds, fileIdsFull= uploadFilesViaNamingConvention.uploadFilesNaming(authorisation, files, url, log)

    forWait=""

    #filter files uploaded above. Use standard filter setting, parameter set in function
    trim= filterScripts.trimFilterData(authorisation, fileIds, log)
    forWait=trim


    #wait till filters are applied
    helper.waitTilfFinished(authorisation, forWait, log)

    #download trimmed file
    filename = "trimmed"
    fileId = getUserData(authorisation, trim, log)
    file.downloadFile(authorisation, fileId, filename, log)

    #compare trimmed file with example-file
    file.fileCompare(filename, "./testData/trimConfirm.tsv", log)

    #delete file after comparison
    os.remove('./logs/' + filename + '.tsv')

    #label file, used parameters described in funciotn
    label = filterScripts.labelFilterData(authorisation, fileIds, log)
    forWait = label

    # wait till filters are applied
    helper.waitTilfFinished(authorisation, forWait, log)


    #download labeled file
    filename = "labeled"
    fileIdLabeled = getUserData(authorisation, label, log)
    file.downloadFile(authorisation, fileIdLabeled, filename, log)

    #compare labeled file with testfile
    file.fileCompare(filename, "./testData/labelConfirm.tsv", log)

    #delete file after comparison
    os.remove('./logs/' + filename + '.tsv')

    #trims the labeled file
    trimLabel = filterScripts.trimLabelFilterData(authorisation, fileIdLabeled, log)
    forWait = trimLabel

    # wait till filters are applied
    helper.waitTilfFinished(authorisation, forWait, log)

    #downloades the trimmed and labeld
    filename = "trimLabeled"
    fileId = getUserData(authorisation, trimLabel, log)
    file.downloadFile(authorisation, fileId, filename, log)

    #compares the labeld and trimmed file with a model
    file.fileCompare(filename, "./testData/trimLabelConfirm.tsv", log)

    #delete file after comparison
    os.remove('./logs/' + filename + '.tsv')

