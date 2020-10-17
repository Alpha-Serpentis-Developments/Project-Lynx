package lynx.misc;

public class Truncate {
	public static String truncate(String string, int length) {
		if(string.length() > length)
			string = string.substring(0, length- 4).trim() + "...";
		return string;
	}
}
