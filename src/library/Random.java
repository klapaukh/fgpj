package library;

import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class Random {

	private java.util.Random rand;

	public Random() throws NoSuchAlgorithmException {
		this(ByteBuffer.wrap(
				SecureRandom.getInstance("SHA1PRNG").generateSeed(8)).getLong());
	}

	public Random(long seed) {
		rand = new java.util.Random(seed);
	}

	public long randNum() {
		return rand.nextLong();
	}

	public long max() {
		return Integer.MAX_VALUE;
	}

	public double randReal() {
		return rand.nextDouble();
	}

}
