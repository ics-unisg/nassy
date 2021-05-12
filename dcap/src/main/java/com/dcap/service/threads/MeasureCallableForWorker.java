package com.dcap.service.threads;

import com.dcap.helper.DoubleColumnException;
import com.dcap.service.serviceToolKit.ReportableResultWriter;
import com.dcap.analyzer.*;
import com.dcap.domain.User;
import com.dcap.fileReader.DataFile;
import com.dcap.fileReader.DataFileColumn;
import com.dcap.fileReader.DataFileLine;
import com.dcap.helper.FileException;
import org.apache.commons.math3.stat.descriptive.UnivariateStatistic;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.apache.commons.math3.stat.descriptive.rank.Max;
import org.apache.commons.math3.stat.descriptive.rank.Median;
import org.apache.commons.math3.stat.descriptive.rank.Min;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Class to create the measures
 *
 */
public class MeasureCallableForWorker implements CallableForWorkerInterface {

    private static String type = "Measure";
    //    private List<String> timeSlotList;
    private String timeStampColumnName;

    private String id;
    protected User user;
    private FileNameUser file;
    protected String message;
    //    private List<PhaseLabel> labelList;
    private String labelColumn;
    private String baseline;
    private BaselineValues baselineValues;
    private List<String> columnNames;
    private String path;
    private Boolean labelFlag;
    private List<TimeFrame> frames;
//    private String timeSlotSeperator =",";


    public MeasureCallableForWorker(String id, FileNameUser file, List<TimeFrame> timeFrames, String labelColumn, String baseline, User user, List<String> columnNames, String path, String timeStampColumnName, Boolean label) {
        this.id = id;
        this.file = file;

        this.user = user;

        this.labelColumn = labelColumn;
//        this.labelList = labelList;
        this.baseline = baseline;
        this.columnNames = columnNames;
        this.path = path;
        this.labelFlag = label;
        this.baselineValues = null;
        this.message = "Calculating Average Load for .tsv file";
        this.timeStampColumnName = timeStampColumnName;
        this.frames = timeFrames;
    }

//    public MeasureCallableForWorker(String id, List<Pair<String, File>> files, List<String> timeSlotList, String timeStampColumnName, String baseline, User user, List<String> columnNames, String path, String timeSlotSeperator) {
//        this.id = id;
//        this.files = files;
//
//        this.user = user;
//
//        this.timeStampColumnName = timeStampColumnName;
//        this.timeSlotList = timeSlotList;
//        this.baseline = baseline;
//        this.columnNames = columnNames;
//        this.path = path;
//        this.timeSlotSeperator = timeSlotSeperator;
//        this.baselineValues = null;
//        this.message="Calculating Average Load for .tsv file";
//    }

    private List<TimeFrame> baselineCaclulation(List<TimeFrame> frames) {
        TimeFrame tmp = null;
        UnivariateStatistic method = new Mean();
        for (int i = 0; i < frames.size(); i++) {
            if (frames.get(i).getLabel().equals(this.baseline)) {
                System.out.println("Gefunden " + frames.get(i).getLabel());
                tmp = frames.get(i);
                frames.remove(i);
                break;
            }
        }

        Map<String, Double[]> baselineMap = new HashMap<>();

        for (Map.Entry<String, LinkedList<Double>> entry : tmp.getColumns().entrySet()) {
            Double[] value = entry.getValue().toArray(new Double[entry.getValue().size()]);
            baselineMap.put(entry.getKey(), value);
        }
        ;
        Map<String, Double> mean = new HashMap<>();
        for (Map.Entry<String, Double[]> entry : baselineMap.entrySet()) {
            Double[] value = entry.getValue();
            double[] doubles = Stream.of(value).mapToDouble(Double::doubleValue).toArray();
            double evaluate = method.evaluate(doubles);
            mean.put(entry.getKey(), evaluate);

        }

        this.baselineValues = new BaselineValues(mean, method, this.baseline);
        return frames;
    }


