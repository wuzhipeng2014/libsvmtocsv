package toutiao.util;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.junit.Test;
import toutiao.bean.FormatedOriginFeature;
import toutiao.bean.OriginFeature;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

/**
 * Created by zhipengwu on 17-9-6.
 */
public class AnalysisFeature {
    public static DecimalFormat dcmFmt = new DecimalFormat("0.00");

    public static void main(String[] args) {
        // String inputFile = "/home/zhipengwu/secureCRT/std_test_feature_20170822_20170906.txt";
        String inputFile = "/home/zhipengwu/secureCRT/part_origin_feature_20170822_09-07-2.txt";
        try {
            LineIterator lineIterator = FileUtils.lineIterator(new File(inputFile));
            FileWriter fw_pos = new FileWriter(String.format("%s_pos.csv", inputFile));
            FileWriter fw_neg = new FileWriter(String.format("%s_neg.csv", inputFile));
            FileWriter fw = new FileWriter(String.format("%s_all.csv", inputFile));
            String header = "lable,weekMax,weekMin,monthMax, monthMin,monthRatio\n";
            fw_pos.append(header);
            fw_neg.append(header);

            int count_neg = 0;

            String monthShiftCityRatio = "0";
            while (lineIterator.hasNext()) {
                String line = lineIterator.nextLine();
                if (!Strings.isNullOrEmpty(line)) {
                    String[] split = line.split("\t");
                    int length = split.length;

                    if (length > 5) {
                        String keyId = split[0];
                        String lable = split[1];

                        int len = split.length;
                        // split[0]=null; //keyid
                        split[len - 2] = split[len - 2].replaceAll(",", "#").replaceAll(" ", "");
                        split[len - 1] = split[len - 1].replaceAll(",", "#").replaceAll(" ", ""); // allCityName

                        // ===========================特征进行分段处理===========================================

                        // FormatedOriginFeature formatedOriginFeature=new FormatedOriginFeature();
                        // // 将正负例差异较大的特征 分段 转换位向量
                        // OriginFeature originFeature = convertMajorFeatruretoVector(split, formatedOriginFeature);
                        // //将其他向量拷贝到FormatedOriginFeature
                        // cloneFeaturetoFormatedOriginFeature(originFeature,formatedOriginFeature);
                        //
                        // //将formatedOriginFeature 转换为csv文件
                        // String join = formatedOriginFeature.toCsv();

                        // ==================================================================================

                        String join = Joiner.on(",").skipNulls().join(split);

                        fw.append(join + "\n");
                        fw.flush();
                        // if (lable.equalsIgnoreCase("1")) {
                        // fw_pos.append(join+"\n");
                        // fw_pos.flush();
                        // } else {
                        // if (count_neg++<40000) {
                        // fw_neg.append(join + "\n");
                        // fw_neg.flush();
                        // }
                        // }

                    } else {
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

    public static OriginFeature convertMajorFeatruretoVector(String[] split,
            FormatedOriginFeature formatedOriginFeature) {
        OriginFeature originFeature = OriginFeature.createOriginFeature(split);
        List<Double> borderList = Lists.newArrayList();

        // 1.citeNum
        // List<Double> borderList = Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0, 6.0);
        // List<String> citeNumVector = convertoVector(originFeature.citeNum, borderList);
        // formatedOriginFeature.citeNumVector = citeNumVector;
        formatedOriginFeature.citeNumVector = Arrays.asList(originFeature.citeNum);

        // 2.areaNum
        // borderList = Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0, 6.0,7.0,8.0,9.0);
        // List<String> areaNumVector = convertoVector(originFeature.areaNum, borderList);
        // formatedOriginFeature.areaNumVector = areaNumVector;
        formatedOriginFeature.areaNumVector = Arrays.asList(originFeature.areaNum);

        return originFeature;

    }

    public static void cloneFeaturetoFormatedOriginFeature(OriginFeature originFeature,
            FormatedOriginFeature formatedOriginFeature) {
        formatedOriginFeature.keyid = originFeature.keyid;
        formatedOriginFeature.label = originFeature.label;
        formatedOriginFeature.activeDays = originFeature.activeDays;
        formatedOriginFeature.activeWeeks = originFeature.activeWeeks;
        formatedOriginFeature.activeDaysOfTopCity = originFeature.activeDaysOfTopCity;
        formatedOriginFeature.activeDaysOfEndCity = originFeature.activeDaysOfEndCity;
        formatedOriginFeature.gender = originFeature.gender;
        formatedOriginFeature.age = originFeature.age;
        formatedOriginFeature.platform = originFeature.platform;
        // formatedOriginFeature.citeNum=originFeature.citeNum;
        // formatedOriginFeature.areaNum=originFeature.areaNum;
        formatedOriginFeature.shiftCitys0 = originFeature.shiftCitys0;
        formatedOriginFeature.shiftCitys1 = originFeature.shiftCitys1;
        formatedOriginFeature.shiftCitysOnWorkingDay0 = originFeature.shiftCitysOnWorkingDay0;
        formatedOriginFeature.shiftCitysOnWorkingDay1 = originFeature.shiftCitysOnWorkingDay1;
        formatedOriginFeature.shiftCitysOnWeekend0 = originFeature.shiftCitysOnWeekend0;
        formatedOriginFeature.shiftCitysOnWeekend1 = originFeature.shiftCitysOnWeekend1;
        formatedOriginFeature.avgActiveRadius0 = originFeature.avgActiveRadius0;
        formatedOriginFeature.avgActiveRadius1 = originFeature.avgActiveRadius1;
        formatedOriginFeature.maxActiveRadius0 = originFeature.maxActiveRadius0;
        formatedOriginFeature.maxActiveRadius1 = originFeature.maxActiveRadius1;
        formatedOriginFeature.avgActiveRadiusOnWorkingDay0 = originFeature.avgActiveRadiusOnWorkingDay0;
        formatedOriginFeature.avgActiveRadiusOnWorkingDay1 = originFeature.avgActiveRadiusOnWorkingDay1;
        formatedOriginFeature.maxActiveRadiusOnWorkingDay0 = originFeature.maxActiveRadiusOnWorkingDay0;
        formatedOriginFeature.maxActiveRadiusOnWorkingDay1 = originFeature.maxActiveRadiusOnWorkingDay1;
        formatedOriginFeature.avgActiveRadiusOnWeekend0 = originFeature.avgActiveRadiusOnWeekend0;
        formatedOriginFeature.avgActiveRadiusOnWeekend1 = originFeature.avgActiveRadiusOnWeekend1;
        formatedOriginFeature.maxActiveRadiusOnWeekend0 = originFeature.maxActiveRadiusOnWeekend0;
        formatedOriginFeature.maxActiveRadiusOnWeekend1 = originFeature.maxActiveRadiusOnWeekend1;
        formatedOriginFeature.model = originFeature.model;
        formatedOriginFeature.allCity = originFeature.allCity;

    }

    public static String CalRatio(String max, String min) {
        if (!Strings.isNullOrEmpty(min) && !Strings.isNullOrEmpty(max) && !min.equalsIgnoreCase("0")
                && !min.equalsIgnoreCase("0.0")) {
            return String.valueOf(dcmFmt.format(Double.valueOf(max) / Double.valueOf(min)));
        } else {
            return max;
        }
    }

    @Test
    public void formateHeader() {
        String line = "activeDays \n" + "activeWeeks \n" + "activeDaysOfTopCity\n" + "activeDaysOfEndCity\n"
                + "gender\n" + "age\n" + "platform\n" + "citeNum\n" + "areaNum\n" + "shiftCitys0\n" + "shiftCitys1\n"
                + "shiftCitysOnWorkingDay0\n" + "shiftCitysOnWorkingDay1\n" + "shiftCitysOnWeekend0\n"
                + "shiftCitysOnWeekend1\n" + "avgActiveRadius0\n" + "avgActiveRadius1\n" + "maxActiveRadius0\n"
                + "maxActiveRadius1\n" + "avgActiveRadiusOnWorkingDay0\n" + "avgActiveRadiusOnWorkingDay1\n"
                + "maxActiveRadiusOnWorkingDay0\n" + "maxActiveRadiusOnWorkingDay1\n" + "avgActiveRadiusOnWeekend0\n"
                + "avgActiveRadiusOnWeekend1\n" + "maxActiveRadiusOnWeekend0\n" + "maxActiveRadiusOnWeekend1\n"
                + "model\n" + "allCity";
        String[] split = line.split("\n");
        String join = Joiner.on(",").skipNulls().join(split);

        System.out.println(join);

    }
}
