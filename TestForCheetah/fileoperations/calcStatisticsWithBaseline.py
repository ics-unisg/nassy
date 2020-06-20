
import pandas as pd
import numpy as np
import statistics as stati
import scipy.stats as stats
import mysql.connector as mc
from help import setting
from termcolor import colored
from help.error import RequirementError


def correctBaseLine(array, baselineValue):
    retourArray = []
    for i in array:
        retourArray.append(i - baselineValue)
    return retourArray


def createAverageArray(dictOne, dictTwo):
    retourDict = {}
    for e in dictOne:
        retourArray = []
        arrayOne = dictOne[e]
        arrayTwo = dictTwo[e]
        for i in range(len(arrayOne)):
            retourArray.append((arrayOne[i] + arrayTwo[i]) / 2)
        retourDict[e] = retourArray
    return retourDict


prefixCorrected = "baselineCorrected"
prefixMeanValue = "_meanValueOfTimeSlot_"
prefixLeft = "PupilLeft_"
prefixRight = "PupilRight_"


def checkEquality(fileToCheck, name, valueToCheck, log, everythinFineFlag):
    column = fileToCheck[name]
    valueZero = column[0]
    valueToConfirm = float(valueZero.replace(",", "."))
    errorValue = valueToCheck - valueToConfirm
    if np.abs(errorValue) > 0.0002:
        message="Error" +"  "+ str(errorValue)+"  "+ str(valueToCheck)+"  "+ str(valueToConfirm)+"  "+ name
        print(colored(message, "red"))
        log.interlog(__file__, "checkSimilarity", "Error in Equality: "+ name, "Equality, " + str(errorValue)+ " "+ str(valueToCheck)+ " "+ str(valueToConfirm))
        everythinFineFlag=False
    print(name+": from server "+ str(valueToConfirm) +", cacluated in Python: "+str(valueToCheck) + ", difference: "+ str(errorValue) )
    return everythinFineFlag



def createLabels(file, labelColumName):
    myReader = pd.read_csv(file, delimiter="\t")
    labelSet=set()
    labels = myReader[labelColumName]
    for i in labels:
        if type(i) is str and i.strip() != "":
            labelSet.add(i)
    return labelSet



def compareStatistics(labelColumName, labelBuchstaben, file, file1, base, log):
    labelListLeft = dict()
    labelListRight = dict()
    if not len(labelBuchstaben)>0:
        labelBuchstaben=createLabels(file, labelColumName)
    labelBuchstaben.remove(base)
    for b in labelBuchstaben:
        if not b ==base:
            labelListLeft[b] = []
            labelListRight[b] = []
    baseLineValuesLeft = []
    baseLineValuesRight = []



    with open(file) as tsvFile:
        myReader = pd.read_csv(file, delimiter="\t")
        left = myReader["PupilLeft"]
        right = myReader["PupilRight"]
        label = myReader[labelColumName]
        for i in range(len(left)):
            if not pd.isnull(left.get(i)):
                try:
                    resLeft = float(left.get(i).replace(",", "."))
                except AttributeError:
                    resLeft = float(left.get(i))
                try:
                    resRight = float(right.get(i).replace(",", "."))
                except AttributeError:
                    resRight = float(right.get(i))
                actualLabelValue = label.get(i)
                #            print(resLeft, resRight, actualLabelValue)
                if (actualLabelValue in labelBuchstaben):
                    labelListLeft[actualLabelValue].append(resLeft)
                    labelListRight[actualLabelValue].append(resRight)
                elif (actualLabelValue == base):
                    baseLineValuesLeft.append(resLeft)
                    baseLineValuesRight.append(resRight)

        #    print(labelListLeft)
        #    print(baseLineValuesLeft)

        average = np.mean
        meanLeft = average(baseLineValuesLeft)
        print("baseline left", meanLeft)
        meanRight = average(baseLineValuesRight)
        print("baseline right", meanRight)
        meanBoth = average([meanLeft, meanRight])
        print("baseline general", meanBoth)
        averageList = createAverageArray(labelListLeft, labelListRight)

        statistics = [("average", average), ("median", np.median), ("max", np.max), ("min", np.min),
                      ("standardDeviatiom", np.std), ("standardError", stats.sem)]

        everythinFineFlag =True

        for a in labelBuchstaben:
            with open(file1) as checkFile:
                fileToCheck = pd.read_csv(checkFile, delimiter=";", dtype=str)
                for methodName, method in statistics:
                    everythinFineFlag = checkEquality(fileToCheck, methodName + "_PupilRight_" + a, method(labelListRight[a]), log, everythinFineFlag)
                    everythinFineFlag = checkEquality(fileToCheck, methodName + "_PupilLeft_" + a, method(labelListLeft[a]), log, everythinFineFlag)
                    everythinFineFlag = checkEquality(fileToCheck, methodName + "_meanValueOfTimeSlot_" + a, method(averageList[a]), log, everythinFineFlag)
                    everythinFineFlag = checkEquality(fileToCheck, "baselineCorrected_" + base + "_" + methodName + "_PupilLeft_" + a,
                                  method(correctBaseLine(labelListLeft[a], meanLeft)), log, everythinFineFlag)
                    everythinFineFlag = checkEquality(fileToCheck, "baselineCorrected_" + base + "_" + methodName + "_PupilRight_" + a,
                                  method(correctBaseLine(labelListRight[a], meanRight)), log, everythinFineFlag)
                    everythinFineFlag = checkEquality(fileToCheck,
                                  "baselineCorrected_" + base + "_" + methodName + "_meanValueOfTimeSlot_" + a,
                                  method(correctBaseLine(averageList[a], meanBoth)), log, everythinFineFlag)

        log.interlog(__file__, "checkSimilarity", "The files are equal: "+ str(everythinFineFlag), "Equality")
        if  not everythinFineFlag:
            log.close()
            raise RequirementError("Files are not equal")

def compareFromDB(labelColumName,labelBuchstaben, file, file1,base, log):
    dbUser = input("Enter user database User: ")
    dbPassword = input("Enter user password: ")
    schemaName= input("Enter schema name: ")


    connection = mc.connect(host=setting.hostName,
                            user=dbUser,
                            passwd=dbPassword,
                            db=schemaName)
    cur = connection.cursor()
    cur.execute('SELECT path FROM user_data where pk_user_data ='+str(file))
    path=cur.fetchone()

    pathToFileOne= setting.getPathToFiles(str(path[0])[1:])

    cur.execute('SELECT path FROM user_data where pk_user_data =' + str(file1))
    path = cur.fetchone()

    pathToFileTwo = setting.getPathToFiles(str(path[0])[1:])

    compareStatistics(labelColumName,labelBuchstaben, pathToFileOne, pathToFileTwo, base, log)

