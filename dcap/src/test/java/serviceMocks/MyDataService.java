package serviceMocks;

import com.dcap.fileReader.DataFile;
import com.dcap.service.serviceInterfaces.DataServiceInterface;
import com.dcap.transferObjects.GraphData;

import java.util.List;

import static testHelper.TestObjects.graphData;

public class MyDataService implements DataServiceInterface {




    @Override
    public List<GraphData> isInList(Long id, String columnName, String timeStampColumn, Long userId) {
        if(id.equals(new Long(3)) && columnName.equals("name") && timeStampColumn.equals("timeStampColumn") && userId.equals(new Long(2))) {
            return graphData;
        }
        return null;
    }

    @Override
    public void addToList(Long id, String columnName, String timeStampColumn, List<GraphData> cachedData, Long userId) {
        System.err.println("added to list");
    }

    @Override
    public DataFile isInFileCache(Long id) {
        return null;
    }

    @Override
    public void addToFileCache(Long id, DataFile dataFile) {

    }
}
