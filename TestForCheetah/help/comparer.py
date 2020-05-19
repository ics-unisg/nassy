from help import error
import pandas as pd

def valueCompare(x,y):
    if(x!=y):
        raise error.RequirementError("Wrong values")
    return x==y


def compareDataResults(listOne, listTwo, log):
    res = map(lambda x,y: valueCompare(x,y), listOne,listTwo)
    log.interlog(__file__, "list compare", "Lists are indentical.","Check")
    return res

def compareDataResultsWithFile(values, ressourceFile, column_name, log):
    file =  pd.read_csv(ressourceFile, delimiter="\t")
    return compareDataResults(values, list(file[column_name]), log)


def cuter(start, end, x, y):
        if float(x) >= start and float(x) <= end:
            return y
        pass

def compareDataResultsWithFileAndTime(values, starting, ending, ressourceFile, start, end, column_name, eytrackerTS, log):
    if start!=float(starting):
        log.interlog(__file__, "list compare", "Different start.", "Check")
        log.close()
        raise error.RequirementError("Different start", start, starting)
    if end!=float(ending):
        log.interlog(__file__, "list compare", "Different ending.", "Check")
        log.close()
        raise error.RequirementError("Different ending", end, ending)
    file = pd.read_csv(ressourceFile, delimiter="\t")
    column=file[column_name]
    timeStamp = file[eytrackerTS]



    res = list(map(lambda x, y: cuter(start, end, x, y), timeStamp, column))
    res = [i for i in res if i]
    return compareDataResults(values, res, log)



