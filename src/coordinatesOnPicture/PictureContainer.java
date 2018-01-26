package coordinatesOnPicture;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class PictureContainer extends JLabel {
	Dimension minSize;

	public PictureContainer(BufferedImage image) {
		minSize = new Dimension(image.getWidth(), image.getHeight());
		Icon icon = new ImageIcon(image);
		setMaximumSize(minSize);
		setIcon(icon);
		setOpaque(false);
	}

	public void updateIcon(BufferedImage image) {
		Icon icon = new ImageIcon(image);

		setIcon(icon);
	}
}
