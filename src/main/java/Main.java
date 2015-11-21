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
        analizator.analize(testMusic, testText);
        Vector<Integer> endVector = analizator.getEndVector();
        int startSegment = analizator.getStartSegment();

        // Слушаешь музыку
        //test.testPlay(filename);


    }
}
