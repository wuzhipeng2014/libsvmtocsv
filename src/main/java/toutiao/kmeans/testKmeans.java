package toutiao.kmeans;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by zhipengwu on 17-9-19.
 */
public class testKmeans {
    public  static void main(String[] args)
    {
        //初始化一个Kmean对象，将k置为3
        int num;
        System.out.println("输入要分为的类数：");
        num=(new Scanner(System.in)).nextInt();
        k_means k=new k_means(num);
        ArrayList<float[]> dataSet=new ArrayList<float[]>();
        ReadData rd=new ReadData();
        String fileName="/home/zhipengwu/secureCRT/inputlist.txt";
        dataSet=rd.read(fileName);
        //设置原始数据集
        k.setDataSet(dataSet);
        //执行算法
        k.kmeans();
        //得到聚类结果
        ArrayList<ArrayList<float[]>> cluster=k.getCluster();
        //查看结果
        for(int i=0;i<cluster.size();i++)
        {
            k.printDataArray(cluster.get(i), "cluster["+i+"]");
        }
    }
}
