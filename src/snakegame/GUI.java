package snakegame;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.*;

/**
 * Pelin näkymä, josta löytyy myös ohjaustavat (normaali ja automaatti)
 * @author antti
 */
public class GUI{
    Control controller;
    private Node[][] Nodewall;
    private Node[] Path;
    private Node[] Openlist;
    private Node[] Closedlist;
    private Node tmp = new Node();
    private int tempIndex;
    private JFrame frame;
    private JLabel[][] screen;
    private JPanel gamepanel;
    private int x,y;
    private String movedir;
    private final int SIZE = 34;
    private int TIMER = 30;
    private boolean mvlock;
    public boolean autopilot, PathFound;
    private int closedlenght, openlenght;
    private final Color bgColor = Color.BLACK;
    private final Color snColor = Color.CYAN;
    private final Color foodColor = Color.MAGENTA;
    
    public void registerGUI(Control controller){    //controller-luokan rekisteröinti gui:lle jonka jälkeen ajetaan konstruktori
        this.controller = controller;
        ini();
    }
    
    public void ini(){
        frame = new JFrame();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gamepanel = new JPanel(new GridLayout(SIZE,SIZE));
        gamepanel.setBackground(bgColor);
        frame.setContentPane(gamepanel);
        screen = new JLabel[SIZE][SIZE];
        inputKeys();
        this.x=0;
        this.y=0;
        while  (y<SIZE-1){
            screen[this.x][this.y] = new JLabel();
            screen[this.x][this.y].setText("■");
            gamepanel.add(screen[x][y]);
            this.x++;
            if (this.x==SIZE-1 && this.y<SIZE-1){
                this.x=0;
                this.y++;
            }
        }
        frame.setSize(getFrameSize(),getFrameSize());
        frame.setResizable(false);
        controller.resetAll();
    }
    
    public void resetGUI(){             //guin nollaus
        setScore();
        this.x=0;
        this.y=0;
        while  (y<SIZE-1){
            screen[this.x][this.y].setForeground(bgColor);
            this.x++;
            if (this.x==SIZE-1 && this.y<SIZE-1){
                this.x=0;
                this.y++;
            }
        }
    }

    public void start(){        //normaali ohjausfunktio, käytännössä lopetusehtoa ei ole. Peli nollautuu kun mato osuu itseensä
        this.x = SIZE/2;
        this.y = SIZE/2;
        movedir = "LEFT";
        controller.spawnFood();
        while (!"".equals(movedir)){
            switch (movedir){
                case "UP": moveUp();
                    break;
                case "DOWN": moveDown();
                    break;
                case "LEFT": moveLeft();
                    break;
                case "RIGHT": moveRight();
                    break;
                case "": break;
            }
    }
}
    public void stop(){
        movedir = "";
    }
    
    public void moveUp(){               //liikkumisfunktiot
        //movedir="UP";
        if (this.y != 0)
            this.y--;
        else this.y=SIZE-2;
        waitInterval(TIMER);
        updatePosition();
        controller.setCoord(x, y);
        mvlock = false;
    }
    
    public void moveDown(){
        //movedir="DOWN";
        if (this.y != SIZE-2)
            this.y++;
        else this.y=0;
        waitInterval(TIMER);
        updatePosition();
        controller.setCoord(x, y);
        mvlock = false;
    }
    
    public void moveLeft(){
        //movedir="LEFT";
        if (this.x != 0)
            this.x--;
        else this.x=SIZE-2;
        waitInterval(TIMER);
        updatePosition();
        controller.setCoord(x, y);
        mvlock = false;
    }
    
    public void moveRight(){
        //movedir="RIGHT";
        if (this.x != SIZE-2)
            this.x++;
        else this.x=0;
        waitInterval(TIMER);
        updatePosition();
        controller.setCoord(x, y);
        mvlock = false;
    }
    
    public void updatePosition(){                 //madon pään kohdalle vaihdetaan JLabelin väri ja hännän pää muutetaan taustan väriseksi
        screen[controller.getX(0)][controller.getY(0)].setForeground(bgColor);
        screen[controller.getHeadX()][controller.getHeadY()].setForeground(snColor);
    }
    
    public void blink(){                          //pisteen saadessa pieni visuaalinen "feedback"
        screen[controller.getHeadX()][controller.getHeadY()].setForeground(foodColor);
        waitInterval(TIMER);
        screen[controller.getHeadX()][controller.getHeadY()].setForeground(snColor);
    }
    
    public void EndAnimation(){                   //lopetusefekti
        for (int a=0; a<controller.getLenght()-1; a++)
            screen[controller.getX(a)][controller.getY(a)].setForeground(Color.BLACK);
        waitInterval(80);
        for (int a=0; a<controller.getLenght()-1; a++)
            screen[controller.getX(a)][controller.getY(a)].setForeground(snColor);
        waitInterval(40);
        for (int a=0; a<controller.getLenght()-1; a++)
            screen[controller.getX(a)][controller.getY(a)].setForeground(Color.DARK_GRAY);
        waitInterval(500);
    }
    
