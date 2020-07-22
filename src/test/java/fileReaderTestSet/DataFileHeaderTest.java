package fileReaderTestSet;

import com.dcap.fileReader.DataFile;
import com.dcap.fileReader.DataFileColumn;
import com.dcap.fileReader.DataFileHeader;
import com.dcap.helper.DoubleColumnException;
import com.dcap.helper.FileException;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import restTestSet.RestTestInterface;

import java.io.IOException;
import java.util.List;

import static testHelper.HelpingKit.createFile;
import static testHelper.HelpingKit.testEqualityOfLists;

public class DataFileHeaderTest extends RestTestInterface {


    private DataFile dataFileGeneral;
    private DataFileHeader dataFileHeaderFromFile;
    private DataFileHeader dataFileTestHeader;


    /**
     * Sets the test up. Loads test.tsv as file and creates similare header to the header of "dataFileGeneral".
     * @throws IOException
     */
    @Before
    public void setup() throws IOException, DoubleColumnException {

        dataFileGeneral = createFile("./dataForTesting/test.tsv");

        dataFileHeaderFromFile = dataFileGeneral.getHeader();

        dataFileTestHeader = new DataFileHeader();
        dataFileTestHeader.appendColumn("A");
        dataFileTestHeader.appendColumn("B");
        dataFileTestHeader.appendColumn("C");
        dataFileTestHeader.appendColumn("D");
        dataFileTestHeader.appendColumn("E");
        dataFileTestHeader.appendColumn("F");
        dataFileTestHeader.appendColumn("G");
        dataFileTestHeader.appendColumn("H");
        dataFileTestHeader.appendColumn("I");
    }


    /**
     * Tests the functionality of the append column method.
     * It is successful if there is the column added after this operation.
     */
    @Test
    public void appendColumn() throws DoubleColumnException {
        int sizeBefore = dataFileHeaderFromFile.size();
        dataFileHeaderFromFile.appendColumn("Neu");
        int sizeAfter = dataFileHeaderFromFile.size();
        Assert.assertEquals(sizeBefore+1, sizeBefore+1);
        Assert.assertTrue(dataFileHeaderFromFile.hasColumn("Neu"));
    }

    /**
     *This method checks if the get method gives back the same name for two header-elements
     */
    @Test
    public void compareHeaderElements() {
        String ref = dataFileHeaderFromFile.get(1);
        String tar = dataFileTestHeader.get(1);
        Assert.assertEquals(ref,tar);

    }

    /**
     * Tests the functionality of the getName(column) method, which returns the name of the parameterElement.
     */
    @Test
    public void getNameOfELement() {
        String testName = "Ich komme nicht vor";
        DataFileColumn a = new DataFileColumn(testName, 0);
        String ref = dataFileHeaderFromFile.get(a);
        String tar = dataFileTestHeader.get(a);

        Assert.assertEquals(ref,tar);
        Assert.assertEquals(testName,tar);

    }

    @Test
    public void getColumn() throws FileException {
        DataFileColumn a = dataFileHeaderFromFile.getColumn("A");
        Assert.assertEquals("A", a.getName());
    }

    /**
     * Tests, if there is an exception if the columname does not exist in the datafile
     * @throws FileException if the columnName does not exist.
     */
    @Test(expected = FileException.class)
    public void getColumnWithWrongElement() throws FileException {
        DataFileColumn a = dataFileHeaderFromFile.getColumn("Nicht da");

    }

    /**
     * Tests if the getColumns method returns the correct header (and compares it with the constructed header)
     */
    @Test
    public void getColumns() {
        List<DataFileColumn> columnsReference = dataFileHeaderFromFile.getColumns();
        List<DataFileColumn> columnsTarget = dataFileTestHeader.getColumns();
        boolean equal = testEqualityOfLists(columnsReference, columnsTarget);
        Assert.assertTrue(equal);
    }


    /**
     * Checks the getString function. Gets header as string, seperated by the string given as argument.
      */
    @Test
    public void getString() {
        String string = dataFileHeaderFromFile.getString(",");
        Assert.assertEquals("A,B,C,D,E,F,G,H,I", string);
    }

    /**
     * Checks if the header of the file has a column with the name given in the argument.
     */
    @Test
    public void hasColumn() {
        Assert.assertTrue(dataFileHeaderFromFile.hasColumn("A"));
    }

    /**
     * Tests is the size function returns the correct length of the header.
     */
    @Test
    public void size() {
        int size = dataFileHeaderFromFile.size();
        Assert.assertEquals(9,  size);
    }


    /**
     * Checks functionality of copy method;
     */
    @Test
    public void copyHeader() {
        String stringOfHeader = dataFileHeaderFromFile.getString(",");
        DataFileHeader dataFileHeaderCopied = dataFileHeaderFromFile.copyHeader();
        String stringOfCopiedHeader = dataFileHeaderCopied.getString(",");
        Assert.assertEquals(stringOfHeader, stringOfCopiedHeader);
    }
}
