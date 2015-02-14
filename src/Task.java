/**
 * Created with IntelliJ IDEA.
 * User: osipovku
 * Date: 14.02.15
 * Time: 15:13
 * To change this template use File | Settings | File Templates.
 */

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.security.*;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Task {

    static SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");

    static public void main(String[] args){
        Date date = new Date();
        System.out.println(date.getTime());
        try {
            date = df.parse("19.03.1990");
        } catch (ParseException ex) {
            ex.printStackTrace();
        }

        Calendar calendar = new GregorianCalendar();
        System.out.println(calendar.getTime().toString());
        calendar.add(Calendar.MONTH, 1);
        System.out.println(calendar.getTime());
        System.out.println(calendar.get(Calendar.DAY_OF_WEEK));

        //System.out.println(date.toString());
        //System.out.println(df.format(date));
    }
}
