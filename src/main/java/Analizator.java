import java.util.ArrayList;
import java.util.List;

/**
 * Created by elena on 09.11.15.
 */
public class Analizator {

    private List<Long> endList;
    private List<Long> startList = new ArrayList<Long>();
    private Integer startSegment;
    private List<Integer> text;
    private Integer textSize;
    private Integer segmentOneSize;
    private Integer nomerElementa;

    public List<Long> getStartList() {
        return startList;
    }
    public Integer getTextSize() { return textSize;}
    public List<Long> getEndList() {
        return endList;
    }
    public List<Integer> getText() {
        return text;
    }
    public Integer getStartSegment() { return startSegment;}
    public Integer getNomerElementa() { return nomerElementa; }

    private void printMas(List<Complex[]> endMass, int num) {
        for(int i = 0; i < endMass.get(num).length; ++i){
            System.out.print(endMass.get(num)[i] + ", ");
        }
        System.out.println();
    }

    private void printList(List<List<Double>> phaseList, int num) {
        for (int i = 0; i < phaseList.get(num).size(); ++i) {
            System.out.print(phaseList.get(num).get(i) + ", ");
        }
        System.out.println();
    }


    public void analize(List<Long> music, List<Integer> text) {

        for (int i = 0; i < music.size(); ++i){
            startList.add(music.get(i));
        }
        System.out.println(startList.size());
        textSize = text.size();
//   представление массива текста в виде 0 и 1
        List<Boolean> textMass = textConversion(text);


        List<Long> musikNot0 = newMass0(music);
        System.out.println(musikNot0.size());
        List<Complex[]> endMass = razbienie(musikNot0, textSize);
//        printMas(endMass, 0);

//        разбиение на сегменты

//        List<Complex[]> endMass = razbienie(music, textSize);
//        printMas(endMass, 0);

//        получение номера сегмента с которого нужно начинать запись
//        startSegment = nomerSegmenta(endMass);
        startSegment = 0;
        System.out.println(startSegment);

        List<Complex[]> fEndMass = creationFFTorIFFT(endMass, true, 0, endMass.size());
//        printMas(fEndMass, 30);

//        обратное преобразование Фурье
        List<Complex[]> ifftMass2 = creationFFTorIFFT(fEndMass, false, 0, fEndMass.size());
//        printMas(ifftMass2, 3);

        // получение амплитуд
        List<List<Double>> amplitudesList = creationAmplitudes(fEndMass);
//        System.out.print(amplitudesList.get(291));

//        получение фаз
        List<List<Double>> phaseList = creationPhase(fEndMass);
//        printList(phaseList, 12);

//        получение разниц
        List<List<Double>> differencePhaseList = creationDifferencePhaseList(phaseList, startSegment);
//        printList(differencePhaseList, 3);

//        кодирование информации получение новых фаз
        List<List<Double>> conversionPhase = conversionNewPhase(phaseList, differencePhaseList, textMass, startSegment);
//        printList(conversionPhase, 2);

        List<List<Double>> ph = new ArrayList<List<Double>>();
        for ( int i = 0; i < conversionPhase.size(); ++i){
            List<Double> onePh = new ArrayList<Double>();
            for ( int k = 0; k < conversionPhase.get(i).size(); ++k) {
                if (i <= startSegment) {
                    onePh.add(conversionPhase.get(i).get(k));
                }
                if (i > startSegment) {
                    onePh.add(conversionPhase.get(i - 1).get(k) + differencePhaseList.get(i).get(k));
                }
            }
            ph.add(onePh);
        }
//        printList(ph, 2);
        //обратное преобразование из амплитуд и фаз в массив комплексные числа
        List<Complex[]> endComplexList = endComplexList(amplitudesList, ph);
//        printMas(endComplexList, 237);

        // обратное преобразование Фурье
        List<Complex[]> ifftMass = creationFFTorIFFT(endComplexList, false, 0, endComplexList.size());
//        printMas(ifftMass, 237);

//        List<Complex[]> fEndMass2 = creationFFTorIFFT(ifftMass, true, 0, ifftMass.size());
////      printMas(fEndMass2, 237);
//
//        // получение фаз
//        List<List<Double>> phaseList2 = creationPhase(fEndMass2);
//        printList(phaseList2, 2);

        //получение итогового массива
        endList = creationEnd(ifftMass);
    }



