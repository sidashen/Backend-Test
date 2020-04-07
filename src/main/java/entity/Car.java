package entity;

import preparedstatement.crud.PreparedStatementUpdate;

public class Car {
  private String carNumber;

  public Car(String carNumber) {
    this.carNumber = carNumber;
  }

  public Car() {
  }

  public String getCarNumber() {
    return carNumber;
  }

  public void storeCarToDb() {
    String insertSql = "INSERT INTO car (car_number) VALUES (?)";
    PreparedStatementUpdate.update(insertSql, this.carNumber);
  }
}
