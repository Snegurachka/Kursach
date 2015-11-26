import java.util.Vector;

/**
 * Created by elena on 09.11.15.
 */
public class Analizator {

    private Vector<Integer> endVector;
    private Integer startSegment;
    private Vector<Integer> text;

    public Vector<Integer> getEndVector() {
        return endVector;
    }

    public Vector<Integer> getText() {
        return text;
    }

    private void printMas(Vector<Complex[]> endMass, int num) {
        for(int i = 0; i < endMass.get(num).length; ++i){
            System.out.print(endMass.get(num)[i] + ", ");
        }
        System.out.println();
    }

    private void printVector(Vector<Vector<Double>> phaseVector, int num) {
        for (int i = 0; i < phaseVector.get(num).size(); ++i) {
            System.out.print(phaseVector.get(num).get(i) + ", ");
        }
        System.out.println();
    }

    public void analize(Vector<Integer> music, Vector<Integer> text) {

        Vector<Boolean> textMass = textConversion(text);

        int sizeBitText = text.size() * 8;
        Double constant = 3.222;
        int v = (int) Math.ceil((Math.log10((double) sizeBitText) * constant + 1));
        int K = (int) Math.pow(2.0, (double) v + 1);

        Vector<Complex[]> endMass = razbienie(music, K);
//        printMas(endMass, 3);

        Vector<Complex[]> fEndMass = creationFFTorIFFT(endMass, true, 0, endMass.size());
//        printMas(fEndMass, 3);

        // получение амплитуд
        Vector<Vector<Double>> amplitudesVector = creationAmplitudes(fEndMass);
//        System.out.println(amplitudesVector.get(3));

        // получение фаз
        Vector<Vector<Double>> phaseVector = creationPhase(fEndMass);
//        printVector(phaseVector, 3);
//
        //получение разниц раз
        Vector<Vector<Double>> differencePhaseVector = creationDifferencePhaseVector(phaseVector);
//        printVector(differencePhaseVector, 0);

        // получение номера сегмента с которого нужно начинать запись
       // startSegment = nomerSegmenta(phaseVector);

        //кодирование информации получение новых фаз
        Vector<Vector<Double>> conversionPhase = conversionNewPhase(phaseVector, differencePhaseVector, textMass);
//        printVector(conversionPhase, 3);

        //обратное преобразование из амплитуд и фаз в массив комплексные числа
        Vector<Complex[]> endComplexVector = endComplexVector(amplitudesVector, conversionPhase);
//        printMas(endComplexVector, 3);

        // обратное преобразование Фурье
        Vector<Complex[]> ifftMass = creationFFTorIFFT(endComplexVector, false, 0, endComplexVector.size());
//        printMas(ifftMass, 3);

        //получение итогового массива
        endVector = creationEnd(ifftMass);
    }

    public void backAnalize (Vector<Integer> music, Integer N){

        text = new Vector<Integer>();
//        разбиение на сегменты
        int sizeBitText = N * 8;
        Double constant = 3.222;
        int v = (int) Math.ceil((Math.log10((double) sizeBitText) * constant + 1));
        int K = (int) Math.pow(2.0, (double) v + 1);
        Vector<Complex[]> endMass2 = razbienie(music, K);
//        printMas(endMass2, 3);

//        БПФ
        Vector<Complex[]> fEndMass2 = creationFFTorIFFT(endMass2, true, 0, endMass2.size());
//      printMas(fEndMass2, 3);

        // получение фаз
        Vector<Vector<Double>> phaseVector2 = creationPhase(fEndMass2);
//        printVector(phaseVector2, 3);

        //получение кодированного сообщения
        text = poluchenieTexta (phaseVector2, sizeBitText);
    }

    //разбиение на сегменты массива аудио
    public Vector<Complex[]> razbienie(Vector<Integer> music, Integer K) {

        int N = (int) Math.ceil(music.size() / K); // количество сегментов
        double Nn = music.size() / K;

        Vector<Complex[]> mass = new Vector<Complex[]>();
        for (int i = 0; i < N; i++) {
            Complex[] massSegment = new Complex[K];
            for (int k = 0; k < K; k++) {
                massSegment[k] = new Complex(k, 0);
                massSegment[k] = new Complex(music.get(i * K + k), 0);
            }
            mass.add(massSegment);
        }
//        printMas(mass, 0);
        return mass;
    }

    public Integer nomerSegmenta(Vector<Vector<Double>> phaseVector) {
        int i = 0;
        while (!isZeroInVector(phaseVector.get(i))) {
            ++i;
        }
//        System.out.println(i);
        return i;
    }

    public boolean isZeroInVector(Vector<Double> vector) {
        for (Double el : vector) {
            if (el != 0 )
                return true;
        }
        return false;
    }

    public Complex complex(Double amplitude, Double phase) {
        return new Complex(amplitude * Math.cos(phase), amplitude * Math.sin(phase));
    }

    public Vector<Complex[]> endComplexVector(Vector<Vector<Double>> amplitudeVector, Vector<Vector<Double>> phase) {
        Vector<Complex[]> endCoplexVector = new Vector<Complex[]>();
        for (int i = 0; i < amplitudeVector.size(); ++i) {
            Complex[] complexVector = new Complex[amplitudeVector.get(i).size()];
            for (int k = 0; k < amplitudeVector.get(i).size(); ++k) {
                complexVector[k] = complex(amplitudeVector.get(i).get(k), phase.get(i).get(k));
            }
            endCoplexVector.add(complexVector);
        }
        return endCoplexVector;
    }

