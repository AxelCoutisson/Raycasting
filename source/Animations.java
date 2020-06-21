import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import javax.swing.JFrame;

public class Animations {
    //Cette classe ne sert uniquement à afficher le SplashScreen du début
    public static void drawSplashScreen(JFrame frame, long lastTime, int frameWidth, int frameHeight){
        // Afin d'avoir une animation cadencé pour voir des éléments se dessiner, on va venir reprendre la même méthode de tick que dans Engine.java
        long now;
        double delta = 0;
        double ns = 1000000000 / 60.0;

        int ttf = 0; // compteur de frame
        boolean finish = false;
        String title = "alewan Engine";
        String appendTitle = "";
        // les StartPos permettent de centrer l'affichage
        int xStartPos = (frameWidth / 2) - 200;
        int yStartPos = (frameHeight / 2) - 75;

        // Les iterateurs suivants correspondent aux différents traits qui vont se dessiner progressivement
        int ia = 0, ib = 0, ic = 0, id = 0, ie = 0, ig = 0, ih = 0, ik = 0;
        int itext = 0; // permettra d'itérer dans l'affichage du texte pour avoir un effet d'écriture

        while(!finish){
            now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;

            if (delta > 3){
                if (ttf == 109){
                    finish = true;
                }
                BufferStrategy bs = frame.getBufferStrategy();
                Graphics g = bs.getDrawGraphics();
                g.setColor(Color.black);
                g.fillRect(0, 0,frame.getWidth(), frame.getHeight());
                g.setColor(Color.WHITE);

                //Tout les drawarc et draw line vont permettrent de dessiner la lettre G
                g.drawArc( xStartPos, yStartPos , 150, 150, 90, 2+ia);
                if (ttf < 45){
                    ia+=2;
                }

                if (ttf > 10){
                    g.drawArc( xStartPos +10, yStartPos +10, 130, 130, -90, -2+ib);
                    if(ttf < 55)
                        ib-=2;
                }
                if (ttf > 15){
                    g.drawArc( xStartPos, yStartPos, 150, 150, 270, 2+ic);
                    if(ttf < 60)
                        ic+=2;
                }
                if(ttf >20)
                {
                    g.drawArc( xStartPos, yStartPos, 150, 150, 90, -2+id);
                    if(ttf < 55)
                        id-=2;
                }
                if(ttf >22)
                {
                    g.drawArc( xStartPos +10, yStartPos +10, 130, 130, 20, 2+ie);
                    if(ttf < 60)
                        ie+=2;
                }
                if(ttf >25)
                {
                    g.drawArc( xStartPos +10, yStartPos +10, 130, 130, 90, 2+ig);
                    if(ttf < 72)
                        ig+=2;
                }
                if(ttf >27)
                {
                    g.drawArc( xStartPos, yStartPos, 150, 150, 270, -2+ih);
                    if(ttf < 73)
                        ih+=-2;
                }
                if(ttf >27)
                {
                    g.drawArc( xStartPos +10, yStartPos +10, 130, 130, 270, 2+ik);
                    if(ttf < 68)
                        ik+=2;
                }
                if (ttf>30)
                {
                    g.drawLine(xStartPos +75, yStartPos + 75, xStartPos + 77 + (Math.min(ttf, 66)-30)*2, yStartPos + 75);
                }
                if (ttf >55) {
                    g.drawLine(xStartPos +136,yStartPos + 54,xStartPos +145,yStartPos + 49);
                }
                if(ttf >=10) {
                    g.drawLine(xStartPos +75, yStartPos + 85, xStartPos +76 + Math.min(ttf, 73) - 10, yStartPos + 85);
                }
                if (ttf >75)
                {
                    g.drawLine(xStartPos +75,yStartPos + 85,xStartPos +75,yStartPos + 75);
                    if(itext < 13) {
                        //On vient ajouter une lettre après l'autre
                        appendTitle += title.charAt(itext);
                        itext++;
                    }
                    g.setFont(new Font("Calibri", Font.PLAIN, 40));
                    g.drawString(appendTitle, xStartPos + 170, yStartPos + 130);
                    if (itext == 13) {
                        bs.show();
                    }
                }
                if (ttf == 100)
                    finish = true;
                bs.show();
                ttf++;
                delta = 0;
            }
        }
    }
}
