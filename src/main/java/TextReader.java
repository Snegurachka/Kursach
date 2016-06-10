import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

/**
 * Created by elena on 09.11.15.
 */
public class TextReader {

    public Vector<Integer> readFile(String name) throws IOException {
        int size;

        InputStream f1 = new FileInputStream(name);
        size = f1.available();
        
        Vector<Integer> nabor = new Vector<Integer>();

        for (int i = 0; i < size; i++){
            nabor.add(f1.read());
        }
        return nabor;
    }
}
