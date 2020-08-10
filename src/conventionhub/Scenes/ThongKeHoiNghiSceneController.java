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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import org.hibernate.Session;
import utils.HibernateUtils;
import utils.ThreadPool;

public class ThongKeHoiNghiSceneController implements Initializable {

    ObservableList<String> SearchCriteriasList;
    ObservableList<Hoinghi> usableHoinghi;
    
    FXMLLoader loader;
    
    @FXML
    private BorderPane Conventions_BorderPane;
    @FXML
    private TextField SearchBar;
    @FXML
    private ComboBox<String> SearchKeywordType_ComboBox;
    @FXML
    private VBox loadingOverlay;
    @FXML
    private TableView<Hoinghi> ConventionTable;
    @FXML
    private TableColumn<Hoinghi, Integer> clmMaHoiNghi;
    @FXML
    private TableColumn<Hoinghi, String> clmTenHoiNghi;
    @FXML
    private TableColumn<Hoinghi, String> clmDescription;
    @FXML
    private TableColumn<Hoinghi, Date> clmStartTime;
    @FXML
    private TableColumn<Hoinghi, Date> clmEndTime;
    @FXML
    private TableColumn<Hoinghi, Date> clmActions;
    @FXML
    private Button SearchButton;
    @FXML
    private Button RefreshButton;
    @FXML
    private TableColumn<Hoinghi, Integer> clmConfirmed;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loader = new FXMLLoader();
        SearchCriteriasList = FXCollections.observableArrayList("Theo mã hội nghị","Theo tên hội nghị");
        
        SearchKeywordType_ComboBox.setItems(SearchCriteriasList);
        SearchKeywordType_ComboBox.getSelectionModel().select(0); 
        
        ConventionTable.addEventFilter(ScrollEvent.ANY, Event::consume);
        
