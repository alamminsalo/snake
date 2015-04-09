package snakegame;

/**
 *Madon sijaintitaulukot sekä ruuan sijainti ja pistelasku
 *
 * @author antti
 */
public class Snake {
    private int[] x, y;
    private int lenght;
    private int fX=-1, fY=-1;
    private int score;
    
    public Snake(){
    }
    
    public int getLenght(){
        return this.lenght;
    }
    
    public int getX(int i){
        return this.x[i];
    }
    
    public int getY(int i){
        return this.y[i];
    }
    
    public void setPos(int a, int b){
        this.x[lenght] = a;
        this.y[lenght] = b;
        for (int i=0; i<lenght; i++){
            this.x[i]=this.x[i+1];
            this.y[i]=this.y[i+1];
        }
    }
    
    public void setFood(int a, int b){
          this.fX = a;
          this.fY = b;
    }
    
    public void growLenght(int i){
        score+=10;
        this.lenght+=2;
        while (i<this.lenght){
        this.x[i+1]=this.x[i];
        this.y[i+1]=this.y[i];
        i++;
        }
    }
    
    public int getFoodX(){
        return this.fX;
    }
    
    public int getFoodY(){
        return this.fY;
    }
    
    public void initializeGame(int SIZE){
        this.score = 0;
        this.lenght = 6;
        this.x = new int[SIZE];
        this.y = new int[SIZE];
        fX=-1;
        fY=-1;
    }
    
    public int getScore(){
    return this.score;
    }
    
}
