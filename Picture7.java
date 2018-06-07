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
import java.util.Arrays;


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
public final class Picture7 implements ActionListener {
    private BufferedImage image;    // the rasterized image
    private JFrame frame;           // on-screen view
    private String filename;        // name of file

   /**
     * Create an empty w-by-h picture.
     */
    public Picture7(int w, int h) {
        image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        // set to TYPE_INT_ARGB to support transparency
        filename = w + "-by-" + h;
    }

   /**
     * Create a picture by reading in a .png, .gif, or .jpg from
     * the given filename or URL name.
     */
    public Picture7(String filename) {
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
    public Picture7(File file) {
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
    // Method for getting the maximum value
    public static int getMax(int[] inputArray){ 
      int maxValue = inputArray[0]; 
      for(int i=1;i < inputArray.length;i++){ 
        if(inputArray[i] > maxValue){ 
           maxValue = inputArray[i]; 
        } 
      } 
      return maxValue; 
    }
   
    // Method for getting the minimum value
    public static int getMin(int[] inputArray){ 
      int minValue = inputArray[0]; 
      for(int i=1;i<inputArray.length;i++){ 
        if(inputArray[i] < minValue){ 
          minValue = inputArray[i]; 
        } 
      } 
      return minValue; 
    } 

   /**
     * Test client. Reads a picture specified by the command-line argument,
     * and shows it in a window on the screen.
     */
    public static void main(String[] args) {
        Picture7 pic = new Picture7("julia2.png" );
        System.out.printf("%d-by-%d\n", pic.width(), pic.height());
        
        Color testImage;
        
        int imageWidth, imageHeight;
        imageWidth = pic.width();
        imageHeight = pic.height();
        
        int redHisto[]=new int[256];
        int greenHisto[]=new int[256];
        int blueHisto[]=new int[256];
        
        int cdfredHisto[]=new int[256];
        int cdfgreenHisto[]=new int[256];
        int cdfblueHisto[]=new int[256];
        
        int cdfMinRed, cdfMinGreen, cdfMinBlue;
        
        int mapredHisto[]=new int[256];
        int mapgreenHisto[]=new int[256];
        int mapblueHisto[]=new int[256];
        
        Arrays.fill(redHisto, 0);
        Arrays.fill(greenHisto, 0);
        Arrays.fill(blueHisto, 0);
        
        /* Find frequency */
        
        for(int i=0;i<imageWidth;i++)
        {
        	for(int j=0;j<imageHeight;j++)
        	{
        		testImage = pic.get(i,j);
        		
        		int red = (int) (testImage.getRed());
				int green = (int) (testImage.getGreen());
				int blue = (int) (testImage.getBlue());
				int alpha = (int) (testImage.getAlpha());
				
				redHisto[red]++;
				greenHisto[green]++;
				blueHisto[blue]++;
        	}
        }
        
        pic.show();
        
//        System.out.println("Red");
//        for(int i=0; i<=255; i++)
//        	System.out.println(redHisto[i]);
//        
//        System.out.println("Green");
//        for(int i=0; i<=255; i++)
//        	System.out.println(greenHisto[i]);
//        
//        System.out.println("Blue");
//        for(int i=0; i<=255; i++)
//        	System.out.println(blueHisto[i]);
        
        cdfredHisto[0] = redHisto[0];
        cdfgreenHisto[0] = greenHisto[0];
        cdfblueHisto[0] = blueHisto[0];
        
        for(int i=1;i<256;i++)
        {
        	cdfredHisto[i] = cdfredHisto[i-1] + redHisto[i];
        	cdfgreenHisto[i] = cdfgreenHisto[i-1] + greenHisto[i];
        	cdfblueHisto[i] = cdfblueHisto[i-1] + blueHisto[i];
        }
        
        cdfMinRed = getMin(cdfredHisto);
        cdfMinGreen = getMin(cdfgreenHisto);
        cdfMinBlue = getMin(cdfblueHisto);
        
        for(int i=0;i < 256; i++)
        {
        	mapredHisto[i] = Math.round((cdfredHisto[i] - cdfMinRed)*255/(imageWidth*imageHeight - 1));
        	mapgreenHisto[i] = Math.round((cdfgreenHisto[i] - cdfMinGreen)*255/(imageWidth*imageHeight - 1));
        	mapblueHisto[i] = Math.round((cdfblueHisto[i] - cdfMinBlue)*255/(imageWidth*imageHeight - 1));      
        }
        
        System.out.println("Red");
        for(int i=0; i<=255; i++)
        	System.out.println(mapredHisto[i]);
        
        System.out.println("Green");
        for(int i=0; i<=255; i++)
        	System.out.println(mapgreenHisto[i]);
        
        System.out.println("Blue");
        for(int i=0; i<=255; i++)
        	System.out.println(mapblueHisto[i]);
        
        // create new image
        for(int i=0;i<imageWidth;i++)
        {
        	for(int j=0;j<imageHeight;j++)
        	{
        		testImage = pic.get(i,j);
        		
        		int red = (int) (testImage.getRed());
				int green = (int) (testImage.getGreen());
				int blue = (int) (testImage.getBlue());
				int alpha = (int) (testImage.getAlpha());
				Color newColor = new Color(mapredHisto[red],mapgreenHisto[green], mapblueHisto[blue]);
				
				pic.set(i, j, newColor);
        	}
        	
        }
        

        pic.show();
        
    }

}
