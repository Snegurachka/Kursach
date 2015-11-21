import java.util.Vector;

/**
 * Created by elena on 09.11.15.
 */
public class Analizator {

    // TODO эти переменные называются атрибутами объекта. Доступ к ним есть у всех методов объекта.
    private Vector<Integer> endVector;
    private Integer startSegment;

    // TODO методы доступа к атрибутам объекта
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
        // TODO Нет смысла тут писать new Vector<Boolean>: Ты следующей строчкой перезаписываешь значение textMass.
        // TODO так что лучше просто
        // TODO Vector<Boolean> textMass = textConversion(text)
//        Vector<Boolean> textMass = new Vector<Boolean>();
//        textMass = textConversion(text);
        Vector<Boolean> textMass = textConversion(text);

        endVector = new Vector<Integer>();

        Vector<Complex[]> endMass;
        endMass = razbienie(music, text);
        // TODO одинаковые операции по выводу типа этих лучше выделить в отдельный метод,
        // TODO который тут ты будешь вызывать
        // TODO тогда даже с учетом того что это будет закомментировано будет выглядеть читабельней
//        printMas(endMass, 43);


        Vector<Complex[]> fEndMass = new Vector<Complex[]>();
        for (int i = 0; i < endMass.size(); i++) {
            Complex[] fftMass = FFT.fft(endMass.get(i));
            fEndMass.add(fftMass);
        }
//        printMas(fEndMass, 43);
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
//        printVector(phaseVector, 3);

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
//        printVector(differencePhaseVector, 3);

        // получение номера сегмента с которого нужно начинать запись
        startSegment = nomerSegmenta(phaseVector);

        //кодирование информации получение новых фаз
        Vector<Vector<Double>> conversionPhase = conversionNewPhase(phaseVector, differencePhaseVector, textMass);
//        printVector(conversionPhase, 3);

        //обратное преобразование из амплитуд и фаз в массив комплексные числа
        Vector<Complex[]> endComplexVector = endComplexVector(amplitudeVector, conversionPhase);
//        printMas(endComplexVector, 43);

        // обратное преобразование Фурье
        Vector<Complex[]> ifftMass = new Vector<Complex[]>();
        for (int i = 0; i < endComplexVector.size(); i++) {
            Complex[] ifftComplex = FFT.fft(endMass.get(i));
            ifftMass.add(ifftComplex);
        }
//        printMas(endMass, 43);
    }

    //разбиение на сегменты массива аудио
    public Vector<Complex[]> razbienie(Vector<Integer> music, Vector<Integer> text) {
        int sizeText = text.size();
        int sizeMusic = music.size();
        int sizeBitText = sizeText * 8;

        // TODO старайся избегать магических констант
        // TODO см раздел плохая практика программирования
        // TODO https://ru.wikipedia.org/wiki/%D0%9C%D0%B0%D0%B3%D0%B8%D1%87%D0%B5%D1%81%D0%BA%D0%BE%D0%B5_%D1%87%D0%B8%D1%81%D0%BB%D0%BE_(%D0%BF%D1%80%D0%BE%D0%B3%D1%80%D0%B0%D0%BC%D0%BC%D0%B8%D1%80%D0%BE%D0%B2%D0%B0%D0%BD%D0%B8%D0%B5)
        // TODO я это к тому что хрен поймешь, что это за число 3.322
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
//        printMas(mass, 0);
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

    // TODO лучше все же переноси в несколько строк. Чтоб можно было прочитать на одном экране
    // TODO заметь теперь у тебя в передваемых параметрах startSegment
    // TODO потому что доступ к атрибуту объекта есть у всех методов
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
}
