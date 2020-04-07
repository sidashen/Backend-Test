package entities;

import exception.InvalidInitInfoException;
import exception.InvalidTicketException;
import exception.ParkingLotFullException;
import preparedstatement.crud.PreparedStatementQuery;
import preparedstatement.crud.PreparedStatementUpdate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Manager {
  private List<Carpark> carparkList;

  public Manager(List<Carpark> carparkList) {
    this.carparkList = carparkList;
  }

  public Manager() {
  }

  public List<Carpark> parseInitialInput(String initInfo) {
    List<String> initialInfo = Arrays.asList(initInfo.split(","));
    for (String info : initialInfo) {
      String id = info.substring(0, 1);
      int space = Integer.parseInt(info.substring(2));
      checkInitInfo(id, space);
      Carpark carpark = new Carpark(id, space);
      this.carparkList.add(carpark);
    }
    return this.carparkList;
  }

  public static void checkInitInfo(String id, int space) {
    if (id.equals("A") & space > 8) {
      throw new InvalidInitInfoException("停车场初始数据有误，请重新输入");
    } else if (id.equals("B") & space > 10) {
      throw new InvalidInitInfoException("停车场初始数据有误，请重新输入");
    }
  }

  public static void storeDatabase(List<Carpark> carparks) {
    String sql = "INSERT INTO carpark VALUES (?, ?)";
    for (Carpark carpark : carparks) {
      PreparedStatementUpdate.update(sql, carpark.getId(), carpark.getSpace());
    }
  }

  public Ticket managePark(String carNumber) {
    Optional<Carpark> firstAvailableCarpark = carparkList.stream()
      .filter(Carpark::isAvailable).findFirst();
    if (firstAvailableCarpark.isPresent()) {
      return firstAvailableCarpark.get().park(carNumber);
    }
    throw new ParkingLotFullException("非常抱歉，由于车位已满，暂时无法为您停车！");
  }

  public String manageFetch(String ticket) {
    String sql = "SELECT car_number carNumber FROM ticket_A WHERE spot_id = ?";
    List<String> ticketInfo = Arrays.asList(ticket.split(","));
    int spotId = Integer.parseInt(ticketInfo.get(1));
    List<Car> list = PreparedStatementQuery.queryInfoList(Car.class, sql, spotId);
    assert list != null;
    if (list.size() > 0) {
      sql = "DELETE FROM ticket_A WHERE spot_id = ?";
      PreparedStatementUpdate.update(sql, spotId);
      System.out.println("已为您取到车牌号为" + list.get(0).getCarNumber() + "的车辆，很高兴为您服务，祝您生活愉快!");
      return list.get(0).getCarNumber();
    } else {
      sql = "SELECT car_number carNumber FROM ticket_B WHERE spot_id = ?";
      list = PreparedStatementQuery.queryInfoList(Car.class, sql, spotId);
    }
    assert list != null;
    if (list.size() > 0) {
      sql = "DELETE FROM ticket_B WHERE spot_id = ?";
      PreparedStatementUpdate.update(sql, spotId);
      System.out.println("已为您取到车牌号为" + list.get(0).getCarNumber() + "的车辆，很高兴为您服务，祝您生活愉快!");
      return list.get(0).getCarNumber();
    }
    throw new InvalidTicketException("很抱歉，无法通过您提供的停车券为您找到相应的车辆，请您再次核对停车券是否有效！");
  }

}
