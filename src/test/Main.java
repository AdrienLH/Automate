/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import automate.State;
import automate.Automate;
import automate.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.graphstream.graph.Node;

/**
 *
 * @author Adrien Uwindekwe
 */
public class Main {

    public static void main(String[] arg) {
        Automate automate1 = new Automate("a(ba)*");
        State init1 = automate1.addInitialState(new State(automate1, "0", true, false));
        State finale = automate1.addAcceptableState(new State(automate1, "1", false, true));
        automate1.addLettre('a');
        automate1.addLettre('b');
        automate1.addTransition(init1, finale, 'a');
        automate1.addTransition(finale, init1, 'b');

        Automate automate2 = new Automate("ab*");
//        System.setProperty("gs.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
//        try {
//            automate1.write("fichier.dgs");
//        } catch (IOException ex) {
//            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
        State init2 = automate2.addInitialState(new State(automate2, "2", true, false));
        State finale2 = automate2.addAcceptableState(new State(automate2, "3", false, true));
        automate2.addLettre('a');
        automate2.addLettre('b');
        automate2.addTransition(init2, finale2, 'a');
        automate2.addTransition(finale2, finale2, 'b');
        
        System.setProperty("gs.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
        try {
            automate1.write("automate1.dgs");
            automate2.write("automate2.dgs");
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
//        Union u = new Union();
//        KleeneStar kl = new KleeneStar();
//        
        automate1.display();
        automate2.display();
//        kl.kleeneStar(automate1).display();
//        u.union(automate1, automate2).display();
//        try {
//            u.union(automate1, automate2).write("fichier_union.dgs");
//        } catch (IOException ex) {
//            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        kl.kleeneStar(automate).display();

//        try {
//            kl.kleeneStar(automate).write("fichier_op.dgs");
//
//        } catch (IOException ex) {
//            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        try {
//            automate1.read("fichier.dgs");
//        } catch (Exception e) {
//        }
//        System.setProperty("gs.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
//        automate1.display();
    }
}
