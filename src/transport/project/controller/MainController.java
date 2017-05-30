package transport.project.controller;


import java.awt.Toolkit;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class MainController implements Initializable {

    @FXML
    private AnchorPane mainPane;

    @FXML
    private TextField loginField;

    @FXML
    private Group loginGroup;



    @FXML
    private void login(ActionEvent event) {
        try { Parent root;

           if (loginField.getText().equals("d"))
           root = FXMLLoader.load(getClass().getResource("/transport/project/view/DriverMenuMainView.fxml"));
           else  root = FXMLLoader.load(getClass().getResource("/transport/project/view/ManagerMenuMainView.fxml"));

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
      Toolkit kit = Toolkit.getDefaultToolkit();
      loginGroup.setLayoutX((kit.getScreenSize().width-1110)/2);
      loginGroup.setLayoutY((kit.getScreenSize().height-690)/2);


    }

}
