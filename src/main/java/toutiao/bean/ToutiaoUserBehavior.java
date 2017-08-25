package toutiao.bean;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.qunar.mobile.innovation.data.BusinessLine;
import com.qunar.mobile.innovation.data.MobilePlatform;
import com.qunar.mobile.innovation.histories.BusinessLineHistories;

import java.util.List;
import java.util.Map;


/**
 * Created by yuxiang on 17-6-22.
 */
public class ToutiaoUserBehavior {
    public CommParams commParams = new CommParams();
    public List<UserAreas.Area> areas = Lists.newArrayList();
    public Map<String, UserAreas.CityInfo> cites = Maps.newHashMap();

    public int hasHotelOrder; // 是否有酒店订单

    public int hasTrafficOrder; // 是否有交通类订单

    public Map<BusinessLine, BusinessLineHistories> behaviors = Maps.newEnumMap(BusinessLine.class);

    public static class CommParams {
        public String keyId;
        public String gender;
        public String model;
        public MobilePlatform platform;
        public String residentCity;
        public String uid;
        public String vid;
        public String gid;
        public String userName;
        public String pid;
        public String cid;
        public int age;
    }
}
