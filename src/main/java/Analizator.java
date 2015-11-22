import java.util.Vector;

/**
 * Created by elena on 09.11.15.
 */
public class Analizator {

    private Vector<Integer> endVector;
    private Integer startSegment;

    public Vector<Integer> getEndVector() {
        return endVector;
    }

    public Integer getStartSegment() {
        return startSegment;
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
        endVector = new Vector<Integer>();

        Vector<Complex[]> endMass = razbienie(music, text);
//        printMas(endMass, 43);

        Vector<Complex[]> fEndMass = creationFFTorIFFT(endMass, true);
//        printMas(fEndMass, 43);

        // получение амплитуд
        Vector<Vector<Double>> amplitudesVector = creationAmplitudes(fEndMass);
        //System.out.println(amplitudeVector.get(20));

        // получение фаз
        Vector<Vector<Double>> phaseVector = creationPhase(fEndMass);
//        printVector(phaseVector, 3);

        //получение разниц раз
        Vector<Vector<Double>> differencePhaseVector = creationDifferencePhaseVector(phaseVector);
//        printVector(differencePhaseVector, 3);

        // получение номера сегмента с которого нужно начинать запись
        startSegment = nomerSegmenta(phaseVector);

        //кодирование информации получение новых фаз
        Vector<Vector<Double>> conversionPhase = conversionNewPhase(phaseVector, differencePhaseVector, textMass);
//        printVector(conversionPhase, 3);

        //обратное преобразование из амплитуд и фаз в массив комплексные числа
        Vector<Complex[]> endComplexVector = endComplexVector(amplitudesVector, conversionPhase);
//        printMas(endComplexVector, 43);

        // обратное преобразование Фурье
        Vector<Complex[]> ifftMass = creationFFTorIFFT(endComplexVector, false);
//        printMas(endMass, 43);

        //получение итогового массива
        Vector <Complex> end = creationEnd(ifftMass);

    }

    //разбиение на сегменты массива аудио
    public Vector<Complex[]> razbienie(Vector<Integer> music, Vector<Integer> text) {
        int sizeText = text.size();
        int sizeMusic = music.size();
        int sizeBitText = sizeText * 8;

        Double constant = 3.222;
        int v = (int) Math.ceil((Math.log10((double) sizeBitText) * constant + 1));
        int K = (int) Math.pow(2.0, (double) v + 1);
        int N = (int) Math.ceil(sizeMusic / K); // количество сегментов
        double Nn = sizeMusic / K;

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
        while (isZeroInVector(phaseVector.get(i))) {
            ++i;
        }
//        System.out.println(i);
        return i;
    }

    public boolean isZeroInVector(Vector<Double> vector) {
        for (Double el : vector) {
            if (el == 0)
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

        for (int i = 0; i < startSegment; ++i){
            Vector<Double> newPhaseOneSegment = new Vector<Double>();
            for ( int k = 0; k < phaseVector.get(i).size(); ++k){
                newPhaseOneSegment.add(phaseVector.get(i).get(k));
            }
            newPhase.add(newPhaseOneSegment);
        }

        for (int i = startSegment; i < phaseVector.size(); ++i) {
            Vector<Double> newPhaseOneSegment = new Vector<Double>();
            for (int k = 0; k < phaseVector.get(i).size(); ++k) {
                if (counter < textMass.size()) {
                    if (k == 0 || k == (phaseVector.get(i).size() - 1)) {
                        newPhaseOneSegment.add(phaseVector.get(i).get(k));
                    }
                    if (k > 0 && k < (phaseVector.get(i).size() - 1)) {
                        if (textMass.get(counter)) {
                            newPhaseOneSegment.add(Math.PI / 2);
                        } else {
                            newPhaseOneSegment.add(Math.PI / (-2));
                        }
                        counter++;
                    }
                } else {
                    newPhaseOneSegment.add(phaseVector.get(i).get(k));
                }
            }
            newPhase.add(newPhaseOneSegment);
        }
//        printVector(newPhase, 3);

        Vector<Vector<Double>> newEndPhase = new Vector<Vector<Double>>();
        for (int i = 0; i < newPhase.size(); ++i) {
            Vector<Double> newEndPhaseOneSegment = new Vector<Double>();
            newEndPhaseOneSegment.add(newPhase.get(i).get(0));
            for (int k = 1; k < newPhase.get(i).size(); ++k) {
                newEndPhaseOneSegment.add(newPhase.get(i).get(k - 1) + differencePhaseVector.get(i).get(k));
            }
            newEndPhase.add(newEndPhaseOneSegment);
        }
        return newEndPhase;
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

    public Vector<Vector<Double>> creationDifferencePhaseVector (Vector<Vector<Double>> phaseVector){
        Vector<Vector<Double>> differencePhaseVector = new Vector<Vector<Double>>();
        for (int i = 0; i < phaseVector.size(); i++) {
            Vector<Double> differencePhase = new Vector<Double>();
            differencePhase.add(0.0);
            for (int k = 1; k < phaseVector.get(i).size(); k++) {
                Double difference = phaseVector.get(i).get(k) - phaseVector.get(i).get(k - 1);
                differencePhase.add(difference);
            }
            differencePhaseVector.add(differencePhase);
        }
        return differencePhaseVector;
    }

    public Vector<Complex[]> creationFFTorIFFT (Vector<Complex[]> mass, Boolean bool) {
        Vector<Complex[]> fEndMass = new Vector<Complex[]>();
        for (int i = 0; i < mass.size(); i++) {
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

    public Vector<Complex> creationEnd (Vector<Complex[]> ifftMass){
        Vector <Complex>  end = new Vector<Complex>();
        for ( int i = 0; i < ifftMass.size(); ++i){
            for (int k = 0; k < ifftMass.get(i).length; ++k){
                end.add(ifftMass.get(i)[k]);
            }
        }
        return end;
    }



}
