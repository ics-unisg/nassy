package com.dcap.fileReader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

import com.dcap.helper.FileException;
import com.dcap.helper.FilterException;
import org.apache.commons.io.input.BOMInputStream;

/**
 * Class from Cheetah_Web1.0
 * some modification by uli
 *
 */

public class DataFile {
    public static final String SEPARATOR_SEMICOLON = ";";
    public static final String SEPARATOR_TABULATOR = "\t";
    //public static final String SEPARATOR_COMMA = ",";
    public static final String SEPARATOR_COMMA = ".";
    public static final String COLLAPSED_COLUMNS = "cheetah_internal_collapsed_columns";
    private String path;

    private List<DataFileLine> content;
    private DataFileHeader header;
    private String separator;

    private boolean hasHeader;
    private String decimalSeparator;

    public static List<DataFileLine> extractContent(BufferedReader reader, String decimalSeparator, String separator)
            throws IOException {
        return extractContent(reader, decimalSeparator, separator, Integer.MAX_VALUE);
    }

    public static List<DataFileLine> extractContent(BufferedReader reader, String decimalSeparator, String separator, int limit)
            throws IOException {
        List<DataFileLine> content = new ArrayList<DataFileLine>();
        String line = reader.readLine();
        while (line != null) {
            DataFileLine currentLine = new DataFileLine(decimalSeparator);
            String[] token = line.split(separator, -1);
            for (String string : token) {
                currentLine.add(string.intern());
            }
            content.add(currentLine);
            if (content.size() >= limit) {
                break;
            }

            line = reader.readLine();
        }

        return content;
    }

    public static DataFileHeader extractHeader(BufferedReader reader, String separator) throws IOException {
        String line = reader.readLine();
        DataFileHeader header = new DataFileHeader();
        String[] token=null;
        if(line!=null) {
            token = line.split(separator, -1);
            for (String string : token) {
                header.appendColumn(string.intern());
            }
        }

        return header;
    }

    public DataFile(File file, String path, String decimalSeparator) throws FileNotFoundException {
        this(new BOMInputStream(new FileInputStream(file)), path, decimalSeparator);
    }

    public DataFile(File file, String path, String separator, boolean hasHeader, String decimalSeparator) throws FileNotFoundException {
        this.separator = separator;
        this.hasHeader = hasHeader;
        this.decimalSeparator = decimalSeparator;
        this.path=path;
        try {
            read(new BOMInputStream(new FileInputStream(file)));
        } catch (IOException e) {
            //TODO do something senseful
        }
    }

    public DataFile(InputStream input, String path, String separator, boolean hasHeader, String decimalSeparator) throws FileNotFoundException {
        this(input, path, decimalSeparator);
        this.separator = separator;
        this.hasHeader = hasHeader;
        this.decimalSeparator = decimalSeparator;
        this.path=path;
    }

    public DataFile(InputStream input, String path, String separator) {
        this(input, path, SEPARATOR_TABULATOR, false);
    }

    public DataFile(InputStream input, String path, String separatorToken, boolean hasHeader) {
        this.separator = separatorToken;
        this.hasHeader = hasHeader;
        this.decimalSeparator = SEPARATOR_COMMA;
        this.path=path;
        try {
            read(input);
        } catch (IOException e) {
            //TODO do something senseful
        }
    }

    public DataFile(String separator) throws IOException {
        this(new ByteArrayInputStream("".getBytes()), null, separator);

        header = new DataFileHeader();
        content = new LinkedList<>();
    }

    public DataFile(String separator, boolean hasHeader) throws IOException {
        this(new ByteArrayInputStream("".getBytes()), null, separator, hasHeader);

        header = new DataFileHeader();
        content = new LinkedList<>();
    }

    /**
     * Some data sources provide the time stamps as ms, some as microseconds - adapt all values to microseconds.
     *
     * @param column
     * @throws IOException
     */
    public void adaptTimestamps(DataFileColumn column) throws IOException {
        if (content.size() < 2) {
            return;
        }

        LinkedList<DataFileLine> content = getContent();
        double firstTimestamp = content.get(0).getDouble(column);
        double secondTimestamp = content.get(1).getDouble(column);
        if (secondTimestamp - firstTimestamp > 100) {
            return; // already -microseconds
        }

        for (DataFileLine line : content) {
            if (line.isEmpty(column)) {
                continue;
            }

            long timestamp = (long) (line.getDouble(column) * 1000);
            line.setValue(column, timestamp);
        }
    }

