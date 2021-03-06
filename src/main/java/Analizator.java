import java.util.ArrayList;
import java.util.List;

/**
 * Created by elena on 09.11.15.
 */
public class Analizator {

    private List<Long> endList;
    private List<Long> startList = new ArrayList<Long>();
    private List<Integer> text;
    private Integer textSize;
//    private Integer segmentOneSize;
    private Integer nomerElementa;


    public Integer getTextSize() { return textSize;}
    public List<Long> getEndList() {
        return endList;
    }
    public List<Integer> getText() {
        return text;
    }
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
        System.out.println("Размер музыки: " + music.size());
        for (int i = 0; i < music.size(); ++i) {
            startList.add(music.get(i));
        }
//        System.out.println(startList.size());
        textSize = text.size();

//        Gnuplot.printList("initialVector.gnuplot", startList);
//        System.out.println();
//        System.out.println("начальный текст");
//        System.out.println(text);

//   представление массива текста в виде 0 и 1
        List<Boolean> textMass = textConversion(text);
//        System.out.println("текст");
//        System.out.println(textMass);
        System.out.println("Количество бит изображения:" + textMass.size());

        List<Long> musikNot0 = newMass0(startList);
        Gnuplot.printList("initialVector.gnuplot", musikNot0);
//        разбиение на сегменты
        List<Complex[]> endMass = razbienie(musikNot0, textSize);
        System.out.println("Размер одного сегмента: " + endMass.get(0).length);
//        printMas(endMass, 0);


//      преобразование Фурье
        List<Complex[]> fEndMass = creationFFTorIFFT(endMass, true, 0, endMass.size());
//        printMas(fEndMass, 30);

////        обратное преобразование Фурье
//        List<Complex[]> ifftMass2 = creationFFTorIFFT(fEndMass, false, 0, fEndMass.size());
////        printMas(ifftMass2, 3);

        // получение амплитуд
        List<List<Double>> amplitudesList = creationAmplitudes(fEndMass);
//        System.out.print("Амплитуды: " + amplitudesList.get(0));
//        System.out.println();
        List<Double> amp = new ArrayList<Double>();
        for (int i = 0; i < amplitudesList.get(0).size() / 2; i++)
        {
            amp.add(amplitudesList.get(0).get(i));
        }
        Gnuplot.printList("initialAmplitude.gnuplot", amp);

//        получение фаз
        List<List<Double>> phaseList = creationPhase(fEndMass);
        System.out.println("Фазы начальные: " + phaseList.get(0).get(1));
        System.out.println("Фазы начальные: " + phaseList.get(0).get(2));
//        printList(phaseList, 0);

        List<Double> faazi = new ArrayList<Double>();
        for (int i = 0; i < 11; i++)
        {
            faazi.add(phaseList.get(0).get(i));
        }
        Gnuplot.printList("initialPhase.gnuplot", faazi);


//        получение разниц
        List<List<Double>> differencePhaseList = creationDifferencePhaseList(phaseList);
//        printList(differencePhaseList, 3);

//        кодирование информации получение новых фаз
        List<List<Double>> conversionPhase = conversionNewPhase(phaseList, differencePhaseList, textMass);
//        printList(conversionPhase, 0);

        List<List<Double>> ph = new ArrayList<List<Double>>();
        for (int i = 0; i < conversionPhase.size(); ++i) {
            List<Double> onePh = new ArrayList<Double>();
            for (int k = 0; k < conversionPhase.get(i).size(); ++k) {
                if (i == 0) {
                    onePh.add(conversionPhase.get(i).get(k));
                } else
                    onePh.add(conversionPhase.get(i - 1).get(k) + differencePhaseList.get(i).get(k));
            }
            ph.add(onePh);
        }

        List<Double> endFaazi = new ArrayList<Double>();
        for (int i = 0; i < 11; i++)
        {
            endFaazi.add(ph.get(0).get(i));
        }
        Gnuplot.printList("endPhase.gnuplot", endFaazi);


