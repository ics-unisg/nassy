import pandas as pd
import math

#file1adress="../data/P10@cph@IdeaReductionThesis 1_P10_prepared@filtered(4).tsv"
file1adress="./logs/download.tsv"
file2adress="./logs/p001b@toConfirm.tsv"

column1name="PupilLeft"
cclumn2name="PupilLeft"

#print("load first file")
fileOne = pd.read_csv(open(file1adress, "r"), delimiter="\t", low_memory=False)

#fileTwo=csv.reader(open(file2adress, "r"))

cOne=fileOne[column1name].tolist()
del fileOne


#print("load second file")
filTwo = pd.read_csv(open(file2adress, "r"), delimiter="\t", low_memory=False)
cTwo=filTwo[cclumn2name].tolist()
#print(len(cOne))

del filTwo
if(len(cOne)!= len(cTwo)):
    #print("Error, not same length")
    exit()

flag=True
for i in range(len(cOne)):
    cell2 = cTwo[i]
    cell1 = cOne[i]
    if(i%1000==0):
        #print("1000 geschafft....", str(i/1000))

    if(not pd.isnull(cell1) and not pd.isnull(cell2)):
        one = float(cell1)
        two = float(cell2)
        abs1 = abs(one - two)
        if(abs1 >0.0002):
            #print("we are different ", i, abs1, cell1, cell2)
            flag=False
    else:
        if (not pd.isnull(cell1) or not pd.isnull(cell2)):
            #print("One is empty ", i, cell1, cell2)
            flag=False
if(flag):
    #print("Same, same, not different")
else:
    #print("BUHUHUHU")
