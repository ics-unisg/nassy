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
import io

import pandas as pd

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


df = pd.read_csv(sys.stdin)
stream = io.StringIO()
df.to_csv(stream, sep='\t')

files = [('files', ('file.tsv', stream.getvalue(), 'application/octet-stream'))]
url = "http://"+setting.httpPrefix+"/cheetah/api/user/uploadFile"
fileIds, fileIdsFull = uploadFilesViaNamingConvention.uploadFilesWithoutNaming(authorisation, files, listOfCreatedSubjects[0], url, log)
measureTaskIds = nassyFilterData(authorisation, fileIds, log)
helper.waitTilfFinished(authorisation, measureTaskIds, log)
measureFileId = tasks.getUserDataFileId(authorisation, measureTaskIds, log)[0]

#download the measure file
print(file.downloadAndReadFile(authorisation, measureFileId, "full", log).decode("utf-8"))



#cu.deleteUserForTest()