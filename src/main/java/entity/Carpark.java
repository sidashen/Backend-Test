package entity;

import preparedstatement.crud.PreparedStatementQuery;
import preparedstatement.crud.PreparedStatementUpdate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static util.StringListConverter.*;

public class Carpark {
  private String id;
  private int space;
  private String spotNumber;

  public Carpark(String id, int space) {
    this.id = id;
    this.space = space;
    this.spotNumber = parseSpaceToSpotNumber();
  }

  public Carpark() {
  }

  public String getId() {
    return id;
  }

  public int getSpace() {
    return space;
  }

  public String getSpotNumber() {
    return spotNumber;
  }

  public void update() {
    String sql = "UPDATE carpark SET space = ? WHERE id = ?";
    PreparedStatementUpdate.update(sql, this.space, this.id);
  }

  public static Carpark findById(String id) {
    String querySql = "SELECT space,  spot_Number spotNumber FROM carpark WHERE id = ?";
    return PreparedStatementQuery.queryInfo(Carpark.class, querySql, id);
  }

  public static List<Carpark> findAll() {
    String sql = "SELECT id, space, spot_number spotNumber FROM carpark";
    return PreparedStatementQuery.queryInfoList(Carpark.class, sql);
  }

  public void updateSpotNumber(String spotNumber) {
    List<String> spotNumberList = StringToList(spotNumber);
    spotNumberList.add(spotNumber);
    this.spotNumber = ListToString(spotNumberList);
  }

  Ticket park(String carNumber, String spotNumber) {
    String minSpotNumber = this.findMinSpotNumber(spotNumber);
    String currentSpotNumber = this.removeMinSpotNumber(spotNumber, minSpotNumber);
    this.updateCarPark(currentSpotNumber);
    Ticket ticket = new Ticket(minSpotNumber, carNumber, this.id);
    ticket.storeTicketToDb();
    Car car = new Car(carNumber);
    car.storeCarToDb();
    return ticket;
  }

  Boolean isAvailable() {
    return this.spotNumber.length() > 0;
  }

  private String findMinSpotNumber(String spotNumber) {
    List<String> spotNumberList = new ArrayList<>();
    if (spotNumber.length() == 1) {
      spotNumberList.add(spotNumber);
    } else {
      spotNumberList = StringToList(spotNumber);
    }
    return Collections.min(spotNumberList);
  }

  private String removeMinSpotNumber(String spotNumber, String minSpot) {
    List<String> spotNumberList = StringToList(spotNumber);
    spotNumberList.remove(minSpot);
    return ListToString(spotNumberList);
  }

  private String parseSpaceToSpotNumber() {
    List<String> spotNumberList = new ArrayList<>();
    for (int i = 1; i < this.space + 1; i++) {
      spotNumberList.add(String.valueOf(i));
    }
    return ListToString(spotNumberList);
  }

  private void updateCarPark(String spotNumber) {
    String updateCarParkSql = "UPDATE carpark SET spot_number = ? where id = ?";
    PreparedStatementUpdate.update(updateCarParkSql, spotNumber, this.id);
  }

}
