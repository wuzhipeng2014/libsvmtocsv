package toutiao.Feature;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import toutiao.Feature.Bean.BasicFeature;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by zhipengwu on 17-10-11.
 */
public class Preprocessing {
    public static void main(String[] args) {
        String basicFeatureIntputFile = "/home/zhipengwu/secureCRT/hotel_feature_20170926_10-10_01.txt";
        String basicFeatureOutputFile = "/home/zhipengwu/secureCRT/hotel_feature_20170926_10-10_01.csv";
        String headerName = "Label, activeDays, activeDaysOfTopCity, cityNum, areaNum, shiftAreas, weekMaxshiftAreas, monthMaxshiftAreas, averageWeekshiftAreas, averageMonthshiftAreas, shiftAreaNumRatio, midShiftAreaNum, phoneLevel, residentCityLevel";

        String header = "";
        boolean firstLine = true;

        try {
            LineIterator lineIterator = FileUtils.lineIterator(new File(basicFeatureIntputFile));
            FileWriter fw = new FileWriter(basicFeatureOutputFile);
            while (lineIterator.hasNext()) {
                String line = lineIterator.nextLine();
                if (firstLine) {
                    header = generateHeader(line);
                    fw.append(header + "\n");
                    firstLine = false;
                }
                BasicFeature basicFeature = BasicFeature.convertToBasicFeature(line);
                //todo 将部分特征进行分段处理

                if (basicFeature != null) {
                    String csvFormateRow = basicFeature.toCSVFormateRow();
                    fw.append(csvFormateRow + "\n");
                }
            }
            lineIterator.close();
            fw.flush();
            fw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static String generateHeader(String line) {
        String[] split = line.split("\t");
        int length = split.length;
        StringBuilder result = new StringBuilder("Label");
        int i = 0;
        while (++i < length-1) {
            result.append(",I" + i);
        }
        return result.toString();

    }

}
