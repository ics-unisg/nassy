package com.dcap.repository;

import com.dcap.domain.EventOld;
import com.dcap.domain.Subject;
import com.dcap.domain.EventOld;
import com.dcap.domain.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventInterface extends JpaRepository<EventOld, Long> {

    public List<EventOld> findByName(String name);

    public List<EventOld> findBySubject(Subject subject);

}
