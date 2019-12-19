package com.badlogic.gdx.graphics.g2d.freetype;

import com.badlogic.gdx.graphics.g2d.PixmapPacker;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeBitmapFontData;

public class ExtendedFreeTypeBitmapFontData extends FreeTypeBitmapFontData {

	public PixmapPacker getPacker() {
		return this.packer;
	}	

}
