package toutiao.util;

import com.google.common.base.Strings;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;

/**
 * Created by zhipengwu on 17-9-6.
 */
public class AnalysisFeature {
    public static DecimalFormat dcmFmt = new DecimalFormat("0.00");

    public static void main(String[] args) {
        String inputFile = "/home/zhipengwu/secureCRT/std_test_feature_20170822_20170906.txt";
        try {
            LineIterator lineIterator = FileUtils.lineIterator(new File(inputFile));
            FileWriter fw_pos = new FileWriter(String.format("%s_pos.csv", inputFile));
            FileWriter fw_neg = new FileWriter(String.format("%s_neg.csv", inputFile));
            FileWriter fw = new FileWriter(String.format("%s_all.csv", inputFile));
            String header="lable,weekMax,weekMin,monthMax, monthMin,monthRatio\n";
            fw_pos.append(header);
            fw_neg.append(header);

            String monthShiftCityRatio="0";
            while (lineIterator.hasNext()) {
                String line = lineIterator.nextLine();
                if (!Strings.isNullOrEmpty(line)) {
                    String[] split = line.split("\t");
                    int length = split.length;
                    if (length > 5) {
                        String keyId=split[0];
                        String lable = split[1];
                        String monthMin = split[length - 1];
                        String monthMax = split[length - 2];
                        monthShiftCityRatio=CalRatio(monthMax,monthMin);
                        String weekMin = split[length - 3];
                        String weekMax = split[length - 4];
                        fw.append(String.format("%s,%s,%s,%s,%s,%s,%s\n", keyId,lable, weekMax, weekMin, monthMax, monthMin,monthShiftCityRatio));
                        if (lable.equalsIgnoreCase("1")) {
                            fw_pos.append(String.format("%s,%s,%s,%s,%s,%s\n", lable, weekMax, weekMin, monthMax, monthMin,monthShiftCityRatio));
                            fw_pos.flush();
                        } else {
                            fw_neg.append(String.format("%s,%s,%s,%s,%s,%s\n", lable, weekMax, weekMin, monthMax, monthMin,monthShiftCityRatio));
                            fw_neg.flush();
                        }

                    }

                }
            }
            fw_neg.close();
            fw_pos.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public static String CalRatio(String max,String min){
        if (!Strings.isNullOrEmpty(min)&&!Strings.isNullOrEmpty(max)&&!min.equalsIgnoreCase("0")&&!min.equalsIgnoreCase("0.0")){
            return String.valueOf(dcmFmt.format(Double.valueOf(max)/Double.valueOf(min)));
        }else {
            return max;
        }
    }
}
