package conventionhub.Scenes;

import conventionhub.Bus.DangkyhoinghiBus;
import conventionhub.Bus.HoinghiBus;
import conventionhub.Bus.TinhTrangXoaHoinghiBus;
import conventionhub.Bus.TinhtrangxoaDiadiemBus;
import conventionhub.pojos.Diadiem;
import conventionhub.pojos.Hoinghi;
import conventionhub.pojos.TinhtrangxoaDiadiem;
import conventionhub.pojos.TinhtrangxoaHoinghi;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
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
import javafx.scene.control.Pagination;
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

public class QuanLyHoiNghiSceneController implements Initializable {

    FXMLLoader loader;
    
    //Loaded pages of conventions
    HashMap<Integer,ObservableList<Hoinghi>> loaded_pages_hoinghi;
    Date latestLoadDate;
    
    @FXML
    private BorderPane Conventions_BorderPane;
    @FXML
    private Button AddNewButton;
    @FXML
    private Button RefreshButton;
    @FXML
    private TableView<Hoinghi> ConventionsTable;
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
    private VBox loadingOverlay;
    @FXML
    private TableColumn<Hoinghi, Boolean> clmTTXoa;
    @FXML
    private Pagination ConventionDisplay_Pagination;
    @FXML
    private TableColumn<Hoinghi, Boolean> clmDelete;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loader = new FXMLLoader();
        loaded_pages_hoinghi = new HashMap<>();
        
