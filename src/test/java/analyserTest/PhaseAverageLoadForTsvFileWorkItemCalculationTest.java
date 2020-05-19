package analyserTest;

import com.dcap.analyzer.*;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import restTestSet.RestTestInterface;

import java.io.File;
import java.util.*;


/**
 * Tests the class that calculates the class that calculates the measures for the given timeslots
 */
public class PhaseAverageLoadForTsvFileWorkItemCalculationTest extends RestTestInterface {

    private PhaseAverageLoadForTsvFileWorkItemCalculation phaseAverageLoadForTsvFileWorkItemCalculation;

    @Before
    public void setUp() throws Exception {
        ReportableResult reportableResult = new ReportableResult("reportableResult");
        ArrayList<TimeFrame> timeFrames = new ArrayList<>();
        TimeFrame timeFrameA = new TimeFrame("a");
        timeFrameA.add("links", 3.0);
        timeFrameA.add("links", 3.1);
        timeFrameA.add("links", 3.2);
        timeFrameA.add("links", 3.3);
        timeFrameA.add("links", 3.4);
        timeFrameA.add("links", 3.5);
        timeFrameA.add("links", 3.6);
        timeFrameA.add("links", 3.7);
        timeFrameA.add("links", 3.8);
        timeFrameA.add("links", 3.9);
        timeFrameA.add("rechts", 2.0);
        timeFrameA.add("rechts", 2.1);
        timeFrameA.add("rechts", 2.2);
        timeFrameA.add("rechts", 2.3);
        timeFrameA.add("rechts", 2.4);
        timeFrameA.add("rechts", 2.5);
        timeFrameA.add("rechts", 2.6);
        timeFrameA.add("rechts", 2.7);
        timeFrameA.add("rechts", 2.8);
        timeFrameA.add("rechts", 2.9);
        TimeFrame timeFrameB = new TimeFrame("b");
        timeFrameB.add("links", 3.0);
        timeFrameB.add("links", 3.1);
        timeFrameB.add("links", 3.2);
        timeFrameB.add("links", 3.3);
        timeFrameB.add("links", 3.4);
        timeFrameB.add("links", 3.5);
        timeFrameB.add("links", 3.6);
        timeFrameB.add("links", 3.7);
        timeFrameB.add("links", 3.8);
        timeFrameB.add("links", 3.9);
        timeFrameB.add("rechts", 2.0);
        timeFrameB.add("rechts", 2.1);
        timeFrameB.add("rechts", 2.2);
        timeFrameB.add("rechts", 2.3);
        timeFrameB.add("rechts", 2.4);
        timeFrameB.add("rechts", 2.5);
        timeFrameB.add("rechts", 2.6);
        timeFrameB.add("rechts", 2.7);
        timeFrameB.add("rechts", 2.8);
        timeFrameB.add("rechts", 2.9);
        timeFrames.add(timeFrameA);
        timeFrames.add(timeFrameB);
        HashMap<String, Double> values= new HashMap<>();
        values.put("rechts", 1.5);
        values.put("links", 1.0);
        BaselineValues baselineValues = new BaselineValues(values, new Mean(), "mean");
        phaseAverageLoadForTsvFileWorkItemCalculation = new PhaseAverageLoadForTsvFileWorkItemCalculation(timeFrames, reportableResult, baselineValues);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void adFileName() {
        phaseAverageLoadForTsvFileWorkItemCalculation.adFileName("A", "B");
        List<ReportableResultEntry> listOfEntries = phaseAverageLoadForTsvFileWorkItemCalculation.getReportableResult().getResults().get("B");
        Assert.assertEquals("A",listOfEntries.get(0).getResult());

    }

    @Test
    public void calculate() {
        phaseAverageLoadForTsvFileWorkItemCalculation.calculate(new Mean(), "prefix", "mean");
        ReportableResult reportableResult = phaseAverageLoadForTsvFileWorkItemCalculation.getReportableResult();
        Map<String, List<ReportableResultEntry>> results = reportableResult.getResults();
        results.get(0);
        String rb = "rechts_b";
        String ra = "rechts_a";
        String la = "links_a";
        String lb = "links_b";
        String bl = "baselineCorrected_";
        String pf = "prefix";
        String m = "mean_";
        String mvotsa="meanValueOfTimeSlot_a";
        String mvotsb="meanValueOfTimeSlot_b";

        Assert.assertEquals("2,45",results.get(m+ra).get(0).getResult());
        Assert.assertEquals("2,45",results.get(m+rb).get(0).getResult());
        Assert.assertEquals("3,45",results.get(m+la).get(0).getResult());
        Assert.assertEquals("3,45",results.get(m+lb).get(0).getResult());
        Assert.assertEquals("2,95",results.get(m+mvotsa).get(0).getResult());
        Assert.assertEquals("2,95",results.get(m+mvotsb).get(0).getResult());

        Assert.assertEquals("0,95",results.get(bl+pf+m+ra).get(0).getResult());
        Assert.assertEquals("0,95",results.get(bl+pf+m+rb).get(0).getResult());
        Assert.assertEquals("2,45",results.get(bl+pf+m+la).get(0).getResult());
        Assert.assertEquals("2,45",results.get(bl+pf+m+lb).get(0).getResult());
        Assert.assertEquals("1,7",results.get(bl+pf+m+mvotsa).get(0).getResult());
        Assert.assertEquals("1,7",results.get(bl+pf+m+mvotsb).get(0).getResult());


    }

    @Test
    public void getReportableResult() {
        ReportableResult reportableResult = phaseAverageLoadForTsvFileWorkItemCalculation.getReportableResult();
        Assert.assertEquals("reportableResult", reportableResult.getIdentifier());
    }
}