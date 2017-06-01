package transport.project.controller;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import transport.project.util.DatabaseToolkit;
import transport.project.util.ValuesChecker;


public class ManagerCoursesTabEditController implements Initializable {

   
    @FXML
    private TextField distanceField;

    @FXML
    private TextField startingPointField;

    @FXML
    private TextField destinationField;

    @FXML
    private DatePicker startDateField;

    @FXML
    private DatePicker endDateField;

    @FXML
    private TextField loadField;

    @FXML
    private TextField cubatureField;

    @FXML
    private ChoiceBox<?> driverField;
    
    private final String MANAGER_ID = "1";
    
    private boolean editCourse = false;

        public boolean isEditCourse() {
            return editCourse;
        }

        public void setEditCourse(boolean editCourse) {
            this.editCourse = editCourse;
        }
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        addDrivers();
    }    
    
    public void addDrivers() {
        try {
            String query = "SELECT d.driver_id, d.first_name, d.last_name "
                     + "FROM driver d; ";
            ResultSet resultSet = DatabaseToolkit.getInstance().executeQuery(query);
            ObservableList drivers = FXCollections.observableArrayList();
            while(resultSet.next()) {
                drivers.add(resultSet.getString("d.driver_id") + ": " + 
                        resultSet.getString("d.first_name")+" "+resultSet.getString("d.last_name"));
            }
            driverField.setItems(drivers);
        } catch (SQLException ex) {
            Logger.getLogger(ManagerCoursesTabEditController.class.getName()).log(Level.SEVERE, null, ex);
        }
            
    }
    
    @FXML
    public void executeUpdate() {
        String vehicle = chooseVehicle();
        if(vehicle.equals("")) ValuesChecker.printError("Brak odpowiedniego pojazdu");
        else {
            if(checkData()) 
               if(!isEditCourse()) addCourse(vehicle);
            else updateCourse();
        } 
    }
    
    
    public String chooseVehicle() {
        return 2+"";
        //implement algorithm
    }
            
    public boolean checkData() {
       return (ValuesChecker.checkDecimal("Dystans", distanceField.getText())
               && ValuesChecker.checkString("Miejsce początkowe", startingPointField.getText())
               && ValuesChecker.checkString("Miejsce końcowe", destinationField.getText())
               && startDateField.getValue()!=null && ValuesChecker.checkDate("Data początkowa", startDateField.getValue())
               && endDateField.getValue()!=null && ValuesChecker.checkDate("Data końcowa", endDateField.getValue())
               && ValuesChecker.checkDates(startDateField.getValue(), endDateField.getValue())
               && ValuesChecker.checkDecimal("Ładunek", loadField.getText())
               && ValuesChecker.checkDecimal("Kubatura", cubatureField.getText())
               && ValuesChecker.checkDriverAvailibility("Kierowca niedostępny we wskazanym czasie",
                                   driverField.getValue().toString()));         
    }
    
    public void addCourse(String vehicle) {
        String query = "INSERT INTO `order` (distance, starting_point, destination, start_date, end_date,"
                + " load_weight, cubature, incineration, vehicle_registration_id, vehicle_user_user_id,"
                + " driver_driver_id, driver_user_user_id)"
                + " VALUES "
                + "("+distanceField.getText() + ",\""
                     + startingPointField.getText() + "\",\""
                     + destinationField.getText() + "\",\'"
                     + startDateField.getValue() + "\',\'"
                     + endDateField.getValue() + "\',"
                     + loadField.getText() + ","
                     + cubatureField.getText() + ","
                     + "0.0," 
                     + vehicle + ","
                     + MANAGER_ID +","
                     + driverField.getValue().toString().split(":")[0] + ","
                     + "2);";
        DatabaseToolkit.getInstance().executeUpdate(query);
        Stage stage = (Stage) driverField.getScene().getWindow();
        stage.close();
    }

    private void updateCourse() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
