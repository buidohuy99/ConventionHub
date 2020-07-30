/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conventionhub.Scenes;

import conventionhub.Bus.UserBus;
import conventionhub.pojos.User;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;
import utils.ThreadPool;

public class DangKySceneController implements Initializable{
    FXMLLoader loader;
    
    
    @FXML
    private TextField Username;
    @FXML
    private TextField MatKhau;
    @FXML
    private Button Register_Button;
    @FXML
    private TextField HoTen;
    @FXML
    private TextField Email;
    @FXML
    private Button GoBack_Button;
    @FXML
    private GridPane DangKy_Pane;
    @FXML
    private VBox loadingOverlay;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("conventionhub/Scenes/DangNhapScene.fxml"));
    }    
    
    @FXML
    private void Register_OnClicked(ActionEvent event) {
        if(UserBus.checkUserAvailable()){
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Đã đăng nhập");
            alert.setHeaderText("Xảy ra lỗi khi thực hiện đăng ký");
            alert.setContentText("Bạn đã đăng nhập!");
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                    Stage s = (Stage)((Node)event.getSource()).getScene().getWindow();
                    s.close();
                }
            });
            return;
        }
        DangKy_Pane.setDisable(true);
        loadingOverlay.setVisible(true);
        String username = Username.getText();
        String password = MatKhau.getText();
        String Hoten = HoTen.getText();
        if(Hoten.equals("")) Hoten = null;
        String EmailUser = Email.getText();
        Date rightNow = new Date();
        User newUser = new User(username, password, EmailUser, false, rightNow, rightNow, false);
        newUser.setTen(Hoten);
        Task<Void> tryAddingUser = new Task<Void>(){
            @Override
            protected Void call() throws Exception {
                UserBus.addUser(newUser);
                return null;
            }       
        };
        tryAddingUser.setOnFailed((t) -> {
            Exception exc = (Exception) tryAddingUser.getException();
            if(exc instanceof UserBus.UserBusException){
               Alert alert = new Alert(AlertType.ERROR);
               alert.setHeaderText(exc.getMessage());
               alert.setContentText(((UserBus.UserBusException) exc).getExplanationString());    
               alert.show(); 
               loadingOverlay.setVisible(false);
               DangKy_Pane.setDisable(false);
            }
        });
        tryAddingUser.setOnSucceeded((t) -> {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setHeaderText("Thành công");
            alert.setContentText("Bạn đã đăng ký thành công");  
            alert.show();
            Stage s = (Stage)((Node)event.getSource()).getScene().getWindow();
            loadingOverlay.setVisible(false);
            DangKy_Pane.setDisable(false);
            s.close();
        });   
        ThreadPool.submit(tryAddingUser);
    }

    @FXML
    private void GoBack_OnClicked(ActionEvent event) {
        Stage s = (Stage)((Node)event.getSource()).getScene().getWindow();
        try{
            Parent temp = loader.load();
            Scene scene = new Scene(temp, 300, 300);
            s.setTitle("Đăng nhập");
            s.setScene(scene);
            s.setResizable(false);
        }catch(IOException e){
            e.printStackTrace(System.err);
        }  
    }
    
}
