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
import javafx.scene.Scene;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import transport.project.model.Driver;
import transport.project.model.Vehicle;
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
    
    private final int INSERT = 1; 
    private final int UPDATE = 2; 
    private final int SELECT = 3; 
    
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
    
     private void openWindow(int state, Driver driver) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/transport/project/view/ManagerDriversManipulateDataView.fxml"));
            
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setScene(new Scene((Pane) loader.load()));
            
            ManagerDriversManipulateDataController controller =
                    loader.<ManagerDriversManipulateDataController>getController();
            if(state==UPDATE) controller.initState(state,driver);
            else controller.initState(state);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(ManagerVehiclesTabController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    public void add() {
        openWindow(INSERT,null);
    }
    
    @FXML
    public void remove() {
       String driverToRemove = showRemoveDialog().split(":")[0];
       if(!driverToRemove.equals("")) {
           try {
               ResultSet resultSet = databaseToolkit.executeQuery("SELECT user_user_id FROM driver WHERE driver_id="+driverToRemove+";");
               resultSet.next();
               databaseToolkit.executeUpdate("DELETE FROM driver WHERE driver_id="+driverToRemove+";");
               databaseToolkit.executeUpdate("DELETE FROM user WHERE user_id="+resultSet.getInt("user_user_id")+";");
               
               driversTable.setItems(searchData(ALL_DATA));
           } catch (SQLException ex) {
               Logger.getLogger(ManagerDriversTabController.class.getName()).log(Level.SEVERE, null, ex);
           }
       }
    }
    
    public String showRemoveDialog() {
        ArrayList<String> choices = new ArrayList();
        searchData(ALL_DATA).forEach(x -> choices.add(x.getNumber()+": "+x.getFirstName()+" "
                +x.getLastName()));
        ChoiceDialog<String> dialog = new ChoiceDialog<>("",choices);
        dialog.setTitle("Usuwanie kierowcy.");
        dialog.setHeaderText("Aby usunąć pracownika z systemu, wybierz go z poniższej listy. \n"
                + "Pamiętaj, że wiąże się to również z usunięciem jego konta z systemu.\n"
                + "TEJ OPERACJI NIE DA SIĘ COFNĄĆ!");
        dialog.setContentText("Kierowca do usunięcia: ");
        Optional<String> optional = dialog.showAndWait();
        return optional.get();
        }
    
    @FXML
    public void update() {
        String driverId = showUpdateDialog();
        if(!driverId.equals(""))
        openWindow(UPDATE,searchData("SELECT * FROM `driver` WHERE driver_id="+driverId+";").get(0));
    }
    
      public String showUpdateDialog() {
        ArrayList<String> choices = new ArrayList();
        searchData(ALL_DATA).forEach(x -> choices.add(x.getNumber()+": " 
                +x.getFirstName()+" "+x.getLastName()));
        ChoiceDialog<String> dialog = new ChoiceDialog<>("",choices);
        dialog.setTitle("Edytowanie kierowcy");
        dialog.setHeaderText("Aby edytować dane kierowcy w systemie, wybierz go z poniższej listy. \n");
        dialog.setContentText("Kierowca do modyfikacji: ");
        Optional<String> optional = dialog.showAndWait();
        return optional.get().split(":")[0];
    }
      
    @FXML
    public void select() {
        openWindow(SELECT,null);
    }  
    
    @FXML
    public void refresh() {
        driversTable.setItems(searchData(ALL_DATA));   
    }  
    

}

