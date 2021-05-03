package configurationPackage;

import com.dcap.security.MySecurityAccessorInterface;
import com.dcap.service.serviceInterfaces.DataProcessingServiceInterface;
import com.dcap.service.serviceInterfaces.DataServiceInterface;
import com.dcap.service.serviceInterfaces.UserDataServiceInterface;
import org.junit.Before;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import serviceMocks.MyDataProcessingService;
import serviceMocks.MyDataService;
import serviceMocks.MySecurityAccessorMock;
import serviceMocks.MyUserDataService;

@ContextConfiguration
@SpringBootApplication
//@Import({DemoApplication.class})
@PropertySource("/test.properties")
@EnableJpaRepositories(basePackages = {"com.dcap.repository"}, entityManagerFactoryRef = "")
@EntityScan(basePackages = {"com.dcap.domain"})
@ComponentScan(basePackages = {"com.dcap.service","com.dcap.rest", "com.dcap.repository"})
public class MyTestConfiguration {

    @Primary
    @Bean
    MySecurityAccessorInterface mySecurityAccessor(){
        return new MySecurityAccessorMock();
    }

    @Primary
    @Bean
    DataProcessingServiceInterface myDataProcessingService(){
        return new MyDataProcessingService();
    }

    @Primary
    @Before
    DataServiceInterface myDataService(){
        return new MyDataService();
    }

    @Primary
    @Bean
    UserDataServiceInterface myUserDataService(){
        return new MyUserDataService();
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder(11);
    }

}
