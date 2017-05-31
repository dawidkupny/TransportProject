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
import transport.project.model.Vehicle;
import transport.project.util.DatabaseToolkit;

public class ManagerVehiclesTabController implements Initializable {
    final String ALL_DATA = "SELECT registration_number, vehicle_availibilty, brand, model,"
                                         + "carrying_capacity, cubature FROM vehicle;";
    
    @FXML
    private TableView vehiclesTable;
    
    @FXML
    private TableColumn vehicleIdColumn;
     
    @FXML
    private TableColumn vehicleRegistrationColumn; 
    
    @FXML
    private TableColumn vehicleAvailibilityColumn; 
    
    @FXML
    private TableColumn vehicleBrandColumn; 
    
    @FXML
    private TableColumn vehicleModelColumn; 
    
    @FXML
    private TableColumn vehicleLoadColumn;
    
    @FXML
    private TableColumn vehicleCubatureColumn;
    
    private DatabaseToolkit databaseToolkit;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
      vehicleIdColumn.setCellValueFactory(new PropertyValueFactory<>("number"));
      vehicleRegistrationColumn.setCellValueFactory(new PropertyValueFactory<>("registrationNumber"));
      vehicleAvailibilityColumn.setCellValueFactory(new PropertyValueFactory<>("vehicleAvailibilty"));
      vehicleBrandColumn.setCellValueFactory(new PropertyValueFactory<>("brand"));
      vehicleModelColumn.setCellValueFactory(new PropertyValueFactory<>("model"));
      vehicleLoadColumn.setCellValueFactory(new PropertyValueFactory<>("carryingCapacity"));
      vehicleCubatureColumn.setCellValueFactory(new PropertyValueFactory<>("cubature"));
      
      databaseToolkit = DatabaseToolkit.getInstance();
        
      vehiclesTable.setItems(searchData(ALL_DATA));
    
        
    }    

    private ObservableList<Vehicle> searchData(String query) {
        ObservableList<Vehicle> observableList = FXCollections.observableArrayList();
        try {
            ResultSet resultSet = databaseToolkit.executeQuery(query);
            
            int counter=0;
            while(resultSet.next()) {
                observableList.add(new Vehicle(++counter,
                        resultSet.getString("registration_number"),
                        ((resultSet.getInt("vehicle_availibilty")==1) ? "DOSTĘPNY" : "NIEDOSTĘPNY"),
                        resultSet.getString("brand"),
                        resultSet.getString("model"),
                        resultSet.getBigDecimal("carrying_capacity"),
                        resultSet.getBigDecimal("cubature")
                ));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ManagerDriversTabController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return observableList;
    }
    
}
