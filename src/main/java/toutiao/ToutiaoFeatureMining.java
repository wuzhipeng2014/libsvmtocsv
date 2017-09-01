package toutiao;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.qunar.mobile.innovation.histories.UserHistoryInfo;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import toutiao.bean.FeatureResult;
import toutiao.bean.FormatedFeatureResult;
import toutiao.bean.ToutiaoUserBehavior;
import toutiao.util.CalculateFeature;
import toutiao.util.FeatureUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Created by zhipengwu on 17-8-24.
 */
public class ToutiaoFeatureMining {

    public static void main(String[] args) {
        Gson gson = new GsonBuilder().create();
        // String phonePriceFile="/home/q/zhipeng.wu/logData/toutiao/train/phone_price_result_20170823.txt";

        String inputfileName = "/home/zhipengwu/secureCRT/toutiao_hotel_behavior_test_20170822.txt";
        String outputFileName = String.format("%s.csv", inputfileName);
        String phonePriceFile = "/home/zhipengwu/work/toutiao/Libsvmtocsv/src/main/resources/toutiao/phone_price_result_20170823.txt";
        String cityLevelFile = "/home/zhipengwu/work/toutiao/Libsvmtocsv/src/main/resources/toutiao/city_level.txt";

        boolean appendHeader = false;
        CalculateFeature.loadPhonePrice(phonePriceFile);
        CalculateFeature.calculateCityFrequency(inputfileName);
        CalculateFeature.loadCityLevel(cityLevelFile);

        // String
        // line="{\"commParams\":{\"keyId\":\"9eda907a57ec80c2582c38b10d4e4bba\",\"gender\":\"M\",\"model\":\"PE-TL10\",\"platform\":\"adr\",\"residentCity\":\"北京\",\"uid\":\"864601029571270\",\"vid\":\"60001173\",\"gid\":\"2529CC8B-135F-D0F7-23BB-0274A25BA597\",\"userName\":\"vtzcqlq9922\",\"pid\":\"10010\",\"cid\":\"C1012\",\"age\":50},\"areas\":[{\"avgLon\":116.377,\"avgLat\":39.881,\"nowCity\":\"北京\",\"locations\":{\"116.379,39.933\":{\"2017060817\":1},\"116.378,39.929\":{\"2017071807\":1},\"116.371,39.847\":{\"2017062821\":1},\"116.418,39.917\":{\"2017072907\":1},\"116.372,39.857\":{\"2017072011\":1},\"116.380,39.932\":{\"2017062012\":1},\"116.381,39.933\":{\"2017062607\":1,\"2017062714\":1,\"2017072016\":1,\"2017080600\":1},\"116.374,39.878\":{\"2017062115\":1},\"116.374,39.899\":{\"2017061017\":1,\"2017061121\":1},\"116.368,39.838\":{\"2017062115\":1,\"2017062214\":1},\"116.374,39.846\":{\"2017073021\":1},\"116.369,39.838\":{\"2017061613\":1,\"2017060713\":1,\"2017071022\":2,\"2017072215\":1,\"2017072321\":3,\"2017070613\":1,\"2017071415\":1,\"2017070715\":1,\"2017072416\":1,\"2017071714\":1,\"2017061322\":1,\"2017061401\":1,\"2017070908\":1,\"2017080119\":1,\"2017080407\":1,\"2017080906\":1},\"116.371,39.854\":{\"2017081023\":1},\"116.369,39.837\":{\"2017070413\":1,\"2017071915\":2,\"2017072513\":1,\"2017073109\":1,\"2017072114\":1,\"2017072611\":1,\"2017063017\":1,\"2017070323\":1,\"2017060908\":1,\"2017080121\":1,\"2017080217\":1,\"2017081421\":1,\"2017081622\":1,\"2017082221\":2}}},{\"avgLon\":118.836,\"avgLat\":45.711,\"nowCity\":\"锡林郭勒\",\"locations\":{\"118.836,45.711\":{\"2017070311\":1}}},{\"avgLon\":116.614,\"avgLat\":40.059,\"nowCity\":\"北京\",\"locations\":{\"116.616,40.052\":{\"2017061121\":1,\"2017061222\":1},\"116.613,40.067\":{\"2017061915\":1}}},{\"avgLon\":87.606,\"avgLat\":43.822,\"nowCity\":\"乌鲁木齐\",\"locations\":{\"87.606,43.822\":{\"2017071012\":1}}},{\"avgLon\":112.712,\"avgLat\":32.063,\"nowCity\":\"襄阳\",\"locations\":{\"112.712,32.063\":{\"2017080813\":1}}},{\"avgLon\":116.468,\"avgLat\":39.769,\"nowCity\":\"北京\",\"locations\":{\"116.468,39.769\":{\"2017082115\":2}}}],\"cites\":{\"锡林郭勒\":{\"activeAreas\":1,\"activeDays\":1,\"frequency\":1},\"乌鲁木齐\":{\"activeAreas\":1,\"activeDays\":1,\"frequency\":1},\"襄阳\":{\"activeAreas\":1,\"activeDays\":1,\"frequency\":1},\"北京\":{\"activeAreas\":3,\"activeDays\":47,\"frequency\":57}},\"hasHotelOrder\":1,\"hasTrafficOrder\":0,\"behaviors\":{\"HOTEL\":{\"__rT\":\"UserHotelHistories\",\"pvList\":[],\"clickList\":[],\"bookList\":[],\"orderList\":[{\"orderId\":\"101029648864\",\"hotelSeq\":\"qionghai_2199\",\"cityName\":\"琼海\",\"checkInDate\":\"20161223\",\"checkOutDate\":\"20161224\",\"score\":4.2,\"stars\":\"NOSTAR\",\"level\":\"four\",\"types\":[\"BUSINESS_HOTEL\",\"CHAIN_HOTEL\",\"RESORT_HOTEL\"],\"busi\":\"博鳌镇\",\"brand\":\"huameida\",\"price\":268.0,\"userCoordinate\":{\"x\":18.245794,\"y\":109.49911,\"z\":NaN},\"distance\":156618.3135303691,\"userCityName\":\"三亚\",\"actionTime\":\"20161223
        // 07:55:00.000\"},{\"orderId\":\"101029579759\",\"hotelSeq\":\"sanya_6637\",\"cityName\":\"三亚\",\"checkInDate\":\"20161224\",\"checkOutDate\":\"20161225\",\"score\":4.4,\"stars\":\"NOSTAR\",\"level\":\"four\",\"types\":[\"BUSINESS_HOTEL\",\"RESORT_HOTEL\"],\"busi\":\"海棠湾\",\"brand\":\"\",\"price\":359.0,\"userCoordinate\":{\"x\":18.246345,\"y\":109.497962,\"z\":NaN},\"distance\":30810.61049184764,\"userCityName\":\"三亚\",\"actionTime\":\"20161223
        // 08:21:04.000\"}],\"favoritesList\":[]}}}";
        String line = "";
        try {
            LineIterator lineIterator = FileUtils.lineIterator(new File(inputfileName));
            FileWriter fw = new FileWriter(outputFileName);
            if (appendHeader) {
                fw.append(FeatureResult.header + "\n");
            }
            while (lineIterator.hasNext()) {
                line = lineIterator.nextLine();
                FeatureResult featureResult = new FeatureResult();
                ToutiaoUserBehavior toutiaoUserBehavior = UserHistoryInfo.GSON.fromJson(line,
                        ToutiaoUserBehavior.class);
                CalculateFeature.getAvgShiftCityNum(toutiaoUserBehavior, featureResult);
                CalculateFeature.getAge(toutiaoUserBehavior, featureResult);
                CalculateFeature.getPhoneLevel(toutiaoUserBehavior, featureResult);
                CalculateFeature.getresidentCity(toutiaoUserBehavior, featureResult);
                CalculateFeature.getshiftCityname(toutiaoUserBehavior, featureResult);
                CalculateFeature.getShiftCityTotalNum(toutiaoUserBehavior, featureResult);

                FormatedFeatureResult formatedFeatureResult=new FormatedFeatureResult();
                FeatureUtil.ConverFreatureResult(featureResult);


                FeatureUtil.cloneFeaturetoFormatedFeatureResult(featureResult, formatedFeatureResult);
                FeatureUtil.convertmajorFeaturetoVector(featureResult, formatedFeatureResult);


                // TODO: 17-8-31
                String result = formatedFeatureResult.toString();
//                String result = featureResult.toString();
//                String result = featureResult.printStringByLabel("0");

                if (!Strings.isNullOrEmpty(result)) {
                    fw.append(result + "\n");
                    fw.flush();
                }
            }
            fw.close();
            writeHighFrequencyCity(inputfileName + ".highFrequency");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void writeHighFrequencyCity(String fileName) {
        // 获取分类特征中出现次数较高的值
        try {
            FileWriter fw = new FileWriter(fileName);

            Map<String, Integer> stringIntegerMap = sortByValue(CalculateFeature.countCityFrequency);
            for (String key : stringIntegerMap.keySet()) {
                // System.out.println(key+"\t"+stringIntegerMap.get(key));
                int va = stringIntegerMap.get(key);
                if (va > 1) {
                    fw.append(key + "\n");
                }
            }
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        Map<K, V> result = new LinkedHashMap<>();
        Stream<Map.Entry<K, V>> st = map.entrySet().stream();

        st.sorted(Comparator.comparing(e -> e.getValue())).forEach(e -> result.put(e.getKey(), e.getValue()));

        return result;
    }

}
