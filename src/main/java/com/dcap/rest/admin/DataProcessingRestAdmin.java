package com.dcap.rest.admin;


import com.dcap.domain.DataProcessing;
import com.dcap.rest.DataMsg;
import com.dcap.transferObjects.EasyDataProcessing;
import com.dcap.domain.DataProcessing;
import com.dcap.rest.DataMsg;
import com.dcap.service.serviceInterfaces.DataProcessingServiceInterface;
import com.dcap.transferObjects.EasyDataProcessing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class DataProcessingRestAdmin {

    final DataProcessingServiceInterface dataProcessingService;



    @Autowired
    public DataProcessingRestAdmin(DataProcessingServiceInterface dataProcessingService) {
        this.dataProcessingService = dataProcessingService;
    }

    public ResponseEntity<DataMsg<EasyDataProcessing>> getDataProcessingById(Long id) {
        DataProcessing dataProcessingsById = dataProcessingService.getDataProcessingsById(id);
        EasyDataProcessing easyDataProcessing = new EasyDataProcessing(dataProcessingsById);
        if (dataProcessingsById != null) {
            return ResponseEntity.ok(new DataMsg(0, null, null, easyDataProcessing));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DataMsg(101, null, "Dataprocessing not found", null));

        }
    }



}
