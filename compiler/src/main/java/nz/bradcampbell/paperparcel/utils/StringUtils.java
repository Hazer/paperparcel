package nz.bradcampbell.paperparcel.utils;

public class StringUtils {
  public static String uncapitalizeFirstCharacter(String s) {
    if (s == null || s.length() == 0) {
      return s;
    }
    return s.substring(0, 1).toLowerCase() + s.substring(1);
  }

  public static String capitalizeFirstCharacter(String s) {
    if (s == null || s.length() == 0) {
      return s;
    }
    return s.substring(0, 1).toUpperCase() + s.substring(1);
  }

  public static boolean startsWithVowel(String s) {
    return s != null && s.length() > 0 && "AEIOUaeiou".indexOf(s.charAt(0)) != -1;
  }
}
