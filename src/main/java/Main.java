import com.mpatric.mp3agic.*;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Vector;

/**
 * Created by Neikila on 07.11.2015.
 */
public class Main {
    public static void main(String[] args) throws Exception{
        Music test = new Music();

//        testAnalyzator();
        Vector<Integer> testText;
        String textName = "testkurs.txt";
        TextReader textreader = new TextReader();
        testText = textreader.readFile(textName);
        String filename = "vals.mp3";

        Vector<Integer> testMusic;
        testMusic = test.getData(filename);

        Analizator analizator = new Analizator();
        Vector<Integer> endVector = analizator.analize(testMusic, testText);

        // Слушаешь музыку
        //test.testPlay(filename);


    }
}
