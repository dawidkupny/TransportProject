package transport.project.controller;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import transport.project.model.Vehicle;
import transport.project.util.DatabaseToolkit;
import transport.project.util.ValuesChecker;

public class ManagerVehiclesManipulateDataController implements Initializable {

    @FXML
    private Label titleLabel;
    
    @FXML
    private Button executeButton;
    
    @FXML
    private TextField registrationNumberField;

    @FXML
    private TextField brandField;

    @FXML
    private TextField modelField;

    @FXML
    private TextField loadField;

    @FXML
    private TextField cubatureField;
    
    private final int VEHICLE_AVAILIBLE = 1; 
    
    private final int MANAGER_ID = 1; 
    
    private final int INSERT = 1; 
    private final int UPDATE = 2; 
    private final int SELECT = 3; 
    
    private int state;
    
    private Vehicle vehicle;

    void initState(int state) {
    this.state = state;
    if(state==SELECT){ 
         titleLabel.setText("WYSZUKIWANIE");
         executeButton.setText("WYSZUKAJ");
     }
    }
    
    public void initState(int state, Vehicle vehicle) {
    this.state = state;
    this.vehicle = vehicle;
     if(state==UPDATE) {
         registrationNumberField.setText(vehicle.getRegistrationNumber());
         registrationNumberField.setDisable(true);
         brandField.setText(vehicle.getBrand());
         modelField.setText(vehicle.getModel());
         loadField.setText(vehicle.getCarryingCapacity().toString());
         cubatureField.setText(vehicle.getCubature().toString());
         titleLabel.setText("MODYFIKACJA POJAZDU");
         executeButton.setText("ZAPISZ OPIS");
      }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) { 
    }  
       
    @FXML
    void execute() {
      if(state==INSERT)
          if( ValuesChecker.checkUnique("Unikalność identyfikatora","vehicle","registration_number","\""+registrationNumberField.getText()+"\"")       
               && checkData()) addVehicle();
      if(state==UPDATE) {
          if (checkData()) updateVehicle();}
      if(state==SELECT)
          selectVehicle();
    }
    public boolean checkData() {
      return (ValuesChecker.checkString("Numer rejestracyjny", registrationNumberField.getText())
          && ValuesChecker.checkString("Marka", brandField.getText())
          && ValuesChecker.checkString("Model", modelField.getText())
          && ValuesChecker.checkDecimal("Ładowność", loadField.getText())
          && ValuesChecker.checkDecimal("Kubatura", cubatureField.getText()));          
    }
    
    public void addVehicle() {
        String query = "INSERT INTO `vehicle` (registration_number, vehicle_availibilty, brand, model, carrying_capacity, "
                + " cubature, user_user_id)"
                + " VALUES "
                + "(\""+ registrationNumberField.getText().toUpperCase() + "\", "
                     + VEHICLE_AVAILIBLE + ", \""
                     + brandField.getText().toUpperCase() + "\",\""
                     + modelField.getText().toUpperCase() + "\", "
                     + loadField.getText() + ","
                     + cubatureField.getText() + ","
                     + MANAGER_ID + ");";
        DatabaseToolkit.getInstance().executeUpdate(query);
        Stage stage = (Stage) loadField.getScene().getWindow();
        stage.close();
    }

    private void updateVehicle() {
        String query = "UPDATE `vehicle` SET "
                + "brand = \"" + brandField.getText().toUpperCase() + "\","
                + "model = \"" + modelField.getText().toUpperCase() + "\", "
                + "carrying_capacity = " + loadField.getText() + ","
                + "cubature = " + cubatureField.getText() + " "
                + "WHERE registration_number = \"" + vehicle.getRegistrationNumber() +"\";";
        DatabaseToolkit.getInstance().executeUpdate(query);
        Stage stage = (Stage) loadField.getScene().getWindow();
        stage.close();
        
    }

    private void selectVehicle() {
        String query = prepareQuery();
        ResultSet resultSet = DatabaseToolkit.getInstance().executeQuery(query);
        showResults(resultSet);
        
    }
    
    private String prepareQuery() {
        StringBuilder query = new StringBuilder("SELECT * FROM `vehicle` WHERE 1=1");
        if (!registrationNumberField.getText().equals("")) query.append(" AND registration_number = \"" + registrationNumberField.getText()+"\" ");
        if (!brandField.getText().equals("")) query.append(" AND brand = \"" + brandField.getText()+"\" ");
        if (!modelField.getText().equals("")) query.append(" AND model = \"" + modelField.getText()+"\" ");
        if (!cubatureField.getText().equals("")) query.append(" AND cubature = " + cubatureField.getText());
        if (!loadField.getText().equals("")) query.append(" AND carrying_capacity = " + loadField.getText());
        query.append(";");
        return query.toString();
    }
    
    private void showResults(ResultSet resultSet) {
        StringBuilder formattedResult = new StringBuilder(String.format("%20s  %13s  %10s  %15s  %8s  %8s\n",
                    "NUMER REJESTRACYJNY", "DOSTĘPNOŚĆ", "MARKA", "MODEL", "ŁADOWNOŚĆ", "KUBATURA"));
        
        try {
            while(resultSet.next()) { 
                formattedResult.append(String.format("%20s  %13s  %10s  %15s  %8s  %8s\n",
                        resultSet.getString("registration_number"),
                        ((resultSet.getInt("vehicle_availibilty")==1) ? "DOSTĘPNY" : "NIEDOSTĘPNY"),
                        resultSet.getString("brand"),
                        resultSet.getString("model"),
                        resultSet.getBigDecimal("carrying_capacity").setScale(2).toPlainString(),
                        resultSet.getBigDecimal("cubature").setScale(2).toPlainString()
                ));
               }   
          System.out.println(formattedResult.toString());
          Alert alert = new Alert(AlertType.INFORMATION);
          alert.setTitle("Wyniki wyszukiwania");
          alert.setHeaderText("Na podsawie Twoich warunków wyszukano następujące pozycje: ");
          alert.setContentText(formattedResult.toString());

        alert.showAndWait();
        } catch (SQLException ex) {
            Logger.getLogger(ManagerVehiclesManipulateDataController.class.getName()).log(Level.SEVERE, null, ex);
            
        
        }
    }
}
