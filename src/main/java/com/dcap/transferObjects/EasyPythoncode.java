package com.dcap.transferObjects;

import com.dcap.domain.Pythoncode;

public class EasyPythoncode {
    private Long id;

    private String name;
    private String parameteradress;
    private String codeadress;
    private String type;


    public EasyPythoncode(Pythoncode pythoncode) {
        this.name = pythoncode.getName();
        this.parameteradress = pythoncode.getParameteradress();
        this.codeadress = pythoncode.getCodeadress();
        this.type = pythoncode.getType();
    }

    public EasyPythoncode() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParameteradress() {
        return parameteradress;
    }

    public void setParameteradress(String parameteradress) {
        this.parameteradress = parameteradress;
    }

    public String getCodeadress() {
        return codeadress;
    }

    public void setCodeadress(String codeadress) {
        this.codeadress = codeadress;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
