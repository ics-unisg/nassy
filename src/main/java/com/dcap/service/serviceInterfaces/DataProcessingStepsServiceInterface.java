package com.dcap.service.serviceInterfaces;

import com.dcap.domain.DataProcessing;
import com.dcap.domain.DataProcessingStep;
import com.dcap.domain.DataProcessing;
import com.dcap.domain.DataProcessingStep;

import java.util.List;

public interface DataProcessingStepsServiceInterface {
    List<DataProcessingStep> getDataProcessingStepsByDataProcessing(DataProcessing dp);

    void deleteDatProcessingStep(DataProcessingStep dps);

    DataProcessingStep saveOrUpdate(DataProcessingStep dps);
}
