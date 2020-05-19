package fileReaderTestSet;

import com.dcap.fileReader.*;
import com.dcap.helper.FileException;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import restTestSet.RestTestInterface;
import testHelper.HelpingKit;

import javax.validation.constraints.AssertTrue;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static testHelper.HelpingKit.createFile;

public class DataFileTest extends RestTestInterface {

    private DataFile dataFileGeneral;
    private DataFile dataFileForAdapting;
    private DataFile dataForCollapsing;
    DataFile dataForCollapsingCopy;

    /**
     * Setup for creating the file that are used  in the tests.
     * @throws IOException
     */
    @Before
    public void setup() throws IOException {
        dataFileGeneral = createFile("./dataForTesting/test.tsv");
        dataFileForAdapting = createFile("./dataForTesting/testWithMicroseconds.tsv");
        dataForCollapsing = createFile("./dataForTesting/shortenedFile.tsv");
        dataForCollapsingCopy = createFile("./dataForTesting/shortenedFile.tsv");
    }





    /**
     * Thests if the header is extracted in the right way
     * @throws FileException if getColumn(string) throws it
     * @throws IOException if getColumn(int) throws it
     */
    @Test
    public void adaptTimestamps() throws FileException, IOException {
        DataFileColumn c = dataFileGeneral.getColumn("C");
        List<String> timeStampColumn = dataFileGeneral.getColumn(c.getColumnNumber());
        dataFileGeneral.adaptTimestamps(c);
        List<String> timesTampColumnAfterAdapting = dataFileGeneral.getColumn(c.getColumnNumber());
        Assert.assertTrue(HelpingKit.compareLists(timeStampColumn, timesTampColumnAfterAdapting));

    }

    /**
     * Tests if the time is in Milliseconds and if yes, it adapts them to microseconds
     * @throws FileException if getColumn(string) throws it
     * @throws IOException if getColumn(int) throws it
     */
    @Test
    public void adaptTimestampsToMicro() throws FileException, IOException {
        DataFileColumn c = dataFileForAdapting.getColumn("C");
        List<String> timeStampColumn = dataFileForAdapting.getColumn(c.getColumnNumber());
        dataFileForAdapting.adaptTimestamps(c);
        List<String> timesTampColumnAfterAdapting = dataFileForAdapting.getColumn(c.getColumnNumber());
        Assert.assertFalse(HelpingKit.compareLists(timeStampColumn, timesTampColumnAfterAdapting));
        Assert.assertTrue(Long.valueOf(timesTampColumnAfterAdapting.get(0)) == Long.valueOf(timeStampColumn.get(0)) * 1000l);

    }

    /**
     * Method not used, so no test...
     */
    @Test
    public void addSceneColumn() {
    }


    /**
     * Appends a column and checks if the column in successfully added.
     * @throws IOException if getHeader throws an exception.
     */
    @Test
    public void appendColumn() throws IOException {
        DataFileHeader header = dataFileGeneral.getHeader();
        Assert.assertEquals(9, header.size());
        dataFileGeneral.appendColumn("NewColumn");
        DataFileHeader headerNew = dataFileGeneral.getHeader();
        Assert.assertEquals(10, headerNew.size());
        Assert.assertTrue(headerNew.hasColumn("NewColumn"));
    }

    /**
     * Adds a line to a file and checks if the new line is successfully added.
     */
    @Test
    public void appendLine() {
        List<IDataFileLine> lines = dataFileGeneral.getLines();
        int lineSize = lines.size();
        dataFileGeneral.appendLine();
        List<IDataFileLine> linesNew = dataFileGeneral.getLines();
        int lineSizeNew = linesNew.size();
        Assert.assertTrue(lineSizeNew - lineSize == 1);
    }


    /**
     * Method not used, so no test...
     */
    @Test
    public void appendLine1() {
    }


    /**
     * Method not used, so no test...
     */
    @Test
    public void clearColumn() {
    }

