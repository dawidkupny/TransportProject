package transport.project.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import transport.project.util.DatabaseToolkit;
import transport.project.util.ValuesChecker;

public class ManagerVehiclesTabEditController implements Initializable {

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
    
    private boolean editVehicle = false;

        public boolean isEditVehicle() {
            return editVehicle;
        }

        public void setEditVehicle(boolean editVehicle) {
            this.editVehicle = editVehicle;
        }

    private final int VEHICLE_AVAILIBLE = 1; 
    
    private final int MANAGER_ID = 1; 
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }  
    
    @FXML
    void executeUpdate(ActionEvent event) {
    if(checkData()) 
         if(!isEditVehicle()) addVehicle();
            else updateVehicle();
    }
    
    public boolean checkData() {
      return (ValuesChecker.checkString("Numer rejestracyjny", registrationNumberField.getText())
          && ValuesChecker.checkUnique("Unikalność identyfikatora","vehicle","registration_number","\""+registrationNumberField.getText()+"\"")
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
