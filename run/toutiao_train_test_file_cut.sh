#!/bin/sh

baseDir=/home/zhipengwu/secureCRT
#filename=toutiao_hotel_combine_feature_20170822.csv
filename=hotel_feature_20170926_10-12_01.csv

## 将头条特征数据才分为训练数据和测试数据

cat $baseDir/$filename|head -300001>$baseDir/train_$filename

cat $baseDir/$filename|head -1>$baseDir/test_$filename

cat $baseDir/$filename|tail -100000>>$baseDir/test_$filename