package client.utils;

import java.awt.Font;
import javax.swing.JComponent;

public final class FontUtils {
	public static void setFont(final JComponent comp, final int size) {
		Font font = comp.getFont();
		font = font.deriveFont(font.getStyle(), size);
		comp.setFont(font);
	}
}
