package com.dcap.service.threads;

import com.dcap.service.serviceInterfaces.UserServiceInterface;
import com.dcap.analyzer.FileNameUser;
import com.dcap.analyzer.TimeFrame;
import com.dcap.domain.*;
import com.dcap.filters.FilterRequest;
import com.dcap.helper.FilterException;
import com.dcap.service.Exceptions.PermissionException;
import com.dcap.service.Exceptions.RepoExeption;
import com.dcap.service.NotificationService;
import com.dcap.service.UserService;
import com.dcap.service.serviceInterfaces.UserDataServiceInterface;
import com.dcap.transferObjects.MeasureRequest;
import com.dcap.helper.Pair;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.File;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * This class controlls the worker queue and schedules the tasks
 * Based on a idea of Joseph Wang
 *
 * @author uli
 */

@Service
public class WorkerController {

    private final NotificationService notificationService;
    private final UserDataServiceInterface userDataServiceInterface;
    private final UserService userService;


//    private final int POOL_SIZE=Runtime.getRuntime().availableProcessors()-2;
    private final int POOL_SIZE=4;
    private final ExecutorService workerExecuter = Executors.newSingleThreadExecutor();
    private final ExecutorService service = Executors.newFixedThreadPool(POOL_SIZE);
    private final Queue<CallableForWorkerInterface> listStore = new ConcurrentLinkedQueue<>();
    private final Map<String, Future<List<ThreadResponse>>> futures = new ConcurrentHashMap<>();
    private volatile boolean isRunning=false;

    @Autowired
    public WorkerController(UserServiceInterface userServiceInterface, NotificationService notificationService, UserDataServiceInterface userDataServiceInterface, UserService userService1){
        this.notificationService = notificationService;
        this.userDataServiceInterface = userDataServiceInterface;
        this.userService = userService1;
        if(!checkOrUpdateRunning(-1)){
            workerExecuter.submit(new MyWorkerController());
        }
        System.out.println(POOL_SIZE);
    }

    public String addTask(MeasureRequest measureRequest, User user) throws RepoExeption {
        return addTask(measureRequest.getFileIds(), measureRequest.getLabels(), measureRequest.getLabelColumn(), measureRequest.getBaseline(), measureRequest.getColumnNames(), measureRequest.getTimeStampColumnName(), user);
    }

    public String addTask(List<Long> fileList, List<String> labels, String labelColumn, String baseline, List<String> columNames, String timeStampColumn, User user) throws RepoExeption {

        List<UserData> fileCollection = new LinkedList<>();
        for(Long id:fileList){
            fileCollection.add(getUserData(id));
        }
        List<FileNameUser> files = new ArrayList<>();
        for(UserData ud: fileCollection){
            String subjectId = ud.getSubject().getSubjectId();
            files.add(new FileNameUser(subjectId, ud.getFilename(), new File(ud.getPath()), ud.getPath(), ud));
        }
        List<TimeFrame> phaseLabels = labels.stream().map(
                label -> createTimePhase(label)
        ).collect(Collectors.toList());
        String path = files.get(0).getFile().getParentFile().getPath() ;

        String timeStampColumName = timeStampColumn;
        boolean labelFlag = false;
        if(labelColumn!=null && !labelColumn.trim().equals("")){
            labelFlag=true;
        }

        String id="";
        for(FileNameUser file:files) {
            if(!id.equals("")){
                id=id+", ";
            }
            String idAct = UUID.randomUUID().toString();
            id=id+ idAct;
            MeasureCallableForWorker callable = new MeasureCallableForWorker(idAct, file, phaseLabels, labelColumn, baseline, user, columNames, path, timeStampColumName, labelFlag);
            listStore.add(callable);
        }
        System.out.println(listStore.size());
        return id;
    }

