import java.util.Vector;

/**
 * Created by elena on 09.11.15.
 */
public class Analizator {

    public Vector<Integer> analize (Vector<Integer> music, Vector<Integer> text){

        Vector <Integer> endVector = new Vector<Integer>();

        Vector<Complex[]> endMass;
        endMass = razbienie(music, text);

        //FFT.show(x, "x");

        // FFT of original data
       // Complex[] y = FFT.fft(x);
        //  FFT.show(y, "y = fft(x)");



        return endVector;
    }
    //разбиение на сегменты массива аудио
    public Vector<Complex[]> razbienie (Vector<Integer> music, Vector<Integer> text){
        int sizeText = text.size();
        int sizeMusic = music.size();
        int sizeBitText = sizeText*8;


        int v = (int) Math.ceil((Math.log10((double) sizeBitText) * 3.322 + 1));
        int K =(int) Math.pow(2.0, (double) v+1);
        int N =(int) Math.ceil(sizeMusic / K); // количество сегментов
        double Nn = sizeMusic / K;

        Vector <Complex[]> mass = new Vector<Complex[]>();
        for (int i = 0; i < 1; i ++){
            Complex[] massSegment = new Complex[K];
            for ( int k = 0; k < K; k ++){
                massSegment[k] = new Complex(k, 0);
               // massSegment[k] = new Complex (-2*Math.random() + 1, 0);
                massSegment[k] = new Complex (music.get(i*K+k), 0);
                System.out.println(massSegment[k]);
            }
            mass.add(massSegment);
        }
        return mass;
    }



    public int analyze(Integer a) {
        int counter = 0;
        for (int k = 7; k >= 0; --k) {
            if ((a & (1 << k)) > 0) {
                //System.out.print('1');
                ++counter;
            } else {
                //System.out.print('0');
            }
        }
        //System.out.println(" - " + counter);
        return counter;
    }

    public void analize (Vector<Integer> temp){
        for (int i = 0; i < temp.size(); ++i) {
            //System.out.println("Temp[" + i + "]: " + (char)temp.get(i).intValue() + ' ' + temp.get(i));
            analyze(temp.get(i));
        }

    }

}
