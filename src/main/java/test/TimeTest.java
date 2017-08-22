package test;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

/**
 * Created by zhipengwu on 17-8-22.
 */
public class TimeTest {
    public static void main(String[] args) {
        DateTime dateTime=DateTime.now();
        LocalDate localDate = dateTime.toLocalDate();
        System.out.println(localDate.toString());
    }
}
