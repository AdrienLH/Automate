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
        resultat = new Automate("Union");
    }

    public Automate union(Automate automate1, Automate automate2) {

        if (automate1.equals(automate2)) {
            return automate1;
        } else {
            //Union automate1 et automate2 dans resultat

            ArrayList<State> init1 = automate1.getInitialStates();
            ArrayList<State> init2 = automate2.getInitialStates();

            for (State st : automate1.getStates()) {
                resultat.addState(new State(resultat, st.getId(), st.isInitial(), st.isAcceptable()));
            }

           
            for (Transition t : automate1.getTransitions()) {
                resultat.addEdge(t.getState1().getId() + "-" + t.getState2().getId(), t.getState1(), t.getState2(), true);
                resultat.addTransition(t.getState1(), t.getState2(), t.getLettre());
            }
            for (State st : resultat.getInitialStates()) {
                st.setStart(false);
            }

            for (State st : automate2.getStates()) {
                
                resultat.addState(new State(resultat, st.getId(), st.isInitial(), st.isAcceptable()));
            }

            for (Transition t : automate2.getTransitions()) {
                resultat.addEdge(t.getState1().getId() + "-" + t.getState2().getId(), t.getState1(), t.getState2(), true);
//                resultat.removeEdge(t.getState1().getEdgeBetween(t.getState2()));
                resultat.addTransition(t.getState1(), t.getState2(), t.getLettre());
            }

            State init = resultat.addInitialState(new State(automate1, automate1.getId() + " U " + automate2.getId(), true, false));

            for (State st : init1) {
                resultat.addTransition(init, st, '#');
            }

            for (State st : init2) {
                resultat.addTransition(init, st, '#');
            }
            return resultat;
        }
    }
}
