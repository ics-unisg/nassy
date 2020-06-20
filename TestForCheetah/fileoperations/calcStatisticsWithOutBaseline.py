
from termcolor import colored

import pandas as pd
import numpy as np
import statistics as stati
import scipy.stats as stats
import mysql.connector as mc

from help.error import RequirementError
from logger.loggerDumb import Logger
from help import setting


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


def checkEquality(fileToCheck, name, valueToCheck, log, everythingFineFlag):
    column = fileToCheck[name]
    try:
        replacedValue = column[0].replace(",", ".")
    except AttributeError:
        replacedValue=0
    valueToConfirm = float(replacedValue)
    errorValue = valueToCheck - valueToConfirm
    if np.abs(errorValue) > 0.0001:
        alarm = str("Error"+ "  " + str(errorValue) +"  " +str(valueToCheck) + "  " + str(valueToConfirm) + "  " + name)
        print(colored(alarm, 'red'))
        log.interlog(__file__, "checkSimilarity", "Error in Equality: " + name, "Equality")
        everythingFineFlag = False
    print(name, valueToCheck, valueToConfirm)
    return everythingFineFlag

def createLabels(file, labelColumName):
    myReader = pd.read_csv(file, delimiter="\t")
    labelSet=set()
    labels = myReader[labelColumName]
    for i in labels:
        if type(i) is str and i.strip() != "":
            labelSet.add(i)
    return labelSet



def compareStatistics(labelColumName, labelBuchstaben, fileFromDatabase, measureFile, log):
    labelListLeft = dict()
    labelListRight = dict()
    if not len(labelBuchstaben)>0:
        labelBuchstaben=createLabels(fileFromDatabase, labelColumName)
    for b in labelBuchstaben:
        labelListLeft[b] = []
        labelListRight[b] = []




    with open(fileFromDatabase) as tsvFile:
        myReader = pd.read_csv(fileFromDatabase, delimiter="\t", low_memory=False)
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
                if (actualLabelValue in labelBuchstaben):
                    labelListLeft[actualLabelValue].append(resLeft)
                    labelListRight[actualLabelValue].append(resRight)


        average = np.mean
        averageList = createAverageArray(labelListLeft, labelListRight)
        statistics = [("average", average), ("median", np.median), ("max", np.max), ("min", np.min),
                      ("standardDeviatiom", stati.stdev), ("standardError", stats.sem)]

        everythinFineFlag = True

        for a in labelBuchstaben:
            with open(measureFile) as checkFile:
                fileToCheck = pd.read_csv(checkFile, delimiter=";")
                for methodName, method in statistics:
                    try:
                        valueToCheckRight = method(labelListRight[a])
                    except AssertionError:
                        valueToCheckRight = 0
                    everythinFineFlag = checkEquality(fileToCheck, methodName + "_PupilRight_" + a, valueToCheckRight, log, everythinFineFlag)
                    try:
                        valueToCheckLeft = method(labelListLeft[a])
                    except AssertionError:
                        valueToCheckLeft = 0
                    everythinFineFlag = checkEquality(fileToCheck, methodName + "_PupilLeft_" + a, valueToCheckLeft, log, everythinFineFlag)
                    try:
                        valueToCheckAverage = method(averageList[a])
                    except AssertionError:
                        valueToCheckAverage = 0
                    everythinFineFlag = checkEquality(fileToCheck, methodName + "_meanValueOfTimeSlot_" + a,
                                                      valueToCheckAverage, log, everythinFineFlag)

    log.interlog(__file__, "checkSimilarity", "The files are equal: " + str(everythinFineFlag), "Equality")
    if not everythinFineFlag:
        log.close()
        raise RequirementError("Files are not equal")


def compareFromDB(labelColumName, labelBuchstaben, dataFile, measures, log):
    dbUser = input("Enter user database User: ")
    dbPassword = input("Enter user password: ")

    connection = mc.connect(host=setting.hostName,
                            user=dbUser,
                            passwd=dbPassword,
                            db=setting.schemaName)
    cur = connection.cursor()
    cur.execute('SELECT path FROM user_data where pk_user_data =' + str(dataFile))
    path=cur.fetchone()

    fileFromDatabase= setting.getPathToFiles(str(path[0])[1:])

    cur.execute('SELECT path FROM user_data where pk_user_data =' + str(measures))
    path = cur.fetchone()

    measureFile = setting.getPathToFiles(str(path[0])[1:])

    compareStatistics(labelColumName,labelBuchstaben, fileFromDatabase, measureFile, log)

def compareWithFile(labelColumName,labelBuchstaben, fileList, nameColumn, OrigPath, log):
    dbUser = input("Enter user database User: ")
    dbPassword = input("Enter user password: ")

    connection = mc.connect(host=setting.hostName,
                            user=dbUser,
                            passwd=dbPassword,
                            db=setting.schemaName)

    for file in fileList:
        cur = connection.cursor()
        cur.execute('SELECT path FROM user_data where pk_user_data =' + str(file))
        path = cur.fetchone()

        pathToMeasureFile = setting.getPathToFiles(str(path[0])[1:])
        fileName=""
        with open(pathToMeasureFile) as getNameFile:
            fileName = pd.read_csv(getNameFile, delimiter=";", low_memory=False)[nameColumn][0]

        compareStatistics(labelColumName,labelBuchstaben, OrigPath+fileName, pathToMeasureFile, log)



#dumb = Logger()
#compareStatistics("MediaName", {}, "../testData/p024@TestStudy@fullFiltered.tsv", "../logs/measures.tsv", dumb)
#dumb = Logger()

#compareFromDB("MediaName", {}, 21360, 21361, dumb)
#compareFromDB("label",{}, 21161, 21336, dumb)

