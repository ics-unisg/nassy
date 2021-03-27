#!/usr/bin/python3
import os
import sys
import net.createStudyAndUser as cu
from logger import logg
from net import createStudyAndUser, uploadFilesViaNamingConvention, filterScripts, file, tasks, measures, operateOnSubjects
from fileoperations import calcStatisticsWithBaseline, calcStatisticsWithOutBaseline
from net.filterScripts import nassyFilterData
from help import helper
from help import setting
import io

import pandas as pd

cu.createUserForTest()



#cu.deleteUserForTest()
