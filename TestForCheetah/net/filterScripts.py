import  requests as rq
import json
from help.error import RequirementError
import os
from help import setting


authorisation0 = {
    'username': 'test.user@test.dcap',
    'password': 'Test'
}

url ="http://"+setting.httpPrefix+"/cheetah/login"


def checkFilterDetails(authorisation, filterName, details, logFile):
    with rq.Session() as s:
        s.post(url=url, data=authorisation)
        r = s.get("http://"+setting.httpPrefix+"/cheetah/api/user/filterdetails/"+filterName)
        decode = r.content.decode('UTF-8')
        loads = json.loads(decode)


        filterListe = loads["resBody"]

        for e in details:
            if not e in filterListe:
                logFile.interlog(__file__, "similarity of details", "Details NOT identical " + filterName,
                                 "Check")
                logFile.close()
                raise RequirementError("Details NOT identical " + filterName)

        for e in filterListe:
            if not e in details:
                logFile.interlog(__file__, "similarity of details", "Details NOT identical " + filterName,
                                 "Check")
                logFile.close()
                raise RequirementError("Details NOT identical " + filterName)
        logFile.interlog(__file__, "similarity of details", "Details are identical for " + filterName,
                         "Check")
        return True



        print(details in filterListe)


def getListOfFilters(authorisation, logFile):
    with rq.Session() as s:
        s.post(url=url, data=authorisation)
        r=s.get("http://"+setting.httpPrefix+"/cheetah/api/user/listoffilters")


        decode = r.content.decode('UTF-8')
        loads = json.loads(decode)
        response=loads["resBody"]
        logFile.logHTTPResponse(__file__, "listOfFilters",response)
        return response


s1= "{  " \
"\"files\":  "

s2=",  \"filters\":  [" \
"{  \"name\":  \"SubstitutePupilFilter\",  \"actualParameters\":  {  \"left_pupil\":  \"PupilLeft\",  \"right_pupil\":  \"PupilRight\",\"timestampcolumn\": \"EyeTrackerTimestamp\"},  \"columns\":  {\"left_pupil\":  \"PupilLeft\",  \"right_pupil\":  \"PupilRight\"  }  },  " \
"{  \"name\":  \"SubstituteGazePointFilter\",  \"actualParameters\":  {  \"leftPupilGazeXName\":  \"GazePointLeftX (ADCSpx)\",  \"leftPupilGazeYName\":  \"GazePointLeftY (ADCSpx)\",\"rightPupilGazeXName\":  \"GazePointRightX (ADCSpx)\",\"rightPupilGazeYName\":  \"GazePointRightY (ADCSpx)\",\"timestampcolumn\":  \"EyeTrackerTimestamp\"},  \"columns\":  {}  },  " \
"{  \"name\":  \"BlinkDetectionFilter\",  \"actualParameters\":  {  \"leftPupilGazeXColumnName\":  \"GazePointLeftX (ADCSpx)\",  \"leftPupilGazeYColumnName\":  \"GazePointLeftY (ADCSpx)\",\"rightPupilGazeXColumnName\":  \"GazePointRightX (ADCSpx)\",\"rightPupilGazeYColumnName\":  \"GazePointRightY (ADCSpx)\",  \"blinkDetectionTimeThreashold\":  \"60\",  \"left_pupil\":  \"PupilLeft\",\"right_pupil\":  \"PupilRight\",\"timestampcolumn\":  \"EyeTrackerTimestamp\"},  \"columns\":  {}  },  " \
"{  \"name\":  \"StandardDeviatonFilter\",  \"actualParameters\":  {\"timestampcolumn\":  \"EyeTrackerTimestamp\"},  \"columns\":  {\"left_pupil\":  \"PupilLeft\",  \"right_pupil\":  \"PupilRight\"}  },  " \
"{  \"name\":  \"LinearInterpolationFilter\",  \"actualParameters\":  {  \"left_pupil\":  \"PupilLeft\",  \"right_pupil\":  \"PupilRight\" ,\"timestampcolumn\":  \"EyeTrackerTimestamp\" },  \"columns\":  { \"left_pupil\":  \"PupilLeft\",  \"right_pupil\":  \"PupilRight\"  }  },  " \
"{  \"name\":  \"ButterworthFilter\",  \"actualParameters\":  {  \"hertz\":  \"4\",  \"sampleRate\":  \"300\",\"timestampcolumn\":  \"EyeTrackerTimestamp\"},  \"columns\":  {\"left_pupil\":  \"PupilLeft\",  \"right_pupil\":  \"PupilRight\"}  }  ],  \"decimalSeparator\":  \".\"  }"



