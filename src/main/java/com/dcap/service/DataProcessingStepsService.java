package com.dcap.service;

import com.dcap.domain.DataProcessing;
import com.dcap.domain.DataProcessingStep;
import com.dcap.repository.DataProcessingStepsInterface;
import com.dcap.domain.DataProcessing;
import com.dcap.domain.DataProcessingStep;
import com.dcap.repository.DataProcessingStepsInterface;
import com.dcap.service.serviceInterfaces.DataProcessingStepsServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service used to store DataProcessingSteps in the database
 *
 * @author uli
 */

@Service
public class DataProcessingStepsService implements DataProcessingStepsServiceInterface {

    private final DataProcessingStepsInterface dataProcessingStepsRepo;

    @Autowired
    public DataProcessingStepsService(DataProcessingStepsInterface dataProcessingStepsRepo) {
        this.dataProcessingStepsRepo = dataProcessingStepsRepo;
    }

    /**
     * Finds all DataProcessingSteps for a given DataProcessing
     * @param dp DataProcessing for finding the DataProcessingSteps
     * @return List of DataProcessingSteps for given DataProcessing item
     */
    @Override
    public List<DataProcessingStep> getDataProcessingStepsByDataProcessing(DataProcessing dp){
        return dataProcessingStepsRepo.findByDataProcessing(dp);
    }

    /**
     * Deletes a given DataProcessing item
     * @param dps DataProcessing item that should be deleted
     */
    @Override
    public void deleteDatProcessingStep(DataProcessingStep dps){
        dataProcessingStepsRepo.delete(dps);
    }

    /**saves or updates DataProcessingStep items in the database
     * @param dps DataProcessingStep item to be saved or updated
     * @return DataProcesStep item saved in the database. Attention: if you create a new Object, the ID is only created by saving it in the database!
            */
    @Override
    public  DataProcessingStep saveOrUpdate(DataProcessingStep dps){
        return dataProcessingStepsRepo.save(dps);
    }
}
