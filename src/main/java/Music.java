import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by elena on 09.11.15.
 */
public class Music {
    private boolean isPlaying = false;
    private boolean stopPlaying = true;

    public boolean isPlaying() {
        return isPlaying;
    }

    public void stop() {
        stopPlaying = true;
    }

    //  прослушивание музыки
    public void testPlay(String filename)
    {
        try {
            File file = new File(filename);
            AudioInputStream in= AudioSystem.getAudioInputStream(file);
            AudioInputStream din = null;
            AudioFormat baseFormat = in.getFormat();
            AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
                    baseFormat.getSampleRate(),
                    16,
                    baseFormat.getChannels(),
                    baseFormat.getChannels() * 2,
                    baseFormat.getSampleRate(),
                    false);
            din = AudioSystem.getAudioInputStream(decodedFormat, in);
            // Play now.
            stopPlaying = false;
            isPlaying = true;
            rawplay(decodedFormat, din);
            in.close();
            isPlaying = false;
        } catch (Exception e)
        {
            System.out.println(e);
            //Handle exception.
        }
    }

    private void rawplay(AudioFormat targetFormat, AudioInputStream din) throws IOException, LineUnavailableException {
        byte[] data = new byte[4096];
        SourceDataLine line = getLine(targetFormat);
        if (line != null)
        {
            // Start
            line.start();
            int nBytesRead = 0, nBytesWritten = 0;
            while (nBytesRead != -1 && !stopPlaying) {
                nBytesRead = din.read(data, 0, data.length);
                if (nBytesRead != -1) nBytesWritten = line.write(data, 0, nBytesRead);
            }
            // Stop
            line.drain();
            line.stop();
            line.close();
            din.close();
        }
    }

    private SourceDataLine getLine(AudioFormat audioFormat) throws LineUnavailableException
    {
        SourceDataLine res = null;
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
        res = (SourceDataLine) AudioSystem.getLine(info);
        res.open(audioFormat);
        return res;
    }
}
