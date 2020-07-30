package utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javafx.concurrent.Task;

public class ThreadPool {
    private static ExecutorService executor = Executors.newFixedThreadPool(10);
    
    public static Future submit(Task t){
        return executor.submit(t);
    }
}
