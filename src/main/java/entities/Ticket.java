package entities;

import exception.InvalidTicketException;
import preparedstatement.crud.PreparedStatementQuery;
import preparedstatement.crud.PreparedStatementUpdate;

import java.util.List;

import static utils.StringListConverter.*;

public class Ticket {
  private String spotNumber;
  private String carNumber;
  private String carparkId;

  public Ticket(String spotNumber, String carNumber, String carparkId) {
    this.spotNumber = spotNumber;
    this.carNumber = carNumber;
    this.carparkId = carparkId;
  }

  public Ticket() {
  }

  public String getSpotNumber() {
    return spotNumber;
  }

  public String getCarparkId() {
    return carparkId;
  }

  public String getCarNumber() {
    return carNumber;
  }

  public static Ticket parseTicket(String ticketString) {
    List<String> ticketFieldList = StringToList(ticketString);
    int ticketFieldSize = 3;
    if (ticketFieldList.size() != ticketFieldSize) {
      throw new InvalidTicketException("invalid input ticket");
    }
    return new Ticket(ticketFieldList.get(1), ticketFieldList.get(2), ticketFieldList.get(0));
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
    return this.carparkId + "," + this.spotNumber + "," + this.carNumber;
  }
}
