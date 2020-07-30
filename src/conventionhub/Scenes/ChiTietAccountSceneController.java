package conventionhub.Scenes;

import conventionhub.Bus.UserBus;
import conventionhub.pojos.User;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.hibernate.Session;
import org.mindrot.jbcrypt.BCrypt;
import utils.HibernateUtils;
import utils.ThreadPool;

public class ChiTietAccountSceneController implements Initializable {

    User user;
    Callback<User, Void> onUpdateSuccessCallback;
    
    @FXML
    private Button SaveEdit_Button;
    @FXML
    private Label Username;
    @FXML
    private PasswordField MatKhau;
    private PasswordField NhapMatKhau;
    @FXML
    private TextField HoTen;
    @FXML
    private TextField Email;
    @FXML
    private Button Quit_Button;
    @FXML
    private GridPane Profile_Pane;
    @FXML
    private VBox loadingOverlay;
    @FXML
    private PasswordField NhapLaiMatKhau;
    @FXML
    private PasswordField MatKhauCu;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }   
    
    public void setOnUpdateSuccessCallback(Callback<User, Void> callback){
        onUpdateSuccessCallback = callback;
    }
    
    public void setUser(){
        if(UserBus.getCurrentUser() == null) return;
        Profile_Pane.setDisable(true);
        loadingOverlay.setVisible(true);
        user = UserBus.refreshInfoCurrentUser();   
        Username.setText(user.getUsername());
        HoTen.setText(user.getTen());
        Email.setText(user.getEmail());
        loadingOverlay.setVisible(false);
        Profile_Pane.setDisable(false);
    }

    @FXML
    private void SaveEdit_OnClicked(ActionEvent event) {
        if(user == null) return;
        Profile_Pane.setDisable(true);
        loadingOverlay.setVisible(true);
        try{
            if(MatKhau.getText() != null && MatKhau.getText().length() != 0 && (MatKhau.getText().length() < 8 || MatKhau.getText().length() > 200)){
                throw new UserBus.UserBusException("Xảy ra lỗi khi thực hiện thay đổi", "Mật khẩu mới phải từ 8 đến 200 ký tự");
            }
            if(HoTen.getText() != null && HoTen.getText().length() > 24){
                throw new UserBus.UserBusException("Xảy ra lỗi khi thực hiện thay đổi", "Tên không được dài quá 24 ký tự");
            }
            if(Email.getText().length() < 8 || Email.getText().length() > 200){
                throw new UserBus.UserBusException("Xảy ra lỗi khi thực hiện thay đổi", "Email phải từ 8 đến 200 ký tự");
            }
            if(!NhapLaiMatKhau.getText().equals(MatKhau.getText())){
                throw new UserBus.UserBusException("Xảy ra lỗi khi thực hiện thay đổi", "Nhập lại mật khẩu không giống với mật khẩu mới");
            }
            if(MatKhau.getText() != null && MatKhau.getText().length() != 0 && MatKhau.getText().equals(MatKhauCu.getText())){
                throw new UserBus.UserBusException("Xảy ra lỗi khi thực hiện thay đổi", "Mật khẩu mới không được giống với mật khẩu cũ đã nhập");
            }
        } catch(UserBus.UserBusException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(ex.getMessage());
            alert.setContentText(ex.getExplanationString());    
            alert.show();
            loadingOverlay.setVisible(false);
            Profile_Pane.setDisable(false);
            return;
        }
        
        user.setTen(HoTen.getText() == null? null : HoTen.getText().length() == 0 ? null : HoTen.getText());
        user.setEmail(Email.getText());
        
        String newPW = MatKhau.getText();
        String usertyped_oldPW = MatKhauCu.getText();
        
        Task<Void> updateUser = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                UserBus.suaDoiUser(user, newPW, usertyped_oldPW);
                return null;
            }
        };
        updateUser.setOnSucceeded((t2) -> {
            loadingOverlay.setVisible(false);
            Profile_Pane.setDisable(false);
            UserBus.getLogInSignUpStage().close();
            if(onUpdateSuccessCallback != null){
                onUpdateSuccessCallback.call(user);
            }         
        });
        updateUser.setOnFailed((t2) -> {
            Exception ex = (Exception) updateUser.getException();
            if(ex instanceof UserBus.UserBusException){
                 Alert alert = new Alert(Alert.AlertType.INFORMATION);
                 alert.setHeaderText(ex.getMessage());
                 alert.setContentText(((UserBus.UserBusException) ex).getExplanationString());    
                 alert.show();
                 loadingOverlay.setVisible(false);
                 Profile_Pane.setDisable(false); 
            }
        }); 
        ThreadPool.submit(updateUser);
    }

    @FXML
    private void QuitButton_OnClicked(ActionEvent event) {
        Stage s = (Stage)((Node)event.getSource()).getScene().getWindow();
        s.close();
    }
    
}
