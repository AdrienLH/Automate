/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import automate.Automate;
import automate.KleeneStar;
import automate.State;
import automate.Union;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Adrien Uwindekwe
 */
public class Main {

    public static void main(String[] arg) {
        Automate automate1 = new Automate("a(ba)*");
        automate1.addAttribute("ui.stylesheet", stylesheet);
        State init1 = automate1.addInitialState("0");
        init1.addAttribute("ui.class", "initial");
        State finale = automate1.addAcceptableState("1");
        finale.addAttribute("ui.class", "acceptable");
        automate1.addLettre('a');
        automate1.addLettre('b');
        automate1.addTransition(init1, finale, 'a');
        automate1.addTransition(finale, init1, 'b');

        Automate automate2 = new Automate("ab*");
//        automate2.addAttribute("ui.stylesheet", stylesheet);
//        State init2 = automate2.addInitialState("2");
//        init2.addAttribute("ui.class", "initial");
//        State finale2 = automate2.addAcceptableState("3");
//        finale2.addAttribute("ui.class", "acceptable");
//        automate2.addLettre('a');
//        automate2.addLettre('b');
//        automate2.addTransition(init2, finale2, 'a');
//        automate2.addTransition(finale2, finale2, 'b');


        try {
            automate1.write("automate1.dgs");
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        automate2.read("automate2.dgs");
//        try {
//            automate2.write("automate2.dgs");
//        } catch (IOException ex) {
//            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
//        }

        Union u = new Union();
        KleeneStar kl = new KleeneStar();
//        
        automate1.display();
        automate2.display();
        
        Automate etoile=kl.kleeneStar(automate1);
        etoile.addAttribute("ui.stylesheet", stylesheet);
        etoile.display();
        
        Automate union=u.union(automate1, automate2);
        union.setAttribute("ui.stylesheet", stylesheet);
        union.display();
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
    protected static String stylesheet =
            "graph {padding :30;}"
            + "node.acceptable {fill-color: yellow; size: 20px;}"
            + "node.initial {fill-color: red; size: 20px;}"
            + "node.state {fill-color: green; size: 20px;}"
            + "edge{text-alignement: center}";
}
