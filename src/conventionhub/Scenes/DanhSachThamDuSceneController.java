package conventionhub.Scenes;

import conventionhub.pojos.Dangkyhoinghi;
import conventionhub.pojos.Hoinghi;
import conventionhub.pojos.User;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Control;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.ScrollEvent;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class DanhSachThamDuSceneController implements Initializable {

    @FXML
    private TableView<User> DanhSachThamDu;
    @FXML
    private TableColumn<User, String> clmUsername;
    @FXML
    private TableColumn<User, String> clmTenUser;
    @FXML
    private TableColumn<User, String> clmEmailUser;
    
    ObservableList<User> dangkyhoinghi;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        DanhSachThamDu.addEventFilter(ScrollEvent.ANY, Event::consume);
        
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
    }  
    
    public void setDangkyhoinghi(List<User> usersDangky){
       dangkyhoinghi = FXCollections.observableList(usersDangky);
       DanhSachThamDu.setItems(dangkyhoinghi);
    }
    
}
