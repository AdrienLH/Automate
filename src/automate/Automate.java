package automate;

import automate.State;
import automate.Transition;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

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
    private NodeFactory  nf;

    public Automate(String id) {
        super(id);
        states = new ArrayList<State>();
        sigma = new HashSet<Character>();
        transitions = new HashSet<Transition>();
        initialStates = new ArrayList<State>();
        acceptableStates = new ArrayList<State>();

        this.addAttribute("sigma", sigma);
        this.addAttribute("ui.stylesheet", stylesheet);
        nf=this.nodeFactory();
        
        this.addAttribute("transitions",transitions);
        this.addAttribute("states", states);
    }
    /*
     * Ajout d'un etat au sein de l'automate
     */

    public State addState(State st) {
        st.addAttribute("ui.class", "state");
        st.addAttribute("label", st.getId());
        this.addNode(st.getId());
        states.add(st);
        return st;
    }

    public State addState() {
        String id = this.getNodeCount() + "";
        State st=this.addNode(id);
        st.addAttribute("ui.class", "state");
        st.addAttribute("label", id);
        
        states.add(st);
        return st;
    }
    /*
     * Suppression d'un etat au sein de l'automate
     */

    public void removeState(State state) {

        this.removeNode(state);
    }

    /*
     * Ajout d'un état final au sein de l'automate
     */
    public State addAcceptableState() {
        String id = this.getNodeCount() + "";
        State sFinal=this.addNode(id);
        sFinal.addAttribute("acceptable", "acceptable");
        sFinal.addAttribute("ui.class", "acceptable");
        sFinal.addAttribute("label", id);
        acceptableStates.add(sFinal);
        states.add(sFinal);
        return sFinal;
    }

    public State addAcceptableState(State st) {
        st.addAttribute("acceptable", "acceptable");
        st.addAttribute("ui.class", "acceptable");
        st.addAttribute("label", st.getId());
        this.addNode(st.getId());
        acceptableStates.add(st);
        states.add(st);
        return st;
    }

    /*
     * Ajout d'un état initial au sein de l'automate
     */
    public State addInitialState() {
        String id = this.getNodeCount() + "";
        State sInitial =this.addNode(id);
        sInitial.addAttribute("initial", "initial");
        sInitial.addAttribute("ui.class", "initial");
        sInitial.addAttribute("label", id);
        
        initialStates.add(sInitial);
        states.add(sInitial);
        return sInitial;
    }

    public State addInitialState(State st) {
        st.addAttribute("initial", "initial");
        st.addAttribute("ui.class", "initial");
        st.addAttribute("label", st.getId());
        this.addNode(st.getId());
        initialStates.add(st);

        states.add(st);
        return st;
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
        sigma.remove(lettre);
    }

    public void addTransition(State a, State b, Character lettre) {
        if (sigma.contains(lettre)) {
            if (a.hasEdgeToward(b)) {
                Edge e = a.getEdgeBetween(b);
                HashSet liste = e.getAttribute("lettres");
                liste.add(lettre);
                e.addAttribute("label", e.getAttribute("lettres").toString());
            } else {
                String id = a.getId() + "-" + b.getId();
                Edge e = this.addEdge(id, a, b, true);
                HashSet liste = new HashSet();
                liste.add(lettre);
                e.addAttribute("lettres", liste);
                e.addAttribute("label", e.getAttribute("lettres").toString());
            }
            Transition t = new Transition(a, b, lettre);
            transitions.add(t);
        } else {
            sigma.add(lettre);
            if (a.hasEdgeToward(b)) {

                Edge e = a.getEdgeBetween(b);
                HashSet liste = e.getAttribute("lettres");
                liste.add(lettre);
                e.addAttribute("label", e.getAttribute("lettres").toString());
            } else {
                String id = a.getId() + "-" + b.getId();
                Edge e = this.addEdge(id, a, b, true);
                HashSet liste = new HashSet();
                liste.add(lettre);
                e.addAttribute("lettres", liste);
                e.addAttribute("label", e.getAttribute("lettres").toString());
            }
            Transition t = new Transition(a, b, lettre);
            transitions.add(t);
        }

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
                    tmp.add((State) node);
                }
            }
            initialStates = tmp;

            //Récupération des états finaux
            tmp = new ArrayList<State>();
            for (Node node : this.getEachNode()) {
                String s = node.getAttribute("final");
                if (s != null && s.equals("final")) {
                    tmp.add((State) node);
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
            transitions.addAll((Collection)this.getAttribute("transitions"));
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
    public State addNode(String id){
       State st=(State)nf.newInstance(id, this);
       super.addNode(st.getId());
       return st;
    }
    
    @Override
    public void addNodeCallback(AbstractNode st){
        super.addNodeCallback((State)nf.newInstance(st.getId(), this));
    }
    @Override
    public NodeFactory<? extends State> nodeFactory(){
        NodeFactory nodeF= new NodeFactory() {

            @Override
            public State newInstance(String id, Graph au) {
                //throw new UnsupportedOperationException("Not supported yet.");
                State st=new State((Automate)au, id, false, false);
                return st;
            }
        };
        
        return nodeF;
        
    }
    
    protected static String stylesheet =
            "graph {padding :30;}"
            + "node.acceptable {fill-color: yellow; size: 20px;}"
            + "node.initial {fill-color: red; size: 20px;}"
            + "node.state {fill-color: green; size: 20px;}"
            + "edge{text-alignement: center}";
}
