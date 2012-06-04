/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automate;

import java.util.*;

/**
 *
 * @author adrien
 * TPE Master 1
 * AS 2011-2012
 * Classe permettant d'effectuer la déterminisation d'un automate non deterministe
 */
public class Determiniser {

    Automate resultat;

    public Determiniser() {
    }

    /*
     * Méthode permettant de calculer le determiniser d'un automate quelconque passé en parametre
     */
    public Automate determiniser(Automate automate) {
        State R = null;
        Standardisation stdOp = new Standardisation();
        resultat = new Automate(automate.getId());



        Queue<State> T = new LinkedList<State>();

        Automate auStd = new Automate(automate.getId());
        if (automate.getInitialStates().size() != 1) {
            auStd = stdOp.standardise(automate);
        } else {
            auStd = automate.getCopy();
        }
        
    
        String id_initial = auStd.getInitialStates().get(0).getId();
        T.addAll(auStd.getInitialStates());
        ArrayList<State> alStateP = new ArrayList<State>();
        ArrayList<State> alStateVisite = new ArrayList<State>();
        while (!T.isEmpty()) {
            State P = T.remove();


            if (resultat.getState(P.getId()) == null) {
                if (P.getId().equals(id_initial) && !P.isAcceptable()) {
                    resultat.addInitialState(P.getId());
                } else if (P.getId().equals(id_initial) && P.isAcceptable()){
                    resultat.addInitialAcceptableState(P.getId());
                }else{
                    resultat.addState(P.getId());
                }
            }

            for (String s : P.getId().split(",")) {
                alStateP.add(auStd.getState(s));
            }

            resultat.getState(P.getId()).addAttribute("visite", "visite");
            HashSet<Character> listChar = new HashSet<Character>();

            String id = "";
            boolean acceptable = false;

            for (Character a : auStd.getSigma()) {
                for (State st : alStateP) {
                    for (Transition t : auStd.getTransitions()) {
                        if (t.getState1().getId().equals(st.getId()) && t.getLettre().equals(a) && !listChar.contains(a)) {
                            if (id.isEmpty()) {
                                id = t.getState2().getId();
                            } else {
                                id = id + "," + t.getState2().getId();
                            }
                            if (t.getState2().isAcceptable()) {
                                acceptable = true;
                            }
                        }
                    }
                    if (!id.isEmpty()) {
                        if (resultat.getState(id) == null) {
                            if (acceptable) {
                                R = resultat.addAcceptableState(id);
                            } else {
                                R = resultat.addState(id);
                            }

                        } else {
                            R = resultat.getState(id);
                        }
                        resultat.addTransition(P, R, a);
                        acceptable = false;
                        alStateVisite.add(P);
                        listChar.add(a);
                        if (!alStateVisite.contains(R)) {
                            T.add(R);
                        }
                        id = "";
                    }

                }
                
                //============Gestion de l'etat puit ===============
                if (listChar.size() != auStd.getSigma().size()) {
                    if (resultat.getState("#") == null) {
                        resultat.addState("#");
                        for (Character c : auStd.getSigma()) {
                            resultat.addTransition(resultat.getState("#"), resultat.getState("#"), c);
                        }
                    }
                    for (Character c : auStd.getSigma()) {
                        if (!listChar.contains(a)) {
                            resultat.addTransition(P, resultat.getState("#"), a);
                        }
                    }
                }

            }
            alStateP.clear();
        }

        return resultat;
    }
}