def fullFilterData(authorisation, ids, logFile):
    with rq.Session() as s:
        s.post(url=url, data=authorisation)
        dataString=s1+str(ids)+s2
        r = s.post("http://"+setting.httpPrefix+"/cheetah/api/user/filterrequest", data=dataString, headers={'content-type': "application/json"})
        decode = r.content.decode('UTF-8')
        loads = json.loads(decode)
        logFile.logHTTPResponse(__file__, "filter all Files", loads)
        taskIds = loads['resBody']
        print(taskIds)
        return taskIds



nassy=",  \"filters\":  [" \
"{  \"name\":  \"SubstitutePupilFilter\",  \"actualParameters\":  {  \"left_pupil\":  \"PupilLeft\",  \"right_pupil\":  \"PupilRight\",\"timestampcolumn\": \"EyeTrackerTimestamp\"},  \"columns\":  {\"left_pupil\":  \"PupilLeft\",  \"right_pupil\":  \"PupilRight\"  }  },  " \
"{  \"name\":  \"SubstituteGazePointFilter\",  \"actualParameters\":  {  \"leftPupilGazeXName\":  \"GazePointLeftX (ADCSpx)\",  \"leftPupilGazeYName\":  \"GazePointLeftY (ADCSpx)\",\"rightPupilGazeXName\":  \"GazePointRightX (ADCSpx)\",\"rightPupilGazeYName\":  \"GazePointRightY (ADCSpx)\",\"timestampcolumn\":  \"EyeTrackerTimestamp\"},  \"columns\":  {}  },  " \
"{  \"name\":  \"StandardDeviatonFilter\",  \"actualParameters\":  {\"timestampcolumn\":  \"EyeTrackerTimestamp\"},  \"columns\":  {\"left_pupil\":  \"PupilLeft\",  \"right_pupil\":  \"PupilRight\"}  },  " \
"{  \"name\":  \"LinearInterpolationFilter\",  \"actualParameters\":  {  \"left_pupil\":  \"PupilLeft\",  \"right_pupil\":  \"PupilRight\" ,\"timestampcolumn\":  \"EyeTrackerTimestamp\" },  \"columns\":  { \"left_pupil\":  \"PupilLeft\",  \"right_pupil\":  \"PupilRight\"  }  },  " \
"{  \"name\":  \"ButterworthFilter\",  \"actualParameters\":  {  \"hertz\":  \"4\",  \"sampleRate\":  \"300\",\"timestampcolumn\":  \"EyeTrackerTimestamp\"},  \"columns\":  {\"left_pupil\":  \"PupilLeft\",  \"right_pupil\":  \"PupilRight\"}  }  ],  \"decimalSeparator\":  \".\"  }"



def nassyFilterData(authorisation, ids, logFile):
    with rq.Session() as s:
        s.post(url=url, data=authorisation)
        dataString=s1+str(ids)+nassy
        r = s.post("http://"+setting.httpPrefix+"/cheetah/api/user/filterrequest", data=dataString, headers={'content-type': "application/json"})
        decode = r.content.decode('UTF-8')
        loads = json.loads(decode)
        logFile.logHTTPResponse(__file__, "filter all Files", loads)
        taskIds = loads['resBody']
        print(taskIds)
        return taskIds







s3= "{  " \
"\"files\":  "

s4=",  \"filters\":  ["
s5="],  \"decimalSeparator\":  \".\"  }"

