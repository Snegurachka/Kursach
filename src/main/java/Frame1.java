import javax.swing.*;
import java.awt.*;
import java.util.Vector;
import  java.io.PrintWriter;
/**
 * Created by elena on 28.11.15.
 */

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Frame1 extends JFrame {

    public Frame1() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(null);
//        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

//        panel.add(Box.createVerticalGlue());

        final JLabel label = new JLabel("аудио");
//        label.setAlignmentX(CENTER_ALIGNMENT);
        label.setSize(200, 50);
        label.setLocation(70, 20);
        panel.add(label);

//        panel.add(Box.createRigidArea(new Dimension(10, 10)));

        JButton button = new JButton("Выбрать аудио");
        button.setSize(200, 50);
        button.setLocation(40, 70);
        panel.add(button);

//        button.setAlignmentX(CENTER_ALIGNMENT);

        final JLabel label2 = new JLabel("текст");
//        label.setAlignmentX(CENTER_ALIGNMENT);
        label2.setSize(200, 50);
        label2.setLocation(70, 150);
        panel.add(label2);

//        panel.add(Box.createRigidArea(new Dimension(10, 10)));

        JButton button2 = new JButton("Выбрать текст");
        button2.setSize(200, 50);
        button2.setLocation(40, 200);
        panel.add(button2);

        JButton button3 = new JButton("Решить");
        button3.setSize(400, 100);
        button3.setLocation(300, 300);
        panel.add(button3);


        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileopen = new JFileChooser();
                int ret = fileopen.showDialog(null, "Открыть файл");
                if (ret == JFileChooser.APPROVE_OPTION) {
                    File file = fileopen.getSelectedFile();
                    label.setText(file.getName());
                }
            }
        });

        button2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileopen = new JFileChooser();
                int ret = fileopen.showDialog(null, "Открыть файл");
                if (ret == JFileChooser.APPROVE_OPTION) {
                    File file = fileopen.getSelectedFile();
                    label2.setText(file.getName());
                }
            }
        });

        button3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Music test = new Music();
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
            }
        });

//        panel.add(button);
//        panel.add(Box.createVerticalGlue());
        getContentPane().add(panel);

        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int screenHeight = screenSize.height;
        int screenWidth = screenSize.width;
        //Установка ширины и высоты фрейма
        // и позиционирвание с помощью платформы
        setSize(screenWidth / 2, screenHeight / 2);
        setLocationByPlatform(true);
        setLocation(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        //Установка пиктограммы и заголовка окна
        // Image img = kit.getImage("icon.gif");
        // setIconImage(img);
        setTitle("Выбор метода");
//        setPreferredSize(new Dimension(260, 220));
//        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static final int DEFAULT_WIDTH = 300;
    public static final int DEFAULT_HEIGHT = 200;
//    public static void main(String[] args) {
//        javax.swing.SwingUtilities.invokeLater(new Runnable() {
//            public void run() {
//                JFrame.setDefaultLookAndFeelDecorated(true);
//                JDialog.setDefaultLookAndFeelDecorated(true);
//                new TestFrame();
//            }
//        });
//    }
}


//public class Frame1 extends JFrame {
//        public Frame1(){
//            //  определение размеров экрана
//            Toolkit kit = Toolkit.getDefaultToolkit();
//            Dimension screenSize = kit.getScreenSize();
//            int screenHeight = screenSize.height;
//            int screenWidth = screenSize.width;
//            //Установка ширины и высоты фрейма
//            // и позиционирвание с помощью платформы
//            setSize(screenWidth / 2, screenHeight / 2);
//            setLocationByPlatform(true);
//            setLocation(DEFAULT_WIDTH, DEFAULT_HEIGHT);
//            //Установка пиктограммы и заголовка окна
//
//            setTitle("Параметры");
//            JPanel buttonPanel = new JPanel();
//            buttonPanel.setLayout(null);
//            JButton button = new JButton("111");
//            button.setSize(300, 100);
//            button.setLocation(200,70);
//            buttonPanel.add(button);
//
//            setContentPane(buttonPanel);
//        }
//        private JPanel buttonPanel;
//
//        public static final int DEFAULT_WIDTH = 300;
//        public static final int DEFAULT_HEIGHT = 200;
//    }

