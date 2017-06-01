package transport.project.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class ValuesChecker {

    public static boolean checkDecimal(String type, String decimal) {
       boolean result =  Pattern.matches("[1-9][0-9]*[.]?[0-9]*", decimal)
                || Pattern.matches("[0][.][1-9]+", decimal);
       
       if (!result) printError(type);
       return result;
       
    }
    
    public static boolean checkString(String fieldType, String string) {
       boolean result =  Pattern.matches("[a-zA-Z0-9]{2,}[ ]?[a-zA-Z0-9]*", string);
       
       if (!result) printError(fieldType);
       return result;
    }
    
    public static boolean checkDate(String type, LocalDate date) {
       boolean result = (date.isAfter(LocalDate.now()));
       
       if (!result) printError(type);
       return result;
    }
    
    public static boolean checkDates(LocalDate startDate, LocalDate endDate) {
       boolean result = (endDate.isAfter(startDate));
       
       if (!result) printError("Chronologia dat");
       return result;
    }
    
    public static boolean checkDriverAvailibility(String fieldType, String choice) {
      // jakie zapytanie?
        return true;
    }
    
    public static boolean checkUnique(String type, String table, String column, String value) {
        try {
            String query = "SELECT * FROM " + table + " WHERE " + column + "=" + value +";";
            ResultSet resultSet = DatabaseToolkit.getInstance().executeQuery(query);
            if(resultSet.next()) {
                printError(type);
                return false;
            } else return true;
        } catch (SQLException ex) {
            Logger.getLogger(ValuesChecker.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
  
    public static boolean checkEmail(String type, String email) {
       boolean result =  Pattern.matches("[a-zA-Z0-9]{2,}[.]?[a-zA-Z0-9]{2,}[@][a-zA-Z0-9]{2,}+[.][a-zA-Z0-9]{2,}", email);
       
       if (!result) printError(type);
       return result;
    }

    public static boolean checkPassword(String type, String pass, String pass2) {
       boolean result =  pass.equals(pass2);
       if (!result) printError(type);
       return result;
    }
    
    public static void printError(String fieldType) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Błąd");
        alert.setHeaderText("Niestety nastąpiło błędne wprowadzenie danych. Problem: ");
        alert.setContentText(fieldType);

        alert.showAndWait();
    }
}
