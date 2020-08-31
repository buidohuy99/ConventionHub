package conventionhub.views;

import conventionhub.Scenes.MainSceneController;
import conventionhub.pojos.Hoinghi;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Stack;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;

public class ConventionCards_ViewController implements Initializable {

    private MainSceneController innerController;
    
    @FXML
    private GridPane Cards_GridPane;
    
    private class ConventionCard{
        public Parent view;
        public ConventionCardController controller;
    }
    
    //Views
    Stack<ConventionCard> ConventionCards;
    static int maxPerRow = 3;
    
    FXMLLoader loader;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ConventionCards = new Stack<>();
        loader = new FXMLLoader();
        for(int i = 0; i < maxPerRow; i++){
            ColumnConstraints temp = new ColumnConstraints();
            temp.hgrowProperty().set(Priority.ALWAYS);
            temp.percentWidthProperty().set(100/maxPerRow);
            Cards_GridPane.getColumnConstraints().add(temp);
        }
    }    
    
    public void setConventionsData(ObservableList<Hoinghi> hn){
        if(hn == null) return;
        innerController.toggleLoadingConventionsBorderPane(true);
        int size = hn.size();
        Cards_GridPane.getChildren().clear();
        Cards_GridPane.getRowConstraints().clear();
        Stack<ConventionCard> temp_stack = new Stack<>();
        for(int i = 0; i < size; i++){
            if(i%maxPerRow == 0){
                RowConstraints temp = new RowConstraints();
                temp.vgrowProperty().set(Priority.ALWAYS);
                temp.valignmentProperty().set(VPos.CENTER);
                Cards_GridPane.getRowConstraints().add(temp);
            }
            ConventionCard spareCard;
            if(ConventionCards.empty()){
                spareCard = new ConventionCard();
                loader.setRoot(null);
                loader.setController(null);
                loader.setLocation(getClass().getClassLoader().getResource("conventionhub/views/ConventionCard.fxml"));
                try{
                    spareCard.view = loader.load();
                } catch (IOException e){
                    e.printStackTrace(System.err);
                    return;
                }
                spareCard.controller = loader.getController();
                spareCard.controller.setInnerController(innerController);
            }else{
                spareCard = ConventionCards.pop();
            }
            temp_stack.push(spareCard);
            spareCard.controller.setHoinghi(hn.get(i));
            Cards_GridPane.add(spareCard.view, (int)(i%maxPerRow), (int)(i/maxPerRow));
        }
        ConventionCards.addAll(temp_stack);
        innerController.toggleLoadingConventionsBorderPane(false);
    }
    
    public void setInnerController(MainSceneController controller){
        innerController = controller;
    }
}
