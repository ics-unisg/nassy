import json


class myPythonClass():
    def execute(self, data, param, col):
        daten = json.loads(data)
        parameters = json.loads(param)
        columns = json.loads(col)

        lefts = daten[parameters["left_pupil"]]

        leftsFloat = [float(i.replace(",", ".")) for i in lefts]


        for i in range(0, len(leftsFloat)):
            leftsFloat[i] = leftsFloat[i]-10

        leftsFloat = [str(i).replace(".", ",") for i in leftsFloat]

        resultingColumnsAsDic = {parameters["left_pupil"]: leftsFloat}

        #print(resultingColumnsAsDic)

        return json.dumps(resultingColumnsAsDic)
