package android.geosvr.dtn.systemlib.util;

public class ByteHelper {
	/**
	 * Routine to convert int to byte array
	 * @param value
	 * @return
	 */
	public static final byte[] int_to_byte_array(int value) {
		return new byte[] { (byte) (value >>> 24), (byte) (value >>> 16),
				(byte) (value >>> 8), (byte) value };
	}
	
	/**
	 * Routine to convert byte array to int
	 * @param b
	 * @return
	 */
	public static final int byte_array_to_int(byte[] b) {
		return (b[0] << 24) + ((b[1] & 0xFF) << 16) + ((b[2] & 0xFF) << 8)
				+ (b[3] & 0xFF);
	}

	/**
	 * Routine to convert short to byte array
	 * @param value
	 * @return
	 */
	public static final byte[] short_to_byte_array(short value) {
		return new byte[] { (byte) (value >>> 8), (byte) value };
	}

	/**
	 * Routine to convert byte array to short
	 * @param b
	 * @return
	 */
	public static final short byte_array_to_short(byte[] b) {
		return (short) (
				           ( b[0] << 8) 
				           +
				            ((b[1] & 0xFF))
				            

		);
	}
}
