package com.dcap.filters;

import com.dcap.analyzer.TimeFrame;
import com.dcap.fileReader.*;
import com.dcap.service.threads.FilterData;
import com.dcap.analyzer.TimeFrame;
import com.dcap.fileReader.*;
import com.dcap.service.Exceptions.TimePhaseException;
import com.dcap.service.threads.FilterData;

import java.util.*;


/**
 * Class to extend file with labels
 *
 * @author uli
 *
 */
public class AddLabelFilter extends AbstractChangingFilter {

    private String label;

    private static final Map<String, ENUMERATED_TYPES> PARAMETERS;





    static {
        PARAMETERS = new HashMap<>();
        PARAMETERS.put("phases", ENUMERATED_TYPES.String);
        PARAMETERS.put("timestampcolumn", ENUMERATED_TYPES.String);
        PARAMETERS.put("label", ENUMERATED_TYPES.String);
        PARAMETERS.put("Phases should be a triple with start timestamp, end timestamp, label, seperarated by comma," +
                "and those seperated by semiclon", ENUMERATED_TYPES.AddidtionalInfo);
        PARAMETERS.put("The parameter label is optional, if it is not set, the column is named \"label\"", ENUMERATED_TYPES.AddidtionalInfo);
    }

    /**
     * Construtctor for the AbstratctChangingFilter
     *
     * @param columns not needed in that case
     * @param parameter contains the name of the timestampcolumn,
     *                  the name of the label (if not set, the default name "label" is chosen),
     *                  the triple with start-time, end-time and name.
     */
    public AddLabelFilter(Map<String, String> columns, Map<String, String> parameter) {
        super("AddLabelFilter", columns, parameter);
        this.parameter=parameter;
        this.timeStampColumn=getTimeStampColumn();
        String label = parameter.get("label");
        if(label==null){
            this.label="label";
        }else {
            this.label=label;
        }
        this.toPreprocess=false;
    }

    /**
     * Empty default constructor
     */
    public AddLabelFilter(){
        super(null, null, null);
        this.toPreprocess=false;
    }

    @Override
    public Map<String, ENUMERATED_TYPES> getRequiredParameters() {
        return PARAMETERS;
    }

    @Override
    public List<FilterData> run(List<FilterData> data) throws Exception {
        data=copyList(data);
        ArrayList<FilterData> returnList = new ArrayList<>();
        String phases = parameter.get("phases");
        List<TimeFrame> timeFrames = getTimeFrames(phases);
        for(FilterData filterData: data) {
            DataFile file = filterData.getDataFile();
            DataFileHeader header = file.getHeader();
            DataFileColumn timestampColumn = header.getColumn(timeStampColumn);
            Long[] timeStamps = DataFileUtils.getTimeStamps(file, timestampColumn, true);
            file.appendColumn(label);

            DataFileColumn labelColumn = file.getHeader().getColumn(label);
            LinkedList<DataFileLine> content = file.getContent();
            Iterator<DataFileLine> iterator = content.iterator();
            int index =0;
            while (iterator.hasNext() && index < timeStamps.length) {

                DataFileLine element = iterator.next();

                Long timeStamp = timeStamps[index];

                boolean inTimePhases = isInTimePhases(timeStamp, timeFrames);
                if(timeFrames.isEmpty()){
                    break;
                }
                if(inTimePhases){
                    element.setValue(labelColumn, timeFrames.get(0).getLabel());
                    System.out.println(timeFrames.get(0).getLabel());
                }

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


    private List<TimeFrame> getTimeFrames(String phases) throws TimePhaseException {
        ArrayList<TimeFrame> timeFrameList = new ArrayList<>();
        String[] rawPhases = null;
        if (phases.contains(";")) {
            rawPhases = phases.split(";");
        }else{
            rawPhases=new String[]{phases};
        }
        for (String p : rawPhases) {
            String[] phaseComponents = p.split(",");
            Long start = Long.valueOf(phaseComponents[0].trim());
            Long end = Long.valueOf(phaseComponents[1].trim());
            String label = phaseComponents[2].trim();
            TimeFrame timeFrame = new TimeFrame(start, end, label);
            timeFrameList.add(timeFrame);
        }

        Collections.sort(timeFrameList, new Comparator<TimeFrame>() {
            @Override
            public int compare(TimeFrame first, TimeFrame second) {
                // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                return first.getStart() < second.getStart() ? -1 : ((first.getStart() > second.getStart()) ? 1 : 0);
            }
        });

        checkList(timeFrameList);

        return timeFrameList;
    }

    private void checkList(ArrayList<TimeFrame> timeFrameList) throws TimePhaseException {
        Long tmp = 0l;
        for(TimeFrame tf: timeFrameList){
            if(tf.getStart()>tf.getEnd()){
                throw  new TimePhaseException("Error in timephase, start greater than end.");
            }
            if(tf.getStart()<=tmp){
                throw  new TimePhaseException("Error in timephase, overlapping phases.");
            }
            tmp=tf.getEnd();
        }
    }

    private boolean isInTimePhases(Long timeStamp, List<TimeFrame> timeFrames) {
        TimeFrame timeFrame = timeFrames.get(0);
        if(timeStamp>=timeFrame.getStart() && timeStamp<=timeFrame.getEnd()){
            return true;
        }
        if(timeFrame.getEnd()<timeStamp){
            timeFrames.remove(0);
        }
        return false;
    }

}
