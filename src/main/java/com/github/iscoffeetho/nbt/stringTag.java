package com.github.iscoffeetho.nbt;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class stringTag extends NBTTag {
	private String v;

	public stringTag() {
		super();
		this.v = "";
	}
	public stringTag(String value) {
		super();
		this.set(value);
	}

	public String get() {
		return this.v;
	}

	public void set(String value) {
		this.v = value;
	}

	@Override
	public void writeType(OutputStream f) throws IOException {
		f.write(stringTag.type);
	}
	@Override
	public void writeValue(OutputStream f) throws IOException {
		int len = this.v.length();
		f.write((len >> 8) & 0xFF);
		f.write(len & 0xFF);
		f.write(this.v.getBytes());
	}

	@Override
	public void readValue(InputStream f) throws IOException {
		int length = f.read() << 8;
		length |= f.read();
		String value = "";
		for (int i = 0; i < length; i++)
			value += (char) f.read();
		this.set(value);
	}

	@Override
	public void dump() {
		System.out.print('"' + this.v + '"');
	}

	static public int type = 8;
}
