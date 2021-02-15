import os
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
    'username': 'test.user@test.dcap',
    'password': 'Test'
}
helper.cleanDatabase(authorisation, studyName)

log = logg.Logger("test_", "./logs/")
studyId = createStudyAndUser.create_study(authorisation, studyName, log)


subject_list = ["p06"]
listOfCreatedSubjects = createStudyAndUser.createSubjects(authorisation, subject_list, studyId, log)
operateOnSubjects.changeSubject(authorisation, listOfCreatedSubjects[0], log)

file1 = "./testData/p06@TestStudy@file.tsv"
files = [('files', (os.path.basename(file1), open(file1, 'rb'), 'application/octet-stream'))]
url = "http://"+setting.httpPrefix+"/cheetah/api/user/uploadFile"

print("========================")
fileIds, fileIdsFull = uploadFilesViaNamingConvention.uploadFilesNaming(authorisation, files, url, log)

print(fileIds)


#calculate measures
measureTaskIds = nassyFilterData(authorisation, fileIds, log)

# wait till task is finished
helper.waitTilfFinished(authorisation, measureTaskIds, log)

# get id of file (first calculated measure
measureFileId = tasks.getUserDataFileId(authorisation, measureTaskIds, log)[0]

#download the measure file
path = file.downloadFile(authorisation, measureFileId, "full", log)

#verify the downloaded statistics, for statistics without baseline
#calcStatisticsWithOutBaseline.compareStatistics("MediaName", {}, file1, path, log)
print(path)





#cu.deleteUserForTest()