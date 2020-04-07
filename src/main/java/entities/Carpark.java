package entities;

import preparedstatement.crud.PreparedStatementQuery;
import preparedstatement.crud.PreparedStatementUpdate;

import java.util.List;

public class Carpark {
  private String id;
  private int space;

  public Carpark(String id, int space) {
    this.id = id;
    this.space = space;
  }

  public Carpark() {
  }

  public String getId() {
    return id;
  }

  public int getSpace() {
    return space;
  }

  public Ticket park(String carNumber) {
    String sql = "";
    if (this.id.equals("B")) {
      sql = "INSERT INTO ticket_B (car_number, carpark_id) VALUES (?, ?)";
    } else {
      sql = "INSERT INTO ticket_A (car_number, carpark_id) VALUES (?, ?)";
    }
    PreparedStatementUpdate.update(sql, carNumber, this.id);
    return getParkTicket(carNumber);
  }

  public Ticket getParkTicket(String carNumber) {
    String sql = "";
    if (this.id.equals("B")) {
      sql = "SELECT carpark_id carparkId, spot_id spotId, car_number carNumber FROM ticket_B WHERE car_number = ?";
    } else {
      sql = "SELECT carpark_id carparkId, spot_id spotId, car_number carNumber FROM ticket_A WHERE car_number = ?";
    }
    List<Ticket> list = PreparedStatementQuery.queryInfoList(Ticket.class, sql, carNumber);
    int spotId = list.get(0).getSpotId();
    String carparkId = list.get(0).getCarparkId();
    Ticket ticket = new Ticket(spotId, carNumber, carparkId);
    this.space -= 1;
    update();
    return ticket;
  }

  public Boolean isAvailable() {
    return this.space > 0;
  }

  public void update() {
    String sql = "UPDATE carpark SET space = ? WHERE id = ?";
    PreparedStatementUpdate.update(sql, this.space, this.id);
  }

}
