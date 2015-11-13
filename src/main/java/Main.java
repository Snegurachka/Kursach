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

        Analizator analizator = new Analizator();
        analizator.analize(testText);

        // Слушаешь музыку
       String filename = "test.mp3";
        //test.testPlay(filename);


        Vector<Integer> testMusic;
        testMusic = test.getData(filename);

        Vector<Integer> endVector = analizator.analize(testMusic, testText);



    }



    public static void testAnalyzator() {
        Vector<Integer> temp = new Vector<Integer>();
        temp.add(10);
        temp.add(7);
        temp.add(4);
        temp.add(19);
        temp.add((int)'a');
        temp.add((int)'#');
        temp.add((int)' ');


        Analizator analizator = new Analizator();
        analizator.analize(temp);
    }
}
