package com.dcap.filters;

import com.dcap.fileReader.*;
import com.dcap.service.threads.FilterData;
import com.dcap.fileReader.*;
import com.dcap.helper.MyLittleFactory;
import com.dcap.service.threads.FilterData;
import org.apache.commons.math3.stat.descriptive.UnivariateStatistic;
import org.apache.commons.math3.stat.descriptive.moment.Mean;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Class to calculate measures for given timeslots
 *
 * @author uli
 *
 */
public class StatisticFilter extends AbstractChangingFilter {

    /**
     * List of all measures that should be applied to a values of labeld parts of a given column
     */
    private List<UnivariateStatistic> meausures;

    /**
     * parameters that are needed to analyze the file
     * this is
     * name of the labeled column (key is "labeledColumn", value is the name of the column in the file)
     * name of the labels that should be investigated (key is "labels", value is a string with all labels seperated with commas (","))
     */

    /**
     * string to inform about the baseline
     */
    private String baselineMessage = "";

    /**
     * Parameters that are needed to construct the class
     */
    private static final Map<String, ENUMERATED_TYPES> PARAMETERS;

    /**
     * defines the parameters:
     * labelColumn: name of the column with the labels in the csv
     * labels; list of all labels in the file, all lables should be seperated by a semicolon (";")
     * statistics: list of all statistic as string, seperated with a simicolon (";")
     */
    static {
        PARAMETERS = new HashMap<>();
        PARAMETERS.put("labelColumn", ENUMERATED_TYPES.String);
        PARAMETERS.put("labels", ENUMERATED_TYPES.String);
        PARAMETERS.put("statistics", ENUMERATED_TYPES.String);
        PARAMETERS.put("labels seperated by \";\"", null);
        PARAMETERS.put("statistics seperated by \";\"", null);
        PARAMETERS.put("Amount of columns > 2", null);
    }


    /**
     * constructor for the class
     *
     * @param name            name of the filter
     * @param columns         name of the columns that are required (as map)
     * @param parameter       parameters that are needed to analyze the file
     *                        name of the labeled column (key is "labeledColumn", value is the name of the column in the file)
     *                        name of the labels that should be investigated (key is "labels", value is a string with all labels seperated with commas (","))
     * @param meausures       list of measures that should be calculated
     */
    public StatisticFilter(String name, Map<String, String> columns, Map<String, String> parameter, List<UnivariateStatistic> meausures) {
        super(name, columns, parameter);
        this.meausures = meausures;
        this.parameter = parameter;
    }

    public StatisticFilter(Map<String, String> columns, Map<String, String> parameter) {
        super("StatisticFilter", columns, parameter);
        ArrayList<UnivariateStatistic> univariateStatistics = new ArrayList<>();
        String[] statistics = parameter.get("statistics").split(";");
        for (String stat : statistics) {
            UnivariateStatistic statistic = MyLittleFactory.getStatistic(stat.trim());
            univariateStatistics.add(statistic);
        }
        this.meausures = univariateStatistics;
    }

