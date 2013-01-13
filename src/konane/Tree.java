/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package konane;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author James Sonntag
 */

//This class was supposed to layer the possible moves and children nodes.
public class Tree {
    public Node mother;
    public List<Node> children;
    
    public Tree(Node mother)
    {
        this.mother = mother;
        children = new ArrayList();
    }
    
    public void addList(List<Node> list)
    {
        children.addAll(list);
    }
    
    public List<Node> getList()
    {
        return children;
    }
    
    public int numberChildren()
    {
        return children.size();
    }
}
