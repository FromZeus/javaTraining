import com.sun.java.util.jar.pack.*;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by s-quark on 5/11/15.
 */
public class Collector implements Runnable{
    private String csv_path, working_path;
    private Pattern csv_pattern = Pattern.compile("\\.csv");

    Collector(String csv_path, String working_path){
        this.csv_path = csv_path;
        this.working_path = working_path;
    }

    public ArrayList<String> listFiles(File folder){
        ArrayList<String> res = new ArrayList<String>();
        for (final File fileEntry: folder.listFiles()){
            if (fileEntry.isDirectory()){
                res.addAll(listFiles(fileEntry));
            }
            else{
                res.add(fileEntry.getPath());
            }
        }
        return res;
    }

    @Override
    public void run(){
        File folder = new File(csv_path);
        Matcher matcher;
        while (true) {
            ArrayList<String> files = listFiles(folder);
            for (final String file : files) {
                matcher = csv_pattern.matcher(file);
                if (matcher.find()) {
                    File csv_file = new File(file);
                    csv_file.renameTo(new File(working_path + csv_file.getName()));

                }
            }
        }
    }
}
