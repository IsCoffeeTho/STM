package com.github.iscoffeetho.nbt;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class longTag extends NBTTag {
	private long v;

	public longTag() {
		super();
		this.v = 0;
	}

	public longTag(int value) {
		super();
		this.set(value);
	}

	public longTag(long value) {
		super();
		this.set(value);
	}

	public long get() {
		return this.v;
	}

	public void set(int value) {
		this.v = (long) value;
	}

	public void set(long value) {
		this.v = value;
	}

	@Override
	public void writeType(OutputStream f) throws IOException {
		f.write(longTag.type);
	}

	@Override
	public void writeValue(OutputStream f) throws IOException {
		long value = 0;
		for (int i = 7; i >= 0; i--) {
			value = this.v >> (i*8);
			f.write((int) value);
		}
	}

	@Override
	public void readValue(InputStream f) throws IOException {
		long value = 0l;
		for (int i = 0; i < 8; i++) {
			value <<= 8;
			value |= f.read();
		}
		this.set(value);
	}

	@Override
	public void dump() {
		System.out.print(Long.toString(this.v) + 'l');
	}

	static public int type = 4;
}
