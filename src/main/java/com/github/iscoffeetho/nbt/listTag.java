package com.github.iscoffeetho.nbt;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;

public class listTag<E extends NBTTag> extends NBTTag {
	public ArrayList<E> arrayList;

	public listTag() {
		super();
		this.arrayList = new ArrayList<E>();
	}

	@Override
	public void writeType(OutputStream f) throws IOException {
		f.write(listTag.type);
	}

	@Override
	public void readValue(InputStream f) throws IOException {
		int type = f.read();
		switch (type) {
			case 1: case 2: case 3:
			case 4: case 5: case 6:
			case 7: case 8: case 9:
			case 10: case 11: case 12:
				break;
			default:
				return;
		}
		int length = f.read() << 3 * 8;
		length |= f.read() << 2 * 8;
		length |= f.read() << 8;
		length |= f.read();
		for (int i = 0; i < length; i++) {
			NBTTag newTag = null;
			switch (type) {
				case 1:
					newTag = new byteTag();
					break;
				case 2:
					newTag = new shortTag();
					break;
				case 3:
					newTag = new intTag();
					break;
				case 4:
					newTag = new longTag();
					break;
				case 5:
					newTag = new floatTag();
					break;
				case 6:
					newTag = new doubleTag();
					break;
				case 7:
					newTag = new byteArrayTag();
					break;
				case 8:
					newTag = new stringTag();
					break;
				case 9:
					newTag = new listTag<>();
					break;
				case 10:
					newTag = new compoundTag("");
					break;
				case 11:
					newTag = new intArrayTag();
					break;
				case 12:
					newTag = new longArrayTag();
					break;
				default:
					newTag = null;
			}
			newTag.readValue(f);
			// this.arrayList.add((E) newTag); // This annoys me
		}
	}

	@Override
	public void dump() {
		this.dump(0);
	}

	@Override
	public void dump(int tab) {
		System.out.print("[");
		Iterator<E> it = this.arrayList.iterator();
		if (!it.hasNext()) {
			System.out.println(" ]");
			return;
		}
		System.out.println("");
		tab++;
		while (it.hasNext()) {
			for (int i = 0; i < tab; i++)
				System.out.print("  ");
			E t = it.next();
			t.dump(tab);
			System.out.println(",");
		}
		tab--;
		for (int i = 0; i < tab; i++)
			System.out.print("  ");
		System.out.print("]");
	}

	static public int type = 9;
}
