import java.awt.Graphics;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileFilter;


public class Engine implements Runnable {
    public enum Etat { // liste des états du jeux. En les modifiants on peut passer du menu au jeu, à l'edit etc...
        Menu,
        Game,
        Play,
        Edit,
        Settings,
        res640,
        res1024,
        Loading,
        loadingDefault,
        Quit
    }
    private static Etat etat; //L'état actuel du jeu, peut être récupéré par la fonction getEtat ou modifié par setEtat.

    private JFrame frame; // La fenêtre de l'application

    private BufferStrategy bs; // se référer au constructeur
    private BufferedImage image;
    private int[] pixels;

    private Menu menu; //instance du menu pour afficher les différents choix
    private int[][] map; // contient la map qui sera lu en jeu
    private Boolean running; // Booléen utilisé pour la boucle du programme
    private Thread thread; // séparera le programme de la façon suivante 1)boucle de jeu, tick, render 2) Keylistener

    private Camera camera; // personnage / vue
    private Screen screen; //instance permettant de calculer le rendu avant de l'afficher
    private Editor editor; // instance permettant de dessiner un map



    public Engine(){
        frame = new JFrame();
        frame.setVisible(true);
        frame.setTitle("GAL - Engine Raycasting");
        frame.setSize(640, 480); //peut être modifié dans le menu
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        etat = Etat.Menu; //premièrement on lance le menu

        // Le bufferStrategy avec 2 couche permet de précalculer l'affichage avant de l'envoyer à la fenêtre
        // celui-ci contient une image composé d'un tableau de pixel que l'on viendra modifier pour avoir un affichage
        frame.createBufferStrategy(2);
        image = new BufferedImage(frame.getWidth(), frame.getHeight(), BufferedImage.TYPE_INT_RGB);
        pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();


        screen = new Screen(frame);
        long lastTime = System.nanoTime();
        camera = new Camera();
        Animations.drawSplashScreen(frame, lastTime,frame.getWidth(), frame.getHeight());
        frame.addKeyListener(camera);
        menu = new Menu(frame);
        editor = new Editor(frame,this);
        frame.addMouseListener(new MouseInput(frame, this));
        thread = new Thread(this);
        running = false;
        start();
    }
    private void start(){
        if (running)
            return; // protège au cas où on crée un autre thread
        running = true;
        thread.start();
    }

    private void stop(){
        if (!running)
            return; // protège au cas où on supprime un thread non existant
        running = false;
        try{
            thread.join();
        }
        catch (Exception e) {
            System.out.println("Somethings went wrong");
        }
    }

    public void run(){
        long lastTime = System.nanoTime();
        final double nbTicks = 30.0;  // utilisé pour cadancer le programme (tick)
        double maxfps = 40.0; //fixe le nombre de render par seconde
        double nst = 1000000000 / nbTicks;
        double nsf = 1000000000 / maxfps;
        double deltat = 0; // utilisé pour cadancer les tick
        double deltaf = 0; // utilisé pour cadancer les fps (render)

        int ticks = 0; // Nombre de mise a jour par seconde (compteur)
        int fps = 0; // nombre d'image parseconde (compteur)
        long timer = System.currentTimeMillis();
        long now;

        while(running){
            now = System.nanoTime();
            deltat += (now - lastTime) / nst; // permet de calculer quand til faut mettre à jour
            deltaf += (now - lastTime) / nsf; // permet de calculer quand il faut render
            lastTime = now;

            if (deltat > 1) // met et à jour les données 30 fois par seconde au max
            {
                tick();

                ticks++;
                deltat --;
            }
            if (deltaf > 1) // render le jeu à 40 fps au max
            {
                render();
                fps++;
                deltaf --;
            }

            if(System.currentTimeMillis() - timer > 1000) // s'exécute toute les secondes
            {
                timer += 1000;
                fps = 0;
                ticks = 0;
            }
        }
    }
    //en fonction de l'état du programme on va render différement par exemple pour le jeu, on vient demander à instance de screen de dessiner les pixel de l'image puis de les afficher
    private void render(){

        if(etat == Etat.Game) {
            screen.update(map, pixels);
            BufferStrategy bs = frame.getBufferStrategy();
            Graphics g = bs.getDrawGraphics();
            g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
            g.dispose();
            bs.show();
        }

        if(etat == Etat.Menu || etat == Etat.Settings || etat == Etat.res640 || etat == Etat.res1024 || etat == Etat.Play) {
            BufferStrategy bs = frame.getBufferStrategy();
            Graphics g = bs.getDrawGraphics();
            menu.render(g);
            g.dispose();
            bs.show();
        }

        if(etat == Etat.Edit) {
            BufferStrategy bs = frame.getBufferStrategy();
            Graphics g = bs.getDrawGraphics();
            editor.render(g);
            g.dispose();
            bs.show();
        }
        if (etat == Etat.res640) {
            frame.setSize(640, 480);
            image = new BufferedImage(frame.getWidth(), frame.getHeight(), BufferedImage.TYPE_INT_RGB);
            pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
            etat = Etat.Settings;
        }
        if (etat == Etat.res1024) {
            frame.setSize(1024, 786);
            image = new BufferedImage(frame.getWidth(), frame.getHeight(), BufferedImage.TYPE_INT_RGB);
            pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
            etat = Etat.Settings;
        }


    }

