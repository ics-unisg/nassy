import fixationsExperiment
import fullTest
#from help import deletePFile
from requirements import req1
from requirements import req2
from requirements import req3
from requirements import req4
from requirements import req5a
from requirements import req5b
from requirements import req5c
from requirements import req6
from requirements import req7
from requirements import req8
from requirements import req9
import net.createStudyAndUser as cu




cu.createUserForTest()
#deletePFile.delete()
fullTest.runFullTest()
fixationsExperiment.checkFixations()
req1.checkRequirementOne()
req2.checkRequirementTwo()
req3.checkRequirementThree()
req4.checkRequirementFour()
req5a.checkRequirementFive()
req5b.checkRequirementFive()
req5c.checkRequirementFive()
req6.checkRequirementSix()
req7.checkRequirementSeven()
req8.checkRequirementEight()
req9.checkRequirementNine()
cu.deleteUserForTest()

#fixationsExperiment.checkFixations()

#fullTest.runFullTest()
