# encoding:utf-8
import argparse, csv, sys

file = open('/home/zhipengwu/secureCRT/toutiao_hotel_behavior_train_20170822.txt.highFrequency')
target_cat_feats =[]
lines = file.readlines()
for line in lines:
    target_cat_feats.append(line.strip('\n'))


print target_cat_feats


cat_feats = set()

with open('/home/zhipengwu/secureCRT/sparse_out.txt', 'w') as f_s:
    for row in csv.DictReader(open('/home/zhipengwu/secureCRT/toutiao_hotel_behavior_train_20170822.txt.csv')):
        for j in range(1, 2):
            field = 'C{0}'.format(j)
            key = field + '-' + row[field]
            cat_feats.add(key)
        feats = []
        for j, feat in enumerate(target_cat_feats, start=1):
            if feat in cat_feats:
                print str(j)
                feats.append(str(j))
        f_s.write(' '.join(feats) + '\n')