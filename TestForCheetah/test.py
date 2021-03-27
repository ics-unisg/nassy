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
def analyze(df):
    stream = io.StringIO()
    df.to_csv(stream, sep='\t')
    studyName = "TestStudy"
    authorisation = {
        'username': 'admin@administrator.dcap',
        'password': 'RQlHQuF'
    }
    helper.cleanDatabase(authorisation, studyName)
    log = logg.Logger("test_", "./logs/")
    studyId = createStudyAndUser.create_study(authorisation, studyName, log)
    subject_list = ["p07"]
    listOfCreatedSubjects = createStudyAndUser.createSubjects(authorisation, subject_list, studyId, log)
    operateOnSubjects.changeSubject(authorisation, listOfCreatedSubjects[0], log)

    files = [('files', ('file.tsv', stream.getvalue(), 'application/octet-stream'))]
    url = "http://"+setting.httpPrefix+"/cheetah/api/user/uploadFile"
    fileIds, fileIdsFull = uploadFilesViaNamingConvention.uploadFilesWithoutNaming(authorisation, files, listOfCreatedSubjects[0], url, log)
    measureTaskIds = nassyFilterData(authorisation, fileIds, log)
    helper.waitTilfFinished(authorisation, measureTaskIds, log)
    measureFileId = tasks.getUserDataFileId(authorisation, measureTaskIds, log)[0]
    return file.downloadAndReadFile(authorisation, measureFileId, "full", log).decode("utf-8")

df = pd.read_csv(sys.stdin)
baseline = df[df['type'] == 'B']
experiment = df[df['type'] == 'M']




#download the measure file
print(analyze(baseline))
print("\n".join(analyze(experiment).split("\n")[1:]))



#cu.deleteUserForTest()
