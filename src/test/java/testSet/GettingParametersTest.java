package testSet;

import com.dcap.DCAPMain;
import com.dcap.filters.AddLabelFilter;
import com.dcap.filters.ENUMERATED_TYPES;
import com.dcap.helper.MyLittleFactory;
import com.dcap.service.Exceptions.AbstractFilterException;
import com.dcap.service.Exceptions.NoSuchFilterException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DCAPMain.class)
public class GettingParametersTest {

    @Test
    public void getParametersFromAddLabelTest(){
        AddLabelFilter addLabelFilter = new AddLabelFilter();
        Map<String, ENUMERATED_TYPES> requiredParameters = addLabelFilter.getRequiredParameters();
        assertEquals(requiredParameters.get("timestampcolumn"),ENUMERATED_TYPES.String);
    }

    @Test
    public void getParametersFromAddLabelTwoTest() throws AbstractFilterException, NoSuchFilterException {
        Map<String, ENUMERATED_TYPES> requiredParameters = MyLittleFactory.getFilterParameters("AddLabelFilter");
        assertEquals(requiredParameters.get("timestampcolumn"),ENUMERATED_TYPES.String);
    }

    @Test
    public void getParametersFromBlinkDetectionFilterTest() throws AbstractFilterException, NoSuchFilterException {
        Map<String, ENUMERATED_TYPES> requiredParameters = MyLittleFactory.getFilterParameters("BlinkDetectionFilter");
        assertEquals(requiredParameters.get("columns"),ENUMERATED_TYPES.InParameter);
    }

}
