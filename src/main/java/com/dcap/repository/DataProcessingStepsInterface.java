package com.dcap.repository;

import com.dcap.domain.DataProcessing;
import com.dcap.domain.DataProcessingStep;
import com.dcap.domain.DataProcessing;
import com.dcap.domain.DataProcessingStep;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DataProcessingStepsInterface extends JpaRepository<DataProcessingStep, Long>{

    public List<DataProcessingStep> findByDataProcessing(DataProcessing dp);
}
