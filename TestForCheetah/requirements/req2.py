from help import helper
from logger import logg
from net import uploadFilesViaNamingConvention
from net import userdata
from net import createStudyAndUser
from net import file
import os
from help import deletePFile
from net.user import createNewUser, deleteUser, createNewUser2
from help import setting

#Data for authorisation

authorisation0 = {
    'username': 'test.user@test.dcap',
    'password': 'Test'
}

def checkRequirementTwo():

    #Standart name for test study
    studyName = "TestStudy"


    #deletes all entries for studies with given name
    helper.cleanDatabase(authorisation0, studyName)

    #cleans database
    deletePFile.delete()
    #create log file
    log = logg.Logger("Requirement2_", "./logs/")

    #create a new user
    authorisation, userId = createNewUser(authorisation0, log)

    #Create Study
    studyId= createStudyAndUser.create_study(authorisation, studyName, log)


    #subject_list=["p001b",  "p024a"]
    subject_list=["p001b", "p024a"]
    listOfCreatedSubjects = createStudyAndUser.createSubjects(authorisation, subject_list, studyId, log)

    #upload two files
    file1="../dataForTesting/p024a@TestStudy@p024a.tsv"
    file2="../TestForCheetah/testData/p001b@TestStudy@p001b.tsv"
    files = [('files', (os.path.basename(file1), open(file1, 'rb'), 'application/octet-stream')),
             ('files', (os.path.basename(file2), open(file2, 'rb'), 'application/octet-stream'))]
    url="http://"+setting.httpPrefix+"/cheetah/api/user/uploadFile"
    fileId, fileIdsFull= uploadFilesViaNamingConvention.uploadFilesNaming(authorisation, files, url, log)

    #upload movie file
    file2="../dataForTesting/p024a@TestStudy@p024a.webm"
    files = [('files',(os.path.basename(file2), open(file2, 'rb'), 'application/octet-stream'))]
    url="http://"+setting.httpPrefix+"/cheetah/api/user/uploadMovieFile"
    fileId2,  fileIds2Full= uploadFilesViaNamingConvention.uploadFilesNaming(authorisation, files, url, log)


    #select one file
    for e in fileIdsFull:
        if e[1]=="p024a@TestStudy@p024a.tsv":
            idToDownload=e[0]
            break

    #download file to a file named "download"
    filename = "download"
    file.downloadFile(authorisation,idToDownload, filename, log)

    #compares the downloaded two files
    file.fileCompare("download", "../dataForTesting/p024a@TestStudy@p024a.tsv", log)


    #delete downloaded file
    file.deletFile(filename, log)

    #checks if all userdata are here
    list=userdata.checkListOfUserData(authorisation,"p024a@TestStudy@p024a,p001b@TestStudy@p001b", 3, log )


    file3= "../TestForCheetah/testData/u1@TestStudy@d1.tsv"
    files = [('files', (os.path.basename(file3), open(file3, 'rb'), 'application/octet-stream'))]
    url="http://"+setting.httpPrefix+"/cheetah/api/user/uploadFile"
    fileId, fileIdsFull= uploadFilesViaNamingConvention.uploadFilesWithoutNaming(authorisation, files, listOfCreatedSubjects[0],url, log)

    list = userdata.checkListOfUserData(authorisation, "p024a@TestStudy@p024a,p001b@TestStudy@p001b,u1@TestStudy@d1", 4, log)

    #create a new user
    authorisation2, userId2 = createNewUser2(authorisation0, log)

    #checks, if there is only a empty list for the new user
    list0=userdata.checkListOfUserData(authorisation2,"", 0, log )

    #Try to download  file with other credentioal
    file.tryToDownloadFile(authorisation2, idToDownload, log)


    #delete userdata
    userdata.deleteFiles(authorisation, list, log)

    #check if list of userdata is empty
    list=userdata.checkListOfUserData(authorisation,"", 0, log )

    #delete user
    deleteUser(authorisation0, userId, log)
    deleteUser(authorisation0, userId2, log)

#checkRequirementTwo()


