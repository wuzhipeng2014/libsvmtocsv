import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Created by zhipengwu on 17-7-27. 将libsvm格式数据转换为csv
 */
public class LibsvmToCSV {
    public static Logger logger = LoggerFactory.getLogger(LibsvmToCSV.class);

    public static Map<String, Integer> featurNametoIndexMap;
    // 计算分类特征出现频次最高的条目
    public static Map<String, Integer> countCategoryFeatureMap = Maps.newHashMap();
    public static Map<String, Integer> countModelFeatureMap = Maps.newHashMap();
    public static Map<String, Integer> countCityFeatureMap = Maps.newHashMap();
    public static Double maxModel=1.0;
    public static Double maxCity=1.0;

    public static void main(String[] args) throws IOException {
        // String baseDir = "/home/zhipengwu/Innovation/BigBang/JavaTutorial/src/main/resources/";
        // // 输入文件名称参数
        // String inputFileName = "hotel_train_20170813.libsvm";
        // String libsvmDescFile = baseDir + "libsvm格式说明20170813.txt"; // libsvm列名文件

        String baseDir = args[0];
        // 输入文件名称参数
        String inputFileName = args[1];
        String libsvmDescFile = baseDir + args[2]; // libsvm列名文件
        boolean addHeader = Boolean.valueOf(args[3]);

        String outputFileName = inputFileName + ".csv";
        String inputFile = baseDir + inputFileName;
        String outputFile = baseDir + outputFileName;
        int dim = 29; // 转换后的维数
        FileWriter csvFile = new FileWriter(outputFile);
        String header = "Label,I1,I2,I3,I4,I5,I6,I7,I8,I9,I10,I11,I12,I13,I14,I15,I16,I17,I18,I19,I20,I21,I22,I23,I24,I25,I26,I27,I28,I29,I30,C1,C2,C3";
        if (addHeader) {
            csvFile.append(header + "\n");
            csvFile.flush();
        }
        Map<Integer, LibsvmDes> libsvmDesMap = loadLibsvmDes(libsvmDescFile);
        // csv文件中列名到列位置的映射
        featurNametoIndexMap = createfeaturNametoIndexMap(libsvmDescFile);

        // ===========输出csv文件各列名================
        FileWriter csv_colum_name_desc = new FileWriter(outputFile + ".desc");
        for (String key : featurNametoIndexMap.keySet()) {
            System.out.println(String.format("%s\t%s", key, featurNametoIndexMap.get(key)));
            csv_colum_name_desc.append(String.format("%s\t%s\n", key, featurNametoIndexMap.get(key)));
        }
        csv_colum_name_desc.flush();
        csv_colum_name_desc.close();

        //========统计分类特征(城市和model)出现频次======
        countCategoryFeatureFrequency(inputFile,libsvmDesMap, featurNametoIndexMap, dim);

        LineIterator lineIterator = FileUtils.lineIterator(new File(inputFile));
        while (lineIterator.hasNext()) {
            String line = lineIterator.nextLine();
            List<String> rowList = parseLine(line, libsvmDesMap, featurNametoIndexMap, dim);
            // ===========生成gbdt的输入特征==========
            rowList = createFeartureforGBDT(rowList);

            // ==========对特征中的某些列做归一化处理====
//            rowList = normalizeFeature(rowList);

            String csvrow = rowList.toString();

            csvrow = csvrow.substring(1, csvrow.length() - 1).replaceAll(" ", "");
            if (csvrow.endsWith(",")) {
                csvrow = csvrow.substring(0, csvrow.length() - 1);
            }
            csvFile.append(csvrow + "\n");
            // System.out.println(rowList.toString());
        }
        csvFile.flush();
        csvFile.close();
        lineIterator.close();

        FileWriter high_frequence = new FileWriter(outputFile + "_high_frequench.txt");

        // 获取分类特征中出现次数较高的值
        Map<String, Integer> stringIntegerMap = sortByValue(countCategoryFeatureMap);
        for (String key : stringIntegerMap.keySet()) {
            // System.out.println(key+"\t"+stringIntegerMap.get(key));
            int va=stringIntegerMap.get(key);
            if (va > 100) {
                high_frequence.append(key + "\t" + stringIntegerMap.get(key) + "\n");
            }
        }

    }