    @SuppressWarnings("unchecked")
    public void addSceneColumn(DataFileColumn sceneDataColumn) throws IOException {
        if (content == null) {
            throw(new IOException("No Content"));
        }

        DataFileColumn column = null;
        DataFileHeader header = getHeader();
        if (!header.hasColumn(WebConstants.PUPILLOMETRY_FILE_COLUMN_SCENE)) {
            column = appendColumn(WebConstants.PUPILLOMETRY_FILE_COLUMN_SCENE);
        } else {
            try {
                column = header.getColumn(WebConstants.PUPILLOMETRY_FILE_COLUMN_SCENE);
            } catch (FileException e) {
                e.printStackTrace();
            }
        }

        String scene = null;
        for (DataFileLine line : content) {
            scene = processScene(sceneDataColumn, scene, line);
            if (scene != null) {
                line.setValue(column, scene);
            } else {
                line.setValue(column, "");
            }

            List<DataFileLine> collapsed = (List<DataFileLine>) line.getMarking(COLLAPSED_COLUMNS);
            if (collapsed == null) {
                continue;
            }

            for (DataFileLine pupillometryFileLine : collapsed) {
                scene = processScene(sceneDataColumn, scene, pupillometryFileLine);
            }
        }
    }

    /**
     * Appends a new column and ensures that data is available for this column.
     *
     * @param columnName
     * @return
     * @throws IOException
     */
    public DataFileColumn appendColumn(String columnName) throws IOException {
        DataFileColumn column = header.appendColumn(columnName);

        // fill the new column with empty values
        for (DataFileLine line : getContent()) {
            line.add("");
        }

        return column;
    }

    public DataFileLine appendLine() {
        DataFileLine newLine = new DataFileLine(decimalSeparator);
        // fill the new column with empty values
        for (int i = 0; i < header.size(); i++) {
            newLine.add("");
        }

        content.add(newLine);
        return newLine;
    }

    public void appendLine(DataFileLine toAppend) {
        if (toAppend.size() != header.getColumns().size()) {
            throw new RuntimeException("Cannot append the given column, as it contains a different numbers of columns.");
        }

        content.add(toAppend.copy());
    }

    /**
     * This method removes all content from the provided column.
     *
     * @param column the column to remove all content from.
     * @throws IOException if the pupillometry file could not be read.
     */
    public void clearColumn(DataFileColumn column) throws IOException {
        for (DataFileLine pupillometryFileLine : getContent()) {
            pupillometryFileLine.setValue(column, "");
        }
    }

    /**
     * Tobii export creates empty lines when there is data for "StudioEvent" and "StudioEventData". This method removes these rows and adds
     * the removed rows to the previous row.
     *
     * @param timestampColumn the column that identifies timestamps
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public void collapseEmptyLines(DataFileColumn timestampColumn) throws IOException {
        if (content == null) {
            throw(new IOException("No Content"));
        }

        Iterator<DataFileLine> iterator = content.iterator();
        DataFileLine lastRowWithTimestamp = null; // add the marking to the previous column (assumption: there will never be an
        // empty row at the beginning of the file)
        while (iterator.hasNext()) {
            DataFileLine current = iterator.next();
            String timestampValue = current.get(timestampColumn);

            if (timestampValue.trim().isEmpty()) {
                List<DataFileLine> collapsed = (List<DataFileLine>) lastRowWithTimestamp.getMarking(COLLAPSED_COLUMNS);
                if (collapsed == null) {
                    collapsed = new LinkedList<>();
                    lastRowWithTimestamp.mark(COLLAPSED_COLUMNS, collapsed);
                }

                collapsed.add(current);
                iterator.remove();
            } else {
                lastRowWithTimestamp = current;
            }
        }
    }

    /**
     * Copies a column.
     *
     * @param toCopy the column to be copied
     * @param target the target column (will be created)
     * @throws IOException
     */
    public void copyColumn(DataFileColumn toCopy, String target) throws IOException {
        if (content == null) {
            throw(new IOException("No Content"));
        }

        DataFileColumn targetColumn = appendColumn(target);
        for (DataFileLine line : getContent()) {
            String valueToCopy = line.get(toCopy);
            line.setValue(targetColumn, valueToCopy);
        }
    }

