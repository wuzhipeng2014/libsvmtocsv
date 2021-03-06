package toutiao.util;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.qunar.mobile.innovation.histories.UserHistoryInfo;
import mobike.util.GeoDistance;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import toutiao.bean.Coordinate;
import toutiao.bean.FeatureResult;
import toutiao.bean.ToutiaoUserBehavior;
import toutiao.bean.UserAreas;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by zhipengwu on 17-8-24.
 */
public class CalculateFeature {
    public static DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyyMMdd");
    public static DecimalFormat dcmFmt = new DecimalFormat("0.00");
    public static Map<String, String> modelPriceMap = Maps.newHashMap();
    public static Map<String, String> cityLevelMap = Maps.newHashMap();
    public static Map<String, String> avgDGPMap = Maps.newHashMap();

    public static String defaultValue = "0";
    public static int maxCityFrequency=1;
    // 日志中城市出现次数
    public static Map<String, Integer> countCityFrequency = Maps.newHashMap();

    // I1.移动城市城市总数
    public static void getShiftCityTotalNum(ToutiaoUserBehavior toutiaoUserBehavior, FeatureResult featureResult) {
        int size = toutiaoUserBehavior.cites.size();
        featureResult.shiftCityTotalNum=String.valueOf(size);
//        featureResult.shiftCityTotalNum=String.valueOf(size+Integer.valueOf(featureResult.predictMissShiftCityNum));
    }

