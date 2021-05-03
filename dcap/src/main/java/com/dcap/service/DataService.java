package com.dcap.service;

import com.dcap.fileReader.DataFile;
import com.dcap.transferObjects.GraphData;
import com.dcap.fileReader.DataFile;
import com.dcap.service.serviceInterfaces.DataServiceInterface;
import com.dcap.transferObjects.GraphData;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * Class for caching data for sending to the frontend.
 *
 * @author uli
 */
@Service
public class DataService implements DataServiceInterface {


    /**
     * Class for storing the Data. Works a LRU-dataCache. If the dataCache is full and a new element ist pushed, it removes the
     * last accessed object
     * @param <K> key
     * @param <V> value
     */
    static class LRUcache<K,V> extends LinkedHashMap<K,V>{

        private static final long serialVersionUID = 1L;

        /**
         * capacity for defining the maximal amount of data cached
         */
        private final int capacity;

        @Override
        protected boolean removeEldestEntry(java.util.Map.Entry<K, V> eldest) {
            return (this.size() > this.capacity);
        }

        /**
         * Constructor of the Class
         * @param capacity specifies the amount of the objects that can be kept.
         */
        public LRUcache(int capacity) {
            super(2, 0.75f, true);
            this.capacity = capacity;
        }




    }
    /**
     * Cache for keeping the objects, initialized with a capacity of 30
     */
    private static LRUcache<Integer,CachedData> dataCache = new LRUcache<>(30);
    /**
     * Cache for Datafiles, for the case that you need more columns of a single file
     */
    private static LRUcache<Long,DataFile> fileCache = new LRUcache<>(3);

    /**
     * Function to check if Data are already in the dataCache.
     * @param id id of the Datafile
     * @param columnName name of the column in the file of the that is from interest
     * @param timeStampColumn name of the timestampcolumn in the file
     * @return Data if the are in the dataCache, otherwise <tt>null</tt>
     */
    @Override
    public List<GraphData> isInList(Long id, String columnName, String timeStampColumn, Long userId){

        for(Map.Entry<Integer, CachedData> entry: dataCache.entrySet()){
            if(entry.getValue().hashCode()==Objects.hash(id, columnName,timeStampColumn, userId)){
                return entry.getValue().getData();
            }
        }
        return null;
    }

    /**
     * Function to put the Data in the dataCache.
     * @param id id of the Datafile
     * @param columnName name of the column in the file
     * @param timeStampColumn name of the timestampcolumn in the file
     * @param cachedData the extracted data of the file
     * @param userId the id of the user that owns the data
     */
    @Override
    public void addToList(Long id, String columnName, String timeStampColumn, List<GraphData> cachedData, Long userId){
        CachedData cachedDataToAdd = new CachedData(id, columnName, timeStampColumn, cachedData, userId);
        dataCache.put(cachedData.hashCode(), cachedDataToAdd);
    }

    @Override
    public DataFile isInFileCache(Long id) {
        for(Map.Entry<Long, DataFile> entry: fileCache.entrySet()){
            if(entry.getKey().equals(id)){
                return entry.getValue();
            }
        }
        return null;
    }

    @Override
    public void addToFileCache(Long id, DataFile dataFile) {
        fileCache.put(id, dataFile);
    }


}
