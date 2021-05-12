package com.dcap.repository;

import com.dcap.domain.UserData;
import com.dcap.domain.UserDataTags;
import com.dcap.domain.UserData;
import com.dcap.domain.UserDataTags;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserDataTagsInterface extends JpaRepository<UserDataTags,Long>{

    List<UserDataTags> findByUserdata(UserData userData);

}
