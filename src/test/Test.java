package test;

import automate.*;
import javax.swing.JFrame;

import org.graphstream.ui.swingViewer.View;
import org.graphstream.ui.swingViewer.Viewer;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Test{

    public static void main(String args[]) {

        /**
         * ********************Construction des automate a et b
         * ***************************************
         */
        Automate a = new Automate("cou");
        Automate b = new Automate("id");

        State st0 = a.addInitialState();
        State st1 = a.addAcceptableState();



        Transition t0 = a.addTransition(st0, st1, 'a');
        Transition t01 = a.addTransition(st0, st0, 'b');
        

        State st2 = b.addInitialAcceptableState();
        State st3 = b.addState();
        State st4 = b.addInitialState();
        

        Transition tb0 = b.addTransition(st2, st3, 'a');
        Transition tb1 = b.addTransition(st3, st4, 'b');
        Transition tb2 = b.addTransition(st4, st2, 'a');
        Transition tb3 = b.addTransition(st4, st4, 'a');
        
        try {
            //        Transition tb2 = b.addTransition(st3, st3, 'c');
                    b.write("b.dgs");
        } catch (IOException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        /**
         * ******************************Standardisation de a
         * **************************************
         */
        Standardisation stdOp = new Standardisation();
        Automate stdB = stdOp.standardise(b);
//        


        /**
         * *******************************Etoile de l'automate
         * a****************************************
         */
        Automate etoile = new Automate("etoile");

        KleeneStar kl = new KleeneStar();
        etoile = kl.kleeneStar(a);
        
        try {
            etoile.write("aetoile.dgs");
        } catch (IOException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        }

        /**
         * *************************Union des automates a et b
         * ************************
         */
        Union un = new Union();
        Automate auUnion = new Automate(a.getId());
        auUnion = un.union(b, a);
        
        
        
        Automate stdu = stdOp.standardise(auUnion);
        try {
            stdu.write("stdu_.dgs");
        } catch (IOException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        }
        

        /************************Concatenation a.b ******************************/
        Concat ct = new Concat();
        Automate ab = new Automate(a.getId()+b.getId());
        ab = ct.concat(b, a);
        
        
        /***********************Determinisation de l'automate a union b ************/
        Determiniser dt = new Determiniser();
        Automate stduDet = new Automate(a.getId()+b.getId());
        stduDet = dt.determiniser(auUnion);
//        
        
//        
        try {
            ab.write("ba.dgs");
//            stdu.write("aub.dgs");
            stduDet.write("aubDet.dgs");
        } catch (IOException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
        /*===============Affichage des résultats====================*/
//        a.display();
//        b.display();
//        stdB.display();
//        etoile.display();
//        auUnion.display();
        stdu.display();
//        ab.display();
        stduDet.display();
    }


}