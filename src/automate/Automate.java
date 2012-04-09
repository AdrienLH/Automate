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
        nf=this.nodeFactory();
        
        this.addAttribute("transitions",transitions);
        this.addAttribute("states", states);
        this.addAttribute("initialStates", initialStates);
        this.addAttribute("acceptableStates", acceptableStates);
        
    }
    /*
     * Ajout d'un etat au sein de l'automate
     */

    public State addState(String id) {
        State st=null;//this.addNode(id);
        this.addNodeCallback(new State(this, id, false, false));
        st=this.getState(id);
        st.addAttribute("label", st);
        states.add(st);
        return st;
    }

    public State addState() {
        String id = this.getNodeCount() + "";
        State st=this.addNode(id);
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
        sFinal.addAttribute("label", id);
        sFinal.setAcceptable(true);
        acceptableStates.add(sFinal);
        states.add(sFinal);
        return sFinal;
    }

    public State addAcceptableState(String id) {
        State st =null;//this.addNode(id);
        this.addNodeCallback(new State(this, id, false, true));
        st=this.getState(id);
        st.setAcceptable(true);
        st.addAttribute("label", st.getId());
        st.setAcceptable(true);
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
        sInitial.addAttribute("label", id);
        sInitial.setInitial(true);
        initialStates.add(sInitial);
        states.add(sInitial);
        return sInitial;
    }

    public State addInitialState(String id) {
        State st= null;
        this.addNodeCallback(new State(this, id, true, false));
        st=this.getState(id);
        st.setInitial(true);
        st.addAttribute("label", st.getId());
        
        initialStates.add(st);

        states.add(st);
        return st;
    }

    public State getState(String id){
        return (State)super.getNode(id);
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
                String s = node.getAttribute("acceptable");
                if (s != null && s.equals("acceptable")) {
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
    
}
