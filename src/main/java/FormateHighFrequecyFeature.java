import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by zhipengwu on 17-8-16.
 */
public class FormateHighFrequecyFeature {
    public static void main(String[] args) {
        String inputFile = "/home/zhipengwu/secureCRT/top_1000_feature.txt";
        String outputFile = "/home/zhipengwu/secureCRT/format_top_1000_feature.txt";
        try {
            FileWriter fw = new FileWriter(outputFile);
            LineIterator lineIterator = FileUtils.lineIterator(new File(inputFile));
            while (lineIterator.hasNext()) {
                String line = lineIterator.nextLine();
                if (!line.contains("-")){
                    continue;
                }
                String[] split = line.split("\t");
                int i = split[0].indexOf("-");
                String feature = split[0].substring(0,i);
                String value = split[0].substring(i+1);
                if (feature.equalsIgnoreCase("allCity")) {
                    feature = "C3";
                } else if (feature.equalsIgnoreCase("model")) {
                    feature = "C2";
                }
                fw.append(String.format("%s-%s\n", feature, value));

                fw.flush();
            }
            fw.close();
            lineIterator.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
