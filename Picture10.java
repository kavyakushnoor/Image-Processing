/*************************************************************************
 *  Compilation:  java Picture8.java
 *  Execution:    java Picture8 imagename
 *
 *  Data type for manipulating individual pixels of an image. The original
 *  image can be read from a file in jpg, gif, or png format, or the
 *  user can create a blank image of a given size. Includes methods for
 *  displaying the image in a window on the screen or saving to a file.
 *
 *  % java Picture8 mandrill.jpg
 *
 *  Remarks
 *  -------
 *   - pixel (x, y) is column x and row y, where (0, 0) is upper left
 *
 *   - see also GrayPicture8.java for a grayscale version
 *
 *************************************************************************/

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;
import java.awt.FileDialog;
import java.awt.Toolkit;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;


/**
 *  This class provides methods for manipulating individual pixels of
 *  an image. The original image can be read from a file in JPEG, GIF,
 *  or PNG format, or the user can create a blank image of a given size.
 *  This class includes methods for displaying the image in a window on
 *  the screen or saving to a file.
 *  <p>
 *  Pixel (x, y) is column x, row y, where (0, 0) is upper left.
 *  <p>
 *  For additional documentation, see
 *  <a href="http://www.cs.princeton.edu/introcs/31datatype">Section 3.1</a> of
 *  <i>Introduction to Programming in Java: An Interdisciplinary Approach</i>
 *  by Robert Sedgewick and Kevin Wayne.
 */
public final class Picture10 implements ActionListener {
    private BufferedImage image;    // the rasterized image
    private JFrame frame;           // on-screen view
    private String filename;        // name of file

   /**
     * Create an empty w-by-h Picture8.
     */
    public Picture10(int w, int h) {
        image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        // set to TYPE_INT_ARGB to support transparency
        filename = w + "-by-" + h;
    }

   /**
     * Create a Picture8 by reading in a .png, .gif, or .jpg from
     * the given filename or URL name.
     */
    public Picture10(String filename) {
        this.filename = filename;
        try {
            // try to read from file in working directory
            File file = new File(filename);
            if (file.isFile()) {
                image = ImageIO.read(file);
            }

            // now try to read from file in same directory as this .class file
            else {
                URL url = getClass().getResource(filename);
                if (url == null) { url = new URL(filename); }
                image = ImageIO.read(url);
            }
        }
        catch (IOException e) {
            // e.printStackTrace();
            throw new RuntimeException("Could not open file: " + filename);
        }

        // check that image was read in
        if (image == null) {
            throw new RuntimeException("Invalid image file: " + filename);
        }
    }

   /**
     * Create a Picture8 by reading in a .png, .gif, or .jpg from a File.
     */
    public Picture10(File file) {
        try { image = ImageIO.read(file); }
        catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not open file: " + file);
        }
        if (image == null) {
            throw new RuntimeException("Invalid image file: " + file);
        }
    }

   /**
     * Return a JLabel containing this Picture8, for embedding in a JPanel,
     * JFrame or other GUI widget.
     */
    public JLabel getJLabel() {
        if (image == null) { return null; }         // no image available
        ImageIcon icon = new ImageIcon(image);
        return new JLabel(icon);
    }

   /**
     * Display the Picture8 in a window on the screen.
     */
    public void show() {

        // create the GUI for viewing the image if needed
        if (frame == null) {
            frame = new JFrame();

            JMenuBar menuBar = new JMenuBar();
            JMenu menu = new JMenu("File");
            menuBar.add(menu);
            JMenuItem menuItem1 = new JMenuItem(" Save...   ");
            menuItem1.addActionListener(this);
            menuItem1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
                                     Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
            menu.add(menuItem1);
            frame.setJMenuBar(menuBar);



            frame.setContentPane(getJLabel());
            // f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setTitle(filename);
            frame.setResizable(false);
            frame.pack();
            frame.setVisible(true);
        }

        // draw
        frame.repaint();
    }

   /**
     * Return the height of the Picture8 (in pixels).
     */
    public int height() {
        return image.getHeight(null);
    }

   /**
     * Return the width of the Picture8 (in pixels).
     */
    public int width() {
        return image.getWidth(null);
    }

   /**
     * Return the Color of pixel (i, j).
     */
    public Color get(int i, int j) {
        return new Color(image.getRGB(i, j));
    }
    
    /**
     * Return the Color of pixel (i, j).
     */
    public Color[][] getColorArray() {
    	Color[][] c = new Color[height()][width()];
    	for(int i = 0; i < c[0].length; i++)
    		for(int j = 0; j < c.length; j++)
    			c[j][i] = new Color(image.getRGB(i, j));
        return c;
    }

   /**
     * Set the Color of pixel (i, j) to c.
     */
    public void set(int i, int j, Color c) {
        if (c == null) { throw new RuntimeException("can't set Color to null"); }
        image.setRGB(i, j, c.getRGB());
    }

   /**
     * Save the Picture8 to a file in a standard image format.
     * The filetype must be .png or .jpg.
     */
    public void save(String name) {
        save(new File(name));
    }

   /**
     * Save the Picture8 to a file in a standard image format.
     */
    public void save(File file) {
        this.filename = file.getName();
        if (frame != null) { frame.setTitle(filename); }
        String suffix = filename.substring(filename.lastIndexOf('.') + 1);
        suffix = suffix.toLowerCase();
        if (suffix.equals("jpg") || suffix.equals("png")) {
            try { ImageIO.write(image, suffix, file); }
            catch (IOException e) { e.printStackTrace(); }
        }
        else {
            System.out.println("Error: filename must end in .jpg or .png");
        }
    }

   /**
     * Opens a save dialog box when the user selects "Save As" from the menu.
     */
    public void actionPerformed(ActionEvent e) {
        FileDialog chooser = new FileDialog(frame,
                             "Use a .png or .jpg extension", FileDialog.SAVE);
        chooser.setVisible(true);
        if (chooser.getFile() != null) {
            save(chooser.getDirectory() + File.separator + chooser.getFile());
        }
    }

   /**
     * Test client. Reads a Picture8 specified by the command-line argument,
     * and shows it in a window on the screen.
     */
    public static void main(String[] args) {
        Picture10 picture1 = new Picture10("lich.png" );
        Picture10 picture2 = new Picture10("joker.png" );
        
        System.out.printf("%d-by-%d\n", picture1.width(), picture1.height());
        System.out.printf("%d-by-%d\n", picture2.width(), picture2.height());
        
        int n = 200;         // # of frames

        int width  = picture1.width();
        int height = picture1.height();
        Picture picture = new Picture(width, height);
        double alpha;
        
        
        for (int k = 0; k <= n; k++) {
            alpha = k/n;//0.5;
            
	        try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
            for (int col = 0; col < width; col++) 
            {
                for (int row = 0; row < height; row++) 
                {
                    Color c1 = picture1.get(col, row);
                    Color c2 = picture2.get(col, row);
                    
                    int r = (int) (alpha * c1.getRed()   + (1 - alpha) * c2.getRed());
                    int g = (int) (alpha * c1.getGreen() + (1 - alpha) * c2.getGreen());
                    int b = (int) (alpha * c1.getBlue()  + (1 - alpha) * c2.getBlue());
                    
                    Color newColor = new Color (r,g,b);
                    
                    picture.set(col, row, newColor);
                }
            }
            picture.show();
        }
        
    }

}
