package toutiao.util;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by zhipengwu on 17-8-25.
 */
public class MergeCsvFile {

    public static Map<String,String> part2FeatureMap=Maps.newHashMap();
    public static void main(String[] args) {
        //step1. 生成原始特征
        AnalysisFeature.main(null);


        //step2. 原始特征和琳姐的特征合并
        String file1="/home/zhipengwu/secureCRT/part_origin_feature_20170822_09-07-2.txt_all.csv";
        String file2="/home/zhipengwu/secureCRT/std_train_hotel_feature_20170822_09-08-1.txt";
        String outfile="/home/zhipengwu/secureCRT/toutiao_hotel_behavior_train_feature_20170822.txt.csv";
        part2FeatureMap.clear();
        //todo 不合并琳姐的特征
        loadPart2Feature(file2);
        joinFile1(file1,outfile);



        //step2.1 将用户标签特征合并
        part2FeatureMap.clear();
        String userLabelFile="/home/zhipengwu/secureCRT/std_train_hotel_label_feature_20170822_09-14-1.txt";
        String outfile2="/home/zhipengwu/secureCRT/toutiao_hotel_behavior_train_feature_20170822_2.txt.csv";
        loadPart2Feature(userLabelFile);
        joinFile1(outfile,outfile2);




        //step3. 原始特征和我的新特征合并
        part2FeatureMap.clear();
        String newFeature="/home/zhipengwu/secureCRT/toutiao_hotel_behavior_train_20170822.txt.csv";
        String resultFile="/home/zhipengwu/secureCRT/toutiao_hotel_combine_feature_20170822.csv";
        loadPart2Feature(newFeature);
        joinFile(outfile2,resultFile);

    }



    public static void loadPart2Feature(String file2){
        try {
            LineIterator lineIterator2 = FileUtils.lineIterator(new File(file2));
            while (lineIterator2.hasNext()){
                String line =lineIterator2.nextLine();
                String[] split = line.split("\t");
                String keyid=split[0];
                part2FeatureMap.put(keyid,line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void loadPart3Feature(String file2){
        try {
            LineIterator lineIterator2 = FileUtils.lineIterator(new File(file2));
            while (lineIterator2.hasNext()){
                String line =lineIterator2.nextLine();
                String[] split = line.split(",");
                String keyid=split[0];
                part2FeatureMap.put(keyid,line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }




    /**
     * 将两个文件合csv文件按行合并在一起
     * @param file1
     * @param outputFile
     */
    public static void joinFile(String file1,String outputFile){
        try {
            LineIterator lineIterator1 = FileUtils.lineIterator(new File(file1));
            FileWriter fw=new FileWriter(outputFile);
//            fw.append(FeatureResult.header+"\n");
            boolean addHeader=true;
            List<String> list= Lists.newArrayList();
            while (lineIterator1.hasNext()){
                String line1 = lineIterator1.nextLine();
                String[] split1 = line1.split(",");
                String keyid=split1[0];
                String[] split2 =null;
                String line2 = part2FeatureMap.get(keyid);

                //todo 抛弃未匹配的用户
                if (Strings.isNullOrEmpty(line2)){
                    continue;
                }
                String[] resultPart2=new String[0];
                if (!Strings.isNullOrEmpty(line2)){
                 split2 = line2.split("\t");
                    //todo
                    resultPart2=Arrays.copyOfRange(split2,2,split2.length);
                }

                List<String> resultList=Lists.newArrayList();
                int k=1;
                for (k = 1; k < split1.length-2; k++) {
                    resultList.add(split1[k]);
                }
                for (int i = 0; i < resultPart2.length; i++) {
                    resultList.add(resultPart2[i]);
                }
                for ( ; k < split1.length; k++) {
                    resultList.add(split1[k]);
                }




//                int len1=split1.length;
//                int len2=split2.length;
//
//                String[] resultPart1 = Arrays.copyOfRange(split1, 1, 17);
//                int lengthPart1 = resultPart1.length;
//                String[] resultPart3 = Arrays.copyOfRange(split1, 17,len1);
//                resultPart1= Arrays.copyOf(resultPart1, len1 + len2-2);
//                System.arraycopy(resultPart2,0,resultPart1,lengthPart1,resultPart2.length);
//                System.arraycopy(resultPart3,0,resultPart1,lengthPart1+resultPart2.length,resultPart3.length);
                String join = Joiner.on(",").skipNulls().join(resultList);
                if (addHeader){
                    addHeader=false;
                    StringBuilder sb=new StringBuilder();
                    sb.append("Label,");
                    int size = resultList.size();
                    for (int i = 1; i < size-2; i++) {
                        sb.append(String.format("I%s,",i));
                    }
                    sb.append("C1,C2\n");
                    fw.append(sb);
                }

                fw.append(join+"\n");
            }
            fw.flush();
            fw.close();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void joinFile1(String file1,String outputFile){
        try {
            LineIterator lineIterator1 = FileUtils.lineIterator(new File(file1));
            FileWriter fw=new FileWriter(outputFile);
//            fw.append(FeatureResult.header+"\n");
            boolean addHeader=true;
            List<String> list= Lists.newArrayList();
            while (lineIterator1.hasNext()){
                String line1 = lineIterator1.nextLine();
                String[] split1 = line1.split(",");
                String keyid=split1[0];
                String[] split2 =null;
                String line2 = part2FeatureMap.get(keyid);
                String[] resultPart2=new String[0];
                if (!Strings.isNullOrEmpty(line2)){
                    split2 = line2.split("\t");
                    //todo
                    resultPart2=Arrays.copyOfRange(split2,2,split2.length);
                }

                List<String> resultList=Lists.newArrayList();
                int k=1;
                for (k = 0; k < split1.length-2; k++) {
                    resultList.add(split1[k]);
                }
                for (int i = 0; i < resultPart2.length; i++) {
                    resultList.add(resultPart2[i]);
                }
                for ( ; k < split1.length; k++) {
                    resultList.add(split1[k]);
                }





                String join = Joiner.on(",").skipNulls().join(resultList);


                fw.append(join+"\n");
            }
            fw.flush();
            fw.close();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
