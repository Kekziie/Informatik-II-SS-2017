package info2.ueb7.gamepackage;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import info2.ueb7.gamepackage.gui.MainGameFrame;

/**
 * The main class to start this game.
 *
 * @author Martin V. Butz, Sebastian Otte
 */
public class Main {

    /**
     * The main class to start this game.
     * 
     * @param args
     *            Arguments are not considered here.
     * @throws UnsupportedLookAndFeelException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     */
    public static void main(String[] args)
        throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {

        // Set to system look and feel.
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        // simply creates a class object that exists as long as the JFrame of
        // the class is visible.
        new MainGameFrame();
    }
}