    private ReportableResult calculate(ReportableResult result, List<TimeFrame> frames, String fileName) {
        String prefix = "";

        if (this.baseline != null && !this.baseline.trim().equals("")) {
            frames = baselineCaclulation(frames);
            if (frames == null) {
                return null;
            }
            prefix = "" + this.baseline + "_";
            System.out.println(this.baselineValues.getMean());
        }

        PhaseAverageLoadForTsvFileWorkItemCalculation calculateMeasure = new PhaseAverageLoadForTsvFileWorkItemCalculation(frames, result,
                this.baselineValues);
        calculateMeasure.adFileName(fileName, "FileName");
        calculateMeasure.calculate(new Mean(), prefix, "average");
        calculateMeasure.calculate(new Median(), prefix, "median");
        calculateMeasure.calculate(new Min(), prefix, "min");
        calculateMeasure.calculate(new Max(), prefix, "max");
        calculateMeasure.calculate(new StandardDeviation(), prefix, "standardDeviatiom");
        calculateMeasure.calculate(new StandardError(), prefix, "standardError");
        return calculateMeasure.getReportableResult();
    }

    @Override
    public Object call() {
       System.err.println("Start task");
        ArrayList<ThreadResponse> threadResponses = new ArrayList<>();
        Map<String, ReportableResult> collectedResults = new HashMap<>();


        ReportableResultWriter reportableResultWriter = new ReportableResultWriter();

        DataFileColumn label = null;
        DataFile dataFile = null;
        DataFileColumn timeStampColumn = null;
        try {
            dataFile = new DataFile(this.file.getFile(), this.file.getPath(), DataFile.SEPARATOR_TABULATOR, true, ".");
            if (this.labelFlag) {
                label = dataFile.getHeader().getColumn(this.labelColumn);
            } else {
                timeStampColumn = dataFile.getHeader().getColumn(timeStampColumnName);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            ThreadResponse response = new ThreadResponse(this.id, "ERROR","Problem with file " + this.file.getName()+"; "+e.getMessage() , null, this.file.getName(), this.user.getId(), null);
            threadResponses.add(response);
        } catch (FileException e) {
            //ThreadResponse response = reportableResultWriter.write(this.id, this.user, this.file.getUserData(), subject, reportableResult, "measures_" + this.file.getName(), "comment", this.path);
            ThreadResponse response = new ThreadResponse(this.id, "ERROR","Problem with file " + this.file.getName()+"; "+e.getMessage() , null, this.file.getName(), this.user.getId(), null);
            threadResponses.add(response);
            return threadResponses;
        } catch (DoubleColumnException e) {
            ThreadResponse response = new ThreadResponse(this.id, "ERROR","Problem with file " + this.file.getName()+"; "+e.getMessage() , null, this.file.getName(), this.user.getId(), null);
            threadResponses.add(response);
            return threadResponses;        }
        Map<String, DataFileColumn> columnsMap = new HashMap<>();
        for (String columName : this.columnNames) {
            DataFileColumn column = null;
            try {
                column = dataFile.getHeader().getColumn(columName);
            } catch (IOException e) {
                ThreadResponse response = new ThreadResponse(this.id, "ERROR","Problem with file " + this.file.getName()+"; "+e.getMessage() , null, this.file.getName(), this.user.getId(), null);
                threadResponses.add(response);
                return threadResponses;
            } catch (FileException e) {
                ThreadResponse response = new ThreadResponse(this.id, "ERROR","Problem with file " + this.file.getName()+"; "+e.getMessage() , null, this.file.getName(), this.user.getId(), null);
                threadResponses.add(response);
                return threadResponses;
            }
            columnsMap.put(columName, column);
        }

//            List<TimeFrame> frames = new ArrayList<>();
//            for (PhaseLabel pl : labelList) {
//                frames.add(new TimeFrame(pl.getLabel()));
//            }

        LinkedList<DataFileLine> content = null;
        try {
            content = dataFile.getContent();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<TimeFrame> timeFramesNew = new ArrayList<>();

        if (this.labelFlag && (this.frames == null || this.frames.isEmpty())) {
            this.frames = createTimeFrames(label, dataFile);
        }

        for (DataFileLine pupillometryFileLine : content) {
            if (this.labelFlag) {
                extractValuesViaLabel(label, columnsMap, this.frames, pupillometryFileLine);
            } else {
                extractValuesViaTimeSlots(timeStampColumn, columnsMap, this.frames, timeFramesNew, pupillometryFileLine);
            }
        }
        if (!timeFramesNew.isEmpty()) {
            this.frames = timeFramesNew;
        }


        ReportableResult reportableResult = new ReportableResult(this.file.getName());
        reportableResult = calculate(reportableResult, this.frames, this.file.getName());


        String subject = this.file.getSubject();
        // DecimalFormat format = new DecimalFormat("0.####", DecimalFormatSymbols.getInstance(
        // Locale.GERMAN));reportableResult.addResult("source_file_name",new ReportableResultEntry(fileName));reportableResult=
        ThreadResponse response = reportableResultWriter.write(this.id, this.user, this.file.getUserData(), subject, reportableResult, "measures_" + this.file.getName(), "comment", this.path);



        threadResponses.add(response);
        return threadResponses;

    }

    private List<TimeFrame>  createTimeFrames(DataFileColumn label, DataFile dataFile) {
        List<String> columnValues = null;
        List<TimeFrame> returnList= new LinkedList<>();
        try {
            columnValues = dataFile.getColumn(label.getColumnNumber());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Set<String> labelSet = columnValues.stream().filter(s -> (!s.trim().equals(""))).collect(Collectors.toSet());
        for (String s : labelSet) {
            returnList.add(new TimeFrame(s));
        }
        return returnList;


    }

    private void extractValuesViaTimeSlots(DataFileColumn timeStampColumn, Map<String, DataFileColumn> columnsMap, List<TimeFrame> frames, ArrayList<TimeFrame> timeFramesNew, DataFileLine pupillometryFileLine) {


        for (TimeFrame frame : frames) {
            if (frameNotInList(timeFramesNew, frame)) {
                timeFramesNew.add(new TimeFrame(frame.getLabel()));
            }
            TimeFrame tempTimeFrame = getTimeFrameByKey(timeFramesNew, frame.getLabel());
            Long timeStamp = pupillometryFileLine.getLong(timeStampColumn);
            for (Map.Entry<String, DataFileColumn> entry : columnsMap.entrySet()) {
                if (frame.matchesTimeFrame(timeStamp)) {
                    if (!pupillometryFileLine.isEmpty(entry.getValue())) {
                        tempTimeFrame.add(entry.getKey(), pupillometryFileLine.getDouble(entry.getValue()));
                    }
                }
            }
        }
    }

    private TimeFrame getTimeFrameByKey(ArrayList<TimeFrame> timeFramesNew, String label) {
        for (TimeFrame tf : timeFramesNew) {
            if (tf.getLabel().equals(label)) {
                return tf;
            }
        }
        return null;
    }

    private boolean frameNotInList(ArrayList<TimeFrame> timeFramesNew, TimeFrame frame) {
        for (TimeFrame tf : timeFramesNew) {
            String label = frame.getLabel();
            if (tf.getLabel().equals(label)) {
                return false;
            }
        }
        return true;
    }

    private void extractValuesViaLabel(DataFileColumn label, Map<String, DataFileColumn> columnsMap, List<TimeFrame> frames, DataFileLine pupillometryFileLine) {
        String lineLabel = pupillometryFileLine.get(label);
        for (TimeFrame frame : frames) {
            if (frame.getLabel().equals(lineLabel)) {
                for (Map.Entry<String, DataFileColumn> entry : columnsMap.entrySet()) {
                    if (!pupillometryFileLine.isEmpty(entry.getValue())) {
                        frame.add(entry.getKey(), pupillometryFileLine.getDouble(entry.getValue()));
                    }
                }
            }
        }
    }

    private void createTempTimeFrames() {
    }

    @Override
    public User getUser() {
        return null;
    }

    @Override
    public String getId() {
        return this.id;
    }
}
