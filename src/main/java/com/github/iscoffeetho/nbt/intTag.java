package com.github.iscoffeetho.nbt;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class intTag extends NBTTag {
	private int v;

	public intTag() {
		super();
		this.v = 0;
	}

	public intTag(int value) {
		super();
		this.set(value);
	}

	public int get() {
		return this.v;
	}

	public void set(int value) {
		this.v = value;
	}

	@Override
	public void writeType(OutputStream f) throws IOException {
		f.write(intTag.type);
	}

	@Override
	public void writeValue(OutputStream f) throws IOException {
		int value = 0;
		for (int i = 3; i >= 0; i--) {
			value = this.v >> (i*8);
			f.write(value);
		}
	}

	@Override
	public void readValue(InputStream f) throws IOException {
		int value = 0;
		for (int i = 0; i < 4; i++) {
			value <<= 8;
			value |= f.read();
		}
		this.set(value);
	}

	@Override
	public void dump() {
		System.out.print(Integer.toString(this.v));
	}

	static public int type = 3;
}
