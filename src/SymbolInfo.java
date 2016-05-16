import java.util.ArrayList;

/**************************************************
*  class used to hold information associated w/
*  Symbs (which are stored in SymbolTables)
*  Update to handle arrays and methods
*
****************************************************/

class SymbolInfo extends Symb {
	
 public ASTNode.Kinds kind; // Should always be Var in CSX-lite
 public ASTNode.Types type; // Should always be Integer or Boolean in CSX-lite
 public ArrayList<String> paramaterList = new ArrayList<String> ();
 public int size;
 
public String methodReturnCode;
 public ASTNode.AdrMode adr;
 public int varIndex;
 public String label;
 public int intval;
 public String strVal;
 
 public String topLabel;
 public String bottomLabel;
 
 public int numberOfLocals;
 

 public SymbolInfo(String id, ASTNode.Kinds k, ASTNode.Types t){    
	super(id);
	kind = k; type = t;};
public SymbolInfo(String id, int k, ASTNode.Types a){
	super(id);
	size = k;
	kind = ASTNode.Kinds.Array; 
	type = a;
	};
 public String toString(){
             return "("+name()+": kind=" + kind+ ", type="+  type+")";};
}

