/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package konane;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 *
 * @author James Sonntag
 */

//This MiniMax class implements the MiniMax agent/algorithm and is a dependent of the Player class
public class MiniMax implements Player {
    public List<Node> list = new ArrayList();
    public boolean black = true;
    public void makeMove(Node[][] board, Main m)
    {
        if(m.nextTurn)
            m.firstMoveMinMax(m.board);
        
        List<Node> list = m.possibleMoves(board);
        int size = list.size();
        Node first = list.get(0);
        for(int i = 0; i < size; i++)
        {
            if(list.get(i) != null && list.get(i).min < first.min)
                first = list.get(i);
        }
        System.out.println(first.col + "," + first.row);
        System.out.println("Now please pick the location to jump to(Proper format: x,x)");
        int check = first.check;
        if(m.turn == false)
            m.turn = true;
        else if(m.turn == true)
            m.turn = false;
        System.out.println(first.moveTo.get(check).col + "," + first.moveTo.get(check).row);
        System.out.println("Number of nodes evaluated = " + size);
        System.out.println("Computing time in nanoSeconds = " + m.elapsedTime);
        m.jump(first.row, first.col, first.moveTo.get(check).row, first.moveTo.get(check).col, m.board, new MiniMax());
        
    }
    
    //This will traverse the best node up, so that we know which move to take.
    public Node move(Node start)
    {
        if(start.moveTo == null)
            return start;
        else
        {
            int max = 0;
            for(int i = 0; i < start.moveTo.size(); i++)
            {
                if(start.moveTo.get(i) != null)
                    max = start.moveTo.get(i).numberChildren;
            }
            
            Node maxNode = start.moveTo.get(0);
            for(int i = 0; i < start.moveTo.size(); i++)
            {
                if(start.moveTo.get(0).numberChildren > max)
                {
                    max = start.moveTo.get(0).numberChildren;
                    maxNode = start.moveTo.get(0);
                }
                
            }
            return maxNode;
        }
    }
    
    //This will give us the minimum number of children to a node
    public Node minValue(Node start)
    {
        if(start.moveTo == null)
            return start;
        else
        {
            int min = start.moveTo.get(0).numberChildren;
            Node minNode = start.moveTo.get(0);
            for(int i = 0; i < start.moveTo.size(); i++)
            {
                if(start.moveTo.get(0).numberChildren < min)
                {
                    min = start.moveTo.get(0).numberChildren;
                    minNode = start.moveTo.get(0);
                }
                
            }
            return minNode;
        }
    }
    
}
