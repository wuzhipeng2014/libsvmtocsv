package toutiao.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.qunar.mobile.innovation.histories.UserHistoryInfo;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import toutiao.bean.ToutiaoUserBehavior;
import toutiao.bean.UserAreas;
import toutiao.kmeans.k_means;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by zhipengwu on 17-9-18.
 */
public class UserBehaviorAnalysis {

    public static void addMap(Map<Integer, Integer> map, Integer key) {
        if (map.containsKey(key)) {
            Integer integer = map.get(key);
            map.put(key, integer + 1);
        } else {
            map.put(key, 1);
        }

    }

    public static Double getAvgofList(List<Integer> list, int denominator) {
        Double sum = 0.0;
        for (Integer i : list) {
            sum += i;
        }
        if (denominator > 0) {
            return sum / denominator;
        } else {
            return sum;
        }
    }

    public static ArrayList<float[]> formatKmeansInputdata(List<Integer> list) {
        ArrayList<float[]> arr = new ArrayList<float[]>();

        for (Integer value : list) {
            float[][] point1 = new float[1][2];
            point1[0][0] = Float.valueOf(value);
            point1[0][1] = Float.valueOf(0);
            arr.add(point1[0]);
        }
        return arr;
    }

    public static void main(String[] args) {
        String line = "{\"commParams\":{\"keyId\":\"db6b51647c886b4874fe0d0f14858e3d\",\"gender\":\"M\",\"model\":\"vivo X9\",\"platform\":\"adr\",\"age\":31},\"areas\":[{\"avgLon\":120.053,\"avgLat\":29.306,\"nowCity\":\"金华\",\"locations\":{\"120.055,29.306\":{\"2017081718\":1,\"2017081917\":1,\"2017082016\":1},\"120.054,29.304\":{\"2017082218\":3,\"2017082219\":3,\"2017082223\":1},\"120.054,29.305\":{\"2017070507\":1,\"2017070419\":2,\"2017073107\":1,\"2017062319\":1,\"2017070807\":1,\"2017062007\":1,\"2017070707\":1,\"2017061808\":1,\"2017072807\":1,\"2017070520\":1,\"2017070420\":1,\"2017081719\":2,\"2017081916\":1,\"2017082121\":1,\"2017082219\":1},\"120.054,29.306\":{\"2017060811\":1,\"2017070400\":1,\"2017080309\":1,\"2017081718\":1,\"2017081819\":1,\"2017081918\":1,\"2017082119\":1},\"120.053,29.306\":{\"2017072400\":1},\"120.052,29.305\":{\"2017072308\":1,\"2017072007\":1,\"2017072900\":1,\"2017071308\":1,\"2017061207\":1},\"120.052,29.316\":{\"2017070901\":1},\"120.054,29.303\":{\"2017080707\":1},\"120.051,29.306\":{\"2017072508\":1,\"2017071908\":1,\"2017080907\":1},\"120.052,29.306\":{\"2017062608\":1,\"2017062708\":1,\"2017062901\":1,\"2017060700\":1,\"2017062208\":1,\"2017062800\":1,\"2017073000\":1,\"2017070211\":1,\"2017060907\":1,\"2017071108\":1,\"2017071600\":1,\"2017071507\":1,\"2017071408\":1,\"2017071208\":1,\"2017061311\":1,\"2017062400\":1,\"2017062500\":1,\"2017071707\":1,\"2017062300\":1,\"2017071808\":1,\"2017062100\":1,\"2017061408\":1,\"2017061700\":1,\"2017061108\":1,\"2017072200\":1,\"2017061908\":1,\"2017070107\":1,\"2017070600\":1,\"2017072608\":1,\"2017072707\":1,\"2017072108\":1,\"2017070307\":1,\"2017063008\":1,\"2017061600\":1,\"2017061005\":1,\"2017061500\":1,\"2017080107\":1,\"2017080207\":1,\"2017080408\":1,\"2017080508\":1,\"2017080808\":1,\"2017081721\":1,\"2017081823\":1,\"2017081822\":2,\"2017081920\":1,\"2017081923\":1,\"2017082122\":1,\"2017082215\":1},\"120.051,29.307\":{\"2017080601\":1}}},{\"avgLon\":120.224,\"avgLat\":29.311,\"nowCity\":\"金华\",\"locations\":{\"120.221,29.309\":{\"2017071009\":1},\"120.226,29.312\":{\"2017070916\":1}}},{\"avgLon\":120.048,\"avgLat\":29.361,\"nowCity\":\"金华\",\"locations\":{\"120.048,29.361\":{\"2017072311\":1}}}],\"cites\":{\"金华\":{\"activeAreas\":3,\"activeDays\":70,\"frequency\":98}},\"hasHotelOrder\":0,\"hasTrafficOrder\":0,\"behaviors\":{}}";

        ToutiaoUserBehavior toutiaoUserBehavior = UserHistoryInfo.GSON.fromJson(line, ToutiaoUserBehavior.class);

        Map<Integer, Integer> weekActiveDayMap = Maps.newTreeMap();
        List<Integer> activeHourList = Lists.newArrayList(); //头条活跃小时

        int predictMissShiftCityNum = new UserBehaviorAnalysis().predictMissShiftCityNum(toutiaoUserBehavior);

        System.out.println(predictMissShiftCityNum);

        List<UserAreas.Area> areas = toutiaoUserBehavior.areas;
        Map<String, String> userBehaviorMap = Maps.newTreeMap();
        int maxWeekofYear = 0;
        int minWeekofYear = 100;
        System.out.println("用户头条行为时间串");

        for (UserAreas.Area area : areas) {
            Map<String, Map<String, Integer>> locations = area.locations;
            Set<String> keySet = locations.keySet();
            for (String key : keySet) {
                String[] split = key.split(",");
                String formatedCoordinate = "";

                Map<String, Integer> stringIntegerMap = locations.get(key);
                for (String date : stringIntegerMap.keySet()) {
                    Integer frequench = stringIntegerMap.get(date);
                    if (split.length == 2) {
                        formatedCoordinate = String.format("%s,%s\t%s", split[1], split[0], frequench);
                    }
                    userBehaviorMap.put(date, formatedCoordinate);
                }
            }
        }

        for (String date : userBehaviorMap.keySet()) {
            String day = date.substring(0, 8);
            DateTime dateTime = new DateTime(day);
            DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyyMMdd");
            DateTime dateTime2 = fmt.parseDateTime(day);
            // todo
            int weekyear = dateTime2.getWeekOfWeekyear();

            int dayOfWeek = dateTime2.getDayOfWeek();
            String hour = date.substring(8);
            activeHourList.add(Integer.valueOf(hour));
            System.out.println(String.format("%s周\t\t%s\t周%s\t\t%s\t%s", weekyear, day, dayOfWeek, hour,
                    userBehaviorMap.get(date)));

            if (weekyear == 34) {
                continue;
            }
            if (weekyear > maxWeekofYear) {
                maxWeekofYear = weekyear;
            }
            if (weekyear < minWeekofYear) {
                minWeekofYear = weekyear;
            }

            addMap(weekActiveDayMap, weekyear);

        }

        List<Integer> weekActivedaysList = Lists.newArrayList();

        System.out.println("周活跃天数");
        for (Integer key : weekActiveDayMap.keySet()) {
            System.out.println(String.format("第%s周\t\t%s", key, weekActiveDayMap.get(key)));
            weekActivedaysList.add(weekActiveDayMap.get(key));
        }

        // todo 一周内没有任何头条行为
        int appearedWeekNum = weekActivedaysList.size();
        while (appearedWeekNum < 33 - minWeekofYear) {
            weekActivedaysList.add(0);
            appearedWeekNum++;
        }

        weekActivedaysList.sort(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }
        });

        System.out.println(weekActivedaysList.toString());
        int size = weekActivedaysList.size();
        int maxWeekAcitveDays = weekActivedaysList.get(size - 1);
        int minWeekAcitveDays = weekActivedaysList.get(0);
        int midIndex = size / 4;
        if (midIndex < 1) {
            midIndex = 1;
        }
        int midWeekAcitveDays = weekActivedaysList.get(midIndex - 1);
        int totalWeeks = maxWeekofYear - minWeekofYear + 1;
        Double avgWeekAcitveDays = getAvgofList(weekActivedaysList, totalWeeks);

        Double thersholdActiveWeek = Math.min(midWeekAcitveDays, avgWeekAcitveDays);

        //todo 查看用户活跃时间段的聚类结果
        System.out.println("查看用户活跃时间段的聚类结果");
        k_means k_means = excuteKmeans(activeHourList, 3);

        ArrayList<ArrayList<float[]>> activeHourCluster = repeatExecuteKMeans(k_means);

        System.out.println("############活跃时间段聚类结果############");
        // 查看聚类结果
        for (int i = 0; i < activeHourCluster.size(); i++) {
            k_means.printDataArray(activeHourCluster.get(i), "cluster[" + i + "]");
        }


        int countMissWeek = 0;

        // kmeans聚类
        ArrayList<float[]> dataSet = formatKmeansInputdata(weekActivedaysList);
        k_means k = new k_means(3);
        // 设置原始数据集
        k.setDataSet(dataSet);
        // 执行算法
        k.kmeans();
        // 得到聚类结果
        ArrayList<ArrayList<float[]>> cluster = k.getCluster();

        // 查看聚类中心
        ArrayList<float[]> center = k.center;
        System.out.println("聚类中心如下: ");
        for (float[] c : center) {
            System.out.println(c[0]);
        }

        // 查看聚类结果
        for (int i = 0; i < cluster.size(); i++) {
            k.printDataArray(cluster.get(i), "cluster[" + i + "]");
        }

        boolean isMinCluster = false;
        // 查看聚类结果中聚类中心最小的一类
        for (int i = 0; i < cluster.size(); i++) {
            ArrayList<float[]> floats = cluster.get(i);
            for (float[] f : floats) {
                if (f[0] == minWeekAcitveDays) {
                    isMinCluster = true;
                }
            }
            if (isMinCluster) {
                for (float[] f : floats) {
                    if (f[0] < thersholdActiveWeek && f[0] < 4) {
                        countMissWeek++;
                    }
                }
            }
        }

        System.out.println(String.format("周最大活跃天数:%s\n 周最小活跃天数:%s \n 周活跃天数中位数:%s \n 周平均活跃天数:%s \n 失踪周内可能移动次数:%s",
                maxWeekAcitveDays, minWeekAcitveDays, midWeekAcitveDays, avgWeekAcitveDays, countMissWeek));

    }

    public int predictMissShiftCityNum(ToutiaoUserBehavior toutiaoUserBehavior) {
        Map<Integer, Integer> weekActiveDayMap = Maps.newTreeMap();

        List<UserAreas.Area> areas = toutiaoUserBehavior.areas;
        Map<String, String> userBehaviorMap = Maps.newTreeMap();
        int maxWeekofYear = 0;
        int minWeekofYear = 100;
        // System.out.println("用户头条行为时间串");

        for (UserAreas.Area area : areas) {
            Map<String, Map<String, Integer>> locations = area.locations;
            Set<String> keySet = locations.keySet();
            for (String key : keySet) {
                String[] split = key.split(",");
                String formatedCoordinate = "";

                Map<String, Integer> stringIntegerMap = locations.get(key);
                for (String date : stringIntegerMap.keySet()) {
                    Integer frequench = stringIntegerMap.get(date);
                    if (split.length == 2) {
                        formatedCoordinate = String.format("%s,%s\t%s", split[1], split[0], frequench);
                    }
                    userBehaviorMap.put(date, formatedCoordinate);
                }
            }
        }

        for (String date : userBehaviorMap.keySet()) {
            String day = date.substring(0, 8);
            DateTime dateTime = new DateTime(day);
            DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyyMMdd");
            DateTime dateTime2 = fmt.parseDateTime(day);
            // todo
            int weekyear = dateTime2.getWeekOfWeekyear();

            int dayOfWeek = dateTime2.getDayOfWeek();
            String hour = date.substring(8);
            // System.out.println(String.format("%s周\t\t%s\t周%s\t\t%s\t%s", weekyear, day, dayOfWeek, hour,
            // userBehaviorMap.get(date)));

            if (weekyear == 34) {
                continue;
            }
            if (weekyear > maxWeekofYear) {
                maxWeekofYear = weekyear;
            }
            if (weekyear < minWeekofYear) {
                minWeekofYear = weekyear;
            }

            addMap(weekActiveDayMap, weekyear);

        }

        List<Integer> weekActivedaysList = Lists.newArrayList();

        // System.out.println("周活跃天数");
        for (Integer key : weekActiveDayMap.keySet()) {
            // System.out.println(String.format("第%s周\t\t%s", key, weekActiveDayMap.get(key)));
            weekActivedaysList.add(weekActiveDayMap.get(key));
        }

        // todo 一周内没有任何头条行为
        int appearedWeekNum = weekActivedaysList.size();
        while (appearedWeekNum < 33 - minWeekofYear) {
            weekActivedaysList.add(0);
            appearedWeekNum++;
        }

        weekActivedaysList.sort(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }
        });

        // System.out.println(weekActivedaysList.toString());
        int size = weekActivedaysList.size();
        int maxWeekAcitveDays = weekActivedaysList.get(size - 1);
        int minWeekAcitveDays = weekActivedaysList.get(0);
        int midIndex = size / 4;
        if (midIndex < 1) {
            midIndex = 1;
        }
        int midWeekAcitveDays = weekActivedaysList.get(midIndex - 1);
        int totalWeeks = maxWeekofYear - minWeekofYear + 1;
        Double avgWeekAcitveDays = getAvgofList(weekActivedaysList, totalWeeks);

        Double thersholdActiveWeek = Math.min(midWeekAcitveDays, avgWeekAcitveDays);

        int countMissWeek = 0;

        // kmeans聚类
        ArrayList<float[]> dataSet = formatKmeansInputdata(weekActivedaysList);
        k_means k = new k_means(3);
        // 设置原始数据集
        k.setDataSet(dataSet);
        // 执行算法
        k.kmeans();
        // 得到聚类结果
        ArrayList<ArrayList<float[]>> cluster = k.getCluster();

        // 查看聚类中心
        ArrayList<float[]> center = k.center;

        boolean isMinCluster = false;
        // 查看聚类结果中聚类中心最小的一类
        for (int i = 0; i < cluster.size(); i++) {
            ArrayList<float[]> floats = cluster.get(i);
            for (float[] f : floats) {
                if (f[0] == minWeekAcitveDays) {
                    isMinCluster = true;
                }
            }
            if (isMinCluster) {
                for (float[] f : floats) {
                    if (f[0] < thersholdActiveWeek && f[0] < 4) {
                        countMissWeek++;
                    }
                }
            }
        }

        // System.out.println(String.format("周最大活跃天数:%s\n 周最小活跃天数:%s \n 周活跃天数中位数:%s \n 周平均活跃天数:%s \n 失踪周内可能移动次数:%s",
        // maxWeekAcitveDays, minWeekAcitveDays, midWeekAcitveDays, avgWeekAcitveDays, countMissWeek));

        return countMissWeek;
    }


    public static k_means excuteKmeans(List<Integer> list, int clusterNum){
        // kmeans聚类
        ArrayList<float[]> dataSet = formatKmeansInputdata(list);
        k_means k = new k_means(clusterNum);
        // 设置原始数据集
        k.setDataSet(dataSet);
        // 执行算法
        k.kmeans();
        // 得到聚类结果
        ArrayList<ArrayList<float[]>> cluster = k.getCluster();

        // 查看聚类中心
        ArrayList<float[]> center = k.center;
        System.out.println("聚类中心如下: ");
        for (float[] c : center) {
            System.out.println(c[0]);
        }

        // 查看聚类结果
        for (int i = 0; i < cluster.size(); i++) {
            k.printDataArray(cluster.get(i), "cluster[" + i + "]");
        }
        return k;
    }

    public static  ArrayList<ArrayList<float[]>>  repeatExecuteKMeans(k_means k){
        ArrayList<ArrayList<float[]>> cluster = k.getCluster();
        List<Integer> clusterList=Lists.newArrayList();
        ArrayList<ArrayList<float[]>> resultCluter=Lists.newArrayList();
        for (int i = 0; i < cluster.size(); i++) {
            clusterList.clear();
            ArrayList<float[]> floats = cluster.get(i);
            for (float[] f:floats){
                clusterList.add(Float.valueOf(f[0]).intValue());
            }
            clusterList.sort(new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    return o1.compareTo(o2);
                }
            });
            int size = clusterList.size();
            Integer minValue = clusterList.get(0);
            Integer maxValue=clusterList.get(size-1);
            if (maxValue-minValue>=3){
                k_means k_means = excuteKmeans(clusterList, 2);
                ArrayList<ArrayList<float[]>> cluster1 = k_means.getCluster();
                for (ArrayList<float[]> f:cluster1) {
                    resultCluter.add(f);
                }

            }else {
                resultCluter.add(floats);
            }
        }

        return resultCluter;

    }


}
