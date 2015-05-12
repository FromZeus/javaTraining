import javafx.util.Pair;
import sun.reflect.generics.tree.Tree;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Callable;

/**
 * Created by s-quark on 5/12/15.
 */
public class Processor implements Callable<TreeMap<String, TreeMap<String, TreeMap<Integer, Pair<Integer, Integer>>>>> {
    private String file_path;
    private DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.ENGLISH);

    Processor(String file_path){
        this.file_path = file_path;
    }

    public String getWeek(String date){
        String result = "", sund, mond;
        Date parsed_date = null;
        try {
            parsed_date =  df.parse(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(parsed_date);
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            mond = df.format(calendar.getTime());
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            sund = df.format(calendar.getTime());
            result = String.format("%s - %s", mond, sund);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return result;
    }

    public void putTransaction(
            TreeMap<String, TreeMap<String, TreeMap<Integer, Pair<Integer, Integer>>>> processed,
            String date, String acc, Integer acc_id, Integer amount, Pair<Integer, Integer> in_out_mult){
        TreeMap<String, TreeMap<Integer, Pair<Integer, Integer>>> tmp2 =
                new TreeMap<String, TreeMap<Integer, Pair<Integer, Integer>>>();
        TreeMap<Integer, Pair<Integer, Integer>> tmp1 = new TreeMap<Integer, Pair<Integer, Integer>>();

        if (processed.containsKey(date)){
            tmp2 = processed.get(date);
            if (tmp2.containsKey(acc)){
                tmp1 = tmp2.get(acc);
                if (tmp1.containsKey(acc_id)){
                    Pair<Integer, Integer> in_out = tmp1.get(acc_id);
                    tmp1.put(acc_id, new Pair<Integer, Integer>(in_out.getKey() + amount * in_out_mult.getKey(),
                            in_out.getValue() - amount * in_out_mult.getValue()));
                }
                else {
                    tmp1.put(acc_id, new Pair<Integer, Integer>(amount * in_out_mult.getKey(),
                            -amount * in_out_mult.getValue()));
                }
            } else {
                tmp1.put(acc_id, new Pair<Integer, Integer>(amount * in_out_mult.getKey(),
                        -amount * in_out_mult.getValue()));
                tmp2.put(acc, tmp1);
            }
        } else {
            tmp1.put(acc_id, new Pair<Integer, Integer>(amount * in_out_mult.getKey(),
                    -amount * in_out_mult.getValue()));
            tmp2.put(acc, tmp1);
            processed.put(date, tmp2);
        }
    }

    @Override
    public TreeMap<String, TreeMap<String, TreeMap<Integer, Pair<Integer, Integer>>>> call() throws Exception {
        TreeMap<String, TreeMap<String, TreeMap<Integer, Pair<Integer, Integer>>>> processed =
                new TreeMap<String, TreeMap<String, TreeMap<Integer, Pair<Integer, Integer>>>>();

        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ";";

        try {
            br = new BufferedReader(new FileReader(file_path));

            while ((line = br.readLine()) != null) {
                String[] transaction = line.split(cvsSplitBy);
                Integer frm_acc_id = 0, to_acc_id = 0, amount = 0;
                String frm_acc = "", to_acc = "", date = "";

                try{
                    date = getWeek(transaction[0]);
                    frm_acc = transaction[1];
                    to_acc = transaction[3];
                } catch (IndexOutOfBoundsException ex){
                    System.out.println("Can't parse file!");
                }

                try{
                    frm_acc_id = Integer.parseInt(transaction[2]);
                    to_acc_id = Integer.parseInt(transaction[4]);
                    amount = Integer.parseInt(transaction[5]);
                } catch(NumberFormatException ex) {
                    System.out.println("Can't parse file!");
                }

                putTransaction(processed, date, frm_acc, frm_acc_id, amount, new Pair<Integer, Integer>(0, 1));
                putTransaction(processed, date, to_acc, to_acc_id, amount, new Pair<Integer, Integer>(1, 0));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return processed;
    }
}
