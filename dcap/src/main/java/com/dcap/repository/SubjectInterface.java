package com.dcap.repository;

import com.dcap.domain.Study;
import com.dcap.domain.Subject;
import com.dcap.domain.Study;
import com.dcap.domain.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SubjectInterface extends JpaRepository<Subject, Long> {

    List<Subject> getSubjectByStudy(Study study);

    Subject findById(Long id);

    @Query(value = "SELECT s.* FROM subjects s join studies t on s.fk_study = t.pk_study where t.fk_user=?2 and s.subject_id=?1", nativeQuery = true)
    List<Subject> findBySubjectId(String subjectId, Long userId);

}