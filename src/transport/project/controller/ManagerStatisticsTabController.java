package transport.project.controller;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import transport.project.util.DatabaseToolkit;

public class ManagerStatisticsTabController implements Initializable{
	private static final String LONGEST_WORKING_DRIVER = "SELECT CONCAT(first_name, ' ', last_name, ' - '), SUM(DATEDIFF(expiration_date, start_date)) as days FROM transport.civil_contract JOIN transport.driver ON driver_driver_id = driver_id GROUP BY driver_driver_id ORDER BY days desc limit 1;";
	private static final String GREATEST_CONTRACTS_DRIVER = "SELECT CONCAT(first_name, ' ', last_name, ' - '), COUNT(driver_driver_id) FROM transport.civil_contract JOIN transport.driver ON driver_driver_id = driver_id GROUP BY driver_driver_id ORDER BY driver_driver_id ASC limit 1;";
	private static final String SMALLEST_CONTRACTS_DRIVER = "SELECT CONCAT(first_name, ' ', last_name, ' - '), COUNT(driver_driver_id) FROM transport.civil_contract JOIN transport.driver ON driver_driver_id = driver_id GROUP BY driver_driver_id ORDER BY driver_driver_id DESC limit 1;";
	private static final String GREATEST_ORDERS_DRIVER = "SELECT CONCAT(first_name, ' ', last_name, ' - '), COUNT(driver_driver_id) FROM transport.order JOIN transport.driver ON driver_driver_id = driver_id GROUP BY driver_driver_id ORDER BY driver_driver_id ASC limit 1;";
	private static final String SMALLEST_ORDERS_DRIVER = "SELECT CONCAT(first_name, ' ', last_name, ' - '), COUNT(driver_driver_id) FROM transport.order JOIN transport.driver ON driver_driver_id = driver_id GROUP BY driver_driver_id ORDER BY driver_driver_id DESC limit 1;";
	private static final String LONGEST_DISTANCE = "SELECT concat('Od ',starting_point, ' do ', destination, ' - '), distance FROM transport.order ORDER BY distance DESC  limit 1;";
	private static final String HIGHEST_SALARY = "SELECT CONCAT(first_name, ' ', last_name, ' - '), salary FROM transport.civil_contract JOIN transport.driver ON driver_driver_id = driver_id ORDER BY salary DESC limit 1;";
	private static final String LOWEST_SALARY = "SELECT CONCAT(first_name, ' ', last_name, ' - '), max(salary) FROM transport.civil_contract JOIN transport.driver ON driver_driver_id = driver_id group by driver_driver_id ORDER BY salary asc limit 1;";
	private static final String LARGEST_INCINERATION = "SELECT concat(brand, ' ', model, ' - '), incineration FROM transport.vehicle JOIN transport.order ON registration_id = vehicle_registration_id ORDER BY incineration desc limit 1;";
	private static final String LOWEST_INCINERATION = "SELECT concat(brand, ' ', model, ' - '), incineration FROM transport.vehicle JOIN transport.order ON registration_id = vehicle_registration_id ORDER BY incineration asc limit 1;";
	private static final String LARGEST_MILEAGE = "SELECT concat(brand, ' ', model, ' - '), sum(distance) FROM transport.vehicle JOIN transport.order ON registration_id = vehicle_registration_id group by registration_id ORDER BY distance desc limit 1;";
	private static final String LOWEST_MILEAGE = "SELECT concat(brand, ' ', model, ' - '), sum(distance) FROM transport.vehicle JOIN transport.order ON registration_id = vehicle_registration_id group by registration_id ORDER BY distance asc limit 1;";
	@FXML
    private Label lowestMileageVehicleLabel;

    @FXML
    private Label largestMileageVehicleLabel;

    @FXML
    private Label greatestOdersDriverLabel;

    @FXML
    private Label longestDistanceOrderLabel;

    @FXML
    private Label lowestIncinerationOrdersLabel;

    @FXML
    private Label smallestOrdersDriverLabel;

    @FXML
    private Label largestIncinerationOrdersLabel;

    @FXML
    private Label highestSalaryDriverLabel;

    @FXML
    private Label greatestContractsDriverLabel;

    @FXML
    private Label longestWorkingDriverLabel;

    @FXML
    private Label smallestContractsDriverLabel;

    @FXML
    private Label lowestSalaryDriverLabel;

    DatabaseToolkit dbToolkit = DatabaseToolkit.getInstance();
    ResultSet resultSet;
    final int firstColumn = 1;
    final int secondColumn = 2;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		printStatistic(longestWorkingDriverLabel, LONGEST_WORKING_DRIVER, " dni");
		printStatistic(greatestContractsDriverLabel, GREATEST_CONTRACTS_DRIVER, "");
		printStatistic(smallestContractsDriverLabel, SMALLEST_CONTRACTS_DRIVER, "");
		printStatistic(greatestOdersDriverLabel, GREATEST_ORDERS_DRIVER, "");
		printStatistic(smallestOrdersDriverLabel, SMALLEST_ORDERS_DRIVER, "");
		printStatistic(longestDistanceOrderLabel, LONGEST_DISTANCE, " km");
		printStatistic(highestSalaryDriverLabel, HIGHEST_SALARY, " zl");
		printStatistic(lowestSalaryDriverLabel, LOWEST_SALARY, " zl");
		printStatistic(largestIncinerationOrdersLabel, LARGEST_INCINERATION, " l");
		printStatistic(lowestIncinerationOrdersLabel, LOWEST_INCINERATION, " l");
		printStatistic(largestMileageVehicleLabel, LARGEST_MILEAGE, " km");
		printStatistic(lowestMileageVehicleLabel, LOWEST_MILEAGE, " km");
	}

	private void printStatistic(Label label, String query, String unit) {
		resultSet = dbToolkit.executeQuery(query);
		try {
			while(resultSet.next()) {
				label.setText(resultSet.getString(firstColumn)+resultSet.getString(secondColumn)+unit);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		dbToolkit.disconnect();
	}

}
