package conventionhub.Scenes;

import conventionhub.Bus.DangkyhoinghiBus;
import conventionhub.Bus.DangkyhoinghiBus.DangkyhoinghiBusException;
import conventionhub.Bus.HoinghiBus;
import conventionhub.Bus.UserBus;
import conventionhub.pojos.Hoinghi;
import conventionhub.pojos.User;
import conventionhub.views.LogIn_SignUp_PromptController;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import utils.HibernateUtils;
import utils.ThreadPool;

public class ChiTietSceneController implements Initializable {

    Hoinghi hnHoinghi;
    FXMLLoader loader;
    Integer tabToReturn;
    
    Stage dialogStage;
    
    Image defaultImage;
    
    @FXML
    private Button Register_Convention;
    @FXML
    private GridPane Information_Pane;
    @FXML
    private Label MaHoiNghi;
    @FXML
    private Label TenHoiNghi;
    @FXML
    private Label TGBatDau;
    @FXML
    private Label TGKetThuc;
    @FXML
    private Label DiaDiem;
    @FXML
    private Label SoLuongThamDu;
    @FXML
    private Label MoTaChiTiet;
    @FXML
    private ImageView Convention_Image;
    @FXML
    private Button GoBackButton;
    @FXML
    private BorderPane ChiTietScene;
    @FXML
    private VBox loadingOverlay;
    @FXML
    private Button See_RegisterList;
    @FXML
    private Button RefreshButton;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loader = new FXMLLoader();
        dialogStage = new Stage();
        defaultImage = new Image("images/default-convention.png");
    }

    public void setHoiNghi(Hoinghi hn){
        Register_Convention.setDisable(false);
        ChiTietScene.setDisable(true);
        Convention_Image.setDisable(true);
        loadingOverlay.setVisible(true);
        
        hnHoinghi = hn;
        if(hnHoinghi != null){
            Session session = HibernateUtils.getSessionFactory().openSession();
            session.refresh(hnHoinghi);
            MaHoiNghi.textProperty().set(hnHoinghi.getMaHn().toString());
            TenHoiNghi.textProperty().set(hnHoinghi.getTenHn());
            TGBatDau.textProperty().set(hnHoinghi.getThoiDiemBatDau().toString());
            TGKetThuc.textProperty().set(hnHoinghi.getThoiDiemKetThuc().toString());
            Date now = new Date();
            if(now.after(hnHoinghi.getThoiDiemBatDau()) || now.equals(hnHoinghi.getThoiDiemBatDau())){
                Register_Convention.setDisable(true);
            }
            if(hnHoinghi.getTinhtrangxoaHoinghi() != null && hnHoinghi.getTinhtrangxoaHoinghi().isTinhtrangxoa()){
                Register_Convention.setDisable(true);
            }
            if(hnHoinghi.getDiadiem() == null){
                DiaDiem.textProperty().set("Không tồn tại địa điểm tổ chức");
                SoLuongThamDu.textProperty().set(String.valueOf(hnHoinghi.getDangkyhoinghis().size()));
            }else{
                DiaDiem.textProperty().set(String.format("%s tại %s", hnHoinghi.getDiadiem().getTenDiaDiem(), hnHoinghi.getDiadiem().getDiaChi()));
                SoLuongThamDu.textProperty().set(String.format("%d / %d", hnHoinghi.getDangkyhoinghis().size(), hnHoinghi.getDiadiem().getSucChua()));
                if(hnHoinghi.getDiadiem().getSucChua() < hnHoinghi.getDangkyhoinghis().size() + 1){
                    Register_Convention.setDisable(true);
                }
            }
            if(hnHoinghi.getChitietHoinghi() == null || hnHoinghi.getChitietHoinghi().getMotaChiTiet().length() == 0
                    || hnHoinghi.getChitietHoinghi().getMotaChiTiet() == null){
                MoTaChiTiet.textProperty().set(hnHoinghi.getMotaNgangon());
            }else{
                MoTaChiTiet.textProperty().set(hnHoinghi.getChitietHoinghi().getMotaChiTiet());
            }
            if(hn.getHinhAnh() == null || hn.getHinhAnh().equals("")){
                Convention_Image.imageProperty().set(defaultImage);
            }else{
                Image image;
                try{
                    image = new Image(hn.getHinhAnh());
                } catch (Exception ex){
                    image = defaultImage;
                }
                if(image.errorProperty().getValue()){
                    image = defaultImage;
                }
                Convention_Image.imageProperty().set(image);
            }
            session.close();
            if(UserBus.getCurrentUser() != null) {
                Task<Boolean> checkUserRegistered = new Task<Boolean>() {
                    @Override
                    protected Boolean call() throws Exception {
                        return DangkyhoinghiBus.checkThamDu(UserBus.getCurrentUser().getIduser(), hn.getMaHn());
                    }
                };
                checkUserRegistered.setOnSucceeded((t1) -> {
                    Boolean b = checkUserRegistered.getValue();
                    if(b){
                        Register_Convention.setDisable(true);
                    }
                    loadingOverlay.setVisible(false);
                    Convention_Image.setDisable(false);
                    ChiTietScene.setDisable(false);
                });
                ThreadPool.submit(checkUserRegistered);
            }else {
                loadingOverlay.setVisible(false);
                Convention_Image.setDisable(false);
                ChiTietScene.setDisable(false);
            }
        }else{   
            loadingOverlay.setVisible(false);
            Convention_Image.setDisable(false);
            ChiTietScene.setDisable(false);
        }
    }
    
    public void setTabToReturnTo(Integer tabIndex){
        tabToReturn = tabIndex;
    }

    @FXML
    private void SeeRegisterList_OnClick(ActionEvent event) {
        ChiTietScene.setDisable(true);
        Convention_Image.setDisable(true);
        loadingOverlay.setVisible(true);
        Stage s = new Stage();
        try{
            loader.setLocation(getClass().getClassLoader().getResource("conventionhub/Scenes/DanhSachThamDuScene.fxml"));
            loader.setRoot(null);
            loader.setController(null);
            Parent temp = loader.load();
            Scene scene = new Scene(temp, 500, 550);
            s.setTitle("Danh sách tham dự");
            s.setScene(scene);
            s.setResizable(false);
        }catch(IOException e){
            e.printStackTrace(System.err);
            return;
        }
        DanhSachThamDuSceneController controller = loader.getController();
        
        Task<List<User>> getDangkyHoinghi = new Task<List<User>>() {
            @Override
            protected List<User> call() throws Exception {
                return DangkyhoinghiBus.getAll_Dangky_Hoinghi(hnHoinghi.getMaHn());
            }
        };
        getDangkyHoinghi.setOnSucceeded((t) -> {
            List<User> dangkys = getDangkyHoinghi.getValue();
            controller.setDangkyhoinghi(dangkys);
            ChiTietScene.setDisable(false);
            Convention_Image.setDisable(false);
            loadingOverlay.setVisible(false);
            s.show();
        });  
        ThreadPool.submit(getDangkyHoinghi);
    }

    @FXML
    private void RegisterConvention_OnClicked(MouseEvent event) {
        Task<Boolean> checkUserRegistered = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                return DangkyhoinghiBus.checkThamDu(UserBus.getCurrentUser().getIduser(), hnHoinghi.getMaHn());
            }
        };
        checkUserRegistered.setOnSucceeded((t1) -> {
            Boolean b = checkUserRegistered.getValue();
            if(b){
                Register_Convention.setDisable(true);
            }
            ChiTietScene.setDisable(false);
            Convention_Image.setDisable(false);
            loadingOverlay.setVisible(false);
        });
        
        if(UserBus.getCurrentUser() == null){
            Stage s = dialogStage;
            Scene scene;
            try{
                loader.setRoot(null);
                loader.setController(null);
                loader.setLocation(getClass().getClassLoader().getResource("conventionhub/views/LogIn_SignUp_Prompt.fxml"));
                Parent temp = loader.load();
                scene = new Scene(temp, 300, 200);      
                s.setResizable(false);
            }catch(IOException e){
                e.printStackTrace(System.err);
                return;
            }
            LogIn_SignUp_PromptController controller = loader.getController();
            controller.setLogInCallBack((p) -> {
                dialogStage.close();
                ChiTietScene.setDisable(true);
                Convention_Image.setDisable(true);
                loadingOverlay.setVisible(true);
                ThreadPool.submit(checkUserRegistered);
                return null; 
            });
            s.setScene(scene);
            s.show();
            return;
        }
        ChiTietScene.setDisable(true);
        Convention_Image.setDisable(true);
        loadingOverlay.setVisible(true);

        Task<Void> addDangKy = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                DangkyhoinghiBus.addDangky(UserBus.getCurrentUser(), hnHoinghi);
                return null;
            }
        };
        addDangKy.setOnSucceeded((t2) -> {
            Register_Convention.setDisable(true);
            ChiTietScene.setDisable(false);
            Convention_Image.setDisable(false);
            loadingOverlay.setVisible(false);
            setHoiNghi(hnHoinghi);
        });
        addDangKy.setOnFailed((t2) -> {
            Exception ex = (Exception) addDangKy.getException();
            if(ex instanceof DangkyhoinghiBus.DangkyhoinghiBusException){
                Register_Convention.setDisable(true);
                ChiTietScene.setDisable(false);
                Convention_Image.setDisable(false);
                loadingOverlay.setVisible(false);
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(ex.getMessage());
                alert.setContentText(((DangkyhoinghiBusException) ex).getExplanationString());  
                alert.show();
            }
        });
        ThreadPool.submit(addDangKy);
    }

    @FXML
    private void GoBack_OnClicked(MouseEvent event) {
        ChiTietScene.setDisable(true);
        Convention_Image.setDisable(true);
        loadingOverlay.setVisible(true);
        Stage s = (Stage)((Node)(event.getSource())).getScene().getWindow();
        Scene scene;
        try{
            loader.setLocation(getClass().getClassLoader().getResource("conventionhub/Scenes/MainScene.fxml"));
            loader.setRoot(null);
            loader.setController(null);
            Parent temp = loader.load();
            scene = new Scene(temp, 800, 600);
            scene.getStylesheets().add("conventionhub/css/global.css");
            s.setTitle("ConventionHub");
            
            s.setResizable(false);
        }catch(IOException e){
            e.printStackTrace(System.err);
            return;
        }
        if(tabToReturn == null) 
            tabToReturn = 0;
        ((MainSceneController)loader.getController()).setLogIn(tabToReturn);
        
        ChiTietScene.setDisable(false);
        Convention_Image.setDisable(false);
        loadingOverlay.setVisible(false);
        s.setScene(scene);
    }

    @FXML
    private void RefreshButton_OnClick(ActionEvent event) {
        setHoiNghi(hnHoinghi);
    }
    
}
