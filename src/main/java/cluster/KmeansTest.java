package cluster;

import java.io.IOException;
import java.util.Map;

/**
 * Created by zhipengwu on 17-9-19.
 */
public class KmeansTest {


    public static void main(String[] args) {
        String inputfile="/home/zhipengwu/secureCRT/inputlist.txt";
        try {
            Process.loadData(inputfile);
            Map.Entry<Integer[], Double> cluster = Process.cluster(3);


            System.out.println();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
