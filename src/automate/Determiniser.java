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



        Queue<State> T = new LinkedList<State>();

        Automate auStd = new Automate(automate.getId());
        if (automate.getInitialStates().size() != 1) {
            auStd = stdOp.standardise(automate);
        } else {
            auStd = automate.getCopy();
        }

        T.addAll(auStd.getInitialStates());
        ArrayList<State> alState = new ArrayList<State>();
        while (!T.isEmpty()) {
            State P = T.remove();


            if (resultat.getState(P.getId()) == null) {
                resultat.addState(P.getId());
            }

            for (String s : P.getId().split(",")) {
                alState.add(auStd.getState(s));
            }

            resultat.getState(P.getId()).addAttribute("visite", "visite");
            HashSet<Character> listChar = new HashSet<Character>();

            String id = "";
            for (State st : alState) {
                for (Character a : auStd.getSigma()) {
                    for (Transition t : auStd.getTransitions()) {
                        if (t.getState1().getId().equals(st.getId()) && t.getLettre().equals(a) && !listChar.contains(a)) {
                            if (id.isEmpty()) {
                                id = t.getState2().getId();
                            } else {
                                id = id + "," + t.getState2().getId();
                            }
                        }
                    }
                    if (!id.isEmpty()) {
                        if (resultat.getState(id) == null) {
                            if(auStd.getState(st.getId()).isAcceptable()){
                                R = resultat.addAcceptableState(id);
                            }else{
                                R = resultat.addState(id);
                            }
                            
                        }
                        resultat.addTransition(P, R, a);
                        listChar.add(a);
                        if (R.getAttribute("visite") == null) {
                            T.add(R);
                        }

                    }
                    id="";
                }
                
                //============Gestion de l'etat puit ===============
//                if(listChar.size()<auStd.getSigma().size()){
//                    if(resultat.getState("#")==null){
//                        resultat.addState("#");
//                    }
//                    
//                    for(Transition t1: resultat.getTransitions()){
//                        if(t1.getState1().getId().equals(P.getId()) && !listChar.contains(t1.getLettre())){
//                            resultat.addTransition(resultat.getState(P.getId()), resultat.getState("#"), t1.getLettre());
//                        }
//                    }
//                }
   
            }
            alState.clear();
        }
        
        
        return resultat;
    }
}
