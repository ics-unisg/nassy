import  pandas as pd


fileOne='../data/measures_2019-08-11_13-14.csv'

fileTwo='../data/measures_2019-08-09_15-58.csv'

fileToCheckOne = pd.read_csv(fileOne, delimiter=";")
fileToCheckTwo = pd.read_csv(fileTwo, delimiter=";")

labelsOne=fileToCheckOne["average_meanValueOfTimeSlot_M1B-Q01.png"]
#print(labelsOne)


labelsTwo=fileToCheckTwo["average_meanValueOfTimeSlot_a"]
#print(labelsTwo)
