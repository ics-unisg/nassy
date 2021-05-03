package com.dcap.domain;


import javax.persistence.*;

/**
 * This class describes properties of cheetahWeb
 *
 * @author uli
 *
 */

@Entity
@Table(name = "settings")
public class Settings implements DatabaseEntryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "pk_settings")
    private Long id;

    @Column(name = "key_column")
    private String keyColumn;
    private String value;

    /**
     * default constructor
      */
    public Settings() {
    }


    /**
     * Constructor for the class
     *
     * @param keyColumn describes the property that will be defined in the value
     * @param value value of the key
     */
    public Settings(String keyColumn, String value) {
        this.keyColumn = keyColumn;
        this.value = value;
    }


    /**
     * The id as in the database
     *
     * @return the id
     */
    public Long getId() {
        return id;
    }

    public String getKeyColumn() {
        return keyColumn;
    }

    public void setKeyColumn(String keyColumn) {
        this.keyColumn = keyColumn;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Settings settings = (Settings) o;

        return id != null ? id.equals(settings.id) : settings.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