    /**
     * Checks if the empty lines in a datafile are removed. Iterates over the not collapsed file and compares if all lines are there.
     * If they is a missing value in the column, the line is skipped.
     *
     * @throws FileException
     * @throws IOException
     */
    @Test
    public void collapseEmptyLines() throws FileException, IOException {

        DataFileColumn eyeTrackerTimestampColumn = this.dataForCollapsing.getColumn("EyeTrackerTimestamp");
        this.dataForCollapsing.collapseEmptyLines(eyeTrackerTimestampColumn);
        //this.dataForCollapsing.writeToFile(new File("/home/uli/masterGit/postCheetah/data/onlyPrepared.tsv"));

        List<IDataFileLine> originalLines = dataForCollapsingCopy.getLines();
        List<IDataFileLine> newLines = this.dataForCollapsing.getLines();

        Assert.assertNotEquals(originalLines.size(),newLines.size());
        DataFileColumn eyeTrackerTimestamp = dataForCollapsingCopy.getColumn("EyeTrackerTimestamp");

        int shift = 0;

        boolean test=true;
        for(int i = 0; i<originalLines.size()-shift; i++){

            IDataFileLine iDataFileLine = originalLines.get(i + shift);
            if(iDataFileLine instanceof  DataFileHeader){
                continue;
            }
            DataFileLine origninalEntry = (DataFileLine) iDataFileLine;

            String stringToProve = origninalEntry.get(eyeTrackerTimestamp);
            if(stringToProve ==null||stringToProve.trim().equals("")){
                  shift++;
                origninalEntry = (DataFileLine) originalLines.get(i + shift);
                System.err.println("nullValue at "+i);
            }
            DataFileLine dataFileLine = (DataFileLine) newLines.get(i);
            if(!origninalEntry.equals(dataFileLine)){
                System.err.println("Hier ein Fehler" + i);
                test=false;

            }

        }
        Assert.assertTrue(test);


    }


    /**
     * Method not used, so no test...
     */
    @Test
    public void copyColumn() {
    }


    /**
     * Method not used, so no test...
     */
    @Test
    public void emptyIy() {
    }

    /**
     * tests if the getColumnMethod with integer returns the correct column
     * @throws IOException if the getColumn(int) throws it.
     */
    @Test
    public void getColumn() throws IOException {
        List<String> column = dataFileGeneral.getColumn(0);
        Assert.assertEquals("21-02-2018", column.get(0));
        Assert.assertEquals(5, column.size());

    }

    /**
     * tests if the getColumnMethod with name (string) returns the correct column
     * @throws IOException if the getColumn(int) throws it.
     */
    @Test
    public void getColumnAlternative() throws FileException {
        DataFileColumn a = dataFileGeneral.getColumn("A");
        Assert.assertEquals(0, a.getColumnNumber());
        Assert.assertEquals("A", a.getName());

    }

    /**
     * tests the method which returns the number of columns in the file.
     * @throws IOException if getColumnCount throws exception
     */
    @Test
    public void getColumnCount() throws IOException {
        int columnCount = dataFileGeneral.getColumnCount();
        Assert.assertEquals(9, columnCount);
    }


    /**
     * Tests the method that extracts the content of file: the content is a list of datafilelines.
     */
    @Test
    public void getContent() throws IOException {
        LinkedList<DataFileLine> content = dataFileGeneral.getContent();
        Assert.assertEquals(5, content.size());
        Assert.assertEquals("[21-02-2018, 10:38:48.825, 1511862040791901, 3,57, 3,50, nothing, nothing, 1, A]", content.get(0).toString());

    }

    /**
     * Tests, if the getDecimalSeperator returns the correct String.
     */
    @Test
    public void getDecimalSeparator() {
        String decimalSeparator = dataFileGeneral.getDecimalSeparator();
        Assert.assertEquals(".", decimalSeparator);
    }

    /**
     * checks if the header is extracted of a datafile.
     * @throws IOException
     */
    @Test
    public void checkHeader() throws IOException {
        DataFileHeader header = dataFileGeneral.getHeader();
        int size = header.size();
        Assert.assertEquals(9, size);
        Assert.assertEquals("A,B,C,D,E,F,G,H,I", header.getString(","));
    }

