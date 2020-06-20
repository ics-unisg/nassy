from help import helper
from logger.logg import Logger
from net import uploadFilesViaNamingConvention
from net import tasks
from net import operateOnSubjects
from net import filterScripts
from net import createStudyAndUser
from net import measures
from net import file
import os
from fileoperations import calcStatisticsWithOutBaseline, calcStatisticsWithBaseline
from help import setting

#Data for authorisation
authorisation = {
    'username': 'test.user@test.dcap',
    'password': 'Test'
}
def runFullTest():
    log = Logger("FullTest")

    #Standart name for test study
    studyName = "TestStudy"

    #create log file

    #Clean database with all artefacts of study
    helper.cleanDatabase(authorisation, studyName)

    #Create Study
    studyId= createStudyAndUser.create_study(authorisation, studyName, log)


    #Create List of users
    subject_list=["p001b",  "p024a", "p057a", "p338b","p551a","p741b",]
    listOfCreatedSubjects = createStudyAndUser.createSubjects(authorisation, subject_list, studyId, log)

    #Check if all subjects are registered
    operateOnSubjects.checkAllSubjects(authorisation, subject_list, studyName, log)

    #change name of a subject
    operateOnSubjects.changeSubject(authorisation, listOfCreatedSubjects[0], log)

    #upload two files
    origPath = "/home/uli/masterGit/DatenBarbara/SM/FINAL TEST/"
    file1= "%sp001b@TestStudy@p001b.tsv" % origPath
    file2= "%sp024a@TestStudy@p024a.tsv" % origPath
    file3= "%sp057a@TestStudy@p057a.tsv" % origPath
    file4= "%sp338b@TestStudy@p338b.tsv" % origPath
    file5= "%sp551a@TestStudy@p551a.tsv" % origPath
    file6= "%sp741b@TestStudy@p741b.tsv" % origPath
    url="http://"+setting.httpPrefix+"/cheetah/api/user/uploadFile"
    files = [('files', (os.path.basename(file4), open(file4, 'rb'), 'application/octet-stream')),
             ('files', (os.path.basename(file5), open(file5, 'rb'), 'application/octet-stream')),
             ('files', (os.path.basename(file6), open(file6, 'rb'), 'application/octet-stream'))]
    fileIds1, fileIdsFull= uploadFilesViaNamingConvention.uploadFilesNaming(authorisation, files, url, log)

    files = [('files',(os.path.basename(file1), open(file1, 'rb'), 'application/octet-stream')),
             ('files', (os.path.basename(file2), open(file2, 'rb'), 'application/octet-stream')),
             ('files', (os.path.basename(file3), open(file3, 'rb'), 'application/octet-stream'))
             ]
    fileIds2, fileIdsFull= uploadFilesViaNamingConvention.uploadFilesNaming(authorisation, files, url, log)

    fileIds=fileIds1
    fileIds=fileIds1+fileIds2
    #check if all filters are listed
    listOfFilters =  filterScripts.getListOfFilters(authorisation, log)
    controlList = ["AddLabelFilter","AverageFilter","BlinkDetectionFilter","ButterworthFilter","LinearInterpolationFilter","StandardDeviatonFilter","StatisticFilter","SubstituteGazePointFilter","SubstitutePupilFilter","TEPRFilter"]
    message="listOfFiltersFromTool, ControllList"
    helper.listContaining(listOfFilters, controlList, message, log)

    #check detail of filters
    details = {'name': 'timestampcolumn', 'type': 'String', 'message': None}, {'name': None, 'type': None, 'message': 'The parameter label is optional, if it is not set, the column is named "label"'}, {'name': None, 'type': None, 'message': 'Phases should be a triple with start timestamp, end timestamp, label, seperarated by comma,and those seperated by semiclon'}, {'name': 'label', 'type': 'String', 'message': None}, {'name': 'phases', 'type': 'String', 'message': None}
    filterScripts.checkFilterDetails(authorisation, "AddLabelFilter", details, log)

    #filter files uploaded above. Use standard filter setting
    taskIds= filterScripts.fullFilterData(authorisation, fileIds, log)

    #wait till filters are applied
    helper.waitTilfFinished(authorisation, taskIds, log)

    #get fileids from taks id
    filteredFileIds = tasks.getUserDataFileId(authorisation, taskIds, log)

    #download the file
    filename = "download"
    file.downloadFileAndSearchEyetrackerTimeStamp(authorisation, filteredFileIds[0], filename, log)

    #delete downloaded file
    file.deletFile(filename, log)
    #calculate measures
    baseline0 = ""
    columNames0 = "[\"PupilLeft\",\"PupilRight\"]"
    fileIDs0 = str(filteredFileIds)
    labelColumn0 = "MediaName"
    labels0 = "[]"
    timeStampColumnNAme0 = "EyeTrackerTimestamp"
#    measureTaskIds = measures.calculateMeasures(authorisation, baseline0, columNames0, fileIDs0, labelColumn0, labels0, timeStampColumnNAme0, log)

    #wait till task is finished
#    helper.waitTilfFinished(authorisation, measureTaskIds, log)

    #get id of file (first calculated measure
#    file_id = tasks.getUserDataFileIdAndParent(authorisation, measureTaskIds, log)

    #verify calculated measure
 #   for f in file_id:

 #       print(f[0], f[1])
 #       calcStatisticsWithOutBaseline.compareFromDB("MediaName", {}, f[1],f[0] , log)

    #############

    baseline0 = "TUTX-Q03.png"
    measureTaskIds = measures.calculateMeasures(authorisation, baseline0, columNames0, fileIDs0, labelColumn0, labels0, timeStampColumnNAme0, log)

    #wait till task is finished
    helper.waitTilfFinished(authorisation, measureTaskIds, log)

    #get id of file (first calculated measure
    file_id = tasks.getUserDataFileIdAndParent(authorisation, measureTaskIds, log)

    #verify calculated measure
    for f in file_id:

        print(f[0], f[1])
        #calcStatisticsWithBaseline.compareFromDB("MediaName", {}, f[1],f[0] , "TUTX-Q03.png",log)
        datafile=file.downloadFile(authorisation, f[1], "file"+str(f[1]),log)
        measurfile=file.downloadFile(authorisation, f[0], "measure"+str(f[0]),log)
        calcStatisticsWithBaseline.compareStatistics("MediaName", {}, datafile,measurfile , "TUTX-Q03.png",log)

#runFullTest()


