/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package konane;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;

/**
 *
 * @author James Sonntag
 */

//This class is the basic Node class, its main function is to keep track of moves that were made and moves that can be made.
public class Node {

    public int check;
    public int row;
    public int col;
    public char color;
    public int jrow;
    public int jcol;
    public int min;
    public Node minMove;
    public int[] moveCounter = new int[4];
    public List<Node> moveTo = new Vector<Node>() {{setSize(0);}};
    public List<Node> moveChildren1 = new Vector<Node>() {{setSize(0);}};;
    public List<Node> moveChildren2 = new Vector<Node>() {{setSize(0);}};;
    public List<Node> moveChildren3 = new Vector<Node>() {{setSize(0);}};;
    public List<Node> moveChildren4 = new Vector<Node>() {{setSize(0);}};;
    public Tree branches = null;
    public int numberChildren;
    public Node passedNode;

    public Node()
    {
        this.row = 0;
        this.col = 0;
        this.color = 0;
        branches = new Tree(this);
    }

    public Node(int row, int col, char color)
    {
        this.row = row;
        this.col = col;
        this.color = color;
        branches = new Tree(this);
    }
    
    //This method was going to store all of the children nodes
    public void branchOut(List<Node> children)
    {
        branches.addList(children);
        numberChildren = children.size();
    }

    public void print()
    {
        System.out.print(row + "," + col + " ");
    }
    
    //This method finds the minimun value for the children nodes
    public void findMin()
    {
        int[] c = new int[4];
        Arrays.fill(c, Integer.MAX_VALUE);
        boolean[] a = new boolean[4];
        if(!moveTo.isEmpty())
        {
            for(int i = 0; i < 4; i++)
            {
                if(i < moveTo.size() && moveTo.get(i) != null)
                    a[i] = true;
            }
            for(int i = 0; i < 4; i++)
            {
                if(a[i] == true)
                {
                    c[i] = moveCounter[i];
                }
            }
        }
        int min = c[0];
        
        for(int i = 0; i < 4; i++)
        {
            if(c[i] < min)
            {
                min = c[i];
                check = i;
            }
        }
        this.min = min;
    }
}
