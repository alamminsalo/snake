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
public class Main {
  
  static int areaSize = 34;
  static Game game = null;

  public static void main(String[] args) {
    game = new Game();
    game.init(areaSize);
  }
}
