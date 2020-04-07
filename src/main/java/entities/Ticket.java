package entities;

import exception.InvalidTicketException;
import preparedstatement.crud.PreparedStatementQuery;
import preparedstatement.crud.PreparedStatementUpdate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Ticket {
  private int spotNumber;
  private String carNumber;
  private String carparkId;

  public Ticket(int spotNumber, String carNumber, String carparkId) {
    this.spotNumber = spotNumber;
    this.carNumber = carNumber;
    this.carparkId = carparkId;
  }

  public Ticket() {
  }

  public int getSpotNumber() {
    return spotNumber;
  }

  public String getCarparkId() {
    return carparkId;
  }

  public String getCarNumber() {
    return carNumber;
  }

  public static Ticket parseTicket(String ticketString) {
    List<String> ticketFieldList = Arrays.stream(ticketString.split(","))
      .collect(Collectors.toList());
    int ticketFieldSize = 3;
    if (ticketFieldList.size() != ticketFieldSize) {
      throw new InvalidTicketException("invalid input ticket");
    }
    return new Ticket(Integer.parseInt(ticketFieldList.get(1)), ticketFieldList.get(2), ticketFieldList.get(0));
  }

  public void deleteTicketFromDb() {
    String deleteSql = "DELETE FROM ticket WHERE car_number = ?";
    PreparedStatementUpdate.update(deleteSql, this.carNumber);
  }

  void storeTicketToDb() {
    String insertSql = "INSERT INTO ticket (spot_number, car_number, carpark_id) VALUES (?, ?, ?)";
    PreparedStatementUpdate.update(insertSql, this.spotNumber, this.carNumber, this.carparkId);
  }

  public Ticket findByCarNumber() {
    String sql = "SELECT carpark_id carparkId, spot_number spotNumber, car_number carNumber FROM ticket WHERE car_number = ?";
    return PreparedStatementQuery.queryInfo(Ticket.class, sql, this.carNumber);
  }

  @Override
  public String toString() {
    String message = "已将您的车牌号为" + this.carNumber
      + "的车辆停到" + this.carparkId + "停车场" + this.spotNumber + "号车位, 停车券为："
      + this.carparkId + "," + this.spotNumber + "," + this.carNumber + ", 请您妥善保存！";
    System.out.println(message);
    return this.carparkId + "," + this.spotNumber + "," + this.carNumber;
  }
}
