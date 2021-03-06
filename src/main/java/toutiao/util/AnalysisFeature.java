package toutiao.util;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
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
import java.util.Map;

import static toutiao.util.FeatureUtil.segmentFeature;

/**
 * Created by zhipengwu on 17-9-6.
 * 将琳姐生成的原始特征进行特殊处理
 */
public class AnalysisFeature {
    public static DecimalFormat dcmFmt = new DecimalFormat("0.00");

    public static Map<String,String> newLableMap=Maps.newHashMap();
    static {
        String newLableFile="/home/zhipengwu/secureCRT/new_labeled_train_20170919.txt";

        loadNewLabelMap(newLableFile);
    }

    public static void main(String[] args) {
//        newLableMap= Maps.newHashMap();

        // String inputFile = "/home/zhipengwu/secureCRT/std_test_feature_20170822_20170906.txt";
        String inputFile = "/home/zhipengwu/secureCRT/part_origin_feature_20170822_09-07-2.txt";

//        String newLableFile="/home/zhipengwu/secureCRT/new_labeled_train_20170919.txt";
//        loadNewLabelMap(newLableFile);

        try {
            LineIterator lineIterator = FileUtils.lineIterator(new File(inputFile));
//            FileWriter fw_pos = new FileWriter(String.format("%s_pos.csv", inputFile));
//            FileWriter fw_neg = new FileWriter(String.format("%s_neg.csv", inputFile));
            FileWriter fw = new FileWriter(String.format("%s_all.csv", inputFile));
            String header = "lable,weekMax,weekMin,monthMax, monthMin,monthRatio\n";
//            fw_pos.append(header);
//            fw_neg.append(header);

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

                        //todo 标签替换(6月之后有订单的为正例)
                        String newLabel = newLableMap.get(keyId);
                        if (!Strings.isNullOrEmpty(newLabel)){
                            split[1]=newLabel;
                        }


                        int len = split.length;
                        // split[0]=null; //keyid
                        split[len - 2] = split[len - 2].replaceAll(",", "#").replaceAll(" ", "");
                        split[len - 1] = split[len - 1].replaceAll(",", "#").replaceAll(" ", ""); // allCityName

                        // ===========================特征进行分段处理===========================================

                        FormatedOriginFeature formatedOriginFeature = new FormatedOriginFeature();
                        // 将正负例差异较大的特征 分段 转换位向量
                        OriginFeature originFeature = convertMajorFeatruretoVector(split, formatedOriginFeature);
                        // 将其他向量拷贝到FormatedOriginFeature
                        cloneFeaturetoFormatedOriginFeature(originFeature, formatedOriginFeature);

                        // 将formatedOriginFeature 转换为csv文件
                        String join = formatedOriginFeature.toCsv();

                        // ==================================================================================

                        // String join = Joiner.on(",").skipNulls().join(split);

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
//            fw_neg.close();
//            fw_pos.close();
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
         borderList = Arrays.asList(0.085,0.64,2.195,4.255,6.755,13.205,19.82,26.77,223.475);
//         String citeNumVector = segmentFeature(originFeature.citeNum, borderList);
//         formatedOriginFeature.citeNumVector = Arrays.asList(citeNumVector);
        formatedOriginFeature.citeNumVector = Arrays.asList(originFeature.citeNum);

        // 2.areaNum
        // borderList = Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0, 6.0,7.0,8.0,9.0);
        // List<String> areaNumVector = convertoVector(originFeature.areaNum, borderList);
        // formatedOriginFeature.areaNumVector = areaNumVector;
        formatedOriginFeature.areaNumVector = Arrays.asList(originFeature.areaNum);

        // 3. activeWeeks
        borderList = Arrays.asList(0.5, 1.5, 2.5, 3.5, 4.5, 5.5, 6.5, 7.5, 8.5, 9.5, 10.5, 11.5, 12.5, 13.5, 14.5, 15.5,
                17.5, 19.5, 21.5, 23.5, 27.5);
        formatedOriginFeature.activeWeeks = segmentFeature(originFeature.activeWeeks, borderList);

        //maxActiveRadiusOnWorkingDay0
//        borderList = Arrays.asList(1.03,4.14,12.76,25.66,71.135);
//        formatedOriginFeature.maxActiveRadiusOnWorkingDay0= segmentFeature(originFeature.maxActiveRadiusOnWorkingDay0, borderList);
        formatedOriginFeature.maxActiveRadiusOnWorkingDay0= originFeature.maxActiveRadiusOnWorkingDay0;


        //maxActiveRadiusOnWorkingDay1
        borderList = Arrays.asList(1.03,5.365,14.26,26.77,71.915,249.715);
        formatedOriginFeature.maxActiveRadiusOnWorkingDay1= segmentFeature(originFeature.maxActiveRadiusOnWorkingDay1, borderList);


        //avgActiveRadiusOnWeekend0
        borderList = Arrays.asList(0.005,0.035,0.295,0.795,1.485,3.195,5.975,10.555,29.165);
//        formatedOriginFeature.avgActiveRadiusOnWeekend0= segmentFeature(originFeature.avgActiveRadiusOnWeekend0, borderList);
        formatedOriginFeature.avgActiveRadiusOnWeekend0= originFeature.avgActiveRadiusOnWeekend0;

        //avgActiveRadiusOnWeekend1
        borderList = Arrays.asList(0.195,3.31,11.035,26.825,74.585);
//        formatedOriginFeature.avgActiveRadiusOnWeekend1= segmentFeature(originFeature.avgActiveRadiusOnWeekend1, borderList);
        formatedOriginFeature.avgActiveRadiusOnWeekend1= (originFeature.avgActiveRadiusOnWeekend1);

        //maxActiveRadiusOnWeekend0
        borderList = Arrays.asList(0.085,0.35,2.03,5.645,15.315,30.275,54.07,75.5,100.5,150.5,200.18);
//        formatedOriginFeature.maxActiveRadiusOnWeekend0= segmentFeature(originFeature.maxActiveRadiusOnWeekend0, borderList);
        formatedOriginFeature.maxActiveRadiusOnWeekend0= (originFeature.maxActiveRadiusOnWeekend0);


        //maxActiveRadiusOnWeekend1
        borderList = Arrays.asList(0.195,3.31,11.035,26.825,74.585);
//        formatedOriginFeature.maxActiveRadiusOnWeekend1= segmentFeature(originFeature.maxActiveRadiusOnWeekend1, borderList);
        formatedOriginFeature.maxActiveRadiusOnWeekend1= (originFeature.maxActiveRadiusOnWeekend1);

        //shiftCitys0
        borderList = Arrays.asList(0.005,0.065,0.155,0.575,1.065,1.695,2.635,3.365,14.025,19.195,34.935,84.735);
        formatedOriginFeature.shiftCitys0= segmentFeature(originFeature.shiftCitys0, borderList);


        //activeDaysOfTopCity
        borderList = Arrays.asList(0.5,1.5,2.5,3.5,4.5,5.5,6.5,7.5,8.5,9.5,10.5,11.5,12.5,13.5,14.5,15.5,16.5,17.5,18.5,20.5,24.5,28.5,34.5,45.5,63.5,75.0,126.0,191.5,288.0,435.0);
//        formatedOriginFeature.activeDaysOfTopCity= segmentFeature(originFeature.activeDaysOfTopCity, borderList);

        formatedOriginFeature.activeDaysOfTopCity=originFeature.activeDaysOfTopCity;



















        return originFeature;

    }

    public static void cloneFeaturetoFormatedOriginFeature(OriginFeature originFeature,
            FormatedOriginFeature formatedOriginFeature) {
        formatedOriginFeature.keyid = originFeature.keyid;
        formatedOriginFeature.label = originFeature.label;
        formatedOriginFeature.activeDays = originFeature.activeDays;
//        formatedOriginFeature.activeWeeks = originFeature.activeWeeks;
//        formatedOriginFeature.activeDaysOfTopCity = originFeature.activeDaysOfTopCity;
        formatedOriginFeature.activeDaysOfEndCity = originFeature.activeDaysOfEndCity;
        formatedOriginFeature.gender = originFeature.gender;
        formatedOriginFeature.age = originFeature.age;
        formatedOriginFeature.platform = originFeature.platform;
        // formatedOriginFeature.citeNum=originFeature.citeNum;
        // formatedOriginFeature.areaNum=originFeature.areaNum;
//        formatedOriginFeature.shiftCitys0 = originFeature.shiftCitys0;
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
//        formatedOriginFeature.maxActiveRadiusOnWorkingDay0 = originFeature.maxActiveRadiusOnWorkingDay0;
//        formatedOriginFeature.maxActiveRadiusOnWorkingDay1 = originFeature.maxActiveRadiusOnWorkingDay1;
//        formatedOriginFeature.avgActiveRadiusOnWeekend0 = originFeature.avgActiveRadiusOnWeekend0;
//        formatedOriginFeature.avgActiveRadiusOnWeekend1 = originFeature.avgActiveRadiusOnWeekend1;
//        formatedOriginFeature.maxActiveRadiusOnWeekend0 = originFeature.maxActiveRadiusOnWeekend0;
//        formatedOriginFeature.maxActiveRadiusOnWeekend1 = originFeature.maxActiveRadiusOnWeekend1;
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


    public static void loadNewLabelMap(String fileName){
        try {
            LineIterator lineIterator = FileUtils.lineIterator(new File(fileName));
            while (lineIterator.hasNext()){
                String line = lineIterator.nextLine();
                String[] split = line.split("\t");
                if (split.length==2) {
                    newLableMap.put(split[0], split[1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
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