    public static List<String> parseLine(String line, Map<Integer, LibsvmDes> libsvmDesMap,
            Map<String, Integer> featurNametoIndexMap, int dim) {
        List<List<String>> rowslist = Lists.newArrayList();
        List<String> rowList = Lists.newArrayList();
        if (Strings.isNullOrEmpty(line)) {
            return null;
        }
        for (int i = 0; i < dim + 1; i++) {
            rowList.add("");
        }

        String[] split = line.split(" ");
        //
        for (String item : split) {
            int indexcolon = item.indexOf(":");
            if (indexcolon < 0) { // 第一列
                rowList.set(0, item);
                continue;
            }
            int index = Integer.valueOf(item.substring(0, indexcolon));

            Double value = Double.valueOf(item.substring(indexcolon + 1));
            if (value > 0 && index >= 0) {
                LibsvmDes libsvmDes = libsvmDesMap.get(index);
                if (libsvmDes != null && featurNametoIndexMap.containsKey(libsvmDes.featureName)) {
                    Integer featureIndex = featurNametoIndexMap.get(libsvmDes.featureName);
                    String pre_s = rowList.get(featureIndex);
                    if (Strings.isNullOrEmpty(pre_s)) {
                        rowList.set(featureIndex, String.format("%s", libsvmDes.featureValue.replaceAll(", ", "-")));
                    } else {
                        rowList.set(featureIndex,
                                String.format("%s#%s", pre_s, libsvmDes.featureValue.replaceAll(", ", "-")));
                    }
                }

            }
        }

        return rowList;
    }

