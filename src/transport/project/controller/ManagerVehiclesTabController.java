package transport.project.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
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
    
    @FXML
    public void remove() {
       String contractToRemove = showRemoveDialog().split(":")[0];
       if(!contractToRemove.equals("")) {
          databaseToolkit.executeUpdate("DELETE FROM vehicle WHERE registration_number=\""+contractToRemove+"\";");
          vehiclesTable.setItems(searchData(ALL_DATA));
       }
    }
    
    public String showRemoveDialog() {
        ArrayList<String> choices = new ArrayList();
        searchData(ALL_DATA).forEach(x -> choices.add(x.getRegistrationNumber() + ": "
                +x.getBrand()+ " " + x.getModel()));
        ChoiceDialog<String> dialog = new ChoiceDialog<>("",choices);
        dialog.setTitle("Usuwanie pojazdu.");
        dialog.setHeaderText("Aby usunąć pojazd z systemu, wybierz go z poniższej listy. \n"
                + "TEJ OPERACJI NIE DA SIĘ COFNĄĆ!");
        dialog.setContentText("Pojazd do usunięcia: ");
        Optional<String> optional = dialog.showAndWait();
        return optional.get();
        }
    
    @FXML
    public void add() {
        openWindow("/transport/project/view/ManagerVehiclesTabEditView.fxml");
        vehiclesTable.setItems(searchData(ALL_DATA));
    }

    private void openWindow(String resource) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(resource));
            Scene scene = new Scene(root);
            
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(ManagerCoursesTabController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
