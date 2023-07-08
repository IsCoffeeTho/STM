package com.github.iscoffeetho.nbt;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class doubleTag extends NBTTag {
	private double v;

	public doubleTag() {
		super();
		this.v = 0;
	}
	public doubleTag(double value) {
		super();
		this.set(value);
	}

	public double get() {
		return this.v;
	}

	public void set(double value) {
		this.v = value;
	}

	
	@Override
	public void writeType(OutputStream f) throws IOException {
		f.write(doubleTag.type);
	}
	@Override
	public void writeValue(OutputStream f) throws IOException {
		long bits = Double.doubleToLongBits(this.v);
		f.write((int) (bits >> 7*8) & 0xFF);
		f.write((int) (bits >> 6*8) & 0xFF);
		f.write((int) (bits >> 5*8) & 0xFF);
		f.write((int) (bits >> 4*8) & 0xFF);
		f.write((int) (bits >> 3*8) & 0xFF);
		f.write((int) (bits >> 2*8) & 0xFF);
		f.write((int) (bits >> 8) & 0xFF);
		f.write((int) bits & 0xFF);
	}

	@Override
	public void readValue(InputStream f) throws IOException {
		long bits = f.read() << 7*8;
		bits |= f.read() << 6*8;
		bits |= f.read() << 5*8;
		bits |= f.read() << 4*8;
		bits |= f.read() << 3*8;
		bits |= f.read() << 2*8;
		bits |= f.read() << 8;
		bits |= f.read();
		this.set(Double.longBitsToDouble(bits));
	}

	@Override
	public void dump() {
		System.out.print(String.format("%f", this.v));
	}


	static public int type = 6;
}
