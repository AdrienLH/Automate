/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automate;

import automate.Automate;
import automate.State;
import automate.Transition;
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
        resultat = null;
    }

    public Automate union(Automate automate1, Automate automate2) {

        resultat = new Automate("(" + automate1.getId() + ")" + " U (" + automate2.getId() + ")");
        
        Automate automate_tmp = new Automate(automate2.getId());
        int id_depart = Integer.valueOf(automate1.getNodeCount()+"");
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
        
        
        if (automate1.equals(automate2)) {
            resultat=automate1.getCopy();
            return resultat;
        } else {
            //Union automate1 et automate_tmp dans resultat
            for (State st : automate1.getStates()) {
                if (st.isAcceptable() && !st.isInitial()) {
                    resultat.addAcceptableState(st.getId());
                } else if (st.isInitial() && !st.isAcceptable()) {
                    resultat.addInitialState(st.getId());
                } else if (st.isAcceptable() && st.isInitial()) {
                    resultat.addInitialAcceptableState(st.getId());
                } else {
                    resultat.addState(st.getId());
                }
            }

            for (State st : automate_tmp.getStates()) {
                if (st.isAcceptable() && !st.isAcceptable()) {
                    resultat.addAcceptableState(st.getId());
                } else if (st.isInitial() && !st.isInitial()) {
                    resultat.addInitialState(st.getId());
                } else if (st.isAcceptable() && st.isInitial()) {
                    resultat.addInitialAcceptableState(st.getId());
                } else {
                    resultat.addState(st.getId());
                }
            }

            for (Transition t : automate1.getTransitions()) {
                String id1 = t.getState1().getId();
                String id2 = t.getState2().getId();
                resultat.addTransition(resultat.getState(id1), resultat.getState(id2), t.getLettre());
            }

            for (Transition t : automate_tmp.getTransitions()) {
                String id1 = t.getState1().getId();
                String id2 = t.getState2().getId();
                resultat.addTransition(resultat.getState(id1), resultat.getState(id2), t.getLettre());
            }

            return resultat;
        }
    }
}
