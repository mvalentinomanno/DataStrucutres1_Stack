/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package outlab4.stack;

/**
 *
 * @author Michael Valentino-Manno
 */
public class PixelRegion {

    private int numPix; //ints for the number of pixles checked
    private int x; //for the x coordinate
    private int y; //for the y coordinate

    PixelRegion(int yCoord, int xCoord) {
        numPix = 0;
        y = yCoord;
        x = xCoord;

    }

    public int getX() { //method returns x
        return x;
    }

    public int getY() { //method returns y
        return y;
    }

    public int getNumPix() { //method retuns the number of pixles checked
        return numPix;
    }

    public void incrament() { //method incraments the number of pixles checked
        numPix++;
    }
}
