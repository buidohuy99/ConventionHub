package conventionhub.views;

import conventionhub.pojos.Hoinghi;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class ConventionCardController implements Initializable {

    @FXML
    private Label TenHoiNghi;
    @FXML
    private Label ThoiGianHoiNghi;
    @FXML
    private Label MoTaNganGon;
    @FXML
    private Label TenDiaDiem;
    @FXML
    private Label SoLuongThamDu;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }   
    
    public void setHoinghi(Hoinghi hn){
        
    }
    
}
