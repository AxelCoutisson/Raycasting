import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import javax.swing.JFrame;

public class Menu {
    private JFrame frame;
    private Rectangle play, edit, settings, quit, option1, option2;

    public Menu(JFrame fram){
        frame = fram;
    }


    //Pour le menu et l'éditeur on vient tout dessiner et vérifier à la main les clics
    //car l'ajout d'action listener drop la camera et rend impossible le déplacement en jeu

    public void render(Graphics g){//en fonction de l'état l'affichage est différent
        Graphics2D g2d = (Graphics2D)g;
        if(Engine.getEtat() == Engine.Etat.Menu)
        {
            play = new Rectangle(frame.getWidth()/2-50 ,150, 100, 55);
            edit = new Rectangle(frame.getWidth()/2 -50,225, 100, 50);
            settings = new Rectangle(frame.getWidth()/2 -100 ,300, 200, 55);
            quit = new Rectangle(frame.getWidth()/2 -50,375, 100, 55);

            Font fnt = new Font("arial", Font.BOLD, 50);
            g.setColor(Color.GRAY);
            g.fillRect(0, 0, frame.getWidth(), frame.getHeight());
            g.setFont(fnt);
            g.setColor(Color.black);
            g.drawString("Galewan Engine", frame.getWidth()/2-200 ,100);
            fnt = new Font("arial", Font.BOLD, 25);
            g2d.draw(play);
            g.drawString("Play", frame.getWidth()/2-51 ,195);

            g2d.draw(edit);
            g.drawString("Edit", frame.getWidth()/2 -50,270);

            g2d.draw(settings);
            g.drawString("Settings", frame.getWidth()/2 -100 ,345);

            g2d.draw(quit);
            g.drawString("Quit", frame.getWidth()/2 -50,425);
        }
        else{
            if(Engine.getEtat() == Engine.Etat.Play){
                quit = new Rectangle(frame.getWidth()/2 -60,375, 120, 50);
                option1 = new Rectangle(frame.getWidth()/2 -105,150, 205, 50);
                option2 = new Rectangle(frame.getWidth()/2 -115,225, 230, 50);
                Font fnt = new Font("arial", Font.BOLD, 50);
                g.setColor(Color.GRAY);
                g.fillRect(0, 0, frame.getWidth(), frame.getHeight());
                g.setFont(fnt);
                g.setColor(Color.black);
                g.drawString("Play", frame.getWidth()/2-50 ,100);
                fnt = new Font("arial", Font.BOLD, 25);
                g2d.draw(option1);
                g.drawString("default", frame.getWidth()/2-90 ,195);

                g2d.draw(option2);
                g.drawString("load", frame.getWidth()/2 -50,270);

                g2d.draw(quit);
                g.drawString("Back", frame.getWidth()/2 -60,420);
            }
            else{
                quit = new Rectangle(frame.getWidth()/2 -60,375, 120, 50);
                option1 = new Rectangle(frame.getWidth()/2 -105,150, 205, 50);
                option2 = new Rectangle(frame.getWidth()/2 -115,225, 230, 50);
                Font fnt = new Font("arial", Font.BOLD, 50);
                g.setColor(Color.GRAY);
                g.fillRect(0, 0, frame.getWidth(), frame.getHeight());
                g.setFont(fnt);
                g.setColor(Color.black);
                g.drawString("Settings", frame.getWidth()/2-100 ,100);
                fnt = new Font("arial", Font.BOLD, 25);
                g2d.draw(option1);
                g.drawString("640x480", frame.getWidth()/2-100 ,195);

                g2d.draw(option2);
                g.drawString("1024x768", frame.getWidth()/2 -115,270);

                g2d.draw(quit);
                g.drawString("Back", frame.getWidth()/2 -60,420);
            }

        }
    }
}
