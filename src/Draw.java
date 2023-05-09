/*
    Interface that most of the classes implement. Defines abstract
    functions for drawing the multiple frames and the menus in them
 */
public interface Draw {
    void drawForm(); // abstract class responsible for drawing the frame
    void drawMenu(int ID); // abstract class responsible for drawaing the menu
}
