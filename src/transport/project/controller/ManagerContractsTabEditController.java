package transport.project.controller;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import transport.project.util.DatabaseToolkit;
import transport.project.util.ValuesChecker;

public class ManagerContractsTabEditController implements Initializable {

    @FXML
    private TextField contractField;

    @FXML
    private DatePicker startDateField;

    @FXML
    private DatePicker endDateField;

    @FXML
    private TextField salaryField;

    @FXML
    private ChoiceBox<?> driverField;
    
    private boolean editContract = false;

    public boolean isEditContract() {
        return editContract;
    }

    public void setEditContract(boolean editContract) {
        this.editContract = editContract;
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
    public void executeUpdate(ActionEvent event) {
        if(checkData()) 
         if(!isEditContract()) addContract();
            else updateContract();
    }
    
    public boolean checkData() {
        return ((contractField.getText().length()>5) 
                && ValuesChecker.checkUnique("Unikalność identyfikatora","civil_contract","contract_number","\""+contractField.getText()+"\"")
                && startDateField.getValue()!=null && ValuesChecker.checkDate("Data rozpoczęcia", startDateField.getValue())
                && endDateField.getValue()!=null && ValuesChecker.checkDate("Data zakończenia", endDateField.getValue())
                && ValuesChecker.checkDates(startDateField.getValue(), endDateField.getValue())
                && ValuesChecker.checkDecimal("Wynagrodzenie", salaryField.getText())
                && !driverField.getValue().toString().equals(""));
    }
    
    public void addContract() {
         String query = "INSERT INTO `civil_contract` (contract_number, start_date, expiration_date,"
                + " salary, driver_driver_id)"
                + " VALUES "
                + "(\""+ contractField.getText().toUpperCase() + "\",\'"
                     + startDateField.getValue() + "\',\'"
                     + endDateField.getValue() + "\',"
                     + salaryField.getText() + ","
                     + driverField.getValue().toString().split(":")[0] + ");";
        DatabaseToolkit.getInstance().executeUpdate(query);
        Stage stage = (Stage) driverField.getScene().getWindow();
        stage.close();
    }

    private void updateContract() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
