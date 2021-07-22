package condor.phantom;

public class PhantomStatus {
  private static boolean enabled = false;

  public static void setEnabled(boolean status) {
    enabled = status;
  }

  public static void enable() {
    enabled = true;
  }

  public static void disable() {
    enabled = false;
  }

  public static boolean isEnabled() {
    return enabled;
  }
}
