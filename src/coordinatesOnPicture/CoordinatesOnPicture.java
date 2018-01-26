
package coordinatesOnPicture;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class CoordinatesOnPicture extends JPanel implements MouseListener {
	PictureContainer container;
	JTextArea textArea;
	private ArrayList<Integer> lesPositionsClicX = new ArrayList<>();
	private ArrayList<Integer> lesPositionsClicY = new ArrayList<>();
	static String filename = "";
	static String saveFilePath = "";
	static BufferedImage img = null;

	public static void main(String[] args) {
		if (args.length == 2) {
			filename = args[0];
			saveFilePath = args[1];
		}
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} catch (UnsupportedLookAndFeelException ex) {
			ex.printStackTrace();
		} catch (IllegalAccessException ex) {
			ex.printStackTrace();
		} catch (InstantiationException ex) {

			ex.printStackTrace();
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}
		UIManager.put("swing.boldMetal", Boolean.FALSE);

		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}

	/**
	 * Create the GUI and show it. For thread safety, this method should be invoked
	 * from the event-dispatching thread.
	 */
	private static void createAndShowGUI() {
		// Create and set up the window.
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Create and set up the content pane.
		JComponent newContentPane = new CoordinatesOnPicture();
		newContentPane.setOpaque(true); // content panes must be opaque

		frame.setContentPane(newContentPane);

		Border border = newContentPane.getBorder();
		Border margin = new EmptyBorder(0, 0, 0, 0);
		newContentPane.setBorder(new CompoundBorder(border, margin));

		// Display the window.
		frame.pack();
		frame.setVisible(true);
	}

	public CoordinatesOnPicture() {
		super(new GridLayout(0, 1));

		File f = null;

		// read image
		try {
			f = new File(filename);

			img = ImageIO.read(f);
		} catch (IOException e) {
			System.out.println(e);
		}
		container = new PictureContainer(img);
		add(container);

		container.addMouseListener(this);
		// container.addMouseMotionListener(this);
		addMouseListener(this);
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {
		/*
		 * left clic = add new point
		 */
		if (e.getButton() == 1) {

			int positionClicSurImageX = e.getLocationOnScreen().x - this.getLocationOnScreen().x;
			int positionClicSurImageY = e.getLocationOnScreen().y - this.getLocationOnScreen().y;
			lesPositionsClicX.add(positionClicSurImageX);
			lesPositionsClicY.add(positionClicSurImageY);

			// print red cross at click position
			for (int i = 0; i < 5; i++) {
				img.setRGB(positionClicSurImageX + i, positionClicSurImageY, Color.RED.getRGB());
				img.setRGB(positionClicSurImageX - i, positionClicSurImageY, Color.RED.getRGB());

				img.setRGB(positionClicSurImageX, positionClicSurImageY + i, Color.RED.getRGB());
				img.setRGB(positionClicSurImageX, positionClicSurImageY - i, Color.RED.getRGB());

			}
			// replace current img with the new one
			container.updateIcon(img);

			/*
			 * Right click = Delete previous point
			 */
		} else if (lesPositionsClicX.size() > 0 && e.getButton() == 3) {
			int indexClicSupprime = lesPositionsClicX.size() - 1;
			int positionClicSurImageXaRetirer = lesPositionsClicX.get(indexClicSupprime);
			int positionClicSurImageYaRetirer = lesPositionsClicY.get(indexClicSupprime);
			lesPositionsClicX.remove(indexClicSupprime);
			lesPositionsClicY.remove(indexClicSupprime);
			for (int i = 0; i < 5; i++) {
				img.setRGB(positionClicSurImageXaRetirer + i, positionClicSurImageYaRetirer, Color.GRAY.getRGB());
				img.setRGB(positionClicSurImageXaRetirer - i, positionClicSurImageYaRetirer, Color.GRAY.getRGB());

				img.setRGB(positionClicSurImageXaRetirer, positionClicSurImageYaRetirer + i, Color.GRAY.getRGB());
				img.setRGB(positionClicSurImageXaRetirer, positionClicSurImageYaRetirer - i, Color.GRAY.getRGB());

			}
			container.updateIcon(img);

			/*
			 * Middle click = save
			 */
		} else if (e.getButton() == 2) {

			File outputfile = new File(filename.replace(".", "_new."));
			try {
				String extension = filename.substring(filename.indexOf('.')).replace(".", "");
				ImageIO.write(img, extension, outputfile);
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			try {
				File saveFile = new File(saveFilePath);
				FileWriter fw = new FileWriter(saveFile);
				BufferedWriter bw = new BufferedWriter(fw);
				StringBuilder sb = new StringBuilder();
				for (Integer i : lesPositionsClicX) {
					Integer positionY = lesPositionsClicY.get(lesPositionsClicX.indexOf(i));
					sb.append(i + ";" + positionY + "\n");
				}
				bw.write(sb.toString());
				bw.close();
				fw.close();
				System.out.println("\n");
				System.out.println("_*_*_*_*_*_*_*_*_*_*_*_");
				System.out.println("All good !");
				System.out.println("XY coordinates saved under:" + saveFile.getAbsolutePath());
				System.out.println("Image with marked coordinates saved under:" + outputfile.getAbsolutePath());
				System.out.println("_*_*_*_*_*_*_*_*_*_*_*_");
				System.out.println("\n");
				System.exit(0);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

}
