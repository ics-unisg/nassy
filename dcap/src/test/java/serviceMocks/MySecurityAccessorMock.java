package serviceMocks;

import com.dcap.domain.User;
import com.dcap.security.MySecurityAccessorInterface;

import static testHelper.TestObjects.user;


public class MySecurityAccessorMock implements MySecurityAccessorInterface {


    @Override
    public User getCurrentUser() {
        System.out.println("GetPrincipal function");

        return user;
    }
}
