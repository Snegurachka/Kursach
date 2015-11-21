import java.util.Vector;

/**
 * Created by elena on 09.11.15.
 */
public class Analizator {

    public Vector<Integer> analize (Vector<Integer> music, Vector<Integer> text){

        Vector <Integer> endVector = new Vector<Integer>();

        Vector<Complex[]> endMass;
        endMass = razbienie(music, text);

        Vector<Complex[]> fEndMass= new Vector<Complex[]>();
        for( int i = 0; i < endMass.size(); i++){
            Complex[] fftMass = FFT.fft(endMass.get(i));
            fEndMass.add(fftMass);
        }

/*        for (int i = 0; i < fEndMass.get(0).length; ++i) {
            System.out.println(fEndMass.get(20)[i]);
        }*/

// получение амплитуд
        Vector<Vector<Double>> amplitudeVector = new Vector<Vector<Double>>();
        for (int i = 0; i < fEndMass.size(); i++){
            Vector<Double> amplitude = new Vector<Double>();
            for (int k = 0; k < fEndMass.get(i).length; k++) {
                Double a = fEndMass.get(i)[k].abs();
                amplitude.add(a);
            }
            amplitudeVector.add(amplitude);
        }
       //System.out.println(amplitudeVector.get(20));

        // получение фаз
        Vector<Vector<Double>> phaseVector = new Vector<Vector<Double>>();
        for (int i = 0; i < fEndMass.size(); i++){
            Vector<Double> phase = new Vector<Double>();
            for (int k = 0; k < fEndMass.get(i).length; k++) {
                Double a = fEndMass.get(i)[k].phase();
                phase.add(a);
            }
            phaseVector.add(phase);
        }

        //получение разниц раз
        Vector<Vector<Double>> differencePhaseVector = new Vector<Vector<Double>>();
        for (int i = 0; i < phaseVector.size(); i++){
            Vector<Double> differencePhase = new Vector<Double>();
            differencePhase.add(0.0);
            for (int k = 1; k < phaseVector.get(i).size(); k++){
                Double difference = phaseVector.get(i).get(k) - phaseVector.get(i).get(k-1);
                differencePhase.add(difference);
            }
            differencePhaseVector.add(differencePhase);
        }

        // получение номера сегмента с которого нужно начинать запись
        Integer startSegment = nomerSegmenta(phaseVector);
        //кодирование информации получение новых фаз



        //обратное преобразование из амплитуд и фаз в массив комплексные числа

        // обратное преобразование Фурье





        //Complex[] iftMass = FFT.ifft(fftMass);

        Vector<Double> temp = phaseVector.get(40);
        for (int i = 0; i < temp.size(); ++i) {
            if (temp.get(i) == 0) {
                System.out.println(i + ") " + temp.get(i));
            }
        }


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
        for (int i = 0; i < N; i ++){
            Complex[] massSegment = new Complex[K];
            for ( int k = 0; k < K; k ++){
                massSegment[k] = new Complex(k, 0);
                massSegment[k] = new Complex (music.get(i*K+k), 0);
            }
            mass.add(massSegment);
        }
        /*for (int i = 0; i < mass.get(0).length; ++i) {
            System.out.println(mass.get(0)[i]);
        }*/
        return mass;
    }

    public Integer nomerSegmenta (Vector<Vector<Double>> phaseVector) {
        int i = 0;
        while(isZeroInVector(phaseVector.get(i))) {
            ++i;
        }
        System.out.println(i);
        return i;
    }

    public boolean isZeroInVector(Vector<Double> vector) {
        for (Double el: vector) {
            if (el == 0)
                return true;
        }
        return false;
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
