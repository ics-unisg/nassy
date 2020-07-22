package com.dcap.filters;

import com.dcap.fileReader.DataFile;
import com.dcap.fileReader.DataFileLine;
import com.dcap.helper.DoubleColumnException;
import com.dcap.service.threads.FilterData;
import com.dcap.fileReader.DataFile;
import com.dcap.fileReader.DataFileLine;
import com.dcap.helper.FileException;
import com.dcap.helper.FilterException;
import com.dcap.service.threads.FilterData;

import java.io.IOException;
import java.util.*;

/**
 * @author uli
 *
 * Abstract class for filters that are used to trim a file, i.e. to remove rowns out of the csv file
 */

public abstract class AbstractTrimFilter extends AbstractDataFilter {

    /**
     * Constructor of the class
     * @param name name of the filter
     * @param columns name of the columns as named in the csv, key is name in the experiment, value is name in the csv
     */
    public AbstractTrimFilter(String name, Map<String, String> columns,  Map<String, String> parameter) {
        super(name, columns, parameter);
    }


    /**
     * runs the filter, trims the file in the sense that it deletes rows which are marked with false in the {@link #createBooleanList(DataFile, Map)} function
     * @param data
     *            list of Filterdatata to be processed
     * @return string with information about the filter
     * @throws Exception
     */
    @Override
    public List<FilterData> run(List<FilterData> data) throws Exception {
        ArrayList<FilterData> returnList = new ArrayList<>();
        for(FilterData filterData: data) {
            String returnMessage = null;
            DataFile file = filterData.getDataFile();
            LinkedList<DataFileLine> content = file.getContent();
            List<DataFileLine> fileLinesToReturn = new LinkedList<>();
            Iterator<DataFileLine> iterator = content.iterator();

            List<Boolean> booleanList = createBooleanList(file, getColumns());
            int index = 0;
            while (iterator.hasNext()) {
                DataFileLine element = iterator.next();

                if (booleanList.get(index)) {
                    fileLinesToReturn.add(element);
                }
                index++;
            }


            returnMessage = getName() + " to intervall with label";

            file.setContent(fileLinesToReturn);
            FilterData filterData1 = new FilterData(file, filterData.getName() + "filtered", filterData.getUserData(), returnMessage);
            returnList.add(filterData1);
        }
        return  returnList;
    }

    /**
     * abstract method that can be used to define the rows that shoud be trimed. Be aware: false means that the rows will be deleted
     * @param file file that should be trimmed
     * @param columns map of the columns, key is the name of the column in the experiment, value is the name of the column in the experiment
     * @return list fo booleans that decides if you should delete the row or not. false to delete, true to append it to the new file
     * @throws IOException
     */
    protected abstract List<Boolean> createBooleanList(DataFile file, Map<String, String> columns) throws IOException, FilterException, FileException, DoubleColumnException;

    @Override
    public Boolean isPreprocessing() {
        return false;
    }
}
