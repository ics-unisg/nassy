package com.dcap.service;

import com.dcap.domain.*;
import com.dcap.repository.NotificationsInterface;
import com.dcap.repository.StudyInterface;
import com.dcap.repository.UsersInterface;
import com.dcap.service.Exceptions.RepoExeption;
import com.dcap.service.serviceInterfaces.SubjectServiceInterface;
import com.dcap.service.serviceInterfaces.UserServiceInterface;
import com.dcap.domain.*;
import com.dcap.repository.NotificationsInterface;
import com.dcap.repository.StudyInterface;
import com.dcap.repository.UsersInterface;
import com.dcap.service.Exceptions.RepoExeption;
import com.dcap.service.serviceInterfaces.SubjectServiceInterface;
import com.dcap.service.serviceInterfaces.UserDataServiceInterface;
import com.dcap.service.serviceInterfaces.  UserServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Service to manage the Users in the database
 *
 * @author uli
 */

@Service
public class UserService implements UserServiceInterface {

    private final UsersInterface userRepo;

    private final NotificationsInterface notificationService;

    private final SubjectServiceInterface subjectService;

    private final StudyInterface studyRepo;

    private final UserDataServiceInterface userDataService;

    private final PasswordEncoder pwe;


    @Autowired
    public UserService(UsersInterface userRepo, NotificationsInterface notificationRepo, SubjectServiceInterface subjectService, StudyInterface studyRepo, UserDataServiceInterface userDataService, PasswordEncoder pwe) {
        this.userRepo = userRepo;
        this.notificationService = notificationRepo;
        this.subjectService = subjectService;
        this.studyRepo = studyRepo;
        this.userDataService = userDataService;
        this.pwe = pwe;
    }


    /**
     * finds User for given id
     *
     * @param id id that belongs to User
     * @return User for given id
     */
    @Override
    public User getUserById(Long id) throws RepoExeption {
        User one = userRepo.findOne(id);
        if (one == null) {
            throw new RepoExeption("user not found");
        }
        one.setPassword(null);
        return one;
    }



    @Override
    public User save(User u) throws RepoExeption {

        User userByEmail = userRepo.findUserByEmail(u.getEmail());

        if (userByEmail != null) {
            throw new RepoExeption("User already in database");
        }
        String encode = pwe.encode(u.getPassword());
        u.setPassword(encode);
        User savedUser = userRepo.save(u);
        return savedUser;
    }

    @Override
    public User updatePassword(User user, String password) throws RepoExeption {
        User userByEmail = userRepo.findUserByEmail(user.getEmail());

        if (userByEmail == null) {
            throw new RepoExeption("User not in database");
        }
        String encode = pwe.encode(password);
        userByEmail.setPassword(encode);
        User savedUser = userRepo.save(userByEmail);
        savedUser.setPassword(null);
        return savedUser;
    }


    @Override
    public User findUserById(Long userId) throws RepoExeption {
        User user = userRepo.findOne(userId);
        if (user == null) {
            throw new RepoExeption("No user for given id.");
        }
        return user;
    }

    /**
     * deletes given User in the database, removes User from Study and deletes all notifications for User
     *
     * @param user User to be deleted
     */
    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public void delete(User user) {
        List<Notifications> notificationByUser = notificationService.findByUser(user);
        for (Notifications not : notificationByUser) {
            notificationService.delete(not);
        }
        List<Subject> allSubjectsForUser = subjectService.getAllSubjectsForUser(user);
        for(Subject s : allSubjectsForUser){
            subjectService.delete(s);
        }
        List<Study> studies = studyRepo.findByUser(user);
        for (Study st : studies) {
            studyRepo.delete(st.getId());
        }

        List<UserData> userDataByUser = userDataService.getUserDataByUser(user);
        for(UserData ud:userDataByUser){
            userDataService.delete(ud);
        }
        userDataService.detacheUserData(user);
        userRepo.delete(user);
    }

    /**
     * deletes given User in the database, removes User from Study and deletes all notifications for User
     *
     * @param id id of User to be deleted
     */
    @Override
    public Boolean delete(Long id) throws RepoExeption {
        User user = userRepo.findById(id);
        if(user==null){
            throw new RepoExeption("No such user...");
        }
        delete(user);
        return true;
    }


    @Override
    public User findUserByEmail(String username) {
        return userRepo.findUserByEmail(username);
    }

    public List<User> getAllUser() {
        List<User> all = userRepo.findAll();
        return all;
    }
}
