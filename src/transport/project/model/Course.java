package transport.project.model;

import java.math.BigDecimal;

public class Course {
	private Integer number;
	private BigDecimal distance;
	private String startingPoint;
	private String destination;
	private BigDecimal loadWeight;
	private BigDecimal cubature;
	private String description;
	private BigDecimal incineration;
	private String brand;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getIncineration() {
		return incineration;
	}

	public void setIncineration(BigDecimal incineration) {
		this.incineration = incineration;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
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

    public Course(Integer number, BigDecimal distance, String startingPoint, String destination, BigDecimal loadWeight, BigDecimal cubature, String description, BigDecimal incineration, String brand, String vehicleToString, String driverToString) {
        this.number = number;
        this.distance = distance;
        this.startingPoint = startingPoint;
        this.destination = destination;
        this.loadWeight = loadWeight;
        this.cubature = cubature;
        this.description = description;
        this.incineration = incineration;
        this.brand = brand;
        this.vehicleToString = vehicleToString;
        this.driverToString = driverToString;
    }

        
        
	
}