import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import javax.swing.JFrame;

public class Editor {
    private JFrame frame;
    private Engine engine;
    private Rectangle spawn, back, save;

    public Editor(JFrame fram, Engine eng){
        frame = fram;
        engine = eng;
    }

    //Pour le menu et l'éditeur on vient tout dessiner et vérifier à la main les clics
    //car l'ajout d'action listener drop la camera et rend impossible le déplacement en jeu

    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        spawn = new Rectangle(frame.getWidth() - 180, 150, 160, 55);
        back = new Rectangle(frame.getWidth() - 165, 300, 125, 55);
        save = new Rectangle(frame.getWidth() - 165, 375, 125, 55);

        Font fnt = new Font("arial", Font.BOLD, 50);
        g.setColor(Color.GRAY);
        g.fillRect(0, 0, frame.getWidth(), frame.getHeight());
        g.setFont(fnt);
        g.setColor(Color.black);
        fnt = new Font("arial", Font.BOLD, 25);
        g2d.draw(spawn);
        g.drawString("spawn", frame.getWidth() -178, 190);

        g2d.draw(back);
        g.drawString("back", frame.getWidth() -160, 345);

        g2d.draw(save);
        g.drawString("save", frame.getWidth() -160, 420);
        //en fonction de la case on vient choisir une couleur différente
        for (int i = 0; i < 20 ; i++){
            for (int j = 0; j< 20; j++){
                g.setColor(Color.GRAY);
                if (engine.getMapInfo(i, j)==1)
                    g.setColor(Color.BLUE);
                if (engine.getMapInfo(i, j)==2)
                    g.setColor(Color.MAGENTA);
                if (frame.getWidth() == 640)
                {
                    g.fillRect(i*20+40,j*20+40, 20, 20);
                    g.setColor(Color.black);
                    g.drawRect(i*20+40,j*20+40, 20, 20);
                }
                else
                {
                    g.fillRect(i*35+40,j*35+40, 35, 35);
                    g.setColor(Color.black);
                    g.drawRect(i*35+40,j*35+40, 35, 35);
                }

            }
        }
    }
}
