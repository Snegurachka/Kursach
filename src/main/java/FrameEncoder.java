import wav.Test;

import javax.swing.*;
import java.awt.*;
import java.util.List;
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

    private JButton audioButton;
    private JButton textButton;
    private JButton musicStartPlay;
    private JButton musicStopPlay;
    private JButton musicEndStartPlay;
    private JButton musicEndStopPlay;
    private JButton solveButton;
    private JButton stopStop;

    private JLabel audioLabel;
    private JLabel textLabel;
    private JLabel endText;
    private JLabel endText1;
    private JLabel endText2;
    private JLabel loaderImageLabel;
    private JLabel musicEndName;


    private void addButtons(JPanel panel) {

        audioButton = new JButton("<html><font size = +1><i> Выбрать аудио </i></font>");
        audioButton.setSize(200, 50);
        audioButton.setLocation(40, 110);
        panel.add(audioButton);

        textButton = new JButton("<html><font size = +1><i> Выбрать файл </i></font>");
        textButton.setSize(200, 50);
        textButton.setLocation(40, 200);
        panel.add(textButton);

        musicStartPlay = new JButton(new ImageIcon("static/start32.png"));
        musicStartPlay.setSize(50, 50);
        musicStartPlay.setLocation(250, 109);
        panel.add(musicStartPlay);

        solveButton = new JButton("<html><font size = +2><b><i> Зашифровать </i></b></font>");
        solveButton.setSize(350, 70);
        solveButton.setLocation(300, 300);
        panel.add(solveButton);

        musicEndStartPlay = new JButton(new ImageIcon("static/start32.png"));
        musicEndStartPlay.setSize(50, 50);
        musicEndStartPlay.setLocation(600, 200);
        panel.add(musicEndStartPlay);

        musicStopPlay = new JButton(new ImageIcon("static/stopp32.png"));
        musicStopPlay.setSize(50, 50);
        musicStopPlay.setLocation(304, 109);
        panel.add(musicStopPlay);

        musicEndStopPlay = new JButton(new ImageIcon("static/stopp32.png"));
        musicEndStopPlay.setSize(50, 50);
        musicEndStopPlay.setLocation(650, 200);
        panel.add(musicEndStopPlay);

        stopStop = new JButton("<html><font size = +2><b><i> Отменить </i></b></font>");
        stopStop.setSize(200, 70);
        stopStop.setLocation(50, 300);
        panel.add(stopStop);
        stopStop.setVisible(false);
    }

    private void addLabels(JPanel panel) {
        final JLabel initialData = new JLabel("<html><font size = +2> Исходные данные: </font>");
        initialData.setSize(400, 50);
        initialData.setLocation(50, 30);
        panel.add(initialData);

        audioLabel = new JLabel("<html><font size = +1> Аудио </font>");
        audioLabel.setSize(200, 50);
        audioLabel.setLocation(70, 70);
        panel.add(audioLabel);

        endText = new JLabel("");
        endText.setSize(200, 50);
        endText.setLocation(400, 50);
        panel.add(endText);

        endText1 = new JLabel("");
        endText1.setSize(200, 50);
        endText1.setLocation(400, 80);
        panel.add(endText1);

        endText2 = new JLabel("");
        endText2.setSize(200, 50);
        endText2.setLocation(400, 110);
        panel.add(endText2);

        textLabel = new JLabel("<html><font size = +1> Секретный файл </font>");
        textLabel.setSize(200, 50);
        textLabel.setLocation(70, 160);
        panel.add(textLabel);

        musicEndName = new JLabel("", SwingConstants.RIGHT);
        musicEndName.setSize(300, 50);
        musicEndName.setLocation(300, 200);
        panel.add(musicEndName);

        loaderImageLabel = new JLabel("", SwingConstants.RIGHT);
        ImageIcon ii = new ImageIcon("static/load.gif");
        loaderImageLabel.setIcon(ii);
        loaderImageLabel.setSize(200, 200);
        loaderImageLabel.setLocation(400, 70);
        panel.add(loaderImageLabel);
        loaderImageLabel.setVisible(false);
    }

    public FrameEncoder() {
        player = new Music();
       // setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        panel.setLayout(null);

        addButtons(panel);
        addLabels(panel);

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
                    stopStop.setVisible(true);
                    new Thread(new Runnable() {
                        public void run() {
                            FrameEncoder.this.encode();
                        }
                    }).start();
                } else {
                    endText1.setText("<html><font size = +1> Вы не ввели исходные данные </font>");
                    endText2.setText("<html><font size = +1> Введите исходные данные  </font>");
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

    private void encode() {
        Test test = new Test();
        test.readWav(absolutePathToAudioFile);
        List<List<Long>> list = test.getBytes();
        List<Long> testMusic = list.get(0);
        Gnuplot.printList("testMusic.gnuplot", testMusic);

        TextReader textreader = new TextReader();
        try {
            List<Integer> testText;
            testText = textreader.readFile(absolutePathToTextFile);

            Analizator analizator = new Analizator();
            analizator.analize(testMusic, testText);
            List<Long> endVector = analizator.getEndList();
            Gnuplot.printList("endVector.gnuplot", endVector);

            Integer textSize = analizator.getTextSize();
            Integer last = absolutePathToAudioFile.lastIndexOf(".");
            String newName = absolutePathToAudioFile.substring(0, last);
            newEndName = newName + "_result" + ".wav";
//                              преобразование в один массив
            List<List<Long>> endMusic = new ArrayList<List<Long>>();
            System.out.println(endVector.size());
            System.out.println(list.get(1).size());
            List<Long> endOneMusic1 = new ArrayList<Long>();
            List<Long> endOneMusic2 = new ArrayList<Long>();
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

            setUIResponse(textSize, oneNameNewAufio);
        } catch (Exception error) {
            System.out.println(error);
            System.out.println("Ошибка при чтении в TextReader");
        }
    }

    private void setUIResponse(int textSize, String oneNameNewAudio) {
        endText.setText("<html><font size = +1> Выполнено успешно </font>");
        endText1.setText("<html><font size = +1> Размер файла: </font>" + textSize);
        musicEndName.setText(oneNameNewAudio);
        loaderImageLabel.setVisible(false);
        stopStop.setVisible(false);
        musicEndStartPlay.setVisible(true);
        musicEndStopPlay.setVisible(true);
    }
}


