package entities;

public class Ticket {
  private int spotId;
  private String carNumber;
  private String carparkId;

  public Ticket(int spotId, String carNumber, String carparkId) {
    this.spotId = spotId;
    this.carNumber = carNumber;
    this.carparkId = carparkId;
  }

  public int getSpotId() {
    return spotId;
  }

  public String getCarparkId() {
    return carparkId;
  }

  @Override
  public String toString() {
    String message = "已将您的车牌号为" + this.carNumber
      + "的车辆停到" + this.carparkId + "停车场" + this.spotId + "号车位, 停车券为："
      + this.carparkId + "," + this.spotId + "," + this.carNumber + ", 请您妥善保存！";
    System.out.println(message);
    return this.carparkId + "," + this.spotId + "," + this.carNumber;
  }
}
