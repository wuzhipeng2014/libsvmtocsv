# encoding:utf-8
import os,sys
# python调用上层目录文件中的方法
BASE_DIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
print BASE_DIR
sys.path.append(BASE_DIR+'/mobike/LinearModel')
import GlobalVariable

for j in range(1, 4):
    print j

print GlobalVariable.GLOBAL_A
print GlobalVariable.IntegerFeartureNum
print GlobalVariable.ffmCategoryFeatureStartIndex
print GlobalVariable.ffmGBDTFeatrueStartIndex
print GlobalVariable.IntegerFeartureRange

