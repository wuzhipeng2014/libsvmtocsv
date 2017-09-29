package toutiao.kmeans;

/**
 * Created by zhipengwu on 17-9-19.
 */
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
public class ReadData {
    //从文件中读取数据
    public ArrayList<float[]> read(String fileName){
        ArrayList<float[]> arr=new ArrayList<float[]>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line = null;
            while((line=reader.readLine())!=null){
                String str[] = line.split("\\s+");
                float[][] point1 = new float[1][2];
                point1[0][0]=Float.parseFloat(str[0].trim());
                point1[0][1]=Float.parseFloat(str[1].trim());
                arr.add(point1[0]);
            }
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }

        return arr;

    }

}
