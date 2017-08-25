package toutiao.bean;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

/**
 * Created by yuxiang on 17-6-22.
 */
public class UserAreas {

    public String keyId;
    public String os;
    public String gender;
    public String model;
    public int age;
    public List<Area> areas = Lists.newArrayList();
    public Map<String, CityInfo> cites = Maps.newHashMap();


    public static class Area {
        public double avgLon;
        public double avgLat;

        public String nowCity;
        public Map<String, Map<String, Integer>> locations = Maps.newHashMap(); // Map<lon_lat, Record>
    }

    public static class CityInfo {
        public int activeAreas;
        public int activeDays;
        public int frequency;

        @Override
        public String toString() {
            return "CityInfo{" +
                    "activeAreas=" + activeAreas +
                    ", activeDays=" + activeDays +
                    ", frequency=" + frequency +
                    '}';
        }
    }

}
