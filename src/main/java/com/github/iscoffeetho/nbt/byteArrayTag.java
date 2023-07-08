package com.github.iscoffeetho.nbt;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;

public class byteArrayTag extends listTag<byteTag> {
	public byte[] getByteArray() {
		byte[] ba = new byte[this.arrayList.size()];
		Iterator<byteTag> it = this.arrayList.iterator();
		int i = 0;
		while (it.hasNext()) {
			ba[i] = (byte) it.next().get();
			i++;
		}
		return ba;
	}

	@Override
	public void writeType(OutputStream f) throws IOException {
		f.write(byteArrayTag.type);
	}
	@Override
	public void writeValue(OutputStream f) throws IOException {
		int len = this.arrayList.size();
		f.write((len >> 24) & 0xFF);
		f.write((len >> 16) & 0xFF);
		f.write((len >> 8) & 0xFF);
		f.write(len & 0xFF);
		if (len == 0)
			return;
		Iterator<byteTag> it = this.arrayList.iterator();
		while (it.hasNext()) {
			it.next().writeValue(f);
		}
	}

	@Override
	public void readFrom(InputStream f) {

	}

	@Override
	public void dump() {
		this.dump(0);
	}

	@Override
	public void dump(int tab) {
		System.out.print("[B;");
		Iterator<byteTag> it = this.arrayList.iterator();
		if (!it.hasNext()) {
			System.out.println(" ]");
			return;
		}
		System.out.println("");
		tab++;
		while (it.hasNext()) {
			for (int i = 0; i < tab; i++)
				System.out.print("  ");
			byteTag t = it.next();
			t.dump(tab);
			System.out.println(",");
		}
		tab--;
		for (int i = 0; i < tab; i++)
			System.out.print("  ");
		System.out.print("]");
	}

	static public int type = 7;
}
