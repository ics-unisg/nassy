package com.dcap.filters;

import com.dcap.fileReader.DataFile;
import com.dcap.fileReader.DataFileColumn;
import com.dcap.fileReader.DataFileLine;
import com.dcap.fileReader.DataFileUtils;
import com.dcap.service.threads.FilterData;
import com.dcap.fileReader.DataFile;
import com.dcap.fileReader.DataFileColumn;
import com.dcap.fileReader.DataFileLine;
import com.dcap.fileReader.DataFileUtils;
import com.dcap.service.threads.FilterData;

import java.util.*;

/**
 * The type AverageFilter. Calculalates the average of the values of two or more columns and appends them as a own column.
 *
 * @author uli
 */
public class AverageFilter extends AbstractChangingFilter {

    private static final Map<String, ENUMERATED_TYPES> PARAMETERS;

    static {
        PARAMETERS = new HashMap<>();
        PARAMETERS.put("columns", ENUMERATED_TYPES.List);
        PARAMETERS.put("Amount of columns > 1", ENUMERATED_TYPES.AddidtionalInfo);
    }


    /**
     * Instantiates a new AverageFilter.
     *
     * @param columns    the columns
     * @param parameters the parameters
     */
    public AverageFilter(Map<String,String> columns, Map<String, String> parameters) {
        super("AverageFilter", columns, parameters);
    }

    public AverageFilter() {
        super("AverageFilter", null, null);
    }

    @Override
    public Map<String, ENUMERATED_TYPES> getRequiredParameters() {
        return PARAMETERS;
    }

    @Override
    public List<FilterData> run(List<FilterData> data) throws Exception {
        data=copyList(data);
        ArrayList<FilterData> returnList = new ArrayList<>();
        for(FilterData filterData: data) {
            DataFile file = filterData.getDataFile();
            if (getColumns().size() < 2) {
                return null;
            }
            setColumnsCountChanged(true);
            ArrayList<double[]> arrayOfData = new ArrayList<>();


            for(String col:getColumns().values()){
                DataFileColumn columnn = file.getHeader().getColumn(col);
                double[] values = DataFileUtils.getDoubleValues(file, columnn, true);
                arrayOfData.add(values);
            }
            file.appendColumn("average");
            List<Double> values = calcAverage(arrayOfData);
            DataFileColumn averageColumn = file.getHeader().getColumn("average");
            LinkedList<DataFileLine> content = file.getContent();
            Iterator<DataFileLine> iterator = content.iterator();
            int index =0;
            while (iterator.hasNext() && index < values.size()) {

                DataFileLine element = iterator.next();
                element.setValue(averageColumn, values.get(index));
                index++;
            }
            String columnsChanged="";
            if(isColumnCountChanged()){
                columnsChanged="; Column Count has changed";
            }


            String returnMessage = getName() + columnsChanged;

            FilterData filterData1 = new FilterData(file, filterData.getName(), filterData.getUserData(), filterData.getMessage() + "; " + returnMessage);
            returnList.add(filterData1);
        }
        return  returnList;

    }

    /**
     * @param data list of values that of the selected columns
     * @return <tt>double[]</tt> with the average values for each line
     */
    private List<Double> calcAverage(List<double[]> data) {
        int numberOfData = data.size();
        int length = data.get(0).length;
        ArrayList<Double> doubles = new ArrayList<>();
        for(int i =0; i<length; i++){
            double value=0;
            for(double[] ar:data){
                value+=ar[i];
            }
            doubles.add(value/data.size());
        }
        return doubles;
    }

    @Override
    public Boolean isPreprocessing(){
        return false;
    }
}