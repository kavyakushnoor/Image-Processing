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
public final class Picture4 implements ActionListener {
    private BufferedImage image;    // the rasterized image
    private JFrame frame;           // on-screen view
    private String filename;        // name of file

   /**
     * Create an empty w-by-h picture.
     */
    public Picture4(int w, int h) {
        image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        // set to TYPE_INT_ARGB to support transparency
        filename = w + "-by-" + h;
    }

   /**
     * Create a picture by reading in a .png, .gif, or .jpg from
     * the given filename or URL name.
     */
    public Picture4(String filename) {
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
    public Picture4(File file) {
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
    public static void main(String[] args) {
    	 Picture4 pic1 = new Picture4("lich.png");
    	 Picture4 pic2 = new Picture4("newImage.png");
//         pic1.show();
         int width  = pic1.width();
         int height = pic1.height();
         
         int width2  = pic2.width();
         int height2 = pic2.height();

         System.out.println("W2 = " + width2 + " H2 = " + height2);
         
         double angle = Math.toRadians(30);
         double sin = Math.sin(angle);
         double cos = Math.cos(angle);

//         Picture pic2 = new Picture(width, height);
         
         for(int x=0;x< pic2.width() -1; x++)
         {
        	 for(int y=0;y<pic2.height()-1;y++)
        	 {
        		 Color newColor = new Color(0,0,0);
        		 pic2.set(x,y, newColor);
        	 }
         }
         
         // rotation
//         for (int x = -256; x < 256; x++) 
//         {
//             for (int y = -256; y < 256; y++) 
//             {
//                 
//            	 double x1 = x * cos - y * sin;
//            	 double y1 = x * sin + y * cos;
//            	 
//            	 x1 = x1 + 256;
//            	 y1 = y1 + 256;
//            	 
//            	 if(x1 >= 0 & y1 >= 0 & x1 < width & y1 < height)
//            	 {
//            		 pic2.set((int)x1,(int)y1, pic1.get(x +256, y+256));
//            	 }
//            	 else
//            	 {
//            	 }
//             }
//         }
         
         for (int x = -256; x < 256; x++) 
         {
             for (int y = -256; y < 256; y++) 
             {
                 
            	 double x1 = x * cos - y * sin;
            	 double y1 = x * sin + y * cos;
            	 
            	 x1 = x1 + 363;
            	 y1 = y1 + 363;
            	  
            	 if(x1 >= 0 & y1 >= 0 & x1 < pic2.width() & y1 < pic2.height())
            	 {
            		 pic2.set((int)x1,(int)y1, pic1.get(x + 256, y + 256));
            	 }
            	 else
            	 {
            	 }
             }
         }
    

         pic2.show();
        
    }

}