    /**
     * checks if the header is extracted of a datafile.
     * @throws IOException
     */
    @Test
    public void checkHeaderAlternative() throws IOException {
        DataFileHeader header = dataFileGeneral.getHeader();
        DataFileHeader dataFileHeader = new DataFileHeader();
        dataFileHeader.appendColumn("A");
        dataFileHeader.appendColumn("B");
        dataFileHeader.appendColumn("C");
        dataFileHeader.appendColumn("D");
        dataFileHeader.appendColumn("E");
        dataFileHeader.appendColumn("F");
        dataFileHeader.appendColumn("G");
        dataFileHeader.appendColumn("H");
        dataFileHeader.appendColumn("I");
        Assert.assertEquals(dataFileHeader.getString(","), header.getString(","));
    }

    /**
     * Not used, so no test
     */
    @Test
    public void getIteratorStartingAt() {
    }

    @Test
    public void getLines() {
        List<IDataFileLine> lines = dataFileGeneral.getLines();
        Assert.assertEquals(6, lines.size());
        Assert.assertTrue(lines.get(0) instanceof DataFileHeader);
        IDataFileLine lineOne = lines.get(1);
        Assert.assertTrue(lineOne instanceof DataFileLine);
        Assert.assertEquals("[21-02-2018, 10:38:48.825, 1511862040791901, 3,57, 3,50, nothing, nothing, 1, A]", lineOne.toString());
    }

    @Test
    public void getSeparator() {
        String separator = dataFileGeneral.getSeparator();
        Assert.assertEquals("\t", separator);
    }

    @Test
    public void hasColumn() {
        Assert.assertTrue(dataFileForAdapting.hasColumn("A"));
    }


    /**
     * Method not used, so no test...
     */
    @Test
    public void processScene() {
    }

    /**
     * Method not used, so no test...
     */
    @Test
    public void removeLine() {
    }

    /**
     * Method not used, so no test...
     */
    @Test
    public void removeNullValues() {
    }

    /**
     * Tests the correctness of a written file.
     * @throws Exception
     */
    @Test
    public void writeToFile() throws Exception {

        File file = new File("./dataForTesting/dummy.tsv");
        dataFileGeneral.writeToFile(file);
        boolean b = FileUtils.contentEquals(file, new File("./dataForTesting/test.tsv"));
        Assert.assertTrue(b);
        file.delete();


    }


    /**
     * Tests, if the copyFile method gives the wright result
     * @throws IOException
     */
    @Test
    public void copyFile() throws IOException {
        DataFile dataFile = dataFileGeneral.copyFile();
        DataFile dataFileCopied = dataFile.copyFile();
        boolean equal = DataFileUtils.dataFileEquality(dataFile, dataFileCopied);
        Assert.assertTrue(equal);
    }


    /**
     * NOT USED
     */
    @Test
    public void getDownsizedDataFileAndWriteBack() throws IOException, FileException {
//        ArrayList<String> columns = new ArrayList<>();
//        columns.add("B");
//        columns.add("D");
//        columns.add("E");
//        LinkedList<DataFileLine> content = dataFileGeneral.getContent();
//        for(int i=0; i<content.size(); i++){
//            if(i==2||i==3){
//                content.get(i).mark("Marc Chagall");
//            }
//        }
//        DataFile downsizedDataFile = dataFileGeneral.getDownsizedDataFile(columns);
//        DataFileColumn datFileCol = downsizedDataFile.getHeader().getColumn("B");
//        List<String> b = downsizedDataFile.getColumn(datFileCol.getColumnNumber());
//        ArrayList<String> listToAdd = new ArrayList<>();
//        for(String s:b){
//            listToAdd.add(s.concat("test"));
//        }
//        Iterator<DataFileLine> iterator = downsizedDataFile.getContent().iterator();
//        int index = 0;
//        while (iterator.hasNext()) {
//            DataFileLine line = iterator.next();
//            line.setValue(datFileCol, listToAdd.get(index));
//            index++;
//        }
//        downsizedDataFile.writeToFile(new File("./dataForTesting/downsized.tsv"));
//        dataFileGeneral.mergeIn();
//        dataFileGeneral.writeToFile(new File("./dataForTesting/merged.tsv"));
    }



}
