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
import cls.ClassDB;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Arena extends JPanel implements ActionListener{
JFrame ex;
    private final int Lebar = 400; //menentukan ukuran layar
    private final int Tinggi = 400; 
    private final int UkuranBola = 10;
    private final int ALL_DOTS = 1000; // menentukan jumlah maksimum kemungkinan titik di layar
    private final int RAND_POS = 30; //Konstanta RAND_POS digunakan untuk menghitung posisi acak makanan
    private final int DELAY = 100;
private int scorenya,scoretinggi,highscore = 0;
    private final int x[] = new int[ALL_DOTS];
    private final int y[] = new int[ALL_DOTS];
    private int dots;
    private int makanan_x;
    private int makanan_y;
    private boolean ArahKiri = false;
    private boolean ArahKanan = true;
    private boolean ArahAtas = false;
    private boolean ArahBawah = false;
    private boolean inGame = true;
  
    private Timer timer;
    private Image ball;
    private Image makanan;
    private Image kepala;
    private int key;
   
    
    public Arena() {
        
      
        addKeyListener(new TAdapter());
        setBackground(new Color(0,0,158));
        setFocusable(true);
        setPreferredSize(new Dimension(Lebar, Tinggi));
        loadImages();
        initGame();
    }

   
    private void loadImages() {

        ImageIcon iid = new ImageIcon("src/dot.png"); //loadImages()metode ini untuk mendapatkan gambar  game
        ball = iid.getImage();

        ImageIcon iia = new ImageIcon("src/head.png");
       makanan = iia.getImage();

        ImageIcon iih = new ImageIcon("src/kanan.png");
        kepala = iih.getImage();
    }

    private void initGame() {

        dots = 6;    // membuat ular

        for (int z = 0; z < dots; z++) {
            x[z] = Lebar/20;
            y[z] = Tinggi/2;
        }

        LokasiMakanan();

        timer = new Timer(DELAY, this); // membuat timer
        timer.start();
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
    }
    private void updatescore(){
         try {            
            Connection c=ClassDB.getkoneksi();
           Statement s=(Statement)c.createStatement();
        String cektinggi="Select * from score  = '" ;
            ResultSet r=s.executeQuery(cektinggi);
            if (r.next()){
             scoretinggi = Integer.parseInt(r.getString("score"));
             if (scorenya <= scoretinggi){
                  return;  
             }
             else{
                  String sqel = "UPDATE score Set score ='" + scorenya ;     
            s.executeUpdate(sqel); 
             }
                      
            }            
        }catch(Exception e) {
            System.out.println(e);
        }
    }
    private void doDrawing(Graphics g) {
        
        if (inGame) {

            g.drawImage(makanan, makanan_x, makanan_y, this);

            for (int z = 0; z < dots; z++) {
                if (z == 0) {
                    g.drawImage(kepala, x[z], y[z], this);
                } else {
                    g.drawImage(ball, x[z], y[z], this);
                }
            }

            Toolkit.getDefaultToolkit().sync();
             String msg = "Score = "+scorenya +" Tahu Tek";
             
        Font small = new Font("Helvetica", Font.BOLD, 10);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, 5, Tinggi - (Tinggi-10));
        
        try {            
            
            Connection c=ClassDB.getkoneksi();
            Statement s= c.createStatement();
            String sql="Select * from score where score = (select max(score) from score)";
            ResultSet r=s.executeQuery(sql);
            if (r.next()){
                highscore = Integer.parseInt(r.getString("score"));
                String scr = "Score Tertinggi "+ highscore;
     
               g.drawString(scr, (Lebar - metr.stringWidth(scr)) -10, Tinggi -5);
            }
            else{
                String scr = "Score Tertinggi = 0";
     
               g.drawString(scr, (Lebar - metr.stringWidth(scr)) -10, Tinggi -5);
            }
            
            r.close();
            s.close();
           
        }catch(Exception e) {
            System.out.println(e);
        }

        } else {
            gameOver(g);         
        }        
    }
    private void gameOver(Graphics g) {
        updatescore();
        if (scorenya <= highscore){
            String msg = "Score Anda = "+ scorenya + " Tahu Tek";
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, (Lebar - metr.stringWidth(msg)) / 2, Tinggi / 2);
       
        }
        else{
            String msgg = "Congratulations! Your High Score is "+ scorenya + " Tahu Tek";
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.YELLOW);
        g.setFont(small);
        g.drawString(msgg, (Lebar - metr.stringWidth(msgg)) / 2, Tinggi / 2);
       
        }
       
           
    }
    
       
    private void CekMakanan() {  // Jika makanan bertabrakan dengan kepala ularnya, jumlah badan ularnya 
                                 // bertambah dan pointnya juga bertambah

        if ((x[0] == makanan_x) && (y[0] == makanan_y)) {

            dots++; 
            scorenya = scorenya + 1;
                          
    
            LokasiMakanan();
        }
    }

    private void pindah() {

        for (int z = dots; z > 0; z--) {
            x[z] = x[(z - 1)];
            y[z] = y[(z - 1)];
        }

        if (ArahKiri) {
            x[0] -= UkuranBola;
        }

        if (ArahKanan) {
            x[0] += UkuranBola;
        }

        if (ArahAtas) {
            y[0] -= UkuranBola;
        }

        if (ArahBawah) {
            y[0] += UkuranBola;
        }
    }

    private void CekTabrakan() {

        for (int z = dots; z > 0; z--) {

            if ((z > 5) && (x[0] == x[z]) && (y[0] == y[z])) {
                inGame = false;
                             
    
            }
        }

        if (y[0] >= Tinggi) {
            inGame = false;
            
        }

        if (y[0] < 0) {
            inGame = false;
          
        }

        if (x[0] >= Lebar) {
            inGame = false;
            
        }

        if (x[0] < 0) {
            inGame = false;
            
        }
        
        if(!inGame) {
            timer.stop();
        }
    }

    private void LokasiMakanan() {

        int r = (int) (Math.random() * RAND_POS);
        makanan_x = ((r * UkuranBola));

        r = (int) (Math.random() * RAND_POS);
        makanan_y = ((r * UkuranBola));
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (inGame) {

            CekMakanan();
            CekTabrakan();
            pindah();
        }

        repaint();
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();

            if ((key == KeyEvent.VK_LEFT) && (!ArahKanan)) {
                ArahKiri = true;
                ArahAtas = false;
                ArahBawah = false;
                 ImageIcon kiri = new ImageIcon("src/kiri.png");
        kepala = kiri.getImage();
            }

            if ((key == KeyEvent.VK_RIGHT) && (!ArahKiri)) {
                ArahKanan = true;
                ArahAtas = false;
                ArahBawah = false;
                 ImageIcon kanan = new ImageIcon("src/kanan.png");
        kepala = kanan.getImage();
            }

            if ((key == KeyEvent.VK_UP) && (!ArahBawah)) {
                ArahAtas = true;
                ArahKanan = false;
                ArahKiri = false;
               ImageIcon atas = new ImageIcon("src/atas.png");
        kepala = atas.getImage();
            }

            if ((key == KeyEvent.VK_DOWN) && (!ArahAtas)) {
                ArahBawah = true;
                ArahKanan = false;
                ArahKiri = false;
                 ImageIcon bawah = new ImageIcon("src/bawah.png");
        kepala = bawah.getImage();
            }
            if ((key == KeyEvent.VK_P) ) {
               
               if(timer.isRunning()){
                   timer.stop();                 
               }  
               else{
                   timer.start();     
               }    
            }    
        }
    }
}