        List<Double> difPhase = new ArrayList<Double>();
        for (int i = 0; i < endFaazi.size(); i++)
        {
            difPhase.add(endFaazi.get(i) - faazi.get(i));
        }
        Gnuplot.printList("differencePhase.gnuplot", difPhase);



//        System.out.println("Фазы преобразованные" + ph.get(0).get(1));
//        System.out.println("Фазы преобразованные" + ph.get(0).get(2));

//        printList(ph, 0);
        //обратное преобразование из амплитуд и фаз в массив комплексные числа
        List<Complex[]> endComplexList = endComplexList(amplitudesList, ph);
//        List<Complex[]> endComplexList = endComplexList(amplitudesList, phaseList);
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
//        Integer razmer1 = endList.size();
//        Gnuplot.printList("endVector.gnuplot", endList);


        List<Long> differenceVector = new ArrayList<Long>();
        List<Long> endVectorr = new ArrayList<Long>();
        for (int i = 0; i < musikNot0.size(); i++) {
            differenceVector.add(endList.get(i) - musikNot0.get(i));
            endVectorr.add(endList.get(i));
        }
        Gnuplot.printList("differenceVector.gnuplot", differenceVector);
        Gnuplot.printList("endVector.gnuplot", endVectorr);
    }


    public void backAnalize (List<Long> music, Integer N){
//        разбиение на сегменты
        int sizeBitText = N * 8;
//        Gnuplot.printList("endVector.gnuplot", music);

        List<Complex[]> endMass2 = razbienie(music, N);

//        БПФ
        List<Complex[]> fEndMass2 = creationFFTorIFFT(endMass2, true, 0, endMass2.size());
//      printMas(fEndMass2, 237);

        // получение амплитуд
        List<List<Double>> amplitudesList2 = creationAmplitudes(fEndMass2);
//        System.out.print(amplitudesList.get(291));
//        List<Double> amp2 = new ArrayList<Double>();
//        for (int i = 0; i < amplitudesList2.get(0).size() / 2; i++)
//        {
//            amp2.add(amplitudesList2.get(0).get(i));
//        }
//        Gnuplot.printList("endAmplitude.gnuplot", amp2);

//        получение фаз
        List<List<Double>> phaseList2 = creationPhase(fEndMass2);
        System.out.println("Фазы после извлечения" + phaseList2.get(0).get(1));
        System.out.println("Фазы после извлечения" + phaseList2.get(0).get(2));

//        printList(phaseList2, 0);
//        System.out.println("FAZA: " + phaseList2.get(0).get(24983));

//        получение кодированного сообщения
        text = poluchenieTexta (phaseList2, sizeBitText);
//        System.out.print(text);
    }





//    разбиение на сегменты массива аудио
    public List<Complex[]> razbienie(List<Long> music, Integer Nnn) {

        int sizeBitText = Nnn * 8;
        Double constant = 3.222;
        int v = (int) Math.ceil((Math.log10((double) sizeBitText) * constant + 1));
        int K = (int) Math.pow(2.0, (double) v + 1);
        int N = (int) Math.ceil(music.size() / (double) K); // количество сегментов
//        System.out.println(N);
        double Nn = music.size() / (double) K;
//        segmentOneSize = music.size() / N;

        List<Double> music1 = new ArrayList<Double>();
        for ( int i = 0; i < N * K; i++){
            if (i < music.size() - 1){
                music1.add((double) music.get(i));
            } else {
                music1.add(0.0);
            }
        }

        List<Complex[]> mass = new ArrayList<Complex[]>();
        for (int i = 0; i < N; i++) {
            Complex[] massSegment = new Complex[K];
            for (int k = 0; k < K; k++) {
                massSegment[k] = new Complex(k, 0);
                massSegment[k] = new Complex(music1.get(i * K + k), 0);
            }
            mass.add(massSegment);
        }
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
                if (count >= 15) {
                    nomerElementa = i;
                    break;
                }
            }
        }
        for (int i = (nomerElementa - 9); i < musicM.size(); i++){
            newMas.add(musicM.get(i));
        }
        return newMas;
    }

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


