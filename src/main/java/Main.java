import wav.Test;

import java.io.File;
import java.io.PrintWriter;
import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws Exception {

//      получение массива текста
        String musikName = "Vagner.wav";
        Test test = new Test();
        test.readWav(musikName);
        List<List<Long>> list = test.getBytes();
        List<Long> testMusic = list.get(0);

//      получение массива текста
        String textName = "text.txt";
        TextReader textreader = new TextReader();
        List<Integer> testText;
        testText = textreader.readFile(textName);

//      запись в аудио текста
        Analizator analizator = new Analizator();
        analizator.analize(testMusic, testText);
        List<Long> endVector = analizator.getEndList();
        Integer startSegment = analizator.getStartSegment();
        Integer textSize = analizator.getTextSize();
        Integer nomerElementa = analizator.getNomerElementa();
        System.out.print("nomer:" + nomerElementa);


//        System.out.println(list.get(1).size());

//      преобразование в один массив
        List<List<Long>> endMusic = new ArrayList<List<Long>>();
        List<Long> endOneMusic1 = new ArrayList<Long>();
        List<Long> endOneMusic2 = new ArrayList<Long>();
        System.out.print("1 :");
        System.out.println(endVector.size());
        System.out.print("2 :");
        System.out.println(list.get(1).size());

        for (int i = nomerElementa; i < list.get(1).size(); i++){
            endOneMusic2.add(list.get(1).get(i));
        }

        if (endOneMusic2.size() > endVector.size()){
            for (int i = 0; i < endOneMusic2.size(); i++){
                if (i < endVector.size()){
                    endOneMusic1.add(endVector.get(i));
                } else
                    endOneMusic1.add((long) 0);
            }
        } else {
            for (int i = 0; i < endOneMusic2.size(); i++){
                endOneMusic1.add(endVector.get(i));
            }
        }

        System.out.print("1 :");
        System.out.println(endOneMusic1.size());
        System.out.print("2 :");
        System.out.println(endOneMusic2.size());
        endMusic.add(endOneMusic1);
        endMusic.add(endOneMusic2);



////        ---------------------------------------
        test.modificationBytes(endMusic);
        test.writeWav("1.wav");

        String filename2 = "1.wav";
        Music test1 = new Music();
        test1.testPlay(filename2);
//
//        ----------------------------------------

        String backfilename = "1.wav";
        Test backtest = new Test();
        backtest.readWav(backfilename);
        List<List<Long>> backlist = backtest.getBytes();
        List<Long> backTestMusic = backlist.get(0);

//        Analizator analizator = new Analizator();
        analizator.backAnalize(backTestMusic, textSize, startSegment);
        List<Integer> text = analizator.getText();
//        new Gnuplot(analizator).printAll();

        PrintWriter out = new PrintWriter("resulttext.txt");

        for (int i = 0; i < text.size(); ++i) {
            int a = text.get(i);
            char b = (char) a;
            out.print(b);
        }

        out.close();

//        EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                SimpleFrame frame = new SimpleFrame();
//                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//                frame.setVisible(true);
//            }
//        });
    }
}


//        System.out.println();
//        Complex[] x = new Complex[16];
//        x[0] = new Complex(0, 0);
//        x[1] = new Complex(1, 0);
//        x[2] = new Complex(2, 0);
//        x[3] = new Complex(3, 0);
//        x[4] = new Complex(4, 0);
//        x[5] = new Complex(5, 0);
//        x[6] = new Complex(6, 0);
//        x[7] = new Complex(7, 0);
//        x[8] = new Complex(8, 0);
//        x[9] = new Complex(9, 0);
//        x[10] = new Complex(10, 0);
//        x[11] = new Complex(11, 0);
//        x[12] = new Complex(12, 0);
//        x[13] = new Complex(13, 0);
//        x[14] = new Complex(14, 0);
//        x[15] = new Complex(15, 0);
//
//        Complex[] y = FFT.fft(x);
//
//
////        for (int i = 0; i < x.length; ++i){
////            System.out.println(x[i]);
////        }
//
//        Complex[] z = FFT.ifft(y);
//
//        System.out.println();
//        for (int i = 0; i < y.length; ++i){
//            System.out.println(y[i] + ", ");
//        }
//        System.out.println();
//        for (int i = 0; i < z.length; ++i){
//            System.out.println(z[i]);
//        }

//        Music test1 = new Music();
//        test1.testPlay(filename);

//        List<List<Long>> list1 = test1.getBytes();

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

