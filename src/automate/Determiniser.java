/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automate;

import java.util.*;

/**
 *
 * @author adrien
 */
public class Determiniser {

    Automate resultat;

    public Determiniser() {
    }

    public Automate determiniser(Automate automate) {
        State R = null;
        Standardisation stdOp = new Standardisation();
        resultat = new Automate(automate.getId());
//        resultat.addInitialState(au.getInitialStates().get(0).getId());
        Queue<State> T = new LinkedList<State>();
//        for(State st: au.getInitialStates()){
//            resultat.addInitialState(st.getId());
//        }

        Automate auStd = new Automate(automate.getId());
        if (automate.getInitialStates().size() != 1) {
            auStd = stdOp.standardise(automate);
        } else {
            auStd = automate.getCopy();
        }

        T.addAll(auStd.getInitialStates());
        while (!T.isEmpty()) {
            State P = T.remove();
            if (P.getId().split(",").length == 1) {
                if (resultat.getState(P.getId()) == null) {
                    resultat.addState(P.getId());
                }
                for (Character a : auStd.getSigma()) {
                    String id = "";
                    for (Transition t : auStd.getTransitions()) {
                        if (t.getState1().getId().equals(P.getId()) && t.getLettre().equals(a)) {
                            if (id.isEmpty()) {
                                id = t.getState2().getId();
                            } else {
                                id = id + "," + t.getState2().getId();
                            }
                        }
                    }
                    if (!id.isEmpty()) {
                        if (resultat.getState(id) == null) {
                            R = resultat.addState(id);
                        }
                        resultat.addTransition(P, R, a);
                        T.add(R);
                    }
                }

            } else {
                for (String s : P.getId().split(",")) {
                    for (Character a : auStd.getSigma()) {
                        String id = "";
                        for (Transition t : auStd.getTransitions()) {
                            if (t.getState1().equals(auStd.getState(s)) && t.getLettre().equals(a)) {
                                if (id.isEmpty()) {
                                    id = t.getState2().getId();
                                } else {
                                    id = id + "," + t.getState2().getId();
                                }
                            }
                        }
                        if (!id.isEmpty()) {
                            if (resultat.getState(id) == null) {
                                R = resultat.addState(id);
                            }
                            resultat.addTransition(P, R, a);
//                            
                        }
                    }
                    T.add(R);
                }
                
            }
            System.out.println();
        }

        return resultat;
    }
}
