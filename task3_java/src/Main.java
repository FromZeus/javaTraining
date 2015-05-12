import javafx.util.Pair;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by s-quark on 5/11/15.
 */
public class Main {
    private static final int N_THREADS = 10;

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        String csv_path = scanner.nextLine();

        String working_path = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "csv_to_process/";
        File working_path_file = new File(working_path);
        working_path_file.mkdir();

        List<Future<TreeMap<String, TreeMap<String, TreeMap<Integer, Pair<Integer, Integer>>>>>> futures =
                new ArrayList<Future<TreeMap<String, TreeMap<String, TreeMap<Integer, Pair<Integer, Integer>>>>>>();

        ExecutorService executor = Executors.newFixedThreadPool(N_THREADS);
        Runnable worker = new Collector(csv_path, working_path);
        executor.execute(worker);

        while (true) {
            for (int i = 0; i < N_THREADS - 1; i++) {
                Callable<TreeMap<String, TreeMap<String, TreeMap<Integer, Pair<Integer, Integer>>>>> proc_worker =
                        new Processor("");
                Future<TreeMap<String, TreeMap<String, TreeMap<Integer, Pair<Integer, Integer>>>>> submit =
                        executor.submit(proc_worker);
                futures.add(submit);
            }
        }

        //executor.shutdown();
        //executor.awaitTermination();
    }

    public <T1, T2> TreeMap<T1, T2> merge(
            TreeMap<T1, T2> mp1,
            TreeMap<T1, T2> mp2
    ){
        TreeMap<T1, T2> res = new TreeMap<T1, T2>(mp1);
        for (Map.Entry<T1, T2> entry : mp2.entrySet())
        {
            if (!res.containsKey(entry.getKey())) {
                res.put(entry.getKey(), entry.getValue());
            }
            else {
                if (entry.getValue().getClass().equals(Map.class))
                {
                    merge(res.get(entry.getKey()), entry.getValue());
                }
            }

        }
    }
}
