/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automate;

import org.graphstream.graph.implementations.MultiNode;

/**
 *
 * @author adrien
 * TPE Master 1
 * AS 2011-2012
 * Classe representant un état d'un automate
 */
public class State extends MultiNode {

    private boolean initial = false;
    private boolean acceptable = false;

    public State(Automate au, String id, boolean initial, boolean acceptable) {
        super(au, id);
        this.acceptable = acceptable;
        this.initial = initial;
    }

    public void setAcceptable(boolean isAcceptable) {
        this.acceptable = isAcceptable;
        if (isAcceptable) {
            this.addAttribute("acceptable", "acceptable");
        }
    }

    public void setInitial(boolean isStart) {
        this.initial = isStart;
        if (isStart) {
            this.addAttribute("initial", "initial");
        }
    }

    public boolean isAcceptable() {
        return acceptable;
    }

    public boolean isInitial() {
        return initial;
    }
}