    public void backAnalize (List<Long> music, Integer N, Integer nomer){

//        text = new ArrayList<Integer>();
//        разбиение на сегменты
        int sizeBitText = N * 8;

        List<Complex[]> endMass2 = razbienie(music, N);


//        printMas(endMass2, 237);

//        БПФ
        List<Complex[]> fEndMass2 = creationFFTorIFFT(endMass2, true, 0, endMass2.size());
//      printMas(fEndMass2, 237);

//        получение фаз
        List<List<Double>> phaseList2 = creationPhase(fEndMass2);
//        printList(phaseList2, 30);

//        получение кодированного сообщения
        text = poluchenieTexta (phaseList2, sizeBitText, nomer);
//        System.out.print(text);
    }








//    разбиение на сегменты массива аудио
    public List<Complex[]> razbienie(List<Long> music, Integer Nnn) {

        int sizeBitText = Nnn * 8;
        Double constant = 3.222;
        int v = (int) Math.ceil((Math.log10((double) sizeBitText) * constant + 1));
        int K = (int) Math.pow(2.0, (double) v + 1);
        int N = (int) Math.ceil(music.size() / (double) K); // количество сегментов
        System.out.println(N);
        double Nn = music.size() / (double) K;
        segmentOneSize = music.size() / N;
//        System.out.println(segmentOneSize);
//        System.out.println(music.size());

        List<Double> music1 = new ArrayList<Double>();
        for ( int i = 0; i < N * K; i++){
            if (i < music.size() - 1){
                music1.add((double) music.get(i));
            } else {
                music1.add(0.0);
            }
        }
//        System.out.println();
//        System.out.println(music1.size());

        List<Complex[]> mass = new ArrayList<Complex[]>();
        for (int i = 0; i < N; i++) {
            Complex[] massSegment = new Complex[K];
            for (int k = 0; k < K; k++) {
                massSegment[k] = new Complex(k, 0);
                massSegment[k] = new Complex(music1.get(i * K + k), 0);
            }
            mass.add(massSegment);
        }
//        printMas(mass, 0);
        return mass;
    }

//    получние массива без 0
    public List<Long> newMass0 (List<Long> musicM){
        List<Long> newMas = new ArrayList<Long>();
        int count = 0;
        for (int i = 0; i < musicM.size(); i++){
            if (musicM.get(i) == 0.0){
                count = 0;
            } else {
                count++;
                if (count >= 7) {
                    nomerElementa = i;
                    break;
                }
            }
        }
        for (int i = (nomerElementa - 6); i < musicM.size(); i++){
            newMas.add(musicM.get(i));
        }
        return newMas;
    }

//    получение номера сегмента
    public Integer nomerSegmenta(List<Complex[]> phaseList) {
        int i = 0;
        while (!isCorrect(phaseList.get(i))) {
            ++i;
        }
//        System.out.println(i);
        return i;
    }
    public boolean isCorrect(Complex[] List) {
        int count = 0;
        for (Complex el : List) {
            if (el.re() == 0.0) {
                count ++;
                if (count > (segmentOneSize / 2)){
                    return false;
                }
            }
        }
        return true;
    }

//    public Integer nomerSegmenta1(List<List<Double>> phaseList) {
//        int i = 0;
//        while (!isCorrect1(phaseList.get(i))) {
//            ++i;
//        }
////        System.out.println(i);
//        return i;
//    }
//
//    public boolean isCorrect1(List<Double> List) {
//        int count = 0;
//        for (Double el : List) {
//            if (el == 0.0) {
//                count ++;
//                if (count > 10){
//                    return false;
//                }
//            }
//        }
//        return true;
//    }


