package com.badlogic.gdx.graphics.g2d;

import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Filter;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.freetype.ExtendedFreeBitmapFontCache;
import com.badlogic.gdx.graphics.g2d.freetype.ExtendedFreeTypeBitmapFontData;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;

public class ExtendedFreeTypeBitmapFont extends BitmapFont {

	public final ArrayMap<Integer, Character> surrogateCodeMap = new ArrayMap<>();
	private final Set<Integer> charSet = new HashSet<>();
	public PixmapPacker packer = null;

	public ExtendedFreeTypeBitmapFont() {
		this(new ExtendedFreeTypeBitmapFontData(), new TextureRegion(), false);
	}

	public ExtendedFreeTypeBitmapFont(ExtendedFreeTypeBitmapFontData data, TextureRegion textureRegion,
			boolean integer) {
		super(data, textureRegion, integer);
	}

	public ExtendedFreeTypeBitmapFont(ExtendedFreeTypeBitmapFontData data, Array<TextureRegion> pageRegions,
			boolean integer) {
		super(data, pageRegions, integer);
	}

	@Override
	public BitmapFontCache newFontCache() {
		return new ExtendedFreeBitmapFontCache(this, integer);
	}

	private void putGlyph(char c, Pixmap pixmap) {
		Rectangle rect = packer.pack(String.valueOf(c), pixmap);
		pixmap.dispose();

		Glyph glyph = new Glyph();
		glyph.id = c;
		glyph.page = packer.getPages().size - 1;
		glyph.srcX = (int) rect.x;
		glyph.srcY = (int) rect.y;
		glyph.width = (int) rect.width;
		glyph.height = (int) rect.height;
		glyph.xadvance = (int) rect.width;
		glyph.yoffset = -(int)rect.height;

		data.down = -rect.height;
		data.capHeight = rect.height;
		data.lineHeight = rect.height;
		data.ascent = -2;
		data.setGlyphRegion(glyph, regions.get(glyph.page));
		data.setGlyph(c, glyph);
	}

	public ExtendedFreeTypeBitmapFont appendEmoji(int code, Pixmap pixmap) {
		if (!charSet.add(code))
			return this;
		if (packer == null) {
			packer = ((ExtendedFreeTypeBitmapFontData) data).getPacker();
		}
		putGlyph((char) code, pixmap);
		return this;
	}

	public ExtendedFreeTypeBitmapFont appendEmoji(int code, String imgname, int size) {
		Pixmap pixmap = new Pixmap(Gdx.files.internal(imgname));
		pixmap.setFilter(Filter.BiLinear);
		Pixmap pixmap2 = new Pixmap(size, size, Format.RGBA8888);
		pixmap2.drawPixmap(pixmap, 0, 0, pixmap.getWidth(), pixmap.getHeight(), 0, 0, size, size);
		pixmap.dispose();
		pixmap = null;
		appendEmoji(code, pixmap2);
		return this;
	}

	public ExtendedFreeTypeBitmapFont appendEmoji(int code, String imgname) {
		return appendEmoji(code, new Pixmap(Gdx.files.internal(imgname)));
	}
	
	public void putSurrogateCode(int original, char replacementCode) {
		surrogateCodeMap.put(original, replacementCode);
	}
}
