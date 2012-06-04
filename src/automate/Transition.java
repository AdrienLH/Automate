package automate;

import org.graphstream.graph.Node;

/**
 *
 * @author adrien
 * TPE Master 1
 * AS 2011-2012
 * Classe representant une transition au sein de l'automate
 */
public class Transition {

    State state1;
    State state2;
    Character lettre;

    public Transition(State state1, State state2, Character lettre) {
        this.state1 = state1;
        this.state2 = state2;
        this.lettre = lettre;
    }

    @Override
    public String toString() {
        return "Transition [lettre=" + lettre + ", node0=" + state1 + ", node1="
                + state2 + "]";
    }

    public Character getLettre() {
        return lettre;
    }

    public State getState1() {
        return state1;
    }

    public State getState2() {
        return state2;
    }
}
