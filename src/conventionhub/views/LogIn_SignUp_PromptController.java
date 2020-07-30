package conventionhub.views;

import conventionhub.Bus.UserBus;
import conventionhub.Scenes.DangNhapSceneController;
import conventionhub.pojos.User;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;

public class LogIn_SignUp_PromptController implements Initializable {

    FXMLLoader loader;
    Callback<User, Void> LogInCallBack;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loader = new FXMLLoader();
    }    

    @FXML
    private void LogIn_OnClicked(MouseEvent event) {
        Stage current = (Stage)((Node) event.getSource()).getScene().getWindow();
        Stage s = UserBus.getLogInSignUpStage();
        loader.setLocation(getClass().getClassLoader().getResource("conventionhub/Scenes/DangNhapScene.fxml"));
        try{
            loader.setRoot(null);
            loader.setController(null);
            Parent temp = loader.load();
            Scene scene = new Scene(temp, 300, 300);
            s.setTitle("Đăng nhập");
            s.setScene(scene);
            s.setResizable(false);
        }catch(IOException e){
            e.printStackTrace(System.err);
            return;
        }
        DangNhapSceneController controller = loader.getController();
        controller.setCallbackLogin(LogInCallBack);
        current.close();
        s.show();
    }

    @FXML
    private void SignUp_OnClicked(MouseEvent event) {
        Stage current = (Stage)((Node) event.getSource()).getScene().getWindow();
        Stage s = UserBus.getLogInSignUpStage();
        loader.setLocation(getClass().getClassLoader().getResource("conventionhub/Scenes/DangKyScene.fxml"));
        try{
            loader.setRoot(null);
            loader.setController(null);
            Parent temp = loader.load();
            Scene scene = new Scene(temp, 400, 400);
            s.setTitle("Đăng ký");
            s.setScene(scene);
            s.setResizable(false);
        }catch(IOException e){
            e.printStackTrace(System.err);
            return;
        }
        current.close();
        s.show();
    }
    
    public void setLogInCallBack(Callback<User, Void> c){
        LogInCallBack = c;
    }
    
}
