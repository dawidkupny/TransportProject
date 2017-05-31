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
	private final static String EMPTY_INCINERATION = "SELECT o.incineration, o.start_date, o.end_date, o.starting_point, o.destination, "+
							"v.registration_number, v.brand FROM transport.order o JOIN transport.vehicle v WHERE incineration = 0 AND o.vehicle_registration_id = v.registration_id;";
    @FXML
    private Label lastNameLabel;

    @FXML
    private Label hireDateLabel;

    public Label getLastNameLabel() {
		return lastNameLabel;
	}

	public void setLastNameLabel(Label lastNameLabel) {
		this.lastNameLabel = lastNameLabel;
	}

	public Label getHireDateLabel() {
		return hireDateLabel;
	}

	public void setHireDateLabel(Label hireDateLabel) {
		this.hireDateLabel = hireDateLabel;
	}

	public Label getIdLabel() {
		return idLabel;
	}

	public void setIdLabel(Label idLabel) {
		this.idLabel = idLabel;
	}

	public Label getFirstNameLabel() {
		return firstNameLabel;
	}

	public void setFirstNameLabel(Label firstNameLabel) {
		this.firstNameLabel = firstNameLabel;
	}

	@FXML
    private Label idLabel;

    @FXML
    private Label firstNameLabel;

    DatabaseToolkit dbToolkit = DatabaseToolkit.getInstance();
    ResultSet resultSet;

    final String EMPTY = "0.00";

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		resultSet = dbToolkit.executeQuery(EMPTY_INCINERATION);

		incinerationUpdate(resultSet);

	}

	private void incinerationUpdate(ResultSet resultSet) {
		TextInputDialog dialog;
		StringBuilder builder;
		try {
			while(resultSet.next()) {
				String incineration = resultSet.getBigDecimal("incineration").toString();
				if(incineration.equals(EMPTY)) {
					dialog = new TextInputDialog(incineration);
					builder = new StringBuilder();
					builder.append("od "+resultSet.getDate("start_date")+" do ");
					builder.append(resultSet.getDate("end_date"));
					builder.append(" z "+resultSet.getString("starting_point")+" do ");
					builder.append(resultSet.getString("destination"));
					builder.append(" samochodem "+resultSet.getString("brand")+" o nr ");
					builder.append(resultSet.getString("registration_number"));

					System.out.println(noIncinerationDialog(dialog, builder.toString()));

					//executeUpdete UPDATE uzupelniony o spalanie do trasy ktorej dane sa pobrane
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
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
}
