package com.github.iscoffeetho.nbt;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class floatTag extends NBTTag {
	private float v;

	public floatTag() {
		super();
		this.v = 0;
	}
	public floatTag(float value) {
		super();
		this.set(value);
	}

	public float get() {
		return this.v;
	}

	public void set(float value) {
		this.v = value;
	}

	@Override
	public void writeType(OutputStream f) throws IOException {
		f.write(floatTag.type);
	}
	@Override
	public void writeValue(OutputStream f) throws IOException {
		int bits = Float.floatToIntBits(this.v);
		f.write((int) (bits >> 3*8) & 0xFF);
		f.write((int) (bits >> 2*8) & 0xFF);
		f.write((int) (bits >> 8) & 0xFF);
		f.write((int) bits & 0xFF);
	}

	@Override
	public void readValue(InputStream f) throws IOException {
		int bits = f.read() << 3*8;
		bits |= f.read() << 2*8;
		bits |= f.read() << 8;
		bits |= f.read();
		this.set(Float.intBitsToFloat(bits));
	}

	@Override
	public void dump() {
		System.out.print(String.format("%f", this.v) + 'f');
	}

	static public int type = 5;
}
