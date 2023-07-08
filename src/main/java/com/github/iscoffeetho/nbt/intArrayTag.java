package com.github.iscoffeetho.nbt;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;

public class intArrayTag extends listTag<intTag> {
	public int[] getIntArray() {
		int[] ia = new int[this.arrayList.size()];
		Iterator<intTag> it = this.arrayList.iterator();
		int i = 0;
		while (it.hasNext()) {
			ia[i] = it.next().get();
			i++;
		}
		return ia;
	}

	@Override
	public void writeType(OutputStream f) throws IOException {
		f.write(intArrayTag.type);
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
		Iterator<intTag> it = this.arrayList.iterator();
		while (it.hasNext()) {
			it.next().writeValue(f);
		}
	}

	@Override
	public void readValue(InputStream f) throws IOException {
		int length = f.read() << 3 * 8;
		length |= f.read() << 2 * 8;
		length |= f.read() << 8;
		length |= f.read();
		for (int i = 0; i < length; i++) {
			intTag newtag = new intTag();
			newtag.readValue(f);
			this.arrayList.add(newtag);
		}
	}

	@Override
	public void dump() {
		this.dump(0);
	}

	@Override
	public void dump(int tab) {
		System.out.print("[I;");
		Iterator<intTag> it = this.arrayList.iterator();
		if (!it.hasNext()) {
			System.out.println(" ]");
			return;
		}
		System.out.println("");
		tab++;
		while (it.hasNext()) {
			for (int i = 0; i < tab; i++)
				System.out.print("  ");
			intTag t = it.next();
			t.dump(tab);
			System.out.println(",");
		}
		tab--;
		for (int i = 0; i < tab; i++)
			System.out.print("  ");
		System.out.print("]");
	}

	static public int type = 11;
}
