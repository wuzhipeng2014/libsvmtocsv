import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Created by zhipengwu on 17-8-29.
 */
public class test {
    public static void main(String[] args) {
         DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyyMMdd");

        DateTime dateTime = fmt.parseDateTime("20170903");
        int dayOfWeek = dateTime.getDayOfWeek();
        System.out.println(dayOfWeek);
    }
}
