package hotel.util;

public class Printer {
    private static final int WIDTH = 65;

    public static void header(String title) {
        System.out.println();
        line();
        int pad = (WIDTH - title.length() - 2) / 2;
        System.out.println(" ".repeat(pad) + "[ " + title + " ]");
        line();
    }

    public static void line() {
        System.out.println("─".repeat(WIDTH));
    }

    public static void success(String msg) { System.out.println("  ✔  " + msg); }
    public static void error(String msg)   { System.out.println("  ✘  " + msg); }
    public static void info(String msg)    { System.out.println("  ℹ  " + msg); }
}
