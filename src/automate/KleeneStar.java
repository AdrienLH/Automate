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
        resultat = null;
    }

    public Automate kleeneStar(Automate automate) {
        
        resultat=new Automate("("+automate.getId()+")"+"*");
        for(State st: automate.getStates()){
            resultat.addState(st.getId());
        }
        
        for(Transition t: automate.getTransitions()){
            String id1=t.getState1().getId();
            String id2=t.getState2().getId();
            resultat.addTransition(resultat.getState(id1), resultat.getState(id2), t.getLettre());
        }
        
        State init = resultat.addInitialState("I");
        
        for (State st : automate.getInitialStates()) {
            String id=st.getId();
            resultat.addTransition(init, resultat.getState(id), '#');
        }
        for (State st : automate.getAcceptableStates()) {
            String id=st.getId();
            resultat.addTransition(resultat.getState(id), init, '#');
        }

        for (State st : resultat.getInitialStates()) {
            if(!init.equals(st)){
                resultat.addTransition(init, st, '#');
            }
            st.setInitial(false);
        }
        return resultat;
    }
}
