package conventionhub.views;

import conventionhub.Scenes.ChiTietSceneController;
import conventionhub.Scenes.MainSceneController;
import conventionhub.pojos.Hoinghi;
import conventionhub.pojos.TinhtrangxoaHoinghi;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
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
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.ScrollEvent;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.hibernate.Session;
import utils.HibernateUtils;
import utils.ThreadPool;

public class ConventionList_ViewController implements Initializable {
    private ObservableList<Hoinghi> conventionData;
    private MainSceneController innerController;
    
    FXMLLoader loader;
    
    @FXML
    private TableView<Hoinghi> ConventionTable;
    @FXML
    private TableColumn<Hoinghi, String> clmTenHoiNghi;
    @FXML
    private TableColumn<Hoinghi, String> clmDescription;
    @FXML
    private TableColumn<Hoinghi, Date> clmStartTime;
    @FXML
    private TableColumn<Hoinghi, Date> clmEndTime;
    @FXML
    private TableColumn<Hoinghi, Integer> clmMaHoiNghi;
    @FXML
    private TableColumn<Hoinghi, Void> clmDetails;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loader = new FXMLLoader();
        
        ConventionTable.addEventFilter(ScrollEvent.ANY, Event::consume);
        
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
        
        clmDetails.setReorderable(false);
        clmDetails.setCellFactory((p) -> {    
            TableCell<Hoinghi, Void> cell = new TableCell<>(){
                Button button = new Button("Chi tiết");
                {
                    button.setOnAction((t) -> {
                        Hoinghi hn = getTableView().getItems().get(getIndex());
                        innerController.toggleLoadingConventionsBorderPane(true);
                        Task<TinhtrangxoaHoinghi> getTTXoa = new Task<TinhtrangxoaHoinghi>() {
                            @Override
                            protected TinhtrangxoaHoinghi call() throws Exception {
                                Session session = HibernateUtils.getSessionFactory().openSession();
                                session.refresh(hn);
                                TinhtrangxoaHoinghi tinhtrang = hn.getTinhtrangxoaHoinghi();
                                session.close();
                                return tinhtrang;
                            }
                        };
                        getTTXoa.setOnSucceeded((t2) -> {
                            TinhtrangxoaHoinghi tinhtrang = getTTXoa.getValue();
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
                                controller.setTabToReturnTo(1);
                                innerController.toggleLoadingConventionsBorderPane(false);
                                s.show();
                            }else{   
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setHeaderText("Lỗi xem chi tiết");
                                alert.setContentText("Hội nghị này đã bị admin xóa");  
                                innerController.toggleLoadingConventionsBorderPane(false);
                                alert.show();
                            }
                        });
                        ThreadPool.submit(getTTXoa);
                    });
                    
                }
                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    setGraphic(empty ? null : button);
                    setAlignment(Pos.CENTER);
                }
            };
            return cell;
        });
    }    

    public void setConventionsData(ObservableList<Hoinghi> data){
        conventionData = data;
        ConventionTable.setItems(data);
    }
    
    public void setInnerController(MainSceneController controller){
        innerController = controller;
    }
}
