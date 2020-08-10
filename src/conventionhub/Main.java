package conventionhub;

import conventionhub.Bus.DangkyhoinghiBus;
import conventionhub.Bus.UserBus;
import java.util.List;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.service.spi.Stoppable;
import utils.HibernateUtils;
import utils.ThreadPool;

public class Main extends Application {
    
    FXMLLoader loader;
    
    @Override
    public void start(Stage primaryStage) throws Exception{
        loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("conventionhub/Scenes/MainScene.fxml"));
        Parent root = loader.load();
        
        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(getClass().getResource("css/global.css").toExternalForm());
        
        primaryStage.setTitle("ConventionHub");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
        
        primaryStage.setOnCloseRequest((t) -> {
            if(UserBus.getLogInSignUpStage() != null){
                UserBus.getLogInSignUpStage().close();
            }
            if(DangkyhoinghiBus.getDuyetDKStage() != null){
                DangkyhoinghiBus.getDuyetDKStage().close();
            }
        });
    }

    public static void main(String[] args) {
        HibernateUtils.getSessionFactory();
        launch(args);
    }
    
    @Override
    public void stop() throws Exception {
        super.stop();
        HibernateUtils.getSessionFactory().close();
        ThreadPool.shutdown();
    }
}
