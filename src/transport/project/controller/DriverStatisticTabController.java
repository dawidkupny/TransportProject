package transport.project.controller;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import transport.project.util.DatabaseToolkit;

public class DriverStatisticTabController implements Initializable{
	private final static String EMPTY_INCINERATION = "SELECT o.order_id, o.incineration, o.start_date, o.end_date, o.starting_point, o.destination, "+
							"v.registration_number, CONCAT(v.brand, ' ', v.model) as brand FROM transport.order o JOIN transport.vehicle v WHERE incineration = 0 AND o.vehicle_registration_id = v.registration_id AND driver_driver_id = ";
	private final static String USER = "SELECT driver_id, first_name, last_name, hire_date FROM transport.driver WHERE user_user_id = ";
	private final static String MAX_SALARY = "SELECT MAX(salary) FROM transport.civil_contract WHERE driver_driver_id = ";
	private final static String MIN_SALARY = "SELECT MIN(salary) FROM transport.civil_contract WHERE driver_driver_id = ";
	private final static String MAX_CONTRACT = "SELECT MAX(DATEDIFF(expiration_date, start_date)) FROM transport.civil_contract WHERE driver_driver_id = ";
	private final static String MIN_CONTRACT = "SELECT MIN(DATEDIFF(expiration_date, start_date)) FROM transport.civil_contract WHERE driver_driver_id = ";
	private final static String ALL_CONTRACTS = "SELECT COUNT(contract_number) FROM transport.civil_contract WHERE driver_driver_id = ";
	private final static String THIS_YEAR_CONTRACTS = "SELECT COUNT(contract_number) FROM transport.civil_contract WHERE YEAR(start_date) = YEAR(sysdate()) AND driver_driver_id = ";
	private final static String LAST_YEAR_CONTRACTS = "SELECT COUNT(contract_number) FROM transport.civil_contract WHERE YEAR(start_date)-1 = YEAR(sysdate()) AND driver_driver_id = ";
	private final static String ALL_ORDERS = "SELECT COUNT(*) FROM transport.order WHERE driver_driver_id = ";
	private final static String THIS_YEAR_ORDERS = "SELECT COUNT(*) FROM transport.order WHERE YEAR(start_date) = YEAR(sysdate()) AND driver_driver_id = ";
	private final static String LAST_YEAR_ORDERS = "SELECT COUNT(*) FROM transport.order WHERE YEAR(start_date) = YEAR(sysdate())-1 AND driver_driver_id = ";
	private final static String TOTAL_DISTANCE = "SELECT SUM(distance) FROM transport.order WHERE driver_driver_id = ";
	private final static String LONGEST_DISTANCE = "SELECT MAX(distance) FROM transport.order WHERE driver_driver_id = ";
	private final static String SHORTEST_DISTANCE = "SELECT MIN(distance) FROM transport.order WHERE driver_driver_id = ";
	private final static String MOST_VISITED_CITY_FPART = "SELECT destination FROM transport.order WHERE driver_driver_id = ";
	private final static String MOST_VISITED_CITY_SPART = " GROUP BY destination ORDER BY count(destination) DESC LIMIT 1;";
	private final static String MOST_DRIVING_VEHICLE_FPART = "SELECT CONCAT(brand, ' ', model) from transport.vehicle JOIN transport.order ON registration_id = vehicle_registration_id WHERE driver_driver_id = ";
	private final static String MOST_DRIVING_VEHICLE_SPART = " GROUP BY brand ORDER BY count(brand) DESC LIMIT 1;";
	private final static String LARGEST_LOAD = "SELECT MAX(load_weight) FROM transport.order WHERE driver_driver_id = ";
	private final static String SMALLEST_LOAD = "SELECT MIN(load_weight) FROM transport.order WHERE driver_driver_id = ";

	@FXML
    private Label lastNameLabel;

    @FXML
    private Label hireDateLabel;

	@FXML
    private Label idLabel;

    @FXML
    private Label firstNameLabel;

    @FXML
    private Label maxSalaryLabel;

    @FXML
    private Label minSalaryLabel;

    @FXML
    private Label maxContractLabel;

    @FXML
    private Label minContractLabel;

    @FXML
    private Label allContractsLabel;

    @FXML
    private Label lastYearContractsLabel;

    @FXML
    private Label thisYearContractsLabel;

    @FXML
    private Label allOrdersLabel;

    @FXML
    private Label totalDistanceLabel;

    @FXML
    private Label thisYearOrdersLabel;

    @FXML
    private Label lastYearOrdersLabel;

    @FXML
    private Label longestDistanceLabel;

    @FXML
    private Label shortestDistanceLabel;

    @FXML
    private Label mostVisitedCityLabel;

    @FXML
    private Label mostDrivingVehicleLabel;

    @FXML
    private Label largestLoadLabel;

    @FXML
    private Label smallestLoadLabel;

