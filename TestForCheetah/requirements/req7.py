import os
from help import helper, comparer
from logger import logg
from net import filterScripts, uploadFilesViaNamingConvention, file, createStudyAndUser, tasks
from help.helper import cleanDatabase
from net.file import downloadFile
from net.filterScripts import ownFilterScript
from net.tasks import getUserData
from net.user import createNewUser, deleteUser
from help import setting


def checkRequirementSeven():

    log = logg.Logger("Requirement7_", "./logs/")


    # informations required for login, i.e. the username and the password for a http session
    authorisation = {
        'username': 'test.user@test.dcap',
        'password': 'Test'
    }

    #Standart name for test study
    studyName = "TestStudy"

    #clean database
    cleanDatabase(authorisation, studyName)

    #create standard study
    studyId = createStudyAndUser.create_study(authorisation, studyName, log)

    # Create List of users
    subject_list = ["u1"]
    listOfCreatedSubjects = createStudyAndUser.createSubjects(authorisation, subject_list, studyId, log)

    # Create a new user
    cred, userId=createNewUser(authorisation, log)

    #upload a file
    ressourceFile= "./testData/u1@TestStudy@d1.tsv"
    files = [('files',(os.path.basename(ressourceFile), open(ressourceFile, 'rb'), 'application/octet-stream'))]
    url="http://"+setting.httpPrefix+"/cheetah/api/user/uploadFile"
    id, fileIdsFull = uploadFilesViaNamingConvention.uploadFilesNaming(authorisation, files, url, log)

    #request data for the right pupil, full track
    eyetracker = "EyeTrackerTimestamp"
    values=tasks.getDataForColumn(authorisation, id[0], eyetracker, "PupilRight", log)

    #compare data
    res=comparer.compareDataResultsWithFile(values, ressourceFile, "PupilRight", log)

    #check if data are save against unauthorized requests
    tasks.tryToGetDataForColumn(cred, id[0], eyetracker, "PupilRight", log)

    #request data for the right pupil, timeslot track
    start = 1492762254845918
    end = 1492762254942576
    values, starting, ending =tasks.getDataForColumnAndTimeslot(authorisation, id[0], eyetracker, start, end, "PupilRight", log)

    #compare data
    res = comparer.compareDataResultsWithFileAndTime(values, starting, ending, ressourceFile, start, end, "PupilRight", eyetracker, log)

    #delete user
    deleteUser(authorisation, userId, log)
