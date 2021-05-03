package com.dcap.service;

import com.dcap.domain.Subject;
import com.dcap.domain.User;
import com.dcap.domain.UserData;
import com.dcap.domain.UserDataTags;
import com.dcap.repository.UserDataInterface;
import com.dcap.repository.UserDataTagsInterface;
import com.dcap.service.Exceptions.RepoExeption;
import com.dcap.domain.Subject;
import com.dcap.domain.User;
import com.dcap.domain.UserData;
import com.dcap.domain.UserDataTags;
import com.dcap.repository.UserDataInterface;
import com.dcap.repository.UserDataTagsInterface;
import com.dcap.service.Exceptions.RepoExeption;
import com.dcap.service.serviceInterfaces.UserDataServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service to manage the UserData in the database
 *
 * @author uli
 */
@Service
public class UserDataService implements UserDataServiceInterface {


    private final UserDataInterface userDataRepo;
    private final UserDataTagsInterface userDataTagRepo;


    @Autowired
    public UserDataService(UserDataInterface userDataRepo, UserDataTagsInterface userDataTagRepo) {
        this.userDataRepo = userDataRepo;
        this.userDataTagRepo = userDataTagRepo;
    }



    /**
     * finds UserData for given id
     * @param id id of the UserData
     * @return UserData for given id
     */
    @Override
    public UserData getUserDataById(Long id) throws RepoExeption {
        UserData byId = userDataRepo.findById(id);
        if(byId==null || byId.isHidden()==true){
            throw new RepoExeption("No data for id");
        }
        return byId;
    }

    /**
     *saves or updates UserData items in the database
     * @param userData UserData item to be saved or updated
     * @return UserData item saved in the database. Attention: if you create a new Object, the ID is only created by saving it in the database!
     */
    @Override
    public UserData saveOrUpdate(UserData userData){
        return userDataRepo.save(userData);
    }

    /**
     * deletes given UserData in the database, sets pointer in derived UserData and in SessionVideos to <code>null</code>,
     * deletes all connected UserDataTags
     * @param userData userData of TimePhase to be deleted
     */
    @Override
    public void delete(UserData userData){
        List<UserDataTags> userDataTags = userDataTagRepo.findByUserdata(userData);
        for (UserDataTags udt:userDataTags){
            userDataTagRepo.delete(udt);
        }

        List<UserData> userDataParent = userDataRepo.findByDerived(userData);
        for(UserData ud: userDataParent){
            ud.setDerived(null);
            userDataRepo.save(ud);
        }
        userData.setHidden(true);
        saveOrUpdate(userData);

        /*List<UserData> userDataParent = userDataRepo.findByDerived(userData);
        for(UserData ud: userDataParent){
            ud.setDerived(null);
            userDataRepo.save(ud);
        }
        String path = userData.getPath();
        File file = new File(path);
        file.delete();
        userDataRepo.delete(userData);*/
    }

    /**
     * Deletes all UserData for a given subject
     * @param subject Subject which UserData should be deleted
     */
    @Override
    @Transactional(value = Transactional.TxType.REQUIRED)
    public void deleteUserDataForSubject(Subject subject) {
        List<UserData> bySubject = userDataRepo.findBySubject(subject);
        for(UserData ud:bySubject){
            delete(ud);
        }
    }


    @Override
    public List<UserData> getUserDataByUser(User user) {
        List<UserData> byUser = userDataRepo.findByUser(user);
        List<UserData> returnList = byUser.stream().filter(e -> (!e.isHidden())).collect(Collectors.toList());

        return returnList;
    }

        /**
         * finds all UserData for given User
         * @param user user that owns the UserData
         * @return list of all UserData that belongs to a given User
         */
    //TODO Maybe it is possible to shorten this function (only get the data via user)
    /**
    @Transactional
    public List<UserData> getUserDataByUser(User user) {
        Set<Study> studies = st.getStudies();
        List<Subject> subjects=new ArrayList<>();
        for(Study st:studies){
            List<Subject> subs = subjectRepo.getSubjectByStudy(st);
            subjects.addAll(subs);
        }
        List<UserData>userDataList=new ArrayList<>();
        for(Subject subject:subjects){
            List<UserData> bySubject = userDataRepo.findBySubject(subject);
            userDataList.addAll(bySubject);
        }
        List<UserData> byUser = userDataRepo.findByUser(user);
        for(UserData u:byUser){
            if(!userDataList.contains(u)){
                userDataList.add(u);
            }
        }
        return  userDataList;
    }**/


    /**
     * Gets list of userdata by subject.
     *
     * @param subject the subject that is connected to the userdata
     * @return the list with userdata
     */
    @Override
    public List<UserData> getUserDataBySubject(Subject subject){

        List<UserData> bySubject = userDataRepo.findBySubject(subject);
        List<UserData> returnList = bySubject.stream().filter(e -> (!e.isHidden())).collect(Collectors.toList());
        return returnList;
    }


    /**
     * Gets a userData by name.
     *x
     * @param name the name
     * @param userId userId of the user that owns the data
     * @return the by name
     * @throws RepoExeption the RepoException
     */
    @Override
    public UserData getByName(String name, Long userId) throws RepoExeption {
        List<UserData> byFilename = userDataRepo.findByFilenameBeginning(name, userId);
        if(byFilename.size()!=1){
            throw new RepoExeption("Could not find exactly one userdata for " + name);
        }
        UserData userData = byFilename.get(0);
        if(userData.isHidden()==true){
            throw new RepoExeption("No userdata for given name");
        }
        return userData;

    }

    @Override
    public List<UserData> getHiddenUserdataBySubject(Subject subject){
        return userDataRepo.findHiddenBySubject(subject.getId());
    }

    @Override
    public UserData getUserDataByTaskId(String taskId) throws RepoExeption {
        UserData userData = userDataRepo.findByTaskId(taskId);
        if(userData==null || userData.isHidden()==true){
            throw new RepoExeption("No userdata for given number");
        }
        return userData;
    }

    @Override
    public boolean cleanAll() {
        List<UserData> byHiddenTrue = userDataRepo.findByHiddenTrue();
        for(UserData userData: byHiddenTrue){
            String path = userData.getPath();
            File file = new File(path);
            file.delete();
            userDataRepo.delete(userData);
        }
        return true;
    }

    @Override
    public void detacheUserData(User user){
        List<UserData> byUser = userDataRepo.findByUser(user);
        for(UserData ud:byUser) {
            ud.setUser(null);
            userDataRepo.save(ud);
        }

    }
}
