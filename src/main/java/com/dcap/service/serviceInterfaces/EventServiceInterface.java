package com.dcap.service.serviceInterfaces;

import com.dcap.domain.EventOld;
import com.dcap.domain.Subject;
import com.dcap.domain.EventOld;
import com.dcap.domain.Subject;

import java.util.List;

public interface EventServiceInterface {
    List<EventOld> getEventsBySubject(Subject subject);

    EventOld saveOrUpdate(EventOld eventOld);

    void delete(EventOld eventOld);
}
