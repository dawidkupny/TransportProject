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
import transport.project.model.Driver;
import transport.project.util.DatabaseToolkit;

public class ManagerDriversTabController implements Initializable {
    final String ALL_DATA = "SELECT driver_id, first_name, last_name, hire_date FROM driver;";

    @FXML 
    private TableView driversTable;
    
    @FXML
    private TableColumn driverIdColumn;
    
    @FXML
    private TableColumn driverFirstNameColumn;
    
    @FXML
    private TableColumn driverLastNameColumn;
    
    @FXML
    private TableColumn driverHireDateColumn;
    
    private DatabaseToolkit databaseToolkit;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {  
        driverIdColumn.setCellValueFactory(new PropertyValueFactory<>("number"));
        driverFirstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        driverLastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        driverHireDateColumn.setCellValueFactory(new PropertyValueFactory<>("hireDate"));
        
        databaseToolkit = DatabaseToolkit.getInstance();
        
        driversTable.setItems(searchData(ALL_DATA));
        
      
        
    }   
    
    public ObservableList<Driver> searchData(String query) {
        ObservableList<Driver> observableList = FXCollections.observableArrayList();
        try {
            ResultSet resultSet = databaseToolkit.executeQuery(query);
            
            while(resultSet.next()) {
                observableList.add(new Driver(resultSet.getInt("driver_id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getDate("hire_date")
                ));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ManagerDriversTabController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return observableList;
    }
    
}