    private TimeFrame createTimePhase(String phaseString) {
        if (!phaseString.contains(";")) {
            return new TimeFrame(phaseString);
        } else {
            String[] splittedFrame = phaseString.split(";");
            return new TimeFrame(Long.valueOf(splittedFrame[1]), Long.valueOf(splittedFrame[2]), splittedFrame[0]);
        }
    }

/*    public String  addTask(MeasureWriterRequest request, User user) throws RepoExeption {
        String id = UUID.randomUUID().toString();
        List<UserData> fileCollection = new LinkedList<>();
        for(Long fileId:request.getFiles()){
            fileCollection.add(getUserData(fileId));
        }        List<File> files = new ArrayList<>();
        for(UserData ud: fileCollection){
            files.add(new File(ud.getPath()));
        }
        String path = files.get(0).getParentFile().getPath() ;

        ExtractMeasureOfFileWorker extractMeasureOfFileWorker = new ExtractMeasureOfFileWorker(id, request, files, path, user);

        listStore.add(extractMeasureOfFileWorker);
        System.out.println(listStore.size());
        return id;
    }*/



    public String  addTask(FilterRequest request, User user) throws RepoExeption, PermissionException, FilterException {
        System.err.println("In der AddTask Funktion");

        String id="";
        List<UserData> fileCollection = new LinkedList<>();
        for(Long fileID:request.getFiles()){
            fileCollection.add(getUserData(fileID));
        }        if(fileCollection.contains(null)){
            throw  new RepoExeption("No file found");
        }
        if(!fileCollection.stream().anyMatch(userData -> userData.getUser().getId().equals(user.getId()))){
            throw new PermissionException("Userdata with id not all owned by user");
        }
        List<Pair<UserData, File>> files = new ArrayList<>();
        for(UserData ud: fileCollection){
            files.add(new Pair<>(ud, new File(ud.getPath())));
        }
        for(Pair<UserData, File> file:files){
            if(!id.equals("")){
                id=id+", ";
            }
            String idAct = UUID.randomUUID().toString();
            id=id+ idAct;
            List<Pair<UserData, File>> fileParameter = new ArrayList<>();
            fileParameter.add(file);
            CallableForWorker callable=new CallableForWorker(idAct, request, fileParameter, user);
            listStore.add(callable);
        }


        return id;
    }


    private class MyWorkerController implements Runnable {
        @Override
        public void run() {
            List<User> allUser = userService.getAllUser();
            boolean administrator = allUser.stream().anyMatch(u -> u.getRole().equals("administrator"));
            if(!administrator){

                boolean useLetters = true;
                boolean useNumbers = false;
                String pw = RandomStringUtils.random(7, useLetters, useNumbers);
                User user = new User("Administrator", "Administrator", "admin@administrator.dcap", pw, "administrator");int length = 10;
                try {
                    userService.save(user);
                } catch (RepoExeption repoExeption) {
                    repoExeption.printStackTrace();
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.err.println("New default administrator was created");
                System.err.println(user.toString());
                System.err.println("The password is: " + pw);
                System.err.println("Please change the password");

            }
            int c = 0;
            Runtime runtime = Runtime.getRuntime();


            checkOrUpdateRunning(1);
            while (!Thread.currentThread().isInterrupted()) {

                if(c %100 ==0) {
                    long allocatedMemory =runtime.totalMemory();
                    System.err.println("allocated memory is " + allocatedMemory / (1024 * 1024*1024));
                    c =1;
                }

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(listStore.size()>0 && (c%6 ==0) ){

                    System.err.println("ListSize " +listStore.size());
                }
                c++;
                for (Map.Entry<String, Future<List<ThreadResponse>>> future: futures.entrySet()) {
                    Future<List<ThreadResponse>> result = future.getValue();
                    if (result.isDone()) {
                        try {
                            treatResult(future.getKey(), result);

                        } catch (Exception e) {
                            System.err.println(e.getMessage());
                            e.printStackTrace();
                        }
                        futures.remove(future.getKey());
                    }
                }
                int possibleTaskCount = POOL_SIZE - futures.size();
                for (int i=0; i<possibleTaskCount; i++) {
                    CallableForWorkerInterface call = listStore.poll();
                    if (call!=null) {
                        Future<List<ThreadResponse>> submit = service.submit(call);
                        String uuid = UUID.randomUUID().toString();
                        futures.put(uuid, submit);
                    }
                }
            }
        }
    }

    private synchronized boolean checkOrUpdateRunning(int value) {
        if(value==1){
            isRunning=true;
            return true;
        }else if (value==0){
            isRunning=false;
            return false;
        }
        return isRunning;
    }


    public List<CallableForWorkerInterface> getQueue(User user) {
        Long id = user.getId();
        return getQueue(id);
    }

