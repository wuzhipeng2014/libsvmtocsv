package toutiao.util;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.junit.Test;

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
//        String inputFile = "/home/zhipengwu/secureCRT/std_test_feature_20170822_20170906.txt";
        String inputFile = "/home/zhipengwu/secureCRT/part_origin_feature_20170822_09-07-2.txt";
        try {
            LineIterator lineIterator = FileUtils.lineIterator(new File(inputFile));
            FileWriter fw_pos = new FileWriter(String.format("%s_pos.csv", inputFile));
            FileWriter fw_neg = new FileWriter(String.format("%s_neg.csv", inputFile));
            FileWriter fw = new FileWriter(String.format("%s_all.csv", inputFile));
            String header="lable,weekMax,weekMin,monthMax, monthMin,monthRatio\n";
            fw_pos.append(header);
            fw_neg.append(header);

            int count_neg=0;

            String monthShiftCityRatio="0";
            while (lineIterator.hasNext()) {
                String line = lineIterator.nextLine();
                if (!Strings.isNullOrEmpty(line)) {
                    String[] split = line.split("\t");
                    int length = split.length;
                    if (length > 5) {
                        String keyId=split[0];
                        String lable = split[1];

                        int len=split.length;
//                        split[0]=null; //keyid
                        split[len-2]=split[len-2].replaceAll(",","#").replaceAll(" ","");
                        split[len-1]=split[len-1].replaceAll(",","#").replaceAll(" ",""); //allCityName
                        String join = Joiner.on(",").skipNulls().join(split);
                        fw.append(join+"\n");
                        fw.flush();
//                        if (lable.equalsIgnoreCase("1")) {
//                            fw_pos.append(join+"\n");
//                            fw_pos.flush();
//                        } else {
//                            if (count_neg++<40000) {
//                                fw_neg.append(join + "\n");
//                                fw_neg.flush();
//                            }
//                        }

                    }else {
                        System.out.println(line);
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


    public static String extractFeatrure(String[] split){
        return null;
    }






    public static String CalRatio(String max,String min){
        if (!Strings.isNullOrEmpty(min)&&!Strings.isNullOrEmpty(max)&&!min.equalsIgnoreCase("0")&&!min.equalsIgnoreCase("0.0")){
            return String.valueOf(dcmFmt.format(Double.valueOf(max)/Double.valueOf(min)));
        }else {
            return max;
        }
    }



    @Test
    public void formateHeader(){
        String line ="activeDays \n" +
                "activeWeeks \n" +
                "activeDaysOfTopCity\n" +
                "activeDaysOfEndCity\n" +
                "gender\n" +
                "age\n" +
                "platform\n" +
                "citeNum\n" +
                "areaNum\n" +
                "shiftCitys0\n" +
                "shiftCitys1\n" +
                "shiftCitysOnWorkingDay0\n" +
                "shiftCitysOnWorkingDay1\n" +
                "shiftCitysOnWeekend0\n" +
                "shiftCitysOnWeekend1\n" +
                "avgActiveRadius0\n" +
                "avgActiveRadius1\n" +
                "maxActiveRadius0\n" +
                "maxActiveRadius1\n" +
                "avgActiveRadiusOnWorkingDay0\n" +
                "avgActiveRadiusOnWorkingDay1\n" +
                "maxActiveRadiusOnWorkingDay0\n" +
                "maxActiveRadiusOnWorkingDay1\n" +
                "avgActiveRadiusOnWeekend0\n" +
                "avgActiveRadiusOnWeekend1\n" +
                "maxActiveRadiusOnWeekend0\n" +
                "maxActiveRadiusOnWeekend1\n" +
                "model\n" +
                "allCity";
        String[] split = line.split("\n");
        String join = Joiner.on(",").skipNulls().join(split);

        System.out.println(join);

    }
}
