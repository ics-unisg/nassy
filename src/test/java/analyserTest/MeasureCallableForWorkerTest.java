package analyserTest;

import com.dcap.analyzer.BaselineValues;
import com.dcap.analyzer.FileNameUser;
import com.dcap.analyzer.TimeFrame;
import com.dcap.domain.User;
import com.dcap.helper.FileException;
import com.dcap.service.threads.MeasureCallableForWorker;
import com.dcap.service.threads.ThreadResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import restTestSet.RestTestInterface;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MeasureCallableForWorkerTest extends RestTestInterface {

    private MeasureCallableForWorker measureCallableForWorker;
private ArrayList<TimeFrame> timeFrames;
    @Before
    public void setup(){
        FileNameUser file = new FileNameUser("1219783", "shortenedFileForStatistics@filteredDrei.tsv", new File("./data/shortenedFileForStatistics@filteredDrei.tsv"), "./data/shortenedFileForStatistics@filteredDrei.tsv", null);
        timeFrames = new ArrayList<>();
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
        User user = new User();
        user.setId(2l);
        ArrayList<String> columnNames = new ArrayList<>();
        columnNames.add("rechts");
        columnNames.add("links");
        measureCallableForWorker = new MeasureCallableForWorker("123", file, timeFrames, "label", "b", user, columnNames, "./data", "EyeTrackerTimestamp", true);

    }


    @Test
    public void baselineCaclulation() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        Method baselineCaclulation = measureCallableForWorker.getClass().getDeclaredMethod("baselineCaclulation", List.class);
        baselineCaclulation.setAccessible(true);
        Object invoke = baselineCaclulation.invoke(measureCallableForWorker, timeFrames);
        //TimeFrame timeFrame = ((List<TimeFrame>) invoke).get(0);
        Field baselineValuesField = measureCallableForWorker.getClass().getDeclaredField("baselineValues");
        baselineValuesField.setAccessible(true);
        BaselineValues baseLineValues = (BaselineValues) baselineValuesField.get(measureCallableForWorker);
        Assert.assertEquals(2.95, baseLineValues.getMeanOverAll(), 0);
    }

    @Test
    public void getId() {
    }
}