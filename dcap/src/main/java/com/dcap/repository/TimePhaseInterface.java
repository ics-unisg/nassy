package com.dcap.repository;

import com.dcap.domain.Subject;
import com.dcap.domain.TimePhase;
import com.dcap.domain.Subject;
import com.dcap.domain.TimePhase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TimePhaseInterface extends JpaRepository<TimePhase, Long>{

    public List<TimePhase> findBySubject(Subject subject);

    public List<TimePhase> findByParent(TimePhase timePhase);

    public TimePhase findById(Long id);

}
