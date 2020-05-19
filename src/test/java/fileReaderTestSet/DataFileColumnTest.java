package fileReaderTestSet;

import com.dcap.fileReader.DataFile;
import com.dcap.fileReader.DataFileColumn;
import com.dcap.helper.FileException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import restTestSet.RestTestInterface;

import java.io.IOException;

import static testHelper.HelpingKit.createFile;


/**
 * Class to test the DataFileColumn.class. Checks all the functionalities
 */
public class DataFileColumnTest extends RestTestInterface {


    private DataFile dataFileGeneral;

    /**
     * Creates a datafile from the dataForTest-directory
     * @throws IOException
     */
    @Before
    public void setup() throws IOException {
        dataFileGeneral = createFile("./dataForTesting/test.tsv");
    }

    /**
     *  Tests the getting Columnummer method. All columns are represented by the name and the index (of their occurance)
     *  This tests tries to check if the number is correct.
     * @throws FileException
     */
    @Test
    public void getColumnNumber() throws FileException {
        DataFileColumn columnA = dataFileGeneral.getColumn("A");
        DataFileColumn columnD = dataFileGeneral.getColumn("D");
        int columnNumberA = columnA.getColumnNumber();
        int columnNumberD = columnD.getColumnNumber();
        Assert.assertEquals(0, columnNumberA);
        Assert.assertEquals(3, columnNumberD);
    }

    /**
     * Tests if getColumn takes the column with the correct name
     * @throws FileException
     */
    @Test
    public void getName() throws FileException {
        DataFileColumn column = dataFileGeneral.getColumn("A");
        String name = column.getName();
        Assert.assertEquals("A", name);
    }
}