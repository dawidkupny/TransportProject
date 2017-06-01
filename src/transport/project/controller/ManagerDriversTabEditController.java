package transport.project.controller;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import transport.project.util.DatabaseToolkit;
import transport.project.util.ValuesChecker;

public class ManagerDriversTabEditController implements Initializable {

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private DatePicker hireDateField;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField passwordField2;
    
    private boolean editDriver = false;

    public boolean isEditDriver() {
        return editDriver;
    }

    public void setEditDriver(boolean editDriver) {
        this.editDriver = editDriver;
    }
    
    

   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }   
    
    @FXML
    void executeUpdate(ActionEvent event) {
        if(checkData()) 
               if(!isEditDriver())  {
                   addUser();
                   addDriver(getUserId());
               }
            else { updateUser();
                   updateDriver();
               }
    }
    
    public boolean checkData() {
        return ( ValuesChecker.checkString("Imię", firstNameField.getText())
                && ValuesChecker.checkString("Nazwisko", lastNameField.getText())
                && ValuesChecker.checkDate("Data zatrudnienia", hireDateField.getValue())
                && ValuesChecker.checkUnique("Unikalność nazwy użytkownika", "user", "username", "\""+usernameField.getText()+"\"")
                && ValuesChecker.checkEmail("E-mail", emailField.getText())
                && ValuesChecker.checkPassword("Hasło", passwordField.getText(), passwordField2.getText())                
                );
    }
    
    public void addUser() {
         String query = "INSERT INTO `user` (username, email, password)"
                + " VALUES (\""
                 +usernameField.getText() + "\",\"" 
                 +emailField.getText() + "\",\""
                 +passwordField.getText() //SZYFROWANIE!!!!
                 +"\");";
         DatabaseToolkit.getInstance().executeUpdate(query);
    }
    
    public String getUserId() {
        try {
            String query = "SELECT user_id FROM user WHERE username=\""+usernameField.getText()+"\";";
            ResultSet resultSet = DatabaseToolkit.getInstance().executeQuery(query);
            resultSet.next();
            return resultSet.getString("user_id");
        } catch (SQLException ex) {
            Logger.getLogger(ManagerDriversTabEditController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public void addDriver(String userId) {
         String query = "INSERT INTO `driver` (first_name, last_name, hire_date, user_user_id)"
                + " VALUES (\""
                 +firstNameField.getText() + "\",\"" 
                 +lastNameField.getText() + "\",\'"
                 +hireDateField.getValue() + "\',"
                 +userId + ");";
        DatabaseToolkit.getInstance().executeUpdate(query);
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.close();
    }

    private void updateUser() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void updateDriver() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
