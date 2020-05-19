package com.dcap.repository;

import com.dcap.domain.Subject;
import com.dcap.domain.User;
import com.dcap.domain.UserData;
import com.dcap.domain.Subject;
import com.dcap.domain.User;
import com.dcap.domain.UserData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface UserDataInterface extends JpaRepository<UserData, Long>{

    public UserData findById(Long id);

    public List<UserData> findByDerived(UserData userData);

    List<UserData> findBySubject(Subject subject);

    List<UserData> findByUser(User user);

    List<UserData> findByHiddenTrue();

    @Query(value = "select * from user_data u where u.hidden=1 and u.fk_subject=?1",nativeQuery = true)
    List<UserData> findHiddenBySubject(Long id);

    @Query(value = "select * from user_data u where u.filename = CONCAT(?1, '.csv') or u.filename  = CONCAT(?1, '.tsv') and u.fk_user=?2", nativeQuery = true)
    List<UserData> findByFilenameBeginning( String filename, Long userId);

    UserData findByTaskId(String taskId);

}
