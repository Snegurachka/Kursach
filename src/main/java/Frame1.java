import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Vector;

/**
 * Created by elena on 28.11.15.
 */

public class Frame1 extends JFrame {

    public static final int DEFAULT_WIDTH = 300;
    public static final int DEFAULT_HEIGHT = 200;

    private String absolutePathToAudioFile;
    private String absolutePathToTextFile;

    public Frame1() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(null);
//        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

//        panel.add(Box.createVerticalGlue());

        final JLabel audioLabel = new JLabel("аудио");
//        audioLabel.setAlignmentX(CENTER_ALIGNMENT);
        audioLabel.setSize(200, 50);
        audioLabel.setLocation(70, 20);
        panel.add(audioLabel);

//        panel.add(Box.createRigidArea(new Dimension(10, 10)));

        JButton audioButton = new JButton("Выбрать аудио");
        audioButton.setSize(200, 50);
        audioButton.setLocation(40, 70);
        panel.add(audioButton);

//        audioButton.setAlignmentX(CENTER_ALIGNMENT);

        final JLabel textLabel = new JLabel("текст");
//        audioLabel.setAlignmentX(CENTER_ALIGNMENT);
        textLabel.setSize(200, 50);
        textLabel.setLocation(70, 150);
        panel.add(textLabel);

//        panel.add(Box.createRigidArea(new Dimension(10, 10)));

        JButton textButton = new JButton("Выбрать текст");
        textButton.setSize(200, 50);
        textButton.setLocation(40, 200);
        panel.add(textButton);

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

        solveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Music test = new Music();
                Vector<Integer> testText;
                String textName = "testkurs.txt";
                TextReader textreader = new TextReader();
                try {
                    testText = textreader.readFile(textName);
//                    testText = textreader.readFile(absolutePathToTextFile);
                    String filename = "vals.mp3";

                    Vector<Integer> testMusic;
                    testMusic = test.getData(filename);
//                    testMusic = test.getData(absolutePathToAudioFile);

                    Analizator analizator = new Analizator();
//                    analizator.analize(testMusic, testText);
//                    Vector<Integer> endVector = analizator.getEndVector();
                } catch (Exception error) {
                    System.out.println(error);
                    System.out.println("Ошибка при чтении в TextReader");
                }
            }
        });

//        panel.add(audioButton);
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
//    public static void main(String[] args) {
//        javax.swing.SwingUtilities.invokeLater(new Runnable() {
//            public void run() {
//                JFrame.setDefaultLookAndFeelDecorated(true);
//                JDialog.setDefaultLookAndFeelDecorated(true);
//                new TestFrame();
//            }
//        });
//    }

    public String getAbsolutePathToAudioFile() {
        return absolutePathToAudioFile;
    }

    public String getAbsolutePathToTextFile() {
        return absolutePathToTextFile;
    }
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

