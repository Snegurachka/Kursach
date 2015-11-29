package wav;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by neikila on 29.11.15.
 */
public class Test1 {

    private static final int	EXTERNAL_BUFFER_SIZE = 128000;
    private static List<Byte> test = new ArrayList<Byte>();

    public static void readFile(String filename)
    {
        String	strFilename = filename;
        File	soundFile = new File(strFilename);
        File out = new File("out10.wav");

		/*
		  We have to read in the sound file.
		*/
        AudioInputStream audioInputStream = null;
        try
        {
            audioInputStream = AudioSystem.getAudioInputStream(out);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.exit(1);
        }

        AudioFormat audioFormat = audioInputStream.getFormat();
        out(audioFormat.toString());

        try {
            AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, new File("out11.wav"));
            audioInputStream.close();
        } catch (Exception e) {

        }

//        byte[] temp = new byte[1];
//        try {
//        } catch (Exception e) {
//
//        }
        System.exit(0);


        boolean bigEndian = false;
        boolean signed = true;
        int bits = 16;
        int channels = 1;
        AudioFormat format;
        format = audioFormat;

        byte[] buffer = new byte[test.size()];
        for (int i = 0; i < test.size(); ++i) {
            buffer[i] = test.get(i);
        }
    }


    private static void printUsageAndExit()
    {
        out("SimpleAudioPlayer: usage:");
        out("\tjava SimpleAudioPlayer <soundfile>");
        System.exit(1);
    }


    private static void out(String strMessage)
    {
        System.out.println(strMessage);
    }
}
