package automate;

import automate.State;
import automate.Transition;
import java.io.IOException;
import java.util.*;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.AbstractNode;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.stream.GraphParseException;

public class Automate extends MultiGraph {

    private ArrayList<State> states;
    private HashSet<Character> sigma;
    private HashSet<Transition> transitions;
    private ArrayList<State> initialStates;
    private ArrayList<State> acceptableStates;
    private NodeFactory nf;

    public Automate(String id) {
        super(id);
        System.setProperty("gs.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
        states = new ArrayList<State>();
        sigma = new HashSet<Character>();
        transitions = new HashSet<Transition>();
        initialStates = new ArrayList<State>();
        acceptableStates = new ArrayList<State>();

        this.addAttribute("sigma", sigma);
        this.addAttribute("ui.quality");
        this.addAttribute("ui.antialias");
//        this.addAttribute("ui.stylesheet", stylesheet);
        nf = this.nodeFactory();

        this.addAttribute("transitions", transitions);
        this.addAttribute("states", states);
        this.addAttribute("initialStates", initialStates);
        this.addAttribute("acceptableStates", acceptableStates);

    }
    /*
     * Ajout d'un etat au sein de l'automate
     */

    public State addState(String id) {
        State st = this.addNode(id);
        st.addAttribute("label", st);
        states.add(st);
        return st;
    }

    public State addState() {
        String id = this.getNodeCount() + "";
        State st = this.addNode(id);
        st.addAttribute("label", id);

        states.add(st);
        return st;
    }
    /*
     * Suppression d'un etat au sein de l'automate
     */

    public void removeState(State state) {

        if (acceptableStates.contains(state)) {
            acceptableStates.remove(state);
        }
        if (initialStates.contains(state)) {
            initialStates.remove(state);
        }
        if (states.contains(state)) {
            states.remove(state);
        }


        for (Iterator<Transition> it = transitions.iterator(); it.hasNext();) {
            Transition t = it.next();
            if ((t.getState1().equals(state) || t.getState2().equals(state))) {
                it.remove();
            }
        }


        this.removeNode(state);
    }

    /*
     * Ajout d'un état final au sein de l'automate
     */
    public State addAcceptableState() {
        String id = this.getNodeCount() + "";
        State sFinal = this.addNode(id);
        sFinal.addAttribute("label", id);
        sFinal.addAttribute("ui.class", "acceptable");
        sFinal.setAcceptable(true);
        acceptableStates.add(sFinal);
        states.add(sFinal);
        return sFinal;
    }

    public State addAcceptableState(String id) {
        State st = this.addNode(id);
        st.setAcceptable(true);
        st.addAttribute("label", st.getId());
        st.addAttribute("ui.class", "acceptable");
        st.setAcceptable(true);
        acceptableStates.add(st);
        states.add(st);
        return st;
    }

    public State addInitialAcceptableState() {
        String id = this.getNodeCount() + "";
        State sFinal = this.addNode(id);
        sFinal.addAttribute("label", id);
        sFinal.addAttribute("ui.class", "initial_acceptable");
        sFinal.setInitial(true);
        sFinal.setAcceptable(true);
        acceptableStates.add(sFinal);
        initialStates.add(sFinal);
        states.add(sFinal);
        return sFinal;
    }

    public State addInitialAcceptableState(String id) {
        State sFinal = this.addNode(id);
        sFinal.addAttribute("label", id);
        sFinal.addAttribute("ui.class", "initial_acceptable");
        sFinal.setInitial(true);
        sFinal.setAcceptable(true);
        acceptableStates.add(sFinal);
        initialStates.add(sFinal);
        states.add(sFinal);
        return sFinal;
    }

    /*
     * Ajout d'un état initial au sein de l'automate
     */
    public State addInitialState() {
        String id = this.getNodeCount() + "";
        State sInitial = this.addNode(id);
        sInitial.addAttribute("label", id);
        sInitial.addAttribute("ui.class", "initial");
        sInitial.setInitial(true);
        initialStates.add(sInitial);
        states.add(sInitial);
        return sInitial;

    }

    public State addInitialState(String id) {
        State st = this.addNode(id);
        st.addAttribute("ui.class", "initial");
        st.setInitial(true);
        st.addAttribute("label", st.getId());

        initialStates.add(st);

        states.add(st);
        return st;
    }

    public State getState(String id) {
        return (State) super.getNode(id);
    }

    public ArrayList<State> getStates() {
        return states;
    }

    public ArrayList<State> getInitialStates() {

        return initialStates;
    }

    public ArrayList<State> getAcceptableStates() {

        return acceptableStates;
    }

    public HashSet<Character> getSigma() {
        return sigma;
    }

    public void addLettre(Character lettre) {
        sigma.add(lettre);
    }

    public void removeLettre(Character lettre) {

        for (Iterator<Transition> itt = transitions.iterator(); itt.hasNext();) {
            Transition t = itt.next();
            if (t.getLettre() == lettre) {
                itt.remove();
            }
        }
        Collection<Edge> alE = this.getEdgeSet();
        HashSet liste = new HashSet();
        for (Edge e : getEachEdge()) {
            if (e.getAttribute("lettres") != null) {
                liste = e.getAttribute("lettres");
                if (liste.contains(lettre)) {
                    liste.remove(lettre);
                    if (liste.isEmpty()) {
                        this.removeEdge(e);
                    }
                }
            }
            e.addAttribute("lettres", liste);
            String label = e.getAttribute("lettres").toString();
            label = label.substring(1, label.length() - 1);
            e.addAttribute("label", label);
        }
        sigma.remove(lettre);
    }

    public void removeTransition(Transition t) {
        HashSet liste = new HashSet();
        for (Edge e : getEachEdge()) {
            if (e.getAttribute("lettres") != null) {
                liste = e.getAttribute("lettres");
                if (liste.contains(t.getLettre())) {
                    liste.remove(t.getLettre());
                    if (liste.isEmpty()) {
                        this.removeEdge(e);
                    }
                }
            }
            if (e != null) {
                e.addAttribute("lettres", liste);
                String label = e.getAttribute("lettres").toString();
                label = label.substring(1, label.length() - 1);
                e.addAttribute("label", label);
            }
        }
        if (transitions.contains(t)) {
            transitions.remove(t);
        }
    }

    public Transition addTransition(State a, State b, Character lettre) {
        if (sigma.contains(lettre)) {
            if (a.hasEdgeToward(b)) {
                Edge e = a.getEdgeBetween(b);
                HashSet liste = e.getAttribute("lettres");
                liste.add(lettre);
                e.addAttribute("lettres", liste);
                String label = e.getAttribute("lettres").toString();
                label = label.substring(1, label.length() - 1);
                e.addAttribute("label", label);
            } else {
                String id = a.getId() + "-" + b.getId();
                Edge e = this.addEdge(id, a, b, true);
                HashSet liste = new HashSet();
                liste.add(lettre);
                e.addAttribute("lettres", liste);
                String label = e.getAttribute("lettres").toString();
                label = label.substring(1, label.length() - 1);
                e.addAttribute("label", label);
            }
            Transition t = new Transition(a, b, lettre);
            transitions.add(t);
            return t;
        } else {
            sigma.add(lettre);
            if (a.hasEdgeToward(b)) {

                Edge e = a.getEdgeBetween(b);
                HashSet liste = e.getAttribute("lettres");
                liste.add(lettre);
                e.addAttribute("lettres", liste);
                String label = e.getAttribute("lettres").toString();
                label = label.substring(1, label.length() - 1);
                e.addAttribute("label", label);
            } else {
                String id = a.getId() + "-" + b.getId();
                Edge e = this.addEdge(id, a, b, true);
                HashSet liste = new HashSet();
                liste.add(lettre);
                e.addAttribute("lettres", liste);
                String label = e.getAttribute("lettres").toString();
                label = label.substring(1, label.length() - 1);
                e.addAttribute("label", label);
            }
            Transition t = new Transition(a, b, lettre);
            transitions.add(t);
            return t;
        }

    }

    public Automate getCopy() {
        Automate copy = new Automate(this.getId());
        for (State st : this.getStates()) {
            if (st.isAcceptable() && !st.isInitial()) {
                copy.addAcceptableState(st.getId());
            } else if (st.isInitial() && !st.isAcceptable()) {
                copy.addInitialState(st.getId());
            } else if (st.isAcceptable() && st.isInitial()) {
                copy.addInitialAcceptableState(st.getId());
            } else {
                copy.addState(st.getId());
            }
        }

        for (Transition t : this.getTransitions()) {
            String id1 = t.getState1().getId();
            String id2 = t.getState2().getId();
            copy.addTransition(copy.getState(id1), copy.getState(id2), t.getLettre());
        }

        return copy;

    }

    public HashSet<Transition> getTransitions() {
        return transitions;
    }

    public void setAcceptableStates(ArrayList<State> acceptableStates) {
        this.acceptableStates = acceptableStates;
    }

    public void setInitialStates(ArrayList<State> startStates) {
        this.initialStates = startStates;
    }

    public void setStates(ArrayList<State> states) {
        this.states = states;
    }

    public void setSigma(HashSet<Character> sigma) {
        this.sigma = sigma;
    }

    public void setTransitions(HashSet<Transition> transitions) {
        this.transitions = transitions;
    }

    @Override
    public void read(String filename) {
        try {
            super.read(filename);

            //Récuperation des états
            ArrayList<State> tmp = new ArrayList<State>();
            for (Node st : this.getEachNode()) {
                tmp.add((State) st);
            }
            states = tmp;

            //Récupération des états initiaux
            tmp = new ArrayList<State>();
            for (Node node : this.getEachNode()) {
                String s = node.getAttribute("initial");
                if (s != null && s.equals("initial")) {
                    State st = (State) node;
                    st.setInitial(true);
                    tmp.add(st);
                }
            }
            initialStates = tmp;

            //Récupération des états finaux
            tmp = new ArrayList<State>();
            for (Node node : this.getEachNode()) {
                String s = node.getAttribute("acceptable");
                if (s != null && s.equals("acceptable")) {
                    State st = (State) node;
                    st.setAcceptable(true);
                    tmp.add(st);
                }
            }
            acceptableStates = tmp;

            //Récupération de l'alphabet sigma
            HashSet<Character> tmpH = new HashSet<Character>();
            String chaine = this.getAttribute("sigma");

            for (int i = 0; i < chaine.length(); i++) {
                if (i != 0 && i != chaine.length() - 1) {
                    Character character = chaine.charAt(i);
                    if (!character.equals(';') && !character.equals(',') && !character.equals(' ')) {
                        tmpH.add(character);
                    }
                }
            }

            sigma = tmpH;
            removeAttribute("sigma");
            addAttribute("sigma", sigma);

            //Récupération des transitions
            for (Edge e : this.getEachEdge()) {
                State a = e.getNode0();
                State b = e.getNode1();

                String string = e.getAttribute("lettres");
                for (int i = 0; i < string.length(); i++) {
                    if (i != 0 && i != string.length() - 1) {
                        Character character = string.charAt(i);
                        if (!character.equals(';') && !character.equals(',') && !character.equals(' ')) {
                            Transition t = new Transition(a, b, character);
                            transitions.add(t);


                        }
                    }
                }

            }


        } catch (IOException ex) {
            Logger.getLogger(Automate.class.getName()).log(Level.SEVERE, null, ex);
        } catch (GraphParseException ex) {
            Logger.getLogger(Automate.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ElementNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Override
    public State addNode(String id) {
//        State st = (State) nf.newInstance(id, this);
        super.addNode(id);
        return this.getState(id);
    }

    @Override
    public void addNodeCallback(AbstractNode st) {
        super.addNodeCallback((State) nf.newInstance(st.getId(), this));
    }

    @Override
    public NodeFactory<? extends State> nodeFactory() {
        NodeFactory nodeF = new NodeFactory() {

            @Override
            public State newInstance(String id, Graph au) {
                //throw new UnsupportedOperationException("Not supported yet.");
                State st = new State((Automate) au, id, false, false);
                return st;
            }
        };
        return nodeF;

    }
}
