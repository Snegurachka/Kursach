import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by elena on 28.11.15.
 */
public class SimpleFrame extends JFrame {
    public SimpleFrame() {
        //  определение размеров экрана
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
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(null);
        JButton button = new JButton("Шифрование");
        button.setSize(300, 100);
        button.setLocation(200, 70);

        JButton button2 = new JButton("Дешифрование");
        button2.setSize(300, 100);
        button2.setLocation(200, 220);
        buttonPanel.add(button);
        buttonPanel.add(button2);

        setContentPane(buttonPanel);

        ActionListener newAction = new
                ActionListener() {
            public void actionPerformed(ActionEvent event) {
                Frame1 frame1 = new Frame1();
                frame1.show();
            }
        };
        ActionListener newAction2 = new
                ActionListener() {
                    public void actionPerformed(ActionEvent event) {
                        Frame1 frame1 = new Frame1();
                        frame1.show();
                    }
                };

        button.addActionListener(newAction);
        button2.addActionListener(newAction2);
    }

    private JPanel buttonPanel;

    public static final int DEFAULT_WIDTH = 300;
    public static final int DEFAULT_HEIGHT = 200;
    }
//        //определение размеров экрана
//        Toolkit kit = Toolkit.getDefaultToolkit();
//        Dimension screenSize = kit.getScreenSize();
//        int screenHeight = screenSize.height;
//        int screenWidth = screenSize.width;
//        //Установка ширины и высоты фрейма
//        // и позиционирвание с помощью платформы
//        setSize(screenWidth / 2, screenHeight / 2);
//        setLocationByPlatform(true);
//        setLocation(DEFAULT_WIDTH, DEFAULT_HEIGHT);
//        //Установка пиктограммы и заголовка окна
//        Image img = kit.getImage("icon.gif");
//        setIconImage(img);
//        setTitle("тра-ла-ла");
//
//        buttonPanel = new JPanel();
//
//
//
//
//        JButton yellow = new JButton("Yellow");
//        JButton blue = new JButton("Blue");
//
//        buttonPanel = new JPanel();
//        buttonPanel.add(yellow);
//        buttonPanel.add(blue);
//        add(buttonPanel);
//
//        //создание действий кнопок
//        CAction yellowAction = new CAction(Color.YELLOW);
//        CAction blueAction = new CAction(Color.BLUE);
//
//        //связывание действий с кнопками
//        yellow.addActionListener(yellowAction);
//        blue.addActionListener(blueAction);
//    }
//    private class CAction implements ActionListener{
//        public CAction(Color c){
//            backgroundColor = c;
//        }
//        public void actionPerformed (ActionEvent event){
//            buttonPanel.setBackground(backgroundColor);
//        }
//        private Color backgroundColor;
//    }
//
//    private JPanel buttonPanel;
//
//    public static final int DEFAULT_WIDTH = 300;
//    public static final int DEFAULT_HEIGHT = 200;



//Box mainBox = Box.createVerticalBox();
//Box box1 = Box.createVerticalBox();
//
//buttonPanel = new JPanel();
//        JToggleButton tButton1 = new JToggleButton("Кнопка выбора 1");
//        JToggleButton tButton2 = new JToggleButton("Кнопка выбора 2");
//        ButtonGroup bg = new ButtonGroup(); // создаем группу взаимного исключения
//        bg.add(tButton1);
//        bg.add(tButton2); // сделали кнопки tButton1 и tButton2 взаимоисключающими
//        box1.add(tButton1);
//        box1.add(tButton2); // добавили кнопки tButton1 и tButton2 на панель box1
//        box1.setBorder(new TitledBorder("Выбор"));
////            Box box2 = Box.createVerticalBox();
////            JCheckBox check1 = new JCheckBox("Флажок 1");
////            JCheckBox check2 = new JCheckBox("Флажок 2");
////            box2.add(check1);
////            box2.add(check2); // добавили флажки на панель box2
////            box2.setBorder(new TitledBorder("Флажки"));
////            Box box3 = Box.createVerticalBox();
////            JRadioButton rButton1 = new JRadioButton("Переключатель 1");
////            JRadioButton rButton2 = new JRadioButton("Переключатель 2");
////            bg = new ButtonGroup(); // создаем группу взаимного исключения
////
////            bg.add(rButton1);
////            bg.add(rButton2); // сделали радиокнопки взаимоисключающими
////            box3.add(rButton1);
////            box3.add(rButton2); // добавили радиокнопки на панель box3
////            box3.setBorder(new TitledBorder("Переключатели"));
//        mainBox.add(box1);
////            mainBox.add(box2);
////            mainBox.add(box3);
//        setContentPane(mainBox);
//
////            pack();