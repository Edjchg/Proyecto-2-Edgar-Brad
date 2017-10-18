import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Map;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.internal.compiler.env.ICompilationUnit;


public class Parser {
	
	public static void parseFile(String filePath) {
	    System.out.println("Starting to parse " + filePath);
	    File javaFile = new File(filePath);
	    BufferedReader in = new BufferedReader(new FileReader(javaFile));
	    final StringBuffer buffer = new StringBuffer();
	    String line = null;
	    while (null != (line = in.readLine())) {
	         buffer.append(line).append("\n");
	    }
	    ASTParser parser = ASTParser.newParser(AST.JLS3);
	    parser.setKind(ASTParser.K_COMPILATION_UNIT);
	    parser.setSource(buffer.toString().toCharArray());
	    parser.setResolveBindings(true);
	    ICompilationUnit icu=JavaCore.createCompilationUnitFrom(file);
	    CompilationUnit cu = (CompilationUnit) parser.createAST(null);

	    ASTNode root = cu.getRoot();
	    cu.accept(new ASTVisitor() {

	        @Override
	        public boolean visit(MethodDeclaration node) {
	            return true;
	        }

	        @Override
	        public void endVisit(MethodDeclaration node) {
	            System.out.println("Method " + node.getName().getFullyQualifiedName() + " is visited");
	        }

	    });
	}

}
