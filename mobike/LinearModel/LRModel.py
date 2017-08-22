# encoding:utf-8

# 使用正则线性模型预测房价

import pandas as pd
import numpy as np
import seaborn as sns
import matplotlib
import matplotlib.pyplot as plt
from scipy.stats import skew
from scipy.stats.stats import pearsonr
import time

start=time.time()
print '程序开始运行时间为:{starttime}'.format(starttime=start)

# 读入训练数据
train = pd.read_csv('/home/zhipengwu/work/toutiao/Libsvmtocsv/src/main/resources/mobike/fomat_train_lng.csv')
test = pd.read_csv('/home/zhipengwu/work/toutiao/Libsvmtocsv/src/main/resources/mobike/fomat_train.csv')

x_train=train.loc[:,'userid':'start_lng_decimal']
y=train.end_lng_decimal


def rmse_cv(model):
    rmse = np.sqrt(-cross_val_score(model, x_train, y, scoring="neg_mean_squared_error", cv=5))
    return (rmse)


# 使用正则化线性模型拟合数据
##------------------------------------------------------------------------------------
from sklearn.linear_model import Ridge, RidgeCV, ElasticNet, LassoCV, LassoLarsCV
from sklearn.model_selection import cross_val_score

print '开始训练模型时间为{model_start}'.format(model_start=time.time())
model_lasso = LassoCV(alphas=[1]).fit(x_train, y)
print '模型训练结束时间为{model_end}'.format(model_end=time.time())

print "lasso rmse: "
print rmse_cv(model_lasso).mean()

preds = pd.DataFrame({"preds": model_lasso.predict(x_train), "true": y})
preds.to_csv('/home/zhipengwu/work/toutiao/Libsvmtocsv/src/main/resources/mobike/format_train_pred.csv',header=True,index=True)

# preds_test=pd.DataFrame({"preds_test":model_lasso.predict(test)});
# preds_test.to_csv('/home/zhipengwu/work/toutiao/Libsvmtocsv/src/main/resources/mobike/format_test_pred.csv',header=True,index=True)


preds["residuals"] = preds["true"] - preds["preds"]
preds.plot(x="preds", y="residuals", kind="scatter")
plt.show()

## 显示估计误差的分布
diff = preds["true"] - preds["preds"]
diff.hist()
plt.show()


end=time.time()


print '程序运行时间为: {elapse}'.format(elapse=end-start)




