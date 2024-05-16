public class Out {
	// Print a line
	public static void ln() {
		System.out.println();
	}
	// Print num line
	public static void ln(int num) {
		if (num != 0) {
			for (int i = 0; i < num; i++) {
				System.out.println();
			}
		}
	}
	// Print one line for each item of the array with the text at the index
	public static void ln(String[] text) {
		for (int i = 0; i < text.length; i++) {
			println(text[i]);
		}
	}
	
	// Print text to the screen
	public static void print(Object o) {
		System.out.print(o);
	}
	public static void println(Object o) {
		System.out.println(o);
	}
	public static void lnprint(Object o) {
		ln();
		print(o);
	}
	// For other formating cases
	public static void print(int lnsBefore, Object o, int lnsAfter) {
		ln(lnsBefore);
		print(o);
		ln(lnsAfter);
	}

	// Clears the screen
	public static void clear() {
		System.out.print("\033[H\033[2J");
		System.out.flush();
	}
	// Clears the screen and prints a line of text
	public static void clear(Object o) {
		clear();
		println(o);
	}
	// Clears the screen and prints an array of text to the screen
	public static void clear(String[] o) {
		clear();
		println(o);
	}

	// Printing with delay to look like typing
	// na means natural // meaning that an offset is applied to make typing look more natural
	public static void type(String text, int ms) {
		for (int i = 0; i < text.length(); i++) {
			print(text);
			utils.sleep(ms);
		}
	}
	public static void type(String text) {
		for (int i = 0; i < text.length(); i++) {
			print(text);
			utils.sleep(50);
		}
	}
	public static void natype(String text) {
		for (int i = 0; i < text.length(); i++) {
			int offset = (int)(Math.random() * 20 + 1);
			int invert = (int)(Math.random() * 2);
			if (invert != 0) {
				offset *= -1;
			}
			type(text,50 + offset);
		}
	}
	public static void typeln(String text, int ms) {
		for (int i = 0; i < text.length(); i++) {
			print(text);
			utils.sleep(ms);
		}
		ln();
	}
	public static void typeln(String text) {
		for (int i = 0; i < text.length(); i++) {
			print(text);
			utils.sleep(50);
		}
		ln();
	}
	public static void natypeln(String text) {
		for (int i = 0; i < text.length(); i++) {
			int offset = (int)(Math.random() * 20 + 1);
			int invert = (int)(Math.random() * 2);
			if (invert != 0) {
				offset *= -1;
			}
			type(text,50 + offset);
		}
		ln();
	}

	// print a string composed of object repeated several times
	public static void bar(int lnsBefore, int len, Object o, int lnsAfter) {
		ln(lnsBefore);
		for (int i = 0; i < len; i++) {
			System.out.print(o);
		}
		ln(lnsAfter);
	}
	public static void bar(int len) {
		bar(0,len,"*",0);
	}
	public static void bar(int len, Object o) {
		bar(0,len,o,0);
	}
	public static void barln(int len) {
		bar(0,len,"*",0);
		ln();
	}
	public static void barln(int len, Object o) {
		bar(0,len,o,0);
		ln();
	}

	// print a bar, text, then a bar
	public static void menu(int lnsBeforeTopBar, int topBarLen, Object topBarChar, int lnsAfterTopBar, String[] text, int charReturnsPerLine, int lnsBeforeBotttomBar, int bottomBarLen, Object bottomBarChar, int lnsAfterBottomBar) {
		int barLenMax = utils.lngstStrInArray(text);
		if (topBarLen == 0) {
			topBarLen = barLenMax;
		}
		if (bottomBarLen == 0) {
			bottomBarLen = barLenMax;
		}
		bar(lnsBeforeTopBar,topBarLen,topBarChar,lnsAfterTopBar);
		for (int i = 0; i < text.length; i++) {
			print(text[i]);
			ln(charReturnsPerLine);
		}
		bar(lnsBeforeBotttomBar,bottomBarLen,bottomBarChar,lnsAfterBottomBar);
	}
	public static void menu() {
		barln(10);
		ln();
		barln(10);
	}
	public static void menu(String text[]) {
		menu(0,0,"*",2,text,1,1,0,"*",1);
	}
}
class utils {
	public static int lngstStrInArray(String[] string) {
		int longest = string[0].length();
		for (int i = 0; i < string.length; i++) {
			if (string[i].length() > longest) {
				longest = string[i].length();
			}
		}
		return longest;
	}
	public static void sleep(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {}
	}
}
