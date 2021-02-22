#!/usr/bin/python3
import os
import sys
import net.createStudyAndUser as cu
from logger import logg
from net import createStudyAndUser, uploadFilesViaNamingConvention, filterScripts, file, tasks, measures, operateOnSubjects
from fileoperations import calcStatisticsWithBaseline, calcStatisticsWithOutBaseline
from net.filterScripts import nassyFilterData
from help import helper
from help import setting

#cu.createUserForTest()

studyName = "TestStudy"
authorisation = {
    'username': 'admin@administrator.dcap',
    'password': 'lNvpjsZ'
}
helper.cleanDatabase(authorisation, studyName)

log = logg.Logger("test_", "./logs/")
studyId = createStudyAndUser.create_study(authorisation, studyName, log)


subject_list = ["p07"]
listOfCreatedSubjects = createStudyAndUser.createSubjects(authorisation, subject_list, studyId, log)
operateOnSubjects.changeSubject(authorisation, listOfCreatedSubjects[0], log)

file1 = sys.argv[1].replace('//', '/')

files = [('files', (os.path.basename(file1), open(file1, 'rb'), 'application/octet-stream'))]
url = "http://"+setting.httpPrefix+"/cheetah/api/user/uploadFile"
fileIds, fileIdsFull = uploadFilesViaNamingConvention.uploadFilesWithoutNaming(authorisation, files, listOfCreatedSubjects[0], url, log)


#calculate measures
measureTaskIds = nassyFilterData(authorisation, fileIds, log)

# wait till task is finished
helper.waitTilfFinished(authorisation, measureTaskIds, log)

# get id of file (first calculated measure
measureFileId = tasks.getUserDataFileId(authorisation, measureTaskIds, log)[0]

#download the measure file
print(file.downloadAndReadFile(authorisation, measureFileId, "full", log).decode("utf-8"))





#cu.deleteUserForTest()