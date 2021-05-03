package serviceMocks;

import com.dcap.domain.ENUMERATED_CATEGORIES;
import com.dcap.domain.Subject;
import com.dcap.domain.User;
import com.dcap.domain.UserData;
import com.dcap.service.Exceptions.RepoExeption;
import com.dcap.service.serviceInterfaces.UserDataServiceInterface;

import static testHelper.TestObjects.userData;

import java.util.List;


public class MyUserDataService implements UserDataServiceInterface {



    @Override
    public UserData getUserDataById(Long id) {
        if(id.equals(userData.getUser().getId())){
            return userData;
        }
        return null;
    }

    @Override
    public UserData saveOrUpdate(UserData userData) {
        return null;
    }

    @Override
    public void delete(UserData userData) {

    }

    @Override
    public void deleteUserDataForSubject(Subject subject) {

    }

    @Override
    public List<UserData> getUserDataByUser(User user) {
        return null;
    }

    @Override
    public List<UserData> getUserDataBySubject(Subject subject) {
        return null;
    }

    @Override
    public UserData getByName(String name, Long userId) throws RepoExeption {
        return null;
    }

    @Override
    public List<UserData> getHiddenUserdataBySubject(Subject user) {
        return null;
    }


    @Override
    public UserData getUserDataByTaskId(String taskId) throws RepoExeption {
        return null;
    }

    @Override
    public boolean cleanAll() {
        return false;
    }

    @Override
    public void detacheUserData(User user) {
    }
}
