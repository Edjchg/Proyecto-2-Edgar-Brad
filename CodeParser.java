package org.proyectoII.MethodsHandler;
import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.proyectoII.ASTguardado.*;

public class CodeParser {
	
	public static void ejecutar() {
		if (ASTSave.getRaiz() != null) {
			ASTSave.getRaiz().limpiarArbol();
		}else {
			IWorkbenchPart ramaTrabajo = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActivePart();
	    		ICompilationUnit CompilerU = null;
	    		try{
	        		IFile archivo = (IFile) ramaTrabajo.getSite().getPage().getActiveEditor().getEditorInput().getAdapter(IFile.class);
	        		CompilerU = (ICompilationUnit) JavaCore.create(archivo);
	        	}catch(Exception e) {
	        		System.err.println("Un error ha ocurrido, debe tener una clase abierta que desee diagramar.");
	        	}	
	        	try {
	        		ASTCreator(CompilerU);
	        		
	        	}catch(JavaModelException exeption){}
		}
	}
	public static void ejecuta2 (ICompilationUnit unidadDeCompilacion) {
		if (ASTSave.getRaiz() != null){
    		ASTSave.getRaiz().limpiarArbol();;
    	}
    	try {
			ASTCreator(unidadDeCompilacion);
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
	}
    	public static void ASTCreator(ICompilationUnit iUnidadCompilacion) throws JavaModelException{
    		CompilationUnit lectorCodigo = parser(iUnidadCompilacion);
    		ASTSave.setCompiler(lectorCodigo);
    		Visitante visitanteClase = new Visitante();
    		lectorCodigo.accept(visitanteClase);
    	}
    	
    	public static CompilationUnit parser(ICompilationUnit unidadDeCompilacion) {
    		ASTParser lectorAnalizador = ASTParser.newParser(AST.JLS3);
    		lectorAnalizador.setSource(unidadDeCompilacion);
    		lectorAnalizador.setResolveBindings(true);
    		lectorAnalizador.setKind(ASTParser.K_COMPILATION_UNIT);
    		return (CompilationUnit) lectorAnalizador.createAST(null);
    		
    		
    	}
	
}
