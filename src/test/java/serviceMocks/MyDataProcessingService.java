package serviceMocks;

import com.dcap.domain.DataProcessing;
import com.dcap.domain.Study;
import com.dcap.domain.User;
import com.dcap.service.serviceInterfaces.DataProcessingServiceInterface;
import static testHelper.TestObjects.user;
import static testHelper.TestObjects.study;

import java.util.List;

public class MyDataProcessingService implements DataProcessingServiceInterface {


    @Override
    public List<DataProcessing> getAllDataProcessings() {
        return null;
    }

    @Override
    public List<DataProcessing> getDataProcessingsByStudy(Study study) {
        return null;
    }

    @Override
    public DataProcessing getDataProcessingsById(Long id) {
        if(id.equals(1l)){


            DataProcessing dataProcessing = new DataProcessing(study, "eins", "zwei", "drei");
            dataProcessing.setId(999l);
            return dataProcessing;
        }

        return null;
    }

    @Override
    public void deleteDataProcessing(DataProcessing dataProcessing) {

    }

    @Override
    public DataProcessing saveOrUpdate(DataProcessing dataProcessing) {
        return null;
    }
}
