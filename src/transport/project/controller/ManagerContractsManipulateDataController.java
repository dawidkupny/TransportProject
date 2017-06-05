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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import transport.project.model.Contract;
import transport.project.model.Course;
import transport.project.util.DatabaseToolkit;
import transport.project.util.ValuesChecker;

public class ManagerContractsManipulateDataController implements Initializable {

    @FXML
    private Label titleLabel;
    
    @FXML
    private Button executeButton;
    
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
    
    private final int INSERT = 1; 
    private final int UPDATE = 2; 
    private final int SELECT = 3; 
    
    private int state;
    
    private Contract contract;

    void initState(int state) {
    this.state = state;
    if(state==SELECT){ 
         titleLabel.setText("WYSZUKIWANIE");
         executeButton.setText("WYSZUKAJ");
     }
    }
    
    public void initState(int state, Contract contract) {
    this.state = state;
    this.contract = contract;
     if(state==UPDATE) {
         contractField.setText(contract.getContractNumber());
         contractField.setDisable(true);
         startDateField.setValue(contract.getStartDate().toLocalDate());
         endDateField.setValue(contract.getExpirationDate().toLocalDate());
         salaryField.setText(contract.getSalary().toString());
         titleLabel.setText("MODYFIKACJA UMOWY");
         executeButton.setText("ZAPISZ ZMIANY");
      }
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
        if(state==INSERT)
          if( ValuesChecker.checkUnique("Unikalność identyfikatora","civil_contract","contract_number","\""+contractField.getText()+"\"") && checkData()) addContract();
      if(state==UPDATE) {
          if (checkData()) updateContract();}
      if(state==SELECT)
          selectContract();
    }
    
    public boolean checkData() {
        return ((contractField.getText().length()>5) 
                && startDateField.getValue()!=null && ValuesChecker.checkDate("Data rozpoczęcia", startDateField.getValue())
                && endDateField.getValue()!=null && ValuesChecker.checkDate("Data zakończenia", endDateField.getValue())
                && ValuesChecker.checkDates(startDateField.getValue(), endDateField.getValue())
                && ValuesChecker.checkDecimal("Wynagrodzenie", salaryField.getText())
                && driverField.getValue()!=null);
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
      String query = "UPDATE `civil_contract` SET "
                + "start_date = \'" + startDateField.getValue() + "\',"
                + "expiration_date = \'" + endDateField.getValue() + "\', "
                + "salary = " + salaryField.getText() + ","
                + "driver_driver_id = " + driverField.getValue().toString().split(":")[0]+ " "
                + "WHERE contract_number = \"" + contractField.getText().toUpperCase() +"\";";
        DatabaseToolkit.getInstance().executeUpdate(query);
        Stage stage = (Stage) salaryField.getScene().getWindow();
        stage.close();
    }
    
    void selectContract() {
        String query = prepareQuery();
        ResultSet resultSet = DatabaseToolkit.getInstance().executeQuery(query);
        showResults(resultSet);
    }
    
    private String prepareQuery() {
        StringBuilder query = new StringBuilder("SELECT * FROM `civil_contract` WHERE 1=1");
        if (!contractField.getText().equals("")) query.append(" AND contract_id = \"" + contractField.getText()+"\" ");
        if (startDateField.getValue()!=null) query.append(" AND start_date = \'" + startDateField.getValue()+"\' ");
        if (endDateField.getValue()!=null) query.append(" AND expiration_date = \'" + endDateField.getValue()+"\' ");
        if (!salaryField.getText().equals("")) query.append(" AND salary = " + salaryField.getText());
        if (driverField.getValue()!=null) query.append(" AND driver_driver_id = " + driverField.getValue().toString().split(":")[0]);
        query.append(";");
        return query.toString();
    }
    
    private void showResults(ResultSet resultSet) {
        StringBuilder formattedResult = new StringBuilder(String.format("%20s  %13s  %10s  %15s  %8s\n",
                    "NUMER", "POCZĄTEK", "KONIEC", "WYPŁATA", "KIEROWCA"));
        
        try {
            while(resultSet.next()) { 
                formattedResult.append(String.format("%20s  %13s  %10s  %15s  %8s\n",
                        resultSet.getString("contract_number"),
                        resultSet.getDate("start_date"),
                        resultSet.getDate("expiration_date"),
                        resultSet.getString("salary"),
                        resultSet.getString("driver_driver_id")
                ));
               }   
          Alert alert = new Alert(Alert.AlertType.INFORMATION);
          alert.setTitle("Wyniki wyszukiwania");
          alert.setHeaderText("Na podsawie Twoich warunków wyszukano następujące pozycje: ");
          alert.setContentText(formattedResult.toString());

        alert.showAndWait();
        } catch (SQLException ex) {
            Logger.getLogger(ManagerVehiclesManipulateDataController.class.getName()).log(Level.SEVERE, null, ex);
            
        
        }
    }
}
