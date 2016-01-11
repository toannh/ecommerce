/**
 * Created by The eXo Platform SEA
 * Author : eXoPlatform
 * toannh@exoplatform.com
 * On 3/10/15
 * #comments here
 */
public class Test {
  public static void main(String[] args) {
    String text = "foobar";
    int rounds = 1000000;

    // Perform some warm-up rounds
    timeStringBuffer(text, rounds);
    timeStringBuilder(text, rounds);
    timeStringBuffer(text, rounds);
    timeStringBuilder(text, rounds);

    // And now the final results
    System.out.println("StringBuffer:  " + timeStringBuffer(text, rounds));
    System.out.println("StringBuilder: " + timeStringBuilder(text, rounds));
  }
    private static long timeStringBuffer(String text, int rounds) {
      long start = System.currentTimeMillis();
      StringBuffer sb = new StringBuffer(text.length() * rounds);
      for (int i = 0; i < rounds; i++) {
        sb.append(text);
      }
      sb.toString();
      return System.currentTimeMillis() - start;
    }

    private static long timeStringBuilder(String text, int rounds) {
      long start = System.currentTimeMillis();
      StringBuilder sb = new StringBuilder(text.length() * rounds);
      for (int i = 0; i < rounds; i++) {
        sb.append(text);}
      sb.toString();
      return System.currentTimeMillis() - start;
    }
}