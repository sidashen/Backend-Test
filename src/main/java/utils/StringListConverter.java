package utils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StringListConverter {
  public static String ListToString(List<String> list) {
    return list.stream().map(Object::toString)
      .collect(Collectors.joining(","));
  }

  public static List<String> StringToList(String string) {
    return Arrays.stream(string.split(",")).collect(Collectors.toList());
  }

}
