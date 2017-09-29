package toutiao.util;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

/**
 * Created by zhipengwu on 17-9-19.
 *
 */
public class MergePredictResult {
    public static void main(String[] args) {
        String featureFile="/home/zhipengwu/secureCRT/toutiao_hotel_behavior_train_20170822.txt.csv";
        String predictKeyidFile="/home/zhipengwu/secureCRT/tail5w_part_origin_feature_20170822_09-07-2.txt_all.csv";

        String predictFile="/home/zhipengwu/secureCRT/test_toutiao_hotel_combine_feature_20170822.out.cal";
        String outputFile="/home/zhipengwu/secureCRT/test_merged_predict_result_20170919.csv";
        int countPos=0;
        try {
            LineIterator featureIterator = FileUtils.lineIterator(new File(featureFile));
            LineIterator predictIterator = FileUtils.lineIterator(new File(predictFile));
            LineIterator predictkeyIdIterator = FileUtils.lineIterator(new File(predictKeyidFile));
            FileWriter fw=new FileWriter(outputFile);


            Map<String,String> featureMap= Maps.newHashMap();

            while (featureIterator.hasNext()){
                String feature = featureIterator.nextLine();
                String[] split = feature.split("\t");
                String keyid=split[0];

                String join = Joiner.on(",").skipNulls().join(split);
                featureMap.put(keyid,join);

            }

            while (predictIterator.hasNext()&&predictkeyIdIterator.hasNext()){
                String predict = predictIterator.nextLine();
                Double aDouble = Double.valueOf(predict);
                Double originValue=aDouble;
                if (aDouble>0.21){
                    aDouble=1.0;
                    countPos++;
                }else {
                    aDouble=0.0;
                }
                String[] split = predictkeyIdIterator.nextLine().split(",");
                String predictkeyId = split[0];


                String newFeatureLabel= split[1];


                String feature = featureMap.get(predictkeyId);
                if (Strings.isNullOrEmpty(feature)){
                    System.out.println(predictkeyId);
                }
                String format = String.format("%s,%s,%s,%s\n", newFeatureLabel,feature, originValue,aDouble);
                fw.append(format);
                fw.flush();

            }

            System.out.println("countPos="+countPos);
            featureIterator.close();
            predictIterator.close();
            fw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
