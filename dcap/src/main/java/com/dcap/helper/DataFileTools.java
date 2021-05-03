package com.dcap.helper;

import com.dcap.fileReader.*;
import com.dcap.fileReader.*;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;


/**
 * Class to check properties of files
 *
 * @author uli
 */
public class DataFileTools {

    /**
     *
     * Method to test if two {@link DataFile} files are equal
     *
     * @param dataFileOne firt file
     * @param dataFileTwo second fiel
     * @param neglect name of columns that should not be tested
     * @return returns true if they are similar
     */
    public static boolean checkSimilarityOfDataFiles(DataFile dataFileOne, DataFile dataFileTwo, List<String> neglect){

        boolean answer = true;

        List<IDataFileLine> fileOneLines = dataFileOne.getLines();
        List<IDataFileLine> fileTwoLines= dataFileTwo.getLines();

        List<DataFileColumn> columnsOne = null;
        List<DataFileColumn> columnsTwo = null;
        System.err.println(fileOneLines.size() + " " + fileTwoLines.size());
        for(int i = 0; i<fileOneLines.size()-2; i++){
            IDataFileLine iDataFileLineTwo = fileTwoLines.get(i);
            IDataFileLine iDataFileLineOne = fileOneLines.get(i);


            if(iDataFileLineOne instanceof DataFileHeader){
                DataFileHeader dataHeaderOne = (DataFileHeader) iDataFileLineOne;
                DataFileHeader dataHeaderTwo = (DataFileHeader) iDataFileLineTwo;
//                if(dataHeaderOne.size()!=dataHeaderTwo.size()){
//                    answer = false;
//                    System.err.println("Header is different size");
//                }


                columnsOne = dataHeaderOne.getColumns();
                columnsTwo = dataHeaderTwo.getColumns();

                Set<String> collectOne = columnsOne.stream().map(e -> e.getName()).collect(Collectors.toSet());
                Set<String> collectTwo = columnsTwo.stream().map(e -> e.getName()).collect(Collectors.toSet());

                Collection<String> disjunction = org.apache.commons.collections4.CollectionUtils.disjunction(collectOne, collectTwo);
                if(neglect!=null){
                    disjunction.removeAll(neglect);
                }
                if(!disjunction.isEmpty()){
                    for(String s: disjunction){
                        System.err.println(s);
                    }
                    answer=false;
                    return answer;
                }
                for(DataFileColumn column:columnsOne){
                    if(neglect != null  && neglect.contains(column.getName())){
                        continue;
                    }
                    if(!dataHeaderOne.get(column).equals(dataHeaderTwo.get(column))){
                        answer =  false;
                        System.err.println("Header is different");
                    }
                }
                continue;
            }

            DataFileLine dataFileLineTwo = (DataFileLine) iDataFileLineTwo;
            DataFileLine dataFileLineOne = (DataFileLine) iDataFileLineOne;

            if(!dataFileLineOne.isEqualTo(dataFileLineTwo, columnsTwo, columnsOne, neglect)){
                System.err.println("Hier ein Fehler" + i);
                answer = false;
//                return answer;
            }
        }

        return answer;
    }

    public static boolean checkSimilarityOfDataFiles(DataFile filteredNew, DataFile filteredServer) {
        boolean b = checkSimilarityOfDataFiles(filteredNew, filteredServer, null);
        return b;
    }
}
