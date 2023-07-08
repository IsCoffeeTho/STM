package com.github.iscoffeetho.nbt;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class byteTag extends NBTTag {
	private int v;

	public byteTag() {
		super();
		this.v = 0;
	}

	public byteTag(int value) {
		super();
		this.set(value);
	}

	public int get() {
		return this.v;
	}

	public void set(int value) {
		
		this.v = value & 0xFF;
		if ((this.v & 0x80) != 0)
			this.v -= 0x100;
	}

	public void writeType(OutputStream f) throws IOException {
		f.write(byteTag.type);
	}

	public void writeValue(OutputStream f) throws IOException {
		f.write(this.v & 0xFF);
	}

	public void readValue(InputStream f) throws IOException {
		this.set(f.read());
	}

	@Override
	public void dump() {
		System.out.print(Integer.toString(this.v) + 'b');
	}

	static public int type = 1;
}
