package toutiao.util;

import com.google.common.base.Strings;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by zhipengwu on 17-9-6.
 */
public class AnalysisFeature {
    public static void main(String[] args) {
        String inputFile = "/home/zhipengwu/secureCRT/std_train_feature_20170822_20170905.txt";
        try {
            LineIterator lineIterator = FileUtils.lineIterator(new File(inputFile));
            FileWriter fw_pos = new FileWriter(String.format("%s_pos.csv", inputFile));
            FileWriter fw_neg = new FileWriter(String.format("%s_neg.csv", inputFile));
            String header="lable,weekMax,weekMin,monthMax, monthMin\n";
            fw_pos.append(header);
            fw_neg.append(header);

            while (lineIterator.hasNext()) {
                String line = lineIterator.nextLine();
                if (!Strings.isNullOrEmpty(line)) {
                    String[] split = line.split("\t");
                    int length = split.length;
                    if (length > 5) {
                        String lable = split[1];
                        String monthMin = split[length - 1];
                        String monthMax = split[length - 2];
                        String weekMin = split[length - 3];
                        String weekMax = split[length - 4];
                        if (lable.equalsIgnoreCase("1")) {
                            fw_pos.append(String.format("%s,%s,%s,%s,%s\n", lable, weekMax, weekMin, monthMax, monthMin));
                            fw_pos.flush();
                        } else {
                            fw_neg.append(String.format("%s,%s,%s,%s,%s\n", lable, weekMax, weekMin, monthMax, monthMin));
                            fw_neg.flush();
                        }

                    }

                }
            }
            fw_neg.close();
            fw_pos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
