package conventionhub.views;

import conventionhub.Bus.DiadiemBus;
import conventionhub.Scenes.ChiTietSceneController;
import conventionhub.Scenes.MainSceneController;
import conventionhub.pojos.Diadiem;
import conventionhub.pojos.Hoinghi;
import conventionhub.pojos.TinhtrangxoaHoinghi;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.hibernate.Session;
import utils.HibernateUtils;
import utils.ThreadPool;

public class ConventionCardController implements Initializable {

    private Hoinghi hnHoinghi;
    private Image defaultImage;
    private FXMLLoader loader;
    private MainSceneController innerController;
    
    @FXML
    private Label TenHoiNghi;
    @FXML
    private Label ThoiGianHoiNghi;
    @FXML
    private Label MoTaNganGon;
    @FXML
    private Label MaHoiNghi;
    @FXML
    private ScrollPane CardScrollPane;
    @FXML
    private VBox loadingOverlay;
    @FXML
    private ImageView HinhAnhHN;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loader = new FXMLLoader();
        CardScrollPane.getStylesheets().add("conventionhub/css/conventionCardStylesheet.css");
        defaultImage = new Image("images/default-convention.png");
    }
    
    public void setInnerController(MainSceneController controller){
        innerController = controller;
    }
    
    private void toggleLoading(boolean isLoading){
        CardScrollPane.setDisable(isLoading);
        loadingOverlay.setVisible(isLoading);
    }
    
    public void setHoinghi(Hoinghi hn){
        if(hn == null || hn.getMaHn() == null) return;
        toggleLoading(true);
        CardScrollPane.setVvalue(0);
        this.hnHoinghi = hn;
        MaHoiNghi.setText(hn.getMaHn().toString());
        TenHoiNghi.setText(hn.getTenHn());
        String thoiGian = "Từ " + hn.getThoiDiemBatDau().toString() + " đến " + hn.getThoiDiemKetThuc().toString();
        ThoiGianHoiNghi.setText(thoiGian);
        MoTaNganGon.setText(hn.getMotaNgangon());
        Image image;
        if(hn.getHinhAnh() != null){
            try{
                image = new Image(hn.getHinhAnh());
            } catch (Exception ex){
                image = defaultImage;
            }
            if(image.errorProperty().getValue()){
                image = defaultImage;
            }
        }else{
            image = defaultImage;
        }
        HinhAnhHN.imageProperty().set(image);
        toggleLoading(false);
    }

    @FXML
    private void SeeDetailsButton_OnClicked(MouseEvent event) {
        Hoinghi hn = this.hnHoinghi;
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
                Stage s = (Stage)((Node) event.getSource()).getScene().getWindow();
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
    }
    
}
