import javax.swing.*;
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
        JButton encodeButton = new JButton("Шифрование");
        encodeButton.setSize(300, 100);
        encodeButton.setLocation(200, 70);

        JButton decodeButton = new JButton("Дешифрование");
        decodeButton.setSize(300, 100);
        decodeButton.setLocation(200, 220);
        buttonPanel.add(encodeButton);
        buttonPanel.add(decodeButton);

        setContentPane(buttonPanel);

        ActionListener newAction = new
                ActionListener() {
            public void actionPerformed(ActionEvent event) {
                FrameEncoder frameEncoder = new FrameEncoder();
                frameEncoder.show();
            }
        };

        ActionListener newAction2 = new
                ActionListener() {
                    public void actionPerformed(ActionEvent event) {
                        FrameDecoder decoder = new FrameDecoder();
                        decoder.show();
                    }
                };
        encodeButton.addActionListener(newAction);
        decodeButton.addActionListener(newAction2);
    }
    private JPanel buttonPanel;
    public static final int DEFAULT_WIDTH = 300;
    public static final int DEFAULT_HEIGHT = 200;
}