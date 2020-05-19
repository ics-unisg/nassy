from help import helper
from logger.logg import Logger
from net import operateOnSubjects, operateOnStudy
from net import createStudyAndUser





#Data for authorisation
from net.user import createNewUser, deleteUser

authorisation = {
    'username': 'test.user@test.dcap',
    'password': 'Test'
}

def checkRequirementOne():
    #create log file
    log = Logger("Requirement1_", "./logs/")

    #Standart name for test study
    studyName = "TestStudy"

    #Name for new test study
    new_name = "NewStudyName"

    #Name for second test study
    secondStudyName = "SecondStudyName"

    #deletes all entries for studies with given name
    helper.cleanDatabase(authorisation, studyName)
    #deletes all entries for studies with given name
    helper.cleanDatabase(authorisation, new_name)
    #deletes all entries for studies with given name
    helper.cleanDatabase(authorisation, secondStudyName)


    #Create Study
    studyId= createStudyAndUser.create_study(authorisation, studyName, log)

    #Create Study2
    studyId2= createStudyAndUser.create_study(authorisation, secondStudyName, log)


    #Create List of users
    subject_list=["Gustav",  "Tick", "Trick", "Track"]
    listOfCreatedSubjects = createStudyAndUser.createSubjects(authorisation, subject_list, studyId, log)

    #Check if all subjects are registered
    operateOnSubjects.checkAllSubjects(authorisation, subject_list, studyName, log)

    #Create List ofnew Users
    subject_list2=["Mickey", "Goofy"]

    listOfCreatedSecondSubjects = createStudyAndUser.createSubjects(authorisation, subject_list2, studyId, log)



    #Get first subject of list
    subjectToChangeId = operateOnSubjects.getSubjectIdByName(authorisation, subject_list[0], log)

    #change name of a subject
    operateOnSubjects.changeSubjectName(authorisation, subjectToChangeId, "Donald", log)

    #checks, if subject with given name is in the list
    operateOnSubjects.getSubjectIdByName(authorisation, "Donald", log)

    #delete subject in that was changed"Gustav",  "Tick", "Trick", "Track"
    operateOnSubjects.deleteSubject(authorisation, subjectToChangeId, log)


    #Check if all subjects are  after the changes registered
    operateOnSubjects.checkAllSubjects(authorisation,  ["Mickey", "Goofy", "Tick", "Trick", "Track"], studyName, log)

    #Try to add subject with same id to study
    createStudyAndUser.tryToCreateDoubleSubject(authorisation, "Mickey", studyId, log)

    #Try to add subject with same id to second study
    createStudyAndUser.tryToCreateDoubleSubject(authorisation, "Mickey", studyId2, log)

    #change name of the study
    operateOnStudy.changeStudy(authorisation, studyId, new_name, log)

    #check, if the list contains the study with the id that has new the given name
    operateOnStudy.checkStudyName(authorisation, studyId, new_name, log)



    #create a new user
    cred, userId=createNewUser(authorisation, log)

    #tries to retrieve a study of other user
    operateOnStudy.tryToGetAlienStudy(cred, studyId, log)

    # Create Study
    studyIdFromSecondUser = createStudyAndUser.create_study(cred, studyName, log)

    listOfCreatedSecondSubjects = createStudyAndUser.createSubjects(cred, subject_list2, studyIdFromSecondUser, log)


    #Check if all subjects are registered
    operateOnSubjects.checkAllSubjects(cred, subject_list2, studyName, log)


    #deletes new user
    deleteUser(authorisation, userId, log)

    #purges database
    helper.cleanDatabase(authorisation, studyName)

    #purges database
    helper.cleanDatabase(authorisation, new_name)

    #purges database
    helper.cleanDatabase(authorisation, secondStudyName)

    #closes logfile
    log.close()
