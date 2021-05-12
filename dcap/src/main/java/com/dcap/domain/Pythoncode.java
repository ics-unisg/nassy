package com.dcap.domain;

import com.dcap.service.Exceptions.PythoncodeException;
import javax.persistence.*;


/**
 *
 * This class is used to describe a Python code and to manage it's storage in the database
 *
 * @author uli1
 */
@Entity
@Table(name = "pythoncode")
public class Pythoncode implements DatabaseEntryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "pk_pythoncode")
    private Long id;

    private String name;
    private String parameteradress;
    private String codeadress;
    private String type;


    public Pythoncode(String name, String parameteradress, String codeadress, String type) throws PythoncodeException {
        this.name = name;
        this.parameteradress = parameteradress;
        this.codeadress = codeadress;
        if (type==null || type.trim().equals("custom")){
            this.type = "custom";
        }else  if(type.trim().equals("trim")){
            this.type = "trim";
        }else {
            throw new PythoncodeException("wrong type");
        }
    }

    public Pythoncode() {
    }

    @Override
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


