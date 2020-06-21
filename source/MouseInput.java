import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

//Cette classe permet de récupérer les coordonnées d'un clic et de vérifier quel bouton à été actionné
//Cette classe est aussi utile pour l'édition des map pour savoir quel case à été actionné où positionner le spawn etc


public class MouseInput implements MouseListener {
    private JFrame frame;
    private static Engine engine;
    private boolean spawn;
    private static int spawnX, spawnY;
    private static JFrame f;

    public MouseInput(JFrame frame, Engine eng){
        this.frame = frame;
        engine = eng;
        spawn = false;
        spawnX = 1;
        spawnY = 1;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        int mx = e.getX();
        int my = e.getY();
        if(Engine.getEtat() == Engine.Etat.Menu){
            if(mx >= frame.getWidth()/2-50 && mx <=frame.getWidth()/2+50 && my >= 150 && my <=205)
                Engine.setEtat(Engine.Etat.Play);
            if(mx >= frame.getWidth()/2-50 && mx <=frame.getWidth()/2+50 && my >= 225 && my <=275){
                engine.prepareMap();
                Engine.setEtat(Engine.Etat.Edit);
            }
            if(mx >= frame.getWidth()/2-100 && mx <=frame.getWidth()/2+100 && my >= 300 && my <=355)
                Engine.setEtat(Engine.Etat.Settings);
            if(mx >= frame.getWidth()/2-50 && mx <=frame.getWidth()/2+50 && my >= 375 && my <=430)
                Engine.setEtat(Engine.Etat.Quit);
        }
        else{
            if(Engine.getEtat() == Engine.Etat.Settings){
                if(mx >= frame.getWidth()/2-50 && mx <=frame.getWidth()/2+50 && my >= 150 && my <=200)
                    Engine.setEtat(Engine.Etat.res640);
                if(mx >= frame.getWidth()/2-50 && mx <=frame.getWidth()/2+50 && my >= 225 && my <=275)
                    Engine.setEtat(Engine.Etat.res1024);
                if(mx >= frame.getWidth()/2-60 && mx <=frame.getWidth()/2+60 && my >= 375 && my <=425)
                    Engine.setEtat(Engine.Etat.Menu);
            }
            else{
                if(Engine.getEtat() == Engine.Etat.Play){
                    if(mx >= frame.getWidth()/2-50 && mx <=frame.getWidth()/2+50 && my >= 150 && my <=200)
                        Engine.setEtat(Engine.Etat.loadingDefault);
                    if(mx >= frame.getWidth()/2-50 && mx <=frame.getWidth()/2+50 && my >= 225 && my <=275)
                        Engine.setEtat(Engine.Etat.Loading);
                    if(mx >= frame.getWidth()/2-60 && mx <=frame.getWidth()/2+60 && my >= 375 && my <=425)
                        Engine.setEtat(Engine.Etat.Menu);
                }
                else{
                    if(Engine.getEtat() == Engine.Etat.Edit){
                        if(frame.getWidth()== 640 && mx >= 60 && mx <= 420 && my >= 60 && my <= 420){
                            int tilei = (int)((mx-40)/20);
                            int tilej = (int)((my-40)/20);
                            if (spawn) {
                                engine.swapSpawnTileMap(tilei, tilej, spawnX, spawnY);
                                spawnX = tilei;
                                spawnY = tilej;
                                spawn = false;
                            }
                            else{
                                engine.swapTileMap(tilei, tilej);
                            }
                        }
                        if(frame.getWidth()== 1024 && mx >= 75 && mx <= 705 && my >= 75 && my <= 705){
                            int tilei = (int)((mx-40)/35);
                            int tilej = (int)((my-40)/35);
                            if (spawn) {
                                engine.swapSpawnTileMap(tilei, tilej, spawnX, spawnY);
                                spawnX = tilei;
                                spawnY = tilej;
                                spawn = false;
                            }
                            else{
                                engine.swapTileMap(tilei, tilej);
                            }
                        }
                        if(mx >= frame.getWidth() - 160 && mx <=frame.getWidth() - 35 && my >= 300 && my <=355)
                            Engine.setEtat(Engine.Etat.Menu);
                        if(mx >= frame.getWidth() - 180 && mx <=frame.getWidth() - 20 && my >= 150 && my <=205)
                            spawn = true;
                        if(mx >= frame.getWidth() - 160 && mx <=frame.getWidth() - 35 && my >= 375 && my <=430) {
                            f = new JFrame("ma fenetre");
                            f.setSize(300, 100);
                            JPanel pannel = new JPanel();
                            JButton button = new JButton("save");

                            JTextArea textArea1 = new JTextArea("File Name");

                            pannel.add(textArea1);
                            pannel.add(button);
                            button.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    MouseInput.exitEdit(textArea1.getText());

                                }
                            });

                            f.getContentPane().add(pannel);
                            f.setVisible(true);
                            f.setSize(300, 100);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {


    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
    public static void exitEdit(String fileName){//on créer un fichier txt contenant les informations d'une map
        Engine.setEtat(Engine.Etat.Menu);
        f.dispose();

        try {
            FileWriter myWriter = new FileWriter(fileName+".txt");
            myWriter.write("20\n");
            myWriter.write("20\n");
            myWriter.write(Integer.toString(spawnX)+'\n');
            myWriter.write(Integer.toString(spawnY)+'\n');
            for (int i = 0; i < 20; i++){
                StringBuilder tmpLineMap = new StringBuilder();
                for (int j=0; j < 20; j++){
                    int mapVal = engine.getMapInfo(i, j);
                    if (mapVal == 2)
                        mapVal = 0;
                    tmpLineMap.append(Integer.toString((mapVal)));
                }
                myWriter.write(tmpLineMap.toString()+'\n');
            }
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
