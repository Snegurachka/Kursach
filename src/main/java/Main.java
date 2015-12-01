import wav.Test;

/**
 * Created by Neikila on 07.11.2015.
 */
public class Main {
    public static void main(String[] args) throws Exception{
        String filename = "antonio_vivaldi_-_the_four_seasons_-_winter_zaycev.wav";
        String filename_v1 = "antonio_vivaldi_-_the_four_seasons_-_winter_zaycev1.wav";
//        Test1.readFile(filename);
        Test test = new Test();
        test.readWav(filename);
//        Test test1 = new Test();
//        test1.readWav(filename);
//
//        List<List<Long>> list = test.getBytes();
//        List<List<Long>> list1 = test1.getBytes();
//
//        int size = list.get(0).size();
//        if (size != list1.get(0).size()) {
//            System.out.println("Error");
//            System.exit(1);
//        }
//
//        for (int i = 0; i < size; ++i) {
//            if (!list.get(0).get(i).equals(list1.get(0).get(i)) || !list.get(1).get(i).equals(list1.get(1).get(i)) ) {
//                System.out.println("...........Ups");
//                System.exit(1);
//            }
//        }
//        System.out.println("OK");

//        EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                SimpleFrame frame = new SimpleFrame();
//                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//                frame.setVisible(true);
//            }
//        });




//        Music test = new Music();
//
////        testAnalyzator();
//        Vector<Integer> testText;
//        String textName = "testkurs.txt";
//        TextReader textreader = new TextReader();
//        testText = textreader.readFile(textName);
//        String filename = "vals.mp3";
//
//        Vector<Integer> testMusic;
//        testMusic = test.getData(filename);
//
//        Analizator analizator = new Analizator();
//        analizator.analize(testMusic, testText);
//        Vector<Integer> endVector = analizator.getEndVector();
//        int N = 25;

//        System.out.println();
//        Complex[] x = new Complex[4];
//        x[0] = new Complex(0, 1);
//        x[1] = new Complex(2, 3);
//        x[2] = new Complex(4, 5);
//        x[3] = new Complex(6, 7);
//
//        for (int i = 0; i < x.length; ++i){
//            System.out.println(x[i]);
//        }
//
//        Complex[] y = FFT.fft(x);
//        Complex[] z = FFT.ifft(y);
//
//        System.out.println();
//        for (int i = 0; i < y.length; ++i){
//            System.out.println(y[i]);
//        }
//        System.out.println();
//        for (int i = 0; i < z.length; ++i){
//            System.out.println(z[i]);
//        }




//        analizator.backAnalize(endVector, N);
//        Vector <Integer> text = analizator.getText();
//
//        PrintWriter out = new PrintWriter("myflle.txt");
//
//        for (int i = 0; i < text.size(); ++i){
//            int a = text.get(i);
//            char b = (char) a;
//            out.println(b);
//        }
//
//        out.close();

        // Слушаешь музыку
        //test.testPlay(filename);


    }
}
