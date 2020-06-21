import java.awt.Color;
import javax.swing.JFrame;

public class Screen {
    // C'est dans cette classe que nous allons utiliser l'algorithme du Raycasting (lancé de rayon) On part d'un point (x,y) qui serala coordonné de notre camé dans notre map
    // et l'on va venir effectuer un lancé de rayon afin de récupérer la distance entre la caméra et le mur rencontré. On vient projeter cette distance sur un plan afin de
    // de ne pas avoir un effet "oeil de poisson" qui revient à voir comme dans une loupe avec des droites incurvés. Ensuite on vient calculer la hauteur du mur perçu par la caméra.

    // Les calculs pour obtenir la distance entre la caméra et le mur relève de la trigonométrie, on ne peux pas utiliser la Tangeante ici à case de l'angle pi/2 c'est pourquoi
    // ici je n'utiliserai que des cos et des sin. La méthode utilisé est celle du DDA en anglais (Digital Differential Analysis)

    //La méthode suivate repose sur 3 règles:
    //  1-tous les murs ont la même taille
    //  2-Nous utliseront une map 2d avec des int pour savoir s'il y a des mur ou non, avec une salle fermé
    //  3-la vue est bloqué sur le centre du regard, ne peut que pivoter ou avancer

    private JFrame frame;
    public Screen(JFrame fram){
        frame = fram;
    }
    public void update(int [][]map, int[] pixels){
        //Premièrement on divise l'écran en deux couleur, celle du plafond et celle du sol.
        for(int n=0; n<pixels.length/2; n++) {
            if(pixels[n] != Color.DARK_GRAY.getRGB()) pixels[n] = Color.DARK_GRAY.getRGB();
        }
        for(int i=pixels.length/2; i<pixels.length; i++) {
            if (pixels[i] != Color.gray.getRGB()) pixels[i] = Color.GRAY.getRGB();
        }

        boolean hit;

        //deuxièmement, pour chaque lancé de rayon, on calcul la distance

        for (int x = 0; x < frame.getWidth(); x++) {
            // Nous auront besoin de la position de la caméra, du plan, et de 4 variable nous permettant de trouver où le rayon va rentrer en collision avec un mur:
            // StepX, StepY, SideDistX, SideDistY. Lesstep sont les éléments d'incrémentation et les sides contiendront une distance initial avant que l'on ajoute les steps.
            // Dans la plupart des DDA, les distances que j'ai appelé StepX et StepY sont souvent nommés Delta

            int rayPosX = (int) Camera.Cx;
            int rayPosY = (int) Camera.Cy;

            double planeX = Camera.xPlane, planeY = Camera.yPlane;


            double CameraX = 2 * x / (double)frame.getWidth() - 1; //x-coordinate in camera space
            double rayDirX = Camera.Dirx + planeX * CameraX;
            double rayDirY = Camera.Diry + planeY * CameraX;

            double Stepx, Stepy;
            int tileStepX, tileStepY, side;

            Stepx = Math.sqrt(1 + (rayDirY*rayDirY) / (rayDirX*rayDirX));
            Stepy = Math.sqrt(1 + (rayDirX*rayDirX) / (rayDirY*rayDirY));

            double sideDistX;
            double sideDistY;

            hit = false;
            side = 0;

            if (rayDirY < 0) {
                tileStepY = -1;
                sideDistY = (Camera.Cy - rayPosY) * Stepy;
            }
            else{
                tileStepY = +1;
                sideDistY = (rayPosY - Camera.Cy + 1.0) * Stepy;
            }
            if(rayDirX < 0){
                tileStepX = -1;
                sideDistX = (Camera.Cx - rayPosX)*Stepx;
            }

            else{
                tileStepX = +1;
                sideDistX = (rayPosX - Camera.Cx + 1.0) * Stepx;
            }


            while(!hit){
                if (sideDistX < sideDistY)
                {
                    sideDistX += Stepx;
                    rayPosX += tileStepX;
                    side = 0;
                }

                else
                {
                    sideDistY += Stepy;
                    rayPosY += tileStepY;
                    side = 1;
                }
                if(map[rayPosX][rayPosY] > 0)
                    hit = true;
            }

            double dist;

            if(side==0)
                dist = Math.abs((rayPosX - Camera.Cx + (1 - tileStepX) / 2) /rayDirX);
            else
                dist = Math.abs((rayPosY - Camera.Cy + (1 - tileStepY) / 2) /rayDirY);

            //Une fois la distance perçu trouvé on vient calculer la hauteur du mur perçu

            int lineHeight;
            if(dist > 0)
                lineHeight = Math.abs((int)(frame.getHeight() / dist));
            else
                lineHeight = frame.getHeight();

            int drawStart = -lineHeight / 2 + frame.getHeight() / 2;
            if(drawStart < 0)
                drawStart = 0;
            int drawEnd = lineHeight / 2 + frame.getHeight() / 2;
            if(drawEnd >= frame.getHeight())
                drawEnd = frame.getHeight() - 1;

            for(int y=drawStart; y<drawEnd; y++) {
                if (dist < 1)
                    dist = 1;
                int color= (int)(0x000000ff/dist); // on effectue une disivion pour avoir un effet de couleur plus sombre lorsque le mur est loin.
                pixels[x + y*(frame.getWidth())] = color;
            }

        }
    }
}