        clmConfirmed.setCellValueFactory(new PropertyValueFactory<>("maHn"));
        clmConfirmed.setReorderable(false);
        clmConfirmed.setCellFactory((p) -> {
            TableCell<Hoinghi, Integer> cell = new TableCell<>(){
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
                            if(UserBus.getCurrentUser() == null) return "Không có người dùng";
                            Dangkyhoinghi dk = DangkyhoinghiBus.getDangky_ofUser_forHoinghi(UserBus.getCurrentUser().getIduser(), getItem());
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
        
        clmMaHoiNghi.setCellValueFactory(new PropertyValueFactory<>("maHn"));
        clmMaHoiNghi.setReorderable(false);
        clmMaHoiNghi.setCellFactory((p) -> {
            TableCell<Hoinghi, Integer> cell = new TableCell<>();
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
        
        clmStartTime.setCellValueFactory(new PropertyValueFactory<>("thoiDiemBatDau"));
        clmStartTime.setReorderable(false);
        clmStartTime.setCellFactory((p) -> {
            TableCell<Hoinghi, Date> cell = new TableCell<>();
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
        
        clmEndTime.setCellValueFactory(new PropertyValueFactory<>("thoiDiemKetThuc"));
        clmEndTime.setReorderable(false);
        clmEndTime.setCellFactory((p) -> {
            TableCell<Hoinghi, Date> cell = new TableCell<>();
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
        
        clmTenHoiNghi.setCellValueFactory(new PropertyValueFactory<>("tenHn"));
        clmTenHoiNghi.setReorderable(false);
        clmTenHoiNghi.setCellFactory((p) -> {
            TableCell<Hoinghi, String> cell = new TableCell<>();
            Text text = new Text();
            cell.setGraphic(text);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.textAlignmentProperty().set(TextAlignment.CENTER);
            text.wrappingWidthProperty().bind(cell.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            return cell;
        });
        
        clmDescription.setCellValueFactory(new PropertyValueFactory<>("motaNgangon"));
        clmDescription.setReorderable(false);
        clmDescription.setCellFactory((p) -> {
            TableCell<Hoinghi, String> cell = new TableCell<>();
            Text text = new Text();
            cell.setGraphic(text);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.textAlignmentProperty().set(TextAlignment.JUSTIFY);
            text.wrappingWidthProperty().bind(cell.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            return cell;
        });
        
        clmActions.setCellValueFactory(new PropertyValueFactory<>("thoiDiemBatDau"));
        clmActions.setReorderable(false);
        clmActions.setCellFactory((p) -> {     
            TableCell<Hoinghi, Date> cell = new TableCell<>(){  
                Button DetailsButton = new Button("Chi tiết");
                Button QuitParticipateButton = new Button("Bỏ tham dự");
                {
                    DetailsButton.setOnAction((t) -> { 
                        Hoinghi hn = getTableView().getItems().get(getIndex());
                        Conventions_BorderPane.setDisable(true);
                        loadingOverlay.setVisible(true);
                        Task<TinhtrangxoaHoinghi> checkTTXoa = new Task<TinhtrangxoaHoinghi>() {
                            @Override
                            protected TinhtrangxoaHoinghi call() throws Exception {
                                Session session = HibernateUtils.getSessionFactory().openSession();
                                session.refresh(hn);
                                TinhtrangxoaHoinghi tinhtrang = hn.getTinhtrangxoaHoinghi();
                                session.close();
                                return tinhtrang;
                            }
                        };
                        checkTTXoa.setOnSucceeded((t1) -> {
                            TinhtrangxoaHoinghi tinhtrang = checkTTXoa.getValue();
                            if(tinhtrang == null || !tinhtrang.isTinhtrangxoa()){
                                Stage s = (Stage)((Node) t.getSource()).getScene().getWindow();
                                Scene scene;
                                try{
                                    loader.setLocation(getClass().getClassLoader().getResource("conventionhub/Scenes/ChiTietScene.fxml"));
                                    loader.setRoot(null);
                                    loader.setController(null);
                                    Parent temp = loader.load();
                                    scene = new Scene(temp, 800, 600);
                                    s.setScene(scene);
                                    s.setTitle("Chi tiết hội nghị");
                                    s.setResizable(false);
                                }catch(IOException e){
                                    e.printStackTrace(System.err);
                                    return;
                                }
                                ChiTietSceneController controller = loader.getController();
                                controller.setHoiNghi(hn);
                                controller.setTabToReturnTo(2);
                                loadingOverlay.setVisible(false);
                                Conventions_BorderPane.setDisable(false);
                                s.show();
                            }else{
                                usableHoinghi.remove(getIndex());
                                loadingOverlay.setVisible(false);
                                Conventions_BorderPane.setDisable(false);
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setHeaderText("Lỗi xem chi tiết");
                                alert.setContentText("Hội nghị này đã bị admin xóa");  
                                alert.show();
                            }
                        });
                        ThreadPool.submit(checkTTXoa);
                    });
                    QuitParticipateButton.setOnAction((t) -> {
                        Hoinghi hn = getTableView().getItems().get(getIndex());
                        Conventions_BorderPane.setDisable(true);
                        loadingOverlay.setVisible(true);
                        Task<Void> removeDangky = new Task<Void>() {
                            @Override
                            protected Void call() throws Exception {
                                if(UserBus.getCurrentUser() == null) throw new DangkyhoinghiBus.DangkyhoinghiBusException("Không có người dùng đang đăng nhập", "Không thể lấy dữ liệu thống kê nếu không đăng nhập");
                                DangkyhoinghiBus.removeDangky(hn, UserBus.getCurrentUser());
                                return null;
                            }
                        };
                        removeDangky.setOnSucceeded((t2) -> {
                            usableHoinghi.remove(getIndex());
                            loadingOverlay.setVisible(false);
                            Conventions_BorderPane.setDisable(false);
                        });
                        removeDangky.setOnFailed((t2) -> {
                            Exception ex = (Exception) removeDangky.getException();
                            if(ex instanceof DangkyhoinghiBus.DangkyhoinghiBusException){
                                DangkyhoinghiBus.DangkyhoinghiBusException exc = (DangkyhoinghiBus.DangkyhoinghiBusException) ex;
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setHeaderText(exc.getMessage());
                                alert.setContentText(exc.getExplanationString());  
                                alert.show();   
                                loadingOverlay.setVisible(false);
                                Conventions_BorderPane.setDisable(false);
                            }
                        });
                        ThreadPool.submit(removeDangky);    
                    });
                }
                @Override
                protected void updateItem(Date item, boolean empty) {
                    super.updateItem(item, empty);
                    HBox temp = new HBox(DetailsButton,QuitParticipateButton);
                    temp.spacingProperty().set(10);
                    temp.alignmentProperty().set(Pos.CENTER);
                    
                    setGraphic(empty ? null : temp);
                    setAlignment(Pos.CENTER);
                    
                    QuitParticipateButton.disableProperty().bind(Bindings.createBooleanBinding(() -> {
                        if(item == null || empty)
                            return false;
                        else
                            return item.before(new Date());
                    }, itemProperty()));
                }
            };
            return cell;
        });
    }

    public void setDKHoiNghi(){
        ConventionTable.setItems(null);
        if(UserBus.getCurrentUser() == null) return;
        User current = UserBus.getCurrentUser();
        
        Task<List<Hoinghi>> getDangKyHoiNghi = new Task<List<Hoinghi>>() {
            @Override
            protected List<Hoinghi> call() throws Exception {
                return DangkyhoinghiBus.getAll_Hoinghi_OfUser(current.getIduser());
            }
        };
        
        getDangKyHoiNghi.setOnSucceeded((t) -> {
            List<Hoinghi> list = getDangKyHoiNghi.getValue();
            usableHoinghi = FXCollections.observableList(list);
            
            ConventionTable.setItems(usableHoinghi);
            
            loadingOverlay.setVisible(false);
            Conventions_BorderPane.setDisable(false);
        });
        
        loadingOverlay.setVisible(true);
        Conventions_BorderPane.setDisable(true);
        ThreadPool.submit(getDangKyHoiNghi);
    }

    @FXML
    private void RefreshButton_Clicked(MouseEvent event) {
        setDKHoiNghi();
    }
    
}
