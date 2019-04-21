import processing.core.*;

/**
 * Animation is a subclass of Processing -It will initialize the -Shapes and
 * text are drawn on the screen. -Mouse and keyboard input are read in.
 */
public class Animation extends PApplet {
    private static final int HEIGHT = 600;
    private static final int WIDTH = 600;

    private int white;
    private int black;

    // TODO 1: Decrease the number of points, then increase the number
    private int AmoebaPoints = 30;
    private int AmoebaHeight = 100;
    private int AmoebaWidth = 100;
    private int AmoebaPositionX = 0;
    private int AmoebaPositionY = 0;
    private float[] rValues;
    
    private PImage spritesheet;
    private int spritesheetRows = 4;
    private int spritesheetColumns = 2;
    private int animationFrame = 0;

    /**
     * Launch the Processing Application, Calls settings() once, then setup()
     * once, then draw() 30 times per second.
     */
    public static void main(String args[]) {
        String packageFilename = "Animation";
        PApplet.main(new String[] { packageFilename });
    }

    /**
     * Sets the Application Properties.
     */
    public void settings() {
        size(WIDTH, HEIGHT); // Set size of screen
    }

    /**
     * Called once at the start
     */
    public void setup() {
        // TODO 4 (stretch): Add cat-sprite.png
        spritesheet = loadImage("runningcat.png");
        white = color(255, 255, 255); // (r, g, b) [0, 256)
        black = color(0, 0, 0);

        rValues = new float[AmoebaPoints];
        for (int i = 0; i < rValues.length; i++) {
            rValues[i] = 1.0f;
        }
    }

    /**
     * Called repeatedly (once per frame)
     */
    public void draw() {
        background(white); // Fill background color with white

        // Give the amoebas a greenish color that fluctuates a bit
        int color = color(100, 200 + (frameCount % 23) + (int) (Math.random() * 5.0), 100);

        fill(color, 200f); // 200f is opacity - a bit translucent
        noStroke(); // No border around outside of object

        //
        // Draw the shape of the amoeba. It consists of a closed curve,
        // defined by a set of points. Each point is computed from
        // the rValues array of floats which represents fractional "r"
        // values in polar coordinates.
        // NOTE: curveVertex-based curves are "splines", and they are set up so
        // that the first and last point are are not drawn, but serve as guides for
        // the beginning and ending slopes, respectively - lookup "processing curveVertex" for
        // details. The way I have set this up creates a closed loop of points,
        // which is what we need for drawing the amoeba shape.
        //
        beginShape();
        // place the very first point
        curveVertex(getVertexX(AmoebaPoints - 1), getVertexY(AmoebaPoints - 1));
        // add the remaining points
        for (int i = 0; i < AmoebaPoints; i++) {
            curveVertex(getVertexX(i), getVertexY(i));
        }
        // loop back to the very 1st point drawn in order to close the loop
        curveVertex(getVertexX(0), getVertexY(0));

        // this final point is a guide, it is not drawn
        curveVertex(getVertexX(1), getVertexY(1));
        endShape();

        // update the rValues randomly, between about 0.8 and 1.2
        for (int i = 0; i < AmoebaPoints; i++) {
            float newValue = (float) (rValues[i] * (1 + (Math.random() - 0.5) * 0.1));
            if (newValue > 0.8 && newValue < 1.2) {
                rValues[i] = newValue;
            }
        }
        
        // TODO 3: Uncomment this block
        /*
        int width = spritesheet.width / spritesheetColumns;
        int height = spritesheet.height / spritesheetRows;
        // TODO 4: Instead of increasing the animationFrame with the mouse,
        //         use frameCount to increase the animationFrame. (modify speed so that it looks good)
        int frame = animationFrame % (spritesheetColumns * spritesheetRows);
        int x = frame % spritesheetColumns * width;
        int y = frame / spritesheetColumns % spritesheetRows * height;
        PImage sprite = spritesheet.get(x, y, width, height);
        image(sprite,50,50);
        */
        
        
        // Write mouse position
        fill(black);
        textSize(16);
        textAlign(RIGHT, BOTTOM);
        String amoebaPos = "x: " + AmoebaPositionX + " y:" + AmoebaPositionY;
        text(amoebaPos, 600, 600);
    }

    /**
     * Compute the x coordinate of the point corresponding to rValues[i]
     * defined as cos(rValues[i]) from the center of the shape, scaled
     * appropriately
     * @param i - index of the rValues array
     * @return x coordinate of point corresponding to rValues[i]
     */
    private int getVertexX(int i) {
        AmoebaPositionX = mouseX; // left end of bounding rectangle
        int width = AmoebaWidth;
        return AmoebaPositionX + width / 2 + (int) (width / 2.0 * rValues[i] * Math.cos((2 * Math.PI * i) / AmoebaPoints));
    }

    /**
     * Compute the y coordinate of the point corresponding to rValues[i]
     * defined as cos(rValues[i]) from the center of the shape, scaled
     * appropriately
     * @param i - index of the rValues array
     * @return y coordinate of point corresponding to rValues[i]
     */
    private int getVertexY(int i) {
        // TODO 2: Instead of mouseY, move the amoeba along a sin wave. Pseudocode:
        //    y = frameCount % HEIGHT
        //    norm = y / HEIGHT
        //    rad = norm * PI * 2
        //    siny = sin(rad)
        //    posy = siny * 200 + HEIGHT/2
        AmoebaPositionY = mouseY; // top end of bounding rectangle

        int height = AmoebaHeight;
        return AmoebaPositionY + height / 2 + (int) (height / 2.0 * rValues[i] * Math.sin((2 * Math.PI * i) / AmoebaPoints));
    }
    
    /**
     * Handle Mouse press (Down)
     */
    public void mousePressed() {
        animationFrame++;
    }
}