subPup = "{  \"name\":  \"SubstitutePupilFilter\",  \"actualParameters\":  {  \"left_pupil\":  \"PupilLeft\",  \"right_pupil\":  \"PupilRight\",\"timestampcolumn\": \"EyeTrackerTimestamp\"},  \"columns\":  {\"left_pupil\":  \"PupilLeft\",  \"right_pupil\":  \"PupilRight\"  }  }"
subGaze = "{  \"name\":  \"SubstituteGazePointFilter\",  \"actualParameters\":  {  \"leftPupilGazeXName\":  \"GazePointLeftX (ADCSpx)\",  \"leftPupilGazeYName\":  \"GazePointLeftY (ADCSpx)\",\"rightPupilGazeXName\":  \"GazePointRightX (ADCSpx)\",\"rightPupilGazeYName\":  \"GazePointRightY (ADCSpx)\",\"timestampcolumn\":  \"EyeTrackerTimestamp\"},  \"columns\":  {}  }"
blink = "{  \"name\":  \"BlinkDetectionFilter\",  \"actualParameters\":  {  \"leftPupilGazeXColumnName\":  \"GazePointLeftX (ADCSpx)\",  \"leftPupilGazeYColumnName\":  \"GazePointLeftY (ADCSpx)\",\"rightPupilGazeXColumnName\":  \"GazePointRightX (ADCSpx)\",\"rightPupilGazeYColumnName\":  \"GazePointRightY (ADCSpx)\",  \"blinkDetectionTimeThreashold\":  \"60\",  \"left_pupil\":  \"PupilLeft\",\"right_pupil\": \"PupilRight\",\"timestampcolumn\":  \"EyeTrackerTimestamp\"},  \"columns\":  {}  }"
stdDev = "{  \"name\":  \"StandardDeviatonFilter\",  \"actualParameters\":  {\"timestampcolumn\":  \"EyeTrackerTimestamp\"},  \"columns\":  {\"left_pupil\":  \"PupilLeft\",  \"right_pupil\":  \"PupilRight\"}  }"
linIn = "{  \"name\":  \"LinearInterpolationFilter\",  \"actualParameters\":  {  \"left_pupil\":  \"PupilLeft\",  \"right_pupil\":  \"PupilRight\"  ,\"timestampcolumn\":  \"EyeTrackerTimestamp\"},  \"columns\":  { \"left_pupil\":  \"PupilLeft\",  \"right_pupil\":  \"PupilRight\"  }  }"
butter = "{  \"name\":  \"ButterworthFilter\",  \"actualParameters\":  {  \"hertz\":  \"4\",  \"sampleRate\":  \"300\",\"timestampcolumn\":  \"EyeTrackerTimestamp\"},  \"columns\":  {\"left_pupil\":  \"PupilLeft\",  \"right_pupil\":  \"PupilRight\"}  }"

def filterData(authorisation, ids, filter, logFile):
    with rq.Session() as s:
        fil=""
        if filter =="subPup":
            fil=subPup
        elif filter =="subGaze":
            fil=subGaze
        elif filter =="blink":
            fil=blink
        elif filter =="stdDev":
            fil=stdDev
        elif filter =="linIn":
            fil=linIn
        elif filter =="butter":
            fil=butter
        else:
            raise RequirementError("Wrong filter")
        s.post(url=url, data=authorisation)
        dataString=s3+str(ids)+s4+fil+s5
        r = s.post("http://"+setting.httpPrefix+"/cheetah/api/user/filterrequest", data=dataString, headers={'content-type': 'application/json'})
        decode = r.content.decode('UTF-8')
        loads = json.loads(decode)
        logFile.logHTTPResponse(__file__, "filter all Files with " +filter, loads)
        taskIds = loads['resBody']
        print(taskIds)
        return taskIds



def trimFilterData(authorisation, ids, logFile):
    trim = "{  \"name\":  \"TrimFilter\",  \"actualParameters\":  {\"timestampcolumn\":  \"EyeTrackerTimestamp\",  \"labelcolumn\": \"\", \"label\":  \"\",  \"start\": 1492762254832585, \"end\": 1492762254879250},  \"columns\":  {}  }"
    with rq.Session() as s:

        s.post(url=url, data=authorisation)
        dataString=s3+str(ids)+s4+trim+s5
        r = s.post("http://"+setting.httpPrefix+"/cheetah/api/user/filterrequest", data=dataString, headers={'content-type': 'application/json'})
        decode = r.content.decode('UTF-8')
        loads = json.loads(decode)
        logFile.logHTTPResponse(__file__, "trim files", loads)
        taskIds = loads['resBody']
        print(taskIds)
        return taskIds


label = "{  \"name\":  \"AddLabelFilter\",  \"actualParameters\":  {\"timestampcolumn\":  \"EyeTrackerTimestamp\",  \"label\": \"mylab\",  " \
        "\"phases\":  \"1492762254832585, 1492762254865916, l1; 1492762254932564, 1492762254999228, l2 \"} }"


def labelFilterData(authorisation, ids, logFile):
    with rq.Session() as s:

        s.post(url=url, data=authorisation)
        dataString=s3+str(ids)+s4+label+s5
        r = s.post("http://"+setting.httpPrefix+"/cheetah/api/user/filterrequest", data=dataString, headers={'content-type': 'application/json'})
        decode = r.content.decode('UTF-8')
        loads = json.loads(decode)
        logFile.logHTTPResponse(__file__, "label files", loads)
        taskIds = loads['resBody']
        print(taskIds)
        return taskIds


trimLabel = "{  \"name\":  \"TrimFilter\",  \"actualParameters\":  {\"timestampcolumn\":  \"EyeTrackerTimestamp\",  \"labelcolumn\": \"mylab\", \"label\":  \"l1;l2\",  \"start\": null, \"end\": null},  \"columns\":  {}  }"
def trimLabelFilterData(authorisation, ids, logFile):
    with rq.Session() as s:

        s.post(url=url, data=authorisation)
        dataString=s3+"["+str(ids)+"]"+s4+trimLabel+s5
        r = s.post("http://"+setting.httpPrefix+"/cheetah/api/user/filterrequest", data=dataString, headers={'content-type': 'application/json'})
        decode = r.content.decode('UTF-8')
        loads = json.loads(decode)
        logFile.logHTTPResponse(__file__, "trim label files", loads)
        taskIds = loads['resBody']
        print(taskIds)
        return taskIds

