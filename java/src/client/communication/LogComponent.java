package client.communication;

import java.util.List;
import java.util.*;
import java.awt.*;
import java.awt.font.*;
import java.awt.geom.*;

import javax.swing.*;

import shared.definitions.*;
import client.utils.*;

/**
 * Custom component that displays a log of messages. Used in both the chat and
 * game history views.
 */
@SuppressWarnings("serial")
public final class LogComponent extends JComponent {
	private int LEFT_MARGIN = 5;
	private int RIGHT_MARGIN = 5;
	private int TOP_MARGIN = 3;
	private int BOTTOM_MARGIN = 3;
	
	private Font font;
	
	private List<LogEntry> entries;
	
	LogComponent() {
		this.setBackground(Color.white);
		this.setOpaque(true);
		
		final Font tmpFont = new JLabel("").getFont();
		font = tmpFont.deriveFont(tmpFont.getStyle(), 24);
		
		setEntries(null);
	}
	
	void setEntries(List<LogEntry> entries) {
		if(entries == null || entries.size() == 0) {
			this.entries = new ArrayList<LogEntry>();
			this.entries.add(new LogEntry(CatanColor.WHITE, "No messages"));
		} else {
			this.entries = entries;
		}
		
		if (this.getWidth() > 0) {
			updateSize(this.getWidth());
		}
	}
	
	private void updateSize(int width) {
		final int height = this.getPreferredHeight(width);
		final Dimension newSize = new Dimension(width, height);
		this.setPreferredSize(newSize);
		this.setSize(newSize);
	}
	
	private int getPreferredHeight(int width) {
		final Graphics2D g2 = ImageUtils.DEFAULT_IMAGE.createGraphics();
		return draw(g2, width);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw((Graphics2D)g, this.getWidth());
	}
	
	private int draw(Graphics2D g2, int width) {
		int y = 0;
		
		FontMetrics fontMetrics = this.getFontMetrics(font);
		FontRenderContext fontContext = fontMetrics.getFontRenderContext();
		
		g2.setFont(font);
		
		for (LogEntry entry : entries) {
			final List<String> lines = wrapText(fontContext, entry.getMessage(), width);
			int rectHeight = TOP_MARGIN + BOTTOM_MARGIN + lines.size()
							 * fontMetrics.getHeight();
			
			g2.setColor(entry.getColor().getJavaColor());
			g2.fillRect(0, y, this.getWidth(), rectHeight);
			
			g2.setColor(Color.white);
			g2.drawRect(0, y, this.getWidth(), rectHeight);
			
			g2.setColor(Color.black);
			
			y += TOP_MARGIN + fontMetrics.getAscent();
			
			for (int i = 0; i < lines.size(); ++i) {
				if(i > 0) {
					y += fontMetrics.getHeight();
				}
				g2.drawString(lines.get(i), LEFT_MARGIN, y);
			}
			y += fontMetrics.getDescent() + BOTTOM_MARGIN;
		}
		
		return y;
	}
	
	private List<String> wrapText(FontRenderContext context, String text, int width) {
		final int MAX_WIDTH = width - LEFT_MARGIN - RIGHT_MARGIN;
		
		final List<String> result = new ArrayList<>();
		
		try(final Scanner scanner = new Scanner(text)) {
			scanner.useDelimiter("\\s+");
			String line = "";
			while(scanner.hasNext()) {
				String word = scanner.next();
				
				if(line.length() == 0) {
					// Each line must have at least one word (even if
					// it doesn't fit)
					line = word;
				} else {
					// Check to see if the line can fit another word
					String newLine = line + " " + word;
					
					Rectangle2D bounds = font.getStringBounds(newLine, context);
					if(bounds.getWidth() <= MAX_WIDTH) {
						line = newLine;
					} else {
						result.add(line);
						line = word;
					}
				}
			}
			
			if(line.length() > 0) {
				result.add(line);
			}
		}
		return result;
	}
}
