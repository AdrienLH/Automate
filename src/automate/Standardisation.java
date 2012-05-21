/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automate;

import java.util.ArrayList;
import java.util.Iterator;
import org.graphstream.graph.Node;

/**
 *
 * @author adrien
 */
public class Standardisation {

    Automate resultat;

    public Standardisation() {
    }

    public Automate standardise(Automate au) {
        if (au.getInitialStates().size() == 1) {
            return au.getCopy();
        } else {
            resultat = new Automate(au.getId());
            State d = resultat.addInitialState(au.getNodeCount() + "");
            for (State st : au.getStates()) {
                if (st.isAcceptable()) {
                    resultat.addAcceptableState(st.getId());
                } else if (st.isInitial()) {
                    resultat.addState(st.getId());
                    if (st.isAcceptable()) {
                        d.setAcceptable(true);
                        resultat.getAcceptableStates().add(d);
                    }
                } else if (st.isAcceptable() && st.isInitial()) {
                    resultat.addAcceptableState(st.getId());
                } else {
                    resultat.addState(st.getId());
                }
            }


            for (Transition t : au.getTransitions()) {
                String id1 = t.getState1().getId();
                String id2 = t.getState2().getId();
                resultat.addTransition(resultat.getState(id1), resultat.getState(id2), t.getLettre());
            }
            for (Transition t : au.getTransitions()) {
                if (t.getState1().isInitial()) {
                    resultat.addTransition(d, resultat.getState(t.getState2().getId()), t.getLettre());
                }
            }

            //Suppression des etats isoles
            for (State st : resultat.getStates()) {
                Iterator<State> itState = st.getNeighborNodeIterator();
                int count = 0;
                while (itState.hasNext()) {
                    itState.next();
                    count++;
                }
                if (count == 0) {
                    resultat.removeState(st);
                }
            }
            return resultat;
        }
    }
}
