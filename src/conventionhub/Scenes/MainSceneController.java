package conventionhub.Scenes;

import conventionhub.Bus.DangkyhoinghiBus;
import conventionhub.Bus.HoinghiBus;
import conventionhub.Bus.UserBus;
import conventionhub.pojos.Hoinghi;
import conventionhub.pojos.User;
import conventionhub.views.ConventionCards_ViewController;
import conventionhub.views.ConventionList_ViewController;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Stack;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.Initializable;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.hibernate.Session;
import utils.HibernateUtils;
import utils.ThreadPool;

public class MainSceneController implements Initializable{

    FXMLLoader loader;
    List<Tab> loadedTabs;
    
    @FXML
    private Button LogIn_Out_Button;
    @FXML
    private TabPane ContentsTabPane;
    @FXML
    private ToggleGroup display_type;
    @FXML
    private RadioButton ListDisplay_Button;
    @FXML
    private RadioButton CardsDisplay_Button;  
    @FXML
    private BorderPane Conventions_BorderPane;
    @FXML
    private Pagination ConventionDisplay_Pagination;
    @FXML
    private VBox loadingOverlay;
    @FXML
    private StackPane ConventionDisplayPane;
    @FXML
    private Label TrangThaiDangNhap;
    @FXML
    private Button SignUp_Profile_Button;
    @FXML
    private Button RefreshButton;
    @FXML
    private HBox AccountPane;
    @FXML
    private Label ApplicationDescription;
    
    //Cache the loaded
    Parent ConventionTable_View;
    Parent ConventionCards_View;
    
    //Controllers
    ConventionList_ViewController conventionTableController;
    ConventionCards_ViewController conventionCardsListController;
    
    //Loaded pages of conventions
    HashMap<Integer,ObservableList<Hoinghi>> loaded_pages_hoinghi;
    Date latestLoadDate;
    
    private ChangeListener<Toggle> ToggleConventionDisplayType = new ChangeListener<Toggle>() {
        @Override
        public void changed(ObservableValue<? extends Toggle> ov, Toggle t, Toggle t1) {
            if(display_type.getSelectedToggle() != null && t!=t1){
                loaded_pages_hoinghi.clear();
                createPage(ConventionDisplay_Pagination.getCurrentPageIndex());   
            }
        }  
    };
    
