package entity;

import exception.InvalidInitInfoException;
import exception.InvalidTicketException;
import exception.ParkingLotFullException;
import preparedstatement.crud.PreparedStatementUpdate;

import java.util.*;

public class Manager {
  private List<Carpark> carparkList;

  public Manager(List<Carpark> carparkList) {
    this.carparkList = carparkList;
  }

  public Manager() {
  }

  public List<Carpark> parseInitialInput(String initInfo) {
    String[] initialInfo = initInfo.split(",");
    for (String info : initialInfo) {
      String id = info.substring(0, 1);
      int space = Integer.parseInt(info.substring(2));
      checkInitInfo(id, space);
      Carpark carpark = new Carpark(id, space);
      this.carparkList.add(carpark);
    }
    this.carparkList.sort((Comparator<Carpark>) (o1, o2) -> o1.getId().compareTo(o2.getId()));
    return this.carparkList;
  }

  public static void checkInitInfo(String id, int space) {
    if ((id.equals("A") & space > 8) || (id.equals("B") & space > 10)) {
      throw new InvalidInitInfoException("停车场初始数据有误，请重新输入");
    }
  }

  public static void storeDatabase(List<Carpark> carparks) {
    String sql = "INSERT INTO carpark VALUES (?, ?, ?)";
    for (Carpark carpark : carparks) {
      PreparedStatementUpdate.update(sql, carpark.getId(), carpark.getSpace(), carpark.getSpotNumber());
    }
  }

  public Ticket managePark(String carNumber) {
    Optional<Carpark> firstAvailableCarpark = this.carparkList.stream()
      .filter(Carpark::isAvailable).findFirst();
    if (firstAvailableCarpark.isPresent()) {
      return firstAvailableCarpark.get().park(carNumber, firstAvailableCarpark.get().getSpotNumber());
    }
    throw new ParkingLotFullException("非常抱歉，由于车位已满，暂时无法为您停车！");
  }

  public static void manageFetch(String carParkId, String spotId, Ticket ticket) {
    checkTicket(ticket);
    updateCarPark(carParkId, spotId);
  }

  private static void updateCarPark(String carParkId, String spotId) {
    Carpark carpark = Carpark.findById(carParkId);
    carpark.updateSpotNumber(spotId);
    carpark.update();
  }

  private static void checkTicket(Ticket ticket) {
    if (Objects.isNull(ticket.findByCarNumber())) {
      throw new InvalidTicketException("很抱歉，无法通过您提供的停车券为您找到相应的车辆，请您再次核对停车券是否有效！");
    }
  }
}
