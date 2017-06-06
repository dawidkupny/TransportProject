package transport.project.controller;

import java.awt.Toolkit;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.security.crypto.bcrypt.BCrypt;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import transport.project.util.DatabaseToolkit;


public class MainController implements Initializable {

	public final String PASSWORD = "SELECT password, username, user_id FROM transport.user WHERE username = ";

    @FXML
    private AnchorPane mainPane;

    @FXML
    private TextField loginField;

    @FXML
    private Group loginGroup;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label messageLabel;

    private static int userId;

    public static int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		MainController.userId = userId;
	}

	private static final int ADMIN_ID = 1;

	@FXML
    private void login(ActionEvent event) {
        try { Parent root;

            if ((validate(loginField.getText(), passwordField.getText())) && getUserId() == ADMIN_ID) {
            	root = FXMLLoader.load(getClass().getResource("/transport/project/view/ManagerMenuMainView.fxml"));
            	setStage(root);
            }
            else if(validate(loginField.getText(), passwordField.getText())) {
            	root = FXMLLoader.load(getClass().getResource("/transport/project/view/DriverMenuMainView.fxml"));
            	setStage(root);
            }
            else
            	messageLabel.setVisible(true);

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

    private boolean validate(String username, String password) {
    	DatabaseToolkit dbToolkit = DatabaseToolkit.getInstance();
        ResultSet resultSet = dbToolkit.executeQuery(PASSWORD+"'"+username+"';");
        boolean result = false;
        try {
			while(resultSet.next()) {
				if((BCrypt.checkpw(password, resultSet.getString(1))) && (username.equals(resultSet.getString(2)))) {
					result = true;
					setUserId(resultSet.getInt(3));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		dbToolkit.disconnect();
		return result;
    }

    private void setStage(Parent root) {
    	Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }
}