    public void waitInterval(int time){            //pelin nopeus, pienemmällä time-arvolla peli nopeutuu
        if (time > 0){
            try{
            Thread.sleep(time);
            }
            catch(InterruptedException e){
            }
        }
    }
    
    public void setFoodPos(int a, int b){
        screen[a][b].setForeground(foodColor);
    }
    
    public void removeFood(int a, int b){
        screen[a][b].setForeground(bgColor);
    }
    
    public int getGridSize(){
        return this.SIZE;
    }
    
    public void setTimer(int i){
        this.TIMER = i;
    }
    
    public int getFrameSize(){                          //skaalautuvuutta, jos pelin ruudukon kokoa suurennellaan tai pienennetään
        if (this.SIZE>29)
            return this.SIZE*11;
        else return this.SIZE*12;
    }
    public void inputKeys(){
        gamepanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "UP");
        gamepanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "LEFT");
        gamepanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "DOWN");
        gamepanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "RIGHT");
        gamepanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0), "AUTO");
        
        gamepanel.getActionMap().put("UP", new AbstractAction(){

            @Override
            public void actionPerformed(ActionEvent ae) {
                if (!movedir.equals("DOWN"))
                    if (mvlock == false){
                        movedir = "UP"; 
                        mvlock = true;
                    }
            }
        });
        gamepanel.getActionMap().put("LEFT", new AbstractAction(){

            @Override
            public void actionPerformed(ActionEvent ae) {
                if (!movedir.equals("RIGHT"))
                    if (mvlock == false){
                        movedir = "LEFT"; 
                        mvlock = true;
                }
            }
        });
        gamepanel.getActionMap().put("DOWN", new AbstractAction(){

            @Override
            public void actionPerformed(ActionEvent ae) {
                if (!movedir.equals("UP"))
                    if (mvlock == false){
                        movedir = "DOWN"; 
                        mvlock = true;
                    }
            }
        });
        gamepanel.getActionMap().put("RIGHT", new AbstractAction(){

            @Override
            public void actionPerformed(ActionEvent ae) {
                if (!movedir.equals("LEFT"))
                    if (mvlock == false){
                        movedir = "RIGHT";
                        mvlock = true;
                    }
            }
        });
        
        gamepanel.getActionMap().put("AUTO", new AbstractAction(){

            @Override
            public void actionPerformed(ActionEvent ae) {
                pilotToggle();
            }
        });
    }
    
    public void setScore(){                 //pisteen asetus ikkunan titleen
        frame.setTitle("Score: "+controller.getScore());
    }

    public void newRecord(){
        frame.setTitle("Score: "+controller.getScore()+" New Record!");
    }
    
    private void pilotToggle(){
        autopilot = autopilot == false;
    }
    
    public void initPathfind(int xDest, int yDest){ //reitinhakualgoritmin alku
        createNodeWalls();
        initLists();
        Pathfind(xDest, yDest);
    }
    
    private void createNodeWalls(){                 //muodostetaan madosta itsestään koostuvat "seinät" jotka jätetään reitinhaussa pois
        Nodewall = new Node[SIZE][SIZE];
        for (int i=1; i<controller.getLenght(); i++){
            if (controller.getX(i)==controller.getHeadX() && controller.getY(i)==controller.getHeadY())
                break;
            Nodewall[controller.getX(i)][controller.getY(i)]= new Node();
            Nodewall[controller.getX(i)][controller.getY(i)].G=i+2;
        }
    }
    
    private void initLists(){                       //suljettujen ja avointen listojen alustus
        closedlenght=-1;
        openlenght=0;
        Openlist = new Node[SIZE*SIZE];
        Closedlist = new Node[SIZE*SIZE];
        Openlist[0] = new Node();
        Openlist[0].xPos=controller.getHeadX();
        Openlist[0].yPos=controller.getHeadY();
        Openlist[0].G=0;
        Openlist[0].F = getFcost(Openlist[0].xPos,Openlist[0].yPos,0);
        Openlist[0].startnode = true;
    }
    
    private void Pathfind(int xDest, int yDest){    //itse reitinhaku algoritmi
        while(!PathFound){
            sortOpenlist();
            if (openlenght < 0 || closedlenght == Math.pow(SIZE-2,2)){      //lopetusfunktio, jos reittiä ei löydy
                    while (true){
                        int a = controller.getRand();
                        int b = controller.getRand();
                        if (searchClosedlist(a,b)== 1){
                            initPathfind(a,b);
                            break;
                        }
                    }
                break;
            }
            toClosedList(xDest,yDest);              //ensimmäinen avoimessa listassa oleva koordinaatti menee suljettuun listaan, jossa tarkastetaan onko se määränpää
            if (PathFound)
                    break;
            createNeighbors(Closedlist[closedlenght].xPos,Closedlist[closedlenght].yPos,Closedlist[closedlenght].G); //suljetun listan viimeiselle sijainnille luodaan "naapurit" joille annetaan ns. kustannusarvot
        }
        if (PathFound)
            reverseList();
        //PathFound=false;
        //System.out.println("Recursion unroll..");
    }
    
    private void sortOpenlist(){                    //avoimen listan pitäminen järjestyksessä matkakustannuksen perusteella on tärkeää, että algoritmi on nopea
        if (openlenght>0)
            for (int i=openlenght; i>0; i--){
                if (Openlist[i].F < Openlist[i-1].F){
                    tmp = Openlist[i];
                    Openlist[i] = Openlist[i-1];
                    Openlist[i-1] = tmp;
                }
            }
        //System.out.println("Open list size "+openlenght);
    }
    
    private void toClosedList(int xDest, int yDest){
        //System.out.println("Closed list size "+closedlenght);
        closedlenght++;
        Closedlist[closedlenght] = new Node();
        Closedlist[closedlenght] = Openlist[0];
        if (openlenght>0)
            for (int i=0; i<openlenght; i++){       //avoimesta listasta poistetaan ensimmäinen sijainti
                Openlist[i] = Openlist[i+1];
            }
        if (openlenght>-1)
            openlenght--;
        if (searchClosedlist(xDest,yDest)==1){
            PathFound = true;
            //System.out.println("Path Found!");
        }
    }
    
    private void createNeighbors(int xPos, int yPos, int G){
        for (int i=0; i<4; i++){            //nelisuuntainen switch-lauseke
            switch (i){
                case 0: xPos++;
                    break;
                case 1: xPos--;
                    yPos++;
                    break;
                case 2: xPos--;
                    yPos--;
                    break;
                case 3: xPos++;
                    yPos--;
                    break;
            }
            if (xPos!=SIZE-1 && xPos!=-1 && yPos!=-1 && yPos!=SIZE-1){      //jätetään huomiotta ruudukon reunan yli menevät pisteet
                tempIndex = searchOpenlist(xPos,yPos);
                if (((Nodewall[xPos][yPos]==null) || Nodewall[xPos][yPos].G<G+1) && searchClosedlist(xPos,yPos)==0){    //tarkastetaan että piste ei ole seinä ja se ei ole suljetussa listassa
                    if (tempIndex==-1){                       //piste ei ole valmiiksi avoimessa listassa
                        openlenght++;
                        Openlist[openlenght] = new Node();
                        Openlist[openlenght].xPos = xPos;
                        Openlist[openlenght].yPos = yPos;
                        Openlist[openlenght].parent = Closedlist[closedlenght];
                        Openlist[openlenght].G = G+1;
                        Openlist[openlenght].F = getFcost(xPos,yPos,G+1);
                    }
                    else if (Openlist[tempIndex].F > getFcost(xPos,yPos,G+1)){ //jos piste on jo avoimessa listassa mutta reitti tätä kautta olisi nopeampi, asetetaan nykyinen node sen parentiksi
                        Openlist[tempIndex].parent = Closedlist[closedlenght];
                        Openlist[tempIndex].G = G+1;
                        Openlist[tempIndex].F = getFcost(xPos,yPos,G+1);
                    }
                }
            }
        }
    }
    
    private int getFcost(int xPos, int yPos, int G){        //matkakustannuslaskufunktio
        return G + calculateH(xPos,yPos);
    }
    
    private int calculateH(int xPos, int yPos){             //heuristiikkalasku, jolla arvioidaan etäisyyttä kohteeseen algoritmin nopeuttamiseksi
        return Math.abs(xPos-controller.getFoodX())+Math.abs(yPos-controller.getFoodY());
    }
    
    private int searchOpenlist(int xPos, int yPos){
        for (int i=openlenght; i>0; i--){
            if (xPos==Openlist[i].xPos && yPos==Openlist[i].yPos)
                return i;
        }
        return -1;
    }
    
    private int searchClosedlist(int xPos, int yPos){
        for (int i=closedlenght; i>0; i--){
            if (xPos==Closedlist[i].xPos && yPos==Closedlist[i].yPos)
                return 1;
        }
        return 0;
    }

    private void reverseList(){                     //muodostunut linkitetty lista käännetään ympäri, jolloin muodostuu suora reitti alkupaikasta kohteeseen
        Path = new Node[SIZE*SIZE];                 //madon pään sijainti tulee viimeiseen ja ruoka ensimmäiseen indeksiin
        for (int i=0; true; i++){
            Path[i]=Closedlist[closedlenght];
            if (Closedlist[closedlenght].parent!=null)
                Closedlist[closedlenght] = Closedlist[closedlenght].parent;
            else {
                tempIndex = i;          //asetetaan reitin pituus
                break;
            }
        }
    }

    public void TraversePath(){                     //seurataan kerrallaan muodostettua reittiä ja vähennetään tempIndex-lukua, joka kertoo reitin pituuden
        if (tempIndex>0){
            if (Path[tempIndex-1].xPos<Path[tempIndex].xPos)
                movedir="LEFT";
            else if (Path[tempIndex-1].xPos>Path[tempIndex].xPos)
                movedir="RIGHT";
            else if (Path[tempIndex-1].yPos<Path[tempIndex].yPos)
                movedir="UP";
            else if (Path[tempIndex-1].yPos>Path[tempIndex].yPos)
                movedir="DOWN";
            --tempIndex;
        }
        if (tempIndex == 0)
            PathFound = false;
    }
}

    
    
    
    


