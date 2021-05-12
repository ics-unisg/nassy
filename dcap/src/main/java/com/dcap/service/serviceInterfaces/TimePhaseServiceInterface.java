package com.dcap.service.serviceInterfaces;

import com.dcap.domain.Subject;
import com.dcap.domain.TimePhase;

import java.util.List;

public interface TimePhaseServiceInterface {
    List<TimePhase> getTimePhaseBySubject(Subject subject);

    TimePhase getTimePhaseById(Long id);

    void delete(TimePhase timePhase);

    void delete(Long id);

    TimePhase saveOrUpdate(TimePhase timePhase);
}
