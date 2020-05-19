package com.dcap.service;

import com.dcap.domain.Subject;
import com.dcap.domain.TimePhase;
import com.dcap.repository.TimePhaseInterface;
import com.dcap.service.serviceInterfaces.TimePhaseServiceInterface;
import com.dcap.service.serviceToolKit.ServiceTools;
import com.dcap.domain.Subject;
import com.dcap.domain.TimePhase;
import com.dcap.repository.TimePhaseInterface;
import com.dcap.service.serviceInterfaces.TimePhaseServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.dcap.service.serviceToolKit.ServiceTools.deleteTimePhase;

/**
 * Service that manages the TimePhases in the database
 *
 * @author uli
 *
 */

@Service
public class TimePhaseService implements TimePhaseServiceInterface {

    private final TimePhaseInterface timePhaseRepo;

    @Autowired
    public TimePhaseService(TimePhaseInterface timePhaseRepo) {
        this.timePhaseRepo = timePhaseRepo;
    }

    /**
     * finds all TimePhases for a given subject
     * @param subject subject, that is owner of TimePhase
     * @return list fo TimePhases connected to a Subjectt
     */
    @Override
    public List<TimePhase> getTimePhaseBySubject(Subject subject){
        return timePhaseRepo.findBySubject(subject);
    }

    /**
     * finds TimePhase for given id
     * @param id id of TimePhase
     * @return TimePhase for given id
     */
    @Override
    public TimePhase getTimePhaseById(Long id){
        return timePhaseRepo.findById(id);
    }

    /**
     * deletes given TimePhase in the database
     * @param timePhase TimePhase to be deleted
     */
    @Override
    public void delete(TimePhase timePhase){
        ServiceTools.deleteTimePhase(timePhase, timePhaseRepo);
    }

    /**
     * deletes given TimePhase in the database
     * @param id Id of TimePhase to be deleted
     */
    @Override
    public void delete(Long id){
        delete(timePhaseRepo.findById(id));
    }

    /**
     *saves or updates TimePhase items in the database
     * @param  timePhase TimePhase item to be saved or updated
     * @return  TimePhase item saved in the database. Attention: if you create a new Object, the ID is only created by saving it in the database!
     */
    @Override
    public TimePhase saveOrUpdate(TimePhase timePhase){
        return timePhaseRepo.save(timePhase);
    }


}
