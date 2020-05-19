import os

import pandas as pd

from help.error import RequirementError


def compareColumns(file1adress, file2adress, column1name, column2name, log):
    fileOne = pd.read_csv(open(file1adress, "r"), delimiter="\t", low_memory=False)

    #fileTwo=csv.reader(open(file2adress, "r"))

    cOne=fileOne[column1name].tolist()
    del fileOne


    filTwo = pd.read_csv(open(file2adress, "r"), delimiter="\t", low_memory=False)
    cTwo=filTwo[column2name].tolist()

    result=""
    del filTwo
    if(len(cOne)!= len(cTwo)):
        log.interlog(__file__, "in comparison of column: " + column1name + " " + column2name, "Error, not same length", "Check")
        exit()
    flag=True
    for i in range(len(cOne)):
        cell2 = cTwo[i]
        cell1 = cOne[i]
        if (not pd.isnull(cell1) and not pd.isnull(cell2)):
            if type(cell1) == str:
                cell1 = cell1.replace(",", ".")
                cell2 = cell2.replace(",", ".")
            one = float(cell1)
            two = float(cell2)
            abs1 = abs(one - two)
            if (abs1 > 0.0002):
                result = result + "|different:" + str(i) + " " + str(abs1) + " " + str(cell1) + " " + str(cell2)
                flag = False
        else:
            if (not pd.isnull(cell1) or not pd.isnull(cell2)):
                result = result + "|One is empty " + str(i)
                flag = False
    filename=os.path.basename(file2adress)
    if (flag):
        log.interlog(__file__, "in comparison of column: " + column1name + " " + column2name +" in file " + filename, "Same, same, not different", "Check")
    else:
        raise RequirementError("Files are not equal")
        log.interlog(__file__, "in comparison of column: " + column1name + " " + column2name +" in file " + filename, result, "Check")
        log.close()