def ownFilterScript(authorisation, fileId, jsonString, logFile):
    with rq.Session() as s:
        s.post(url=url, data=authorisation)
        dataString = s3 + str(fileId) + s4 + jsonString + s5
        r = s.post("http://"+setting.httpPrefix+"/cheetah/api/user/filterrequest", data=dataString,
                   headers={'content-type': 'application/json'})
        decode = r.content.decode('UTF-8')
        loads = json.loads(decode)
        logFile.logHTTPResponse(__file__, "trim label files", loads)
        taskIds = loads['resBody']
        print(taskIds)
        return taskIds

def doFilterData(authorisation, jsonString, fileId, name, log):
    with rq.Session() as s:
        s.post(url=url, data=authorisation)
        dataString = s3 + str(fileId) + s4 + jsonString + s5
        r = s.post("http://"+setting.httpPrefix+"/cheetah/api/user/filterrequest", data=dataString,
                   headers={'content-type': 'application/json'})
        decode = r.content.decode('UTF-8')
        loads = json.loads(decode)
        log.logHTTPResponse(__file__, " filter file for "+name, loads)
        taskIds = loads['resBody']
        print(taskIds)
        return taskIds

def checkIfFinished(s, taskId):
    task_id = "http://"+setting.httpPrefix+"/cheetah/api/user/taskFinished/" + taskId
    print(task_id)
    # print(task_id)
    resp = s.get(task_id)
    ret = json.loads(resp.content)['resBody']
    print(ret)
    return ret


def addFilter(authorisation, pythonFile, parameterFile, job_data, logFile):
    # list of files that should be uploaded
    files = [('files', (os.path.basename(pythonFile), open(pythonFile, 'rb'), 'application/octet-stream')),
             ('files', (os.path.basename(parameterFile), open(parameterFile, 'rb'), 'application/octet-stream'))]


    # http adress of the python upload
    urlUpload = "http://"+setting.httpPrefix+"/cheetah/api/admin/uploadpython"

    with rq.Session() as s:
        # logs in with the credentials
        s.post(url=url, data=authorisation)
        # make a post request to upload the file
        r = s.post(urlUpload, data=job_data, files=files)

        # extracts the statuscode of the response
        decode = r.content.decode('UTF-8')
        loads = json.loads(decode)
        statusCode = loads['statusCode']

        # checks if it is the right statuscode
        if not (statusCode == 0):
            logFile.logHTTPResponse(__file__, "python code NOT successful added", loads)
            logFile.close()
            raise RequirementError(loads)
        logFile.logHTTPResponse(__file__, "python code successful added", loads)


def deleteFilter(authorisation, name, logFile):
    urlUpload = "http://"+setting.httpPrefix+"/cheetah/api/admin/deletepython/"+name

    with rq.Session() as s:
        # logs in with the credentials
        s.post(url=url, data=authorisation)
        # make a post request to upload the file
        r = s.delete(urlUpload)
        decode = r.content.decode('UTF-8')
        loads = json.loads(decode)
        statusCode = loads['statusCode']

        # checks if it is the right statuscode
        if not (statusCode == 0):
            logFile.logHTTPResponse(__file__, "python code NOT successful deleted", loads)
            logFile.close()
            raise RequirementError("python code NOT successful added")
        logFile.logHTTPResponse(__file__, "python code successful deleted", loads)


"""
        # requests the list of all filters
        list = s.get(urlFilterList)

        # extracts the list of all files of the response
        filterList = list.content.decode('UTF-8')
        filters = json.loads(filterList)
        myList = filters["resBody"]

        # checks, if the name is in the list
        if not job_data["name"] in myList:
            print("Filter not in list")
            exit

        status = "the code was uploaded."
        if (statusCode == 105):
            status = "the code is already on the server."
        print("Test successful, " + status)

#loggerDumg = Logger();

#details = {'name': 'timestampcolumn', 'type': 'String', 'message': None}, {'name': None, 'type': None, 'message': 'The parameter label is optional, if it is not set, the column is named "label"'}, {'name': None, 'type': None, 'message': 'Phases should be a triple with start timestamp, end timestamp, label, seperarated by comma,and those seperated by semiclon'}, {'name': 'label', 'type': 'String', 'message': None}, {'name': 'phases', 'type': 'String', 'message': None}

#checkFilterDetails(authorisation0, "AddLabelFilter", details, loggerDumg)"""