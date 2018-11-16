package exp.soundboard;

import exp.gui.SoundboardFrame;
import java.util.ArrayList;
import java.util.Iterator;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

public class GlobalKeyMacroListener implements NativeKeyListener {
	SoundboardFrame soundboardFrame;
	ArrayList<Integer> pressedKeys;

	public GlobalKeyMacroListener(SoundboardFrame frame) {
		this.soundboardFrame = frame;
		this.pressedKeys = new ArrayList<Integer>();
	}

	public void nativeKeyPressed(NativeKeyEvent e) {
		int pressed = e.getKeyCode();
		Utils.submitNativeKeyPressTime(NativeKeyEvent.getKeyText(pressed), e.getWhen());
		boolean alreadyPressed = false;
		for (Iterator localIterator = this.pressedKeys.iterator(); localIterator.hasNext();) {
			int i = ((Integer) localIterator.next()).intValue();
			if (pressed == i) {
				alreadyPressed = true;
				break;
			}
		}
		if (!alreadyPressed) {
			this.pressedKeys.add(Integer.valueOf(pressed));
		}
		if (pressed == Utils.stopKey) {
			Utils.stopAllClips();
		} else if (pressed == Utils.modspeedupKey) {
			Utils.incrementModSpeedUp();
		} else if (pressed == Utils.modspeeddownKey) {
			Utils.decrementModSpeedDown();
		} else if (pressed == Utils.getOverlapSwitchKey()) {
			boolean overlap = Utils.isOverlapSameClipWhilePlaying();
			Utils.setOverlapSameClipWhilePlaying(!overlap);
		}
		checkMacros();
	}

	public void nativeKeyReleased(NativeKeyEvent e) {
		int released = e.getKeyCode();
		for (int i = 0; i < this.pressedKeys.size(); i++) {
			if (released == ((Integer) this.pressedKeys.get(i)).intValue()) {
				this.pressedKeys.remove(i);
			}
		}
	}

	public void nativeKeyTyped(NativeKeyEvent arg0) {
	}

	public boolean isSpeedModKeyHeld() {
		for (Iterator localIterator = this.pressedKeys.iterator(); localIterator.hasNext();) {
			int key = ((Integer) localIterator.next()).intValue();
			if (key == Utils.slowKey) {
				return true;
			}
		}
		return false;
	}

	public ArrayList<Integer> getPressedNativeKeys() {
		ArrayList<Integer> array = new ArrayList();
		for (Integer i : this.pressedKeys) {
			array.add(new Integer(i.intValue()));
		}
		return array;
	}

	private void checkMacros() {
		boolean modspeed = false;
		if (isSpeedModKeyHeld()) {
			modspeed = true;
		}
		ArrayList<SoundboardEntry> potential = new ArrayList();
		for (SoundboardEntry entry : SoundboardFrame.soundboard.getSoundboardEntries()) {
			int[] actKeys = entry.getActivationKeys();
			if ((actKeys.length > 0) && (entry.matchesPressed(this.pressedKeys))) {
				potential.add(entry);
			}
		}

		if (potential.size() == 1) {
			((SoundboardEntry) potential.get(0)).play(this.soundboardFrame.audioManager, modspeed);
		} else {
			int highest = 0;
			ArrayList<SoundboardEntry> potentialCopy = new ArrayList<SoundboardEntry>(potential);
			for (SoundboardEntry p : potentialCopy) {
				int matches = p.matchesHowManyPressed(this.pressedKeys);
				if (matches > highest) {
					highest = matches;
				} else if (matches < highest) {
					potential.remove(p);
				}
			}
			potentialCopy = new ArrayList<SoundboardEntry>(potential);
			for (SoundboardEntry p : potentialCopy) {
				int matches = p.matchesHowManyPressed(this.pressedKeys);
				if (matches < highest) {
					potential.remove(p);
				}
			}

			for (SoundboardEntry p : potential) {
				p.play(this.soundboardFrame.audioManager, modspeed);
			}
		}
	}
}
