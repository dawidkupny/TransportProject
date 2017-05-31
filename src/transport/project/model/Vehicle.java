package transport.project.model;

import java.math.BigDecimal;

public class Vehicle {

    private Integer number;
    private String  registrationNumber;
    private String vehicleAvailibilty;
    private String brand;
    private String model;
    private BigDecimal carryingCapacity;
    private BigDecimal cubature;

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getVehicleAvailibilty() {
        return vehicleAvailibilty;
    }

    public void setVehicleAvailibilty(String vehicleAvailibilty) {
        this.vehicleAvailibilty = vehicleAvailibilty;
    }

    

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }
    
    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public BigDecimal getCarryingCapacity() {
        return carryingCapacity;
    }

    public void setCarryingCapacity(BigDecimal carryingCapacity) {
        this.carryingCapacity = carryingCapacity;
    }

    public BigDecimal getCubature() {
        return cubature;
    }

    public void setCubature(BigDecimal cubature) {
        this.cubature = cubature;
    }

    public Vehicle(Integer number, String registrationNumber, String vehicleAvailibilty, String brand, String model, BigDecimal carryingCapacity, BigDecimal cubature) {
        this.number = number;
        this.registrationNumber = registrationNumber;
        this.vehicleAvailibilty = vehicleAvailibilty;
        this.brand = brand;
        this.model = model;
        this.carryingCapacity = carryingCapacity;
        this.cubature = cubature;
    }
    
    
    
}
