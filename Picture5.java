/*************************************************************************
 *  Compilation:  java Picture.java
 *  Execution:    java Picture imagename
 *
 *  Data type for manipulating individual pixels of an image. The original
 *  image can be read from a file in jpg, gif, or png format, or the
 *  user can create a blank image of a given size. Includes methods for
 *  displaying the image in a window on the screen or saving to a file.
 *
 *  % java Picture mandrill.jpg
 *
 *  Remarks
 *  -------
 *   - pixel (x, y) is column x and row y, where (0, 0) is upper left
 *
 *   - see also GrayPicture.java for a grayscale version
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
public final class Picture5 implements ActionListener {
    private BufferedImage image;    // the rasterized image
    private JFrame frame;           // on-screen view
    private String filename;        // name of file

   /**
     * Create an empty w-by-h picture.
     */
    public Picture5(int w, int h) {
        image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        // set to TYPE_INT_ARGB to support transparency
        filename = w + "-by-" + h;
    }

   /**
     * Create a picture by reading in a .png, .gif, or .jpg from
     * the given filename or URL name.
     */
    public Picture5(String filename) {
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
     * Create a picture by reading in a .png, .gif, or .jpg from a File.
     */
    public Picture5(File file) {
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
     * Return a JLabel containing this Picture, for embedding in a JPanel,
     * JFrame or other GUI widget.
     */
    public JLabel getJLabel() {
        if (image == null) { return null; }         // no image available
        ImageIcon icon = new ImageIcon(image);
        return new JLabel(icon);
    }

   /**
     * Display the picture in a window on the screen.
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
     * Return the height of the picture (in pixels).
     */
    public int height() {
        return image.getHeight(null);
    }

   /**
     * Return the width of the picture (in pixels).
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
     * Save the picture to a file in a standard image format.
     * The filetype must be .png or .jpg.
     */
    public void save(String name) {
        save(new File(name));
    }

   /**
     * Save the picture to a file in a standard image format.
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
     * Test client. Reads a picture specified by the command-line argument,
     * and shows it in a window on the screen.
     */
//    public static void main(String[] args) {
//    	Picture pic1 = new Picture("lich.png");
//        pic1.show();
//        int width  = pic1.width();
//        int height = pic1.height();
//        double x0 = 0.5 * (width  - 1);
//        double y0 = 0.5 * (height - 1);
//
//        Picture pic2 = new Picture(width, height);
//
//        // swirl
//        
//        int xx0 = 100, yy0 = 100;
//        int xx1 = 400, yy1 = 400;
//        
//        for (int sx = 0; sx < width; sx++) {
//            for (int sy = 0; sy < height; sy++) {
//            	
//            	if (sx >= xx0 && sx <= xx1 && sy >=yy0 && sy <=yy1){
//            	
//	                double dx = sx - x0;
//	                double dy = sy - y0;
//	                double r = Math.sqrt(dx*dx + dy*dy);
//	                double angle = Math.PI / 256 * r;
//	                int tx = (int) (+dx * Math.cos(angle) - dy * Math.sin(angle) + x0);
//	                int ty = (int) (+dx * Math.sin(angle) + dy * Math.cos(angle) + y0);
//	
//	                // plot pixel (sx, sy) the same color as (tx, ty) if it's in bounds
//	                if (tx >= 0 && tx < width && ty >= 0 && ty < height)
//	                    pic2.set(sx, sy, pic1.get(tx, ty));
//            	}
//            	else
//            		pic2.set(sx, sy, pic1.get(sx, sy));
//                
//            }
//        }
//
//        pic2.show();
//    }
    public static void main(String[] args) {
    	Picture pic1 = new Picture("lich.png");
//    	Picture pic2 = new Picture("lich_edge.png");
        pic1.show();
        int width  = pic1.width();
        int height = pic1.height();

        Picture pic2 = new Picture(width, height);

        // warp
        
        int x0 = 100;
        int x1 = 200;
        int x2 = 300;
        int x3 = 400;
        
        int i,j;
        
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
            	
            	// x axis
            	if(x < 100)
            	{
            		i = (int)(200 * x)/100;
            	}
            	else if(x > 100 & x < 400)
            	{
            		i = (x - 100)/3 + 200;
            	}
            	else
            	{
            		i = ((212/112)*(x-400)) + 300;
            	}
            	
            	// y axis
            	
//            	if(y < 100)
//            	{
//            		j = (int)(200 * y)/100;
//            	}
//            	else if(y > 100 & y < 400)
//            	{
//            		j = (y - 100)/3 + 200;
//            	}
//            	else
//            	{
//            		j = ((212/112)*(y-400)) + 300;
//            	}
            	
            	
            	pic2.set(x, y, pic1.get(i, y));
                
            }
        }

        pic2.show();
    }
}
