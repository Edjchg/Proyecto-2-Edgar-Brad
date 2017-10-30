package org.proyectoII.MethodsHandler;


import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.proyectoII.ASTguardado.*;


public class Visitante extends ASTVisitor {
	ASTSave raiz = new ASTSave(null, "raiz");
	
	public boolean visitanteMetodo(MethodDeclaration puntoMetodo) {
		if(ASTSave.getRaiz() == null) {
			ASTSave.setRaiz(raiz);
		}
		try {
			ASTSave guardaMetodo = new ASTSave(puntoMetodo, puntoMetodo.getName().toString());
			raiz.agregarHijo(guardaMetodo);
			Block bloqueMetodo = (Block)puntoMetodo.getBody();
			guardaMetodo.agregarNuevoHijo(bloqueMetodo.statements());
		}catch(Exception a) {a.printStackTrace();} 
	
		return super.visit(puntoMetodo);
    }

			
		
}

