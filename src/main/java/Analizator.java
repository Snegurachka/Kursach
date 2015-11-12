import java.util.Vector;

/**
 * Created by elena on 09.11.15.
 */
public class Analizator {

    public void analize (Vector<Integer> music, Vector<Integer> text){

        Vector <Integer> endVector = new Vector<Integer>();

        Vector<Vector<Integer>> endMass;
        endMass = razbienie(music, text);


    }
    //разбиение на сегменты массива аудио
    public Vector<Vector<Integer>> razbienie (Vector<Integer> music, Vector<Integer> text){
        int sizeText = text.size();
        int sizeMusic = music.size();
        int sizeBitText = sizeText*8;

        int v = (int) Math.ceil((Math.log10((double) sizeBitText) * 3.322 + 1));
        int K =(int) Math.pow(2.0, (double) v+1);
        double N = Math.ceil(sizeMusic / K); // количество сегментов
        double Nn = sizeMusic / K;

        Vector <Vector<Integer>> mass = new Vector<Vector<Integer>>();
        for (int i = 0; i < N; i ++){

            Vector <Integer> massSegment = new Vector<Integer>();
            for ( int k = 0; k < K; k ++){
                massSegment.add(music.get(i*K+k));
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
