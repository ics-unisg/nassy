package com.dcap.service.serviceInterfaces;

import com.dcap.domain.Pythoncode;
import com.dcap.filters.ENUMERATED_TYPES;
import com.dcap.service.Exceptions.PythoncodeException;
import com.dcap.domain.Pythoncode;
import com.dcap.filters.ENUMERATED_TYPES;
import com.dcap.service.Exceptions.PythoncodeException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public interface PythonCodeServiceInterface {



    List<String> getAllListedPythonFilterNames();

    File getCodeFile(String name);

    Pythoncode getPythonCodeElement(String name);

    HashMap<String, ENUMERATED_TYPES> getParameters(String name) throws IOException;

    String getCodeAdress(String name);

    String getParameterAddress(String name);

    boolean deletePythonCode(String name) throws PythoncodeException;
}
