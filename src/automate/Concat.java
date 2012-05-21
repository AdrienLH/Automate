/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automate;

import java.util.ArrayList;

/**
 *
 * @author adrien
 */
public class Concat {

    Automate resultat;

    public Concat() {
        resultat = null;
    }

    public Automate concat(Automate automate1, Automate automate2) {
        Standardisation stdOp = new Standardisation();
        Automate auStd = new Automate(automate2.getId());
        resultat = new Automate(automate1.getId() + automate2.getId());

        if (automate2.getInitialStates().size() != 1) {
            auStd = stdOp.standardise(automate2);
        } else {
            auStd = automate2.getCopy();
        }

        State d2 = auStd.getInitialStates().get(0);
        for (State st : automate1.getStates()) {
            if (st.isInitial()) {
                resultat.addInitialState(st.getId());
            } else if (st.isAcceptable()) {
                if (d2.isAcceptable()) {
                    resultat.addAcceptableState(st.getId());
                } else {
                    resultat.addState(st.getId());
                }
            } else {
                resultat.addState(st.getId());
            }
        }

        for (State st : auStd.getStates()) {
            if (st.isAcceptable() && !st.equals(d2)) {
                resultat.addAcceptableState(st.getId());
            } else if (!st.equals(d2)) {
                resultat.addState(st.getId());
            }
        }

        for (Transition t : automate1.getTransitions()) {
            String id1 = t.getState1().getId();
            String id2 = t.getState2().getId();

            resultat.addTransition(resultat.getState(id1), resultat.getState(id2), t.getLettre());
        }

        for (Transition t : auStd.getTransitions()) {
            String id1 = t.getState1().getId();
            String id2 = t.getState2().getId();
            if (!t.getState1().equals(d2) && !t.getState2().equals(d2)) {
                resultat.addTransition(resultat.getState(id1), resultat.getState(id2), t.getLettre());
            }

            if (t.getState2().equals(d2) && !t.getState1().equals(d2)) {
                for (State st1 : automate1.getAcceptableStates()) {
                    id2 = st1.getId();
                    resultat.addTransition(resultat.getState(id1), resultat.getState(id2), t.getLettre());
                }
            } else if (t.getState2().equals(d2) && t.getState1().equals(d2)) {
                for (State st1 : automate1.getAcceptableStates()) {
                    id2 = st1.getId();
                    resultat.addTransition(resultat.getState(id2), resultat.getState(id2), t.getLettre());
                }
            }
        }

        for (State st : automate1.getAcceptableStates()) {
            for (Transition t : auStd.getTransitions()) {
                String id1 = st.getId();
                String id2 = t.getState2().getId();
                if (t.getState1().equals(d2) && !t.getState2().equals(d2)) {
                    resultat.addTransition(resultat.getState(id1), resultat.getState(id2), t.getLettre());
                }
            }
        }
        return resultat;
    }
}
