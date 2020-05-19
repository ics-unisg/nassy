package com.dcap.service;


import com.dcap.domain.*;
import com.dcap.repository.StudyInterface;
import com.dcap.repository.SubjectInterface;
import com.dcap.repository.TimePhaseInterface;
import com.dcap.service.Exceptions.RepoExeption;
import com.dcap.service.serviceInterfaces.SubjectServiceInterface;
import com.dcap.service.serviceToolKit.ServiceTools;
import com.dcap.domain.*;
import com.dcap.repository.*;
import com.dcap.service.Exceptions.RepoExeption;
import com.dcap.service.serviceInterfaces.SubjectServiceInterface;
import com.dcap.service.serviceInterfaces.UserDataServiceInterface;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.dcap.service.serviceToolKit.ServiceTools.deleteTimePhase;

/**
 * Service to manage the Subjects in the database
 *
 * @author uli
 */

@Service
public class SubjectService implements SubjectServiceInterface {

    private final SubjectInterface subjectRepo;
    private final TimePhaseInterface timePhaseRepo;
    private final UserDataServiceInterface userDataService;
    private final StudyInterface studyRepo;


    public SubjectService(SubjectInterface subjectRepo, TimePhaseInterface timePhaseRepo, UserDataServiceInterface userDataService, StudyInterface studyRepo) {
        this.subjectRepo = subjectRepo;
        this.timePhaseRepo = timePhaseRepo;
        this.userDataService = userDataService;
        this.studyRepo = studyRepo;
    }

    /**
     * finds all Subjects for a given study
     * @param study that have the subjects
     * @return list of all subjects for a given study
     */
    @Override
    public List<Subject> getAllSubjectsForStudy(Study study){
        return subjectRepo.getSubjectByStudy(study);
    }

    /**
     *saves or updates Subject items in the database
     * @param  subject Subject item to be saved or updated
     * @return  Subject item saved in the database. Attention: if you create a new Object, the ID is only created by saving it in the database!
     */
    @Override
    public Subject save(Subject subject, User user) throws RepoExeption {
        List<Subject> bySubjectId = subjectRepo.findBySubjectId(subject.getSubjectId(), user.getId());
        if(!(bySubjectId==null|| bySubjectId.isEmpty())){
            throw  new RepoExeption("Subject " +subject.getSubjectId() + " already in database");
        }
        return subjectRepo.save(subject);
    }

    /**
     * finds Subjec for a given id
     * @param id of the Subject
     * @return Subject with given id
     */
    @Override
    public Subject getSubjectById(Long id) throws RepoExeption {
        Subject byId = subjectRepo.findById(id);
        if(byId==null){
            throw new RepoExeption("could not find subcjet "+id );
        }
        return byId;
    }

    /**
     * Finds Subjects for a given subject_id and a study, i.e. for example property that is unique in a study, e.g. CPR-Number
     * @param subjetId id of a subject, unique in a study
     * @param study name of the study
     * @param user user that is the owner of the subjects
     * @return subject with those properties
     */
    @Override
    public Subject getSubjectBySubjectAndStudy(String subjetId, String study, User user) throws RepoExeption {
        List<Subject> bySubjectId = subjectRepo.findBySubjectId(subjetId, user.getId());
        List<Subject> collect = bySubjectId.stream().filter(s -> (s.getStudy().getName().equals(study))).collect(Collectors.toList());
        if(collect.size()==1){
            return collect.get(0);
        }else{
            throw new RepoExeption("no subject for given study");
        }
    }

    /**
     * deletes given Subject in the database
     * @param subject Subject to be deleted
     */
    @Override
    public void delete(Subject subject){
        List<TimePhase> timePhaseBySubject = timePhaseRepo.findBySubject(subject);
        for(TimePhase tp:timePhaseBySubject){
            ServiceTools.deleteTimePhase(tp, timePhaseRepo);
        }
        userDataService.deleteUserDataForSubject(subject);
        List<UserData> userDataBySubject = userDataService.getHiddenUserdataBySubject(subject);
        for(UserData ud:userDataBySubject){
            ud.setSubject(null);
            userDataService.saveOrUpdate(ud);
        }
        subjectRepo.delete(subject);
    }




    @Override
    public Subject updateSubject(Subject fullSubject) throws RepoExeption {
        Subject byId = subjectRepo.findById(fullSubject.getId());
        if(byId==null){
            throw new RepoExeption("No subject for id " +fullSubject.getSubjectId() + " in the database");
        }
        Subject save = subjectRepo.save(fullSubject);
        return save;
    }

    @Override
    public List<Subject> getAllSubjectsForUser(User user){
        ArrayList<Subject> subjectList = new ArrayList<>();
        List<Study> studiesByyUsers = studyRepo.findByUser(user);
        for(Study study:studiesByyUsers){
            List<Subject> subjectByStudy = subjectRepo.getSubjectByStudy(study);
            subjectList.addAll(subjectByStudy);
        }
        return subjectList;

    }

    /**
     * deletes given Subject in the database
     * @param subjectId Id of Subject to be deleted
     */
    @Override
    public void delete(Long subjectId) throws RepoExeption {
        Subject subject = subjectRepo.findById(subjectId);
        if(subject==null){
            throw new RepoExeption("No subject for id " +subjectId + " in the database");
        }

        subjectRepo.delete(subjectId);
    }

    @Override
    public Boolean sujbectExists(Long subjectId) {
        if(subjectRepo.findById(subjectId)!=null){
            return true;
        }
        return false;
    }

    @Override
    public Subject addSubjectToStudy(Long subjectId, Long studyId) throws RepoExeption {
        Subject subject= subjectRepo.findById(subjectId);
        Study study = studyRepo.findById(studyId);

        Study studyOfSubject = subject.getStudy();
        if(studyOfSubject!=null && studyOfSubject.equals(study)){
            return subject;
        }
        if(studyOfSubject!=null){
            throw new RepoExeption("Subject already mapped to another study");
        }
        subject.setStudy(study);
        subject = subjectRepo.save(subject);
        return subject;
    }

    @Override
    public Subject removeSubjectFromStudy(Long subjectId, Long studyId) throws RepoExeption {
        Subject subject= subjectRepo.findById(subjectId);
        Study study = studyRepo.findById(studyId);
        Study studyOfSubject = subject.getStudy();
        if(studyOfSubject!=null){
            throw new RepoExeption("Subject has not study");
        }
        if(!studyOfSubject.equals(study)){
            throw new RepoExeption("Subject is attached to another study, to study " + studyOfSubject.getId() + ".");
        }
        subject.setStudy(null);
        subject = subjectRepo.save(subject);
        return subject;
    }

}