    DatabaseToolkit dbToolkit = DatabaseToolkit.getInstance();
    ResultSet resultSet;

    final String EMPTY = "0.00";
    final int columnIndex = 1;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		userInfo(USER+MainController.getUserId());
		incinerationUpdate();
		printStatistic(maxSalaryLabel, MAX_SALARY+MainController.getUserId(), " zl");
		printStatistic(minSalaryLabel, MIN_SALARY+MainController.getUserId(), " zl");
		printStatistic(maxContractLabel, MAX_CONTRACT+MainController.getUserId(), " dni");
		printStatistic(minContractLabel, MIN_CONTRACT+MainController.getUserId(), " dni");
		printStatistic(allContractsLabel, ALL_CONTRACTS+MainController.getUserId(), "");
		printStatistic(thisYearContractsLabel, THIS_YEAR_CONTRACTS+MainController.getUserId(), "");
		printStatistic(lastYearContractsLabel, LAST_YEAR_CONTRACTS+MainController.getUserId(), "");
		printStatistic(allOrdersLabel, ALL_ORDERS+MainController.getUserId(), "");
		printStatistic(thisYearOrdersLabel, THIS_YEAR_ORDERS+MainController.getUserId(), "");
		printStatistic(lastYearOrdersLabel, LAST_YEAR_ORDERS+MainController.getUserId(), "");
		printStatistic(totalDistanceLabel, TOTAL_DISTANCE+MainController.getUserId(), " km");
		printStatistic(longestDistanceLabel, LONGEST_DISTANCE+MainController.getUserId(), " km");
		printStatistic(shortestDistanceLabel, SHORTEST_DISTANCE+MainController.getUserId(), " km");
		printStatistic(mostVisitedCityLabel, MOST_VISITED_CITY_FPART+MainController.getUserId()+MOST_VISITED_CITY_SPART, "");
		printStatistic(mostDrivingVehicleLabel, MOST_DRIVING_VEHICLE_FPART+MainController.getUserId()+MOST_DRIVING_VEHICLE_SPART, "");
		printStatistic(largestLoadLabel, LARGEST_LOAD+MainController.getUserId(), " kg");
		printStatistic(smallestLoadLabel, SMALLEST_LOAD+MainController.getUserId(), " kg");
	}

	private void incinerationUpdate() {
		resultSet = dbToolkit.executeQuery(EMPTY_INCINERATION+MainController.getUserId());
		TextInputDialog dialog;
		StringBuilder builder;
		int orderId;
		try {
			while(resultSet.next()) {
				String incineration = resultSet.getBigDecimal("incineration").toString();
				if(incineration.equals(EMPTY)) {
					dialog = new TextInputDialog(incineration);
					orderId = resultSet.getInt("order_id");
					builder = new StringBuilder();
					builder.append("od "+resultSet.getDate("start_date")+" do ");
					builder.append(resultSet.getDate("end_date"));
					builder.append(" z "+resultSet.getString("starting_point")+" do ");
					builder.append(resultSet.getString("destination"));
					builder.append(" samochodem "+resultSet.getString("brand")+" o nr ");
					builder.append(resultSet.getString("registration_number"));

					dbToolkit.executeUpdate("UPDATE transport.order SET incineration = "+noIncinerationDialog(dialog, builder.toString())+" WHERE order_id = "+orderId+" AND driver_driver_id = "+MainController.getUserId());
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		dbToolkit.disconnect();
	}

	private String noIncinerationDialog(TextInputDialog dialog, String text) {
		String info = "Masz trase "+text+"\nw której brak informacji o ilości zużytej benzyny.";
		dialog.setTitle("Brak infomacji");
		dialog.setHeaderText(info);
		dialog.setContentText("Podaj ilośc zużytej benzyny:");
		dialog.getDialogPane().lookupButton(ButtonType.CANCEL).setVisible(false);

		Optional<String> value = null;
		do {
		value = dialog.showAndWait();
		dialog.setHeaderText(info + "\n\nPodano nieprawidlowa wartosc. Sprobuj ponownie");
		} while(!Pattern.matches("[1-9]{1}[0-9]{0,}\\.{0,1}[0-9]{0,2}", value.get()));
		return value.get();
	}

	private void userInfo(String query) {
		resultSet = dbToolkit.executeQuery(query);
		try {
			while(resultSet.next()) {
				idLabel.setText(resultSet.getString("driver_id"));
				hireDateLabel.setText(resultSet.getDate("hire_date").toString());
				firstNameLabel.setText(resultSet.getString("first_name"));
				lastNameLabel.setText(resultSet.getString("last_name"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		dbToolkit.disconnect();
	}

	private void printStatistic(Label label, String query, String unit) {
		resultSet = dbToolkit.executeQuery(query);
		try {
			while(resultSet.next()) {
				label.setText(resultSet.getString(columnIndex)+unit);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		dbToolkit.disconnect();
	}
}
