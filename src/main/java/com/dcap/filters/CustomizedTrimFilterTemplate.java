package com.dcap.filters;

import com.dcap.domain.Pythoncode;
import com.dcap.fileReader.DataFile;
import com.dcap.fileReader.DataFileColumn;
import com.dcap.fileReader.DataFileUtils;
import com.dcap.domain.Pythoncode;
import com.dcap.fileReader.DataFile;
import com.dcap.fileReader.DataFileColumn;
import com.dcap.fileReader.DataFileUtils;
import com.dcap.helper.FileException;
import com.dcap.service.PythonCodeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Class to aplly a filter to a file with help of a Pyhton Script (Python2)
 *
 * @author uli
 */

public class CustomizedTrimFilterTemplate extends AbstractTrimFilter {

    /**
     * map of parameters that are needed for the trimming. Key is name of paramater, value is the value of the parameter
     * The only mandatory parameter is the id of the filter ("myPythonCode":"<number>")
     * The other paramaters depend on the filter     */
    private String codeAddress;
    private String parameterAddress;

    private Boolean toPreprocess = false;

    /**

     * Constructor of the filter
     * @param columns map that contains the list of all strings that must be considerated, value is the name of the column in the experiment, the
     *                key is the name of the column in the csv file
     *                 The key is only a internal name. For setting the parameters, the name of the value is used (it should be the same as in the programm).
     */
    public CustomizedTrimFilterTemplate(Pythoncode pythonCodeElement, Map<String,String> columns, Map<String,String> actualParameters) {
        super(pythonCodeElement.getName(), columns, actualParameters);
        this.codeAddress = pythonCodeElement.getCodeadress();
        this.parameterAddress = pythonCodeElement.getParameteradress();
    }

    /**
     * Creates a list of booleans.
     * The function will get the path to a python script that will do the filtering. The script has two parameters,
     * one parameter is the map that are used for the calcuation (as key is the name that of the list, the value is a list of the values as
     * strings - be aware, must be ASCII), the second parameter is the list ov values. The python script will return a list of booleans that ist used
     * for the trimming
     * @param file file that should be trimmed
     * @param columns map of the columns that are used for the creation of the Booleans, key is the name of the column in the experiment, value is the name of the column in the experiment
     * @return list of booleans that decides if you should delete the row or not. false to delete, true to append it to the new file
     * @throws IOException
     */
    @Override
    protected List<Boolean> createBooleanList(DataFile file, Map<String, String> columns) throws IOException, FileException {
        file=file.copyFile();//TODO check, if necessary
        ObjectMapper objectMapper = new ObjectMapper();

        Map<String, List<String>> argumentMap = new HashMap<>();


        for(Map.Entry<String, String> col: getColumns().entrySet()){
            DataFileColumn columnn = file.getHeader().getColumn(col.getValue());
            List<String> values = DataFileUtils.getRawValues(file, columnn, true);
            argumentMap.put(col.getValue(), values);
        }
        String arrayJson = objectMapper.writeValueAsString(argumentMap);
        String parameterJson = objectMapper.writeValueAsString(parameter);
        String columnsForPython = objectMapper.writeValueAsString(getColumns());
        //System.out.println(s);
        PythonInterpreter interpreter = new PythonInterpreter();
//        interpreter.execfile("/home/uli/Desktop/test/trim.py");
        interpreter.execfile(codeAddress);
        interpreter.set("arg1", arrayJson);
        interpreter.set("arg2", parameterJson);
        interpreter.set("arg3", columnsForPython);
        PyObject eval = interpreter.eval("myPythonClass().execute(arg1, arg2, arg3)");
        String json = eval.toString();
        List rawBooleanList = null;
        try {
            rawBooleanList = objectMapper.readValue(json, List.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<Object> stringArrayList = new ArrayList<Object>(rawBooleanList);
        List<Boolean> listToReturn = stringArrayList.stream().map(e -> toBool(e)).collect(Collectors.toList());
        return  listToReturn;

    }

    /**
     * function to map a string to a boolean (if "True", "true", "T", "t" or "1", then true, else false
     * @param valueToBeMapped string to be mapped
     * @return boolean
     */
    private Boolean toBool(Object valueToBeMapped) {
        if(valueToBeMapped instanceof  String && (valueToBeMapped.equals("True")||valueToBeMapped.equals("true")||valueToBeMapped.equals("T")||valueToBeMapped.equals("t")||valueToBeMapped.equals("1"))) {
            return true;
        }else if(valueToBeMapped instanceof Boolean && valueToBeMapped.equals(true)){
            return true;
        }else if(valueToBeMapped instanceof Number && valueToBeMapped.equals(1)){
            return true;
        }else{
            return false;
        }
    }

    /**
     * returns the description of the parameters that are needed as input. Returns the mame of the parameter as key and the type as value
     * @return list of required parameters
     */
    @Override
    public Map<String, ENUMERATED_TYPES> getRequiredParameters() {
        HashMap<String, ENUMERATED_TYPES> parameters = null;
        try {
            parameters = PythonCodeService.getStringENUMERATED_typesHashMap(parameterAddress);
            System.err.println("looking for parameters");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return parameters;
    }

    @Override
    public Boolean isPreprocessing() {
        return toPreprocess;
    }
}
