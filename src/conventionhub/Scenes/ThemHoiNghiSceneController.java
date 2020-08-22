package conventionhub.Scenes;

import conventionhub.Bus.DiadiemBus;
import conventionhub.Bus.HoinghiBus;
import conventionhub.Bus.TinhtrangxoaDiadiemBus;
import conventionhub.pojos.ChitietHoinghi;
import conventionhub.pojos.Diadiem;
import conventionhub.pojos.Hoinghi;
import conventionhub.pojos.TinhtrangxoaDiadiem;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.hibernate.Session;
import utils.HibernateUtils;
import utils.ThreadPool;

public class ThemHoiNghiSceneController implements Initializable {

    FXMLLoader loader;
    ObservableList<Diadiem> diadiem;
    FileChooser innerFileChooser;
    Stage stageForFileChooser;
    Image defaultImage;
    Hoinghi hnatWork;
    
    //Choices
    Diadiem diadiemDachon;
    
    //Loaded pages of conventions
    HashMap<Integer,ObservableList<Diadiem>> loaded_pages_diadiem;
    Date latestLoadDate;
    
    //Edit Diadiem property
    private BooleanProperty isEditDiadiemOn;
    
    @FXML
    private Button ThemDiaDiemButton;
    @FXML
    private TextField TenDiaDiemField;
    @FXML
    private TextField DiaDiemAddress_Field;
    @FXML
    private TextField DiaDiemCapacity_Field;
    @FXML
    private TableView<Diadiem> DiaDiem_TableView;
    @FXML
    private BorderPane ThemHoiNghi_BorderPane;
    @FXML
    private VBox loadingOverlay;
    @FXML
    private TableColumn<Diadiem, Integer> clmMaDiadiem;
    @FXML
    private TableColumn<Diadiem, String> clmTenDiadiem;
    @FXML
    private TableColumn<Diadiem, String> clmDiaChi;
    @FXML
    private TableColumn<Diadiem, Integer> clmSucChua;
    @FXML
    private TableColumn<Diadiem, Boolean> clmTTXoa;
    @FXML
    private TableColumn<Diadiem, Boolean> clmDelete;
    @FXML
    private TableColumn<Diadiem, Boolean> clmActions;
    @FXML
    private ScrollPane Diadiem_ScrollPane;
    @FXML
    private Pagination DiadiemDisplay_Pagination;
    @FXML
    private Label ChosenDiadiem_Label;
    @FXML
    private Button RefreshDSDiaDiemButton;
    @FXML
    private ImageView ConventionImage_Display;
    @FXML
    private Button SuaDiadiemDangchonButton;
    @FXML
    private Button DeselectSelectionDiadiemButton;
    @FXML
    private TextField imageLink_Field;
    @FXML
    private TextArea MoTaChiTiet_Field;
    @FXML
    private TextField TenHoiNghi_Field;
    @FXML
    private TextField MoTaNganGon_Field;
    @FXML
    private DatePicker TDBD_DatePicker;
    @FXML
    private TextField TDBD_GioField;
    @FXML
    private TextField TDBD_PhutField;
    @FXML
    private DatePicker TDKT_DatePicker;
    @FXML
    private TextField TDKT_GioField;
    @FXML
    private TextField TDKT_PhutField;
    @FXML
    private VBox outsideImageView_VBox;
    @FXML
    private Button GoBackButton;
    @FXML
    private Label pageTitle;
    @FXML
    private Button Add_Edit_HoinghiButton;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loader = new FXMLLoader();
        
