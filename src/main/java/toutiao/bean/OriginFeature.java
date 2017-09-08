package toutiao.bean;

import toutiao.util.FeatureUtil;

import java.util.Arrays;
import java.util.List;

/**
 * Created by zhipengwu on 17-9-8.
 */
public class OriginFeature {

    public String  keyid;
    public String  label;
    public String  activeDays ;
    public String  activeWeeks ;
    public String  activeDaysOfTopCity;
    public String  activeDaysOfEndCity;
    public String  gender;
    public String  age;
    public String  platform;
    public String  citeNum;
    public String  areaNum;
    public String  shiftCitys0;
    public String  shiftCitys1;
    public String  shiftCitysOnWorkingDay0;
    public String  shiftCitysOnWorkingDay1;
    public String  shiftCitysOnWeekend0;
    public String  shiftCitysOnWeekend1;
    public String  avgActiveRadius0;
    public String  avgActiveRadius1;
    public String  maxActiveRadius0;
    public String  maxActiveRadius1;
    public String  avgActiveRadiusOnWorkingDay0;
    public String  avgActiveRadiusOnWorkingDay1;
    public String  maxActiveRadiusOnWorkingDay0;
    public String  maxActiveRadiusOnWorkingDay1;
    public String  avgActiveRadiusOnWeekend0;
    public String  avgActiveRadiusOnWeekend1;
    public String  maxActiveRadiusOnWeekend0;
    public String  maxActiveRadiusOnWeekend1;
    public String  model;
    public String  allCity;


    public static OriginFeature createOriginFeature(String[] split){
        OriginFeature originFeature=new OriginFeature();
        for (int i = 0; i < split.length; i++) {
            originFeature.keyid=split[0];
            originFeature.label=split[1];
            originFeature.activeDays=split[2];
            originFeature.activeWeeks=split[3];
            originFeature.activeDaysOfTopCity=split[4];
            originFeature.activeDaysOfEndCity=split[5];
            originFeature.gender=split[6];
            originFeature.age=split[7];
            originFeature.platform=split[8];
            originFeature.citeNum=split[9];
            originFeature.areaNum=split[10];
            originFeature.shiftCitys0=split[11];
            originFeature.shiftCitys1=split[12];
            originFeature.shiftCitysOnWorkingDay0=split[13];
            originFeature.shiftCitysOnWorkingDay1=split[14];
            originFeature.shiftCitysOnWeekend0=split[15];
            originFeature.shiftCitysOnWeekend1=split[16];
            originFeature.avgActiveRadius0=split[17];
            originFeature.avgActiveRadius1=split[18];
            originFeature.maxActiveRadius0=split[19];
            originFeature.maxActiveRadius1=split[20];
            originFeature.avgActiveRadiusOnWorkingDay0=split[21];
            originFeature.avgActiveRadiusOnWorkingDay1=split[22];
            originFeature.maxActiveRadiusOnWorkingDay0=split[23];
            originFeature.maxActiveRadiusOnWorkingDay1=split[24];
            originFeature.avgActiveRadiusOnWeekend0=split[25];
            originFeature.avgActiveRadiusOnWeekend1=split[26];
            originFeature.maxActiveRadiusOnWeekend0=split[27];
            originFeature.maxActiveRadiusOnWeekend1=split[28];
            originFeature.model=split[29];
            originFeature.allCity=split[30];
        }

        return originFeature;

    }

    public void convertFeature(){
        List<Double> borderList = Arrays.asList(0.28, 0.55);
        FeatureUtil.convertoVector(avgActiveRadius0,borderList);
    }


    public void smoothFeature(){
        if (Double.valueOf(avgActiveRadiusOnWorkingDay0)>1000){

        }



    }


    public  String toCsv(){
        String format = String.format("%s,%s,%s,%s,%s, %s,%s,%s,%s,%s, %s,%s,%s,%s,%s,%s, %s,%s,%s,%s,%s,%s, %s,%s,%s,%s,%s",
                keyid, label, activeDays, activeWeeks, activeDaysOfTopCity,
                activeDaysOfEndCity, gender, age, platform, citeNum,
                areaNum, shiftCitys0, shiftCitys1, shiftCitysOnWorkingDay0, shiftCitysOnWorkingDay1,
                shiftCitysOnWeekend0, shiftCitysOnWeekend1, avgActiveRadius0, avgActiveRadius1, maxActiveRadius0,
                maxActiveRadius1, avgActiveRadiusOnWorkingDay0, avgActiveRadiusOnWorkingDay1, maxActiveRadiusOnWorkingDay0, maxActiveRadiusOnWorkingDay1, avgActiveRadiusOnWeekend0, avgActiveRadiusOnWeekend1, maxActiveRadiusOnWeekend0, maxActiveRadiusOnWeekend1,
                model, allCity);

        return format;
    }

}
