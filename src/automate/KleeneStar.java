/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automate;

import automate.Automate;
import automate.State;
import automate.State;
import automate.Transition;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author adrien
 * TPE Master 1
 * AS 2011-2012
 * Classe permettant de calculer l'étoile d'un automate
 */
public class KleeneStar {

    private Automate resultat;

    public KleeneStar() {
        resultat = null;
    }

    public Automate kleeneStar(Automate automate) {

        resultat = new Automate("(" + automate.getId() + ")" + "*");
        Automate std = new Automate(automate.getId());
        if (automate.getInitialStates().size() > 1) {//Ici on verifie si l'automate dont on veut calculer son etoile est standard
            //Si non on le standardise
            Standardisation stdOp = new Standardisation();
            std = stdOp.standardise(automate);
        } else {
            std = automate.getCopy();
        }
        for (State st : std.getStates()) {
            if (st.isAcceptable()) {
                resultat.addAcceptableState(st.getId());
            } else if (st.isInitial()) {
                resultat.addInitialAcceptableState(st.getId());
            } else if (st.isAcceptable() && st.isInitial()) {
                resultat.addInitialAcceptableState(st.getId());
            } else {
                resultat.addState(st.getId());
            }
        }

        for (Transition t : std.getTransitions()) {
            String id1 = t.getState1().getId();
            String id2 = t.getState2().getId();
            resultat.addTransition(resultat.getState(id1), resultat.getState(id2), t.getLettre());
        }

        for (State st : std.getAcceptableStates()) {
            for (Transition t : std.getTransitions()) {
                if (t.getState1().isInitial()) {
                    String id1 = st.getId();
                    String id2 = t.getState2().getId();
                    resultat.addTransition(resultat.getState(id1), resultat.getState(id2), t.getLettre());
                }
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
