package it.polimi.ingsw.Client.CLI;

public class StringUtils {

    public static String center(String s, int size) {
        return center(s, size, ' ');
    }

    /**
     * Centers the string received
     * @param s string received
     * @param size size of the whole string to size
     * @param pad character used to center
     * @return String centered
     */
    public static String center(String s, int size, char pad) {
        if (s == null || size <= s.length()) return s;
        StringBuilder sb = new StringBuilder(size);
        sb.append(String.valueOf(pad).repeat((size - s.length()) / 2));
        sb.append(s);
        while (sb.length() < size) { sb.append(pad); }
        return sb.toString();
    }
}
