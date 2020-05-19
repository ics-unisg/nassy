package testSet;


import com.dcap.DCAPMain;
import com.dcap.domain.DataProcessing;
import com.dcap.domain.Study;
import com.dcap.service.DataProcessingService;
import com.dcap.service.Exceptions.RepoExeption;
import com.dcap.service.serviceInterfaces.StudyServiceInterface;
import com.dcap.transferObjects.EasyFilterRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DCAPMain.class)
public class DataProcessingTest {

    @Autowired
    DataProcessingService dataProcessingService;

    @Autowired
    StudyServiceInterface studyServiceInterface;

    String load ="{\"files\": [20796],\"filters\": [{\"name\": \"SubstitutePupilFilter\",\"actualParameters\": {\"left_pupil\": \"PupilLeft\"," +
            "\"right_pupil\": \"PupilRight\"},\"columns\": {\"timestampcolumn\": \"EyeTrackerTimestamp\"}},{\"name\": \"LinearInterpolationFilter\"," +
            "\"actualParameters\": {\"left_pupil\": \"PupilLeft\",\"right_pupil\": \"PupilRight\"},\"columns\": {\"timestampcolumn\": \"EyeTrackerTimestamp\"," +
            "\"left_pupil\": \"PupilLeft\",\"right_pupil\": \"PupilRight\"}}],\"decimalSeparator\": \",\"}";

    @Before
    public void addStudy(){
        Study toDelete = new Study("ToDelete", "", null, null);
        try {
            studyServiceInterface.save(toDelete);
        } catch (RepoExeption repoExeption) {
            repoExeption.printStackTrace();
        }

    }

    @After
    public void deleteStudy() throws RepoExeption {
        Study toDelete = studyServiceInterface.findStudyByName("ToDelete").get(0);
        studyServiceInterface.deleteStudy(toDelete);

    }

    @Test
    public void dataProcTest(){

        ObjectMapper objectMapper = new ObjectMapper();

        EasyFilterRequest easyFilterRequest=null;
        try {
            easyFilterRequest = objectMapper.readValue(load, EasyFilterRequest.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String jsonLoad = null;
        try {
            jsonLoad = objectMapper.writeValueAsString(easyFilterRequest);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        Study studyById = null;
        studyById = studyServiceInterface.findStudyByName("ToDelete").get(0);
        DataProcessing dataProcessing = new DataProcessing(studyById, "Test", "comment", jsonLoad);
        DataProcessing dataProcessing1 = dataProcessingService.saveOrUpdate(dataProcessing);
        Assert.assertNotNull(dataProcessing1);
        String trialComputationConfiguration = dataProcessing1.getTrialComputationConfiguration();
        EasyFilterRequest easyFilterRequest1 = null;
        try {
            easyFilterRequest1 = objectMapper.readValue(trialComputationConfiguration, EasyFilterRequest.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(easyFilterRequest1.getName());

    }

    @Test
    public void testIfExists(){
        DataProcessing dataProcessingsById = dataProcessingService.getDataProcessingsById(999l);
        Assert.assertNull(dataProcessingsById);

    }

}
