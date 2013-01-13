/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package konane;

import java.util.Scanner;
import java.io.PrintStream;
import java.util.*;
/**
 *
 * @author James Sonntag
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public long elapsedTime;
    public int boardSize = 0;
    public boolean nextTurn = true;
    public boolean turn = true;
    Node[][] board;
    int first = 0;
    char direction;
    int middleA;
    int middleB;
    Scanner scan = new Scanner(System.in);
    Player player1;
    Player player2;
    boolean finish = false;
    int nextTry;
    int p1;
    int p2;
    int MinMaxA;
    int MinMaxB;
    int MaxDepth;
    
    //Starting the program
    public static void main(String[] args) {
        Main m = new Main();
        m.runGame(m);
    }
    
    //Initializing the type of players from input
    public void startPlayers()
    {
        System.out.println("Shall player 1 be human, minMax, or minMax with pruning? Please enter 1, 2, or 3 respectively");
        int game = scan.nextInt();
        switch(game)
        {
            case 1: player1 = new Human();
                    p1 = 1;
                    break;
            case 2: player1 = new MiniMax();
                    p1 = 2;
                    break;
            case 3: player1 = new Prune();
                    p1 = 3;
                    break;
            default: System.out.println("That was not an accepted answer");
                     break;
        }
        System.out.println("Shall player 2 be human, minMax, or minMax with pruning? Please enter 1, 2, or 3 respectively");
        game = scan.nextInt();
        switch(game)
        {
            case 1: player2 = new Human();
                    p2 = 1;
                    break;
            case 2: player2 = new MiniMax();
                    p2 = 2;
                    break;
            case 3: player2 = new Prune();
                    p2 = 3;
                    break;
            default: System.out.println("That was not an accepted answer");
                     break;
        }
        
    }
    
    //Runs the game, starting by initializing the game board and assigning the types to the players.
    //It then runs the firstmove methods and then finally lets the game be played.
    public void runGame(Main m)
    {
        initBoard();
        startPlayers();
        int counter = 0;
        String[] cord;
        printBoard();
        
        if(p1 == 1)
            firstMove(board);
        else
            firstMoveMinMax(board);
        printBoard();
        if(p2 == 1)
            firstMove(board);
        else
            firstMoveMinMax(board);
        printBoard();
        
        play(m);
    }
    
    //This method runs the abstract makeMove method until someone cannot make another move, thus proclaiming victory to the other player
    public void play(Main m)
    {
        while(!finished(board))
        {
                if(turn == true){
                    System.out.println("Player 1, please pick the first piece(Proper format: x,x)");
                    player1.makeMove(board, m);
                }
                else if(turn == false){
                    System.out.println("Player 2, please pick the first piece(Proper format: x,x)");
                    player2.makeMove(board, m);
                }
                printBoard();
        }
        if(turn == true)
            System.out.println("Player 2 wins!");
        else if(turn == false)
            System.out.println("Player 1 wins!");
    }

    //Initializes the board by asking for the board size and then setting all the pieces in their place.
    public void initBoard()
    {
        System.out.println("How many squares would you like the board to be? (4, 6, or 8)");
        int input = scan.nextInt();
        boardSize = input;
        MaxDepth = boardSize * 2;
        board = new Node[input][input];
        board[0][0] = new Node(0, 0, 'b');

        
        char color = 'b';
        for(int row = 0; row < input; row++)
        {
            
            for(int col = 0; col < input; col++)
            {
                
                board[row][col] = new Node(row, col, color);
                if(color == 'b')
                {
                    color = 'w';
                } else
                    color = 'b';
                
            }
            if(color == 'b')
                {
                    color = 'w';
                } else
                    color = 'b';
        }
    }

    //Removes a piece.
    public void remove(int a, int b, Node[][] bo)
    {
        bo[a][b].color = ' ';
    }

    //This is the method that askes the human players to pick the first piece to remove
    public void firstMove(Node[][] bo)
    {
        int a;
        int b;
        String in;
        String[] cord;
        int half = board.length / 2;
        if(turn == true)
        {
            System.out.println("Player 1, please pick the first piece to be removed(Proper format: x,x)");
            in = scan.next();
            cord = in.split(",");
            a = Integer.parseInt(cord[0]); 
            b = Integer.parseInt(cord[1]);
            if(a == b)
            {
                if(a == 0 || a == board.length-1)
                {
                    turn = false;
                    remove(a,b, bo);
                    first = a;
                    
                }
                else if(a == half || a == half - 1)
                {
                        turn = false;
                        remove(a,b, bo);
                        first = a;
                        
                }
                else{
                    System.out.println("Invalid move, try again");
                    firstMove(bo);
                }
            }
            else{
                    System.out.println("Invalid move, try again");
                    firstMove(bo);
                }
        }
        else if(turn == false)
        {
            System.out.println("Player 2, pick a piece adjacent to the last removed piece(Proper format: x,x).");
            in = scan.next();
            cord = in.split(",");
            a = Integer.parseInt(cord[0]); 
            b = Integer.parseInt(cord[1]);
            if (board[a][b].color == 'w')
            {
                if(a == (first+1) || a == (first-1) || b == (first+1) || b == (first-1))
                {
                    remove(a,b, bo);
                    turn = true;
                }
                else{
                    System.out.println("Invalid move, try again");
                    firstMove(bo);
                }
            }
        }
            
    }
    
    //This is the firstMove method for the MiniMax agents. The first piece to be removed is random.
    public void firstMoveMinMax(Node[][] bo)
    {
        
        int half = board.length / 2;
        if(turn == true)
        {
            double ran = Math.random();
            System.out.println("Player 1, please pick the first piece to be removed(Proper format: x,x)");
            if(ran < .25)
            {
                MinMaxA = 0;
                MinMaxB = MinMaxA;
                System.out.println(MinMaxA + "," + MinMaxB);
            }
            else if(ran < .5)
            {
                MinMaxA = board.length - 1;
                MinMaxB = MinMaxA;
                System.out.println(MinMaxA + "," + MinMaxB);
            }
            else if(ran < .75)
            {
                MinMaxA = half - 1;
                MinMaxB = MinMaxA;
                System.out.println(MinMaxA + "," + MinMaxB);
            }
            else
            {
                MinMaxA = half;
                MinMaxB = MinMaxA;
                System.out.println(MinMaxA + "," + MinMaxB);
            }
            
            if(MinMaxA == MinMaxB)
            {
                if(MinMaxA == 0 || MinMaxA == board.length-1)
                {
                    turn = false;
                    remove(MinMaxA,MinMaxB, bo);
                    first = MinMaxA;
                    
                }
                else if(MinMaxA == half || MinMaxA == half - 1)
                {
                        turn = false;
                        remove(MinMaxA,MinMaxB, bo);
                        first = MinMaxA;
                        
                }
            }
        }
        else if(turn == false)
        {
            System.out.println("Player 2, pick a piece adjacent to the last removed piece(Proper format: x,x).");
            double ran = Math.random();
            if(MinMaxA < board.length && MinMaxA!=0)
            {
                if(ran < .5)
                {
                    MinMaxA = first-1;
                    MinMaxB = first;
                }
                else
                {
                    MinMaxB = first-1;
                    MinMaxA = first;
                }
                System.out.println(MinMaxA + "," + MinMaxB);
            }
            else if(MinMaxA >= 0)
            {
                if(ran < .5)
                {
                    MinMaxA = first+1;
                    MinMaxB = first;
                }
                else
                {
                    MinMaxB = first+1;
                    MinMaxA = first;
                }
                System.out.println(MinMaxA + "," + MinMaxB);
            }
            else
                System.out.println("What" + MinMaxB);
            if (board[MinMaxA][MinMaxB].color == 'w')
            {
                if(MinMaxA == (first+1) || MinMaxA == (first-1) || MinMaxB == (first+1) || MinMaxB == (first-1))
                {
                    remove(MinMaxA,MinMaxB, bo);
                    turn = true;
                    nextTurn = false;
                }
            }
        }
    }

    //This method return true if a movement from one space to another is legal.
    public boolean legal(int startA, int startB, int finA, int finB, Node[][] b)
    {
        int difA = startA - finA;
        int difB = startB - finB;
        middleA = startA;
        middleB = startB;

        //These conditions check which direction the piece was moved and find the middle piece.
        if(difA > 0)
        {
            direction = 'l';
            middleA = startA - 1;
        }
        else if(difA < 0)
        {
            direction = 'r';    
            middleA = startA + 1;
        }
        else if(difB > 0)
        {
            direction = 'u';
            middleB = startB -1;
        }
        else if(difB < 0)
        {
            direction = 'd';
            middleB = startB + 1;
        }

        //Depending on whos turn it is and what color has been moved, these conditions will say whether the move is legal or not
        if(turn == true && b[startA][startB].color == 'b' && b[finA][finB].color == ' ' && b[middleA][middleB].color == 'w' && Math.abs(difA) < 3 && Math.abs(difB) < 3 )
        {
            if(difA == 0 || difB == 0)
            {
                return true;
            }
            else
                return false;
        }
        else if(turn == false && b[startA][startB].color == 'w' && b[finA][finB].color == ' ' && b[middleA][middleB].color == 'b' && Math.abs(difA) < 3 && Math.abs(difB) < 3 )
        {
            if(difA == 0 || difB == 0)
            {
                return true;
            }
            else
                return false;
        }
        else
            return false;
    }

    //Changes the location of a piece.
    public void change(int startA, int startB, int finA, int finB, Node[][] b)
    {
        char temp = b[startA][startB].color;
        b[startA][startB].color = ' ';
        b[finA][finB].color = temp;
    }
    
    //Checks to see if another move (aka another jump) can happen
    public boolean anotherMove(int finA, int finB, Node[][] b)
    {
        String in;
            if(direction == 'u')
            {
                nextTry = finB-2;
                if(nextTry < boardSize && nextTry > -1)
                {
                    if(legal(finA, finB, finA, nextTry, b))
                    {
                        return true;
                    }
                }
            }
            else if(direction == 'd')
            {
                nextTry = finB+2;
                if(nextTry < boardSize && nextTry > -1)
                {
                    if(legal(finA, finB, finA, nextTry, b))
                    {
                        return true;
                    }
                }
            }
            else if(direction == 'l')
            {
                nextTry = finA-2;
                if(nextTry < boardSize && nextTry > -1)
                {
                    if(legal(finA, finB, nextTry, finB, b))
                    {
                        return true;
                    }
                }
            }
            else if(direction == 'r')
            {
                nextTry = finA+2;
                if(nextTry < boardSize && nextTry > -1)
                {
                    if(legal(finA, finB, nextTry, finB, b))
                    {
                        return true;
                    }
                }
            }
            return false;
    }

    //This method implements the jumping of pieces. Combining both the legal method and the anotherMove method
    public void jump(int startA, int startB, int finA, int finB, Node[][] b, Player player)
    {
        if(legal(startA, startB, finA, finB, b))
        {
            remove(middleA, middleB, b);
            change(startA, startB, finA, finB, b);
            if(anotherMove(finA, finB, b))
            {
                if(player == new Human())       //Depending on whether the player is Human or an agent, it will then take the necessary steps.
                {
                    if(direction =='u' || direction == 'd')
                    {
                        System.out.println("Would you like to make another move?");
                                String in = scan.nextLine();
                                if(in.toLowerCase().equals("yes"))
                                {
                                    jump(finA, finB, finA, nextTry, b, player);
                                }
                    }
                    else if(direction =='l' || direction =='r')
                    {
                        System.out.println("Would you like to make another move?");
                                String in = scan.nextLine();
                                if(in.toLowerCase().equals("yes"))
                                {
                                    jump(finA, finB, nextTry, finB, b, player);
                                }
                    }
                }
                else                            //Agent
                {
                    if(direction =='u' || direction == 'd')
                    {
                        if(turn == true)
                            System.out.println("Player1 makes a double jump!");
                        else
                            System.out.println("Player2 makes a double jump!");
                        jump(finA, finB, finA, nextTry, b, player);
                    }
                    else if(direction =='l' || direction =='r')
                    {
                        if(turn == true)
                            System.out.println("Player1 makes a double jump!");
                        else
                            System.out.println("Player2 makes a double jump!");
                        jump(finA, finB, nextTry, finB, b, player);
                    }
                }
            }
        }

    }
    
    //Method that returns true if there are not more moves to be made.
    public boolean finished(Node[][] b)
    {
        for(int row = 0; row < boardSize; row++)
        {
            for(int col = 0; col < boardSize; col++)
            {
                if(row+2 < boardSize && row+2 > -1)
                {
                    if(legal(row, col, row+2, col, b))
                    {
                        return false;
                    }
                }
                if(row-2 < boardSize && row-2 > -1)
                {
                    if(legal(row, col, row-2, col, b))
                    {
                        return false;
                    }
                }
                if(col+2 < boardSize && col+2 > -1)
                {
                    if(legal(row, col, row, col+2, b))
                    {
                        return false;
                    }
                }
                if(col-2 < boardSize && col-2 > -1)
                {
                    if(legal(row, col, row, col-2, b))
                    {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    
    //Method that prints the board.
    public void printBoard()
    {
        for(int row = 0; row < boardSize; row++)
        {
            for(int col = 0; col < boardSize; col++)
            {
                System.out.print("|" + board[row][col].color);
            }
            System.out.println("|");
        }
    }
    
    //This method was to be implemented with the Tree class, it was going to add each possible move to a nodes moveTo list.
    //This method serves no purpose at the moment.
    public void possibleM(Node[][] b, Node start, int depth)
    {
        Node[][] temp = b.clone();
        List<Node> children = new ArrayList();
        if(legal(start.row, start.col, start.row, start.col + 2,b))
        {
            Node move = new Node(start.row, start.col+2, start.color);
            for(int row = 0; row < board.length; row++)
            {
                for(int col = 0; col < board.length; col++)
                {
                    if(board[row][col].color == opposite(start.color))
                    {
                        int next = col+2;
                        if(next < boardSize && next > -1)
                        {
                            if(legal(row, col, row, next, b))
                            {
                                children.add(board[row][next]);
                                temp = b.clone();
                            }
                        }
                        next = col-2;
                        if(next < boardSize && next > -1)
                        {
                            if(legal(row, col, row, next, b))
                            {
                                children.add(board[row][next]);
                                temp = b.clone();
                            }
                        }
                        next = row+2;
                        if(next < boardSize && next > -1)
                        {
                            if(legal(row, col, row, next, b))
                            {
                                children.add(board[next][col]);
                                temp = b.clone();
                            }
                        }
                        next = row-2;
                        if(next < boardSize && next > -1)
                        {
                            if(legal(row, col, row, next, b))
                            {
                                children.add(board[next][col]);
                                temp = b.clone();
                            }
                        }
                    }
                }
            }
            move.branchOut(children);
            start.moveTo.add(move);
        }
        if(legal(start.row, start.col, start.row, start.col - 2,b))
        {
            Node move = new Node(start.row, start.col-2, start.color);
            for(int row = 0; row < board.length; row++)
            {
                for(int col = 0; col < board.length; col++)
                {
                    if(board[row][col].color == opposite(start.color))
                    {
                        int next = col+2;
                        if(next < boardSize && next > -1)
                        {
                            if(legal(row, col, row, next, b))
                            {
                                children.add(board[row][next]);
                                temp = b.clone();
                            }
                        }
                        next = col-2;
                        if(next < boardSize && next > -1)
                        {
                            if(legal(row, col, row, next, b))
                            {
                                children.add(board[row][next]);
                                temp = b.clone();
                            }
                        }
                        next = row+2;
                        if(next < boardSize && next > -1)
                        {
                            if(legal(row, col, row, next, b))
                            {
                                children.add(board[next][col]);
                                temp = b.clone();
                            }
                        }
                        next = row-2;
                        if(next < boardSize && next > -1)
                        {
                            if(legal(row, col, row, next, b))
                            {
                                children.add(board[next][col]);
                                temp = b.clone();
                            }
                        }
                    }
                }
            }
            move.branchOut(children);
            start.moveTo.add(move);
        }
        if(legal(start.row, start.col, start.row+2, start.col,b))
        {
            Node move = new Node(start.row+2, start.col, start.color);
            for(int row = 0; row < board.length; row++)
            {
                for(int col = 0; col < board.length; col++)
                {
                    if(board[row][col].color == opposite(start.color))
                    {
                        int next = col+2;
                        if(next < boardSize && next > -1)
                        {
                            if(legal(row, col, row, next, b))
                            {
                                children.add(board[row][next]);
                                temp = b.clone();
                            }
                        }
                        next = col-2;
                        if(next < boardSize && next > -1)
                        {
                            if(legal(row, col, row, next, b))
                            {
                                children.add(board[row][next]);
                                temp = b.clone();
                            }
                        }
                        next = row+2;
                        if(next < boardSize && next > -1)
                        {
                            if(legal(row, col, row, next, b))
                            {
                                children.add(board[next][col]);
                                temp = b.clone();
                            }
                        }
                        next = row-2;
                        if(next < boardSize && next > -1)
                        {
                            if(legal(row, col, row, next, b))
                            {
                                children.add(board[next][col]);
                                temp = b.clone();
                            }
                        }
                    }
                }
            }
            move.branchOut(children);
            start.moveTo.add(move);
        }
        if(legal(start.row, start.col, start.row-2, start.col,b))
        {
            Node move = new Node(start.row-2, start.col, start.color);
            for(int row = 0; row < board.length; row++)
            {
                for(int col = 0; col < board.length; col++)
                {
                    if(board[row][col].color == opposite(start.color))
                    {
                        int next = col+2;
                        if(next < boardSize && next > -1)
                        {
                            if(legal(row, col, row, next, b))
                            {
                                children.add(board[row][next]);
                                temp = b.clone();
                            }
                        }
                        next = col-2;
                        if(next < boardSize && next > -1)
                        {
                            if(legal(row, col, row, next, b))
                            {
                                children.add(board[row][next]);
                                temp = b.clone();
                            }
                        }
                        next = row+2;
                        if(next < boardSize && next > -1)
                        {
                            if(legal(row, col, row, next, b))
                            {
                                children.add(board[next][col]);
                                temp = b.clone();
                            }
                        }
                        next = row-2;
                        if(next < boardSize && next > -1)
                        {
                            if(legal(row, col, row, next, b))
                            {
                                children.add(board[next][col]);
                                temp = b.clone();
                            }
                        }
                    }
                }
            }
            move.branchOut(children);
            start.moveTo.add(move);
        }
        for(int i = 0; i < start.moveTo.size(); i++)
        {
            if(depth < MaxDepth)
                possibleM(temp, start.moveTo.get(i), depth++);
        }
            
    }
    
    //This method returns the opposite color of a given piece.
    public char opposite(char a)
    {
        if(a == 'b')
            return 'w';
        if(a == 'w')
            return 'b';
        else
            return ' ';
    }
    
    //This method returns a List of possible moves to make, and the list of moves that the opponent would be able to make after the move
    public List<Node> possibleMoves(Node[][] b)
    {
        long start = System.nanoTime();
            Node[][] testBoard = b.clone();
            boolean turn = this.turn;
            List<Node> list = new Vector<Node>() {{setSize(0);}};
            
            //This loop goes through every square of the game board and checks to see if a move can be made.
            //Certain conditions apply, such as making sure that if it is black's turn, then the only piece to be moved is black's
            for(int row = 0; row< boardSize; row++)
            {
                for(int col = 0; col<boardSize; col++)
                {
                    if(testBoard[row][col].color == 'b' && turn == true)
                    {
                        int next = col +2;
                        if(next < boardSize && next > -1 && turn == true)
                        {
                            if(legal(row, col, row, next, testBoard))
                            {
                                testBoard[row][col].row = row;
                                testBoard[row][col].col = col;
                                testBoard[row][col].jrow = row;
                                testBoard[row][col].jcol = next;
                                jump(row, col, row, next, testBoard, new MiniMax());
                                turn = false;
                                testBoard[row][col].moveTo.add(0,testBoard[row][next]);
                                testBoard[row][col].moveChildren1.addAll(checkChange(testBoard, testBoard[row][next], testBoard[row][col].moveCounter[0]));
                                testBoard = b.clone();
                            }else
                                testBoard[row][col].moveTo.add(0, null);
                        }else
                            testBoard[row][col].moveTo.add(0, null);
                        next = col - 2;
                        if(next < boardSize && next > -1 && turn == true)
                        {
                            if(legal(row, col, row, next, testBoard))
                            {
                                testBoard[row][col].row = row;
                                testBoard[row][col].col = col;
                                testBoard[row][col].jrow = row;
                                testBoard[row][col].jcol = next;
                                testBoard[row][col].moveTo.add(1,testBoard[row][next]);
                                turn = false;
                                jump(row, col, row, next, testBoard, new MiniMax());
                                testBoard[row][col].moveChildren2.addAll(checkChange(testBoard, testBoard[row][next], testBoard[row][col].moveCounter[1]));
                                testBoard = b.clone();
                            }else
                                testBoard[row][col].moveTo.add(1, null);
                        }else
                            testBoard[row][col].moveTo.add(1, null);
                        next = row +2;
                        if(next < boardSize && next > -1 && turn == true)
                        {
                            if(legal(row, col, next, col, testBoard))
                            {
                                testBoard[row][col].row = row;
                                testBoard[row][col].col = col;
                                testBoard[row][col].jrow = next;
                                testBoard[row][col].jcol = col;
                                testBoard[row][col].moveTo.add(2,testBoard[next][col]);
                                turn = false;
                                jump(row, col, next, col, testBoard, new MiniMax());
                                testBoard[row][col].moveChildren3.addAll(checkChange(testBoard, testBoard[next][col], testBoard[row][col].moveCounter[2]));
                                testBoard = b.clone();
                            }else
                                testBoard[row][col].moveTo.add(2, null);
                        }else
                            testBoard[row][col].moveTo.add(2, null);
                        next = row - 2;
                        if(next < boardSize && next > -1 && turn == true)
                        {
                            if(legal(row, col, next, col, testBoard))
                            {
                                testBoard[row][col].row = row;
                                testBoard[row][col].col = col;
                                testBoard[row][col].jrow = next;
                                testBoard[row][col].jcol = col;
                                testBoard[row][col].moveTo.add(3,testBoard[next][col]);
                                turn = false;
                                jump(row, col, next, col, testBoard, new MiniMax());
                                testBoard[row][col].moveChildren4.addAll(checkChange(testBoard, testBoard[next][col], testBoard[row][col].moveCounter[3]));
                                testBoard = b.clone();
                            }else
                                testBoard[row][col].moveTo.add(3, null);
                        }else
                            testBoard[row][col].moveTo.add(3, null);
                    }
                    //Test for white
                    else if(testBoard[row][col].color == 'w' && turn == false)
                    {
                        int next = col +2;
                        if(next < boardSize && next > -1 && turn == false)
                        {
                            if(legal(row, col, row, next, testBoard))
                            {
                                testBoard[row][col].row = row;
                                testBoard[row][col].col = col;
                                testBoard[row][col].jrow = row;
                                testBoard[row][col].jcol = next;
                                jump(row, col, row, next, testBoard, new MiniMax());
                                testBoard[row][col].moveTo.add(0,testBoard[row][next]);
                                turn = true;
                                testBoard[row][col].moveChildren1.addAll(checkChange(testBoard, testBoard[row][next], testBoard[row][col].moveCounter[0]));
                                testBoard = b.clone();
                            }else
                                testBoard[row][col].moveTo.add(0, null);
                        }else
                            testBoard[row][col].moveTo.add(0, null);
                        next = col - 2;
                        if(next < boardSize && next > -1 && turn == false)
                        {
                            if(legal(row, col, row, next, testBoard))
                            {
                                testBoard[row][col].row = row;
                                testBoard[row][col].col = col;
                                testBoard[row][col].jrow = row;
                                testBoard[row][col].jcol = next;
                                testBoard[row][col].moveTo.add(1,testBoard[row][next]);
                                turn = true;
                                jump(row, col, row, next, testBoard, new MiniMax());
                                testBoard[row][col].moveChildren2.addAll(checkChange(testBoard, testBoard[row][next], testBoard[row][col].moveCounter[1]));
                                testBoard = b.clone();
                            }else
                                testBoard[row][col].moveTo.add(1, null);
                        }else
                            testBoard[row][col].moveTo.add(1, null);
                        next = row +2;
                        if(next < boardSize && next > -1 && turn == false)
                        {
                            if(legal(row, col, next, col, testBoard))
                            {
                                testBoard[row][col].row = row;
                                testBoard[row][col].col = col;
                                testBoard[row][col].jrow = next;
                                testBoard[row][col].jcol = col;
                                testBoard[row][col].moveTo.add(2,testBoard[next][col]);
                                turn = true;
                                jump(row, col, next, col, testBoard, new MiniMax());
                                testBoard[row][col].moveChildren3.addAll(checkChange(testBoard, testBoard[next][col], testBoard[row][col].moveCounter[2]));
                                testBoard = b.clone();
                            }else
                                testBoard[row][col].moveTo.add(2, null);
                        }else
                            testBoard[row][col].moveTo.add(2, null);
                        next = row - 2;
                        if(next < boardSize && next > -1 && turn == false)
                        {
                            if(legal(row, col, next, col, testBoard))
                            {
                                testBoard[row][col].row = row;
                                testBoard[row][col].col = col;
                                testBoard[row][col].jrow = next;
                                testBoard[row][col].jcol = col;
                                testBoard[row][col].moveTo.add(3,testBoard[next][col]);
                                turn = true;
                                jump(row, col, next, col, testBoard, new MiniMax());
                                testBoard[row][col].moveChildren4.addAll(checkChange(testBoard, testBoard[next][col], testBoard[row][col].moveCounter[3]));
                                testBoard = b.clone();
                            }else
                                testBoard[row][col].moveTo.add(3, null);
                        }else
                            testBoard[row][col].moveTo.add(3, null);
                    }
                    testBoard[row][col].findMin();
                    list.add(testBoard[row][col]);
                }
            }
            elapsedTime = System.nanoTime() - start;
        return list;
        
    }
    
    //Removes duplicate items in a list
    public static void removeDup(List list)
    {
        HashSet h = new HashSet(list);
        list.clear();
        list.addAll(h);
    }
    
    //This method returns a list of pieces that will be affected by a move.
    public List<Node> checkChange(Node[][] b, Node node, int counter)
    {
        Node[][] testBoard = b;
        List<Node> list = new Vector<Node>() {{setSize(0);}};
        for(int row = 0; row< boardSize; row++)
        {
            for(int col = 0; col<boardSize; col++)
            {
                if(b[row][col].color == 'w' && node.color == 'b')
                {
                    int next = col +2;
                    if(next < boardSize && next > -1)
                    {
                        if(legal(row, col, row, next, testBoard))
                        {
                            list.add(testBoard[row][next]);
                            counter++;
                            if(anotherMove(row, next, testBoard))
                                counter++;
                        }
                    }
                    next = col - 2;
                    if(next < boardSize && next > -1)
                    {
                        if(legal(row, col, row, next, testBoard))
                        {
                            list.add(testBoard[row][next]);
                            counter++;
                            if(anotherMove(row, next, testBoard))
                                counter++;
                        }
                    }
                    next = row+2;
                    if(next < boardSize && next > -1)
                    {
                        if(legal(row, col, next, col, testBoard))
                        {
                            list.add(testBoard[next][col]);
                            counter++;
                            if(anotherMove(next, col, testBoard))
                                counter++;
                        }
                    }
                    next = row-2;
                    if(next < boardSize && next > -1)
                    {
                        if(legal(row, col, next, col, testBoard))
                        {
                            list.add(testBoard[next][col]);
                            counter++;
                            if(anotherMove(next, col, testBoard))
                                counter++;
                        }
                    }
                }
                else if(b[row][col].color == 'b' && node.color == 'w')
                {
                    int next = col +2;
                    if(next < boardSize && next > -1)
                    {
                        if(legal(row, col, row, next, testBoard))
                        {
                            list.add(testBoard[row][next]);
                            counter++;
                            if(anotherMove(row, next, testBoard))
                                counter++;
                        }
                    }
                    next = col - 2;
                    if(next < boardSize && next > -1)
                    {
                        if(legal(row, col, row, next, testBoard))
                        {
                            list.add(testBoard[row][next]);
                            counter++;
                            if(anotherMove(row, next, testBoard))
                                counter++;
                        }
                    }
                    next = row+2;
                    if(next < boardSize && next > -1)
                    {
                        if(legal(row, col, next, col, testBoard))
                        {
                            list.add(testBoard[next][col]);
                            counter++;
                            if(anotherMove(next, col, testBoard))
                                counter++;
                        }
                    }
                    next = row-2;
                    if(next < boardSize && next > -1)
                    {
                        if(legal(row, col, next, col, testBoard))
                        {
                            list.add(testBoard[next][col]);
                            counter++;
                            if(anotherMove(next, col, testBoard))
                                counter++;
                        }
                    }
                }
            }
        }
        return list;
    }
}
