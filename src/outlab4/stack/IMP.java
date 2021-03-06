/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package outlab4.stack;

/*
 *Hunter Lloyd
 * Copyrite.......I wrote, ask permission if you want to use it outside of class. 
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.awt.image.PixelGrabber;
import java.awt.image.MemoryImageSource;
import java.util.Stack;

class IMP implements MouseListener {

    JFrame frame;
    JPanel mp;
    JButton start;
    JScrollPane scroll;
    JMenuItem openItem, exitItem, resetItem;
    Toolkit toolkit;
    File pic;
    ImageIcon img;
    int colorX, colorY;
    int[] pixels;
    int[] results;
    //Instance Fields you will be using below

    //This will be your height and width of your 2d array
    int height = 0, width = 0;

    //your 2D array of pixels
    int picture[][];

    /* 
     * In the Constructor I set up the GUI, the frame the menus. The open pulldown 
     * menu is how you will open an image to manipulate. 
     */
    IMP() {
        toolkit = Toolkit.getDefaultToolkit();
        frame = new JFrame("Image Processing Software by Hunter");
        JMenuBar bar = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenu functions = getFunctions();
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent ev) {
                quit();
            }
        });
        openItem = new JMenuItem("Open");
        openItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                handleOpen();
            }
        });
        resetItem = new JMenuItem("Reset");
        resetItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                reset();
            }
        });
        exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                quit();
            }
        });
        file.add(openItem);
        file.add(resetItem);
        file.add(exitItem);
        bar.add(file);
        bar.add(functions);
        frame.setSize(600, 600);
        mp = new JPanel();
        mp.setBackground(new Color(0, 0, 0));
        scroll = new JScrollPane(mp);
        frame.getContentPane().add(scroll, BorderLayout.CENTER);
        JPanel butPanel = new JPanel();
        butPanel.setBackground(Color.black);
        start = new JButton("start");
        start.setEnabled(false);
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                fun2();
            }
        });
        butPanel.add(start);
        frame.getContentPane().add(butPanel, BorderLayout.SOUTH);
        frame.setJMenuBar(bar);
        frame.setVisible(true);
    }

    /* 
    * This method creates the pulldown menu and sets up listeners to selection of the menu choices. If the listeners are activated they call the methods 
    * for handling the choice, fun1, fun2, fun3, fun4, etc. etc. 
     */
    private JMenu getFunctions() {
        JMenu fun = new JMenu("Functions");
        JMenuItem firstItem = new JMenuItem("MyExample - fun1 method");

        firstItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                fun1();
            }
        });

        //fun.add(firstItem);
        fun.add(firstItem);

        return fun;

    }

    /*
   * This method handles opening an image file, breaking down the picture to a one-dimensional array and then drawing the image on the frame. 
   * You don't need to worry about this method. 
     */
    private void handleOpen() {
        img = new ImageIcon();
        JFileChooser chooser = new JFileChooser();
        int option = chooser.showOpenDialog(frame);
        if (option == JFileChooser.APPROVE_OPTION) {
            pic = chooser.getSelectedFile();
            img = new ImageIcon(pic.getPath());
        }
        width = img.getIconWidth();
        height = img.getIconHeight();

        JLabel label = new JLabel(img);
        label.addMouseListener(this);
        pixels = new int[width * height];

        results = new int[width * height];

        Image image = img.getImage();

        PixelGrabber pg = new PixelGrabber(image, 0, 0, width, height, pixels, 0, width);
        try {
            pg.grabPixels();
        } catch (InterruptedException e) {
            System.err.println("Interrupted waiting for pixels");
            return;
        }
        for (int i = 0; i < width * height; i++) {
            results[i] = pixels[i];
        }
        turnTwoDimensional();
        mp.removeAll();
        mp.add(label);

        mp.revalidate();
    }

    /*
   * The libraries in Java give a one dimensional array of RGB values for an image, I thought a 2-Dimensional array would be more usefull to you
   * So this method changes the one dimensional array to a two-dimensional. 
     */
    private void turnTwoDimensional() {
        picture = new int[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                picture[i][j] = pixels[i * width + j];
            }
        }

    }

    /*
   *  This method takes the picture back to the original picture
     */
    private void reset() {
        for (int i = 0; i < width * height; i++) {
            pixels[i] = results[i];
        }
        Image img2 = toolkit.createImage(new MemoryImageSource(width, height, pixels, 0, width));

        JLabel label2 = new JLabel(new ImageIcon(img2));
        mp.removeAll();
        mp.add(label2);

        mp.revalidate();
    }

    /*
   * This method is called to redraw the screen with the new image. 
     */
    private void resetPicture() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                pixels[i * width + j] = picture[i][j];
            }
        }
        Image img2 = toolkit.createImage(new MemoryImageSource(width, height, pixels, 0, width));

        JLabel label2 = new JLabel(new ImageIcon(img2));
        mp.removeAll();
        mp.add(label2);

        mp.revalidate();

    }

    /*
     * This method takes a single integer value and breaks it down doing bit manipulation to 4 individual int values for A, R, G, and B values
     */
    private int[] getPixelArray(int pixel) {
        int temp[] = new int[4];
        temp[0] = (pixel >> 24) & 0xff;
        temp[1] = (pixel >> 16) & 0xff;
        temp[2] = (pixel >> 8) & 0xff;
        temp[3] = (pixel) & 0xff;
        return temp;

    }

    /*
     * This method takes an array of size 4 and combines the first 8 bits of each to create one integer. 
     */
    private int getPixels(int rgb[]) {
        int alpha = 0;
        int rgba = (rgb[0] << 24) | (rgb[1] << 16) | (rgb[2] << 8) | rgb[3];
        return rgba;
    }

    public void getValue() {
        int pix = picture[colorY][colorX];
        int temp[] = getPixelArray(pix);
        System.out.println("Color value " + temp[0] + " " + temp[1] + " " + temp[2] + " " + temp[3]);
    }

    /**
     * ************************************************************************************************
     * This is where you will put your methods. Every method below is called
     * when the corresponding pulldown menu is used. As long as you have a
     * picture open first the when your fun1, fun2, fun....etc method is called
     * you will have a 2D array called picture that is holding each pixel from
     * your picture.
     * ***********************************************************************************************
     */
    /*
    * Example function that just removes all red values from the picture. 
    * Each pixel value in picture[i][j] holds an integer value. You need to send that pixel to getPixelArray the method which will return a 4 element array 
    * that holds A,R,G,B values. Ignore [0], that's the Alpha channel which is transparency, we won't be using that, but you can on your own.
    * getPixelArray will breaks down your single int to 4 ints so you can manipulate the values for each level of R, G, B. 
    * After you make changes and do your calculations to your pixel values the getPixels method will put the 4 values in your ARGB array back into a single
    * integer value so you can give it back to the program and display the new picture. 
     */
    private void fun1() {

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int rgbArray[] = new int[4];

                //get three ints for R, G and B
                rgbArray = getPixelArray(picture[i][j]);

                rgbArray[1] = 0;
                //take three ints for R, G, B and put them back into a single int
                picture[i][j] = getPixels(rgbArray);
            }
        }
        resetPicture();
    }

    /*
   * fun2
   * This is where you will write your STACK
   * All the pixels are in picture[][]
   * Look at above fun1() to see how to get the RGB out of the int (getPixelArray)
   * and then put the RGB back to an int (getPixels)
     */
    private void fun2() {
        PixelRegion first = new PixelRegion(colorY, colorX); //Chosen pixel
        Stack<PixelRegion> stack = new Stack<PixelRegion>(); //Create stack using java api, that holds PixelRegion instances
        int num = picture[colorY][colorX]; //Gets num so we can call getPixel Array
        int thresh = 10; //Threshold of 10, so if a rgb val is within 10, it will change the color
        int color[] = getPixelArray(num); //Array of agb values to compare each pixel to original
        stack.push(first); //push first pixel onto the stack
        int white[] = new int[4]; //new array
        for (int i = 0; i < white.length; i++) { //for fills the array with values meaning white
            white[i] = 255;
        }

        while (stack.isEmpty() != true) { //while executes until stack is empty
            int x = stack.peek().getX();  //get x and y value of pixel on top of stack
            int y = stack.peek().getY();
            int pixNum = stack.peek().getNumPix(); //pixNum is the amount of pixels around the current one, that have been checked already

            if (x < 2 || y < 2 || x > (width - 2) || y > (height - 2)) { //creates a boarder to about out of bounds when checking a color on the edge
               // pixNum = -1; //setting pixNum to -1 will call pop in the default of the switch
            }

            stack.peek().incrament(); //incrament the num of pixels checked
            int rgb[] = new int[4];

            //Each case of the switch except the default does the same thing, just with a different pixel
            switch (pixNum) { //switch that takes in the amount of pixels checked
                case 0:
                    rgb = getPixelArray(picture[y - 1][x]); //for each case, it sets an array to the rgb values of the current pixel
                    if (Math.abs(color[1] - rgb[1]) < thresh && Math.abs(color[2] - rgb[2]) < thresh && Math.abs(color[3] - rgb[3]) < thresh) {
                        picture[y - 1][x] = getPixels(white); //if compares the colors in each case, if they are all within 10, change it to white
                        stack.push(new PixelRegion(y - 1, x)); //push new pixel onto array in each case
                    }

                    break;
                case 1:
                    rgb = getPixelArray(picture[y - 1][x - 1]); //check top left pixel, if within threshold, push
                    if (Math.abs(color[1] - rgb[1]) < thresh && Math.abs(color[2] - rgb[2]) < thresh && Math.abs(color[3] - rgb[3]) < thresh) {
                        picture[y - 1][x - 1] = getPixels(white);
                        stack.push(new PixelRegion(y - 1, x - 1));
                    }

                    break;
                case 2:
                    rgb = getPixelArray(picture[y - 1][x + 1]); //check next pixel, if within threshold, push
                    if (Math.abs(color[1] - rgb[1]) < thresh && Math.abs(color[2] - rgb[2]) < thresh && Math.abs(color[3] - rgb[3]) < thresh) {
                        picture[y - 1][x + 1] = getPixels(white);
                        stack.push(new PixelRegion(y - 1, x + 1));
                    }

                    break;
                case 3:
                    rgb = getPixelArray(picture[y][x + 1]); //check next pixel, if in threshold, push
                    if (Math.abs(color[1] - rgb[1]) < thresh && Math.abs(color[2] - rgb[2]) < thresh && Math.abs(color[3] - rgb[3]) < thresh) {
                        picture[y][x + 1] = getPixels(white);
                        stack.push(new PixelRegion(y, x + 1));
                    }
                    break;
                case 4:
                    rgb = getPixelArray(picture[y + 1][x + 1]); //check next pixel, if in threshold, push
                    if (Math.abs(color[1] - rgb[1]) < thresh && Math.abs(color[2] - rgb[2]) < thresh && Math.abs(color[3] - rgb[3]) < thresh) {
                        picture[y + 1][x + 1] = getPixels(white);
                        stack.push(new PixelRegion(y + 1, x + 1));
                    }
                    break;
                case 5:
                    rgb = getPixelArray(picture[y + 1][x]); //check next pixel, if in threshold, push
                    if (Math.abs(color[1] - rgb[1]) < thresh && Math.abs(color[2] - rgb[2]) < thresh && Math.abs(color[3] - rgb[3]) < thresh) {
                        picture[y + 1][x] = getPixels(white);
                        stack.push(new PixelRegion(y + 1, x));
                    }

                    break;
                case 6:
                    rgb = getPixelArray(picture[y + 1][x - 1]); //check next pixel, if in threshold, push
                    if (Math.abs(color[1] - rgb[1]) < thresh && Math.abs(color[2] - rgb[2]) < thresh && Math.abs(color[3] - rgb[3]) < thresh) {
                        picture[y + 1][x - 1] = getPixels(white);
                        stack.push(new PixelRegion(y + 1, x - 1));
                    }

                    break;
                case 7:
                    rgb = getPixelArray(picture[y][x - 1]);
                    if (Math.abs(color[1] - rgb[1]) < thresh && Math.abs(color[2] - rgb[2]) < thresh && Math.abs(color[3] - rgb[3]) < thresh) {
                        picture[y][x - 1] = getPixels(white);
                        stack.push(new PixelRegion(y, x - 1));
                    }

                    break;
                default:
                    stack.pop(); //all surrounding pixels have been checked, so call pop
                    break;
            }
        }
        resetPicture(); //resetPicture redraws picture with the changes made
        System.out.println("Function 2 complete!"); //Classy completion message!
    }

    private void quit() {
        System.exit(0);
    }

    @Override
    public void mouseEntered(MouseEvent m) {
    }

    @Override
    public void mouseExited(MouseEvent m) {
    }

    @Override
    public void mouseClicked(MouseEvent m) {
        colorX = m.getX();
        colorY = m.getY();
        System.out.println(colorX + "  " + colorY);
        getValue();
        start.setEnabled(true);
    }

    @Override
    public void mousePressed(MouseEvent m) {
    }

    @Override
    public void mouseReleased(MouseEvent m) {
    }

    public static void main(String[] args) {
        IMP imp = new IMP();
    }
}