    //Pareil que pour render on vient actualiser des éléments différents en fonction de l'état du programme.
    //On peut par exemple demander a charger un map ou celle par defaut
    private void tick() {
        if (etat == Etat.Game)
            camera.update(map);
        if (etat == Etat.Quit){
            frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            stop();
        }

        if (etat == Etat.Loading) {

            JFileChooser choix = new JFileChooser();
            choix.setFileFilter(new FileFilter() {
                @Override
                public boolean accept(File f) {
                    String filename = f.getName().toLowerCase();
                    return filename.endsWith(".txt");
                }

                @Override
                public String getDescription() {
                    return "txt Images (*.txt)";
                }
            });
            int retour = choix.showOpenDialog(frame);
            if(retour == JFileChooser.APPROVE_OPTION){
                File myObj = new File(choix.getSelectedFile().getAbsolutePath());
                try {
                    Scanner sc = new Scanner(myObj);
                    int width = sc.nextInt();
                    int height = sc.nextInt();
                    Camera.Cx = sc.nextInt();
                    Camera.Cy = sc.nextInt();
                    map = new int[width][height];
                    for(int i = 0; i < width; i++){
                        String str = sc.next();
                        for (int j = 0; j < height; j++){
                            map[i][j] = Integer.parseInt(String.valueOf(str.charAt(j)));
                        }
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    etat = Etat.Menu;
                }

                etat = Etat.Game;
            }
            if(retour == JFileChooser.CANCEL_OPTION)
                etat = Etat.Menu;

        }
        if (etat == Etat.loadingDefault){
            map = new int[][]{
                    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
                    {1, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
                    {1, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
                    {1, 0, 0, 0, 0, 1, 1, 0, 0, 1 },
                    {1, 0, 0, 0, 0, 1, 0, 0, 0, 1 },
                    {1, 1, 0, 0, 0, 0, 0, 0, 0, 1 },
                    {1, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
                    {1, 0, 0, 0, 0, 0, 1, 0, 0, 1 },
                    {1, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
                    {1, 1, 1, 1, 1, 1, 1, 1, 1, 1 }
            };
            etat = Etat.Game;
        }
    }
    //set les case de la map pour qu'elle soit une salle vide
    public void prepareMap(){
        map = new int[20][20];
        for (int i=0; i<20; i++){
            for (int j=0; j<20; j++){
                if(i == 0 || i == 19 || j == 0 || j==19)
                    map[i][j] = 1;
                else
                    map[i][j] = 0;
            }
        }
        map[1][1] = 2;
    }

    //récupère la valeur d'une case
    public int getMapInfo(int x, int y){
        return map[x][y];
    }

    //modidifie la valeur d'une case
    public void swapTileMap(int x, int y) {
        if (map[x][y] == 0)
            map[x][y] = 1;
        else if (map[x][y] == 1)
            map[x][y] = 0;
    }

    //on midifie l'emplacement du spawn
    public void swapSpawnTileMap(int x, int y, int oldX, int oldY) {
        map[x][y] = 2;
        map[oldX][oldY] = 0;
    }

    // On récupère l'état du programme
    public static void setEtat(Etat e){
        etat = e;
    }

    //On modifie l'état du programme
    public static Etat getEtat(){
        return etat;
    }

    public static void main (String[] args) {
        Engine engine = new Engine();
    }

}