    /**
     * @param data data that should be proceeded
     * @return string with informations about the process
     * @throws Exception
     */
    @Override
    public List<FilterData> run(List<FilterData> data) throws Exception {
        data=copyList(data);
        ArrayList<FilterData> returnList = new ArrayList<>();
        for (FilterData filterData : data) {
            DataFile file = filterData.getDataFile();
            DataFileHeader header = file.getHeader();

            Map<String, String> columns = getColumns();
            for (Map.Entry<String, String> entry : columns.entrySet()) {

                DataFileColumn ValueColumnn = file.getHeader().getColumn(entry.getValue());
                double[] values = DataFileUtils.getDoubleValues(file, ValueColumnn, true);
                DataFileColumn labelColumn = file.getHeader().getColumn(parameter.get("labelColumn"));
                List<String> labelColumnList = DataFileUtils.getRawValues(file, labelColumn, true);
                String[] labelColumnArray = labelColumnList.stream().toArray(String[]::new);
                String[] labelsTemp = parameter.get("labels").split(";");
                List<String> labels = Arrays.asList(labelsTemp);
                TimeSlotForMeasures timeSlots = extractTimeSlots(labelColumnArray, values, labels, entry.getKey());


                for (UnivariateStatistic method : meausures) {
                    //calc
                    timeSlots = calculateMeasure(method, timeSlots, null);
                    String columnNameToAppend = "STAT_" + method.getClass().getSimpleName() + "_" + entry.getKey();
                    file.appendColumn(columnNameToAppend);
                    DataFileColumn newMeasureColumn = file.getHeader().getColumn(columnNameToAppend);
                    LinkedList<DataFileLine> content = file.getContent();
                    Iterator<DataFileLine> iterator = content.iterator();
                    int index = 0;
                    Map<String, Double> resultMap = timeSlots.getResults();
                    while (iterator.hasNext() && index < labelColumnList.size()) {

                        DataFileLine element = iterator.next();
                        String s = labelColumnList.get(index);
                        Double aDouble = resultMap.get(s);
                        if (aDouble != null) {
                            element.setValue(newMeasureColumn, aDouble);
                        }
                        index++;
                    }
                }
            }
            String columnsChanged = "";
            if (isColumnCountChanged()) {
                columnsChanged = "; Column Count has changed";
            }
            String appliedMeasures = "";
            for (UnivariateStatistic uvs : meausures) {
                appliedMeasures += uvs.getClass().getSimpleName() + "; ";
            }

            if (baselineMessage.trim().equals("")) {
                baselineMessage = "No baseline was defined";
            }
            String returnMessage = getName() + "; Applied filters are: " + appliedMeasures + columnsChanged + baselineMessage;
            FilterData filterData1 = new FilterData(file, filterData.getName(), filterData.getUserData(), filterData.getMessage() + "; " + returnMessage);
            returnList.add(filterData1);
        }
        return returnList;
    }

    /**
     * function to extract the timeslots that should be used for the calculation
     *
     * @param labelColumnArray array with the labels as found in the csv
     * @param values           array of all values as doubles as found in csv
     * @param labels           labels that should be extracted
     * @param columnName       name of the column in the experiment
     * @return {@link TimeSlotForMeasures} with the values
     * @throws DataFileException
     */
    private TimeSlotForMeasures extractTimeSlots(String[] labelColumnArray, double[] values, List<String> labels, String columnName) throws DataFileException {
        if (labelColumnArray.length != values.length) {
            throw new DataFileException("arrays not same length");
        }
        Map<String, List<Double>> timeSlots = new HashMap<String, List<Double>>();
        for (String l : labels) {
            timeSlots.put(l, new ArrayList<Double>());
        }
        for (int i = 0; i < labelColumnArray.length; i++) {
            if (labels.contains(labelColumnArray[i])) {
                timeSlots.get(labelColumnArray[i]).add(values[i]);
            }
        }
        return new TimeSlotForMeasures(columnName, timeSlots);
    }


    /**
     * calcuates the measure for given timeslots
     *
     * @param method    method that will be used for the calculation
     * @param timeSlots timeslots that are the basis for the calculation
     * @param baselLine name of the baseline (the label of the values that should be used for baseline). If no baseline is defined, the
     * @return the same timeslots as used as input, but the {@link TimeSlotForMeasures#results} have the results of the calculation
     */
    private TimeSlotForMeasures calculateMeasure(UnivariateStatistic method, TimeSlotForMeasures timeSlots, String baselLine) {
        if (baselLine != null && !baselLine.equals("")) {
            List<Double> valuesForBaseLine = timeSlots.getTimeSlots().remove(baselLine);
            timeSlots.getResults().remove(baselLine);
            double[] primitiveValuesForBaseLine = valuesForBaseLine.stream().mapToDouble(Double::valueOf).toArray();
            double baseline = new Mean().evaluate(primitiveValuesForBaseLine);
            timeSlots.setBaseline(baseline);
            for (Map.Entry<String, List<Double>> ts : timeSlots.getTimeSlots().entrySet()) {
                List<Double> listBeforeBaselineSubtraction = ts.getValue();
                List<Double> listAfterBaselineSubtraction = listBeforeBaselineSubtraction.stream().map(d -> (d - baseline)).collect(Collectors.toList());
                ts.setValue(listAfterBaselineSubtraction);
            }
            baselineMessage = baselLine + " The baseline for " + timeSlots.getColumnName() + " is " + baselLine + ". The value is " + baseline + ".";
        }
        for (Map.Entry<String, List<Double>> e : timeSlots.getTimeSlots().entrySet()) {
            double[] arrayForCalculation = e.getValue().stream().mapToDouble(Double::valueOf).toArray();
            double evaluate = method.evaluate(arrayForCalculation);
            timeSlots.getResults().put(e.getKey(), evaluate);
        }
        return timeSlots;

    }
}
