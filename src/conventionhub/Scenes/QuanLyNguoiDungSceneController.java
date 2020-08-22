package conventionhub.Scenes;

import conventionhub.Bus.HoinghiBus;
import conventionhub.Bus.UserBus;
import conventionhub.pojos.User;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import org.hibernate.Session;
import utils.HibernateUtils;
import utils.ThreadPool;

public class QuanLyNguoiDungSceneController implements Initializable {

    FXMLLoader loader;
    
    //Loaded pages of conventions
    HashMap<Integer,ObservableList<User>> loaded_pages_user;
    Date latestLoadDate;
    
    @FXML
    private Button RefreshButton;
    @FXML
    private TableColumn<User, String> clmUsername;
    @FXML
    private TableColumn<User, String> clmTenUser;
    @FXML
    private TableColumn<User, String> clmEmailUser;
    @FXML
    private TableColumn<User, Boolean> clmTTBlock;
    @FXML
    private TableColumn<User, Boolean> clmBlock;
    @FXML
    private VBox loadingOverlay;
    @FXML
    private TableColumn<User, Integer> clmUserID;
    @FXML
    private BorderPane Users_BorderPane;
    @FXML
    private TableView<User> UsersTable;
    @FXML
    private Pagination UsersDisplay_Pagination;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loader = new FXMLLoader();
        loaded_pages_user = new HashMap<>();
        
        latestLoadDate = new Date();
        UsersTable.addEventFilter(ScrollEvent.ANY, Event::consume);
        UsersDisplay_Pagination.currentPageIndexProperty().addListener((ov, t, t1) -> {
            createPage(t1.intValue());
        });
        
        clmUserID.setCellValueFactory(new PropertyValueFactory<>("iduser"));
        clmUserID.setReorderable(false);
        clmUserID.setCellFactory((p) -> {
            TableCell<User, Integer> cell = new TableCell<>();
            Text text = new Text();
            cell.setGraphic(text);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.textAlignmentProperty().set(TextAlignment.CENTER);
            text.wrappingWidthProperty().bind(cell.widthProperty());
            text.textProperty().bind(Bindings.createStringBinding(() -> {
                if (cell.getItem() == null) {
                    return null ;
                } else {
                    return cell.getItem().toString();
                }
            }, cell.emptyProperty(), cell.itemProperty()));
            return cell;
        });
        
        clmUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        clmUsername.setReorderable(false);
        clmUsername.setCellFactory((p) -> {
            TableCell<User, String> cell = new TableCell<>();
            Text text = new Text();
            cell.setGraphic(text);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.textAlignmentProperty().set(TextAlignment.JUSTIFY);
            text.wrappingWidthProperty().bind(cell.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            return cell;
        });
        
        clmTenUser.setCellValueFactory(new PropertyValueFactory<>("ten"));
        clmTenUser.setReorderable(false);
        clmTenUser.setCellFactory((p) -> {
            TableCell<User, String> cell = new TableCell<>();
            Text text = new Text();
            cell.setGraphic(text);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.textAlignmentProperty().set(TextAlignment.JUSTIFY);
            text.wrappingWidthProperty().bind(cell.widthProperty());
            text.textProperty().bind(Bindings.createStringBinding(() -> {
                if(cell.isEmpty()){
                    return null;
                }
                if(cell.getItem() == null){
                    return "Không có tên hiển thị";
                }else{
                    return cell.getItem().length() == 0 ? "Không có tên hiển thị" : cell.getItem();
                }
            }, cell.emptyProperty(), cell.itemProperty()));
            return cell;
        });
        
        clmEmailUser.setCellValueFactory(new PropertyValueFactory<>("email"));
        clmEmailUser.setReorderable(false);
        clmEmailUser.setCellFactory((p) -> {
            TableCell<User, String> cell = new TableCell<>();
            Text text = new Text();
            cell.setGraphic(text);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.textAlignmentProperty().set(TextAlignment.JUSTIFY);
            text.wrappingWidthProperty().bind(cell.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            return cell;
        });
        
        clmTTBlock.setCellValueFactory((p) -> {
            User user = (User) p.getValue();
            return user.tinhtrangblockProperty();
        });
        clmTTBlock.setReorderable(false);
        clmTTBlock.setCellFactory((p) -> {
            TableCell<User, Boolean> cell = new TableCell<>();
            Text text = new Text();
            cell.setGraphic(text);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.textAlignmentProperty().set(TextAlignment.CENTER);
            text.wrappingWidthProperty().bind(cell.widthProperty());
            text.textProperty().bind(Bindings.createStringBinding(() -> {
                if (cell.getItem() == null) {
                    return null ;
                } else {
                    return cell.getItem() ? "Đã chặn" : "Chưa chặn";
                }
            }, cell.emptyProperty(), cell.itemProperty()));
            return cell;
        });
        
        clmBlock.setCellValueFactory((p) -> {
            User user = (User)p.getValue();
            return user.tinhtrangblockProperty();
        });
        clmBlock.setReorderable(false);
        clmBlock.setCellFactory((p) -> {     
            TableCell<User, Boolean> cell = new TableCell<>(){ 
                Button BlockButton = new Button("Chặn");
                {
                    BlockButton.setOnAction((t) -> {
                        Users_BorderPane.setDisable(true);
                        loadingOverlay.setVisible(true);
                        User user = getTableView().getItems().get(getIndex());
                        Task<Void> updateTTBlock = new Task<Void>(){
                            @Override
                            protected Void call() throws Exception {
                                UserBus.updateBlockUser(user);
                                return null;
                            }
                        };
                        updateTTBlock.setOnSucceeded((t2) -> {
                            user.tinhtrangblockProperty().set(!user.tinhtrangblockProperty().get());
                            loadingOverlay.setVisible(false);
                            Users_BorderPane.setDisable(false);              
                        });
                        updateTTBlock.setOnFailed((t2) -> {
                            Exception ex = (Exception) updateTTBlock.getException();
                            if(ex instanceof UserBus.UserBusException){
                                UserBus.UserBusException exc = (UserBus.UserBusException) ex;
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setHeaderText(exc.getMessage());
                                alert.setContentText(exc.getExplanationString());  
                                alert.show();
                                loadingOverlay.setVisible(false);
                                Users_BorderPane.setDisable(false); 
                            }
                        });
                        ThreadPool.submit(updateTTBlock);
                    });
                }
                @Override
                protected void updateItem(Boolean item, boolean empty) {
                    super.updateItem(item, empty);
                    
                    setGraphic(empty ? null : BlockButton);
                    setAlignment(Pos.CENTER);
                    
                    BlockButton.textProperty().bind(Bindings.createStringBinding(() -> {
                        Boolean b = getItem();
                        String output = b == null ? "Chặn" : b ? "Bỏ chặn" : "Chặn";
                        return output;
                    }, emptyProperty(), itemProperty()));
                    
                    BlockButton.disableProperty().bind(Bindings.createBooleanBinding(() -> {
                        if(getItem() != null){
                            User user = getTableView().getItems().get(getIndex());
                            User current = UserBus.getCurrentUser();
                            if(user != null && current != null)
                                return user.getIduser().equals(current.getIduser());
                            else
                                return true;
                        }else
                            return true;
                    }, emptyProperty(), itemProperty()));
                }
            };
            return cell;
        });
    }

    public void resetUsers(){
        loaded_pages_user.clear();
        latestLoadDate = new Date();
        if(UsersDisplay_Pagination.getCurrentPageIndex() != 0)
            UsersDisplay_Pagination.currentPageIndexProperty().set(0);
        else
            createPage(0);
    }
    
    private void setUsers(ObservableList<User> list){
        UsersTable.setItems(list);
    }
    
    private void createPage(int page){
        Users_BorderPane.disableProperty().set(true);
        loadingOverlay.setVisible(true);
        
        int maxpagesize = 10;
        
        //Load page numbers
        Task<Integer> getPageNumbers = new UserBus.getTotalPages_User(maxpagesize, latestLoadDate);
        getPageNumbers.setOnSucceeded((t) -> {
            //Set indicator
            UsersDisplay_Pagination.setPageCount(getPageNumbers.getValue());
            //Load page
            if(!loaded_pages_user.containsKey(page + 1)){
                Task<ObservableList<User>> getPage = new UserBus.getAll_User_AtPage(page + 1, maxpagesize, latestLoadDate);
                getPage.setOnSucceeded((t1) -> {
                    loaded_pages_user.put(page + 1, getPage.getValue());
                    setUsers(getPage.getValue());
                    loadingOverlay.setVisible(false);
                    Users_BorderPane.disableProperty().set(false);
                });
                ThreadPool.submit(getPage);
            }else{  
                setUsers(loaded_pages_user.get(page + 1));                    
                loadingOverlay.setVisible(false);
                Users_BorderPane.disableProperty().set(false);
            }
        });
        ThreadPool.submit(getPageNumbers);
    }
        
    @FXML
    private void RefreshButton_Clicked(MouseEvent event) {
        resetUsers();
    }
    
}