//  кодирование информацйии, получение новых фаз
    public List<List<Double>> conversionNewPhase(
            List<List<Double>> phaseList,
            List<List<Double>> differencePhaseList,
            List<Boolean> textMass) {

        List<List<Double>> newPhase = new ArrayList<List<Double>>();
        int counter = 0;

        for (int i = 0; i < phaseList.size(); ++i){
            List<Double> newp = new ArrayList<Double>();
            for ( int k = 0; k < phaseList.get(i).size(); ++k){
                newp.add(phaseList.get(i).get(k));
            }
            newPhase.add(newp);
        }

//        for (int i = 2; i < phaseList.get(0).size(); i++) {
//            if (counter < textMass.size()) {
//                if (i > 0 && i < (phaseList.get(0).size() - 1)) {
//                    if (textMass.get(counter)) {
////                        if (phaseList.get(0).get(i) < 0){
//////                        if ( phaseList.get(0).get((phaseList.get(0).size() - 1) - i) > 0 ){
////
//////                            newPhase.get(0).set(i, (phaseList.get(0).get(i) + Math.PI) );
//////                            newPhase.get(0).set(phaseList.get(0).size() - i, -(phaseList.get(0).get(i) + Math.PI));
////
//////                            newPhase.get(0).set((phaseList.get(0).size() - 1) - i, (phaseList.get(0).get(phaseList.get(0).size() - 1 - i)));
//////                            newPhase.get(0).set(i + 1, (-(phaseList.get(0).get(phaseList.get(0).size() - 1 - i))));
//////                            newPhase.get(0).set((phaseList.get(0).size() - 1) - i, -0.1);
//////                            newPhase.get(0).set(i + 1, 0.1);
////                        }
////                        newPhase.get(0).set(i, Math.PI / (-2));
////                        newPhase.get(0).set((phaseList.get(0).size() / 2) - i, Math.PI / (2));
//                        newPhase.get(0).set((phaseList.get(0).size()) - i, Math.PI / (-2.0));
//                        newPhase.get(0).set(i, Math.PI / (2.0));
//                    } else {
////                        if (phaseList.get(0).get(i) > 0){
////
////                            newPhase.get(0).set((phaseList.get(0).size()) - i, -(phaseList.get(0).get(i) - Math.PI));
////                            newPhase.get(0).set(i, (phaseList.get(0).get(i) - Math.PI));
//////                            newPhase.get(0).set((phaseList.get(0).size() - 1) - i, (-(phaseList.get(0).get(phaseList.get(0).size() - 1 - i))));
//////                            newPhase.get(0).set(i + 1, (phaseList.get(0).get(phaseList.get(0).size() - 1 - i)));
//////                            newPhase.get(0).set((phaseList.get(0).size() - 1) - i, 0.1);
//////                            newPhase.get(0).set(i + 1, -0.1);
////                        }
////                        newPhase.get(nomer).set(i, Math.PI / 2);
////                        newPhase.get(nomer).set((phaseList.get(nomer).size() / 2) + i, Math.PI / 2);
//                        newPhase.get(0).set((phaseList.get(0).size()) - i, Math.PI / 2.0);
//                        newPhase.get(0).set(i, Math.PI / -2.0);
//                    }
//                    counter++;
//                }
//            }
//        }



        for (int i = 1; i < phaseList.get(0).size(); i++) {

            if (counter < textMass.size()) {
                if (i > 0 && i < (phaseList.get(0).size() - 1) / 2) {
                    if (textMass.get(counter)) {
                        if (phaseList.get(0).get(i) > 0 && phaseList.get(0).get(i) < (Math.PI / 4.0)){
                            newPhase.get(0).set(i, ((3 * Math.PI )/ 8.0));
                            newPhase.get(0).set(phaseList.get(0).size() - i, -((3 * Math.PI )/ 8.0));
                            if ( (Math.abs(newPhase.get(0).get(i - 1) - newPhase.get(0).get(i))) - Math.abs((phaseList.get(0).get(i - 1) - phaseList.get(0).get(i))) > (Math.PI / 4.0)){
                                newPhase.get(0).set(i, -(Math.PI / 16.0));
                                newPhase.get(0).set(phaseList.get(0).size() - i, (Math.PI / 16.0));
                            }
                        }
                        if (phaseList.get(0).get(i) > (Math.PI / 2.0) && phaseList.get(0).get(i) < ((3 * Math.PI) / 4.0)){
                            newPhase.get(0).set(i, ((7 * Math.PI) / 8.0));
                            newPhase.get(0).set(phaseList.get(0).size() - i, -(((7 * Math.PI) / 8.0)));
                            if ( (Math.abs(newPhase.get(0).get(i - 1) - newPhase.get(0).get(i))) - Math.abs((phaseList.get(0).get(i - 1) - phaseList.get(0).get(i))) > (Math.PI / 4.0)){
                                newPhase.get(0).set(i, (3 * Math.PI / 8.0));
                                newPhase.get(0).set(phaseList.get(0).size() - i, -(3 * Math.PI / 8.0));
                            }

                        }
                        if (phaseList.get(0).get(i) < -(Math.PI / 4.0) && phaseList.get(0).get(i) >  -(Math.PI / 2.0)){
                            newPhase.get(0).set(i, -(5 * Math.PI / 8.0));
                            newPhase.get(0).set(phaseList.get(0).size() - i, (5 * Math.PI / 8.0));
                            if ( (Math.abs(newPhase.get(0).get(i - 1) - newPhase.get(0).get(i))) - Math.abs((phaseList.get(0).get(i - 1) - phaseList.get(0).get(i))) > (Math.PI / 4.0)){
                                newPhase.get(0).set(i, -(Math.PI / 8.0));
                                newPhase.get(0).set(phaseList.get(0).size() - i, (Math.PI / 8.0));
                            }
                        }
                        if (phaseList.get(0).get(i) < -((3 * Math.PI) / 4.0) && phaseList.get(0).get(i) >  -Math.PI) {
                                newPhase.get(0).set(i, ((7 * Math.PI )/ 8.0));
                                newPhase.get(0).set(phaseList.get(0).size() - i, -(((7 * Math.PI )/ 8.0)));
                            if ( (Math.abs(newPhase.get(0).get(i - 1) - newPhase.get(0).get(i))) - Math.abs((phaseList.get(0).get(i - 1) - phaseList.get(0).get(i))) > (Math.PI / 4.0)){
                                newPhase.get(0).set(i, -(5 * Math.PI / 8.0));
                                newPhase.get(0).set(phaseList.get(0).size() - i, (5 * Math.PI / 8.0));
                            }
                        }



                        if (phaseList.get(0).get(i) > (Math.PI / 4.0) && phaseList.get(0).get(i) < (Math.PI / 2.0)){
                            newPhase.get(0).set(i, ((3 * Math.PI )/ 8.0));
                            newPhase.get(0).set(phaseList.get(0).size() - i, -(((3 * Math.PI )/ 8.0)));
//                            if (phaseList.get(0).get(i) < 3 * Math.PI / 8.0){
//                                newPhase.get(0).set(i, ((5 * Math.PI )/ 16.0));
//                                newPhase.get(0).set(phaseList.get(0).size() - i, -(((5 * Math.PI )/ 16.0)));
//                            } else {
//                                newPhase.get(0).set(i, (7 * Math.PI / 16.0));
//                                newPhase.get(0).set(phaseList.get(0).size() - i, -(7 * Math.PI / 16.0));
//                            }
                        }
                        if (phaseList.get(0).get(i) > ((3 * Math.PI) / 4.0) && phaseList.get(0).get(i) <  Math.PI){
                            newPhase.get(0).set(i, ((7 * Math.PI )/ 8.0));
                            newPhase.get(0).set(phaseList.get(0).size() - i, -(((7 * Math.PI )/ 8.0)));
//                            if (phaseList.get(0).get(i) < (7 * Math.PI / 8.0)){
//                                newPhase.get(0).set(i, ((13 * Math.PI )/ 16.0));
//                                newPhase.get(0).set(phaseList.get(0).size() - i, -(((13 * Math.PI )/ 16.0)));
//                            } else {
//                                newPhase.get(0).set(i, (15 * Math.PI / 16.0));
//                                newPhase.get(0).set(phaseList.get(0).size() - i, -(15 * Math.PI / 16.0));
//                            }
                        }
                        if (phaseList.get(0).get(i) < 0 && phaseList.get(0).get(i) >  -(Math.PI / 4.0)){
                            newPhase.get(0).set(i, -((Math.PI )/ 8.0));
                            newPhase.get(0).set(phaseList.get(0).size() - i, (Math.PI / 8.0));
//                            if (phaseList.get(0).get(i) > -(Math.PI / 8.0)){
//                                newPhase.get(0).set(i, -((Math.PI )/ 16.0));
//                                newPhase.get(0).set(phaseList.get(0).size() - i, (((Math.PI )/ 16.0)));
//                            } else {
//                                newPhase.get(0).set(i, -(3 * Math.PI / 16.0));
//                                newPhase.get(0).set(phaseList.get(0).size() - i, ((3 * Math.PI / 16.0)));
//                            }
                        }
                        if (phaseList.get(0).get(i) < -(Math.PI / 2.0) && phaseList.get(0).get(i) >  -((3* Math.PI) / 4.0)) {
                            newPhase.get(0).set(i, -((5 * Math.PI) / 8.0));
                            newPhase.get(0).set(phaseList.get(0).size() - i, (((5 * Math.PI) / 8.0)));
//                            if (phaseList.get(0).get(i) > -(5 * Math.PI / 8.0)){
//                                newPhase.get(0).set(i, -((9 * Math.PI) / 16.0));
//                                newPhase.get(0).set(phaseList.get(0).size() - i, (((9 * Math.PI) / 16.0)));
//                            } else {
//                                newPhase.get(0).set(i, -(11 * Math.PI / 16.0));
//                                newPhase.get(0).set(phaseList.get(0).size() - i, (11 * Math.PI / 16.0));
//                            }
                        }

                    } else {
                        if (phaseList.get(0).get(i) > (Math.PI / 4.0) && phaseList.get(0).get(i) < (Math.PI / 2.0)){
                            newPhase.get(0).set(i, ((5 * Math.PI )/ 8.0));
                            newPhase.get(0).set(phaseList.get(0).size() - i, -(((5 * Math.PI )/ 8.0)));
                            if ( (Math.abs(newPhase.get(0).get(i - 1) - newPhase.get(0).get(i))) - Math.abs((phaseList.get(0).get(i - 1) - phaseList.get(0).get(i))) > (Math.PI / 4.0)){
                                newPhase.get(0).set(i, -(7 * Math.PI / 8.0));
                                newPhase.get(0).set(phaseList.get(0).size() - i, (7 * Math.PI / 8.0));
                            }
                        }
                        if (phaseList.get(0).get(i) > ((3 * Math.PI) / 4.0) && phaseList.get(0).get(i) <  Math.PI){
                            newPhase.get(0).set(i, -((7 * Math.PI )/ 8.0));
                            newPhase.get(0).set(phaseList.get(0).size() - i, (((7 * Math.PI )/ 8.0)));
                            if ( (Math.abs(newPhase.get(0).get(i - 1) - newPhase.get(0).get(i))) - Math.abs((phaseList.get(0).get(i - 1) - phaseList.get(0).get(i))) > (Math.PI / 4.0)){
                                newPhase.get(0).set(i, -(3 * Math.PI / 8.0));
                                newPhase.get(0).set(phaseList.get(0).size() - i, (3 * Math.PI / 8.0));
                            }
                        }
                        if (phaseList.get(0).get(i) < 0 && phaseList.get(0).get(i) >  -(Math.PI / 4.0)){
                            newPhase.get(0).set(i, -((3 * Math.PI )/ 8.0));
                            newPhase.get(0).set(phaseList.get(0).size() - i, (((3 * Math.PI )/ 8.0)));
                            if ( (Math.abs(newPhase.get(0).get(i - 1) - newPhase.get(0).get(i))) - Math.abs((phaseList.get(0).get(i - 1) - phaseList.get(0).get(i))) > (Math.PI / 4.0)){
                                newPhase.get(0).set(i, (Math.PI / 16.0));
                                newPhase.get(0).set(phaseList.get(0).size() - i, -(Math.PI / 16.0));
                            }
                        }
                        if (phaseList.get(0).get(i) < -(Math.PI / 2.0) && phaseList.get(0).get(i) >  -((3* Math.PI) / 4.0)) {
                            newPhase.get(0).set(i, -((7 * Math.PI )/ 8.0));
                            newPhase.get(0).set(phaseList.get(0).size() - i, (((7 * Math.PI )/ 8.0)));
                            if ( (Math.abs(newPhase.get(0).get(i - 1) - newPhase.get(0).get(i))) - Math.abs((phaseList.get(0).get(i - 1) - phaseList.get(0).get(i))) > (Math.PI / 4.0)){
                                newPhase.get(0).set(i, -(3 * Math.PI / 8.0));
                                newPhase.get(0).set(phaseList.get(0).size() - i, (3 * Math.PI / 8.0));
                            }
                        }

                        if (phaseList.get(0).get(i) > 0 && phaseList.get(0).get(i) < (Math.PI / 4.0)) {
                            newPhase.get(0).set(i, ((Math.PI) / 8.0));
                            newPhase.get(0).set(phaseList.get(0).size() - i, -((Math.PI) / 8.0));
//                            if (phaseList.get(0).get(i) < (Math.PI / 8.0)) {
//                                newPhase.get(0).set(i, ((Math.PI) / 16.0));
//                                newPhase.get(0).set(phaseList.get(0).size() - i, -((Math.PI) / 16.0));
//                            } else {
//                                newPhase.get(0).set(i, ((3 * Math.PI) / 16.0));
//                                newPhase.get(0).set(phaseList.get(0).size() - i, -((3 * Math.PI) / 16.0));
//                            }
                        }
                        if (phaseList.get(0).get(i) > (Math.PI / 2.0) && phaseList.get(0).get(i) < ((3 * Math.PI) / 4.0)) {
                            newPhase.get(0).set(i, ((5 * Math.PI) / 8.0));
                            newPhase.get(0).set(phaseList.get(0).size() - i, -((5 * Math.PI) / 8.0));
//                            if (phaseList.get(0).get(i) < (5 * Math.PI / 8.0)) {
//                                newPhase.get(0).set(i, ((9 * Math.PI) / 16.0));
//                                newPhase.get(0).set(phaseList.get(0).size() - i, -((9 * Math.PI) / 16.0));
//                            } else {
//                                newPhase.get(0).set(i, ((11 * Math.PI) / 16.0));
//                                newPhase.get(0).set(phaseList.get(0).size() - i, -((11 * Math.PI) / 16.0));
//                            }
                        }
                        if (phaseList.get(0).get(i) < -(Math.PI / 4.0) && phaseList.get(0).get(i) >  -(Math.PI / 2.0)) {
                            newPhase.get(0).set(i, -((3 * Math.PI) / 8.0));
                            newPhase.get(0).set(phaseList.get(0).size() - i, ((3 * Math.PI) / 8.0));
//                            if (phaseList.get(0).get(i) > -(3 * Math.PI / 8.0)) {
//                                newPhase.get(0).set(i, -((5 * Math.PI) / 16.0));
//                                newPhase.get(0).set(phaseList.get(0).size() - i, ((5 * Math.PI) / 16.0));
//                            } else {
//                                newPhase.get(0).set(i, -((7 * Math.PI) / 16.0));
//                                newPhase.get(0).set(phaseList.get(0).size() - i, ((7 * Math.PI) / 16.0));
//                            }
                        }
                        if (phaseList.get(0).get(i) < -((3 * Math.PI) / 4.0) && phaseList.get(0).get(i) >  -Math.PI) {
                            newPhase.get(0).set(i, -((7 * Math.PI) / 8.0));
                            newPhase.get(0).set(phaseList.get(0).size() - i, ((7 * Math.PI) / 8.0));
//                            if (phaseList.get(0).get(i) > -(7 * Math.PI / 8.0)) {
//                                newPhase.get(0).set(i, -((13 * Math.PI) / 16.0));
//                                newPhase.get(0).set(phaseList.get(0).size() - i, ((13 * Math.PI) / 16.0));
//                            } else {
//                                newPhase.get(0).set(i, -((15 * Math.PI) / 16.0));
//                                newPhase.get(0).set(phaseList.get(0).size() - i, ((15 * Math.PI) / 16.0));
//                            }
                        }
                    }
                    counter++;
//                    if (counter == textMass.size()){
//                        counter = 0;
//                    }

                }
            }
        }
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
    public List<List<Double>> creationDifferencePhaseList (List<List<Double>> phaseList){
        List<List<Double>> differencePhaseList = new ArrayList<List<Double>>();
        for (int i = 0; i < phaseList.size(); i++) {
            List<Double> differencePhase = new ArrayList<Double>();
            for (int k = 0; k < phaseList.get(i).size(); k++) {
                if ((i == 0 )){
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

    public List<Integer> poluchenieTexta (List<List<Double>> phaseList, int sizeText){

        List <Integer> text = new ArrayList<Integer>();
        for (int i = 1; i < phaseList.get(0).size(); i++) {

//            if (phaseList.get(0).get(i) > 0){
//                text.add(1);
//            }
//
//            if (phaseList.get(0).get(i) < 0){
//                text.add(0);
//            }

//            для разбиения на 8
            if (phaseList.get(0).get(i) > 0 && phaseList.get(0).get(i) < (Math.PI / 4.0)) {
                text.add(0);
            }
            if (phaseList.get(0).get(i) > (Math.PI / 2.0) && phaseList.get(0).get(i) < ((3 * Math.PI) / 4.0)) {
                text.add(0);
            }
            if (phaseList.get(0).get(i) < -(Math.PI / 4.0) && phaseList.get(0).get(i) > -(Math.PI / 2.0)) {
                text.add(0);
            }
            if (phaseList.get(0).get(i) < -((3 * Math.PI) / 4.0) && phaseList.get(0).get(i) > -(Math.PI)) {
                text.add(0);
            }
            if (phaseList.get(0).get(i) > (Math.PI / 4.0) && phaseList.get(0).get(i) < (Math.PI / 2.0)) {
                text.add(1);
            }
            if (phaseList.get(0).get(i) > ((3 * Math.PI) / 4.0) && phaseList.get(0).get(i) < Math.PI) {
                text.add(1);
            }
            if (phaseList.get(0).get(i) < 0 && phaseList.get(0).get(i) > -(Math.PI / 4.0)) {
                text.add(1);
            }
            if (phaseList.get(0).get(i) < -(Math.PI / 2.0) && phaseList.get(0).get(i) > -((3 * Math.PI) / 4.0)) {
                text.add(1);
            }
        }

        List <Integer> textEnd = new ArrayList<Integer>();
        for (int i = 0; i < sizeText; ++i) {
            textEnd.add(text.get(i));
        }
        System.out.println("итоговый массив");
        System.out.println(textEnd);

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
            int t = perevod2v10(textOne.get(i));
//            int t = 0;
//            for (int k = 0; k < textOne.get(i).size(); ++k){
//                if ( textOne.get(i).get((textOne.get(i).size() - 1) - k) == 1) {
//                    t += Math.pow(2, k);
//                }
//            }
            text1.add(t);
        }
//        System.out.println(text1);
        return text1;
    }

    public static int perevod2v10(List<Integer> list) {
        int a = 0;
        for (int i = 0; i < list.size() && i < 8; ++i) {
            if (list.get(i) == 1)
                a = a | (1 << (7 - i));

        }
        if (a > 127)
            return (a - 256);
        else
            return a;
    }
}
