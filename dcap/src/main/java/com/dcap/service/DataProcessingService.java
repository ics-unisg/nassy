package com.dcap.service;

import com.dcap.domain.DataProcessing;
import com.dcap.domain.DataProcessingStep;
import com.dcap.domain.Study;
import com.dcap.repository.DataProcessingInterface;
import com.dcap.repository.DataProcessingStepsInterface;
import com.dcap.domain.DataProcessing;
import com.dcap.domain.DataProcessingStep;
import com.dcap.domain.Study;
import com.dcap.repository.DataProcessingInterface;
import com.dcap.repository.DataProcessingStepsInterface;
import com.dcap.service.serviceInterfaces.DataProcessingServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service used to manage DataProcessings in database
 *
 * @author uli
 */

@Service
public class DataProcessingService implements DataProcessingServiceInterface {

    private final DataProcessingInterface dataProcessingRepo;
    private final DataProcessingStepsInterface dataProcessingStepsRepo;


    @Autowired
    public DataProcessingService(DataProcessingInterface dataProcessingRepo, DataProcessingStepsInterface dataProcessingStepsRepo) {
        this.dataProcessingRepo = dataProcessingRepo;
        this.dataProcessingStepsRepo = dataProcessingStepsRepo;
    }

    /**
     * lists all DataProcessings in the database
     * @return List of al DataProcessing
     */
    @Override
    public List<DataProcessing> getAllDataProcessings(){
        return dataProcessingRepo.findAll();
    }


    /**
     * gets a list with all DataProcessings for a given study (all DataProcessings that are connected to a study)
     * @param study study that is connected to the DataProcessings
     * @return List of DataProcessings, that are connected to a study
     */
    @Override
    public List<DataProcessing> getDataProcessingsByStudy(Study study){
        return dataProcessingRepo.findByStudy(study);
    }

    /**
     * Returns DataProcessing with a given id
     * @param id id of the DataProcessing
     * @return DataProcessing with the given id
     */
    @Override
    public DataProcessing getDataProcessingsById(Long id){
        return dataProcessingRepo.findById(id);
    }

    /**
     * deletes a DataProcessing item and all connected DataProcessingSteps
     * @param dataProcessing DataProcessing item, that should be deleted
     */
    @Override
    public void deleteDataProcessing(DataProcessing dataProcessing){
        List<DataProcessingStep> byDataProcessing = dataProcessingStepsRepo.findByDataProcessing(dataProcessing);
        for(DataProcessingStep dps:byDataProcessing){
            dataProcessingStepsRepo.delete(dps);
        }
        dataProcessingRepo.delete(dataProcessing);
    }

    /**
     *saves or updates DataProcessing items in the database
     * @param dataProcessing DataProcessing item to be saved or updated
     * @return DataProcess item saved in the database. Attention: if you create a new Object, the ID is only created by saving it in the database!
     */
    @Override
    public DataProcessing saveOrUpdate(DataProcessing dataProcessing){
        return dataProcessingRepo.save(dataProcessing);
    }


}
