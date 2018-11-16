package exp.soundboard;

import java.awt.Toolkit;
import java.awt.event.KeyEvent;

public class KeyEventIntConverter {
	public static String getKeyEventText(int keyCode) {
		if ((keyCode >= 96) && (keyCode <= 105)) {
			String numpad = Toolkit.getProperty("AWT.numpad", "NumPad");
			char c = (char) (keyCode - 96 + 48);
			return numpad + " " + c;
		}
		return KeyEvent.getKeyText(keyCode);
	}
}
