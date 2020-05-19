import json


class myPythonClass():
    def execute(self, data, param, col):
        daten = json.loads(data)
        parameters = json.loads(param)
        columns = json.loads(col)

        label = daten[parameters["label"]]


        toKeep =[]

        for i in range(0, len(label)):
           if label[i] !=None and label[i].strip()!="":
               toKeep.append(1)
           else:
               toKeep.append(0)


        return json.dumps(toKeep)