        defaultImage = new Image("images/default-convention.png");
        diadiem = FXCollections.observableArrayList();
        loaded_pages_diadiem = new HashMap<>();
        isEditDiadiemOn = new SimpleBooleanProperty(false);
        stageForFileChooser = new Stage();
        innerFileChooser = new FileChooser();
        innerFileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("JPG", "*.jpg"),
            new FileChooser.ExtensionFilter("PNG", "*.png")
        );
        ConventionImage_Display.fitWidthProperty().bind(outsideImageView_VBox.widthProperty());
        ConventionImage_Display.fitHeightProperty().bind(outsideImageView_VBox.heightProperty());
        
        latestLoadDate = new Date();
        DiaDiem_TableView.addEventFilter(ScrollEvent.ANY, Event::consume);
        DiadiemDisplay_Pagination.currentPageIndexProperty().addListener((ov, t, t1) -> {
            createPage(t1.intValue());
        });   
        ThemDiaDiemButton.textProperty().bind(Bindings.createStringBinding(() -> {
            if(!isEditDiadiemOn.get())
                return "Thêm địa điểm";
            else
                return "Sửa địa điểm";
        }, isEditDiadiemOn));
        SuaDiadiemDangchonButton.textProperty().bind(Bindings.createStringBinding(() -> {
            if(!isEditDiadiemOn.get())
                return "Sửa địa điểm đang chọn";
            else
                return "Ngưng chỉnh sửa";
        }, isEditDiadiemOn));
        TDBD_DatePicker.getEditor().setDisable(true);
        TDKT_DatePicker.getEditor().setDisable(true);
        
        //Binding field length and input
        DiaDiemCapacity_Field.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (!newValue.matches("\\d*")) {
                newValue = newValue.replaceAll("[^\\d]", "");
            }
            DiaDiemCapacity_Field.setText(newValue.substring(0, Math.min(newValue.length(), 10)));
        });
        TenDiaDiemField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            TenDiaDiemField.setText(newValue.substring(0, Math.min(newValue.length(), 45)));
        });
        DiaDiemAddress_Field.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            DiaDiemAddress_Field.setText(newValue.substring(0, Math.min(newValue.length(), 70)));
        });
            //For others than diadiem yo
        TDBD_GioField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (!newValue.matches("\\d*")) {
                newValue = newValue.replaceAll("[^\\d]", "");
            }
            String s = newValue.substring(0, Math.min(newValue.length(), 2));
            if(s != null && s.length() != 0)
                s = Integer.valueOf(s) > 23 ? "23" : Integer.valueOf(s) < 0 ? "0" : s;
            TDBD_GioField.setText(s);
        });
        TDBD_PhutField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (!newValue.matches("\\d*")) {
                newValue = newValue.replaceAll("[^\\d]", "");
            }
            String s = newValue.substring(0, Math.min(newValue.length(), 2));
            if(s != null && s.length() != 0)
                s = Integer.valueOf(s) > 59 ? "59" : Integer.valueOf(s) < 0 ? "0" : s;
            TDBD_PhutField.setText(s);
        });
        TDKT_GioField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (!newValue.matches("\\d*")) {
                newValue = newValue.replaceAll("[^\\d]", "");
            }
            String s = newValue.substring(0, Math.min(newValue.length(), 2));
            if(s != null && s.length() != 0)
                s = Integer.valueOf(s) > 23 ? "23" : Integer.valueOf(s) < 0 ? "0" : s;
            TDKT_GioField.setText(s);
        });
        TDKT_PhutField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (!newValue.matches("\\d*")) {
                newValue = newValue.replaceAll("[^\\d]", "");
            }
            String s = newValue.substring(0, Math.min(newValue.length(), 2));
            if(s != null && s.length() != 0)
                s = Integer.valueOf(s) > 59 ? "59" : Integer.valueOf(s) < 0 ? "0" : s;
            TDKT_PhutField.setText(s);
        });
        TenHoiNghi_Field.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            TenHoiNghi_Field.setText(newValue.substring(0, Math.min(newValue.length(), 70)));
        });
        MoTaNganGon_Field.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            MoTaNganGon_Field.setText(newValue.substring(0, Math.min(newValue.length(), 200)));
        });
        
        clmMaDiadiem.setCellValueFactory(new PropertyValueFactory<>("maDiaDiem"));
        clmMaDiadiem.setReorderable(false);
        clmMaDiadiem.setCellFactory((p) -> {
            TableCell<Diadiem, Integer> cell = new TableCell<>();
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
        
        clmTenDiadiem.setCellValueFactory(new PropertyValueFactory<>("tenDiaDiem"));
        clmTenDiadiem.setReorderable(false);
        clmTenDiadiem.setCellFactory((p) -> {
            TableCell<Diadiem, String> cell = new TableCell<>();
            Text text = new Text();
            cell.setGraphic(text);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.textAlignmentProperty().set(TextAlignment.CENTER);
            text.wrappingWidthProperty().bind(cell.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            return cell;
        });
        
        clmDiaChi.setCellValueFactory(new PropertyValueFactory<>("diaChi"));
        clmDiaChi.setReorderable(false);
        clmDiaChi.setCellFactory((p) -> {
            TableCell<Diadiem, String> cell = new TableCell<>();
            Text text = new Text();
            cell.setGraphic(text);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.textAlignmentProperty().set(TextAlignment.JUSTIFY);
            text.wrappingWidthProperty().bind(cell.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            return cell;
        });
        
        clmSucChua.setCellValueFactory(new PropertyValueFactory<>("sucChua"));
        clmSucChua.setReorderable(false);
        clmSucChua.setCellFactory((p) -> {
            TableCell<Diadiem, Integer> cell = new TableCell<>();
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
        
        clmTTXoa.setCellValueFactory((p) -> {
            Diadiem dd = (Diadiem)p.getValue();
            BooleanProperty bp = dd.tinhtrangxoaProperty();
            return bp;
        });
        clmTTXoa.setReorderable(false);
        clmTTXoa.setCellFactory((p) -> {
            TableCell<Diadiem, Boolean> cell = new TableCell<>(){
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
                        Boolean b = getItem();
                        String output = b == null ? "Chưa xóa" : b ? "Đã xóa" : "Chưa xóa";
                        return output;
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
            Diadiem dd = (Diadiem)p.getValue();
            BooleanProperty bp = dd.tinhtrangxoaProperty();
            return bp;
        });
        clmDelete.setReorderable(false);
        clmDelete.setCellFactory((p) -> {    
            TableCell<Diadiem, Boolean> cell = new TableCell<>(){
                public Button DeleteButton = new Button("Xóa/Phục hồi");
                {   
                    DeleteButton.setOnAction((t) -> {
                        ThemHoiNghi_BorderPane.setDisable(true);
                        ConventionImage_Display.setDisable(true);
                        loadingOverlay.setVisible(true);
                        Diadiem diadiem = getTableView().getItems().get(getIndex());
                        Task<Void> xoaDiadiem = new Task<Void>() {
                            @Override
                            protected Void call() throws Exception {
                                DiadiemBus.xoaDiadiem(diadiem);
                                return null;
                            }
                        };
                        xoaDiadiem.setOnFailed((t1) -> {
                            Exception ex = (Exception) xoaDiadiem.getException();
                            if(ex instanceof DiadiemBus.DiadiemBusException){
                                DiadiemBus.DiadiemBusException exc =(DiadiemBus.DiadiemBusException) ex;
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setHeaderText(exc.getMessage());
                                alert.setContentText(exc.getExplanationString());  
                                alert.show();
                                loadingOverlay.setVisible(false);
                                ConventionImage_Display.setDisable(false);
                                ThemHoiNghi_BorderPane.disableProperty().set(false);
                            }
                        });
                        xoaDiadiem.setOnSucceeded((t1) -> {
                            diadiem.tinhtrangxoaProperty().set(!diadiem.tinhtrangxoaProperty().get());
                            loadingOverlay.setVisible(false);
                            ConventionImage_Display.setDisable(false);
                            ThemHoiNghi_BorderPane.disableProperty().set(false);
                        });                     
                        ThreadPool.submit(xoaDiadiem);                   
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
        
        clmActions.setCellValueFactory((p) -> {
            Diadiem dd = (Diadiem)p.getValue();
            BooleanProperty bp = dd.tinhtrangxoaProperty();
            return bp;
        });
        clmActions.setReorderable(false);
        clmActions.setCellFactory((p) -> {    
            TableCell<Diadiem, Boolean> cell = new TableCell<>(){
                public Button SelectButton = new Button("Chọn địa điểm");
                {   
                    SelectButton.setOnAction((t) -> {
                        ThemHoiNghi_BorderPane.setDisable(true);
                        ConventionImage_Display.setDisable(true);
                        loadingOverlay.setVisible(true);
                        Diadiem diadiem = getTableView().getItems().get(getIndex());
                        
                        Task<TinhtrangxoaDiadiem> getTTXoa = new Task<TinhtrangxoaDiadiem>() {
                            @Override
                            protected TinhtrangxoaDiadiem call() throws Exception {
                                return TinhtrangxoaDiadiemBus.getTinhtrangxoa_forDiadiem(diadiem.getMaDiaDiem());
                            }
                        };
                        getTTXoa.setOnSucceeded((t1) -> {
                            TinhtrangxoaDiadiem tt = getTTXoa.getValue();
                            if(tt != null && tt.isTinhtrangxoa()){
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setHeaderText("Lỗi không thể chọn địa điểm đã bị xóa");
                                alert.setContentText("Địa điểm này đã bị xóa. Vui lòng refresh thông tin");  
                                alert.show();
                                loadingOverlay.setVisible(false);
                                ConventionImage_Display.setDisable(false);
                                ThemHoiNghi_BorderPane.disableProperty().set(false);
                                return;
                            }
                            diadiemDachon = diadiem;
                            
                            //Disable all selection related controls of diadiem
                            DiaDiem_TableView.setDisable(true);
                            SuaDiadiemDangchonButton.setDisable(false);
                            DeselectSelectionDiadiemButton.setDisable(false);
                            ChosenDiadiem_Label.setText(diadiemDachon.getMaDiaDiem().toString());
                            
                            loadingOverlay.setVisible(false);
                            ConventionImage_Display.setDisable(false);
                            ThemHoiNghi_BorderPane.disableProperty().set(false);
                        });
                        ThreadPool.submit(getTTXoa);
                    });      
                }
                @Override
                protected void updateItem(Boolean item, boolean empty) {
                    super.updateItem(item, empty);
                    setGraphic(empty ? null : SelectButton);
                    setAlignment(Pos.CENTER);
                    
                    SelectButton.disableProperty().bind(Bindings.createBooleanBinding(() -> {
                        Boolean b = getItem();
                        boolean output = b == null ? false : b ;
                        return output;
                    }, emptyProperty(), itemProperty()));
                }
            };
            return cell;
        });
    } 
    
    public void setHoinghi(Hoinghi hn){
        hnatWork = hn;
        if(hn == null){
            pageTitle.setText("Thêm hội nghị mới");
            Add_Edit_HoinghiButton.setText("Thêm mới hội nghị này");
            resetDiadiems();
        }else{
            ThemHoiNghi_BorderPane.setDisable(true);
            ConventionImage_Display.setDisable(true);
            loadingOverlay.setVisible(true);
            
            Add_Edit_HoinghiButton.setText("Sửa thông tin hội nghị này");
            Session session = HibernateUtils.getSessionFactory().openSession();
            session.refresh(hnatWork);
            pageTitle.setText(String.format("Sửa thông tin hội nghị %d", hnatWork.getMaHn()));
            
            TenHoiNghi_Field.setText(hnatWork.getTenHn());
            MoTaNganGon_Field.setText(hnatWork.getMotaNgangon());
            
            Date TDBD = hnatWork.getThoiDiemBatDau(), TDKT = hnatWork.getThoiDiemKetThuc();
            Instant instant = TDBD.toInstant();
            LocalDate localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
            TDBD_DatePicker.setValue(localDate);
            instant = TDKT.toInstant();
            localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
            TDKT_DatePicker.setValue(localDate);
            TDBD_GioField.setText(String.valueOf(TDBD.getHours()));
            TDBD_PhutField.setText(String.valueOf(TDBD.getMinutes()));
            TDKT_GioField.setText(String.valueOf(TDKT.getHours()));
            TDKT_PhutField.setText(String.valueOf(TDKT.getMinutes()));
            
            if(hnatWork.getChitietHoinghi() != null){
                MoTaChiTiet_Field.setText(hnatWork.getChitietHoinghi().getMotaChiTiet());
            }
            
            imageLink_Field.setText(hnatWork.getHinhAnh());
            Image imgFromlinkString;
            boolean isError = false;
            if(hnatWork.getHinhAnh() != null){
                try{
                    imgFromlinkString = new Image(hnatWork.getHinhAnh());
                } catch (Exception ex){
                    imgFromlinkString = defaultImage;
                    imageLink_Field.setText(null);
                    isError = true;
                }
                if(imgFromlinkString.isError()) {
                    imgFromlinkString = defaultImage;
                    imageLink_Field.setText(null);
                    isError = true;
                }
            }else{
                imgFromlinkString = defaultImage;
                imageLink_Field.setText(null);
            }
            ConventionImage_Display.setImage(imgFromlinkString);
            if(isError){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Hình ảnh hội nghị không thể được tải về");
                alert.setContentText("Đường dẫn đến hình ảnh có lỗi, không thể hiển thị ảnh.");  
                alert.show();
            }
            
            diadiemDachon = hnatWork.getDiadiem();
            if(isEditDiadiemOn.get()){
                TenDiaDiemField.clear();
                DiaDiemAddress_Field.clear();
                DiaDiemCapacity_Field.clear();
            }
            if(diadiemDachon == null){
                session.close();
                ChosenDiadiem_Label.setText("Chưa chọn địa điểm");
                SuaDiadiemDangchonButton.setDisable(true);
                isEditDiadiemOn.set(false);
                DeselectSelectionDiadiemButton.setDisable(true);
                DiaDiem_TableView.setDisable(false);
                resetDiadiems();
            }else{
                Integer madd = diadiemDachon.getMaDiaDiem();
                session.close();
                Task<Diadiem> fetchSeparateInstance = new Task<Diadiem>() {
                    @Override
                    protected Diadiem call() throws Exception {
                        return DiadiemBus.getDiadiem_ById(madd);
                    }
                };
                fetchSeparateInstance.setOnSucceeded((t) -> {
                    diadiemDachon = fetchSeparateInstance.getValue();
                    ChosenDiadiem_Label.setText(Integer.toString(madd));
                    SuaDiadiemDangchonButton.setDisable(false);
                    isEditDiadiemOn.set(false);
                    DeselectSelectionDiadiemButton.setDisable(false);
                    DiaDiem_TableView.setDisable(true);
                    resetDiadiems();
                });
                ThreadPool.submit(fetchSeparateInstance);
            }
        }
    }
    
    private void clearAllTextFields(){
        TenHoiNghi_Field.clear();
        
    }
    
    private void resetDiadiems(){
        loaded_pages_diadiem.clear();
        latestLoadDate = new Date();
        if(DiadiemDisplay_Pagination.getCurrentPageIndex() != 0)
            DiadiemDisplay_Pagination.currentPageIndexProperty().set(0);
        else
            createPage(0);
    }

    @FXML
    private void GoBack_OnClicked(MouseEvent event) {
        ThemHoiNghi_BorderPane.setDisable(true);
        ConventionImage_Display.setDisable(true);
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
        ((MainSceneController)loader.getController()).setLogIn(3);
        
        loadingOverlay.setVisible(false);
        ConventionImage_Display.setDisable(false);
        ThemHoiNghi_BorderPane.setDisable(false);
        s.setScene(scene);
    }

    @FXML
    private void AddHoinghiButton_OnClicked(MouseEvent event) {
        if(TenHoiNghi_Field.getText().length() == 0 || MoTaNganGon_Field.getText().length() == 0
                || TDBD_DatePicker.getEditor().getText().length() == 0 
                || TDKT_DatePicker.getEditor().getText().length() == 0
                || TDBD_GioField.getText().length() == 0 || TDBD_PhutField.getText().length() == 0
                || TDKT_GioField.getText().length() == 0 || TDKT_PhutField.getText().length() == 0)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Thiếu thông tin cần thiết");
            alert.setContentText("Xin điền đủ các trường có đánh dấu (*)");  
            alert.show();
            return;
        }
        
        //Get base info
        String tenHn = TenHoiNghi_Field.getText();
        String moTaNganGon = MoTaNganGon_Field.getText();
        
        LocalDate localDate = TDBD_DatePicker.getValue();
        Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
        Date tempD = Date.from(instant);
        tempD.setHours(Integer.valueOf(TDBD_GioField.getText()));
        tempD.setMinutes(Integer.valueOf(TDBD_PhutField.getText()));
        Date TDBD = tempD;
        
        localDate = TDKT_DatePicker.getValue();
        instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
        tempD = Date.from(instant);
        tempD.setHours(Integer.valueOf(TDKT_GioField.getText()));
        tempD.setMinutes(Integer.valueOf(TDKT_PhutField.getText()));
        Date TDKT = tempD;
        
        if(TDKT.before(TDBD) || TDKT.equals(TDBD)){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Thời điểm không hợp lệ");
            alert.setContentText("Thời điểm bắt đầu phải trước thời điểm kết thúc!");  
            alert.show();
            return;
        }
        
        ThemHoiNghi_BorderPane.setDisable(true);
        ConventionImage_Display.setDisable(true);
        loadingOverlay.setVisible(true);
        
        if(diadiemDachon == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Bạn chưa chọn địa điểm");
            alert.setContentText("Vui lòng chọn địa điểm để tạo hội nghị");  
            alert.show();
            loadingOverlay.setVisible(false);
            ConventionImage_Display.setDisable(false);
            ThemHoiNghi_BorderPane.disableProperty().set(false);
            return;
        }
        Hoinghi hn;
        if(hnatWork == null){
            hn = new Hoinghi(diadiemDachon, tenHn, moTaNganGon, TDBD, TDKT, new Date(), new Date());
        }else{
            hnatWork.setTenHn(tenHn);
            hnatWork.setDiadiem(diadiemDachon);
            hnatWork.setMotaNgangon(moTaNganGon);
            hnatWork.setThoiDiemBatDau(TDBD);
            hnatWork.setThoiDiemKetThuc(TDKT);
            hn = hnatWork;
        }
        //Get additional infos
        String linkAnh = imageLink_Field.getText() == null || imageLink_Field.getText().length() == 0 ? null : imageLink_Field.getText();       
        hn.setHinhAnh(linkAnh);

        String motaChiTiet = MoTaChiTiet_Field.getText();
        
        Task<Void> themHoinghiMoi = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                HoinghiBus.saveOrUpdateHoinghi(hn, motaChiTiet);
                return null;
            }
        };
        themHoinghiMoi.setOnSucceeded((t1) -> { 
            Stage s = (Stage)((Node)(event.getSource())).getScene().getWindow();
            Scene scene;
            try{
                loader.setLocation(getClass().getClassLoader().getResource("conventionhub/Scenes/ChiTietScene.fxml"));
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
            ChiTietSceneController controller = loader.getController();
            controller.setTabToReturnTo(3);
            controller.setHoiNghi(hn);

            loadingOverlay.setVisible(false);
            ConventionImage_Display.setDisable(false);
            ThemHoiNghi_BorderPane.setDisable(false);
            s.setScene(scene);
        });
        themHoinghiMoi.setOnFailed((t2) -> {
            Exception exc = (Exception) themHoinghiMoi.getException();
            if(exc instanceof HoinghiBus.HoinghiBusException){
                HoinghiBus.HoinghiBusException ht = (HoinghiBus.HoinghiBusException) exc;
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(ht.getMessage());
                alert.setContentText(ht.getExplanationString());  
                alert.show();
            }else{
                exc.printStackTrace(System.err);
            }
            loadingOverlay.setVisible(false);
            ConventionImage_Display.setDisable(false);
            ThemHoiNghi_BorderPane.disableProperty().set(false);
        });
        ThreadPool.submit(themHoinghiMoi);
        
    }

    @FXML
    private void BrowseImage_OnClicked(MouseEvent event) {
        File chosenFile = innerFileChooser.showOpenDialog(stageForFileChooser);
        if(chosenFile != null){
            ThemHoiNghi_BorderPane.setDisable(true);
            ConventionImage_Display.setDisable(true);
            loadingOverlay.setVisible(true);
            
            String linkString = chosenFile.toURI().toString();
            if(linkString.length() > 260){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Lỗi đường dẫn file quá dài");
                alert.setContentText("Đường dẫn file quá dài, xin cung cấp đường dẫn khác");  
                alert.show();
                loadingOverlay.setVisible(false);
                ConventionImage_Display.setDisable(false);
                ThemHoiNghi_BorderPane.setDisable(false);
                return;
            }
            imageLink_Field.setText(linkString);
            Image imgFromlinkString;
            boolean isError = false;
            try{
                imgFromlinkString = new Image(linkString);
            } catch (Exception ex){
                imgFromlinkString = defaultImage;
                imageLink_Field.setText(null);
                isError = true;
            }
            if(imgFromlinkString.isError()) {
                imgFromlinkString = defaultImage;
                imageLink_Field.setText(null);
                isError = true;
            }
            ConventionImage_Display.setImage(imgFromlinkString);
            if(isError){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Hình ảnh hội nghị không thể được tải về");
                alert.setContentText("Đường dẫn đến hình ảnh có lỗi, không thể hiển thị ảnh.");  
                alert.show();
            }
            
            loadingOverlay.setVisible(false);
            ConventionImage_Display.setDisable(false);
            ThemHoiNghi_BorderPane.setDisable(false);
        }
    }

    @FXML
    private void ResetDefaultImage_OnClicked(MouseEvent event) {
        ThemHoiNghi_BorderPane.setDisable(true);
        ConventionImage_Display.setDisable(true);
        loadingOverlay.setVisible(true);
        
        imageLink_Field.clear();
        ConventionImage_Display.setImage(defaultImage);
        
        loadingOverlay.setVisible(false);
        ConventionImage_Display.setDisable(false);
        ThemHoiNghi_BorderPane.setDisable(false);
    }

    @FXML
    private void BoChonDiaDiemButton_OnClicked(MouseEvent event) {
        if(diadiemDachon == null) return;
        diadiemDachon = null;
        ChosenDiadiem_Label.setText("Chưa chọn địa điểm");
        SuaDiadiemDangchonButton.setDisable(true);
        if(isEditDiadiemOn.get()){
            TenDiaDiemField.clear();
            DiaDiemAddress_Field.clear();
            DiaDiemCapacity_Field.clear();
        }
        isEditDiadiemOn.set(false);
        DeselectSelectionDiadiemButton.setDisable(true);
        DiaDiem_TableView.setDisable(false);
    }

    @FXML
    private void RefreshDSDiaDiem_OnClicked(MouseEvent event) {
        resetDiadiems();
    }

    @FXML
    private void ThemDiaDiemButton_OnClicked(MouseEvent event) {
        if(DiaDiemAddress_Field.getText().length() == 0 || TenDiaDiemField.getText().length() == 0 || DiaDiemCapacity_Field.getText().length() == 0){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Không thể để trống");
            alert.setContentText("Không thể để các thông tin của địa điểm là rỗng");  
            alert.show();
            return;
        }
        ThemHoiNghi_BorderPane.setDisable(true);
        ConventionImage_Display.setDisable(true);
        loadingOverlay.setVisible(true);
        if(isEditDiadiemOn.get()){
            diadiemDachon.setTenDiaDiem(TenDiaDiemField.getText());
            diadiemDachon.setDiaChi(DiaDiemAddress_Field.getText());
            diadiemDachon.setSucChua(Integer.valueOf(DiaDiemCapacity_Field.getText()));
            
            Task<Void> updateDiadiemTask = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    DiadiemBus.saveOrUpdateDiadiem(diadiemDachon);
                    return null;
                }
            };
            
            updateDiadiemTask.setOnSucceeded((t) -> {
                isEditDiadiemOn.set(!isEditDiadiemOn.get());
                TenDiaDiemField.clear();
                DiaDiemAddress_Field.clear();
                DiaDiemCapacity_Field.clear();
                resetDiadiems();
            });
            
            updateDiadiemTask.setOnFailed((t) -> {
                Exception ex = (Exception) updateDiadiemTask.getException();
                if(ex instanceof DiadiemBus.DiadiemBusException){
                    DiadiemBus.DiadiemBusException exc = (DiadiemBus.DiadiemBusException) ex;
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(exc.getMessage());
                    alert.setContentText(exc.getExplanationString());  
                    alert.show();
                    loadingOverlay.setVisible(false);
                    ConventionImage_Display.setDisable(false);
                    ThemHoiNghi_BorderPane.setDisable(false);
                }
            });
            
            ThreadPool.submit(updateDiadiemTask);
        }else{
            Date d = new Date();
            Diadiem newDD = new Diadiem(TenDiaDiemField.getText(), DiaDiemAddress_Field.getText(), Integer.valueOf(DiaDiemCapacity_Field.getText()), d, d);
            Task<Void> saveDiadiemTask = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    DiadiemBus.saveOrUpdateDiadiem(newDD);
                    return null;
                }
            };
            
            saveDiadiemTask.setOnSucceeded((t) -> {
                TenDiaDiemField.clear();
                DiaDiemAddress_Field.clear();
                DiaDiemCapacity_Field.clear();
                resetDiadiems();
            });
            
            saveDiadiemTask.setOnFailed((t) -> {
                Exception ex = (Exception) saveDiadiemTask.getException();
                if(ex instanceof DiadiemBus.DiadiemBusException){
                    DiadiemBus.DiadiemBusException exc = (DiadiemBus.DiadiemBusException) ex;
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(exc.getMessage());
                    alert.setContentText(exc.getExplanationString());  
                    alert.show();
                    loadingOverlay.setVisible(false);
                    ConventionImage_Display.setDisable(false);
                    ThemHoiNghi_BorderPane.setDisable(false);
                }
            });
            
            ThreadPool.submit(saveDiadiemTask);
        }
    }
    
    private void setDiadiems(ObservableList<Diadiem> list){
        DiaDiem_TableView.setItems(list);
    }
    
    private void createPage(int page){
        ThemHoiNghi_BorderPane.disableProperty().set(true);
        ConventionImage_Display.setDisable(true);
        loadingOverlay.setVisible(true);
        
        int maxpagesize = 5;
        
        //Load page numbers
        Task<Integer> getPageNumbers = new Task<Integer>() {
            @Override
            protected Integer call() throws Exception {
                return DiadiemBus.getTotalPages_Diadiem(maxpagesize, latestLoadDate);
            }
        };
        
        getPageNumbers.setOnSucceeded((t) -> {
            //Set indicator
            DiadiemDisplay_Pagination.setPageCount(getPageNumbers.getValue());
            //Load page
            if(!loaded_pages_diadiem.containsKey(page + 1)){
                Task<ObservableList<Diadiem>> getPage = new Task<ObservableList<Diadiem>>() {
                    @Override
                    protected ObservableList<Diadiem> call() throws Exception {
                        return DiadiemBus.getAll_Diadiem_AtPage(page + 1, maxpagesize, latestLoadDate);
                    }
                };
                getPage.setOnSucceeded((t1) -> {
                    loaded_pages_diadiem.put(page + 1, getPage.getValue());
                    setDiadiems(getPage.getValue());
                    loadingOverlay.setVisible(false);
                    ConventionImage_Display.setDisable(false);
                    ThemHoiNghi_BorderPane.disableProperty().set(false);
                });
                ThreadPool.submit(getPage);
            }else{  
                setDiadiems(loaded_pages_diadiem.get(page + 1));                    
                loadingOverlay.setVisible(false);
                ConventionImage_Display.setDisable(false);
                ThemHoiNghi_BorderPane.disableProperty().set(false);
            }
        });
        ThreadPool.submit(getPageNumbers);
    }

    @FXML
    private void SuaDiaDiemDangChonButton_OnClicked(MouseEvent event) {
        isEditDiadiemOn.set(!isEditDiadiemOn.get());
        if(isEditDiadiemOn.get()){
            ThemHoiNghi_BorderPane.setDisable(true);
            ConventionImage_Display.setDisable(true);
            loadingOverlay.setVisible(true);
            Task<Diadiem> refreshDiadiem = new Task<Diadiem>() {
                @Override
                protected Diadiem call() throws Exception {
                    Session session = HibernateUtils.getSessionFactory().openSession();
                    session.refresh(diadiemDachon);
                    session.close();
                    return diadiemDachon;
                }
            };
            refreshDiadiem.setOnSucceeded((t) -> {
                Diadiem dd = refreshDiadiem.getValue();
                TenDiaDiemField.setText(dd.getTenDiaDiem());
                DiaDiemAddress_Field.setText(dd.getDiaChi());
                DiaDiemCapacity_Field.setText(String.valueOf(dd.getSucChua()));
                loadingOverlay.setVisible(false);
                ConventionImage_Display.setDisable(false);
                ThemHoiNghi_BorderPane.setDisable(false);
            });
            ThreadPool.submit(refreshDiadiem);
        }else{
            TenDiaDiemField.clear();
            DiaDiemAddress_Field.clear();
            DiaDiemCapacity_Field.clear();
        }
    }
    
}
