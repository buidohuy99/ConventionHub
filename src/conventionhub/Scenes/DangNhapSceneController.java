package conventionhub.Scenes;

import conventionhub.Bus.UserBus;
import conventionhub.pojos.User;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import utils.ThreadPool;

public class DangNhapSceneController implements Initializable {

    FXMLLoader loader;
    Callback<User, Void> onLogInSuccess;
    
    @FXML
    private Button LogIn_Button;
    @FXML
    private TextField Username;
    @FXML
    private TextField MatKhau;
    @FXML
    private Button Register_Button;
    @FXML
    private GridPane DangNhap_Pane;
    @FXML
    private VBox loadingOverlay;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("conventionhub/Scenes/DangKyScene.fxml"));
    }    
    
    public void setCallbackLogin(Callback<User, Void> callback){
        this.onLogInSuccess = callback;
    }

    @FXML
    private void LogIn_OnClicked(ActionEvent event) {
        if(UserBus.checkUserAvailable()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Đã đăng nhập");
            alert.setHeaderText("Xảy ra lỗi khi thực hiện đăng nhập");
            alert.setContentText("Bạn đã đăng nhập!");
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                    Stage s = (Stage)((Node)event.getSource()).getScene().getWindow();
                    s.close();
                }
            });
            return;
        }
        DangNhap_Pane.setDisable(true);
        loadingOverlay.setVisible(true);
        String username = Username.getText();
        String password = MatKhau.getText();
        Task<User> getUserr = new Task<User>(){
            @Override
            protected User call() throws Exception {
                return UserBus.getUser(username, password);
            }  
        };
        getUserr.setOnSucceeded((t) -> {
            User getUser = getUserr.getValue();
            if(getUser == null){
                loadingOverlay.setVisible(false);
                DangNhap_Pane.setDisable(false);
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Xảy ra lỗi dữ liệu nhập");
                alert.setContentText("Username/mật khẩu không chính xác hoặc bạn bị chặn!");
                alert.show();
                return;
            }
            if(onLogInSuccess != null){
                onLogInSuccess.call(getUser);
            }
            loadingOverlay.setVisible(false);
            DangNhap_Pane.setDisable(false);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Thành công");
            alert.setContentText("Bạn đã đăng nhập thành công");  
            alert.show();
            Stage s = (Stage)((Node)event.getSource()).getScene().getWindow();
            s.close();
        });
        ThreadPool.submit(getUserr);
        
    }

    @FXML
    private void Register_OnClicked(ActionEvent event) {
        Stage s = (Stage)((Node)event.getSource()).getScene().getWindow();
        try{
            Parent temp = loader.load();
            Scene scene = new Scene(temp, 400, 400);
            s.setTitle("Đăng ký");
            s.setScene(scene);
            s.setResizable(false);
        }catch(IOException e){
            e.printStackTrace(System.err);
        }
    }
    
}
