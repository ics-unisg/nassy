package com.dcap.filters;

import com.dcap.fileReader.DataFile;
import com.dcap.fileReader.DataFileColumn;
import com.dcap.fileReader.DataFileLine;
import com.dcap.service.threads.FilterData;
import com.dcap.fileReader.DataFile;
import com.dcap.fileReader.DataFileColumn;
import com.dcap.fileReader.DataFileLine;
import com.dcap.service.threads.FilterData;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Filter to trim files
 *
 * @author uli
 */

public class TrimFilter extends AbstractTrimFilter {

    private String labelColumn;
    private List<String> label;
    private Long start;
    private Long end;

    private static final Map<String, ENUMERATED_TYPES> PARAMETERS;


    /**
     * defines the parameters:
     * timestampcolumn: name of the timestampcolumn in the csv
     * label: name of the column with the labels in the csv to seperate
     * start; value in the timestampcolumn in the csv
     * end; value in the timestampcolumn in the csv
     *
     * NOTE: ONLY label OR start and end should be setted
     */
    //TODO check parameters
    static{
        PARAMETERS=new HashMap<>();
        PARAMETERS.put("timestampcolumn", ENUMERATED_TYPES.String);
        PARAMETERS.put("labelcolumn", ENUMERATED_TYPES.String);
        PARAMETERS.put("label", ENUMERATED_TYPES.String);
        PARAMETERS.put("start", ENUMERATED_TYPES.Long);
        PARAMETERS.put("end", ENUMERATED_TYPES.Long);
        PARAMETERS.put("columns", ENUMERATED_TYPES.InParameter);

    }

    public TrimFilter(Map<String, String> columns, Map<String, String> parameter){
        //this(null);
        super("TrimFilter",columns, parameter);
        this.timeStampColumn=parameter.get("timestampcolumn");
        if(parameter.get("label")!=null && !parameter.get("label").equals("")){
            this.label=java.util.Arrays.asList(parameter.get("label").split(";"));
            this.label = this.label.stream().map(e -> e.trim()).collect(Collectors.toList());
            this.labelColumn=parameter.get("labelcolumn");
        }else{
            this.start=Long.valueOf(parameter.get("start"));
            this.end=Long.valueOf(parameter.get("end"));
        }
    }

/*    private TrimFilter(String labelColumn){
        super("TrimFilter", null, parameter);
        this.timeStampColumn=getTimeStampColumn();
        this.labelColumn=labelColumn;
    }*/

    public TrimFilter(){
        super(null, null, null);
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
            String returnMessage = null;
            DataFile file = filterData.getDataFile();
            LinkedList<DataFileLine> content = file.getContent();
            List<DataFileLine> fileLinesToReturn = new LinkedList<>();
            Iterator<DataFileLine> iterator = content.iterator();
            if (label == null) {
                int index = 0;
                DataFileColumn timeColumn = file.getHeader().getColumn(timeStampColumn);
                while (iterator.hasNext()) {
                    DataFileLine element = iterator.next();

                    Long timeStamp = element.getLong(timeColumn);
                    if (timeStamp >= start && timeStamp <= end) {
                        fileLinesToReturn.add(element);
                    }
                    if (timeStamp > end) {
                        break;
                    }

                }

                returnMessage = getName() + " to intervall between " + start + " and " + end;
            } else {
                DataFileColumn column = file.getHeader().getColumn(labelColumn);
                while (iterator.hasNext()) {
                    DataFileLine element = iterator.next();

                    String labelValue = element.getStringOfColumn(column);
                    if (label.contains(labelValue)) {
                        fileLinesToReturn.add(element);
                    }

                }

                returnMessage = getName() + " to intervall with label \"" + label + "\"";
            }
            file.setContent(fileLinesToReturn);
            String name = filterData.getName();

            name = name.indexOf(".") > 0 ? name.substring(0, name.lastIndexOf(".")) : name;
            FilterData filterData1 = new FilterData(file, name + "filtered.tsv", filterData.getUserData(), returnMessage);
            returnList.add(filterData1);
        }
        return  returnList;
    }

    @Override
    protected List<Boolean> createBooleanList(DataFile file, Map<String, String> columns) {
        return null;
    }
}
