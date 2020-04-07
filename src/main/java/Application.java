import entities.Carpark;
import entities.Manager;
import entities.Ticket;
import preparedstatement.crud.PreparedStatementQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static entities.Manager.manageFetch;
import static entities.Manager.storeDatabase;

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
    List<Carpark> carparks = new ArrayList<>();
    Manager manager = new Manager(carparks);
    carparks = manager.parseInitialInput(initInfo);
    storeDatabase(carparks);
  }

  public static String park(String carNumber) {
    String sql = "SELECT * FROM carpark";
    List<Carpark> list = PreparedStatementQuery.queryInfoList(Carpark.class, sql);
    Manager manager = new Manager(list);
    Ticket ticket = manager.managePark(carNumber);
    return ticket.toString();
  }

  public static String fetch(String ticket) {
    Ticket currentTicket = Ticket.parseTicket(ticket);
    manageFetch(currentTicket.getCarparkId(), currentTicket.getSpotNumber());
    currentTicket.deleteTicketFromDb();
    return currentTicket.getCarNumber();
  }

}
