import entities.Car;
import entities.Carpark;
import entities.Manager;
import entities.Ticket;
import exception.InvalidTicketException;
import preparedstatement.crud.PreparedStatementQuery;
import preparedstatement.crud.PreparedStatementUpdate;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Application {

  public static void main(String[] args) {
    operateParking();
  }

  public static void operateParking() {
    while (true) {
      System.out.println("1. 初始化停车场数据\n2. 停车\n3. 取车\n4. 退出\n请输入你的选择(1~4)：");
      Scanner printItem = new Scanner(System.in);
      String choice = printItem.next();
      if (choice.equals("4")) {
        System.out.println("系统已退出");
        break;
      }
      handle(choice);
    }
  }

  private static void handle(String choice) {
    Scanner scanner = new Scanner(System.in);
    if (choice.equals("1")) {
      System.out.println("请输入初始化数据\n格式为\"停车场编号1：车位数,停车场编号2：车位数\" 如 \"A:8,B:9\"：");
      String initInfo = scanner.next();
      init(initInfo);
    } else if (choice.equals("2")) {
      System.out.println("请输入车牌号\n格式为\"车牌号\" 如: \"A12098\"：");
      String carInfo = scanner.next();
      String ticket = park(carInfo);
      String[] ticketDetails = ticket.split(",");
      System.out.format("已将您的车牌号为%s的车辆停到%s停车场%s号车位，停车券为：%s，请您妥善保存。\n", ticketDetails[2], ticketDetails[0], ticketDetails[1], ticket);
    } else if (choice.equals("3")) {
      System.out.println("请输入停车券信息\n格式为\"停车场编号1,车位编号,车牌号\" 如 \"A,1,8\"：");
      String ticket = scanner.next();
      String car = fetch(ticket);
      System.out.format("已为您取到车牌号为%s的车辆，很高兴为您服务，祝您生活愉快!\n", car);
    }
  }

  public static void init(String initInfo) {
    List<String> initialInfo = Arrays.asList(initInfo.split(","));
    String firstCarpark = initialInfo.get(0).substring(0, 1);
    String firstCarparkSpace = initialInfo.get(0).substring(2);
    String secondCarpark = initialInfo.get(1).substring(0, 1);
    String secondCarparkSpace = initialInfo.get(1).substring(2);
    String sql = "INSERT INTO carpark VALUES (?, ?)";
    PreparedStatementUpdate.update(sql, firstCarpark, firstCarparkSpace);
    PreparedStatementUpdate.update(sql, secondCarpark, secondCarparkSpace);
  }

  public static String park(String carNumber) {
    String sql = "SELECT * FROM carpark";
    List<Carpark> list = PreparedStatementQuery.queryInfoList(Carpark.class, sql);
    Manager manager = new Manager(list);
    Ticket ticket = manager.managePark(carNumber);
    return ticket.toString();
  }

  public static String fetch(String ticket) {
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
