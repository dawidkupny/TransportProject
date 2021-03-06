package transport.project.model;

import java.math.BigDecimal;
import java.sql.Date;

public class Contract {
	private Integer number;
	private String contractNumber;
    private String driverToString;
	private Date startDate;
	private Date expirationDate;
	private BigDecimal salary;

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public String getContractNumber() {
		return contractNumber;
	}

	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
	}

    public String getDriverToString() {
            return driverToString;
    }

    public void setDriverToString(String driverToString) {
            this.driverToString = driverToString;
    }


	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public BigDecimal getSalary() {
		return salary;
	}

	public void setSalary(BigDecimal salary) {
		this.salary = salary;
	}

	public Contract(Integer number, String contractNumber, Date startDate, Date expirationDate, BigDecimal salary, String driverToString) {
		this.number = number;
		this.contractNumber = contractNumber;
        this.driverToString = driverToString;
		this.startDate = startDate;
		this.expirationDate = expirationDate;
		this.salary = salary;
	}
}
