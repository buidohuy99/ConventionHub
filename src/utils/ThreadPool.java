package utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import javafx.concurrent.Task;

public class ThreadPool {
    private static ExecutorService executor = Executors.newFixedThreadPool(10);
    
    public static Future submit(Task t){
        return executor.submit(t);
    }
    
    public static void shutdown(){
        executor.shutdown();
        try{
            if(!executor.awaitTermination(800, TimeUnit.MILLISECONDS)){
                executor.shutdownNow();
            }
        }catch(InterruptedException e){
            executor.shutdownNow();
        }
    }
}
