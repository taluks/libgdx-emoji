package com.libgdxemoji;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Emoji;
import com.badlogic.gdx.graphics.g2d.ExtendedFreeTypeBitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.freetype.ExtendedFreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.Hinting;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;

public class EmojiTest extends ApplicationAdapter {

	Stage stage;

	public EmojiTest() {

	}

	@Override
	public void create() {
		FreeTypeFontGenerator generator = new ExtendedFreeTypeFontGenerator(Gdx.files.internal("arialbd.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = 16;
		parameter.hinting = Hinting.None;
		parameter.magFilter = TextureFilter.Linear;
		parameter.minFilter = TextureFilter.Linear;
		ExtendedFreeTypeBitmapFont font = (ExtendedFreeTypeBitmapFont) generator.generateFont(parameter);

		for (short i = 0; i < Emoji.CODES.length; i++) {
			int originalCode = Emoji.CODES[i];
			char replacementCode = (char) ('\uFFFF' - i);
			font.putSurrogateCode(originalCode, replacementCode);
			font.appendEmoji(replacementCode, String.format("emoji/%s.png", Emoji.hexFromCodePoint(originalCode)));
		}
		
		TextFieldStyle style = new TextFieldStyle();
		style.font = font;
		style.cursor =  new NinePatchDrawable(new NinePatch(new Texture(Gdx.files.internal("cursor.png")), 1, 1, 1, 1));
		style.selection =  new NinePatchDrawable(
				new NinePatch(new Texture(Gdx.files.internal("text-field-selection.png")), 1, 1, 1, 1)).tint(Color.FIREBRICK);
		style.fontColor = Color.WHITE;
		style.font.getData().markupEnabled = true;

		TextArea textArea = new TextArea("", style);
		textArea.setSize(250, 250);
		
		String text = FreeTypeFontGenerator.DEFAULT_CHARS.replaceAll("\\[", "") + "\n"; // remove [ for TextArea with markupEnabled
		for (int code : Emoji.CODES) {
			text += String.valueOf(Character.toChars(code)) + " ";
		}
		textArea.setText("TextArea text and emojis:\n" + text);		
		
		text = text.codePoints().map(i -> font.surrogateCodeMap.containsKey(i)? font.surrogateCodeMap.get(i): i)
				.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
		
		String colorGreen = String.format("[#%s]", Color.GREEN.toString());
		String colorWhite = String.format("[#%s]", Color.WHITE.toString());
		LabelStyle labelStyle = new LabelStyle(font, Color.WHITE);
		Label label = new Label(colorGreen + "Label text and emojis:\n" + colorWhite + text, labelStyle);
		label.setAlignment(Align.topLeft);
		label.setWrap(true);

		Table table = new Table();
		table.setFillParent(true);
		table.add(textArea).expand().fill().row();
		table.add(label).expand().fill();
		table.debugAll();

		stage = new Stage();
		stage.addActor(table);
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.act();
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
	}
}
