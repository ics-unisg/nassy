package com.dcap.filters;

import com.dcap.fileReader.*;
import com.dcap.helper.FileException;
import com.dcap.service.threads.FilterData;
import com.dcap.fileReader.*;
import com.dcap.helper.FileException;
import com.dcap.helper.MyLittleFactory;
import com.dcap.service.threads.FilterData;
import org.apache.commons.math3.stat.descriptive.UnivariateStatistic;
import org.apache.commons.math3.stat.descriptive.moment.Mean;

import javax.sound.sampled.DataLine;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author uli
 *
 * Class to calculate TEPR for given Files
 */
public class TEPRFilter extends AbstractSpecialFilters {



    private static final Map<String, ENUMERATED_TYPES> PARAMETERS;

    /**
     * defines the parameters:
     * name of the labeled column with informations for the TEPR setting (key is "labelColumn", value is the name of the column in the file)
     * name of the name column with for the experiments for the TEPR setting (key is "nameColumn", value is the name of the column in the file
     * time that should be removed after the stimulus because of the reaction on the light condition
     * statistics: list of all statistic as string, seperated with a simicolon (";")
     */
    static {
        PARAMETERS = new HashMap<>();
        PARAMETERS.put("labelColumn", ENUMERATED_TYPES.Double);
        PARAMETERS.put("nameColumn", ENUMERATED_TYPES.String);
        PARAMETERS.put("statistics", ENUMERATED_TYPES.String);
        PARAMETERS.put("period", ENUMERATED_TYPES.Long);
        PARAMETERS.put("columns", ENUMERATED_TYPES.List);
        PARAMETERS.put("statistics seperated by \";\"", ENUMERATED_TYPES.AddidtionalInfo);
        PARAMETERS.put("Amount of columns > 2", ENUMERATED_TYPES.AddidtionalInfo);
    }


    /**
     * List of all measures that should be applied to the trials
     */
    private List<UnivariateStatistic> meausures;



    /**
     * Period that should be ignored at the beginning of the response
     */
    private Long period;


    /**
     * name of the timestampcolumn in the csv
     */


    /**
     * constructor for the class
     * @param columns name of the columns that are required (as map)
     * @param parameter parameters that are needed to analyze the file
     *                    name of the labeled column with infomations for the TEPR setting (key is "labelColumn", value is the name of the column in the file)
     *                    name of the name column with for the experiments for the TEPR setting (key is "nameColumn", value is the name of the column in the file
     * @param meausures list of measures that should be calculate
     * @param period period that should be ignored at the beginning of the response
     */
    public TEPRFilter(Map<String, String> columns, Map<String, String> parameter, List<UnivariateStatistic> meausures, Long period) {
        super("TEPRFilter", columns, parameter);
        this.meausures=meausures;
        this.period = period;
        this.timeStampColumn=getTimeStampColumn();
    }

    /**
     * constructor for the class
     * @param columns name of the columns that are required (as map)
     * @param parameter parameters that are needed to analyze the file
     *                    name of the labeled column with informations for the TEPR setting (key is "labelColumn", value is the name of the column in the file)
     *                    name of the name column with for the experiments for the TEPR setting (key is "nameColumn", value is the name of the column in the file
     *                    period that should be ignored at the beginning of the response (key is "period", value is the time as long
     *                    meausures list of measures that should be calculate (key is "statistics", value is string is list of statistics, seperated by semicolons.
     */

    //TODO remove id and timestampcolumn
    public TEPRFilter(Map<String, String> columns, Map<String, String> parameter) {
        this(columns, parameter, null, Long.valueOf(parameter.get("period")));
        ArrayList<UnivariateStatistic> univariateStatistics = new ArrayList<>();
        String[] statistics = parameter.get("statistics").split(";");
        for (String stat : statistics) {
            UnivariateStatistic statistic = MyLittleFactory.getStatistic(stat);
            univariateStatistics.add(statistic);
        }
        this.meausures=univariateStatistics;
    }

    public TEPRFilter(){
        super(null, null, null);
    }