    private void createPage(int page){
        Conventions_BorderPane.disableProperty().set(true);
        loadingOverlay.setVisible(true);
        
        //Create UI if not available
        if(ListDisplay_Button.isSelected()){
            if(ConventionTable_View == null){
                loader.setRoot(null);
                loader.setController(null);
                loader.setLocation(getClass().getClassLoader().getResource("conventionhub/views/ConventionList_View.fxml"));
                try{
                    ConventionTable_View = loader.load();
                } catch (IOException ex){
                    ex.printStackTrace(System.err);
                    return;
                }
                conventionTableController = loader.getController();
                conventionTableController.setInnerController(this);
            }
        } else{
            if(ConventionCards_View == null){
                loader.setRoot(null);
                loader.setController(null);
                loader.setLocation(getClass().getClassLoader().getResource("conventionhub/views/ConventionCards_View.fxml"));
                try{
                    ConventionCards_View = loader.load();
                } catch (IOException ex){
                    ex.printStackTrace(System.err);
                    return;
                }
                conventionCardsListController = loader.getController();
                conventionCardsListController.setInnerController(this);
            }
        }
        
        int maxpagesize = ListDisplay_Button.isSelected() ? 5 : 6;
        
        //Load page numbers
        Task<Integer> getPageNumbers = new Task<Integer>() {
            @Override
            protected Integer call() throws Exception {
                return HoinghiBus.getTotalPages_Hoinghi_NotDeleted(maxpagesize, latestLoadDate);
            }
        }; 
        getPageNumbers.setOnSucceeded((t) -> {
            //Set indicator
            ConventionDisplay_Pagination.setPageCount(getPageNumbers.getValue());
            //Load page
            if(!loaded_pages_hoinghi.containsKey(page + 1)){
                Task<ObservableList<Hoinghi>> getPage = new Task<ObservableList<Hoinghi>>() {
                    @Override
                    protected ObservableList<Hoinghi> call() throws Exception {
                        return HoinghiBus.getAll_Hoinghi_AtPage_NotDeleted(page + 1,maxpagesize,latestLoadDate);
                    }
                }; 
                getPage.setOnSucceeded((t1) -> {
                    loaded_pages_hoinghi.put(page + 1, getPage.getValue());
                    if(ListDisplay_Button.isSelected()){
                        conventionTableController.setConventionsData(getPage.getValue());
                    } else{
                        conventionCardsListController.setConventionsData(getPage.getValue());
                    }
                    loadingOverlay.setVisible(false);
                    Conventions_BorderPane.disableProperty().set(false);
                });
                ThreadPool.submit(getPage);
            }else{  
                if(ListDisplay_Button.isSelected()){
                   conventionTableController.setConventionsData(loaded_pages_hoinghi.get(page + 1));                    
                } else{
                   conventionCardsListController.setConventionsData(loaded_pages_hoinghi.get(page + 1));                    
                }
                loadingOverlay.setVisible(false);
                Conventions_BorderPane.disableProperty().set(false);
            }
        });
        ThreadPool.submit(getPageNumbers);
        
        if(ListDisplay_Button.isSelected()){
            ConventionDisplayPane.getChildren().clear();
            ConventionDisplayPane.getChildren().add(ConventionTable_View);
        }else{
            ConventionDisplayPane.getChildren().clear();
            ConventionDisplayPane.getChildren().add(ConventionCards_View);
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb){
        loader = new FXMLLoader();
        loaded_pages_hoinghi = new HashMap<>();
        loadedTabs = new ArrayList<>();
        
        loadingOverlay.setVisible(false);
        
        String gioithieu = "Tên ứng dụng: ConventionHub%n"
                + "Phiên bản: 1.1%n"
                + "Các chức năng hỗ trợ tiêu biểu:%n"
                + "     - Hiển thị thông tin hội nghị và tham gia hội nghị%n"
                + "     - Hệ thống tài khoản và lưu lại lịch sử đăng ký hội nghị%n"
                + "     - Quản trị các hội nghị và những thông tin liên quan%n"
                + "     - Duyệt đăng ký hội nghị%n"
                + "     - Quản trị người dùng và chặn người dùng%n";
        ApplicationDescription.setText(String.format(gioithieu));
        
        latestLoadDate = new Date();
        ConventionDisplay_Pagination.currentPageIndexProperty().addListener((ov, t, t1) -> {
            createPage(t1.intValue());
        });   
        TrangThaiDangNhap.textProperty().set("Bạn chưa đăng nhập vào hệ thống.");
        
        display_type.selectedToggleProperty().addListener(ToggleConventionDisplayType);
        ContentsTabPane.getSelectionModel().selectedIndexProperty().addListener((ov, t, t1) -> {
            if(t1 == null) return;
            switch(t1.intValue()){
                case 1: 
                    loaded_pages_hoinghi.clear();
                    latestLoadDate = new Date();
                    if(ListDisplay_Button.selectedProperty().get()){
                        if(ConventionDisplay_Pagination.getCurrentPageIndex() != 0)
                            ConventionDisplay_Pagination.currentPageIndexProperty().set(0);
                        else
                            createPage(0);
                    } else {
                        ListDisplay_Button.selectedProperty().set(true);
                    }
                    break;
            }
        });
        
        //Refresh user value on change tab
        ContentsTabPane.getSelectionModel().selectedIndexProperty().addListener((ov, t, t1) -> {
            if(UserBus.getCurrentUser() == null) return;
            ContentsTabPane.setDisable(true);
            AccountPane.setDisable(true);
            //User indication logged in
            Task<User> refreshTask = new Task<User>() {
                @Override
                protected User call() throws Exception {
                    return UserBus.refreshInfoCurrentUser();
                }
            };
            refreshTask.setOnSucceeded((t2) -> {
                User p = refreshTask.getValue();
                String name = p.getTen() == null ? p.getUsername() : p.getTen();
                TrangThaiDangNhap.setText(String.format("Chào mừng %s", name));
                LogIn_Out_Button.setText("Log out");
                SignUp_Profile_Button.setText("Profile");
                if(p.isIsAdmin() && loadedTabs.size() == 1){
                    addAdminTabs();
                }else if(!p.isIsAdmin() && loadedTabs.size() == 3){
                    loadedTabs.remove(1);
                    loadedTabs.remove(1);
                    ContentsTabPane.getTabs().remove(3);
                    ContentsTabPane.getTabs().remove(3);
                }
                
                AccountPane.setDisable(false);
                ContentsTabPane.setDisable(false);
            });
            ThreadPool.submit(refreshTask);
        });
    }
    
    @FXML
    private void LogInOutButton_OnClicked(MouseEvent event) {
        if(!UserBus.checkUserAvailable()){
            AccountPane.setDisable(true);
            ContentsTabPane.setDisable(true);
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
            controller.setCallbackLogin((p) -> {
                setLogIn(0);
                return null;
            });
            ContentsTabPane.setDisable(false);
            AccountPane.setDisable(false);
            s.show(); 
        }else{
            if(UserBus.getCurrentUser() == null) return;
            ContentsTabPane.setDisable(true);
            AccountPane.setDisable(true);
            if(UserBus.getLogInSignUpStage() != null){
                UserBus.getLogInSignUpStage().close();
            }
            if(DangkyhoinghiBus.getDuyetDKStage() != null){
                DangkyhoinghiBus.getDuyetDKStage().close();
            }            
            Task<User> refreshTask = new Task<User>() {
                @Override
                protected User call() throws Exception {
                    return UserBus.refreshInfoCurrentUser();
                }
            };
            refreshTask.setOnSucceeded((t2) -> {
                User p = refreshTask.getValue();
                UserBus.logoutCurrentUser();
                TrangThaiDangNhap.setText("Bạn chưa đăng nhập vào hệ thống.");
                LogIn_Out_Button.setText("Log In");
                SignUp_Profile_Button.setText("Sign Up");
  
                ContentsTabPane.getSelectionModel().select(0);
                ContentsTabPane.getTabs().remove(2);
                if(p.isIsAdmin()){
                    ContentsTabPane.getTabs().remove(2);
                    ContentsTabPane.getTabs().remove(2);
                }
                loadedTabs.clear();
                AccountPane.setDisable(false);
                ContentsTabPane.setDisable(false);
            });
            ThreadPool.submit(refreshTask);
        }
    }
    
    private void addAdminTabs(){
        //QLY HN
        Tab quanLyHoiNghiTab = new Tab("Quản lý hội nghị");
        loader.setLocation(getClass().getClassLoader().getResource("conventionhub/Scenes/QuanLyHoiNghiScene.fxml"));
        Parent temp;
        try{
            loader.setRoot(null);
            loader.setController(null);
            temp = loader.load();
        }catch(IOException e){
            e.printStackTrace(System.err);
            return;
        }
        QuanLyHoiNghiSceneController qlhn_controller = loader.getController();
        quanLyHoiNghiTab.contentProperty().set(temp);
        //QLY User
        Tab quanLyUserTab = new Tab("Quản lý người dùng");
        loader.setLocation(getClass().getClassLoader().getResource("conventionhub/Scenes/QuanLyNguoiDungScene.fxml"));
        try{
            loader.setRoot(null);
            loader.setController(null);
            temp = loader.load();
        }catch(IOException e){
            e.printStackTrace(System.err);
            return;
        }
        QuanLyNguoiDungSceneController qlnd_controller = loader.getController();
        quanLyUserTab.contentProperty().set(temp);

        //Add tabs and selection listener to reload
        ContentsTabPane.getSelectionModel().selectedIndexProperty().addListener((ov, t2, t1) -> {
            if(t1 == null) return;
            switch(t1.intValue()){
                case 3:
                    qlhn_controller.resetHoinghis();
                    return;
            }
            if(t2 == null) return;
            if(t2.intValue() == 3){
                if(DangkyhoinghiBus.getDuyetDKStage() != null){
                    DangkyhoinghiBus.getDuyetDKStage().close();
                }
            }
        });
        ContentsTabPane.getSelectionModel().selectedIndexProperty().addListener((ov, t2, t1) -> {
            if(t1 == null) return;
            switch(t1.intValue()){
                case 4:
                    qlnd_controller.resetUsers();
                    break;
            }
        });
        loadedTabs.add(quanLyHoiNghiTab);
        loadedTabs.add(quanLyUserTab);
        ContentsTabPane.getTabs().add(quanLyHoiNghiTab);
        ContentsTabPane.getTabs().add(quanLyUserTab);
    }
    
    public void setLogIn(int tabToDisplay){
        if(UserBus.getCurrentUser() == null) {
            if(tabToDisplay >= 0 && tabToDisplay < ContentsTabPane.getTabs().size())
                ContentsTabPane.getSelectionModel().select(tabToDisplay);  
            return;
        }
        
        ContentsTabPane.setDisable(true);
        AccountPane.setDisable(true);
        //User indication logged in
        Task<User> refreshTask = new Task<User>() {
            @Override
            protected User call() throws Exception {
                return UserBus.refreshInfoCurrentUser();
            }
        };
        refreshTask.setOnSucceeded((t) -> {
            User p = refreshTask.getValue();
            String name = p.getTen() == null ? p.getUsername() : p.getTen();
            TrangThaiDangNhap.setText(String.format("Chào mừng %s", name));
            LogIn_Out_Button.setText("Log out");
            SignUp_Profile_Button.setText("Profile");
            //Add quan ly tabs if is admin
            if(p.isIsAdmin()){
                addAdminTabs();
            }     
            //Set Selected tab atlast
            int displayedTab = tabToDisplay;
            if(tabToDisplay < 0 || tabToDisplay >= ContentsTabPane.getTabs().size()) displayedTab = 0;
            ContentsTabPane.getSelectionModel().select(displayedTab);  
            AccountPane.setDisable(false);
            ContentsTabPane.setDisable(false);
        });
        
        //Add thong ke tab
        Tab thongKeHoiNghiTab = new Tab("Thống kê hội nghị");
        loader.setLocation(getClass().getClassLoader().getResource("conventionhub/Scenes/ThongKeHoiNghiScene.fxml"));
        Parent temp;
        try{
            loader.setRoot(null);
            loader.setController(null);
            temp = loader.load();
        }catch(IOException e){
            e.printStackTrace(System.err);
            return;
        }
        ThongKeHoiNghiSceneController tk_controller = loader.getController();
        thongKeHoiNghiTab.contentProperty().set(temp);
        
        //Add normal tabs and selection listener to reload them
        ContentsTabPane.getSelectionModel().selectedIndexProperty().addListener((ov, t, t1) -> {
            if(t1 == null) return;
            switch(t1.intValue()){
                case 2:
                    tk_controller.setDKHoiNghi();
                    break;
            };
        });
        loadedTabs.add(thongKeHoiNghiTab);
        ContentsTabPane.getTabs().add(thongKeHoiNghiTab);      
        
        ThreadPool.submit(refreshTask);
    }

    @FXML
    private void SignUp_ProfileButton_OnClicked(MouseEvent event) {
        if(!UserBus.checkUserAvailable()){
            AccountPane.setDisable(true);
            ContentsTabPane.setDisable(true);
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
            ContentsTabPane.setDisable(false);
            AccountPane.setDisable(false);
            s.show(); 
        } else {
            AccountPane.setDisable(true);
            ContentsTabPane.setDisable(true);
            Stage s = UserBus.getLogInSignUpStage();
            loader.setLocation(getClass().getClassLoader().getResource("conventionhub/Scenes/ChiTietAccountScene.fxml"));
            try{
                loader.setRoot(null);
                loader.setController(null);
                Parent temp = loader.load();
                Scene scene = new Scene(temp, 400, 400);
                s.setTitle("Profile");
                s.setScene(scene);
                s.setResizable(false);
            }catch(IOException e){
                e.printStackTrace(System.err);
                return;
            }
            ChiTietAccountSceneController controller = loader.getController();
            controller.setUser();
            controller.setOnUpdateSuccessCallback((p) -> {
                if(p == null) return null;
                AccountPane.setDisable(true);
                String name = p.getTen() == null ? p.getUsername() : p.getTen();
                TrangThaiDangNhap.setText(String.format("Chào mừng %s", name));
                AccountPane.setDisable(false);
                return null; 
            });
            ContentsTabPane.setDisable(false);
            AccountPane.setDisable(false);
            s.show(); 
        }
    }

    @FXML
    private void RefreshButton_OnClicked(MouseEvent event) {
        loaded_pages_hoinghi.clear();
        latestLoadDate = new Date();
        if(ListDisplay_Button.selectedProperty().get()){
            if(ConventionDisplay_Pagination.getCurrentPageIndex() != 0)
                ConventionDisplay_Pagination.currentPageIndexProperty().set(0);
            else
                createPage(0);
        } else {
            if(ConventionDisplay_Pagination.getCurrentPageIndex() != 0)
                ConventionDisplay_Pagination.currentPageIndexProperty().set(0);
            else
                createPage(0);
        }
    }
    
    public void toggleLoadingConventionsBorderPane(boolean disable){
        if(disable){
            Conventions_BorderPane.disableProperty().set(disable);
            loadingOverlay.setVisible(disable);
        }else{
            loadingOverlay.setVisible(disable);
            Conventions_BorderPane.disableProperty().set(disable);
        }
    }

}

