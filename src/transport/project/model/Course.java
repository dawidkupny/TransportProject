package transport.project.model;

import java.math.BigDecimal;
import java.sql.Date;

public class Course {
	private Integer number;
	private BigDecimal distance;
	private String startingPoint;
	private String destination;
	private Date startDate;
	private Date endDate;
	private BigDecimal loadWeight;
	private BigDecimal cubature;
	private BigDecimal incineration;
    private String vehicleToString;
    private String driverToString;

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public BigDecimal getDistance() {
		return distance;
	}

	public void setDistance(BigDecimal distance) {
		this.distance = distance;
	}

	public String getStartingPoint() {
		return startingPoint;
	}

	public void setStartingPoint(String startingPoint) {
		this.startingPoint = startingPoint;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public BigDecimal getLoadWeight() {
		return loadWeight;
	}

	public void setLoadWeight(BigDecimal loadWeight) {
		this.loadWeight = loadWeight;
	}

	public BigDecimal getCubature() {
		return cubature;
	}

	public void setCubature(BigDecimal cubature) {
		this.cubature = cubature;
	}

	public BigDecimal getIncineration() {
		return incineration;
	}

	public void setIncineration(BigDecimal incineration) {
		this.incineration = incineration;
	}

    public String getVehicleToString() {
            return vehicleToString;
    }

    public void setVehicleToString(String vehicleToString) {
            this.vehicleToString = vehicleToString;
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

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Course(Integer number, BigDecimal distance, String startingPoint, String destination, Date startDate, Date endDate, BigDecimal loadWeight, BigDecimal cubature, BigDecimal incineration, String vehicleToString, String driverToString) {
        this.number = number;
        this.distance = distance;
        this.startingPoint = startingPoint;
        this.destination = destination;
        this.startDate = startDate;
        this.endDate = endDate;
        this.loadWeight = loadWeight;
        this.cubature = cubature;
        this.incineration = incineration;
        this.vehicleToString = vehicleToString;
        this.driverToString = driverToString;
    }
}