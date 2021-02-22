import pandas as pd
import statistics as st
import math

name1="p741b@TestStudy@p741b@filtered(4).tsv"
name2="p024a@TestStudy@p024a@filtered.tsv"
file1 = open("../data/"+name1, "r")

one = pd.read_csv(file1, delimiter="\t", low_memory=False)


mnOne = one["MediaName"]
leftOne = one["PupilLeft"]

oneValues=[]
for i in range(len(mnOne)):
    if mnOne[i]=="M4B-Q20.png":
        v = leftOne[i]
        if type(v) == float and math.isnan(v):
            continue
        oneValues.append(v)



# file2 = open("does not exist"+name2, "r")
# two= pd.read_csv(file2, delimiter="\t", low_memory=False)
# mnTwo= two["MediaName"]
# leftTwo= two["PupilLeft"]
#
# twoValues=[]
# for i in range(len(mnTwo)):
#     if mnTwo[i]=="M1B-Q01.png":
#         twoValues.append(leftTwo[i])



#print(st.mean(oneValues))
##print(twoValues)