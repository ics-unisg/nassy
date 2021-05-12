package com.dcap.service.serviceInterfaces;

import com.dcap.fileReader.DataFile;
import com.dcap.transferObjects.GraphData;
import com.dcap.fileReader.DataFile;
import com.dcap.transferObjects.GraphData;

import java.util.List;

public interface DataServiceInterface {
    List<GraphData> isInList(Long id, String columnName, String timeStampColumn, Long userId);

    void addToList(Long id, String columnName, String timeStampColumn, List<GraphData> cachedData, Long userId);

    DataFile isInFileCache(Long id);

    void addToFileCache(Long id, DataFile dataFile);
}
