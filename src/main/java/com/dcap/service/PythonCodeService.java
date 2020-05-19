package com.dcap.service;

import com.dcap.domain.Pythoncode;
import com.dcap.filters.ENUMERATED_TYPES;
import com.dcap.repository.PythonCodeInterface;
import com.dcap.domain.Pythoncode;
import com.dcap.filters.ENUMERATED_TYPES;
import com.dcap.repository.PythonCodeInterface;
import com.dcap.service.Exceptions.PythoncodeException;
import com.dcap.service.serviceInterfaces.PythonCodeServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service to manage the Python codes in the database
 *
 * @author uli
 */
@Service
public class PythonCodeService implements PythonCodeServiceInterface {

    final PythonCodeInterface pythonCodeRepo;

    @Autowired
    public PythonCodeService(PythonCodeInterface pythonCodeRepo) {

        this.pythonCodeRepo = pythonCodeRepo;
    }

    @Override
    public List<String> getAllListedPythonFilterNames(){
        List<Pythoncode> allEntries = pythonCodeRepo.findAll();
        List<String> names = allEntries.stream().map(e -> e.getName()).collect(Collectors.toList());
        return names;
    }

    @Override
    public File getCodeFile(String name){
        Pythoncode byName = pythonCodeRepo.findByName(name);
        File file = new File(byName.getCodeadress());
        return file;
    }

    @Override
    public Pythoncode getPythonCodeElement(String name){
        Pythoncode byName = pythonCodeRepo.findByName(name);
        return byName;
    }

    @Override
    public HashMap<String, ENUMERATED_TYPES> getParameters(String name) throws IOException {
        Pythoncode byName = pythonCodeRepo.findByName(name);
        String parameteradress = byName.getParameteradress();

        return getStringENUMERATED_typesHashMap(parameteradress);

    }

    @Override
    public String getCodeAdress(String name) {
        Pythoncode byName = pythonCodeRepo.findByName(name);
        return byName.getCodeadress();
    }

    @Override
    public String getParameterAddress(String name) {
        Pythoncode byName = pythonCodeRepo.findByName(name);
        return byName.getParameteradress();
    }

    public Pythoncode saveCode(Pythoncode code) throws PythoncodeException {


        Pythoncode byName = pythonCodeRepo.findByName(code.getName());
        if(byName!=null){
            throw new PythoncodeException("Codename already exists");
        }
        Pythoncode savedCode = pythonCodeRepo.save(code);
        return savedCode;
    }

    @Override
    public boolean deletePythonCode(String name) throws PythoncodeException {
        Pythoncode byName = pythonCodeRepo.findByName(name);
        if(byName==null){
            throw new PythoncodeException("No such file");
        }
        try {
            Files.delete((new File(byName.getParameteradress())).toPath());
            Files.delete(new File(byName.getCodeadress()).toPath());
        } catch (IOException e) {
            throw new PythoncodeException("Could not delete file on system.");
        }
        pythonCodeRepo.delete(byName.getId());
        byName=pythonCodeRepo.findByName(name);
        if(byName!=null){
            return false;
        }else {
            return true;
        }
    }

    static public HashMap<String, ENUMERATED_TYPES> getStringENUMERATED_typesHashMap(String parameteradress) throws IOException {
        HashMap<String, ENUMERATED_TYPES> params = new HashMap<String, ENUMERATED_TYPES>();
        File file = new File(parameteradress);
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line;
        while ((line = bufferedReader.readLine())!=null){
            String[] split = line.split(",");
            params.put(split[0], ENUMERATED_TYPES.valueOf(split[1].trim()));
        }
        return params;
    }
}
