/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automate;

import automate.State;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author Adrien
 */
public class KleeneStar {

    private Automate resultat;

    public KleeneStar() {
        resultat = new Automate("Etoile");
    }

    public Automate kleeneStar(Automate automate) {
        resultat = automate;
        State init = resultat.addInitialState(new State(resultat, "("+automate.getId() +")"+ "*", true, true));

        for (State st : resultat.getAcceptableStates()) {
            resultat.addTransition(st, init, '#');
        }

        for (State st : resultat.getInitialStates()) {
            if(!init.equals(st)){
                resultat.addTransition(init, st, '#');
            }
            st.setStart(false);
        }
        return resultat;
    }
}
