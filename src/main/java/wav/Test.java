package wav;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by neikila on 28.11.15.
 */

public class Test {
    public List<List <Long>> bytes = new ArrayList<List <Long>>();
    public List<List<Long>> bytesNew = new ArrayList<List<Long>>();

    public List<List<Long>> getBytes() {
        return bytes;
    }

    public void modificationBytes (List<List<Long>> a){
        for (int i = 0; i < a.size(); ++i){
            List<Long> byteOne = new ArrayList<Long>();
            for (int k = 0; k < a.get(i).size(); ++k){
                byteOne.add(a.get(i).get(k));
            }
            bytesNew.add(byteOne);
        }
    }

    public void readWav(String filename) {
        try
        {
            // Open the wav file specified as the first argument
            WavFile wavFile = WavFile.openWavFile(new File(filename));

            // Display information about the wav file
            wavFile.display();

            // Get the number of audio channels in the wav file
            int numChannels = wavFile.getNumChannels();
            for (int i = 0; i < numChannels; ++i) {
                bytes.add(new ArrayList<Long>());
            }

            // Create a buffer of 100 frames
            long[] buffer = new long[100 * numChannels];

            int framesRead;
//            double min = Double.MAX_VALUE;
//            double max = Double.MIN_VALUE;

            do {
                // Read frames into buffer
                framesRead = wavFile.readFrames(buffer, 100);

                // Loop through frames and look for minimum and maximum value
                for (int s=0 ; s<framesRead; s++)
                {
                    bytes.get(0).add(buffer[2 * s]);
                    bytes.get(1).add(buffer[2 * s + 1]);
//                    if (buffer[s] > max) max = buffer[s];
//                    if (buffer[s] < min) min = buffer[s];
                }
            } while (framesRead != 0);

            // Close the wavFile
            wavFile.close();

            // Output the minimum and maximum value
//            System.out.printf("Min: %f, Max: %f\n", min, max);
        }
        catch (Exception e)
        {
            System.err.println(e);
        }
    }

    public void writeWav(String filename)
    {
        try
        {
            int sampleRate = 44100;		// Samples per second

            // Calculate the number of frames required for specified duration
            long numFrames = bytesNew.get(0).size();

            // Create a wav file with the name specified as the first argument
            WavFile wavFile = WavFile.newWavFile(new File(filename), 2, numFrames, 16, sampleRate);

            // Create a buffer of 100 frames
            long[][] buffer = new long[2][100];

            // Initialise a local frame counter
            long frameCounter = 0;

            // Loop until all frames written
            while (frameCounter < numFrames)
            {
                // Determine how many frames to write, up to a maximum of the buffer size
                long remaining = wavFile.getFramesRemaining();
                int toWrite = (remaining > 100) ? 100 : (int) remaining;

                // Fill the buffer, one tone per channel
                for (int s=0 ; s<toWrite ; s++, frameCounter++)
                {
                    buffer[0][s] = bytesNew.get(0).get((int)frameCounter);
                    buffer[1][s] = bytesNew.get(1).get((int)frameCounter);
                }

                // Write the buffer
                wavFile.writeFrames(buffer, toWrite);
            }

            // Close the wavFile
            wavFile.close();
        }
        catch (Exception e)
        {
            System.err.println(e);
        }
    }
}
