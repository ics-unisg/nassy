package testHelper;

import com.dcap.domain.*;
import com.dcap.transferObjects.GraphData;

import java.util.ArrayList;
import java.util.List;

public class TestObjects {

    public static List<GraphData> graphData;

    public static User user;
    public static UserData userData;
    public static Study study;
    public static Subject subject;


    static{
        graphData= new ArrayList<>();
        graphData.add(new GraphData(1l,"1"));
        graphData.add(new GraphData(2l,"2"));
        graphData.add(new GraphData(3l,"3"));
        graphData.add(new GraphData(4l,"4"));
        user= new User("Donald", "Duck", "donald@duck.com", "123", "administrator");
        user.setId(22l);
        study = new Study("study", "comment", null, user);
        study.setId(2l);
        subject = new Subject(study, "1123@456.78", "subId", "comemnt", null);
        userData = new UserData(subject, null, "fileName", "type", "./dataForTesting/nuiKurzLabel@filtered.tsv",
                true, "Comment", 123l, ENUMERATED_CATEGORIES.DATA, user, "123" );
    }
}
