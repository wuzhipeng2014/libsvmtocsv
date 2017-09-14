#!/bin/sh

baseDir=/home/zhipengwu/secureCRT

## 将头条特征数据才分为训练数据和测试数据

cat $baseDir/toutiao_hotel_combine_feature_20170822.csv|head -150001>$baseDir/train_toutiao_hotel_combine_feature_20170822.csv

cat $baseDir/toutiao_hotel_combine_feature_20170822.csv|head -1>$baseDir/test_toutiao_hotel_combine_feature_20170822.csv

cat $baseDir/toutiao_hotel_combine_feature_20170822.csv|tail -50000>>$baseDir/test_toutiao_hotel_combine_feature_20170822.csv