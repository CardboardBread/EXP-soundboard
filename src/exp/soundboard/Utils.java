package exp.soundboard;

import exp.gui.SettingsFrame;
import exp.gui.SoundboardFrame;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.prefs.Preferences;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioFormat.Encoding;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.DataLine.Info;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;

public class Utils {
	private static ThreadGroup clipPlayerThreadGroup = new ThreadGroup("Clip Player Group");
	private static final String prefsName = "Expenosa's Soundboard";
	public static final Preferences prefs = Preferences.userRoot().node("Expenosa's Soundboard");
	private static boolean PLAYALL = true;
	public static final int BUFFERSIZE = 2048;
	public static final float STANDARDSAMPLERATE = 44100.0F;
	private static float modifiedPlaybackSpeed;
	public static final float modifiedSpeedIncrements = 0.05F;
	public static final float modifiedSpeedMin = 0.1F;
	public static final float modifiedSpeedMax = 2.0F;
	public static final AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100.0F, 16, 2, 4,
			44100.0F, false);
	public static AudioFormat modifiedPlaybackFormat;
	public static int stopKey = 19;
	public static int slowKey = 35;
	public static int modspeedupKey = 39;
	public static int modspeeddownKey = 37;
	private static int overlapSwitchKey = 36;
	public static MicInjector micInjector = new MicInjector();
	private static Robot robot;
	public static boolean autoPTThold = true;
	private static ArrayList<Integer> pttkeys = new ArrayList<Integer>();
	private static int currentlyPlayingClipCount = 0;

	private static ConcurrentHashMap<String, Long> lastNativeKeyPressMap = new ConcurrentHashMap<String, Long>();
	private static ConcurrentHashMap<String, Long> lastRobotKeyPressMap = new ConcurrentHashMap<String, Long>();

	public static String fileEncoding = System.getProperty("file.encoding");

	public static boolean overlapSameClipWhilePlaying = true;

	public static void playNewSoundClipThreaded(File file, final SourceDataLine primarySpeaker,
			final SourceDataLine secondarySpeaker) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Utils.ClipPlayer clip = new Utils.ClipPlayer(file, primarySpeaker, secondarySpeaker);
				if (!Utils.overlapSameClipWhilePlaying) {
					Utils.stopFilePlaying(file);
				}
				clip.start();
			}
		});
	}

	public static String[] getMixerNames(DataLine.Info lineInfo) {
		ArrayList<String> mixerNames = new ArrayList<String>();
		Mixer.Info[] info = AudioSystem.getMixerInfo();
		Mixer.Info[] arrayOfInfo1;
		int j = (arrayOfInfo1 = info).length;
		for (int i = 0; i < j; i++) {
			Mixer.Info elem = arrayOfInfo1[i];
			Mixer mixer = AudioSystem.getMixer(elem);
			try {
				if (mixer.isLineSupported(lineInfo)) {
					mixerNames.add(elem.getName());
				}
			} catch (NullPointerException e) {
				System.err.println(e);
			}
		}
		String[] returnarray = new String[mixerNames.size()];
		return (String[]) mixerNames.toArray(returnarray);
	}

	public static void stopAllClips() {
		PLAYALL = false;
		zeroCurrentClipCount();
	}

	public static int getStopKey() {
		return stopKey;
	}

	public static void setStopKey(int stopKey) {
		Utils.stopKey = stopKey;
	}

	public static int getModifiedSpeedKey() {
		return slowKey;
	}

	public static void setModifiedSpeedKey(int slowKey) {
		Utils.slowKey = slowKey;
	}

	public static void startMicInjector(String inputMixerName, String outputMixerName) {
		boolean inputexists = false;
		boolean outputexists = false;
		if (isMicInjectorRunning())
			stopMicInjector();
		String[] arrayOfString;
		int j = (arrayOfString = MicInjector.getMixerNames(MicInjector.targetDataLineInfo)).length;
		for (int i = 0; i < j; i++) {
			String mixer = arrayOfString[i];
			if (mixer.equals(inputMixerName)) {
				inputexists = true;
			}
		}
		j = (arrayOfString = MicInjector.getMixerNames(MicInjector.sourceDataLineInfo)).length;
		for (int i = 0; i < j; i++) {
			String mixer = arrayOfString[i];
			if (mixer.equals(outputMixerName)) {
				outputexists = true;
			}
		}
		if ((inputexists) && (outputexists)) {
			micInjector.setInputMixer(inputMixerName);
			micInjector.setOutputMixer(outputMixerName);
			micInjector.start();
		}
	}

	public static void stopMicInjector() {
		micInjector.stopRunning();
	}

	public static void setMicInjectorGain(float level) {
		micInjector.setGain(level);
	}

	public static float getMicInjectorGain() {
		return MicInjector.getGain();
	}

	public static boolean isMicInjectorRunning() {
		return micInjector.isRunning();
	}

	public static void startMp3Decoder() {
		InputStream loaderfile = ClipPlayer.class.getResourceAsStream("loader.mp3");
		try {
			AudioSystem.getAudioFileFormat(loaderfile);
			AudioInputStream stream = AudioSystem.getAudioInputStream(loaderfile);
			stream.close();
		} catch (UnsupportedAudioFileException | IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean initGlobalKeyLibrary() {
		try {
			GlobalScreen.registerNativeHook();
		} catch (NativeHookException ex) {
			System.err.println("There was a problem registering the native hook.");
			System.err.println(ex.getMessage());
			JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(),
					"Error occured whilst initiating global hotkeys", 0);
		}
		return true;
	}

	public static boolean deregisterGlobalKeyLibrary() {
		if (GlobalScreen.isNativeHookRegistered()) {
			try {
				GlobalScreen.unregisterNativeHook();
			} catch (NativeHookException e) {
				e.printStackTrace();
			}
			return true;
		}
		return false;
	}

	public static boolean isFileSupported(File file) {
		try {
			AudioSystem.getAudioFileFormat(file);
			return true;
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static synchronized void setModifiedPlaybackSpeed(float speed) {
		modifiedPlaybackSpeed = speed;
		float newSampleRate = 44100.0F * speed;
		modifiedPlaybackFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, newSampleRate, 16, 2, 4,
				newSampleRate, false);
	}

	public static synchronized float getModifiedPlaybackSpeed() {
		return modifiedPlaybackSpeed;
	}

	public static int getModspeedupKey() {
		return modspeedupKey;
	}

	public static void setModspeedupKey(int modspeedupKey) {
		Utils.modspeedupKey = modspeedupKey;
	}

	public static int getModspeeddownKey() {
		return modspeeddownKey;
	}

	public static void setModspeeddownKey(int modspeeddownKey) {
		Utils.modspeeddownKey = modspeeddownKey;
	}

	public static int getOverlapSwitchKey() {
		return overlapSwitchKey;
	}

	public static void setOverlapSwitchKey(int overlapSwitchKey) {
		Utils.overlapSwitchKey = overlapSwitchKey;
	}

	public static void incrementModSpeedUp() {
		float speed = modifiedPlaybackSpeed + 0.05F;
		if (speed > 2.0F) {
			speed = 2.0F;
		}
		setModifiedPlaybackSpeed(speed);
		if (SettingsFrame.instance != null) {
			SettingsFrame.instance.updateDisplayedModSpeed();
		}
	}

	public static void decrementModSpeedDown() {
		float speed = modifiedPlaybackSpeed - 0.05F;
		if (speed < 0.1F) {
			speed = 0.1F;
		}
		setModifiedPlaybackSpeed(speed);
		if (SettingsFrame.instance != null) {
			SettingsFrame.instance.updateDisplayedModSpeed();
		}
	}

	public static JFileChooser getFileChooser() {
		return SoundboardFrame.filechooser;
	}

	public MicInjector getMicInjector() {
		return micInjector;
	}

	public static Robot getRobotInstance() {
		if (robot != null) {
			return robot;
		}
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
		if (robot != null) {
			return robot;
		}

		return null;
	}

	public static boolean checkAndUseAutoPPThold() {
		if ((!autoPTThold) || (pttkeys.size() == 0)) {
			return false;
		}
		if (SoundboardFrame.soundboard.entriesContainPTTKeys(pttkeys)) {
			return false;
		}

		ArrayList<Integer> pressed = SoundboardFrame.macroListener.getPressedNativeKeys();
		Robot robot = getRobotInstance();
		int noofkeys = pttkeys.size();
		for (int i = 0; i < noofkeys; i++) {
			int key = ((Integer) pttkeys.get(i)).intValue();
			boolean pressedAlready = false;
			for (Integer nativekey : pressed) {
				if (KeyEventIntConverter.getKeyEventText(key).toLowerCase()
						.equals(NativeKeyEvent.getKeyText(nativekey.intValue()).toLowerCase())) {
					pressedAlready = true;
					break;
				}
			}
			if (!pressedAlready) {
				robot.keyPress(key);
				submitRobotKeyPressTime(KeyEventIntConverter.getKeyEventText(key));
				System.out.println("Robot pressed: " + KeyEvent.getKeyText(key));
			}
		}
		return true;
	}

	public static boolean checkAndReleaseHeldPPTKeys() {
		if (!autoPTThold) {
			return false;
		}
		if (SoundboardFrame.soundboard.entriesContainPTTKeys(pttkeys)) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					JOptionPane.showMessageDialog(null,
							"A soundboard entry is using a key that conflicts with a 'Push to Talk' key. \n Disable 'Auto-hold PTT keys', or edit the entry or PTT keys.",
							"Alert!", 0);
				}
			});
			return false;
		}

		if (currentlyPlayingClipCount == 0) {
			Robot robot = getRobotInstance();
			for (Integer i : pttkeys) {
				if (wasKeyLastPressedByRobot(KeyEventIntConverter.getKeyEventText(i.intValue()))) {
					robot.keyRelease(i.intValue());
					System.out.println("Robot released: " + KeyEvent.getKeyText(i.intValue()));
				}
			}
		}
		return true;
	}

	public static ArrayList<Integer> getPTTkeys() {
		return pttkeys;
	}

	public static void setPTTkeys(Collection<Integer> pTTkeys) {
		pttkeys = new ArrayList<Integer>(pTTkeys);
	}

	public static boolean isAutoPTThold() {
		return autoPTThold;
	}

	public static void setAutoPTThold(boolean autoPTThold) {
		Utils.autoPTThold = autoPTThold;
	}

	public static synchronized void incrementCurrentClipCount() {
		currentlyPlayingClipCount += 1;
	}

	public static synchronized void decrementCurrentClipCount() {
		if (currentlyPlayingClipCount >= 1) {
			currentlyPlayingClipCount -= 1;
		}
	}

	public static synchronized void zeroCurrentClipCount() {
		currentlyPlayingClipCount = 0;
	}

	public static ArrayList<Integer> stringToIntArrayList(String string) {
		String arrayString = string.replace('[', ' ').replace(']', ' ').trim();
		ArrayList<Integer> array = new ArrayList<Integer>();
		String[] numberstring = arrayString.split(",");
		String[] arrayOfString1;
		int j = (arrayOfString1 = numberstring).length;
		for (int i = 0; i < j; i++) {
			String s = arrayOfString1[i];
			if (!s.equals("")) {
				int i2 = Integer.parseInt(s.trim());
				array.add(Integer.valueOf(i2));
			}
		}
		return array;
	}

	public static void submitNativeKeyPressTime(String key, long time) {
		lastNativeKeyPressMap.put(key.toLowerCase(), Long.valueOf(time));
	}

	public static void submitRobotKeyPressTime(String key) {
		long time = System.currentTimeMillis();
		lastNativeKeyPressMap.put(key.toLowerCase(), Long.valueOf(time));
		lastRobotKeyPressMap.put(key.toLowerCase(), Long.valueOf(time));
	}

	public static long getLastNativeKeyPressTimeForKey(String keyname) {
		Long time = (Long) lastNativeKeyPressMap.get(keyname.toLowerCase());
		if (time == null) {
			return 0L;
		}
		return time.longValue();
	}

	public static long getLastRobotKeyPressTimeForKey(String keyname) {
		Long time = (Long) lastRobotKeyPressMap.get(keyname.toLowerCase());
		if (time == null) {
			return 0L;
		}
		return time.longValue();
	}

	public static boolean wasKeyLastPressedByRobot(String keyname) {
		long human = getLastNativeKeyPressTimeForKey(keyname);
		long robot = getLastRobotKeyPressTimeForKey(keyname);
		if (robot == human) {
			return true;
		}
		return false;
	}

	public static boolean isOverlapSameClipWhilePlaying() {
		return overlapSameClipWhilePlaying;
	}

	public static void setOverlapSameClipWhilePlaying(boolean overlap) {
		overlapSameClipWhilePlaying = overlap;
		if (SettingsFrame.instance != null) {
			SettingsFrame.instance.updateOverlapSwitchCheckBox();
		}
	}

	public static boolean stopFilePlaying(File file) {
		boolean stopped = false;
		String filepath = file.toString();
		Thread[] threads = new Thread[clipPlayerThreadGroup.activeCount()];
		clipPlayerThreadGroup.enumerate(threads);

		System.out.println("Thread count: " + threads.length);
		System.out.println("Thread groups: " + clipPlayerThreadGroup.activeGroupCount());
		System.out.println("Requesting: " + filepath + " to stop");
		Thread[] arrayOfThread1;
		int j = (arrayOfThread1 = threads).length;
		for (int i = 0; i < j; i++) {
			Thread thread = arrayOfThread1[i];
			System.out.println("thread name: " + thread.getName());
			if (filepath.equals(thread.getName())) {
				ClipPlayer cp = (ClipPlayer) thread;
				cp.stopPlaying();
				stopped = true;
			}
		}
		return stopped;
	}

	private static class ClipPlayer extends Thread {
		File file;

		SourceDataLine primarySpeaker = null;
		SourceDataLine secondarySpeaker = null;
		boolean playing = true;

		public ClipPlayer(File file, SourceDataLine primarySpeaker, SourceDataLine secondarySpeaker) {
			super(file.toString());
			this.file = file;
			this.primarySpeaker = primarySpeaker;
			this.secondarySpeaker = secondarySpeaker;
		}

		public void run() {
			playSoundClip(this.file, this.primarySpeaker, this.secondarySpeaker);
		}

		public void stopPlaying() {
			System.out.println("Stopping clip: " + this.file.getName());
			this.playing = false;
		}

		private void playSoundClip(File file, SourceDataLine primarySpeaker, SourceDataLine secondarySpeaker)
     {
       Utils.PLAYALL = true;
       AudioInputStream clip = null;
       AudioFormat clipformat = null;
       try {
         clip = AudioSystem.getAudioInputStream(file);
         clipformat = clip.getFormat();
         if (!clipformat.equals(Utils.format)) {
           clip = AudioSystem.getAudioInputStream(Utils.format, clip);
         }
       } catch (UnsupportedAudioFileException e) {
         e.printStackTrace();
         JOptionPane.showMessageDialog(null, file.getName() + " uses an unsupported format.", "Unsupported Format", 0);
       } catch (IOException e) {
         e.printStackTrace();
       }
       if (clip != null) {
         Utils.incrementCurrentClipCount();
         byte[] buffer = new byte['ࠀ']; // TODO: fix character
         int bytesRead = 0;
         while ((this.playing) && (Utils.PLAYALL)) {
           try {
             bytesRead = clip.read(buffer, 0, 2048);
           } catch (IOException e) {
             e.printStackTrace();
           }
           Utils.checkAndUseAutoPPThold();
           if (bytesRead > 0) {
             primarySpeaker.write(buffer, 0, bytesRead);
             if (secondarySpeaker != null) {
               secondarySpeaker.write(buffer, 0, bytesRead);
             }
           }
           if (bytesRead < 2048) {
             this.playing = false;
           }
         }
         Utils.decrementCurrentClipCount();
         Utils.checkAndReleaseHeldPPTKeys();
       }
       if (clip != null) {
         try {
           clip.close();
         } catch (IOException e) {
           e.printStackTrace();
         }
       }
       primarySpeaker.close();
       if (secondarySpeaker != null) {
         secondarySpeaker.close();
       }
     }
	}
}
