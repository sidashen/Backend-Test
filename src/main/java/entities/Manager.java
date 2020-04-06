package entities;

import exception.ParkingLotFullException;

import java.util.List;
import java.util.Optional;

public class Manager {
  private List<Carpark> carparkList;

  public Manager(List<Carpark> carparkList) {
    this.carparkList = carparkList;
  }

  public List<Carpark> getCarparkList() {
    return carparkList;
  }

  public void setCarparkList(List<Carpark> carparkList) {
    this.carparkList = carparkList;
  }

  public Ticket managePark(String carNumber) {
    Optional<Carpark> firstAvailableCarpark = carparkList.stream()
      .filter(Carpark::isAvailable).findFirst();
    if (firstAvailableCarpark.isPresent()) {
      firstAvailableCarpark.get().park(carNumber);
      return firstAvailableCarpark.get().getParkInfo(carNumber);
    }
    return null;
  }
}
