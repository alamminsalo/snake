/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snakegame;

/**
 *
 * @author antti
 */
public class Pathfinder {

  private Node[][] Nodewall;
  private Node[] Path;
  private Node[] Openlist;
  private Node[] Closedlist;
  private int closedlenght, openlenght;
  private Node tmp = new Node();
  private int tempIndex;
  private boolean pathFound;
  
  private Game game = null;
  private GUI gui = null;
  
  public Pathfinder(Game game, GUI gui) {
    this.game = game;
    this.gui = gui;
  }
  
  public void initPathfind(int xDest, int yDest){ //reitinhakualgoritmin alku
        createNodeWalls();
        initLists();
        findPath(xDest, yDest);
    }
    
    private void createNodeWalls(){                 //muodostetaan madosta itsestään koostuvat "seinät" jotka jätetään reitinhaussa pois
        Nodewall = new Node[gui.getGridSize()][gui.getGridSize()];
        for (int i=1; i<game.getLenght(); i++){
            if (game.getX(i)==game.getHeadX() && game.getY(i)==game.getHeadY())
                break;
            Nodewall[game.getX(i)][game.getY(i)]= new Node();
            Nodewall[game.getX(i)][game.getY(i)].G=i+2;
        }
    }
    
    private void initLists(){                       //suljettujen ja avointen listojen alustus
        closedlenght=-1;
        openlenght=0;
        Openlist = new Node[gui.getGridSize()*gui.getGridSize()];
        Closedlist = new Node[gui.getGridSize()*gui.getGridSize()];
        Openlist[0] = new Node();
        Openlist[0].xPos=game.getHeadX();
        Openlist[0].yPos=game.getHeadY();
        Openlist[0].G=0;
        Openlist[0].F = getFcost(Openlist[0].xPos,Openlist[0].yPos,0);
        Openlist[0].startnode = true;
    }
    
    private void findPath(int xDest, int yDest){    //itse algoritmi
        while(!isPathFound()){
            sortOpenlist();
            if (openlenght < 0 || closedlenght == Math.pow(gui.getGridSize()-2,2)){      //lopetusfunktio, jos reittiä ei löydy
                    while (true){
                        int a = game.getRand();
                        int b = game.getRand();
                        if (searchClosedlist(a,b)== 1){
                            initPathfind(a,b);
                            break;
                        }
                    }
                break;
            }
            toClosedList(xDest,yDest);              //ensimmäinen avoimessa listassa oleva koordinaatti menee suljettuun listaan, jossa tarkastetaan onko se määränpää
            if (isPathFound())
                    break;
            createNeighbors(Closedlist[closedlenght].xPos,Closedlist[closedlenght].yPos,Closedlist[closedlenght].G); //suljetun listan viimeiselle sijainnille luodaan "naapurit" joille annetaan ns. kustannusarvot
        }
        if (isPathFound())
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
            pathFound = true;
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
            if (xPos!=gui.getGridSize()-1 && xPos!=-1 && yPos!=-1 && yPos!=gui.getGridSize()-1){      //jätetään huomiotta ruudukon reunan yli menevät pisteet
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
        return Math.abs(xPos-game.getFoodX())+Math.abs(yPos-game.getFoodY());
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
        Path = new Node[gui.getGridSize()*gui.getGridSize()];                 //madon pään sijainti tulee viimeiseen ja ruoka ensimmäiseen indeksiin
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

    public void traversePath(){                     //seurataan kerrallaan muodostettua reittiä ja vähennetään tempIndex-lukua, joka kertoo reitin pituuden
        if (tempIndex>0){
            if (Path[tempIndex-1].xPos<Path[tempIndex].xPos)
                gui.setMovedir("LEFT");
            else if (Path[tempIndex-1].xPos>Path[tempIndex].xPos)
                gui.setMovedir("RIGHT");
            else if (Path[tempIndex-1].yPos<Path[tempIndex].yPos)
                gui.setMovedir("UP");
            else if (Path[tempIndex-1].yPos>Path[tempIndex].yPos)
                gui.setMovedir("DOWN");
            --tempIndex;
        }
        if (tempIndex == 0)
            pathFound = false;
    }

  public boolean isPathFound() {
    return pathFound;
  }
}
