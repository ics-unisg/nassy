package com.dcap.repository;

import com.dcap.domain.DataProcessing;
import com.dcap.domain.Study;
import com.dcap.domain.DataProcessing;
import com.dcap.domain.Study;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DataProcessingInterface extends JpaRepository<DataProcessing, Long> {


    public List<DataProcessing> findByStudy(Study study);
    public DataProcessing findById(Long id);


}
