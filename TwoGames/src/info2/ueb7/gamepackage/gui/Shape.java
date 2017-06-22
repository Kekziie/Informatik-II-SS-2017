package info2.ueb7.gamepackage.gui;

import java.awt.Color;

/**
 * Shape Class Definitions for the different Game Shapes. Also realizes
 * rotations.
 * 
 * @author mvbutz
 */
public class Shape {
    public enum Tetriminos {
        NoShape, SquareShape, ZShape, SShape, LineShape, TShape, LShape, MirroredLShape
    };
    // protected enum Tetriminos { NoShape, SquareShape, NikoShape};//, ZShape,
    // SShape, LineShape, TShape, LShape, MirroredLShape};

    private static int[][][] coordsTable = new int[][][] { { { 0, 0 }, { 0, 0 }, { 0, 0 }, { 0, 0 } },
        { { 0, 0 }, { 1, 0 }, { 0, 1 }, { 1, 1 } }, { { 0, -1 }, { 0, 0 }, { -1, 0 }, { -1, 1 } },
        { { 0, -1 }, { 0, 0 }, { 1, 0 }, { 1, 1 } }, { { 0, -1 }, { 0, 0 }, { 0, 1 }, { 0, 2 } },
        { { -1, 0 }, { 0, 0 }, { 1, 0 }, { 0, 1 } }, { { -1, -1 }, { 0, -1 }, { 0, 0 }, { 0, 1 } },
        { { 1, -1 }, { 0, -1 }, { 0, 0 }, { 0, 1 } } };

    // private static int[][][] coordsTable = new int[][][] {
    // { { 0, 0 }, { 0, 0 }, { 0, 0 }, { 0, 0 } },
    // { { 0, 0 }, { 1, 0 }, { 0, 1 }, { 1, 1 } },
    // { {0,-3}, {-1,-2}, {1,-2}, {-2,-1}, {2,-1},
    // {-2,0},{-1,0},{0,0},{1,0},{2,0},
    // {-2,1},{-1,1},{1,1},{2,1},
    // {-2,2},{0,2},{2,2},
    // {-2,3},{-1,3},{1,3},{2,3},
    // {-2,4},{-1,4},{0,4},{1,4},{2,4} }};
    // ,
    // { { 0, -1 }, { 0, 0 }, { -1, 0 }, { -1, 1 }},
    // { { 0, -1 }, { 0, 0 }, { 1, 0 }, { 1, 1 } },
    // { { 0, -1 }, { 0, 0 }, { 0, 1 }, { 0, 2 } },
    // { { -1, 0 }, { 0, 0 }, { 1, 0 }, { 0, 1 } },
    // { { -1, -1 }, { 0, -1 }, { 0, 0 }, { 0, 1 } },
    // { { 1, -1 }, { 0, -1 }, { 0, 0 }, { 0, 1 } }
    // };

    public static Color shapeColors[] = { new Color(0, 0, 0), new Color(250, 50, 50), new Color(102, 204, 0),
        new Color(0, 204, 204), new Color(204, 0, 204), new Color(255, 245, 0), new Color(250, 170, 0),
        new Color(102, 102, 250), new Color(0, 150, 230) };

    private Tetriminos pieceShape;

    private int relativeCoordinates[][];

    private int minX, minY, maxX, maxY;

    public Shape clone() {
        Shape ret = new Shape();
        ret.pieceShape = this.pieceShape;
        ret.relativeCoordinates = this.relativeCoordinates.clone();
        ret.minX = minX;
        ret.minY = minY;
        ret.maxX = maxX;
        ret.maxY = maxY;
        return ret;
    }

    public Shape() {
        setShape(Tetriminos.NoShape);
    }

    public void resetRandomShape() {
        setShape(Shape.Tetriminos.values()[(int) (Math.random() * (Shape.Tetriminos.values().length - 1)) + 1]);
    }

    private void setShape(Tetriminos tetShape) {
        pieceShape = tetShape;
        int[][] coords = coordsTable[pieceShape.ordinal()];
        relativeCoordinates = new int[coords.length][2];
        // put one row of the coordinate values from the
        // coordsTable to a coords array of a tetris piece
        minX = minY = 10;
        maxX = maxY = -10;
        for (int i = 0; i < coords.length; i++) {
            for (int j = 0; j < 2; ++j) {
                relativeCoordinates[i][j] = coords[i][j];
            }
            if (minX > relativeCoordinates[i][0])
                minX = relativeCoordinates[i][0];
            if (maxX < relativeCoordinates[i][0])
                maxX = relativeCoordinates[i][0];
            if (minY > relativeCoordinates[i][1])
                minY = relativeCoordinates[i][1];
            if (maxY < relativeCoordinates[i][1])
                maxY = relativeCoordinates[i][1];
        }
    }

    public int[] getLoc(int index) {
        return relativeCoordinates[index].clone();
    }

    public Tetriminos getShape() {
        return pieceShape;
    }

    public int getShapeSize() {
        return relativeCoordinates.length;
    }

    public int minX() {
        return minX;
    }

    public int minY() {
        return minY;
    }

    public int maxX() {
        return maxX;
    }

    public int maxY() {
        return maxY;
    }

    public Shape rotateLeft() {
        if (pieceShape == Tetriminos.SquareShape)
            // The square piece does not need to be rotated,
            return this;
        int[][] cc = relativeCoordinates;
        relativeCoordinates = new int[cc.length][2];
        for (int i = 0; i < relativeCoordinates.length; i++) {
            relativeCoordinates[i][0] = cc[i][1];
            relativeCoordinates[i][1] = -cc[i][0];
        }
        return this;
    }

    public Shape rotateRight() {
        if (pieceShape == Tetriminos.SquareShape)
            // The square piece does not need to be rotated,
            return this;
        int[][] cc = relativeCoordinates;
        relativeCoordinates = new int[cc.length][2];
        for (int i = 0; i < relativeCoordinates.length; i++) {
            relativeCoordinates[i][0] = -cc[i][1];
            relativeCoordinates[i][1] = cc[i][0];
        }
        return this;
    }
}