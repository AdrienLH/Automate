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

        Automate automate_tmp = new Automate(automate2.getId());
        int id_depart = Integer.valueOf(automate1.getNodeCount()+ "");
        for (State st : automate2.getStates()) {
            if (st.isAcceptable() && !st.isInitial()) {
                automate_tmp.addAcceptableState(String.valueOf(Integer.valueOf(st.getId())+id_depart));
            } else if (st.isInitial() && !st.isAcceptable()) {
                automate_tmp.addInitialState(String.valueOf(Integer.valueOf(st.getId())+id_depart));
            } else if (st.isAcceptable() && st.isInitial()) {
                automate_tmp.addInitialAcceptableState(String.valueOf(Integer.valueOf(st.getId())+id_depart));
            } else {
                automate_tmp.addState(String.valueOf(Integer.valueOf(st.getId())+id_depart));
            }
        }

        for (Transition t : automate2.getTransitions()) {
            int id1 = Integer.valueOf(t.getState1().getId());
            int id2 = Integer.valueOf(t.getState2().getId());
            automate_tmp.addTransition(automate_tmp.getState(String.valueOf(id_depart+id1)), automate_tmp.getState(String.valueOf(id_depart+id2)), t.getLettre());
        }


        if (automate2.getInitialStates().size() != 1) {
            auStd = stdOp.standardise(automate2);
        } else {
            auStd = automate_tmp.getCopy();
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
