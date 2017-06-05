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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import transport.project.model.Driver;
import transport.project.model.Vehicle;
import transport.project.util.DatabaseToolkit;
import transport.project.util.ValuesChecker;

public class ManagerDriversManipulateDataController implements Initializable {

    @FXML
    private Label titleLabel, label1, label2, label3, label4;
    
    @FXML 
    private Button executeButton;
    
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
    
    private final int INSERT = 1; 
    private final int UPDATE = 2; 
    private final int SELECT = 3; 
    
    private int state;
    
    private Driver driver;

    void initState(int state) {
    this.state = state;
    if(state==SELECT){ 
         titleLabel.setText("WYSZUKIWANIE");
         executeButton.setText("WYSZUKAJ");
         usernameField.setVisible(false);
         emailField.setVisible(false);
         passwordField.setVisible(false);
         passwordField2.setVisible(false);
         label1.setVisible(false);
         label2.setVisible(false);
         label3.setVisible(false);
         label4.setVisible(false);
     }
    }
    
    public void initState(int state, Driver driver) {
    this.state = state;
    this.driver = driver;
     if(state==UPDATE) {
         firstNameField.setText(driver.getFirstName());
         lastNameField.setText(driver.getLastName());
         hireDateField.setValue(driver.getHireDate().toLocalDate());
         usernameField.setVisible(false);
         emailField.setVisible(false);
         passwordField.setVisible(false);
         passwordField2.setVisible(false);
         label1.setVisible(false);
         label2.setVisible(false);
         label3.setVisible(false);
         label4.setVisible(false);
         titleLabel.setText("MODYFIKACJA");
         executeButton.setText("ZAPISZ OPIS");
      }
    }
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }   
    
    @FXML
    void executeUpdate(ActionEvent event) {
      if(state==INSERT)
          if(checkUserData() && checkDriverData()) {
              addUser();
              addDriver(getUserId());
          }
      if(state==UPDATE) {
          if (checkDriverData()) updateDriver();}
      if(state==SELECT)
          selectDriver();
    }
    
    public boolean checkUserData() {
        return ( ValuesChecker.checkUnique("Unikalność nazwy użytkownika", "user", "username", "\""+usernameField.getText()+"\"")
                && ValuesChecker.checkEmail("E-mail", emailField.getText())
                && ValuesChecker.checkPassword("Hasło", passwordField.getText(), passwordField2.getText())                
                );
    }
    
    public boolean checkDriverData() {
        return ( ValuesChecker.checkString("Imię", firstNameField.getText())
                && ValuesChecker.checkString("Nazwisko", lastNameField.getText())
                && ValuesChecker.checkDate("Data zatrudnienia", hireDateField.getValue()));
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
            Logger.getLogger(ManagerDriversManipulateDataController.class.getName()).log(Level.SEVERE, null, ex);
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

    private void updateDriver() {
   String query = "UPDATE `driver` SET "
                + "first_name = \"" + firstNameField.getText() + "\", "
                + "last_name = \"" + lastNameField.getText() + "\", "
                + "hire_date = \'" + hireDateField.getValue() + "\' " 
                + "WHERE driver_id = " + driver.getNumber() +";";
   System.out.println(query);
        DatabaseToolkit.getInstance().executeUpdate(query);
        Stage stage = (Stage) hireDateField.getScene().getWindow();
        stage.close();
    }

    private void selectDriver() {
        String query = prepareQuery();
        ResultSet resultSet = DatabaseToolkit.getInstance().executeQuery(query);
        showResults(resultSet);
    }
    
    private String prepareQuery() {
        StringBuilder query = new StringBuilder("SELECT * FROM `driver` WHERE 1=1");
        if (!firstNameField.getText().equals("")) query.append(" AND first_name = \"" + firstNameField.getText()+"\" ");
        if (!lastNameField.getText().equals("")) query.append(" AND last_name = \"" + lastNameField.getText()+"\" ");
        if (hireDateField.getValue()!=null) query.append(" AND hire_date = \'" + hireDateField.getValue()+"\' ");
         query.append(";");
        return query.toString();
    }
    
    private void showResults(ResultSet resultSet) {
        StringBuilder formattedResult = new StringBuilder(String.format("%25s  %25s  %10s\n",
                    "IMIĘ", "NAZWISKO", "DATA ZATR."));
        
        try {
            while(resultSet.next()) { 
                formattedResult.append(String.format("%25s  %25s  %10s\n",
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getDate("hire_date").toString()
                ));
               }   
          Alert alert = new Alert(Alert.AlertType.INFORMATION);
          alert.setTitle("Wyniki wyszukiwania");
          alert.setHeaderText("Na podstawie Twoich warunków wyszukano następujące pozycje: ");
          alert.setContentText(formattedResult.toString());

        alert.showAndWait();
        } catch (SQLException ex) {
            Logger.getLogger(ManagerVehiclesManipulateDataController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
