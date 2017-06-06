package transport.project.model;

import java.sql.Date;

public class Driver {
  private Integer number;
  private String firstName;
  private String lastName;
  private Date hireDate;

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getHireDate() {
        return hireDate;
    }

    public void setHireDate(Date hireDate) {
        this.hireDate = hireDate;
    }

    public Driver(Integer number, String firstName, String lastName, Date hireDate) {
        this.number = number;
        this.firstName = firstName;
        this.lastName = lastName;
        this.hireDate = hireDate;
    }

    @Override
    public String toString() {
        return this.firstName+" "+this.lastName;
    }
}
