package org.proyectoII.ASTguardado;

import java.util.ArrayList;
import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;

public class ASTSave {
	public Boolean next;
	static ASTSave raiz;
	String nombre;
	ASTNode nodo;
	List<ASTSave> hijo;
	static CompilationUnit compiler;
	
	public ASTSave(ASTNode temp, ASTSave padre, String nombre) {
		this.nodo = temp;
		this.hijo =  new ArrayList<ASTSave>();
		this.nombre = nombre;
	}
	
	public ASTSave(ASTNode nodo, String nombre) {
		this.nodo = nodo;
		this.hijo = new ArrayList<ASTSave>();
		this.nombre = nombre;	
	}
	
	public ASTSave(ASTNode nodo, Boolean next, String nombre) {
		this(nodo, nombre);
		this.next = next;
			
	}
	
	public Boolean getNext() {
		return next;
	}

	public void setNext(Boolean next) {
		this.next = next;
	}

	public static ASTSave getRaiz() {
		return raiz;
	}

	public static void setRaiz(ASTSave raiz) {
		ASTSave.raiz = raiz;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public ASTNode getNodo() {
		return nodo;
	}

	public void setNodo(ASTNode nodo) {
		this.nodo = nodo;
	}

	public List<ASTSave> getHijo() {
		return hijo;
	}

	public void setHijo(List<ASTSave> hijo) {
		this.hijo = hijo;
	}


	public static void setCompiler(CompilationUnit compiler) {
		ASTSave.compiler = compiler;
	}
	
	public void agregarHijo(ASTSave metodo) {
		hijo.add(metodo);
		
	}
	
	public List<String> getMetodos(){
		List<String> total = new ArrayList<>();
		if (raiz.equals(null)) {
			return null;
		}
		for (ASTSave metodo : raiz.getHijo()) {
			total.add(metodo.getNombre());
			
		}
		return total;
	}
	
	public static ASTSave getNombreMetodos(String nombreMetodo) {
		for (ASTSave nodo: raiz.getHijo()) {
			if (nodo.getNombre().equals(nombreMetodo)) {
				return nodo;
			}
		}
		return null;
	}
	
	public ASTSave encuentraEnLinea(Integer numeroLinea) {
		if (nodo != null & (compiler.getLineNumber(nodo.getStartPosition()) == numeroLinea)) {
			return this;
			
		}else {
			if(hijo.size()!= 0) {
				for (ASTSave hijos : hijo) {
					ASTSave nodoTemporal = hijos.encuentraEnLinea(numeroLinea);
					if (nodoTemporal != null) {
						return nodoTemporal;
					}
				}
			}
		}
		return null;
	
	}
	
	public void agregarNuevoHijo(List<Block> Statments) {
		for (Object declaracion: Statments) {
			ASTNode hijo = (ASTNode) declaracion;
			this.metodoAgregarAux(hijo);
		}
	
	
	}
	public void limpiarArbol() {
		this.hijo.clear();
		ASTSave.setRaiz(null);
	}
	/**
	 * Metodo que pregunta si lo que encuentra es una variable, invocacion de un metodo,
	 * if, while, do while, guardandolos en la lista ASTSave.
	 */
	public void metodoAgregarAux(ASTNode hijo1) {
		String [] obtieneClase = hijo1.getClass().toString().split("\\.");
		String objeto = obtieneClase[obtieneClase.length -1]; 
		
		if (objeto.equalsIgnoreCase("VariableDeclarationStatement")) {
			VariableDeclarationStatement declaracionVariable = (VariableDeclarationStatement) hijo1;
			ASTSave guardaVariable = new ASTSave(declaracionVariable,declaracionVariable.toString());
			this.agregarHijo(guardaVariable);
		}else if(objeto.equalsIgnoreCase("ExpressionStatment")) {
			ExpressionStatement expresion = (ExpressionStatement) hijo1;
			try {
				MethodInvocation metodoInvocado = (MethodInvocation)expresion.getExpression();
				ASTSave guardaMetodo = new ASTSave(metodoInvocado, metodoInvocado.toString());
				this.agregarHijo(guardaMetodo);
			}catch(Exception excepcion) {
				
			}
		}else if (objeto.equalsIgnoreCase("IfStatement")) {
			IfStatement obtieneIf = (IfStatement) hijo;
			ASTSave guardaIf = new ASTSave(obtieneIf,obtieneIf.toString());
			this.agregarHijo(guardaIf);
			ASTSave despuesIf = new ASTSave (null,true, obtieneIf.getExpression().toString());
			guardaIf.agregarHijo(despuesIf);
			Block Bloque1 = (Block) obtieneIf.getThenStatement();
			despuesIf.agregarNuevoHijo(Bloque1.statements());
			
			if (obtieneIf.getElseStatement() instanceof Block) {
				ASTSave guardaElse = new ASTSave(null,false,obtieneIf.getExpression().toString());
				guardaIf.agregarHijo(guardaElse);
				Block Bloque2 = (Block) obtieneIf.getElseStatement();
				guardaElse.agregarNuevoHijo(Bloque2.statements());
			} else {
				ASTSave guardaElse = new ASTSave(null,false, obtieneIf.getExpression().toString());
				guardaIf.agregarHijo(guardaElse);
				IfStatement segundoIf = (IfStatement) obtieneIf.getElseStatement();
				guardaElse.metodoAgregarAux(segundoIf);
			}
		}else if (objeto.equalsIgnoreCase("WhileStatement")) {
			WhileStatement obtieneWhile = (WhileStatement) hijo;
			ASTSave guardaWhile = new ASTSave(obtieneWhile, obtieneWhile.toString());
			this.agregarHijo(guardaWhile);
			Block bloqueWhileCuerpo = (Block) obtieneWhile.getBody();
			guardaWhile.agregarNuevoHijo(bloqueWhileCuerpo.statements());
			
		}else if (objeto.equalsIgnoreCase("ForStatement")) {
			ForStatement obtieneFor = (ForStatement) hijo;
			ASTSave guardaFor = new ASTSave(obtieneFor, obtieneFor.toString());
			this.agregarHijo(guardaFor);
			Block bloqueForCuerpo = (Block) obtieneFor.getBody();
			guardaFor.agregarNuevoHijo(bloqueForCuerpo.statements());
		}
	}
	public void mostrarArbol() {
		if (nodo == null) {
			
		}else {
			String[] Class = nodo.getClass().toString().split("\\.");
			String  linea = Class[Class.length-1];
			if (linea.equalsIgnoreCase("VariableDeclarationStatement")) {
				VariableDeclarationStatement nuevaVariable =  (VariableDeclarationStatement) nodo;
				System.out.println(nuevaVariable.toString());
			}else if (linea.equalsIgnoreCase("ExpressionStatement")) {
				ExpressionStatement expresion = (ExpressionStatement) nodo;
				System.out.println(expresion);
			}else if(linea.equalsIgnoreCase("IfStatement")) {
				IfStatement unIf = (IfStatement) nodo;
				System.out.println(unIf.toString());
			}
		}
		for (ASTSave hijos: hijo) {
			hijos.mostrarArbol();
		}
	}
}