    /**
     * Returns a copy of this file with headers only; omits the content.
     *
     * @return
     * @throws IOException
     */
    public DataFile emptyCopy() throws IOException {
        DataFile copy = new DataFile(decimalSeparator);
        copy.separator = separator;
        for (DataFileColumn column : getHeader().getColumns()) {
            copy.appendColumn(column.getName());
        }

        return copy;
    }

    public List<String> getColumn(int columnNumber) throws IOException {
        if (content == null) {
            throw(new IOException("No Content"));
        }

        List<String> column = new ArrayList<String>();
        for (IDataFileLine line : content) {
            column.add(line.get(columnNumber));
        }

        return column;
    }

    public DataFileColumn getColumn(String columnName) throws FileException {
        return header.getColumn(columnName);
    }

    public int getColumnCount() throws IOException {
        if (content == null) {
            throw(new IOException("No Content"));
        }

        if (content.isEmpty()) {
            return 0;
        }

        return content.get(0).size();
    }

    public LinkedList<DataFileLine> getContent() throws IOException {
        if (content == null) {
            throw(new IOException("No Content"));
        }

        return new LinkedList<>(content);
    }

    public String getDecimalSeparator() {
        return decimalSeparator;
    }

    public DataFileHeader getHeader() throws IOException {
        if (hasHeader && header == null) {
            throw(new IOException("No Content"));
        }

        return header;
    }

    public ListIterator<DataFileLine> getIteratorStartingAt(long timestamp, DataFileColumn timestampColumn) {
        int index = 0;
        for (DataFileLine line : content) {
            long currentTimestamp = line.getLong(timestampColumn);
            if (currentTimestamp > timestamp) {
                break;
            }
            index++;
        }

        return content.listIterator(index);
    }

    public List<IDataFileLine> getLines() {
        List<IDataFileLine> lines = new LinkedList<IDataFileLine>();
        lines.add(header);
        lines.addAll(content);
        return lines;
    }

    public String getSeparator() {
        return separator;
    }

    public boolean hasColumn(String column) {
        return header.hasColumn(column);
    }

    public String processScene(DataFileColumn sceneDataColumn, String scene, DataFileLine pupillometryFileLine) {
        String sceneData = pupillometryFileLine.get(sceneDataColumn);
        // nothing has changed - life is good
        if (sceneData == null || sceneData.trim().isEmpty()) {
            return scene;
        }

        if (scene == null) {
            scene = sceneData;
        } else {
            if (scene.equals(sceneData)) {
                scene = null;
            } else {
                throw new IllegalStateException("Did not find the closing scene data for scene: " + scene);
            }
        }
        return scene;
    }


