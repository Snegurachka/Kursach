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
                        FrameDecoder decoder = new FrameDecoder();
                        decoder.show();
                    }
                };
        button.addActionListener(newAction);
        button2.addActionListener(newAction2);
    }
    private JPanel buttonPanel;
    public static final int DEFAULT_WIDTH = 300;
    public static final int DEFAULT_HEIGHT = 200;
}