package entities;

import preparedstatement.crud.PreparedStatementUpdate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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

  public void setSpotNumber(String spotNumber) {
    this.spotNumber = spotNumber;
  }

  public Ticket park(String carNumber, String spotNumber) {
    int minSpotNumber = this.findMinSpotNumber(spotNumber);
    String currentSpotNumber = this.removeMinSpotNumber(spotNumber, minSpotNumber);
    this.updateCarPark(currentSpotNumber);
    Ticket ticket = new Ticket(minSpotNumber, carNumber, this.id);
    ticket.storeTicketToDb();
    return ticket;
  }

  public Boolean isAvailable() {
    return this.spotNumber.length() > 0;
  }

  public void update() {
    String sql = "UPDATE carpark SET space = ? WHERE id = ?";
    PreparedStatementUpdate.update(sql, this.space, this.id);
  }

  private int findMinSpotNumber(String spotNumber) {
    List<Integer> spotNumberList = new ArrayList<>();
    if (spotNumber.length() == 1) {
      spotNumberList.add(Integer.parseInt(spotNumber));
    } else {
      spotNumberList = Arrays.stream(spotNumber.split(","))
                       .map(Integer::parseInt).collect(Collectors.toList());
    }
    return Collections.min(spotNumberList);
  }

  private String removeMinSpotNumber(String spotNumber, Integer minSpot) {
    List<Integer> spotNumberList = Arrays.stream(spotNumber.split(","))
                                  .map(Integer::parseInt).collect(Collectors.toList());
    spotNumberList.remove(minSpot);
    return spotNumberList.stream().map(Object::toString)
      .collect(Collectors.joining(","));
  }

  private String parseSpaceToSpotNumber() {
    List<Integer> spotNumberList = new ArrayList<>();
    for (int i = 1; i < space + 1; i++) {
      spotNumberList.add(i);
    }
    return spotNumberList.stream().map(Object::toString)
      .collect(Collectors.joining(","));
  }

  private void updateCarPark(String spotNumber) {
    String updateCarParkSql = "UPDATE CARPARK SET spotNumber = ? where id = ?";
    PreparedStatementUpdate.update(updateCarParkSql, spotNumber, this.id);
  }

}
