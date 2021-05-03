package com.dcap.repository;

import com.dcap.domain.Pythoncode;
import com.dcap.domain.Pythoncode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PythonCodeInterface extends JpaRepository<Pythoncode, Long> {

    public Pythoncode findByName(String name);
}
