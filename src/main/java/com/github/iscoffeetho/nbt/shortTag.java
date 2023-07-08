package com.github.iscoffeetho.nbt;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class shortTag extends NBTTag {
	private int v;

	public shortTag() {
		super();
		this.v = 0;
	}
	public shortTag(int value) {
		super();
		this.set(value);
	}

	public int get() {
		return this.v;
	}

	public void set(int value) {
		this.v = value & 0xFFFF;
		if ((this.v & 0x8000) != 0)
			this.v -= 0x10000;
	}

	@Override
	public void writeType(OutputStream f) throws IOException {
		f.write(shortTag.type);
	}
	@Override
	public void writeValue(OutputStream f) throws IOException {
		f.write((this.v >> 8) & 0xFF);
		f.write(this.v & 0xFF);
	}

	@Override
	public void readValue(InputStream f) throws IOException {
		int value = f.read() << 8;
		value |= f.read();
		this.set(value);
	}

	@Override
	public void dump() {
		System.out.print(Integer.toString(this.v) + 's');
	}


	static public int type = 2;
}