        latestLoadDate = new Date();
        ConventionsTable.addEventFilter(ScrollEvent.ANY, Event::consume);
        ConventionDisplay_Pagination.currentPageIndexProperty().addListener((ov, t, t1) -> {
            createPage(t1.intValue());
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
                Button EditDetailsButton = new Button("Chỉnh sửa");
                Button ConfirmButton = new Button("Duyệt ĐK");
                {
                    DetailsButton.setOnAction((t) -> {
                        Conventions_BorderPane.setDisable(true);
                        loadingOverlay.setVisible(true);
                        Hoinghi hn = getTableView().getItems().get(getIndex());
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
                        controller.setTabToReturnTo(3);
                        loadingOverlay.setVisible(false);
                        Conventions_BorderPane.setDisable(false);
                        s.show();
                    });
                    EditDetailsButton.setOnAction((t) -> {
                        Conventions_BorderPane.setDisable(true);
                        loadingOverlay.setVisible(true);
                        Hoinghi hn = getTableView().getItems().get(getIndex());
                        Session session = HibernateUtils.getSessionFactory().openSession();
                        session.refresh(hn);
                        if(hn.getThoiDiemBatDau().after(new Date())){
                            session.close();
                            Stage s = (Stage)((Node) t.getSource()).getScene().getWindow();
                            Scene scene;
                            try{
                                loader.setLocation(getClass().getClassLoader().getResource("conventionhub/Scenes/ThemHoiNghiScene.fxml"));
                                loader.setRoot(null);
                                loader.setController(null);
                                Parent temp = loader.load();
                                scene = new Scene(temp, 800, 600);
                                s.setScene(scene);
                                s.setTitle("Chỉnh sửa hội nghị");
                                s.setResizable(false);
                            }catch(IOException e){
                                e.printStackTrace(System.err);
                                return;
                            }
                            ThemHoiNghiSceneController controller = loader.getController();
                            controller.setHoinghi(hn);

                            loadingOverlay.setVisible(false);
                            Conventions_BorderPane.setDisable(false);
                            s.show();
                        }else{
                            session.close();
                            loadingOverlay.setVisible(false);
                            Conventions_BorderPane.setDisable(false);
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setHeaderText("Lỗi chỉnh sửa");
                            alert.setContentText("Hội nghị này đã/đang diễn ra");  
                            alert.show();  
                        }
                    });
                    ConfirmButton.setOnAction((t) -> {
                        Conventions_BorderPane.setDisable(true);
                        loadingOverlay.setVisible(true);
                        Hoinghi hn = getTableView().getItems().get(getIndex());
                        Session session = HibernateUtils.getSessionFactory().openSession();
                        session.refresh(hn);
                        if(hn.getThoiDiemBatDau().after(new Date())){
                            session.close();
                            Stage s = DangkyhoinghiBus.getDuyetDKStage();
                            Scene scene;
                            try{
                                loader.setLocation(getClass().getClassLoader().getResource("conventionhub/Scenes/DuyetDanhSachThamDuScene.fxml"));
                                loader.setRoot(null);
                                loader.setController(null);
                                Parent temp = loader.load();
                                scene = new Scene(temp, 600, 400);
                                scene.getStylesheets().add("conventionhub/css/global.css");
                                s.setScene(scene);
                                s.setTitle("Duyệt đăng ký hội nghị");
                                s.setResizable(false);
                            }catch(IOException e){
                                e.printStackTrace(System.err);
                                return;
                            }
                            DuyetDanhSachThamDuSceneController controller = loader.getController();
                            controller.setHoinghi(hn);

                            loadingOverlay.setVisible(false);
                            Conventions_BorderPane.setDisable(false);
                            s.show();
                        }else{
                            session.close();
                            loadingOverlay.setVisible(false);
                            Conventions_BorderPane.setDisable(false);
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setHeaderText("Lỗi duyệt đăng ký");
                            alert.setContentText("Hội nghị này đã/đang diễn ra");  
                            alert.show();
                        }
                    });
                }
                @Override
                protected void updateItem(Date item, boolean empty) {
                    super.updateItem(item, empty);
                    HBox temp = new HBox(DetailsButton, EditDetailsButton, ConfirmButton);
                    temp.spacingProperty().set(10);
                    temp.alignmentProperty().set(Pos.CENTER);
                    
                    setGraphic(empty ? null : temp);
                    setAlignment(Pos.CENTER);
                    
                    EditDetailsButton.disableProperty().bind(Bindings.createBooleanBinding(() -> {
                        if(item == null || empty)
                            return false;
                        else
                            return item.before(new Date()) || item.equals(new Date());
                    }, itemProperty()));
                    ConfirmButton.disableProperty().bind(Bindings.createBooleanBinding(() -> {
                        if(item == null || empty)
                            return false;
                        else
                            return item.before(new Date()) || item.equals(new Date());
                    }, itemProperty()));
                }
            };
            return cell;
        });
        
        clmTTXoa.setCellValueFactory((p) -> {
            Hoinghi hn = (Hoinghi)p.getValue();
            BooleanProperty bp = hn.tinhtrangxoaProperty();
            TinhtrangxoaHoinghi tt = TinhTrangXoaHoinghiBus.getTinhtrangxoa_forHoinghi(hn.getMaHn());
            boolean ttxoa = tt == null ? false : tt.isTinhtrangxoa();
            bp.set(ttxoa);
            return bp;
        });
        clmTTXoa.setReorderable(false);
        clmTTXoa.setCellFactory((p) -> {
            TableCell<Hoinghi, Boolean> cell = new TableCell<>(){
                Text text = new Text();
                {
                    text.textAlignmentProperty().set(TextAlignment.CENTER);
                }
                @Override
                protected void updateItem(Boolean item, boolean empty) {
                    super.updateItem(item, empty);
                    setPrefHeight(Control.USE_COMPUTED_SIZE);
                    text.wrappingWidthProperty().bind(widthProperty());
                    text.textProperty().bind(Bindings.createStringBinding(() -> {
                        if (getItem() == null) {
                            return null;
                        } else {
                            Boolean b = getItem();
                            String output = b == null ? "Chưa xóa" : b ? "Đã xóa" : "Chưa xóa";
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
        
        clmDelete.setCellValueFactory((p) -> {
            Hoinghi hn = (Hoinghi)p.getValue();
            BooleanProperty bp = hn.tinhtrangxoaProperty();
            return bp;
        });
        clmDelete.setReorderable(false);
        clmDelete.setCellFactory((p) -> {    
            TableCell<Hoinghi, Boolean> cell = new TableCell<>(){
                public Button DeleteButton = new Button("Xóa/Phục hồi");
                {   
                    DeleteButton.setOnAction((t) -> {
                        Conventions_BorderPane.setDisable(true);
                        loadingOverlay.setVisible(true);
                        Hoinghi hn = getTableView().getItems().get(getIndex());
                        Task<Void> xoaHoinghi = new Task<Void>() {
                            @Override
                            protected Void call() throws Exception {
                                HoinghiBus.xoaHoinghi(hn);
                                return null;
                            }
                        };
                        xoaHoinghi.setOnFailed((t1) -> {
                            Exception ex = (Exception) xoaHoinghi.getException();
                            if(ex instanceof HoinghiBus.HoinghiBusException){
                                HoinghiBus.HoinghiBusException exc =(HoinghiBus.HoinghiBusException) ex;
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setHeaderText(exc.getMessage());
                                alert.setContentText(exc.getExplanationString());  
                                alert.show();
                                loadingOverlay.setVisible(false);
                                Conventions_BorderPane.setDisable(false);
                            }
                        });
                        xoaHoinghi.setOnSucceeded((t1) -> {
                            hn.tinhtrangxoaProperty().set(!hn.tinhtrangxoaProperty().get());
                            loadingOverlay.setVisible(false);
                            Conventions_BorderPane.setDisable(false);
                        });
                        ThreadPool.submit(xoaHoinghi);                       
                    });      
                }
                @Override
                protected void updateItem(Boolean item, boolean empty) {
                    super.updateItem(item, empty);
                    setGraphic(empty ? null : DeleteButton);
                    setAlignment(Pos.CENTER);
                    
                    DeleteButton.textProperty().bind(Bindings.createStringBinding(() -> {
                        Boolean b = getItem();
                        String output = b == null ? "Xóa" : b ? "Phục hồi" : "Xóa";
                        return output;
                    }, emptyProperty(), itemProperty()));
                }
            };
            return cell;
        });
    }    

    public void resetHoinghis(){
        loaded_pages_hoinghi.clear();
        latestLoadDate = new Date();
        if(ConventionDisplay_Pagination.getCurrentPageIndex() != 0)
            ConventionDisplay_Pagination.currentPageIndexProperty().set(0);
        else
            createPage(0);
    }
    
    private void setHoiNghis(ObservableList<Hoinghi> list){
        ConventionsTable.setItems(list);
    }
    
    @FXML
    private void AddNewButton_Clicked(MouseEvent event) {
        Conventions_BorderPane.setDisable(true);
        loadingOverlay.setVisible(true);
        Stage s = (Stage)((Node) event.getSource()).getScene().getWindow();
        Scene scene;
        try{
            loader.setLocation(getClass().getClassLoader().getResource("conventionhub/Scenes/ThemHoiNghiScene.fxml"));
            loader.setRoot(null);
            loader.setController(null);
            Parent temp = loader.load();
            scene = new Scene(temp, 800, 600);
            scene.getStylesheets().add("conventionhub/css/global.css");
            s.setScene(scene);
            s.setTitle("Thêm mới hội nghị");
            s.setResizable(false);
        }catch(IOException e){
            e.printStackTrace(System.err);
            return;
        }
        ThemHoiNghiSceneController controller = loader.getController();
        controller.setHoinghi(null);
        loadingOverlay.setVisible(false);
        Conventions_BorderPane.setDisable(false);
        s.show();
    }

    @FXML
    private void RefreshButton_Clicked(MouseEvent event) {
        resetHoinghis();
    }
    
    private void createPage(int page){
        Conventions_BorderPane.disableProperty().set(true);
        loadingOverlay.setVisible(true);
        
        int maxpagesize = 5;
        
        //Load page numbers
        Task<Integer> getPageNumbers = new HoinghiBus.getTotalPages_Hoinghi(maxpagesize, latestLoadDate);
        getPageNumbers.setOnSucceeded((t) -> {
            //Set indicator
            ConventionDisplay_Pagination.setPageCount(getPageNumbers.getValue());
            //Load page
            if(!loaded_pages_hoinghi.containsKey(page + 1)){
                Task<ObservableList<Hoinghi>> getPage = new HoinghiBus.getAll_Hoinghi_AtPage(page + 1, maxpagesize, latestLoadDate);
                getPage.setOnSucceeded((t1) -> {
                    loaded_pages_hoinghi.put(page + 1, getPage.getValue());
                    setHoiNghis(getPage.getValue());
                    loadingOverlay.setVisible(false);
                    Conventions_BorderPane.disableProperty().set(false);
                });
                ThreadPool.submit(getPage);
            }else{  
                setHoiNghis(loaded_pages_hoinghi.get(page + 1));                    
                loadingOverlay.setVisible(false);
                Conventions_BorderPane.disableProperty().set(false);
            }
        });
        ThreadPool.submit(getPageNumbers);
    }
    
}
