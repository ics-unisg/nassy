package staticTests;

import com.dcap.fileReader.DataFile;
import com.dcap.helper.DataFileTools;

import java.io.IOException;
import java.util.ArrayList;

import static testHelper.HelpingKit.createFile;

public class checkEquality {

    public static void main2(String[] args) throws IOException {
        DataFile originalFile = createFile("/home/uli/masterGit/postCheetah/data/shortenedFile.tsv");
//        DataFile filteredNew = createFile("/home/uli/masterGit/postCheetah/data/shortenedFile@filtered(9).tsv");
        DataFile filteredNew = createFile("/home/uli/masterGit/postCheetah/data/shortenedFile@filtered(11).tsv");
//        DataFile filteredServer = createFile("/home/uli/masterGit/postCheetah/dataForTesting/Test1@shortenedFile@filtered.tsv");
//        DataFile filteredServer = createFile("/home/uli/masterGit/postCheetah/dataForTesting/Test1@shortenedFile@filtered.tsv");
        DataFile filteredServer = createFile("/home/uli/masterGit/postCheetah/dataForTesting/Test1@shortenedFile@filteredNeu.tsv");

        ArrayList<String> skipList = new ArrayList<>();
        skipList.add("Scene");
        skipList.add("MissingValue");

        boolean b = DataFileTools.checkSimilarityOfDataFiles(originalFile, filteredServer, skipList);
        System.out.println("Shoul be false: " + b);


        boolean b1 = DataFileTools.checkSimilarityOfDataFiles(filteredNew, filteredServer, skipList);
        System.out.println("Should be true: " + b1);



    }

    public static void main(String[] args) throws IOException {
        DataFile fileLocal = createFile("/home/uli/masterGit/postCheetah/data/1219183@Study Manuel@sowiPM2@filtered(8).tsv");
        DataFile fileServer = createFile("/home/uli/Downloads/Test1@1219183@Study Manuel@sowiPM2@filtered(4).tsv");
        ArrayList<String> skipList = new ArrayList<>();
        skipList.add("Scene");


        boolean b1 = DataFileTools.checkSimilarityOfDataFiles(fileLocal, fileServer, skipList);
        System.out.println("Should be true: " + b1);
    }

}
