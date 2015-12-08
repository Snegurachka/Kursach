import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

/**
 * Created by elena on 08.12.15.
 */

public class Gnuplot {
    private String directory = "out";
    private HashMap<String, Double> maxValues = new HashMap<String, Double>();
    private HashMap<String, Double> minValues = new HashMap<String, Double>();
    private String old = "old";
    private String newf = "newf";


    private List<Long> resultNewList;



    public Gnuplot(Analizator analizator) {
        this.resultNewList = analizator.getEndList();


    }

    public void printAll() {
        try {

            printToFile2(newf, resultNewList);
            List<String> I = new ArrayList<String>();
            I.add(newf);
            createGnuplotScript("result.script", I, "I, A", "Time, s");

        } catch (Exception e) {
            System.out.println("Error While printing to files");
        }
    }

    public void printToFile2(String fileName, List<Long> f) throws Exception {
        PrintWriter out = new PrintWriter(new File(directory, fileName));
        Long max = resultNewList.get(0);
        Long min = resultNewList.get(0);
        for (int i = 0; i < resultNewList.size(); ++i) {
            double time = i;
            Long vector = resultNewList.get(i);
            out.println(time + " " + vector);
            if (vector > max) {
                max = vector;
            }
            if (vector < min) {
                min = vector;
            }
        }
//        maxValues.put(fileName,(double) max);
//        minValues.put(fileName, (double) min);
        out.close();
    }


    public void createGnuplotScript(String fileName, List<String> graph, String YTitle, String XTitle) throws Exception{
        double max = maxValues.get(graph.get(0));
        double min = minValues.get(graph.get(0));
        for (int i = 0; i < graph.size(); i++) {

            double maxTemp = maxValues.get(graph.get(i));
            double minTemp = minValues.get(graph.get(i));
            if (maxTemp > max)
                max = maxTemp;
            if (minTemp < min)
                min = minTemp;
        }

        PrintWriter out = new PrintWriter(new File(directory, fileName));
        String scriptCode = "set terminal x11 size 1360, 700\n" +
                "set xlabel '" + XTitle + "'\n" +
                "set ylabel '" + YTitle + "'\n" +
                "set xrange [" + 0 + ":" + resultNewList.size() + "]\n" +
                "set yrange [" + min + ":" + max + "]\n" +
                "set grid\n" +
                "plot ";
        for (int i = 0; i < (graph.size() - 1); ++i) {
            scriptCode += "'" + graph.get(i) + "' using 1:2 w l lw 2 title '" +
                    graph.get(i) + "',\\\n";
        }
        String temp = graph.get(graph.size() - 1);
        scriptCode += "'" + temp + "' using 1:2 w l lw 2 title '" + temp + "'\n" +
                "pause -1";
        out.print(scriptCode);
        out.close();
    }
}