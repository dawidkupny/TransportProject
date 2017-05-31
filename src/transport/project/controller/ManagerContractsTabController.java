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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import transport.project.model.Contract;
import transport.project.util.DatabaseToolkit;

public class ManagerContractsTabController implements Initializable {
    final String ALL_DATA = "SELECT c.contract_id, c.contract_number, c.start_date, "
                          + "c.expiration_date, c.salary, d.driver_id, d.first_name, d.last_name "
                          + "FROM civil_contract c JOIN driver d ON (c.driver_driver_id = d.driver_id);";

    @FXML
    private TableView contractsTable;
    
    @FXML
    private TableColumn contractIdColumn;
     
    @FXML
    private TableColumn contractContractColumn; 
    
    @FXML
    private TableColumn contractDriverColumn; 
    
    @FXML
    private TableColumn contractStartDateColumn; 
    
    @FXML
    private TableColumn contractExpirationDateColumn; 
    
    @FXML
    private TableColumn contractSalaryColumn;
    
    private DatabaseToolkit databaseToolkit;
            
          
    @Override
    public void initialize(URL url, ResourceBundle rb) {
      contractIdColumn.setCellValueFactory(new PropertyValueFactory<>("number"));
      contractContractColumn.setCellValueFactory(new PropertyValueFactory<>("contractNumber"));
      contractDriverColumn.setCellValueFactory(new PropertyValueFactory<>("driverToString"));
      contractStartDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
      contractExpirationDateColumn.setCellValueFactory(new PropertyValueFactory<>("expirationDate"));
      contractSalaryColumn.setCellValueFactory(new PropertyValueFactory<>("salary"));
      
      databaseToolkit = DatabaseToolkit.getInstance();
        
      contractsTable.setItems(searchData(ALL_DATA));
    
    }    
    
    public ObservableList<Contract> searchData(String query) {
        ObservableList<Contract> observableList = FXCollections.observableArrayList();
        try {
            ResultSet resultSet = databaseToolkit.executeQuery(query);
            
            int counter=0;
            while(resultSet.next()) {
                observableList.add(new Contract(++counter,
                        resultSet.getString("c.contract_number"),
                        resultSet.getDate("c.start_date"),
                        resultSet.getDate("c.expiration_date"),
                        resultSet.getBigDecimal("c.salary"),
                        (resultSet.getInt("d.driver_id") + ": " + resultSet.getString("d.first_name") 
                                + " " + resultSet.getString("d.last_name"))
                ));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ManagerDriversTabController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return observableList;
    }
}
