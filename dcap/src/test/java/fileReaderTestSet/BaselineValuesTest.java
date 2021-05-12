package fileReaderTestSet;

import com.dcap.analyzer.BaselineValues;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import restTestSet.RestTestInterface;

import java.util.HashMap;

import static org.junit.Assert.*;

/**
 * Tests the functionality of the BaseLineValues.class
 */
public class BaselineValuesTest extends RestTestInterface {

    private BaselineValues baselineValues;
    private HashMap<String, Double> values;

    /**
     * Creates a BaseLineValue vor test
     */
    @Before
    public void setUp(){
        values = new HashMap<>();
        values.put("Eins", 2.5);
        values.put("Zwei", 3.0);
        values.put("Drei", 3.5);
        baselineValues = new BaselineValues(values, new Mean(), "mean");

    }

    /**
     * Tests the Getter function for the hashmap
     */
    @Test
    public void getMean() {
        Assert.assertEquals(values, baselineValues.getMean());

    }

    /**
     * Tests, if the mean over all baselinevalues is correct.
     */
    @Test
    public void getMeanOverAll() {
        Assert.assertEquals(3.0, baselineValues.getMeanOverAll(), 0);
    }
}