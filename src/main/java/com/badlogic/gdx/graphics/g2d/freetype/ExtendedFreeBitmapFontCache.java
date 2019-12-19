package com.badlogic.gdx.graphics.g2d.freetype;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.ExtendedFreeTypeGlyphLayout;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pools;

public class ExtendedFreeBitmapFontCache extends BitmapFontCache {

	private final Array<ExtendedFreeTypeGlyphLayout> pooledLayouts = new Array<>();

	public ExtendedFreeBitmapFontCache(BitmapFont font) {
		super(font);
	}

	public ExtendedFreeBitmapFontCache(BitmapFont font, boolean integer) {
		super(font, integer);
	}	

	@Override
	public GlyphLayout addText(CharSequence str, float x, float y, int start, int end, float targetWidth, int halign,
			boolean wrap, String truncate) {	
		ExtendedFreeTypeGlyphLayout layout = Pools.obtain(ExtendedFreeTypeGlyphLayout.class);
		pooledLayouts.add(layout);
		layout.setText(getFont(), str, start, end, getColor(), targetWidth, halign, wrap, truncate);
		addText(layout, x, y);
		return layout;
	}

	public void clear() {
		Pools.freeAll(pooledLayouts, true);
		super.clear();
	}

}
