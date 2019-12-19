package com.badlogic.gdx.graphics.g2d.freetype;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ExtendedFreeTypeBitmapFont;
import com.badlogic.gdx.utils.Array;

public class ExtendedFreeTypeFontGenerator extends FreeTypeFontGenerator {

	public ExtendedFreeTypeFontGenerator(FileHandle fontFile) {
		super(fontFile);
	}

	@Override
	public BitmapFont generateFont(FreeTypeFontParameter parameter) {
		return generateFont(parameter, new ExtendedFreeTypeBitmapFontData());
	}

	@Override
	public BitmapFont generateFont(FreeTypeFontParameter parameter, FreeTypeBitmapFontData data) {
		parameter.incremental = true;
		boolean updateTextureRegions = data.regions == null && parameter.packer != null;
		if (updateTextureRegions) data.regions = new Array<>();
		generateData(parameter, data);
		if (updateTextureRegions)
			parameter.packer.updateTextureRegions(data.regions, parameter.minFilter, parameter.magFilter, parameter.genMipMaps);
		ExtendedFreeTypeBitmapFont font = new ExtendedFreeTypeBitmapFont((ExtendedFreeTypeBitmapFontData) data,	data.regions, true);
		font.setOwnsTexture(parameter.packer == null);
		return font;
	}

}
