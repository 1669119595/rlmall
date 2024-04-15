import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TestClass {

    @Test
    public void getDate(){
        Calendar instance = Calendar.getInstance();
        Date time = instance.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timeString = format.format(time);
        System.out.println(timeString);
        System.out.println(instance.getTimeZone());
    }
}
