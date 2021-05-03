package com.dcap.service;



import com.dcap.domain.*;
import com.dcap.repository.*;
import com.dcap.service.Exceptions.RepoExeption;
import com.dcap.service.serviceInterfaces.StudyServiceInterface;
import com.dcap.service.serviceInterfaces.SubjectServiceInterface;
import com.dcap.domain.*;
import com.dcap.repository.*;
import com.dcap.service.Exceptions.RepoExeption;
import com.dcap.service.serviceInterfaces.StudyServiceInterface;
import com.dcap.service.serviceInterfaces.SubjectServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author uli
 */

@Service
public class    StudyService implements StudyServiceInterface {

    private final StudyInterface studyRepo;
    private final UsersInterface userRepo;
    private final DataProcessingInterface dataProcessingRepo;
    private final DataProcessingStepsInterface dataProcessingStepsRepo;
    private final SubjectInterface subjectRepo;
    private final SubjectServiceInterface subjectService;

    /**
     * Instantiates a new Study service.
     *  @param studyRepo               the study repo
     * @param userRepo                the user repo
     * @param dataProcessingRepo      the data processing repo
     * @param dataProcessingStepsRepo the data processing steps repo
     * @param subjectRepo             the subject repo
     * @param subjectService
     */
    @Autowired
    public StudyService(StudyInterface studyRepo, UsersInterface userRepo, DataProcessingInterface dataProcessingRepo, DataProcessingStepsInterface dataProcessingStepsRepo, SubjectInterface subjectRepo, SubjectServiceInterface subjectService) {
        this.studyRepo = studyRepo;
        this.userRepo = userRepo;
        this.dataProcessingRepo = dataProcessingRepo;
        this.dataProcessingStepsRepo = dataProcessingStepsRepo;
        this.subjectRepo = subjectRepo;
        this.subjectService = subjectService;
    }

    @Override
    public List<Study> findStudyByName(String name){
        return studyRepo.findByName(name);
    }

    @Override
    public Study save(Study study) throws RepoExeption {

        User user = study.getUsers();
        List<Study> studies = studyRepo.findByUser(user);
        for(Study s:studies){
            if(s.getName().equals(study.getName())){
                throw new RepoExeption("Entry with name "+ s.getName() + "already in");
            }
        }

        study = studyRepo.save(study);
        return study;
    }



    /**
     * deletes given Study in the database,
     * @param studyToDelete Study to be deleted
     */
    @Override
    @Transactional(value = Transactional.TxType.REQUIRED)
    public void deleteStudy(Study studyToDelete) throws RepoExeption {
        if(studyToDelete.getId()==null){
            throw new RepoExeption("No id given");
        }

        List<DataProcessing> dataProcessingsByStudy = dataProcessingRepo.findByStudy(studyToDelete);
        for(DataProcessing dp:dataProcessingsByStudy){
            deleteDataProcessing(dp);
        }
        List<Subject> subjectByStudy = subjectRepo.getSubjectByStudy(studyToDelete);
        for(Subject s:subjectByStudy){
            subjectService.delete(s);
        }
        studyRepo.delete(studyToDelete);
    }

    private void deleteDataProcessing(DataProcessing dataProcessing) {
        List<DataProcessingStep> byDataProcessing = dataProcessingStepsRepo.findByDataProcessing(dataProcessing);
        for(DataProcessingStep dps:byDataProcessing){
            dataProcessingStepsRepo.delete(dps);
        }
        dataProcessingRepo.delete(dataProcessing);
    }


    @Override
    public Study findStudyById(Long id) throws RepoExeption {
        Study byId = studyRepo.findById(id);
        if(byId==null){
            throw new RepoExeption("No study with id " + id + " in the database");
        }
        return byId;
    }

    @Override
    public void delete(Long id) throws RepoExeption {
        Study byId = studyRepo.findById(id);
        deleteStudy(byId);

    }

    @Override
    public List<Study> getAllStudiesForUser(User user){
        return studyRepo.findByUser(user);

    }

    @Override
    public Study findStudyForUserAndName(User user, String name) throws RepoExeption {
        List<Study> collect = getStudies(user, name);
        if(collect.size()!=1){
            throw new RepoExeption("No result for request");
        }
        return collect.get(0);
    }

    @Override
    public boolean checkIfStudyForUserAndNameExists(User user, String name) {
        List<Study> collect = getStudies(user, name);
        return collect.size()>0;
    }

    @Override
    public List<Study> getListOfStudiesForUser(User user) {
        List<Study> byUser = studyRepo.findByUser(user);
        return byUser;
    }

    @Override
    public Study updateSave(Study study) throws RepoExeption {
        User user = study.getUsers();
        List<Study> studies = studyRepo.findByUser(user);
        for(Study s:studies){
            if(s.getName().equals(study.getName())){
                study = studyRepo.save(study);
                return study;
            }
        }

        throw new RepoExeption("No study with name "+ study.getName() + " in database");
    }

    private List<Study> getStudies(User user, String name) {
        List<Study> studyByName = studyRepo.findByName(name);
        List<Study> studiesOfUser = studyByName.stream().filter(study -> study.getUsers().equals(user)).collect(Collectors.toList());
        return studiesOfUser.stream().filter(study -> study.getName().equals(name)).collect(Collectors.toList());
    }


    /*private void delete(Study study) {
        Set<User> users = study.getUsers();
        for(User user:users){
            Set<Study> studies = user.getStudies();
            studies.remove(study);
            user.setStudies(studies);
            userRepo.save(user);
        }
        studyRepo.delete(study);
    }*/
}

