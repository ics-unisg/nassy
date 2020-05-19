package com.dcap.analyzer;

import org.apache.commons.math3.stat.descriptive.UnivariateStatistic;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;


/**
 * Class to calculate the statistics
 *
 */
public class PhaseAverageLoadForTsvFileWorkItemCalculation {

    private List<TimeFrame> frames;
    private ReportableResult reportableResult;
    private BaselineValues baseline;

    public PhaseAverageLoadForTsvFileWorkItemCalculation(List<TimeFrame> frames, ReportableResult reportableResult,
                                                         BaselineValues baseline) {
        super();
        this.frames = frames;
        this.reportableResult = reportableResult;
        this.baseline = baseline;

    }

    public void adFileName(String fileName, String columnName) {
        reportableResult.addResult(columnName, new ReportableResultEntry(fileName));

    }

    public void calculate(UnivariateStatistic method, String prefix, String measureName) {
        DecimalFormat format = new DecimalFormat("0.######", DecimalFormatSymbols.getInstance(Locale.GERMAN));
        //For each timeframe calculate the measure
        for (TimeFrame tf : frames) {
//            ArrayList<Double> meanValuesOfTimeframe = new ArrayList<>();
//            ArrayList<Double> meanValuesOfTimeframeBaseLineCorrected = new ArrayList<>();
//            HashMap<String, LinkedList<Double>> columnsBaseLineCorrected = new HashMap<>();
//            Set<Map.Entry<String, LinkedList<Double>>> entriesList = tf.getColumns().entrySet();
//            int size = entriesList.iterator().next().getValue().size();
//            HashMap<Integer, List<Double>> allEntriesTogether0 = new HashMap<>();
//            for (int i = 0; i < size; i++) {
//                ArrayList<Double> doubles = new ArrayList<>();
//                allEntriesTogether0.put(i, doubles);
//            }
//
//            int index = 0;
//            for (Map.Entry<String, LinkedList<Double>> entry : entriesList) { //getting out every column
//                LinkedList<Double> column = entry.getValue(); //getting the values
//                if (baseline != null) {
//                    LinkedList<Double> baselineCorrectedColumn = new LinkedList<>();
//                    columnsBaseLineCorrected.put(entry.getKey(), baselineCorrectedColumn);
//                }
//                for (int i = 0; i < column.size(); i++) {
//                    allEntriesTogether0.get(i).add(column.get(i));
//                    if (baseline != null) {
//                        Double baseLineValue = baseline.getMean().get(entry.getKey());
//                        double newValue = column.get(i) - baseLineValue;
//                        columnsBaseLineCorrected.get(entry.getKey()).add(newValue);
//                    }
//                }
//            }
//            index++;
//            for(Map.Entry<Integer, List<Double>>  entry: allEntriesTogether0.entrySet()){
//                double evaluate = new Mean().evaluate(entry.getValue().stream().mapToDouble(x -> x).toArray());
//                meanValuesOfTimeframe.add(evaluate);
//                if (baseline != null) {
//                    evaluate = evaluate - baseline.getMeanOverAll();
//                    meanValuesOfTimeframeBaseLineCorrected.add(evaluate);
//                }
//            }
            ReportableResultEntry errorIfNoData = new ReportableResultEntry("No data for this timeslot available");

            writeResults(method, measureName, format, tf, errorIfNoData, tf.getColumns().entrySet(), "");
            if (baseline != null) {
                writeResults(method, measureName, format, tf, errorIfNoData, tf.getColumnsBaseLineCorrected(baseline), "baselineCorrected_" + prefix);
            }

            writeResultForOverall(method, measureName, format, tf, tf.getMeanValuesOfTimeframe(baseline), errorIfNoData, "");
            if (baseline != null) {
                writeResultForOverall(method, measureName, format, tf, tf.getMeanValuesOfTimeframeBaseLineCorrected(baseline), errorIfNoData, "baselineCorrected_" + prefix);
            }
        }
        //baseline = null;
    }

    private void writeResults(UnivariateStatistic method, String measureName, DecimalFormat format, TimeFrame tf, ReportableResultEntry errorIfNoData, Set<Map.Entry<String, LinkedList<Double>>> entries, String prefix) {
        for (Map.Entry<String, LinkedList<Double>> entry : entries) {
            LinkedList<Double> columEntry = entry.getValue();
            if (columEntry.size() > 0) {
                double[] doubles = columEntry.stream().mapToDouble(Double::doubleValue).toArray();
                double evaluatedValue = method.evaluate(doubles);
                reportableResult.addResult(prefix + measureName + "_" + entry.getKey() + "_" + tf.getLabel(),
                        new ReportableResultEntry(format.format(evaluatedValue)));
            } else {
                reportableResult.addResult(prefix + measureName + "_" + entry.getKey() + "_" + tf.getLabel(), errorIfNoData);
            }
        }
    }

    private void
    writeResultForOverall(UnivariateStatistic method, String measureName, DecimalFormat format, TimeFrame tf, ArrayList<Double> meanValuesOfTimeframe, ReportableResultEntry errorIfNoData, String prefix) {
        if (meanValuesOfTimeframe.size() > 0) {
            double[] meanValuesOfTimeframeArray = meanValuesOfTimeframe.stream().mapToDouble(Double::doubleValue).toArray();
            double meanValueOfTimeFrameResult = method.evaluate(meanValuesOfTimeframeArray);
            reportableResult.addResult(prefix + measureName + "_meanValueOfTimeSlot_" + tf.getLabel(),
                    new ReportableResultEntry(format.format(meanValueOfTimeFrameResult)));
        } else {
            reportableResult.addResult(prefix + measureName + "_meanValueOfTimeSlot_" + tf.getLabel(), errorIfNoData);
        }
    }

    public ReportableResult getReportableResult() {
        return reportableResult;
    }

}