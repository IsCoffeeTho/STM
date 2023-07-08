package com.github.iscoffeetho.nbt;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class NBTTag {
	public String name = null;

	public NBTTag() {
		this.name = null;
	}

	public NBTTag(String name) {
		this.name = name;
	}

	public void writeTo(OutputStream f) throws IOException {
		this.writeType(f);
		this.writeName(f);
		this.writeValue(f);
	}

	public void writeType(OutputStream f) throws IOException {
		f.write(0);
	}

	public void writeName(OutputStream f) throws IOException {
		if (this.name != null) {
			int len = this.name.length();
			f.write((len >> 8) & 0xFF);
			f.write((len) & 0xFF);
			f.write(this.name.getBytes());
		}
	}

	public void writeValue(OutputStream f) throws IOException {
		// f.write(0);
	}

	public void readFrom(InputStream f) throws IOException {
		this.readName(f);
		this.readValue(f);
	}

	public void readName(InputStream f) throws IOException {
		this.name = "";
		int len = f.read() << 8;
		len |= f.read();
		for (int i = 0; i < len; i++) {
			this.name += (char) f.read();
		}
	}

	public void readValue(InputStream f) throws IOException {
		
	}

	public void dump() {
		System.out.print("NULL");

	}

	public void dump(int tab) {
		this.dump();
	}

	public void print() {
		this.dump();
		System.out.println("");
	}

	static public int type = 0;
}
