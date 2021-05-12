package testSet;


import com.dcap.DCAPMain;
import com.dcap.filters.IDataFilter;
import com.dcap.filters.SubstitutePupilFilter;
import com.dcap.helper.MyLittleFactory;
import com.dcap.service.Exceptions.AbstractFilterException;
import com.dcap.service.Exceptions.NoSuchFilterException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DCAPMain.class)
public class Classfindertest {


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void testClass() throws NoSuchFilterException, AbstractFilterException {
        HashMap<String, String> actualParameters = new HashMap<>();
        actualParameters.put("left_pupil", "left");
        actualParameters.put("right_pupil", "right");
        IDataFilter substitutePupilFilter = MyLittleFactory.getFilter("SubstitutePupilFilter", null, actualParameters);
        assertThat(substitutePupilFilter, instanceOf(SubstitutePupilFilter.class));
    }

    @Test(expected = NoSuchFilterException.class)
    public void testAbsurdClass() throws NoSuchFilterException, AbstractFilterException {
        HashMap<String, String> actualParameters = new HashMap<>();

        IDataFilter substitutePupilFilter = MyLittleFactory.getFilter("AbstractChangingFilterle", null, actualParameters);
    }

    @Test(expected = AbstractFilterException.class)
    public void testAbstractClass() throws NoSuchFilterException, AbstractFilterException {
        HashMap<String, String> actualParameters = new HashMap<>();

        IDataFilter substitutePupilFilter = MyLittleFactory.getFilter("AbstractChangingFilter", null, actualParameters);
    }

    @Test
    public void ListTest() {
        List<String> listOfAllFilters = MyLittleFactory.getListOfAllFilters();
        assertTrue(listOfAllFilters.contains("AddLabelFilter"));
        assertTrue(listOfAllFilters.size()>=11);
    }

    @Test
    public void checkStatic() throws InterruptedException {
        List<String> listOfAllFilters = MyLittleFactory.getListOfAllFilters();
        System.out.println(listOfAllFilters.contains("AddLabelFilter"));
        Thread.sleep(1000);

        List<String> listOfAllFilters1 = MyLittleFactory.getListOfAllFilters();
        System.out.println(listOfAllFilters1.contains("AddLabelFilter"));
        Thread.sleep(1000);

        List<String> listOfAllFilters2 = MyLittleFactory.getListOfAllFilters();
        System.out.println(listOfAllFilters2.contains("AddLabelFilter"));
        Thread.sleep(1000);

        List<String> listOfAllFilters3 = MyLittleFactory.getListOfAllFilters();
        System.out.println(listOfAllFilters3.contains("AddLabelFilter"));
        Thread.sleep(1000);
    }


}
