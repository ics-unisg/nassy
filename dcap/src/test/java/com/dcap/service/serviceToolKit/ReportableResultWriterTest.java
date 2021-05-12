package com.dcap.service.serviceToolKit;

import com.dcap.analyzer.ReportableResult;
import com.dcap.service.threads.ThreadResponse;
import com.dcap.analyzer.ReportableResult;
import com.dcap.analyzer.ReportableResultEntry;
import com.dcap.domain.ENUMERATED_CATEGORIES;
import com.dcap.domain.User;
import com.dcap.domain.UserData;
import com.dcap.service.serviceToolKit.ReportableResultWriter;
import com.dcap.service.threads.ThreadResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import restTestSet.RestTestInterface;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ReportableResultWriterTest extends RestTestInterface {

    private String path="./dataForTesting/";
    private String prefix="prefix";
    private String id= "id";
    private User user;
    private ReportableResult result;
    private String comment ="comment";







    private static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("YYYY-MM-dd_HH-mm");
    private String fileName;

    /**
     * Creates akk data needed for the testing, especially the reportable results
     */
    @Before
    public void setUp() {
        result = new ReportableResult("123");
        result.addResult("subject", new ReportableResultEntry("12"));
        result.addResult("eins", new ReportableResultEntry("1"));
        result.addResult("zwei", new ReportableResultEntry("2"));

        user= new User("Donald", "Duck", "donald@duck.com", "123", "administrator");
        fileName = prefix + "_" + DATE_FORMAT.format(new java.util.Date()) + ".csv";
    }

    /**
     * Deletes the file written during the test
     */
    @After
    public void tearDown() throws Exception {
        new File(path+"/"+fileName).delete();
    }

    /**
     * tests the overall result of the writer. Gets as input the results from the statistics and creates the ThreadResponse
     */
    @Test
    public void write() {

        String fileName = prefix + "_" + DATE_FORMAT.format(new java.util.Date()) + ".csv";
        String message = "Analysis complete. Find the file '" + fileName + "' in your data section!";
        UserData userData = new UserData(null, null, fileName, "application/octet-stream", path, false, message, null, ENUMERATED_CATEGORIES.DATA, user, id);
        ThreadResponse write = new ReportableResultWriter().write(id, user, userData, null, result, prefix, comment, path);
        ThreadResponse controllValue = new ThreadResponse(id, "Measure", message, path+"/"+fileName, fileName, user.getId(), userData, message);

        Assert.assertEquals(controllValue, write);

    }

    /**
     * Covered with test above
     */
    @Test
    public void writeResultFile() {
    }

    /**
     * Tests the method that extracts the headers out of the results
     */
//    @Test
//    public void extractHeaders() {
//        List<String> strings = new ReportableResultWriter().extractHeaders(results);
//        ArrayList<String> controllString = new ArrayList<>();
//        controllString.add("subject");
//        controllString.add("eins");
//        controllString.add("zwei");
//        Assert.assertTrue(controllString.containsAll(strings) && strings.containsAll(controllString));
//        List<String> strings2 = new ReportableResultWriter().extractHeaders(results, "drei", "vier");
//        controllString.add("drei");
//        controllString.add("vier");
//        Assert.assertTrue(controllString.containsAll(strings2) && strings2.containsAll(controllString));
//    }

    /**
     * Tests the method that writes the header for the csv
     */
    @Test
    public void writeHeaders() {
        ArrayList<String> strings = new ArrayList<>();
        strings.add("eins");
        strings.add("zwei");
        strings.add("drei");
        String string = new ReportableResultWriter().writeHeaders(strings).toString();
        Assert.assertEquals("eins;zwei;drei;\n", string);

    }
}
