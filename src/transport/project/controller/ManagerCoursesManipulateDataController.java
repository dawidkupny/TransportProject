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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import transport.project.model.Course;
import transport.project.util.DatabaseToolkit;
import transport.project.util.ValuesChecker;


public class ManagerCoursesManipulateDataController implements Initializable {

    @FXML
    private Label titleLabel, driverLabel;
    
    @FXML 
    private Button executeButton;
    
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
    
    private final int INSERT = 1; 
    private final int UPDATE = 2; 
    private final int SELECT = 3; 
    
    private int state;
    
    private Course course;

    void initState(int state) {
    this.state = state;
    if(state==SELECT){ 
         driverLabel.setVisible(false);
         driverField.setVisible(false);
         titleLabel.setText("WYSZUKIWANIE");
         executeButton.setText("WYSZUKAJ");
     }
    }
    
    public void initState(int state, Course course) {
    this.state = state;
    this.course = course;
     if(state==UPDATE) {
         distanceField.setText(course.getDistance().toString());
         startingPointField.setText(course.getStartingPoint());
         destinationField.setText(course.getDestination());
         startDateField.setValue(course.getStartDate().toLocalDate());
         endDateField.setValue(course.getEndDate().toLocalDate());
         loadField.setText(course.getLoadWeight().toString());
         cubatureField.setText(course.getCubature().toString());
         titleLabel.setText("MODYFIKACJA");
         executeButton.setText("ZAPISZ OPIS");
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
            Logger.getLogger(ManagerCoursesManipulateDataController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    public void executeUpdate() {
      if(state==INSERT)
          if(checkData()) {
              if(chooseVehicle().equals("-1")) ValuesChecker.printError("Brak odpowiedniego pojazdu");
              else addCourse(chooseVehicle());
          }
      if(state==UPDATE) {
          if (checkData()) updateCourse();}
      if(state==SELECT)
          selectCourse();
    }
    
    
    public String chooseVehicle() {
        try {
            String query = "SELECT registration_id FROM `vehicle` " +
                    " WHERE carrying_capacity>" + loadField.getText() +
                    " AND cubature>" + cubatureField.getText() +
                    " ORDER BY carrying_capacity ASC, cubature ASC " +
                    " LIMIT 1;";
            ResultSet resultSet = DatabaseToolkit.getInstance().executeQuery(query);
            if(!resultSet.next()) {
                ValuesChecker.printError("Brak odpowiedniego pojazdu");
                return "-1";
            }
            else {String registrationId = resultSet.getString("registration_id");
                  query = "UPDATE `vehicle` SET vehicle_availibilty=0 WHERE registration_id="+registrationId+";";
                  DatabaseToolkit.getInstance().executeUpdate(query);
                  return registrationId;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ManagerCoursesManipulateDataController.class.getName()).log(Level.SEVERE, null, ex);
        }
         return "-1";
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
               && driverField.getValue()!=null && ValuesChecker.checkDriverAvailibility("Kierowca niedostępny we wskazanym czasie",
                                   driverField.getValue().toString(), startDateField, endDateField));         
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

    public void updateCourse() {
       String query = "UPDATE `order` SET "
                + "distance = " + distanceField.getText() + ", "
                + "starting_point = \"" + startingPointField.getText()+ "\", "
                + "destination = \"" + destinationField.getText()+ "\", "
                + "start_date = \'" + startDateField.getValue() + "\', "
                + "end_date = \'" + endDateField.getValue() + "\', "
                + "load_weight = " + loadField.getText() + ","
                + "cubature = " + cubatureField.getText() + " "
                + "WHERE order_id = " + course.getNumber() +";";
        DatabaseToolkit.getInstance().executeUpdate(query);
        Stage stage = (Stage) cubatureField.getScene().getWindow();
        stage.close();
    }
    
    void selectCourse() {
        String query = prepareQuery();
        ResultSet resultSet = DatabaseToolkit.getInstance().executeQuery(query);
        showResults(resultSet);
    }
    
    private String prepareQuery() {
        StringBuilder query = new StringBuilder("SELECT * FROM `order` WHERE 1=1");
        if (!distanceField.getText().equals("")) query.append(" AND distance = " + distanceField.getText()+" ");
        if (!startingPointField.getText().equals("")) query.append(" AND starting_point = \"" + startingPointField.getText()+"\" ");
        if (!destinationField.getText().equals("")) query.append(" AND destination = \"" + destinationField.getText()+"\" ");
        if (startDateField.getValue()!=null) query.append(" AND start_date = \'" + startDateField.getValue()+"\' ");
        if (endDateField.getValue()!=null) query.append(" AND end_date = \'" + endDateField.getValue()+"\' ");
        if (!cubatureField.getText().equals("")) query.append(" AND cubature = " + cubatureField.getText());
        if (!loadField.getText().equals("")) query.append(" AND load_weight = " + loadField.getText());
        query.append(";");
        return query.toString();
    }
    
    private void showResults(ResultSet resultSet) {
        StringBuilder formattedResult = new StringBuilder(String.format("%10s  %15s  %15s  %8s  %8s  %8s  %8s\n",
                    "DYSTANS", "POCZĄTEK", "KONIEC", "CZAS POCZ.", "CZAS KON.", "KUBATURA", "ŁADUNEK"));
        
        try {
            while(resultSet.next()) { 
                formattedResult.append(String.format("%10s  %15s  %15s  %8s  %8s  %8s  %8s\n",
                        resultSet.getBigDecimal("distance"),
                        resultSet.getString("starting_point"),
                        resultSet.getString("destination"),
                        resultSet.getDate("start_date"),
                        resultSet.getDate("end_date"),
                        resultSet.getBigDecimal("cubature"),
                        resultSet.getBigDecimal("load_weight")
                ));
               }   
          Alert alert = new Alert(Alert.AlertType.INFORMATION);
          alert.setTitle("Wyniki wyszukiwania");
          alert.setHeaderText("Na podstawie Twoich warunków wyszukano następujące pozycje: ");
          alert.setContentText(formattedResult.toString());

        alert.showAndWait();
        } catch (SQLException ex) {
            Logger.getLogger(ManagerVehiclesManipulateDataController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
