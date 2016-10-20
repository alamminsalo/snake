package snakegame;

/**
 * Ohjelman "keskus" jonka välillä tieto kulkee luokalta toiselle
 *
 * @author antti
 */
import java.io.*;

public class Game {

  private Snake snake;
  private GUI gui;
  private Pathfinder pathfinder;
  
  public void init(int areaSize) {
    snake = new Snake();
    gui = new GUI();
    pathfinder = new Pathfinder(this, gui);
    
    gui.init(areaSize, this);
  }

  public void setCoord(int x, int y) { //asetetaan uusi koordinaatti guihin ja pelin sisäiseen logiikkaan
    snake.setPos(x, y);
    checkCollision();
    checkFood(x, y);
    if (gui.isAutopilot()) {
      if (!pathfinder.isPathFound()) {
        pathfinder.initPathfind(getFoodX(), getFoodY());
      }
      if (pathfinder.isPathFound()) {
        pathfinder.traversePath();
      }
    }
  }

  private void checkCollision() {
    for (int i = 0; i < (snake.getLenght() - 3); i++) {
      if (getHeadX() == snake.getX(i) && getHeadY() == snake.getY(i)) {
        checkScore();
        gui.EndAnimation();
        resetAll();
        break;
      }
    }
  }

  private void checkFood(int x, int y) {
    if (snake.getFoodX() == x && snake.getFoodY() == y) {
      snake.growLenght(snake.getLenght());
      gui.blink();
      gui.setScore();
      spawnFood();
    }
  }

  public void resetAll() {
    snake.initializeGame(gui.getGridSize() * gui.getGridSize());
    gui.resetGUI();
    gui.start();
  }

  public void spawnFood() {
    int x = getRand();
    int y = getRand();
    if (checkFoodCollision(x, y) == 0) {
      snake.setFood(x, y);
      gui.setFoodPos(x, y);
    } else {
      spawnFood();
    }
  }

  private int checkFoodCollision(int x, int y) {
    for (int i = 1; i < (snake.getLenght()); i++) {
      if (x == snake.getX(i) && y == snake.getY(i)) {
        return 1;
      }
    }
    return 0;
  }

  public String getScore() {
    return Integer.toString(snake.getScore());
  }

  public int getHeadX() {
    return snake.getX(snake.getLenght());
  }

  public int getHeadY() {
    return snake.getY(snake.getLenght());
  }

  public int getX(int i) {
    return snake.getX(i);
  }

  public int getY(int i) {
    return snake.getY(i);
  }

  public void forcedReset() {
    gui.EndAnimation();
    resetAll();
  }

  public int getFoodX() {
    return snake.getFoodX();
  }

  public int getFoodY() {
    return snake.getFoodY();
  }

  public int getLenght() {
    return snake.getLenght();
  }

  public int getRand() {
    return (0 + (int) (Math.random() * (((gui.getGridSize() - 2) - 0) + 0)));
  }

  private void checkScore() {
    try {
      File scorefile = new File("score.txt");
      if (!scorefile.exists()) {
        writeScore();
      }
      BufferedReader reader = new BufferedReader(new FileReader("score.txt"));
      if (Integer.parseInt(reader.readLine()) < snake.getScore()) {
        writeScore();
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void writeScore() throws FileNotFoundException, UnsupportedEncodingException {
    PrintWriter writer = new PrintWriter("score.txt", "UTF-8");
    writer.println(getScore());
    writer.close();
    gui.newRecord();
  }
}
