package com.dcap.repository;

import com.dcap.domain.Study;
import com.dcap.domain.User;
import com.dcap.domain.Study;
import com.dcap.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudyInterface extends JpaRepository<Study, Long> {

    public List<Study> findByName(String name);
    public Study findById(Long id);
    public List<Study> findByUser(User user);
}
