import wav.Test;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws Exception {
/*
//      получение массива аудио
        String musikName = "Vagner.wav";
        Test test = new Test();
        test.readWav(musikName);
        List<List<Long>> list = test.getBytes();
        List<Long> testMusic = list.get(0);
//        System.out.println(list.get(0).size());

//      получение массива текста
        String textName = "text.txt";
        TextReader textreader = new TextReader();
        List<Integer> testText;
        testText = textreader.readFile(textName);
//        System.out.println(testText);

//        получение массива изображения
        byte[] bytesFromFile = null;
        String filename = "test2-min.jpg";
        List <Integer> listImage = new ArrayList<Integer>();
        try {
            // Получение массива байт из картинки
            bytesFromFile = Files.readAllBytes(Paths.get(filename));
            // Вывод в консоль массива байт, если убрать "(char)", то увидишь просто поток цифр.
            for (int i = 0; i < bytesFromFile.length; ++i) {
//                System.out.print(bytesFromFile[i]);
//                System.out.print(", ");
                listImage.add((int)bytesFromFile[i]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

//      запись в аудио текста
        Analizator analizator = new Analizator();
//        analizator.analize(testMusic, testText);
        analizator.analize(testMusic, listImage);
        List<Long> endVector = analizator.getEndList();
        Integer textSize = analizator.getTextSize();
        Integer nomerElementa = analizator.getNomerElementa();
//        System.out.print("nomer:" + nomerElementa);

//      преобразование в один массив
        List<List<Long>> endMusic = new ArrayList<List<Long>>();
        List<Long> endOneMusic1 = new ArrayList<Long>();
        List<Long> endOneMusic2 = new ArrayList<Long>();

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
        analizator.backAnalize(backTestMusic, textSize);
        List<Integer> text = analizator.getText();
//        System.out.println();
//        System.out.println(text);
//        new Gnuplot(analizator).printAll();

//        System.out.println();
//        System.out.println(listImage);
//        System.out.println();
//        System.out.println(text);

//        получение изображения
        sbor(text);

*/

//      получение текстового файла
//        PrintWriter out = new PrintWriter("resulttext.txt");
//        for (int i = 0; i < text.size(); ++i) {
//            int a = text.get(i);
//            char b = (char) a;
//            out.print(b);
//        }
//        out.close();

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                SimpleFrame frame = new SimpleFrame();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
            }
        });
    }

//  получение изображения
    public static void sbor (List<Integer> image)
    {
        try {
            // Запись в НОВЫЙ файл путь до которого someFile.png
            final FileOutputStream fos = new FileOutputStream(new File("someFile1.jpg"));
            for (Integer num : image) {
                fos.write(num);
            }
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}

