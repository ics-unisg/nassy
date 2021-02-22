import os

path = "does not exist"
studyName="TestStudy"

files=os.listdir(path)

def rename(f, name):
    newName = name[0] + "@" + studyName + "@" + name[0] + ".tsv"
    os.rename(path+f, path+newName)

def renameFiles():
    for file in files:
        if file.endswith("tsv"):
            name=file.split(".")
            #print(name[0])
            rename(file, name)

def getSubjects():
    for file in files:
        if file.endswith("tsv"):
            name=file.split("@")
            #print(name[0])


getSubjects()