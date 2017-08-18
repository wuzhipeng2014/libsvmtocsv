import com.google.common.collect.Sets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.File;
import java.io.IOException;
import java.util.Set;

/**
 * Created by zhipengwu on 17-8-18.
 * 手机型号特征挖掘
 */
public class ModelFeatureMining {

    public static void main(String[] args) {
        String inputFile="/home/zhipengwu/secureCRT/model_frequency.txt";
        Set<String> brandSet= Sets.newHashSet();
        try {
            LineIterator lineIterator = FileUtils.lineIterator(new File(inputFile));
            while (lineIterator.hasNext()){
                String line =lineIterator.nextLine();
                String[] split = line.split("\t");
                String model=split[0];
                String[] split1 = model.split("[ -]");
                String brand=split1[0];
                brandSet.add(brand);
            }

            for (String brand:brandSet){
                System.out.println(brand);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
