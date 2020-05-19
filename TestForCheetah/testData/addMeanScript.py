import json


class myPythonClass():
    def execute(self, data, param, col):
        daten = json.loads(data)
        parameters = json.loads(param)
        columns = json.loads(col)

        lefts = daten[parameters["left_pupil"]]
        rights = daten[parameters["right_pupil"]]

        leftsFloat = [float(i.replace(",", ".")) for i in lefts]
        rightsloat = [float(i.replace(",", ".")) for i in rights]

        mittelArray = []
        for i in range(0, len(leftsFloat)):
            mittel = (leftsFloat[i] + rightsloat[i]) / 2
            mittelArray.append(mittel)

        results = [str(i) for i in mittelArray]

        resultingColumnsAsDic = {"Mean": results}

        return json.dumps(resultingColumnsAsDic)