    public List<Complex[]> endComplexList(List<List<Double>> amplitudeList, List<List<Double>> phase) {
        List<Complex[]> endCoplexList = new ArrayList<Complex[]>();
        for (int i = 0; i < amplitudeList.size(); ++i) {
            Complex[] complexList = new Complex[amplitudeList.get(i).size()];
            for (int k = 0; k < amplitudeList.get(i).size(); ++k) {
                complexList[k] = complex(amplitudeList.get(i).get(k), phase.get(i).get(k));
            }
            endCoplexList.add(complexList);
        }
        return endCoplexList;
    }

    //    преобразование из амплитуды и фазы в комплексное число
    public Complex complex(Double amplitude, Double phase) {
        return new Complex(amplitude * Math.cos(phase), amplitude * Math.sin(phase));
    }

//    представление массива текста в виде 0 и 1
    public List<Boolean> textConversion(List<Integer> text) {
        List<Boolean> bool = new ArrayList<Boolean>();

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

    public List<List<Double>> conversionNewPhase(
            List<List<Double>> phaseList,
            List<List<Double>> differencePhaseList,
            List<Boolean> textMass,
            Integer nomer) {

        List<List<Double>> newPhase = new ArrayList<List<Double>>();
        int counter = 0;

        for (int i = 0; i < phaseList.size(); ++i){
            List<Double> newp = new ArrayList<Double>();
            for ( int k = 0; k < phaseList.get(i).size(); ++k){
                newp.add(phaseList.get(i).get(k));
            }
            newPhase.add(newp);
        }

        for (int i = 0; i < phaseList.get(startSegment).size(); ++i) {
            if (counter < textMass.size()) {
                if (i > 0 && i < (phaseList.get(nomer).size() - 1)) {
                    if (textMass.get(counter)) {
//                        newPhase.get(nomer).set(i, Math.PI / (-2));
//                        newPhase.get(nomer).set((phaseList.get(nomer).size() / 2) + i, Math.PI / (-2));
                        newPhase.get(nomer).set((phaseList.get(nomer).size() - 1) - i, Math.PI / (-2));
                        newPhase.get(nomer).set(i + 1, Math.PI / (2));
                    } else {
//                        newPhase.get(nomer).set(i, Math.PI / 2);
//                        newPhase.get(nomer).set((phaseList.get(nomer).size() / 2) + i, Math.PI / 2);
                        newPhase.get(nomer).set((phaseList.get(nomer).size() - 1) - i, Math.PI / 2);
                        newPhase.get(nomer).set(i + 1, Math.PI / -2);
                    }
                    counter++;
                }
            }
        }


//        for (int i = 0; i < nomer + 1; ++i){
//            List<Double> newPhaseOneSegment = new ArrayList<Double>();
//            for (int k = 0; k < phaseList.get(0).size(); ++k){
//                newPhaseOneSegment.add(phaseList.get(i).get(k));
//            }
//            newPhase.add(newPhaseOneSegment);
//        }
//        List<Double> newPhaseOneSegment = new ArrayList<Double>();
//        for (int i = 0; i < phaseList.get(nomer).size(); ++i) {
//
//            if (counter < textMass.size()) {
//                if (i > 0 && i < (phaseList.get(nomer).size() - 1)) {
//                    if (textMass.get(counter)) {
////                        newPhase.get(nomer).set(i, Math.PI / (-2));
////                        newPhase.get(nomer).set((phaseList.get(nomer).size() / 2) + i, Math.PI / (-2));
//                        newPhase.get(nomer).set((phaseList.get(nomer).size() - 1) - i, Math.PI / (-2));
////                        newPhase.get(nomer).set(i + 1, Math.PI / (2));
//                    } else {
////                        newPhase.get(nomer).set(i, Math.PI / 2);
////                        newPhase.get(nomer).set((phaseList.get(nomer).size() / 2) + i, Math.PI / 2);
//                        newPhase.get(nomer).set((phaseList.get(nomer).size() - 1) - i, Math.PI / 2);
////                        newPhase.get(nomer).set(i + 1, Math.PI / -2);
//                    }
//                    counter++;
//                }
//            }
//        }
//        for (int i = nomer + 1; i < phaseList.size(); ++i){
//            List<Double> phasePhase = new ArrayList<Double>();
//            for (int k = 0; k < phaseList.get(i).size(); ++k){
//                phasePhase.add(newPhase.get(i - 1).get(k) + differencePhaseList.get(i).get(k));
//            }
//            newPhase.add(phasePhase);
//        }
        return newPhase;
    }

    public List<List<Double>> creationAmplitudes (List<Complex[]> fEndMass){
        List<List<Double>> amplitudeList = new ArrayList<List<Double>>();
        for (int i = 0; i < fEndMass.size(); i++) {
            List<Double> amplitude = new ArrayList<Double>();
            for (int k = 0; k < fEndMass.get(i).length; k++) {
                Double a = fEndMass.get(i)[k].abs();
                amplitude.add(a);
            }
            amplitudeList.add(amplitude);
        }
        return amplitudeList;
    }

    public List<List<Double>> creationPhase (List<Complex[]> fEndMass) {
        List<List<Double>> phaseList = new ArrayList<List<Double>>();
        for (int i = 0; i < fEndMass.size(); i++) {
            List<Double> phase = new ArrayList<Double>();
            for (int k = 0; k < fEndMass.get(i).length; k++) {
                Double a = fEndMass.get(i)[k].phase();
                phase.add(a);
            }
            phaseList.add(phase);
        }
        return phaseList;
    }

//  получение разниц фаз
    public List<List<Double>> creationDifferencePhaseList (List<List<Double>> phaseList, Integer nomer){
        List<List<Double>> differencePhaseList = new ArrayList<List<Double>>();
        for (int i = 0; i < phaseList.size(); i++) {
            List<Double> differencePhase = new ArrayList<Double>();
            for (int k = 0; k < phaseList.get(i).size(); k++) {
                if ((i <= nomer )){
                    differencePhase.add(0.0);
                } else {
                    Double difference = phaseList.get(i).get(k) - phaseList.get(i - 1).get(k);
                    differencePhase.add(difference);
                }
            }
            differencePhaseList.add(differencePhase);
        }
        return differencePhaseList;
    }

    public List<Complex[]> creationFFTorIFFT (List<Complex[]> mass, Boolean bool, int startSegment, int endSegment) {
        List<Complex[]> fEndMass = new ArrayList<Complex[]>();
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

    public List<Long> creationEnd (List<Complex[]> ifftMass){
        List <Long>  end = new ArrayList<Long>();
        for ( int i = 0; i < ifftMass.size(); ++i){
            for (int k = 0; k < ifftMass.get(i).length; ++k){
                end.add((long) ifftMass.get(i)[k].re());
            }
        }
        return end;
    }

    public List<Integer> poluchenieTexta (List<List<Double>> phaseList, int sizeText, Integer nomer){

        List <Integer> text = new ArrayList<Integer>();
        for (int i = 0; i < phaseList.get(nomer).size(); i++) {
                if (i == 0){
                    i++;
                    continue;
                }
                if (phaseList.get(nomer).get(phaseList.get(nomer).size() - i) > 0) {
                    text.add(0);
                } else {
                    text.add(1);
                }
        }
        List <Integer> textEnd = new ArrayList<Integer>();
        for (int i = 0; i < sizeText; ++i) {
            textEnd.add(text.get(i));
        }

        List<List<Integer>> textOne = new ArrayList<List<Integer>>();
        int n = 8;
        for (int i = 0; i < textEnd.size() / 8; ++i){
            List<Integer> one = new ArrayList<Integer>();
            for (int k = 0; k < n; ++k){
                one.add(textEnd.get(i * n + k));
            }
            textOne.add(one);
        }

        List<Integer> text1 = new ArrayList<Integer>();

        for (int i = 0; i < textOne.size(); ++i){
            int t = 0;
            for (int k = 0; k < textOne.get(i).size(); ++k){
                if ( textOne.get(i).get((textOne.get(i).size() - 1) - k) == 1) {
                    t += Math.pow(2, k);
                }
            }
            text1.add(t);
        }
        return text1;
    }
}
