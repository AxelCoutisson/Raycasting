import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Camera implements KeyListener {

    //dans cette classe on vient modifier les variables nécéssaires dans Screen.java en fonction des touches qui ont été actionné.
    public static double Cx, Cy, Dirx, Diry, xPlane, yPlane, rotSpeed;
    private boolean up, right, down, left;
    private Camera instance = null;
    public Camera() {
        Cx = 2;
        Cy = 2;
        Dirx = 1;
        Diry = 0;
        xPlane = 0;
        yPlane = -0.66;
        rotSpeed = 0.1;
        up = false;
        down = false;
        left = false;
        right = false;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_Z) {
            up = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_Q) {
            left = true;

        }
        if (e.getKeyCode() == KeyEvent.VK_S) {
                down = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_D) {
            right = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            Engine.setEtat(Engine.Etat.Menu);
            Cx = 2;
            Cy = 2;
            Dirx = 1;
            Diry = 0;
            xPlane = 0;
            yPlane = -0.66;
            rotSpeed = 0.1;
            up = false;
            down = false;
            left = false;
            right = false;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_Z) {
            up = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_Q) {
            left = false;

        }
        if (e.getKeyCode() == KeyEvent.VK_S) {
            down = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_D) {
            right = false;
        }
    }
    public void update(int[][] m){
        int[][] map = m;
        if (up) {
            double Cpx = Cx + Dirx/3;
            double Cpy = Cy + Diry/3;
            if (map[(int)Cpx][(int)Cy] == 0)
                Cx = Cpx;
            if (map[(int)Cx][(int)Cpy] == 0)
                Cy = Cpy;
        }
        if (left) {
            double oldDirx = Dirx;
            double oldxPlane = xPlane;
            Dirx = Dirx*Math.cos(rotSpeed)-Diry*Math.sin(rotSpeed);
            Diry = oldDirx*Math.sin(rotSpeed)+Diry*Math.cos(rotSpeed);

            xPlane=xPlane*Math.cos(rotSpeed) - yPlane*Math.sin(rotSpeed);
            yPlane=oldxPlane*Math.sin(rotSpeed) + yPlane*Math.cos(rotSpeed);

        }
        if (down) {
            double Cpx = Cx - Dirx/3;
            double Cpy = Cy - Diry/3;
            if (map[(int)Cpx][(int)Cy] == 0)
                Cx = Cpx;
            if (map[(int)Cx][(int)Cpy] == 0)
                Cy = Cpy;
        }
        if (right) {
            double oldDirx = Dirx;
            double oldxPlane = xPlane;
            Dirx = Dirx*Math.cos(-rotSpeed)-Diry*Math.sin(-rotSpeed);
            Diry = oldDirx*Math.sin(-rotSpeed)+Diry*Math.cos(-rotSpeed);

            xPlane=xPlane*Math.cos(-rotSpeed) - yPlane*Math.sin(-rotSpeed);
            yPlane=oldxPlane*Math.sin(-rotSpeed) + yPlane*Math.cos(-rotSpeed);

        }
    }

}