    public List<CallableForWorkerInterface> getQueue(Long id){
        List<CallableForWorkerInterface> queueForUser = listStore.stream().filter(element -> (element.getUser().equals(id))).collect(Collectors.toList());
        return queueForUser;
    }

    public synchronized boolean removeFromQueue(CallableForWorkerInterface callable){
        boolean remove = listStore.remove(callable);
        return remove;
    }

    private void treatResult(String key, Future<List<ThreadResponse>> value) throws ExecutionException, InterruptedException {
        List<ThreadResponse> threadResponses = value.get();
        for(ThreadResponse threadResponse:threadResponses) {
            String type = threadResponse.getType();
            User user = null;
            try {
                user = userService.getUserById(threadResponse.getUserId());
            } catch (RepoExeption repoExeption) {
                repoExeption.printStackTrace();
            }
            if (type.equals("Filter")) {
                String path = threadResponse.getPath()+"/"+threadResponse.getName();
                String notificationText = "Finished task filtering file " + threadResponse.getUserData().getFilename() + " as " + threadResponse.getName() + ".";
                String typeForUD = getString(path);
                Notifications success = new Notifications(user, notificationText, "success", path, false, new Date(), threadResponse.getId());
                notificationService.safeOrUpdate(success);
                UserData parentUserData = threadResponse.getUserData();
                UserData userData = new UserData(threadResponse.getUserData().getSubject(), parentUserData,  threadResponse.getName(), typeForUD, path, false, threadResponse.getMessage(), null,  ENUMERATED_CATEGORIES.DATA, user, threadResponse.getId());
                userDataServiceInterface.saveOrUpdate(userData);
            } else if (type.equals("Measure") || type.equals("Measure2")) {
                String path = threadResponse.getPath();
                Notifications success = new Notifications(user, threadResponse.getNotification(), "success", path, false, new Date(), threadResponse.getId());
                notificationService.safeOrUpdate(success);
                UserData userData = new UserData(null, threadResponse.getUserData().getDerived(), threadResponse.getName(), "text/tab-separated-values", path, false, threadResponse.getMessage(), null, ENUMERATED_CATEGORIES.DATA, user, threadResponse.getId());
                userDataServiceInterface.saveOrUpdate(userData);
            } else if (type.equals("Trimming")) {
                String path = threadResponse.getPath()+"/"+threadResponse.getName();
                Notifications success = new Notifications(user, threadResponse.getNotification(), "success", path, false, new Date(), threadResponse.getId());
                notificationService.safeOrUpdate(success);
                Subject subject = threadResponse.getUserData().getSubject();
                String typeForUD = getString(path);
                UserData parentUserData = threadResponse.getUserData();
                UserData userData       = new UserData(subject, parentUserData,  threadResponse.getName(), typeForUD, path, false, threadResponse.getMessage(), null, ENUMERATED_CATEGORIES.DATA, user, threadResponse.getId());
                userDataServiceInterface.saveOrUpdate(userData);
            } else if (type.equals("TEPR")) {
                String path = threadResponse.getPath()+"/"+threadResponse.getName();
                Notifications success = new Notifications(user, threadResponse.getMessage(), "success", path, false, new Date(), threadResponse.getId());
                notificationService.safeOrUpdate(success);
                UserData userData = new UserData(null, null,  threadResponse.getName(), "text/tab-separated-values", path, false, threadResponse.getMessage(), null, ENUMERATED_CATEGORIES.DATA, user, threadResponse.getId());
                System.err.println(threadResponse.getType());

                userDataServiceInterface.saveOrUpdate(userData);
            } else if (type.equals("ERROR")) {
                Notifications success = new Notifications(user, threadResponse.getMessage(), "error", null, false, new Date(), threadResponse.getId());
                notificationService.safeOrUpdate(success);

            }
            Thread.sleep(500);
        }
    }

    private String getString(String path) {
        String typeForUD="unknown";
        if(path.endsWith("csv")|| path.endsWith("tsv")){
            typeForUD="text/tab-separated-values";
        }else if(path.endsWith("webm")){
            typeForUD="video/webm";
        }
        return typeForUD;
    }


    private void checkThreads() {
        System.out.println("Checking");


    }
    private UserData getUserData(Long id) throws RepoExeption {
        UserData userDataById = userDataServiceInterface.getUserDataById(id);
        return userDataById;
    }

}
