/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package snake;

/**
 *
 * @author KomangRyan
 */
import javax.swing.ImageIcon;
import javax.swing.JFrame;
public class Ular extends JFrame{
    public Ular() {

        add(new Arena());    
        setResizable(false);
        pack();
        setTitle("Snake Game Vol 1: Tahu Tek Hunter");
        ImageIcon icon = new ImageIcon("src/snake.png");
        setIconImage(icon.getImage());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
