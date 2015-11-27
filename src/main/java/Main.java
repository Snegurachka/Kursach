import java.util.Vector;
import  java.io.PrintWriter;

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
        int N = 25;



        analizator.backAnalize(endVector, N);
        Vector <Integer> text = analizator.getText();

        PrintWriter out = new PrintWriter("myflle.txt");

        for (int i = 0; i < text.size(); ++i){
            int a = text.get(i);
            char b = (char) a;
            out.println(b);
        }

        out.close();

        // Слушаешь музыку
        //test.testPlay(filename);


    }
}
