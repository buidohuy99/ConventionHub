package conventionhub.Scenes;

import conventionhub.Bus.DangkyhoinghiBus;
import conventionhub.Bus.UserBus;
import conventionhub.pojos.Dangkyhoinghi;
import conventionhub.pojos.DangkyhoinghiId;
import conventionhub.pojos.Hoinghi;
import conventionhub.pojos.TinhtrangxoaHoinghi;
import conventionhub.pojos.User;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import org.hibernate.Session;
import utils.HibernateUtils;
import utils.ThreadPool;

public class DuyetDanhSachThamDuSceneController implements Initializable {

    FXMLLoader loader;
    Hoinghi hn;
    ObservableList<User> usableDangky;
    
    @FXML
    private BorderPane Attendance_BorderPane;
    @FXML
    private Button RefreshButton;
    @FXML
    private TableView<User> AttendanceTable;
    @FXML
    private TableColumn<User, String> clmUsername;
    @FXML
    private TableColumn<User, String> clmTenUser;
    @FXML
    private TableColumn<User, String> clmEmailUser;
    @FXML
    private TableColumn<User, Integer> clmConfirmed;
    @FXML
    private TableColumn<User, Integer> clmActions;
    @FXML
    private VBox loadingOverlay;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loader = new FXMLLoader();
        
        AttendanceTable.addEventFilter(ScrollEvent.ANY, Event::consume);
        
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
        
        clmConfirmed.setCellValueFactory(new PropertyValueFactory<>("iduser"));
        clmConfirmed.setReorderable(false);
        clmConfirmed.setCellFactory((p) -> {
            TableCell<User, Integer> cell = new TableCell<>(){
                Text text = new Text();
                {
                    text.textAlignmentProperty().set(TextAlignment.CENTER);
                }
                @Override
                protected void updateItem(Integer item, boolean empty) {
                    super.updateItem(item, empty);
                    setPrefHeight(Control.USE_COMPUTED_SIZE);
                    text.wrappingWidthProperty().bind(widthProperty());
                    text.textProperty().bind(Bindings.createStringBinding(() -> {
                        if (getItem() == null) {
                            return null;
                        } else {
                            if(hn == null) return "Không có đăng ký";
                            Integer iduser = getItem();
                            Dangkyhoinghi dk = DangkyhoinghiBus.getDangky_ofUser_forHoinghi(iduser, hn.getMaHn());
                            String output = dk == null ? "Không có đăng ký" : dk.isDaDuocDuyet() ? "Đã duyệt" : "Chưa duyệt";
                            return output;
                        }
                    }, emptyProperty(), itemProperty()));
                    
                    if (empty || item == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        setGraphic(text);
                    }
                }
            };
            
            return cell;
        });
        
        clmActions.setCellValueFactory(new PropertyValueFactory<>("iduser"));
        clmActions.setReorderable(false);
        clmActions.setCellFactory((p) -> {     
            TableCell<User, Integer> cell = new TableCell<>(){ 
                Button DuyetButton = new Button("Duyệt");
                {
                    DuyetButton.setOnAction((t) -> {
                        if(hn == null) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setHeaderText("Không thể duyệt đăng ký của hội nghị rỗng");
                            alert.setContentText("Không có hội nghị để duyệt đăng ký");  
                            alert.show(); 
                            return;
                        }
                        User user = getTableView().getItems().get(getIndex());
                        Attendance_BorderPane.setDisable(true);
                        loadingOverlay.setVisible(true);
                        Task<Void> saveTask = new Task<Void>(){
                            @Override
                            protected Void call() throws Exception {
                                DangkyhoinghiBus.duyetDangky(user.getIduser(), hn.getMaHn());
                                return null;
                            }                           
                        };
                        saveTask.setOnSucceeded((t1) -> {
                            usableDangky.remove(getIndex());
                            loadingOverlay.setVisible(false);
                            Attendance_BorderPane.setDisable(false);
                        });
                        saveTask.setOnFailed((t1) -> {
                            Exception ex = (Exception) saveTask.getException();
                            if(ex instanceof DangkyhoinghiBus.DangkyhoinghiBusException){
                                DangkyhoinghiBus.DangkyhoinghiBusException exc = (DangkyhoinghiBus.DangkyhoinghiBusException) ex;
                                usableDangky.remove(getIndex());
                                loadingOverlay.setVisible(false);
                                Attendance_BorderPane.setDisable(false);
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setHeaderText(exc.getMessage());
                                alert.setContentText(exc.getExplanationString());  
                                alert.show(); 
                            }
                        });
                        ThreadPool.submit(saveTask);
                    });
                }
                @Override
                protected void updateItem(Integer item, boolean empty) {
                    super.updateItem(item, empty);
                    
                    setGraphic(empty ? null : DuyetButton);
                    setAlignment(Pos.CENTER);
                }
            };
            return cell;
        });
    }    

    public void setHoinghi(Hoinghi hn){
        this.hn = hn;
        if(this.hn != null){
            Hoinghi operateOn = this.hn;
            Attendance_BorderPane.setDisable(true);
            loadingOverlay.setVisible(true);
            Task<List<User>> getDangKys = new Task<List<User>>() {
                @Override
                protected List<User> call() throws Exception {
                    return DangkyhoinghiBus.getAll_ChuaDuyetDangky_Hoinghi(operateOn.getMaHn());
                }
            };
            getDangKys.setOnSucceeded((t) -> {
                List<User> list = getDangKys.getValue();
                usableDangky = FXCollections.observableList(list);
                
                AttendanceTable.setItems(usableDangky);
                AttendanceTable.refresh();
                
                loadingOverlay.setVisible(false);
                Attendance_BorderPane.setDisable(false);
            });
            
            ThreadPool.submit(getDangKys);
        }else{
            usableDangky.clear();
        }
    }
    
    @FXML
    private void RefreshButton_Clicked(MouseEvent event) {
        setHoinghi(hn);
    }
    
}
