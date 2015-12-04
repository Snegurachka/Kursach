import wav.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by elena on 05.12.15.
 */
public class FrameDecoder extends JFrame {
        public static final int DEFAULT_WIDTH = 300;
        public static final int DEFAULT_HEIGHT = 200;



        private String absolutePathToAudioFile;
        private Integer startSegment;
        private Integer textSize;

        public FrameDecoder() {
            // setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            JPanel panel = new JPanel();
            panel.setLayout(null);

            final JLabel initialData = new JLabel("Исходные данные:");
            initialData.setSize(200, 50);
            initialData.setLocation(50, 10);
            panel.add(initialData);

            final JLabel audioLabel = new JLabel("Выберите аудио");
            audioLabel.setSize(200, 50);
            audioLabel.setLocation(70, 60);
            panel.add(audioLabel);

            JButton audioButton = new JButton("Выбрать аудио");
            audioButton.setSize(200, 50);
            audioButton.setLocation(40, 100);
            panel.add(audioButton);

            final JLabel sizeTextLabel = new JLabel("Введите размер сообщения");
            sizeTextLabel.setSize(200, 50);
            sizeTextLabel.setLocation(50, 160);
            panel.add(sizeTextLabel);

            final JTextField sizeTextWindow = new JTextField();
            sizeTextWindow.setSize(200, 50);
            sizeTextWindow.setLocation(40, 200);
            panel.add(sizeTextWindow);

            final JLabel startSegmentLabel = new JLabel("Введите номер сегмента");
            startSegmentLabel.setSize(200, 50);
            startSegmentLabel.setLocation(50, 260);
            panel.add(startSegmentLabel);

            final JTextField startSegmentWindow = new JTextField();
            startSegmentWindow.setSize(200, 50);
            startSegmentWindow.setLocation(40, 300);
            panel.add(startSegmentWindow);

            final JLabel endText = new JLabel("");
            endText.setSize(200, 50);
            endText.setLocation(400, 80);
            panel.add(endText);

            final JLabel endText1 = new JLabel("");
            endText1.setSize(200, 50);
            endText1.setLocation(400, 110);
            panel.add(endText1);

            final JLabel endText2 = new JLabel("");
            endText2.setSize(200, 50);
            endText2.setLocation(400, 140);
            panel.add(endText2);

            JButton solveButton = new JButton("Решить");
            solveButton.setSize(400, 100);
            solveButton.setLocation(300, 300);
            panel.add(solveButton);

            audioButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JFileChooser fileopen = new JFileChooser();
                    int ret = fileopen.showDialog(null, "Открыть файл");
                    if (ret == JFileChooser.APPROVE_OPTION) {
                        File file = fileopen.getSelectedFile();
                        absolutePathToAudioFile = file.getAbsolutePath();
                        audioLabel.setText(file.getName());
                    }
                }
            });

            solveButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                   // String backfilename = "1.wav";
                    Test backtest = new Test();
                    backtest.readWav(absolutePathToAudioFile);
                    Integer last = absolutePathToAudioFile.lastIndexOf(".");
                    String newName = absolutePathToAudioFile.substring(0, last);
                    String newEndName = newName + "_result_text.txt";
                    List<List<Long>> backlist = backtest.getBytes();
                    List<Long> backTestMusic = backlist.get(0);
                    Analizator analizator = new Analizator();
                    textSize = Integer.parseInt(sizeTextWindow.getText());
                    startSegment = Integer.parseInt(startSegmentWindow.getText());
                    analizator.backAnalize(backTestMusic, textSize, startSegment);
                    List<Integer> text = analizator.getText();
                    try {
                        PrintWriter out = new PrintWriter(newEndName);
                        for (int i = 0; i < text.size(); ++i){
                            int a = text.get(i);
                            char b = (char) a;
                            out.print(b);
                        }
                        out.close();

                        Integer startNew = newEndName.lastIndexOf("/");
                        String oneNameNewAufio = newEndName.substring(startNew + 1);
                        String NewNameFile = oneNameNewAufio;

                        endText.setText("Дешифрование завершено!");
                        endText1.setText("Извлеченный текстовый файл:");
                        endText2.setText(NewNameFile);
                    } catch (Exception error) {
                        System.out.println(error);
                        System.out.println("Ошибка при чтении в TextReader");
                    }
                }
            });

            getContentPane().add(panel);

            Toolkit kit = Toolkit.getDefaultToolkit();
            Dimension screenSize = kit.getScreenSize();
            int screenHeight = screenSize.height;
            int screenWidth = screenSize.width;
            //Установка ширины и высоты фрейм и позиционирвание с помощью платформы
            setSize(screenWidth / 2, screenHeight / 2);
            setLocationByPlatform(true);
            setLocation(DEFAULT_WIDTH, DEFAULT_HEIGHT);
            //Установка пиктограммы и заголовка окна
            // Image img = kit.getImage("icon.gif");
            // setIconImage(img);
            setTitle("Дешифрование");
//        setPreferredSize(new Dimension(260, 220));
//        pack();
            setLocationRelativeTo(null);
            setVisible(true);
        }

        public String getAbsolutePathToAudioFile() {
            return absolutePathToAudioFile;
        }

    }

