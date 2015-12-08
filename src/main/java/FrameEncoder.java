import wav.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by elena on 28.11.15.
 */

public class FrameEncoder extends JFrame {

    public static final int DEFAULT_WIDTH = 300;
    public static final int DEFAULT_HEIGHT = 200;

    private String absolutePathToAudioFile = null;
    private String absolutePathToTextFile = null;
    private String newEndName;
    final private Music player;
    private Thread play;

    public FrameEncoder() {
        player = new Music();
       // setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        panel.setLayout(null);

        final JLabel initialData = new JLabel("Исходные данные:");
        initialData.setSize(200, 50);
        initialData.setLocation(50, 10);
        panel.add(initialData);

        final JLabel audioLabel = new JLabel("аудио");
        audioLabel.setSize(200, 50);
        audioLabel.setLocation(70, 60);
        panel.add(audioLabel);

        JButton audioButton = new JButton("Выбрать аудио");
        audioButton.setSize(200, 50);
        audioButton.setLocation(40, 100);
        panel.add(audioButton);

        final JLabel endText = new JLabel("");
        endText.setSize(200, 50);
        endText.setLocation(400, 50);
        panel.add(endText);

        final JLabel endText1 = new JLabel("");
        endText1.setSize(200, 50);
        endText1.setLocation(400, 80);
        panel.add(endText1);

        final JLabel endText2 = new JLabel("");

        endText2.setSize(200, 50);
        endText2.setLocation(400, 110);
        panel.add(endText2);

        final JLabel textLabel = new JLabel("текст");
        textLabel.setSize(200, 50);
        textLabel.setLocation(70, 150);
        panel.add(textLabel);

        JButton textButton = new JButton("Выбрать текст");
        textButton.setSize(200, 50);
        textButton.setLocation(40, 190);
        panel.add(textButton);

        JButton musicStartPlay = new JButton(new ImageIcon("static/playButtonImage.png"));
        musicStartPlay.setSize(50, 50);
        musicStartPlay.setLocation(250, 99);
        panel.add(musicStartPlay);

        final JLabel musicEndName = new JLabel("", SwingConstants.RIGHT);
        musicEndName.setSize(300, 50);
        musicEndName.setLocation(300, 200);
        panel.add(musicEndName);

        JButton solveButton = new JButton("Решить");
        solveButton.setSize(400, 100);
        solveButton.setLocation(300, 300);
        panel.add(solveButton);

        final JButton musicEndStartPlay = new JButton(new ImageIcon("static/playButtonImage.png"));
        musicEndStartPlay.setSize(50, 50);
        musicEndStartPlay.setLocation(600, 200);
        panel.add(musicEndStartPlay);

        final JButton musicStopPlay = new JButton(new ImageIcon("static/stoop2.png"));
        musicStopPlay.setSize(50, 50);
        musicStopPlay.setLocation(300, 99);
        panel.add(musicStopPlay);

        final JButton musicEndStopPlay = new JButton(new ImageIcon("static/stoop2.png"));
        musicEndStopPlay.setSize(50, 50);
        musicEndStopPlay.setLocation(650, 200);
        panel.add(musicEndStopPlay);


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

        textButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileopen = new JFileChooser();
                int ret = fileopen.showDialog(null, "Открыть файл");
                if (ret == JFileChooser.APPROVE_OPTION) {
                    File file = fileopen.getSelectedFile();
                    textLabel.setText(file.getName());
                    absolutePathToTextFile = file.getAbsolutePath();
                }
            }
        });

        musicStartPlay.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                playMusic(absolutePathToAudioFile);
            }
        });
        musicStopPlay.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                player.stop();
            }
        });

        musicEndStartPlay.setVisible(false);
        musicEndStopPlay.setVisible(false);

        musicEndStartPlay.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                playMusic(newEndName);
            }
        });

        musicEndStopPlay.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                player.stop();
            }
        });

        solveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (absolutePathToAudioFile != null && absolutePathToTextFile != null) {
                    endText1.setText("");
                    endText2.setText("");
                    loaderImageLabel.setVisible(true);
                    new Thread(new Runnable() {
                        public void run() {
                            Test test = new Test();
                            test.readWav(absolutePathToAudioFile);
                            java.util.List<java.util.List<Long>> list = test.getBytes();
                            java.util.List<Long> testMusic = list.get(0);
                            Gnuplot.printList("testMusic.gnuplot", testMusic);

//                            System.out.println(list.get(0).size());
//                            System.out.println(list.get(1).size());

                            TextReader textreader = new TextReader();
                            try {
                                java.util.List<Integer> testText;
                                testText = textreader.readFile(absolutePathToTextFile);

                                Analizator analizator = new Analizator();
                                analizator.analize(testMusic, testText);
                                java.util.List<Long> endVector = analizator.getEndList();
                                Gnuplot.printList("endVector.gnuplot", endVector);

                                Integer startSegment = analizator.getStartSegment();
                                Integer textSize = analizator.getTextSize();
                                Integer last = absolutePathToAudioFile.lastIndexOf(".");
                                String newName = absolutePathToAudioFile.substring(0, last);
                                newEndName = newName + "_" + textSize + "_" + startSegment + ".wav";
//                              преобразование в один массив
                                java.util.List<java.util.List<Long>> endMusic = new ArrayList<java.util.List<Long>>();
                                System.out.println(endVector.size());
                                System.out.println(list.get(1).size());
                                java.util.List<Long> endOneMusic1 = new ArrayList<Long>();
                                java.util.List<Long> endOneMusic2 = new ArrayList<Long>();
                                for (int i = 0; i < endVector.size(); ++i) {
                                    if (i < list.get(1).size() - 1) {
                                        endOneMusic1.add(endVector.get(i));
                                        endOneMusic2.add(list.get(1).get(i));
                                    } else {
                                        endOneMusic1.add(endVector.get(i));
                                        endOneMusic2.add((long) 0 );
                                    }
                                }
                                System.out.println(endOneMusic1.size());
                                System.out.println(endOneMusic2.size());
                                endMusic.add(endOneMusic1);
                                endMusic.add(endOneMusic2);

                                test.modificationBytes(endMusic);
                                test.writeWav(newEndName);
                                Integer lastNew = newEndName.lastIndexOf("/");
                                String oneNameNewAufio = newEndName.substring(lastNew + 1);

                                endText.setText("Выполнено успешно!");
                                endText1.setText("Размер текста: " + textSize);
                                endText2.setText("Номер сегмента: " + startSegment);
                                musicEndName.setText(oneNameNewAufio);
                                loaderImageLabel.setVisible(false);
                                musicEndStartPlay.setVisible(true);
                                musicEndStopPlay.setVisible(true);
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
        setTitle("Шифрование");
//        setPreferredSize(new Dimension(260, 220));
//        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public String getAbsolutePathToAudioFile() {
        return absolutePathToAudioFile;
    }

    public String getAbsolutePathToTextFile() {
        return absolutePathToTextFile;
    }

    public String getNewEndName() { return newEndName; }

    private void playMusic(final String filenameToPlay) {
        if (player.isPlaying()) {
            player.stop();
            if (play != null)
                try {
                    play.join();
                } catch (Exception exc) {

                }
        }
        play = new Thread(new Runnable() {
            public void run() {
                player.testPlay(filenameToPlay);
            }
        });
        play.start();
    }
}


