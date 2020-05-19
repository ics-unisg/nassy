package com.dcap.rest.user;


import com.dcap.filters.DataFilterRegistry;
import com.dcap.filters.ENUMERATED_TYPES;
import com.dcap.filters.IDataFilter;
import com.dcap.rest.DataMsg;
import com.dcap.filters.DataFilterRegistry;
import com.dcap.filters.ENUMERATED_TYPES;
import com.dcap.filters.IDataFilter;
import com.dcap.rest.DataMsg;
import com.dcap.service.Exceptions.AbstractFilterException;
import com.dcap.service.Exceptions.NoSuchFilterException;
import com.dcap.service.PythonCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class FilterDetailsRestUser {

    final PythonCodeService pythonCodeService;

    @Autowired
    public FilterDetailsRestUser( PythonCodeService pythonCodeServie) {
        this.pythonCodeService = pythonCodeServie;
    }

    public ResponseEntity<DataMsg<ArrayList<ParameterForMessage>>> getFilterDetails(String filterName) throws IOException {
        IDataFilter filter = null;
        Map<String, ENUMERATED_TYPES> requiredParameters=null;
        try {
            filter = DataFilterRegistry.getFilter(filterName);
           requiredParameters = filter.getRequiredParameters();
        } catch (AbstractFilterException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DataMsg<>(1201, null, "Filter was Abstract", null));
        } catch (NoSuchFilterException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new DataMsg<>(1201, null, "No such Filter", null));
        }

        ArrayList<ParameterForMessage> requiredParametersResponse = new ArrayList<>();
        for(Map.Entry<String, ENUMERATED_TYPES> entry: requiredParameters.entrySet()){
            requiredParametersResponse.add(new ParameterForMessage(entry.getKey(), entry.getValue()));

        }
        return ResponseEntity.ok(new DataMsg<>(0, null, null, requiredParametersResponse));
    }

    public ResponseEntity<DataMsg<List<String>>> getListOfFilters() {
        List<String> listOfFilters = DataFilterRegistry.getListOfFilters();
        //List<String> allListedPythonFilterNames = pythonCodeService.getAllListedPythonFilterNames();
        //listOfFilters.addAll(allListedPythonFilterNames);
        return ResponseEntity.ok(new DataMsg<>(0, null, null, listOfFilters));

    }

    public class ParameterForMessage{
        public String name;
        public String type;
        public String message;

        public ParameterForMessage(String name, ENUMERATED_TYPES type) {
            if(!type.equals(ENUMERATED_TYPES.AddidtionalInfo)) {
                this.name = name;
                this.type = type.toString();
            }
            else {
                this.message = name;
            }
        }
    }
}
