package automate;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.ui.graphicGraph.stylesheet.StyleConstants.Units;
import org.graphstream.ui.spriteManager.Sprite;
import org.graphstream.ui.spriteManager.SpriteManager;

import automate.State;
/*
 * Auteur Rousseau Pierre 
 * Dans le cadre du tpe master 1 informatique universit� Le Havre.
 * Classe ayant pour but d'habiller un automate a l'aide de sprite.
 * 
 */
public  class WearingSprite {
	static final String INITIAL="INITIAL_SPRITE";
	static final String FINAL="FINAL_SPRITE";
	
	static double x_initiale=-22, y_initiale=0, z_initiale=0;
	static double x_final=0 ,y_final=0, z_final=0; 
	
	static String styleInitiale =  "size:25px;fill-mode: image-scaled; fill-image: url('ressource/images/createTransition.png');";
	static String styleFinale = "size: 15px;shape: circle; stroke-mode:plain;fill-color:white;";
	static String styleEdge = "size:0px;text-size:15px;fill-color:white;text-alignment:above;";
	
	
	public static void wearAutomate(Graph graph){
		String style =" graph {fill-color: white;} " +
				"node {size: 20px;shape: circle; stroke-mode:plain;fill-color:white; } " +
				"edge{fill-color:black; arrow-size:5px; text-alignment:under; text-size:15px;}";
		
		System.setProperty("gs.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		graph.addAttribute("ui.quality");
		graph.addAttribute("ui.antialias");	
		
		graph.addAttribute("ui.stylesheet", style);
	}
	/*
	 * M�thode statique ayant pour but de mettre un sprite en forme de fl�che sur le
	 * state pass� en param�tre. Bien veillez a avoir l'image nomm�e dans la constante
	 * styleInitiale disponible.
	 * 
	 * Lors de la suppr�ssion d'un �tat il faut imp�rativement appeler la m�thode 
	 * removeAllSprite apr�s ou avant la suprr�ssion de l'�tat.
	 */
	public static void putInitialSprite(SpriteManager spriteManager, State state){
		Sprite s = spriteManager.addSprite(INITIAL+state.getId());
		s.attachToNode(state.getId());
		s.setPosition(Units.PX, x_initiale,y_initiale,z_initiale);
		s.addAttribute("ui.style",styleInitiale);
		
	}
	
	/*
	 * M�thode statique ayant pour but de mettre un sprite en double cercle sur un 
	 * �tat. On retrouve les m�mes sp�cifit� que pour la m�thode pr�c�dente.
	 */
	public static void putAcceptableSprite(SpriteManager spriteManager, State state){
		Sprite s = spriteManager.addSprite(FINAL+state.getId());
		s.attachToNode(state.getId());
		s.addAttribute("label",state.getId());
		s.setPosition(Units.PX,x_final,y_final,z_final);
		s.addAttribute("ui.style", styleFinale);
		
		
	}
	
	/*
	 * M�thode statique a appel� apr�s la suprr�sion d'un �tat car sinon les sprites
	 * vont rester dans le graphe.On doit pass� en param�tre le SpriteManager de l'automate
	 * l'id de l'�tat et deux boolean indicant quel sprite enlev�.
	 */
	public static void removeAllSprite(SpriteManager spriteManager, String state,boolean initial,boolean acceptable){
		if(initial){
			Sprite s=spriteManager.getSprite(INITIAL+state);
			s.detach();
			spriteManager.removeSprite(s.getId());
		}
		if(acceptable){
			Sprite s=spriteManager.getSprite(FINAL+state);
			s.detach();
			spriteManager.removeSprite(s.getId());
		}
	}
	
	/*
	 * M�thode permettant de mettre deux sprites sur un �tat de facon a repr�senter un
	 * �tat InitialAcceptable
	 */
	public static void putInitialAcceptableSprite(SpriteManager spriteManager, State state){
		Sprite sInitial = spriteManager.addSprite(INITIAL+state.getId());
		sInitial.attachToNode(state.getId());
		sInitial.setPosition(Units.PX, x_initiale,y_initiale,z_initiale);
		sInitial.addAttribute("ui.style",styleInitiale);

		Sprite sFinal = spriteManager.addSprite(FINAL+state.getId());
		sFinal.attachToNode(state.getId());
		sFinal.addAttribute("label",state.getId());
		sFinal.setPosition(Units.PX,x_final,y_final,z_final);
		sFinal.addAttribute("ui.style", styleFinale);
		
	}
	
	/*
	 * M�thode statique permettant de fixer les lettres sur les ar�tes.
	 * Le param�tre string repr�sente toutes les lettres a mettre sur l'ar�te.
	 */
	public static void putSpriteOnEdge(SpriteManager spriteManager, Edge e, String chaine){
		Sprite s = spriteManager.addSprite(e.getId());
		s.attachToEdge(e.getId());
		s.addAttribute("label", chaine);
		s.setPosition(0.5);
		s.addAttribute("ui.style", styleEdge);
	}
	
	/*
	 * M�thode servant a supprimer le sprite d'une ar�te
	 */
	public static void removeSpriteOnEdge(SpriteManager spriteManager, Edge e){
		Sprite s=spriteManager.getSprite(e.getId());
		s.detach();
		spriteManager.removeSprite(s.getId());
	
	}
}
