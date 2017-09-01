package toutiao.util;

import com.google.common.base.Joiner;

import java.util.Arrays;
import java.util.List;

/**
 * Created by zhipengwu on 17-8-31.
 */
public class Constant {
    public static List<Double> radiusBorderList= Arrays.asList(8.0, 16.0, 32.0);


    public static void main(String[] args) {
        String value="31";
        List<String> strings = FeatureUtil.convertoVector(value, radiusBorderList);
        String join = Joiner.on(",").skipNulls().join(strings);
        System.out.println(join);
    }
}
