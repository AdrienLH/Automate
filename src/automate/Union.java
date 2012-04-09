/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automate;

import java.util.ArrayList;
import java.util.HashSet;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiNode;

/**
 *
 * @author Adrien
 */
public class Union {

    Automate resultat;

    public Union() {
        resultat=null;
    }

    public Automate union(Automate automate1, Automate automate2) {
        
        resultat = new Automate("("+automate1.getId()+")"+" U ("+automate2.getId()+")");
        if (automate1.equals(automate2)) {
            return automate1;
        } else {
            //Union automate1 et automate2 dans resultat

            State init=resultat.addInitialState("I");
            for (State st : automate1.getStates()) {
                resultat.addState(st.getId());
            }

            for (State st : automate2.getStates()) {
                resultat.addState(st.getId());
            }

            for (Transition t : automate1.getTransitions()) {
                String id1 = t.getState1().getId();
                String id2 = t.getState2().getId();
                resultat.addTransition(resultat.getState(id1), resultat.getState(id2), t.getLettre());
            }

            for (Transition t : automate2.getTransitions()) {
                String id1 = t.getState1().getId();
                String id2 = t.getState2().getId();
                resultat.addTransition(resultat.getState(id1), resultat.getState(id2), t.getLettre());
            }
            
            
            for (State st : automate1.getInitialStates()) {
                String id=st.getId();
                resultat.addTransition(init, resultat.getState(id), '#');
            }
            
            for (State st : automate2.getInitialStates()) {
                String id=st.getId();
                resultat.addTransition(init, resultat.getState(id), '#');
            }
            
            return resultat;
        }
    }
}