    // 建立csv列名到列位置的映射
    public static Map<String, Integer> createfeaturNametoIndexMap(String libsvmDesFile) {
        Map<String, Integer> featurNametoIndexMap = Maps.newHashMap();
        int count = 1;
        try {
            LineIterator lineIterator = FileUtils.lineIterator(new File(libsvmDesFile));
            while (lineIterator.hasNext()) {
                String line = lineIterator.nextLine();
                String[] split = line.split("\t");
                if (split == null || split.length != 2) {
                    continue;
                }
                String desc = split[1];
                int indexBrace = desc.indexOf("[");
                if (indexBrace > 0) {
                    String featureName = desc.substring(0, indexBrace);
                    if (!Strings.isNullOrEmpty(featureName) && !featurNametoIndexMap.containsKey(featureName)) {
                        featurNametoIndexMap.put(featureName, count);
                        count++;
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            logger.error("加载文件失败" + libsvmDesFile);

        }
        return featurNametoIndexMap;

    }

    // 加载libsvm描述文件到map
    public static Map<Integer, LibsvmDes> loadLibsvmDes(String file) {
        Map<Integer, LibsvmDes> libsvmDesMap = Maps.newHashMap();

        try {
            LineIterator lineIterator = FileUtils.lineIterator(new File(file));
            while (lineIterator.hasNext()) {
                String line = lineIterator.nextLine();
                String[] split = line.split("\t");
                if (split == null || split.length != 2) {
                    continue;
                }
                int index = Integer.valueOf(split[0]);
                String desc = split[1];
                if (desc.contains("*")) {
                    continue;
                }
                String featureName = "";
                String featureValue = "";
                int indexBrace = desc.indexOf("[");
                if (indexBrace > 0) {
                    featureName = desc.substring(0, indexBrace);
                    featureValue = desc.substring(indexBrace);
                    libsvmDesMap.put(index, new LibsvmDes(index, featureName, featureValue));
                }
            }
        } catch (IOException e) {
            logger.error("加载文件失败" + file);
            e.printStackTrace();
        }
        return libsvmDesMap;

    }

    public static class LibsvmDes {
        public LibsvmDes() {
        }

        public LibsvmDes(int index, String featureName, String featureValue) {
            this.featureName = featureName;
            this.featureValue = featureValue;
            this.index = index;
        }

        public String featureName;
        public String featureValue;
        public int index;
    }

    // 为gbdt输入构建特征
    public static List<String> createFeartureforGBDT(List<String> rowList) throws IOException {
        DecimalFormat dcmFmt = new DecimalFormat("0.00");
        // platform, model, allCity 三列移到list的末尾
        Integer platformIndex = featurNametoIndexMap.get("platform");
        Integer modelIndex = featurNametoIndexMap.get("model");
        Integer allCityIndex = featurNametoIndexMap.get("allCity");
        String platform = rowList.get(platformIndex);
        String model = rowList.get(modelIndex);
        String allCity = rowList.get(allCityIndex);
        String[] cities = allCity.split("#");
        String formate_model = "model-" + rowList.get(modelIndex);
        String formate_allCity = "allCity-" + rowList.get(allCityIndex);
        // 统计用户所有移动的城市的热度之和手机机型热度
        addMap(formate_model,countCategoryFeatureMap);
        addMap(formate_allCity,countCategoryFeatureMap);

        for (int i = 0; i < rowList.size(); i++) {
            String s = rowList.get(i);
            String value = "-10";
            if (Strings.isNullOrEmpty(s)) {
                continue;
            }
            if (s.contains("[") && (s.contains("]") || s.contains(")"))) {
                s = s.substring(1, s.length() - 1);
            }
            // 处理范围类型的特征
            if (s.contains("-")) {
                String[] split = s.split("-");
                if (split.length == 2 && isNum(split[0])) {
                    if (split[1].equalsIgnoreCase(":")) {
                        value = split[0];
                    } else if (isNum(split[1])) {
                        String format = dcmFmt.format((Double.valueOf(split[1]) + Double.valueOf(split[0])) / 2);
                        // if (Math.abs(Float.valueOf(format))<1.0){
                        // format=String.valueOf( Float.valueOf(format)*100);
                        // }
                        if (Double.valueOf(format) > 20000) {
                            format = "-10";
                        }
                        value = String.valueOf(format);
                    }
                }
            }
            // 处理性别特征
            if (s.equalsIgnoreCase("F")) {
                value = "2";
            }
            if (s.equalsIgnoreCase("M")) {
                value = "1";
            }
            if (s.equalsIgnoreCase("X")) {
                value = "3";
            }

            // 处理数字特征
            if (isNum(s)) {
                if (Double.valueOf(s) > 200000) {
                    s = "-10";
                }
                value = s;
            }
            rowList.set(i, value);
        }

        //根据计算的城市和机型热度更新对应列
        if (countModelFeatureMap.get(model)>0) {
            rowList.set(modelIndex, dcmFmt.format((10*countModelFeatureMap.get(model))/maxModel));
        }
        rowList.add(MD5Util.getMd5(model));
        rowList.add(platform);
        rowList.add(model);
        rowList.add(allCity);

        Double citySum=0.0;
        int tempmaxCity=0;
        String tempmaxCityname="";
        for (String city : cities) {
            citySum+=countCityFeatureMap.get(city);
            if (countCityFeatureMap.get(city)>tempmaxCity){
                tempmaxCity=countCityFeatureMap.get(city);
                tempmaxCityname=city;
            }
        }
        rowList.set(allCityIndex,dcmFmt.format((citySum*10)/maxCity));
        rowList.set(platformIndex,MD5Util.getMd5(tempmaxCityname));
        return rowList;
    }

    public static boolean isNum(String str) {

        try {
            new BigDecimal(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        Map<K, V> result = new LinkedHashMap<>();
        Stream<Map.Entry<K, V>> st = map.entrySet().stream();

        st.sorted(Comparator.comparing(e -> e.getValue())).forEach(e -> result.put(e.getKey(), e.getValue()));

        return result;
    }

    // 对输入特征中的某些列做归一化处理
    public static List<String> normalizeFeature(List<String> rowList) {
        // 将分布比较集中的特征进行对数处理
        DecimalFormat dcmFmt = new DecimalFormat("0.00");
        String featureNames = "avgActiveRadiusOnWorkingDay0,avgActiveRadiusOnWorkingDay1,maxActiveRadiusOnWeekend1,maxActiveRadiusOnWeekend0,shiftCitysOnWeekend0,shiftCitysOnWeekend1,avgActiveRadiusOnWeekend1,avgActiveRadiusOnWeekend0,maxActiveRadiusOnWorkingDay0,maxActiveRadiusOnWorkingDay1,maxActiveRadius0,maxActiveRadius1,avgActiveRadius0,avgActiveRadius1,shiftCitysOnWorkingDay1,shiftCitysOnWorkingDay0,shiftCitys0";
        String[] features = featureNames.split(",");
        for (String feature : features) {
            Integer index = featurNametoIndexMap.get(feature);
            if (index > -1 && index < rowList.size()) {
                String value = rowList.get(index);
                if (isNum(value) && Double.valueOf(value) > 0) {
                    String format = dcmFmt.format(Math.log1p(Double.valueOf(value) * 10));

                    int s = (int) (Double.valueOf(format) * 100);

                    String result = String.valueOf(s);
                    rowList.set(index, result);
                }

            }
        }
        return rowList;
    }

    public static void addMap(String key, Map<String, Integer> map) {
        if (Strings.isNullOrEmpty(key) || map == null) {
            return;
        }
        if (map.containsKey(key)) {
            Integer integer = map.get(key);
            map.put(key, integer + 1);
        } else {
            map.put(key, 1);
        }
    }

    //统计分类特征分别出现的次数
    public static void countCategoryFeatureFrequency(String inputFile,Map<Integer, LibsvmDes> libsvmDesMap,
                                                     Map<String, Integer> featurNametoIndexMap, int dim){
        LineIterator lineIterator = null;
        try {
            lineIterator = FileUtils.lineIterator(new File(inputFile));
            while (lineIterator.hasNext()) {
                String line = lineIterator.nextLine();
                List<String> rowList = parseLine(line, libsvmDesMap, featurNametoIndexMap, dim);
                Integer platformIndex = featurNametoIndexMap.get("platform");
                Integer modelIndex = featurNametoIndexMap.get("model");
                Integer allCityIndex = featurNametoIndexMap.get("allCity");
                String platform = rowList.get(platformIndex);
                String model = rowList.get(modelIndex);
                String allCity = rowList.get(allCityIndex);
                String[] cities = allCity.split("#");
                // 统计用户所有移动的城市的热度之和手机机型热度
                addMap(model, countModelFeatureMap);
                if (countModelFeatureMap.get(model)>maxModel){
                    maxModel=Double.valueOf(countModelFeatureMap.get(model));
                }
                for (String city : cities) {
                    addMap(city, countCityFeatureMap);
                    if (countCityFeatureMap.get(city)>maxCity){
                        maxCity=Double.valueOf(countCityFeatureMap.get(city));
                    }
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }




}