    private List<IDataFileLine> read(InputStream input) throws IOException {
        if (content != null) {
            return getLines();
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        if (hasHeader) {
            header = extractHeader(reader, separator);
        }
        content = extractContent(reader, decimalSeparator, separator);

        reader.close();
        input = null;
        return getLines();
    }



    public boolean removeLine(IDataFileLine pupillometryFileLine) {
        return content.remove(pupillometryFileLine);
    }

    /**
     * Remove all null values, indicated by nullValueMask.
     *
     * @param nullValueMask
     * @throws IOException
     */
    public void removeNullValues(String nullValueMask) throws IOException {
        for (DataFileLine line : getContent()) {
            for (DataFileColumn column : header.getColumns()) {
                String value = line.get(column);

                if (value.equals(nullValueMask)) {
                    line.setValue(column, "");
                }
            }
        }
    }

    public void writeToFile(File file) throws IOException {
        BufferedWriter fileWriter = new BufferedWriter(new FileWriter(file));
        List<IDataFileLine> lines = getLines();
        for (IDataFileLine line : lines) {
            String string = line.getString(separator);
            fileWriter.write(string + "\n");
        }

        fileWriter.close();
    }

    /**
     * method to intiate File with a header out of a list of headers
     *
     * @param listForHeader
     * @throws IOException
     */
    public void createHeader(List<String> listForHeader) throws IOException {
        for (String headerElement : listForHeader) {
            this.appendColumn(headerElement);
        }
    }

    public void setContent(List<DataFileLine> content) {
        this.content = content;
    }

    public void setHeader(DataFileHeader header) {
        this.header = header;
    }

    public DataFile copyFile() throws IOException {
        if(content==null) {
            throw (new IOException("No Content"));
        }
        String separator = this.getSeparator();
        String decimalSeparator = this.getDecimalSeparator();
        DataFile newDataFile = new DataFile(new ByteArrayInputStream("".getBytes()), this.path, separator, hasHeader, decimalSeparator);
        newDataFile.content = new LinkedList<>();
        if (hasHeader) {
            newDataFile.setHeader(this.header.copyHeader());
        }
        LinkedList<DataFileLine> content = this.getContent();
        for (DataFileLine line : content) {
            DataFileLine copy = line.copy();
            newDataFile.appendLineToContent(copy);
        }
        return newDataFile;
    }

    private void appendLineToContent(DataFileLine lineToAppend) {
        content.add(lineToAppend);
    }

//    public DataFile getDownsizedDataFile(List<String> columns) throws IOException, FileException {

//        String separator = this.getSeparator();
//        String decimalSeparator = this.getDecimalSeparator();
//        DataFile newDataFile = new DataFile(new ByteArrayInputStream("".getBytes()), this.path,separator, hasHeader, decimalSeparator);
//
//        newDataFile.content = new LinkedList<>();
//
//        DataFileHeader newDataFileHeader = new DataFileHeader();
//        for (String columnName : columns) {
//            newDataFileHeader.appendColumn(columnName);
//        }
//        newDataFile.setHeader(newDataFileHeader);
//
//
//        for (IDataFileLine line : this.content) {
//            DataFileLine dataFileLineToAppend = newDataFile.appendLine();
//            for (String names : columns) {
//                String valueOfColumn = line.get(getColumn(names).getColumnNumber());
//                dataFileLineToAppend.setValue(newDataFileHeader.getColumn(names), valueOfColumn);
//            }
//            try {
//                Map<String, Object> markings = ((DataFileLine) line).getMarkings();
//                if (markings != null) {
//                    dataFileLineToAppend.setMarkings(markings);
//
//                }
//            } catch (ClassCastException e) {
//                //No further handling needed;
//            }
//
//
//        }
//        return newDataFile;
//    }

    /**
     * This method makes the assumption that no filter except the trimming filter will delete a row.
     *
     * @throws IOException
     */
//    public void mergeIn() throws IOException, FileException {
//        DataFileHeader headerToBeMergedIn = this.getHeader();
//        this.input = new BOMInputStream(new FileInputStream(new File(path)));
//        LinkedList<DataFileLine> contentToBeMergedIn = getContent();
//        List<DataFileColumn> columns = headerToBeMergedIn.getColumns();
//        List<String> listOfColums = columns.stream().map(a -> a.getName()).collect(Collectors.toList());
//        content=null;
//        read();
//        for (String name : listOfColums) {
//            if (!hasColumn(name)) {
//                appendColumn(name);
//            }
//        }
//        for (int i = 0; i < this.getContent().size(); i++) {
//            DataFileLine dataFileLine = this.content.get(i);
//            try {
//                DataFileLine dataFileLineToBeMergedIn = contentToBeMergedIn.get(i);
//                for (String name : listOfColums) {
//                    String dataToBeMergedIn = dataFileLineToBeMergedIn.get(headerToBeMergedIn.getColumn(name));
//                    dataFileLine.setValue(this.header.getColumn(name), dataToBeMergedIn);
//                }
//
//
//                Map<String, Object> markings = dataFileLineToBeMergedIn.getMarkings();
//                if (markings != null) {
//                    for (Map.Entry<String, Object> m : markings.entrySet()) {
//                        dataFileLine.mark(m.getKey(), m.getValue());
//                    }
//                }
//            }catch (IndexOutOfBoundsException e){
//                for (String name : listOfColums) {
//                    dataFileLine.deleteValue(this.header.getColumn(name));
//                }
//            }
//        }
//
//    }
}
