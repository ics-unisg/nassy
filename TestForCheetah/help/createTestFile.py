import os
import random

with open("../testData/max@TestStudy@averageTest.tsv", "w") as file_one:
    with open("../testData/confirmAverageTest.tsv", "w") as file_two:
       file_one.write("EyeTrackerTimestamp\tcolumnOne\tcolumnTwo\tcolumnThreee"+"\n")
       file_two.write("One\tTwo\tThree\taverageTwo\taverageThree"+"\n")
       for i in range(100):
            a1=random.randint(0,5)
            a2=random.randint(0,5)
            a3=random.randint(0,5)
            b1=random.randint(10,99)
            b2=random.randint(10,99)
            b3=random.randint(10,99)

            file_one.write(str(i)+"\t"+str(a1)+","+str(b1)+"\t"+str(a2)+","+str(b2)+"\t"+str(a3)+","+str(b3)+"\n")
            n1=a1+(b1/100)
            n2=a2+(b2/100)
            n3=a3+(b3/100)
            file_two.write(str(n1)+"\t"+str(n2)+"\t"+str(n3)+"\t"+str((n1+n2)/2)+"\t"+str((n1+n2+n3)/3)+"\n")