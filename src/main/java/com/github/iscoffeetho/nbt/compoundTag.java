package com.github.iscoffeetho.nbt;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import java.util.Iterator;

public class compoundTag extends NBTTag {
	private ArrayList<NBTTag> _ = null;

	public compoundTag(String name) {
		super(name);
		this._ = new ArrayList<NBTTag>();
	}

	public void add(String name, NBTTag tag) {
		tag.name = name;
		this.add(tag);
	}

	public void add(NBTTag tag) {
		if (tag.name == null)
			tag.name = Integer.toString(this._.size());
		this._.add(tag);
	}

	public NBTTag get(String name) {
		Iterator<NBTTag> it = this._.iterator();
		while (it.hasNext()) {
			NBTTag tag = it.next();
			if (tag.name.equals(name))
				return tag;
		}
		return null;
	}

	public Iterator<NBTTag> iterator() {
		return this._.iterator();
	}

	public void remove(String name) {
		Iterator<NBTTag> it = _.iterator();
		while (it.hasNext()) {
			NBTTag tag = it.next();
			if (tag.name.equals(name)) {
				it.remove();
				return;
			}
		}
	}

	@Override
	public void writeType(OutputStream f) throws IOException {
		f.write(compoundTag.type);
	}

	@Override
	public void writeValue(OutputStream f) throws IOException {
		Iterator<NBTTag> it = this.iterator();
		while (it.hasNext()) {
			it.next().writeTo(f);
		}
		f.write(0);
	}

	@Override
	public void readFrom(InputStream f) throws IOException {
		if (f.read() != compoundTag.type)
			throw new IOException("Invalid Type, trying to read compound but didn't find it.");
		this.readName(f);
		this.readValue(f);
	}

	public void readValue(InputStream f) throws IOException {
		while (true) {
			int type = f.read();
			NBTTag newTag = null;
			switch (type) {
				case 0:
					return;
				case 1: newTag = new byteTag(); break;
				case 2: newTag = new shortTag(); break;
				case 3: newTag = new intTag(); break;
				case 4: newTag = new longTag(); break;
				case 5: newTag = new floatTag(); break;
				case 6: newTag = new doubleTag(); break;
				case 7: newTag = new byteArrayTag(); break;
				case 8: newTag = new stringTag(); break;
				case 9: newTag = new listTag<>(); break;
				case 10: newTag = new compoundTag(""); break; // this is a workaround sadly bc java
				case 11: newTag = new intArrayTag(); break;
				case 12: newTag = new longArrayTag(); break;
				default:
					throw new IOException("Invalid Type " + Integer.toHexString(type) + ".");
			}
			newTag.readName(f);
			newTag.readValue(f);
			this._.add(newTag);
		}
	}

	@Override
	public void dump() {
		this.dump(0);
	}

	@Override
	public void dump(int tab) {
		System.out.print("{");
		Iterator<NBTTag> it = this._.iterator();
		if (!it.hasNext()) {
			System.out.print(" }");
			return;
		}
		System.out.println("");
		tab++;
		while (it.hasNext()) {
			for (int i = 0; i < tab; i++)
				System.out.print("  ");
			NBTTag t = it.next();
			System.out.print('"' + t.name + "\" : ");
			t.dump(tab);
			System.out.println(",");
		}
		tab--;
		for (int i = 0; i < tab; i++)
			System.out.print("  ");
		System.out.print("}");
	}

	static public int type = 10;
}
