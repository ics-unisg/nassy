package testHelper;

import com.dcap.domain.User;
import com.dcap.fileReader.DataFile;
import com.dcap.helper.DoubleColumnException;
import com.dcap.rest.DataMsg;
import com.dcap.security.MyUserPrincipal;
import com.dcap.helper.Pair;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class HelpingKit {


    public static void setPrincipal(Long id, String first, String last, String email, String password, String roles) {
        User user = new User(first, last, email, password, roles);
        user.setId(id);
        MyUserPrincipal myUserPrincipal = new MyUserPrincipal(user);
        Authentication authMock = Mockito.mock(Authentication.class);
        Mockito.when(authMock.getPrincipal()).thenReturn(myUserPrincipal);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authMock);
        SecurityContextHolder.setContext(securityContext);
    }

    public static void setPrincipal(){
        setPrincipal(2l, "Dagobert", "Duck", "dagobert@duck.org", "123", "administrator");
    }

    public static <T> Pair<T, Integer> analyseResponseEntitiy(ResponseEntity<DataMsg<T>> responseEntity) {
        HttpStatus statusCode = responseEntity.getStatusCode();
        System.out.println("Statuscode: " + statusCode);
        DataMsg<T> body = responseEntity.getBody();
        String messageHumanReadable = body.getMessageHumanReadable();
        System.out.println("Messagse human readable: " + messageHumanReadable);
        Integer statusCodeInternal = body.getStatusCode();
        System.out.println("internal Status code: " + statusCodeInternal);
        T resBody = body.getResBody();
        if(resBody!=null){
            System.out.println("Message Content: " + resBody.toString());
        }
        return new Pair(resBody,statusCodeInternal) ;
    }


    public static boolean compareLists(List list1, List list2){

        if(list1.size()!=list2.size()){
            return false;
        }
        for(int i=0; i<list1.size(); i++){
            if(!list1.get(i).equals(list2.get(i))){
                return false;
            }
        }
        return  true;
    }

    public static DataFile createFile(String pathname) throws IOException {
        File file = new File(pathname);
        DataFile dataFile = null;
        try {
            dataFile = new DataFile(file, pathname,"\t", true, ".");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DoubleColumnException e) {
            e.printStackTrace();
        }
        dataFile.getContent();

        return dataFile;
    }

    public static <T> boolean testEqualityOfLists(List<T> columnsReference, List<T> columnsTarget) {
        if(columnsReference.size()!=columnsTarget.size()){
            return false;
        }
        for(int i =0; i<columnsReference.size(); i++){
            if(!columnsReference.get(i).equals(columnsTarget.get(i))){
                return false;
            }
        }
        return true;
    }

}

