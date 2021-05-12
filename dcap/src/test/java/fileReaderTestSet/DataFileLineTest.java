package fileReaderTestSet;

import com.dcap.fileReader.DataFile;
import com.dcap.fileReader.DataFileColumn;
import com.dcap.fileReader.DataFileLine;
import com.dcap.helper.FileException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import restTestSet.RestTestInterface;

import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.*;
import static testHelper.HelpingKit.createFile;

public class DataFileLineTest extends RestTestInterface {

    private DataFile dataFileGeneral;
    private DataFileLine dataFileLine;
    private String dataFileLineString ="[21-02-2018, 10:38:48.825, 1511862040791901, 3,57, 3,50, nothing, nothing, 1, A]";
    private DataFileColumn columnI;
    private DataFileColumn columnC;
    private DataFileColumn columnH;
    private DataFileLine dataFileLineMark;

    /**
     * Creates a datafile from the dataForTest-directory
     * @throws IOException
     */
    @Before
    public void setup() throws IOException, FileException {
        dataFileGeneral = createFile("./dataForTesting/test.tsv");
        columnI = dataFileGeneral.getHeader().getColumn("I");
        columnC = dataFileGeneral.getHeader().getColumn("C");
        columnH = dataFileGeneral.getHeader().getColumn("H");
        dataFileLine = dataFileGeneral.getContent().get(0);
        dataFileLineMark = dataFileLine.copy();
        dataFileLineMark.mark("marking");

    }

    /**
     * Checks if the add method puts the string in the end of the datafileLine.
     */
    @Test
    public void add() {
        dataFileLine.add("testString");
        Assert.assertEquals("[21-02-2018, 10:38:48.825, 1511862040791901, 3,57, 3,50, nothing, nothing, 1, A, testString]", dataFileLine.toString());
    }

    /**
     * Checks, if the copy function returns the same list.
     */
    @Test
    public void copy() {
        DataFileLine dataFileLineCopied = this.dataFileLine.copy();
        Assert.assertEquals(dataFileLine, dataFileLineCopied);

    }


    /**
     * Checks, if the deleteValue removes the value of the column.
     */
    @Test
    public void deleteValue() {
        dataFileLine.deleteValue(columnI);
        String newDataFileLine = dataFileLine.get(columnI);
        Assert.assertEquals("", newDataFileLine);

    }

    /**
     * Checks the get method with a number.
     */
    @Test
    public void get() {
        String value = dataFileLine.get(2);
        Assert.assertEquals("1511862040791901", value);
    }

    /**
     * Checks the get method with a column
     */
    @Test
    public void get1() {
        String value = dataFileLine.get(columnC);
        Assert.assertEquals("1511862040791901", value);
    }

    /**
     * Checks the functionality of the getDoubelFunction
     */
    @Test
    public void getDouble() {
        Double value = dataFileLine.getDouble(columnC);
        Assert.assertEquals(1511862040791901d,value,1 );
    }

    /**
     * Checks the functionality of the getInteger
     */
    @Test
    public void getInteger() {
        int value = dataFileLine.getInteger(columnH);
        Assert.assertEquals(1, value);
    }

    /**
     * Checks the functionality of the getInteger
     */
    @Test(expected = NumberFormatException.class)
    public void getIntegerFailed() {
        int value = dataFileLine.getInteger(columnC);
    }

    /**
     * Checks the functionality of the getLong
     */
    @Test
    public void getLong() {
        Long value = dataFileLine.getLong(columnC);
        Assert.assertEquals(1511862040791901l, value,0);
    }

    /**
     * Checks the functionality of the getString
     */
    @Test
    public void getStringOfColumn() {
        String value = dataFileLine.getStringOfColumn(columnC);
        Assert.assertEquals("1511862040791901", value);
    }

    /**
     * Tests, if the marking is successful
     */
    @Test
    public void getMarking() {
        Object marking = dataFileLineMark.getMarking("marking");
        Assert.assertNotNull(marking);
    }

    /**
     * Check, if the getMarkings gives back the right  markings
     */
    @Test
    public void getMarkings() {
        Map<String, Object> markings = dataFileLineMark.getMarkings();
        Assert.assertTrue(markings.keySet().contains("marking"));
        Assert.assertTrue(markings.size()==1);
    }

    /**
     * Tests, if the getString method works properly
     */
    @Test
    public void getString() {
        String string = dataFileLine.getString(",");
        System.err.println(string);
        Assert.assertEquals("21-02-2018,10:38:48.825,1511862040791901,3,57,3,50,nothing,nothing,1,A", string);
    }

    /**
     * tests, if the isEmpty function returns the correct boolean
     */
    @Test
    public void isEmpty() {
        boolean empty = dataFileLine.isEmpty(columnC);
        Assert.assertFalse(empty);
        dataFileLine.deleteValue(columnC);
                empty = dataFileLine.isEmpty(columnC);
        Assert.assertTrue(empty);
    }

    /**
     * tests, if the isMarked function returns the correct boolean
     */
    @Test
    public void isMarked() {
        Assert.assertFalse(dataFileLine.isMarked("marking"));
        Assert.assertFalse(dataFileLineMark.isMarked("marcing"));
        Assert.assertTrue(dataFileLineMark.isMarked("marking"));
    }

    /**
     * tests, if the markFunction adds a correct marking
     */
    @Test
    public void mark() {
        Assert.assertNull(dataFileLine.getMarkings());
        dataFileLine.mark("marking1");
        Assert.assertEquals(1,dataFileLine.getMarkings().size());
        Assert.assertTrue(dataFileLine.isMarked("marking1"));
    }

    /**
     * tests, if the markFunction with string and objects adds a correct marking
     */

    @Test
    public void mark1() {
        Assert.assertNull(dataFileLine.getMarkings());
        dataFileLine.mark("marking", "nix");
        Assert.assertEquals(1,dataFileLine.getMarkings().size());
        Assert.assertTrue(dataFileLine.isMarked("marking"));
        Assert.assertEquals("nix",dataFileLine.getMarking("marking"));




    }


    /**
     * Tests the setValue for doubles
     */
    @Test
    public void setValue() {
        String stringOfColumn = dataFileLine.getStringOfColumn(columnH);
        Assert.assertEquals("1", stringOfColumn);
        dataFileLine.setValue(columnH, 12.5);
        Assert.assertEquals(12.5,dataFileLine.getDouble(columnH),0);
        Assert.assertEquals("12.5",dataFileLine.getStringOfColumn(columnH));

    }

    /**
     * Tests the setValue for strings
     */
    @Test
    public void setValue1() {
        String stringOfColumn = dataFileLine.getStringOfColumn(columnH);
        Assert.assertEquals("1", stringOfColumn);
        dataFileLine.setValue(columnH, "meep");
        Assert.assertEquals("meep" ,dataFileLine.getStringOfColumn(columnH));
    }

    /**
     * Tests the setValue for Longs
     */
    @Test
    public void setValue2() {
        String stringOfColumn = dataFileLine.getStringOfColumn(columnH);
        Assert.assertEquals("1", stringOfColumn);
        dataFileLine.setValue(columnH, 12l);
        Assert.assertEquals(new Long(12), dataFileLine.getLong(columnH));
        Assert.assertEquals("12",dataFileLine.getStringOfColumn(columnH));
    }


    /**
     * checks, if it returns the size of the line
     */
    @Test
    public void size() {

        Assert.assertEquals(9, dataFileLine.size());

    }


}