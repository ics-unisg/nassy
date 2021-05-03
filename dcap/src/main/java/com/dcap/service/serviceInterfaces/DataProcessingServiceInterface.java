package com.dcap.service.serviceInterfaces;

import com.dcap.domain.DataProcessing;
import com.dcap.domain.Study;
import com.dcap.domain.DataProcessing;
import com.dcap.domain.Study;

import java.util.List;

public interface DataProcessingServiceInterface {

    List<DataProcessing> getAllDataProcessings();

    List<DataProcessing> getDataProcessingsByStudy(Study study);

    DataProcessing getDataProcessingsById(Long id);

    void deleteDataProcessing(DataProcessing dataProcessing);

    DataProcessing saveOrUpdate(DataProcessing dataProcessing);
}