    public Vector<Boolean> textConversion(Vector<Integer> text) {
        Vector<Boolean> bool = new Vector<Boolean>();

        for (int i = 0; i < text.size(); ++i) {
            int a = text.get(i);
            for (int k = 7; k >= 0; --k) {
                if ((a & (1 << k)) > 0) {
                    bool.add(true);
                } else {
                    bool.add(false);
                }
            }
        }
        return bool;
    }

    public Vector<Vector<Double>> conversionNewPhase(
            Vector<Vector<Double>> phaseVector,
            Vector<Vector<Double>> differencePhaseVector,
            Vector<Boolean> textMass) {

        Vector<Vector<Double>> newPhase = new Vector<Vector<Double>>();
        int counter = 0;

        for (int i = 0; i < 4; ++i){
            Vector<Double> newPhaseOneSegment = new Vector<Double>();
            for (int k = 0; k < phaseVector.get(0).size(); ++k){
                newPhaseOneSegment.add(phaseVector.get(i).get(k));
            }
            newPhase.add(newPhaseOneSegment);
        }
        Vector<Double> newPhaseOneSegment = new Vector<Double>();
        for (int i = 0; i < phaseVector.get(3).size(); ++i) {

            if (counter < textMass.size()) {
                if (i > 0 && i < (phaseVector.get(3).size() - 1)) {
                    if (textMass.get(counter)) {
                        newPhase.get(3).set((phaseVector.get(3).size() - 1) - i, Math.PI / (-2));
                    } else {
                        newPhase.get(3).set((phaseVector.get(3).size() - 1) - i, Math.PI / 2);
                    }
                    counter++;
                }
            }
        }
        for (int i = 4; i < phaseVector.size(); ++i){
            Vector<Double> phasePhase = new Vector<Double>();
            for (int k = 0; k < phaseVector.get(i).size(); ++k){
                phasePhase.add(newPhase.get(i - 1).get(k) + differencePhaseVector.get(i).get(k));
            }
            newPhase.add(phasePhase);
        }

        return newPhase;
    }

    public Vector<Vector<Double>> creationAmplitudes (Vector<Complex[]> fEndMass){
        Vector<Vector<Double>> amplitudeVector = new Vector<Vector<Double>>();
        for (int i = 0; i < fEndMass.size(); i++) {
            Vector<Double> amplitude = new Vector<Double>();
            for (int k = 0; k < fEndMass.get(i).length; k++) {
                Double a = fEndMass.get(i)[k].abs();
                amplitude.add(a);
            }
            amplitudeVector.add(amplitude);
        }
        return amplitudeVector;
    }

    public Vector<Vector<Double>> creationPhase (Vector<Complex[]> fEndMass) {
        Vector<Vector<Double>> phaseVector = new Vector<Vector<Double>>();
        for (int i = 0; i < fEndMass.size(); i++) {
            Vector<Double> phase = new Vector<Double>();
            for (int k = 0; k < fEndMass.get(i).length; k++) {
                Double a = fEndMass.get(i)[k].phase();
                phase.add(a);
            }
            phaseVector.add(phase);
        }
        return phaseVector;
    }
//  получение разниц фаз
    public Vector<Vector<Double>> creationDifferencePhaseVector (Vector<Vector<Double>> phaseVector){
        Vector<Vector<Double>> differencePhaseVector = new Vector<Vector<Double>>();
        for (int i = 0; i < phaseVector.size(); i++) {
            Vector<Double> differencePhase = new Vector<Double>();
            for (int k = 0; k < phaseVector.get(i).size(); k++) {
                if ((i == 0 || i == 1 || i == 2 || i == 3 )){
                    differencePhase.add(0.0);
                } else {
                    Double difference = phaseVector.get(i).get(k) - phaseVector.get(i - 1).get(k);
                    differencePhase.add(difference);
                }
            }
            differencePhaseVector.add(differencePhase);
        }
        return differencePhaseVector;
    }

    public Vector<Complex[]> creationFFTorIFFT (Vector<Complex[]> mass, Boolean bool, int startSegment, int endSegment) {
        Vector<Complex[]> fEndMass = new Vector<Complex[]>();
        for (int i = startSegment; i < endSegment; i++) {
            if (bool){
                Complex[] fftMass = FFT.fft(mass.get(i));
                fEndMass.add(fftMass);
            } else {
                Complex[] fftMass = FFT.ifft(mass.get(i));
                fEndMass.add(fftMass);
            }
        }
        return fEndMass;
    }

    public Vector<Integer> creationEnd (Vector<Complex[]> ifftMass){
        Vector <Integer>  end = new Vector<Integer>();
        for ( int i = 0; i < ifftMass.size(); ++i){
            for (int k = 0; k < ifftMass.get(i).length; ++k){
                end.add((int) ifftMass.get(i)[k].re());
            }
        }
        return end;
    }

    public Vector<Integer> poluchenieTexta (Vector<Vector<Double>> phaseVector, int sizeText){
        Vector <Integer> text = new Vector<Integer>();
        for (int i = 0; i < phaseVector.get(3).size(); i++) {
                if (i == 0){
                    i ++;
                    continue;
                }
                if (phaseVector.get(3).get(phaseVector.get(3).size() - i) > 0) {
                    text.add(0);
                } else {
                    text.add(1);
                }
        }
        Vector <Integer> textEnd = new Vector<Integer>();
        for (int i = 0; i < sizeText; ++i){
            textEnd.add(text.get(i));
        }
        return textEnd;
    }

}
