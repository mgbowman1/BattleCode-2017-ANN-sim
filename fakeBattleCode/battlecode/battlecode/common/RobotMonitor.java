package battlecode.common;

public class RobotMonitor {
	
	private static int bytecodesUsed;
	private static int maxBytecodes;
	
	public static void pause() {
		
	}
	
	public static int getBytecodesLeft() {
		return maxBytecodes - bytecodesUsed;
	}
	
	public static int getBytecodeNum() {
		return bytecodesUsed;
	}
	
	public static void setMaxBytecodes(int b) {
		maxBytecodes = b;
	}
	
	public static void setBytecodesUsed(int b) {
		bytecodesUsed = b;
	}
	
	public static void useBytecodes(int b) {
		bytecodesUsed += b;
	}
	
}
