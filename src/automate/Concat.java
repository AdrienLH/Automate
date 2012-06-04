/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automate;

import java.util.ArrayList;

/**
 *
 * @author adrien
 * TPE Master 1
 * AS 2011-2012
 * Classe permettant d'effectuer la concatenation de deux automates
 */
public class Concat {

    Automate resultat;
    private String id_automate="";
    public Concat() {
        
    }
    /**
     * @return Automate resultat de la concatenation de deux automates
     * Méthode permettant de calculer la concatenation de deux automates
     */
    public Automate concat(Automate automate1, Automate automate2) {
        Standardisation stdOp = new Standardisation();
        Automate auStd = new Automate(automate2.getId());
        id_automate=automate1.getId() + automate2.getId();
        resultat = new Automate(id_automate);
        
        Automate automate_tmp = new Automate(automate2.getId());
        automate2=stdOp.standardise(automate2);
        int id_depart = Integer.valueOf(automate1.getNodeCount()+ "");

        automate_tmp=automate2.numeroter(id_depart-1);

        if (automate_tmp.getInitialStates().size() != 1) {
            auStd = stdOp.standardise(automate_tmp);
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
            if (st.isAcceptable() && !st.equals(d2) && resultat.getState(st.getId())==null) {
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
