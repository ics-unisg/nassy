package com.dcap.service;

import com.dcap.domain.EventOld;
import com.dcap.domain.Subject;
import com.dcap.repository.EventInterface;
import com.dcap.domain.EventOld;
import com.dcap.domain.Subject;
import com.dcap.repository.EventInterface;
import com.dcap.service.serviceInterfaces.EventServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service to manage the Events in the database
 *
 * @author uli
 */

@Service
public class EventService implements EventServiceInterface {

    private final EventInterface eventRepo;

    @Autowired
    public EventService(EventInterface eventRepo) {
        this.eventRepo = eventRepo;
    }

    /**
     * Lists all Events for a given subject
     * @param subject subject that is connected to the Events
     * @return events for a given subject
     */
    @Override
    public List<EventOld> getEventsBySubject(Subject subject){
        return eventRepo.findBySubject(subject);
    }

    /**
     *saves or updates EventOld items in the database
     * @param eventOld EventOld item to be saved or updated
     * @return EventOld item saved in the database. Attention: if you create a new Object, the ID is only created by saving it in the database!
     */
    @Override
    public EventOld saveOrUpdate(EventOld eventOld){
        return eventRepo.save(eventOld);
    }

    /**
     * deletes a given EventOld in the database
     * @param eventOld EventOld to be deleted
     */
    @Override
    public void delete(EventOld eventOld){
        eventRepo.delete(eventOld);
    }
}
