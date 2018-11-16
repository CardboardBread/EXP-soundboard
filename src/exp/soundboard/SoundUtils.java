package exp.soundboard;

import java.nio.ByteBuffer;

public class SoundUtils {
	public static short[] byteToShortArray(byte[] byteArray) {
		short[] shortArray = new short[byteArray.length / 2];
		for (int i = 0; i < shortArray.length; i++) {
			int ub1 = byteArray[(i * 2 + 0)] & 0xFF;
			int ub2 = byteArray[(i * 2 + 1)] & 0xFF;
			shortArray[i] = ((short) ((ub2 << 8) + ub1));
		}
		return shortArray;
	}

	public static byte[] shortArrayToByteArray(short[] shortArray) {
		byte[] byteArray = new byte[shortArray.length * 2];
		ByteBuffer.wrap(byteArray).order(java.nio.ByteOrder.LITTLE_ENDIAN).asShortBuffer().put(shortArray);
		return byteArray;
	}
}
