package com.dcap.service.threads;

import com.dcap.domain.User;
import com.dcap.domain.UserData;
import com.dcap.fileReader.DataFile;
import com.dcap.fileReader.DataFileColumn;
import com.dcap.fileReader.DataFileHeader;
import com.dcap.filters.*;
import com.dcap.helper.FileException;
import com.dcap.helper.FilterException;
import com.dcap.helper.Pair;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Callable sent to the worker to do the filtering
 *
 * @author uli
 */
public class CallableForWorker implements CallableForWorkerInterface {
    private FilterRequest request;
    private List<Pair<UserData, File>> files;
    private User user;
    private String id;
    private Boolean preprocess;

    private static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("YYYY-MM-dd_HH-mm");


    public CallableForWorker(String id, FilterRequest request, List<Pair<UserData, File>> files, User user) throws FilterException {
        this.request = request;
        this.user = user;
        this.files = files;
        this.id = id;
       setPreprocess(request.getFilter());
    }



    public List<ThreadResponse> call() throws IOException, FileException {
        String type = null;
        ArrayList<ThreadResponse> threadResponseList = new ArrayList<>();
        try {
            Thread.sleep(000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String path = files.get(0).getValue().getParentFile().getPath();
        List<FilterData> filterDataList = new ArrayList<>();
        List<IDataFilter> filterList = request.getFilter();
        for (Pair<UserData, File> file : files) {

            DataFile dataFile = null;
            try {
                dataFile = new DataFile(file.getValue(), file.getKey().getPath(), DataFile.SEPARATOR_TABULATOR, true, request.getDecimalSeparator());
//                String timestampcolumn = filterList.get(0).getColumns().get("timestampcolumn");
                String timestampcolumn = filterList.get(0).getTimeStampColumn();
                if(preprocess) {
                    performPreProcessing(dataFile, timestampcolumn);
                }
            } catch (FileNotFoundException e) {
                //TODO bessere try-catch sachen
                e.printStackTrace();
            } catch (FileException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            FilterData dataToAdd = new FilterData(dataFile, file.getKey().getFilename(), file.getKey());
            filterDataList.add(dataToAdd);
        }

        boolean isTrimFilter = filterList.stream().anyMatch(a -> a instanceof AbstractTrimFilter);

        if(false){
            HashSet<String> strings = new HashSet<>();

            for(IDataFilter filter: filterList){

                Collection<String> values = filter.getColumns().values();
                for(String name:values){
                    strings.add(name);
                }
            }
            ArrayList arrayList = new ArrayList(strings);
            for(FilterData fd: filterDataList){
                fd.downSizeFile(arrayList);
            }
        }
        for (IDataFilter filter: filterList) {
            type=checkFilterType(filter); //TODO check, that filter always the same type!!!
            String feedback = null;
            try {
                filterDataList = filter.run(filterDataList);
/*                DataFileColumn pupilLeft = filterDataList.get(0).getDataFile().getColumn("PupilLeft");
                DataFileColumn pupilRight= filterDataList.get(0).getDataFile().getColumn("PupilRight");
                List<String> columnLeft = filterDataList.get(0).getDataFile().getColumn(pupilLeft.getColumnNumber());
                List<String> columnRight = filterDataList.get(0).getDataFile().getColumn(pupilRight.getColumnNumber());
                System.out.println(filter.getName());
                System.out.println(columnLeft.get(0) + "   " +columnRight.get(0));
                System.out.println(columnLeft.get(1) + "   " +columnRight.get(1));
                System.out.println(columnLeft.get(2) + "   " +columnRight.get(2));
                System.out.println(columnLeft.get(3) + "   " +columnRight.get(3));
                System.out.println(columnLeft.get(4) + "   " +columnRight.get(4));
                System.out.println("--------------------------------------");*/
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
                String name = files.stream().map(entry -> entry.getKey().getFilename().toString()).collect(Collectors.joining(","));
                threadResponseList.add(new ThreadResponse(id,"ERROR", "Error in filtering process for file(s) " +  name + ": " +e.getMessage(), null, name, user.getId(), null, e.getMessage() ));
                return threadResponseList;
            }
        }

        if(false){
            for(FilterData df:filterDataList){
                   df.merge();
            }
        }

        String specifier="@filtered";
        for (FilterData resultData : filterDataList) {
            String name = resultData.getName();
            name = name.indexOf(".") > 0 ? name.substring(0, name.lastIndexOf(".")) : name;
            String newName=name+ specifier+".tsv";
            String pathname = path + "/" + newName;
            File file = new File(pathname);
            int ind=1;
            while(file.exists()){
                String infix ="("+ind++ +")";
                newName=name+ specifier+infix+".tsv";
                pathname = path + "/" + newName;
                file = new File(pathname);
            }
            try {
                resultData.getDataFile().writeToFile(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String notification = "Finished task" + "" +"; Written file to " + path +" at " +  DATE_FORMAT.format(new java.util.Date());
            ThreadResponse threadResponse = new ThreadResponse(id,type, resultData.getMessage(), path, newName, user.getId(), resultData.getUserData(),notification );
            threadResponseList.add(threadResponse);
        }





        return threadResponseList;
    }

    private String checkFilterType(IDataFilter filter) {
        if(filter instanceof AbstractChangingFilter){
            return "Filter";
        }else if(filter instanceof TEPRFilter) {
            return "TEPR";
        }else{
            return "Trimming";
        }
    }

    @Override
    public User getUser(){
        return user;
    }




    public String getFileName(){
        return "";
    }

    @Override
    public String getId() {
        return id;
    }

    //Check, if this makes sense
    private void performPreProcessing(DataFile dataFile,String timestampcolumn) throws IOException, FileException {
        DataFileHeader header = dataFile.getHeader();
        DataFileColumn timestampColumn = header.getColumn(timestampcolumn);
        if (timestampColumn == null) {

        }



        // do some pre-processing
        dataFile.collapseEmptyLines(timestampColumn);
        dataFile.removeNullValues("-1");
        dataFile.adaptTimestamps(timestampColumn);


    }

    public Boolean getPreprocess() {
        return preprocess;
    }

    private void setPreprocess(List<IDataFilter> filterList) throws FilterException {
        for(IDataFilter idf: filterList){
            if(preprocess !=null && this.preprocess !=idf.isPreprocessing()){
                throw new FilterException("Not sure if preprocess or not");
            }else if(preprocess==null){
                this.preprocess=idf.isPreprocessing();
            }
        }
    }
}
