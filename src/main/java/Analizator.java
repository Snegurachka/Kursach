import java.util.Vector;

/**
 * Created by elena on 09.11.15.
 */
public class Analizator {

    public Vector<Integer> analize(Vector<Integer> music, Vector<Integer> text) {
        Vector<Boolean> textMass = new Vector<Boolean>();
        textMass = textConversion(text);

        Vector<Integer> endVector = new Vector<Integer>();

        Vector<Complex[]> endMass;
        endMass = razbienie(music, text);
        /*for(int i = 0; i < endMass.get(43).length; ++i){
            System.out.print(endMass.get(43)[i] + ", ");
        }
        System.out.println();*/

        Vector<Complex[]> fEndMass = new Vector<Complex[]>();
        for (int i = 0; i < endMass.size(); i++) {
            Complex[] fftMass = FFT.fft(endMass.get(i));
            fEndMass.add(fftMass);
        }
        /*for (int i = 0; i < fEndMass.get(0).length; ++i) {
            System.out.print(fEndMass.get(43)[i] + ", ");
        }
        System.out.println();*/

        // получение амплитуд
        Vector<Vector<Double>> amplitudeVector = new Vector<Vector<Double>>();
        for (int i = 0; i < fEndMass.size(); i++) {
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
        for (int i = 0; i < fEndMass.size(); i++) {
            Vector<Double> phase = new Vector<Double>();
            for (int k = 0; k < fEndMass.get(i).length; k++) {
                Double a = fEndMass.get(i)[k].phase();
                phase.add(a);
            }
            phaseVector.add(phase);
        }

       /* for (int i = 0; i < phaseVector.get(43).size(); ++i) {
            System.out.print(phaseVector.get(3).get(i) + ", ");
        }
        System.out.println();*/

        //получение разниц раз
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
        /*for (int i = 0; i < differencePhaseVector.get(43).size(); ++i) {
            System.out.print(differencePhaseVector.get(3).get(i) + ", ");
        }
        System.out.println();*/

        // получение номера сегмента с которого нужно начинать запись
        Integer startSegment = nomerSegmenta(phaseVector);
        /*Vector<Double> temp = phaseVector.get(40);
        for (int i = 0; i < temp.size(); ++i) {
            if (temp.get(i) == 0) {
                System.out.println(i + ") " + temp.get(i));
            }
        }*/

        //кодирование информации получение новых фаз
        Vector<Vector<Double>> conversionPhase = conversionNewPhase(phaseVector, differencePhaseVector, textMass, startSegment);
       /* for (int i = 0; i < conversionPhase.get(43).size(); ++i) {
            System.out.print(conversionPhase.get(3).get(i) + ", ");
        }*/

        //обратное преобразование из амплитуд и фаз в массив комплексные числа
        Vector<Complex[]> endComplexVector = endComplexVector(amplitudeVector, conversionPhase);
       /* for (int i = 0; i < endComplexVector.get(43).length; ++i) {
            System.out.print(endComplexVector.get(43)[i] + ", ");
        }
        System.out.println();*/

        // обратное преобразование Фурье
        Vector<Complex[]> ifftMass = new Vector<Complex[]>();
        for (int i = 0; i < endComplexVector.size(); i++) {
            Complex[] ifftComplex = FFT.fft(endMass.get(i));
            ifftMass.add(ifftComplex);
        }
       /* for(int i = 0; i < endMass.get(43).length; ++i){
            System.out.print(endMass.get(43)[i] + ", ");
        }
        System.out.println();*/


        return endVector;
    }

    //разбиение на сегменты массива аудио
    public Vector<Complex[]> razbienie(Vector<Integer> music, Vector<Integer> text) {
        int sizeText = text.size();
        int sizeMusic = music.size();
        int sizeBitText = sizeText * 8;

        int v = (int) Math.ceil((Math.log10((double) sizeBitText) * 3.322 + 1));
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
        /*for (int i = 0; i < mass.get(0).length; ++i) {
            System.out.println(mass.get(0)[i]);
        }*/
        return mass;
    }

    public Integer nomerSegmenta(Vector<Vector<Double>> phaseVector) {
        int i = 0;
        while (isZeroInVector(phaseVector.get(i))) {
            ++i;
        }
        System.out.println(i);
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
        Complex complex = new Complex(amplitude * Math.cos(phase), amplitude * Math.sin(phase));
        return complex;
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

    public Vector<Vector<Double>> conversionNewPhase(Vector<Vector<Double>> phaseVector, Vector<Vector<Double>> differencePhaseVector, Vector<Boolean> textMass, Integer startSegment) {
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
                        if (textMass.get(counter) == true) {
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

       /* for (int i = 0; i < newPhase.get(43).size(); ++i){
            System.out.print(newPhase.get(3).get(i) + ", ");
        }
        System.out.println();*/

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

}
