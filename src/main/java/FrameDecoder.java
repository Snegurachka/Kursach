import wav.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.PrintWriter;
import java.util.List;

/**
 * Created by elena on 05.12.15.
 */
public class FrameDecoder extends JFrame {
    public static final int DEFAULT_WIDTH = 300;
    public static final int DEFAULT_HEIGHT = 200;

    private String absolutePathToAudioFile = null;
    private Integer startSegment = -1;
    private Integer textSize = -1;

        public FrameDecoder() {
            // setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            JPanel panel = new JPanel();
            panel.setLayout(null);

            final JLabel initialData = new JLabel("<html><font size = +2> Исходные данные: </font>");
            initialData.setSize(400, 50);
            initialData.setLocation(50, 30);
            panel.add(initialData);

            final JLabel audioLabel = new JLabel("<html><font size = +1> Аудио </font>");
            audioLabel.setSize(200, 50);
            audioLabel.setLocation(70, 70);
            panel.add(audioLabel);

            JButton audioButton = new JButton("<html><font size = +1><i> Выбрать аудио </i></font>");
            audioButton.setSize(200, 50);
            audioButton.setLocation(40, 110);
            panel.add(audioButton);

            final JLabel sizeTextLabel = new JLabel("<html><font size = +1> Введите размер сообщения </font>");
            sizeTextLabel.setSize(200, 50);
            sizeTextLabel.setLocation(50, 170);
            panel.add(sizeTextLabel);

            final JTextField sizeTextWindow = new JTextField();
            sizeTextWindow.setSize(200, 50);
            sizeTextWindow.setLocation(40, 220);
            panel.add(sizeTextWindow);

//            final JLabel startSegmentLabel = new JLabel("Введите номер сегмента");
//            startSegmentLabel.setSize(200, 50);
//            startSegmentLabel.setLocation(50, 260);
//            panel.add(startSegmentLabel);

//            final JTextField startSegmentWindow = new JTextField();
//            startSegmentWindow.setSize(200, 50);
//            startSegmentWindow.setLocation(40, 300);
//            panel.add(startSegmentWindow);

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

            JButton solveButton = new JButton("<html><font size = +2><b><i> Извлечь </i></b></font>");
            solveButton.setSize(350, 70);
            solveButton.setLocation(300, 300);
            panel.add(solveButton);

            final JLabel loaderImageLabel = new JLabel("", SwingConstants.RIGHT);
            ImageIcon ii = new ImageIcon("static/load.gif");
            loaderImageLabel.setIcon(ii);
            loaderImageLabel.setSize(200, 200);
            loaderImageLabel.setLocation(400, 70);
            panel.add(loaderImageLabel);
            loaderImageLabel.setVisible(false);

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
                    try {
                        textSize = Integer.parseInt(sizeTextWindow.getText());
//                        startSegment = Integer.parseInt(startSegmentWindow.getText());
                    } catch (Exception exc) {
                        textSize = -1;
                        startSegment = -1;
                    }
                    if (absolutePathToAudioFile != null && startSegment >= 0 && textSize >= 0) {
                        endText1.setText("");
                        endText2.setText("");
                        loaderImageLabel.setVisible(true);
                        new Thread(new Runnable() {
                            public void run() {
                                Test backtest = new Test();
                                backtest.readWav(absolutePathToAudioFile);
                                Integer last = absolutePathToAudioFile.lastIndexOf(".");
                                String newName = absolutePathToAudioFile.substring(0, last);
                                String newEndName = newName + "_result_text.txt";
                                List<List<Long>> backlist = backtest.getBytes();
                                List<Long> backTestMusic = backlist.get(0);
                                Analizator analizator = new Analizator();

                                analizator.backAnalize(backTestMusic, textSize);
                                List<Integer> text = analizator.getText();
                                try {
                                    PrintWriter out = new PrintWriter(newEndName);
                                    for (int i = 0; i < text.size(); ++i) {
                                        int a = text.get(i);
                                        char b = (char) a;
                                        out.print(b);
                                    }
                                    out.close();

                                    Integer startNew = newEndName.lastIndexOf("/");
                                    String oneNameNewAufio = newEndName.substring(startNew + 1);

                                    loaderImageLabel.setVisible(false);
                                    endText.setText("Дешифрование завершено!");
                                    endText1.setText("Извлеченный текстовый файл:");
                                    endText2.setText(oneNameNewAufio);

                                } catch (Exception error) {
                                    System.out.println(error);
                                    System.out.println("Ошибка при чтении в TextReader");
                                }
                            }
                        }).start();
                    } else {
                        endText1.setText("Вы не ввели исходные данные");
                        endText2.setText("Введите исходные данные");
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
            setLocationRelativeTo(null);
            setVisible(true);
        }
    }

