/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package konane;

import java.util.List;

/**
 *
 * @author James Sonntag
 */
//The Prun class is the exact same as the MiniMax class, I wasn't able to implement the Alpha-Beta pruning technique effectively.
public class Prune implements Player {
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
        m.jump(first.row, first.col, first.moveTo.get(check).row, first.moveTo.get(check).col, m.board, new MiniMax());
        
    }
    
    
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