    // I2.一天(相邻两天)内移动城市个数 (隔天)平均值(中值), I3.最大值, I5.及城市间移动的总次数
    public static void getAvgShiftCityNum(ToutiaoUserBehavior toutiaoUserBehavior, FeatureResult featureResult) {
        List<UserAreas.Area> areas = toutiaoUserBehavior.areas;
        int[] shiftCityNum = new int[2];
        // 按天存储移动的城市名称
        Map<String, Set<String>> cityCollectByDayMap = Maps.newHashMap();
        // 按天存储经过的各区域
        Map<String, Set<Coordinate>> posCollectByDayMap = Maps.newHashMap();
        // 按天存储每天移动区域个数
        Map<String, Integer> countShiftAreaByDayMap = Maps.newHashMap();

        // 城市间移动次数统计
        for (UserAreas.Area area : areas) {
            String nowCity = area.nowCity;
            for (String posKey : area.locations.keySet()) {
                Map<String, Integer> stringIntegerMap = area.locations.get(posKey);
                for (String date : stringIntegerMap.keySet()) {
                    String formateDate = date.substring(0, 8);
                    DateTime dateTime = fmt.parseDateTime(formateDate);
                    String nextDay = dateTime.plusDays(1).toString("yyyyMMdd");
                    putMap(formateDate, nowCity, cityCollectByDayMap);
                    putMap(nextDay, nowCity, cityCollectByDayMap);

                    addMap(formateDate, countShiftAreaByDayMap);

                    Coordinate coordinate = new Coordinate();
                    String[] posValue = posKey.split(",");
                    coordinate.lng = Double.valueOf(posValue[0]);
                    coordinate.lat = Double.valueOf(posValue[1]);
                    putMap(formateDate, coordinate, posCollectByDayMap);
                    putMap(nextDay, coordinate, posCollectByDayMap);

                }
            }
        }
        int maxShiftCityNum = 0;
        int avgShiftCityNum = 0;
        int tmpcount = 0;
        int mapsize = cityCollectByDayMap.size();
        int midIndex = (int) Math.floor(mapsize / 2);
        int i = 0;
        List<Integer> list = Lists.newArrayList();
        for (String key : cityCollectByDayMap.keySet()) {
            list.add(cityCollectByDayMap.get(key).size());
        }
        list.sort(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }
        });
        // 计算一天内移动城市次数的最大值
        featureResult.maxShiftCityNum = String.valueOf(list.get(mapsize - 1));
        // 计算一天内移动城市的均值
        featureResult.avgShiftCityNum = String.valueOf(list.get(midIndex));

        // 计算跨市移动总次数(总|周末|工作日)
        countShiftCityTimes(cityCollectByDayMap, featureResult);

        List<Double> distanceList = Lists.newArrayList();
        List<String> distanceCrspdDateList=Lists.newArrayList();
        // 每日移动区域半径
        for (String date : posCollectByDayMap.keySet()) {
            Set<Coordinate> coordinates = posCollectByDayMap.get(date);
            List<Coordinate> coordinateList = Lists.newArrayList();
            coordinateList.addAll(coordinates);
            Double tmpMaxActiveRadius = 1000.0;
            // 当天最大活跃半径
            for (int j = 0; j < coordinateList.size(); j++) {
                Coordinate coordinate1 = coordinateList.get(j);
                for (int k = j; k < coordinateList.size(); k++) {
                    Coordinate coordinate2 = coordinateList.get(k);
                    double distance = GeoDistance.getDistance(coordinate1.lat, coordinate1.lng, coordinate2.lat,
                            coordinate2.lng);
                    if (distance > tmpMaxActiveRadius) {
                        tmpMaxActiveRadius = distance;
                    }
                }
            }
            if (tmpMaxActiveRadius > 1000) {
                distanceList.add(tmpMaxActiveRadius);
                distanceCrspdDateList.add(date);
            }
        }
        distanceList.sort(new Comparator<Double>() {
            @Override
            public int compare(Double o1, Double o2) {
                return o1.compareTo(o2);
            }
        });
        int size = distanceList.size();
        if (size > 0) {

            // 单天活跃最大半径
            featureResult.maxDayActiveRadius = String.valueOf(dcmFmt.format(Math.log1p(distanceList.get(size - 1))));
            // 单天活跃平均半径
            featureResult.avgDayActiveRadius = String
                    .valueOf(dcmFmt.format(Math.log1p(distanceList.get((int) Math.floor(size / 2)))));
            // 单天活跃半径差异度
            double v = distanceList.get(size - 1) / distanceList.get((int) Math.floor(size / 2));
            featureResult.DayActiveRadiusRatio = dcmFmt.format(v);

        } else {
            featureResult.maxDayActiveRadius = defaultValue;
            featureResult.avgDayActiveRadius = defaultValue;
            featureResult.DayActiveRadiusRatio = defaultValue;
        }

        //todo 节假日平均移动半径|工作日平均移动半径|节假日平均移动半径/工作日平均移动半径 比值
        calculateAvgClassifiActiveRadius(distanceList,distanceCrspdDateList,featureResult);

        List<Integer> countAreabyDayList = Lists.newArrayList();
        countAreabyDayList.addAll(countShiftAreaByDayMap.values());
        countAreabyDayList.sort(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }
        });
        int countAreabyDayListSize = countAreabyDayList.size();
        // 移动区域总数
        featureResult.shiftAreaToalNum = String.valueOf(areas.size());

        // 每日移动区域个数均值
        featureResult.avgShiftAreaNum = String
                .valueOf(countAreabyDayList.get((int) Math.floor(countAreabyDayListSize / 2)));

        //todo 节假日|工作日 每日移动区域个数及比值
        calculateActiveArea(toutiaoUserBehavior, featureResult);



        // 头条活跃天数
        featureResult.toutiaoActiveDayNum = String.valueOf(cityCollectByDayMap.size());

    }

    // I15 手机等级
    public static void getPhoneLevel(ToutiaoUserBehavior toutiaoUserBehavior, FeatureResult featureResult) {
        String model = toutiaoUserBehavior.commParams.model;
        String price = modelPriceMap.get(model);
        if (Strings.isNullOrEmpty(price) || !isNum(price)) {
            price = "999";
        }
        double ceil = Math.ceil(Double.valueOf(price) / 500);
        featureResult.phoneLevel = String.valueOf(ceil);
    }

    // I17 age,gender,platform
    public static void getAge(ToutiaoUserBehavior toutiaoUserBehavior, FeatureResult featureResult) {
        featureResult.age = String.valueOf(toutiaoUserBehavior.commParams.age);
        String gender = toutiaoUserBehavior.commParams.gender;
        if (gender.equalsIgnoreCase("M")) {
            featureResult.gender = String.valueOf(1);
        } else if (gender.equalsIgnoreCase("F")) {
            featureResult.gender = String.valueOf(2);
        } else {
            featureResult.gender = String.valueOf(3);

        }
        String platform = toutiaoUserBehavior.commParams.platform.toString();
        if (platform.equalsIgnoreCase("adr")) {
            featureResult.platform = String.valueOf(1);

        } else if (platform.equalsIgnoreCase("ios")) {
            featureResult.platform = String.valueOf(2);

        } else {
            featureResult.platform = String.valueOf(3);

        }
        featureResult.Lable = String.valueOf(toutiaoUserBehavior.hasHotelOrder);
        featureResult.keyid = toutiaoUserBehavior.commParams.keyId;

    }

    // C20. 到达过的城市名称
    public static void getshiftCityname(ToutiaoUserBehavior toutiaoUserBehavior, FeatureResult featureResult) {
        Set<String> cities = toutiaoUserBehavior.cites.keySet();

        String joinCities = Joiner.on("##").skipNulls().join(cities);
        featureResult.shiftCityname = joinCities;

        // 计算日志中城市出现热度
        addMap(String.format("C1-%s", joinCities), countCityFrequency);
        int sumFrequency=0;
        for (String city:cities){
            Integer frequency = countCityFrequency.get(city);
            sumFrequency+=frequency;
        }
        featureResult.shfitCityTotalHeat=dcmFmt.format((double)sumFrequency/maxCityFrequency);

    }

    // C21. 常住地
    public static void getresidentCity(ToutiaoUserBehavior toutiaoUserBehavior, FeatureResult featureResult) {
        Map<String, UserAreas.CityInfo> cites = toutiaoUserBehavior.cites;
        int maxCityFrequency = 0;
        String residentCity = "";
        for (String cityName : cites.keySet()) {
            UserAreas.CityInfo cityInfo = cites.get(cityName);
            if (cityInfo.frequency > maxCityFrequency) {
                maxCityFrequency = cityInfo.frequency;
                residentCity = cityName;
            }
        }
        featureResult.residentCity = residentCity;
        //常住地城市所属等级
        String cityLevel = cityLevelMap.get(residentCity);
        if (Strings.isNullOrEmpty(cityLevel)){
            cityLevel="4";
        }
        //常住地城市人均gdp
        String avgGDP = avgDGPMap.get(residentCity);
        if (Strings.isNullOrEmpty(avgGDP)){
            avgGDP="20";
        }
        featureResult.avgGDP=avgGDP;
        //常住地之外城市消费能力
        int spendingPower=0;
        for (String cityName : cites.keySet()) {
            if (!Strings.isNullOrEmpty(cityName)&&!cityName.equalsIgnoreCase(residentCity)){
                String cityGdp = avgDGPMap.get(cityName);
                if (Strings.isNullOrEmpty(cityGdp)){
                    cityGdp="20";
                }
                if (isNum(cityGdp)){
                    spendingPower+=Integer.valueOf(cityGdp);
                }
            }
        }

        featureResult.spendingPower=String.valueOf(spendingPower+(Integer.valueOf(featureResult.predictMissShiftCityNum)*30));





        featureResult.residentCityLevel=cityLevel;
    }

    // 计算跨市移动总次数
    public static void countShiftCityTimes(Map<String, Set<String>> cityCollectByDayMap, FeatureResult featureResult) {
        // 跨市移动总次数
        int shiftCityCount = 0;
        // 周末跨市移动总次数
        Double weekendShiftCityCount = 0.0;
        // 工作日跨市移动总次数
        Double workdayShiftCityCount = 0.0;

        Set<String> dates = cityCollectByDayMap.keySet();
        List<String> dateList = Lists.newArrayList();
        dateList.addAll(dates);
        dateList.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        List<String> shiftCityByDayList = Lists.newArrayList();
        List<String> shiftCityCorrpdDateList = Lists.newArrayList();
        for (String date : dateList) {
            Set<String> citiesSet = cityCollectByDayMap.get(date);
            shiftCityByDayList.addAll(citiesSet);
            int size = citiesSet.size();
            for (int i = 0; i < size; i++) {
                shiftCityCorrpdDateList.add(date);
            }
        }
        // 计算跨市移动总次数(shiftCityByDayList中相邻两个城市名称不同的个数)
        for (int i = 1; i < shiftCityByDayList.size(); i++) {
            if (!shiftCityByDayList.get(i).equalsIgnoreCase(shiftCityByDayList.get(i - 1))) {
                shiftCityCount++; // 跨市移动总次数
                String date = shiftCityCorrpdDateList.get(i);
                DateTime dateTime = fmt.parseDateTime(date);
                int dayOfWeek = dateTime.getDayOfWeek();
                if (dayOfWeek > 5) { // 周末
                    weekendShiftCityCount++;
                } else {
                    workdayShiftCityCount++;
                }
            }
        }
        // 跨市移动总次数
        featureResult.shiftCityTotal = String.valueOf(shiftCityCount);
        // 工作日跨市移动总次数
        featureResult.workdayShiftCityCount = String.valueOf(workdayShiftCityCount);
        // 周末跨市移动总次数
        featureResult.weekendShiftCityCount = String.valueOf(weekendShiftCityCount);
        // 工作日/节假日开始移动次数比值
        if (weekendShiftCityCount > workdayShiftCityCount) {
            if (workdayShiftCityCount == 0) {
                workdayShiftCityCount = 0.5;
            }
            featureResult.weekendShiftCityCountRatio = String
                    .valueOf(dcmFmt.format((double) weekendShiftCityCount / workdayShiftCityCount));
        } else {
            if (weekendShiftCityCount == 0) {
                weekendShiftCityCount = 0.5;
            }
            featureResult.weekendShiftCityCountRatio = String
                    .valueOf(dcmFmt.format((double) workdayShiftCityCount / weekendShiftCityCount));
        }
    }


    //节假日|工作日平均移动半径
    public static void calculateAvgClassifiActiveRadius(List<Double> distanceList,List<String> distanceCrspdDateList,FeatureResult featureResult){
        Double sumWeekendRadius=0.0;
        Double sumWorkdayRadius=0.0;
        int countWeekendDayNum=0;
        int countWorkDayNum=0;

        for (int i = 0; i < distanceCrspdDateList.size(); i++) {
            String date = distanceCrspdDateList.get(i);
            DateTime dateTime = fmt.parseDateTime(date);
            int dayOfWeek = dateTime.getDayOfWeek();
            if (dayOfWeek>5){
                sumWeekendRadius+=distanceList.get(i);
                countWeekendDayNum++;
            }else {
                sumWorkdayRadius+=distanceList.get(i);
                countWorkDayNum++;
            }
        }
        if (countWorkDayNum>0){
            featureResult.avgWorkdayActiveRadius=dcmFmt.format(Math.log1p(sumWorkdayRadius/countWorkDayNum));
        }else {
            featureResult.avgWorkdayActiveRadius=defaultValue;
        }
        if (countWeekendDayNum>0){
            featureResult.avgWeekendActiveRadius=dcmFmt.format(Math.log1p(sumWeekendRadius/countWeekendDayNum));
        }else {
            featureResult.avgWeekendActiveRadius=defaultValue;
        }
        if (!featureResult.avgWorkdayActiveRadius.equalsIgnoreCase(defaultValue)&&!featureResult.avgWeekendActiveRadius.equalsIgnoreCase(defaultValue)){
            featureResult.avgActiveRadiusRatio=dcmFmt.format(Math.exp(Double.valueOf( featureResult.avgWeekendActiveRadius))/Math.exp(Double.valueOf(featureResult.avgWorkdayActiveRadius)));
        }else {
            featureResult.avgActiveRadiusRatio=defaultValue;
        }

    }

    //todo 节假日|工作日 移动区域个数
    public static void calculateActiveArea(ToutiaoUserBehavior toutiaoUserBehavior, FeatureResult featureResult){
        Map<String,Set<String>> dateCoordinateMap=Maps.newHashMap();
        List<UserAreas.Area> areas = toutiaoUserBehavior.areas;
        for (UserAreas.Area area:areas){
            String avgcoordinate=String.format("%s,%s",area.avgLat,area.avgLon);
            Map<String, Map<String, Integer>> locations = area.locations;
            for (String coordinate:locations.keySet()){
                Map<String, Integer> dateCountMap = locations.get(coordinate);
                for (String date:dateCountMap.keySet()){
                    String formatDate=date.substring(0,8);
                    putMap(formatDate,avgcoordinate,dateCoordinateMap);
                }
            }
        }

        Double countWeekendShiftArea=0.0;
        Double countWorkdayShiftArea=0.0;

        int countWeekenddayNum=0;
        int countWorkdayNum=0;
        //节假日|工作日移动区域个数
        for (String date:dateCoordinateMap.keySet()){
            DateTime dateTime = fmt.parseDateTime(date);
            int dayOfWeek = dateTime.getDayOfWeek();
            if (dayOfWeek > 5) { // 周末
                countWeekendShiftArea+=dateCoordinateMap.get(date).size();
                countWeekenddayNum++;
            }else {
                countWorkdayShiftArea+=dateCoordinateMap.get(date).size();
                countWorkdayNum++;
            }
        }


        //节假日移动区域个数
        if (countWeekenddayNum>0&&countWeekendShiftArea>0) {
            featureResult.avgWeekendShiftAreaNum = dcmFmt.format(countWeekendShiftArea/countWeekenddayNum);
            featureResult.weekendShiftAreaNum=String.valueOf(countWeekendShiftArea);
        }else {
            featureResult.weekendShiftAreaNum =defaultValue;
            featureResult.avgWeekendShiftAreaNum=defaultValue;
        }
        //工作日移动区域个数
        if (countWeekenddayNum>0&&countWorkdayShiftArea>0) {
            featureResult.avgWorkdayShiftAreaNum = dcmFmt.format(countWorkdayShiftArea/countWorkdayNum);
            featureResult.workdayShiftAreaNum=String.valueOf(countWorkdayShiftArea);
        }else {
            featureResult.avgWorkdayShiftAreaNum=defaultValue;
            featureResult.workdayShiftAreaNum=defaultValue;
        }
        //工作日|节假日移动区域个数比值
        if (!featureResult.weekendShiftAreaNum.equalsIgnoreCase(defaultValue)&&!featureResult.workdayShiftCityCount.equalsIgnoreCase(defaultValue)&&countWorkdayShiftArea>0){
            featureResult.shiftAreaNumRatio=dcmFmt.format(countWeekendShiftArea/countWorkdayShiftArea);
        }else {
            featureResult.shiftAreaNumRatio=defaultValue;
        }



    }



    public static <T> void putMap(String key, T value, Map<String, Set<T>> map) {
        if (map.containsKey(key)) {
            map.get(key).add(value);
        } else {
            Set<T> set = Sets.newHashSet();
            set.add(value);
            map.put(key, set);
        }
    }

    public static void addMap(String key, Map<String, Integer> map) {
        if (map.containsKey(key)) {
            Integer integer = map.get(key);
            map.put(key, integer + 1);
        } else {
            map.put(key, 1);
        }
    }

    public static boolean isNum(String str) {

        try {
            new BigDecimal(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    /**
     * 加载城市等级
     * @param fileName
     */
    public static void loadCityLevel(String fileName){
        try {
            LineIterator lineIterator = FileUtils.lineIterator(new File(fileName));
            while (lineIterator.hasNext()) {
                String line = lineIterator.nextLine();
                if (!Strings.isNullOrEmpty(line)) {
                    String[] split = line.split(",");
                    if (split.length == 2) {
                        String city = split[0];
                        String level = split[1];
                        cityLevelMap.put(city, level);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //加载常住地城市人均gdp
    public static void loadAvgGDP(String fileName){
        try {
            LineIterator lineIterator = FileUtils.lineIterator(new File(fileName));
            while (lineIterator.hasNext()) {
                String line = lineIterator.nextLine();
                if (!Strings.isNullOrEmpty(line)) {
                    String[] split = line.split("\t");
                    if (split.length == 2) {
                        String city = split[0];
                        String gdp = split[1];
                        if (isNum(gdp)){
                            gdp=String.valueOf(Integer.valueOf(gdp)/1000);
                            city=city.substring(0,city.length()-1);
                            avgDGPMap.put(city, gdp);
                        }

                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载手机价格到map
     *
     * @param phonePriceFile
     */
    public static void loadPhonePrice(String phonePriceFile) {
        try {
            LineIterator lineIterator = FileUtils.lineIterator(new File(phonePriceFile));
            while (lineIterator.hasNext()) {
                String line = lineIterator.nextLine();
                if (!Strings.isNullOrEmpty(line)) {
                    String[] split = line.split(",");
                    if (split.length == 3) {
                        String model = split[1];
                        String price = split[2];
                        String modelKey = String.format("%s", model);
                        modelPriceMap.put(modelKey, price);
                    }
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void calculateCityFrequency(String inputFileName) {
        try {
            LineIterator lineIterator = FileUtils.lineIterator(new File(inputFileName));
            String line = "";
            while (lineIterator.hasNext()) {
                line = lineIterator.nextLine();
                FeatureResult featureResult = new FeatureResult();
                ToutiaoUserBehavior toutiaoUserBehavior = UserHistoryInfo.GSON.fromJson(line,
                        ToutiaoUserBehavior.class);
                for (String city : toutiaoUserBehavior.cites.keySet()) {
                    addMap(city, countCityFrequency);
                    if (countCityFrequency.get(city)>maxCityFrequency){
                        maxCityFrequency=countCityFrequency.get(city);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