    public List<FilterData> run (List<FilterData> filterDataList) throws Exception {
        filterDataList=copyList(filterDataList);
        ArrayList<FilterData> result = new ArrayList<>();
        filterDataList=copyList(filterDataList);
        DataFile dataFile = new DataFile(";", true);
        for(FilterData filterData: filterDataList) {
            DataFileLine dataFileLine = dataFile.appendLine();
            DataFile file = filterData.getDataFile();
            DataFileColumn labelColumn = file.getHeader().getColumn(parameter.get("labelColumn"));
            List<String> labelColumnList = DataFileUtils.getRawValues(file, labelColumn, true);
            String[] labelColumnArray = labelColumnList.stream().toArray(String[]::new);

            DataFileColumn nameColumn = file.getHeader().getColumn(parameter.get("nameColumn"));
            List<String> nameColumnList = DataFileUtils.getRawValues(file, nameColumn, true);
            String[] nameColumnArray = nameColumnList.stream().toArray(String[]::new);

            DataFileColumn timeColumn = file.getHeader().getColumn(timeStampColumn);
            Long[] timeStampArray = DataFileUtils.getTimeStamps(file, timeColumn, true);

            HashMap<String, double[]> columnsWithValues= new HashMap<>();
            for(Map.Entry<String, String> column: getColumns().entrySet()){
                DataFileColumn valueColumnn = file.getHeader().getColumn(column.getValue());
                double[] pupilValues = DataFileUtils.getDoubleValues(file, valueColumnn, true);
                columnsWithValues.put(column.getKey(), pupilValues);
            }



            ArrayList<InnerTimeSlot> timeSlotForTeprList = extractTEPRUnits(labelColumnArray, nameColumnArray, timeStampArray, columnsWithValues);

            for (InnerTimeSlot timeSlotForTepr : timeSlotForTeprList) {
                timeSlotForTepr.baselineAndApplication(period);
            }

            DataFileColumn filename=null;
            try {
                filename = dataFile.getColumn("filename");
            }catch(FileException fe){
                filename=dataFile.appendColumn("filename");
            }
            dataFileLine.setValue(filename, filterData.getName());

            for (InnerTimeSlot timeSlotForTepr : timeSlotForTeprList) {
                String columnNameToAppend = "TEPR_baseline_" + timeSlotForTepr.getName();
                DataFileColumn column=null;
                try{
                    column= dataFile.getColumn(columnNameToAppend);
                }catch(FileException fe){
                    column=dataFile.appendColumn(columnNameToAppend);
                }
                dataFileLine.setValue(column, timeSlotForTepr.getBaseline());
            }

            for (UnivariateStatistic method : meausures) {
                for (InnerTimeSlot timeSlotForTepr : timeSlotForTeprList) {
                    Double resultOfMethod = timeSlotForTepr.applyMeasure(method);
                    String columnNameToAppend = "TEPR_" + method.getClass().getSimpleName() + "_" + timeSlotForTepr.getName();
                    DataFileColumn column=null;
                    try{
                        column= dataFile.getColumn(columnNameToAppend);
                    }catch(FileException fe){
                        column=dataFile.appendColumn(columnNameToAppend);
                    }
                    dataFileLine.setValue(column, resultOfMethod);
                }
            }

            SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("YYYY-MM-dd_HH-mm");
            String ending = DATE_FORMAT.format(new Date()) + ".csv";
            FilterData filterDataForList = new FilterData(dataFile, "resultTepr"+ending, null);
            result.add(filterDataForList);
            return result;

        }


        return  result;
    }



    private ArrayList<InnerTimeSlot> extractTEPRUnits (String[] labelColumnArray, String[]
            nameColumnArray, Long[] timeStampArray, HashMap<String, double[]> columns) throws DataFileException {
        ArrayList<InnerTimeSlot> innerTimeSlotsList = new ArrayList<>();
        String oldNameOfTrial = "";
        InnerTimeSlot actulaTimeSlot = null;

        for(int i =0; i<labelColumnArray.length; i++){
            String nameOfTrial = nameColumnArray[i].trim();
            if (!nameOfTrial.equals("")) {
                if (!nameOfTrial.equals(oldNameOfTrial)) {
                    actulaTimeSlot = new InnerTimeSlot(nameOfTrial);
                    innerTimeSlotsList.add(actulaTimeSlot);
                }
                Set<Map.Entry<String, double[]>> entries = columns.entrySet();
                String label = labelColumnArray[i];
                double val=0;
                for(Map.Entry<String, double[]>e:entries) {
                    val=val+e.getValue()[i];
                }

                val = val/entries.size();

                if (label.equals("PRE")) {
                    actulaTimeSlot.addPre(timeStampArray[i], val);
                } else if (label.equals("RES")) {
                    actulaTimeSlot.addRes(timeStampArray[i],val);
                }
                oldNameOfTrial = nameOfTrial;
            }
        }


        return innerTimeSlotsList;

    }

    @Override
    public Boolean isPreprocessing() {
        return false;
    }


    @Override
    public Map<String, ENUMERATED_TYPES> getRequiredParameters() {
        return PARAMETERS;
    }
}
