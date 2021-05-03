package com.dcap.service.serviceInterfaces;

import com.dcap.domain.Subject;
import com.dcap.domain.User;
import com.dcap.domain.UserData;
import com.dcap.service.Exceptions.RepoExeption;
import com.dcap.domain.Subject;
import com.dcap.domain.User;
import com.dcap.domain.UserData;
import com.dcap.service.Exceptions.RepoExeption;

import javax.transaction.Transactional;
import java.util.List;

public interface UserDataServiceInterface {
    UserData getUserDataById(Long id) throws RepoExeption;

    UserData saveOrUpdate(UserData userData);

    void delete(UserData userData);

    @Transactional(value = Transactional.TxType.REQUIRED)
    void deleteUserDataForSubject(Subject subject);

    List<UserData> getUserDataByUser(User user);

    List<UserData> getUserDataBySubject(Subject subject);

    UserData getByName(String name, Long userId) throws RepoExeption;

    List<UserData> getHiddenUserdataBySubject(Subject user);

    UserData getUserDataByTaskId(String taskId) throws RepoExeption;

    boolean cleanAll();

    void detacheUserData(User user);
}
