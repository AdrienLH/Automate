/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automate;
import org.graphstream.graph.implementations.MultiNode;

/**
 *
 * @author Adrien
 */
public class State extends MultiNode{
    private boolean initial=false;
    private boolean acceptable=false;
    MultiNode state=null;
    public State(Automate au, String id, boolean initial, boolean acceptable){
        super(au, id);
        this.acceptable=acceptable;
        this.initial=initial;
    }
    
    public void setAcceptable(boolean isAcceptable) {
        this.acceptable = isAcceptable;
    }

    public void setInitial(boolean isStart) {
        this.initial = isStart;
    }

    public boolean isAcceptable() {
        return acceptable;
    }

    public boolean isInitial() {
        return initial;
    }
    
    
}
