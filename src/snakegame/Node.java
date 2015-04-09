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
public class Node {
    public Node parent;
    public int F;
    public int G=999;
    public int xPos, yPos;
    public boolean startnode;
}
