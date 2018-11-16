package exp.soundboard;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import org.jnativehook.keyboard.NativeKeyEvent;

public class SoundboardEntry {
	private String file;
	public int[] activationKeysNumbers;

	public SoundboardEntry(File file, int[] keys) {
		Path p = Paths.get(new String(file.getAbsolutePath()), new String[0]);
		this.file = p.toAbsolutePath().toString();
		this.activationKeysNumbers = keys;
		if (this.activationKeysNumbers == null) {
			this.activationKeysNumbers = new int[0];
		}
	}

	public boolean matchesPressed(ArrayList<Integer> pressedKeys) {
		int keysRemaining = this.activationKeysNumbers.length;
		if (keysRemaining == 0)
			return false;
		int[] arrayOfInt;
		int j = (arrayOfInt = this.activationKeysNumbers).length;
		for (int i = 0; i < j; i++) {
			int actkey = arrayOfInt[i];
			for (Iterator localIterator = pressedKeys.iterator(); localIterator.hasNext();) {
				int presskey = ((Integer) localIterator.next()).intValue();
				if (actkey == presskey) {
					keysRemaining--;
				}
			}
		}

		if (keysRemaining <= 0) {
			return true;
		}
		return false;
	}

	public int matchesHowManyPressed(ArrayList<Integer> pressedKeys)
   {
     int matches = 0;
     int j; int i; for (Iterator localIterator = pressedKeys.iterator(); localIterator.hasNext(); 
         i < j)
     {
       int key = ((Integer)localIterator.next()).intValue();
       int[] arrayOfInt; j = (arrayOfInt = this.activationKeysNumbers).length;i = 0; continue;int hotkey = arrayOfInt[i];
       if (key == hotkey) {
         matches++;
       }
       i++;
     }
     
 
 
 
     return matches;
   }

	public void play(AudioManager audio, boolean moddedspeed) {
		File file = toFile();
		audio.playSoundClip(file, moddedspeed);
	}

	public File toFile() {
		File f = new File(this.file);
		if (!f.exists()) {
			Path p = Paths.get(this.file, new String[0]);
			return p.toFile();
		}
		return f;
	}

	public void setFile(File file) {
		try {
			this.file = new String(file.getAbsolutePath().getBytes(Utils.fileEncoding));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public String getFileString() {
		return this.file;
	}

	public String getFileName() {
		char seperator = File.separatorChar;
		return this.file.substring(this.file.lastIndexOf(seperator) + 1);
	}

	public int[] getActivationKeys() {
		return this.activationKeysNumbers;
	}

	public String getActivationKeysAsReadableString() {
		String s = "";
		if (this.activationKeysNumbers.length == 0)
			return s;
		int[] arrayOfInt;
		int j = (arrayOfInt = getActivationKeys()).length;
		for (int i = 0; i < j; i++) {
			int i2 = arrayOfInt[i];
			s = s.concat(NativeKeyEvent.getKeyText(i2) + "+");
		}
		s = s.substring(0, s.length() - 1);
		return s;
	}

	public void setActivationKeys(int[] activationKeys) {
		this.activationKeysNumbers = activationKeys;
	}
}
