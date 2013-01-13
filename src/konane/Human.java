/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package konane;

/**
 *
 * @author James Sonntag
 */

//The human class asks for and inputs the values that a human player inputs
public class Human implements Player {
    public void makeMove(Node[][] board, Main m)
    {
        int startA;
        int startB;
        int finA;
        int finB;
        String in;
        String[] cord;
        in = m.scan.next();
        cord = in.split(",");
        startB = Integer.parseInt(cord[0]); 
        startA = Integer.parseInt(cord[1]);
        System.out.println("Now please pick the location to jump to(Proper format: x,x)");
        in = m.scan.next();
        cord = in.split(",");
        finB = Integer.parseInt(cord[0]); 
        finA = Integer.parseInt(cord[1]);
        if(m.legal(startA, startB, finA, finB, m.board))
        {
            m.jump(startA, startB, finA, finB, m.board, new Human());
            if(m.turn == false)
                m.turn = true;
            else if(m.turn == true)
                m.turn = false;
        }
        else
        {
            if(m.turn == false)
                m.turn = true;
            else if(m.turn == true)
                m.turn = false;
        }
    }
}
