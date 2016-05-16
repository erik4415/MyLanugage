import java.sql.Types;
import java.util.Iterator;

// The following methods type check  AST nodes used in CSX Lite
//  You will need to complete the methods after line 238 to type check the
//   rest of CSX
//  Note that the type checking done for CSX lite may need to be extended to
//   handle full CSX (for example binaryOpNode).

public class TypeChecking extends Visitor { 

//	static int typeErrors =  0;     // Total number of type errors found 
//  	public static SymbolTable st = new SymbolTable(); 	
	int typeErrors;     // Total number of type errors found 
	SymbolTable st;	
	
	TypeChecking(){
		typeErrors = 0;
		st = new SymbolTable(); 
	}
	
	boolean isTypeCorrect(csxLiteNode n) {
        	this.visit(n);
        	return (typeErrors == 0);
	}
	
	boolean isTypeCorrect(classNode n) {
    	this.visit(n);
    	return (typeErrors == 0);
}
	
	static void assertCondition(boolean assertion){  
		if (! assertion)
			 throw new RuntimeException();
	}
	 void typeMustBe(ASTNode.Types testType,ASTNode.Types requiredType,String errorMsg) {
		 if ((testType != ASTNode.Types.Error) && (testType != requiredType)) {
                        System.out.println(errorMsg);
                        typeErrors++;
                }
        }
	 void typesMustBeEqual(ASTNode.Types type1,ASTNode.Types type2,String errorMsg) {
		 if ((type1 != ASTNode.Types.Error) && (type2 != ASTNode.Types.Error) &&
                     (type1 != type2)) {
                        System.out.println(errorMsg);
                        typeErrors++;
                }
        }
	String error(ASTNode n) {
		return "Error (line " + n.linenum + "): ";
        }

	static String opToString(int op) {
		switch (op) {
			case sym.PLUS:
				return(" + ");
			case sym.MINUS:
				return(" - ");
			case sym.EQ:
				return(" == ");
			case sym.NOTEQ:
				return(" != ");
			case sym.TIMES:
				return(" * ");
			case sym.SLASH:
				return(" / ");
			case sym.CAND:
				return(" && ");
			case sym.COR:
				return(" || ");
			case sym.LT:
				return(" < ");
			case sym.GT:
				return(" > ");
			case sym.LEQ:
				return(" <= ");
			case sym.GEQ:
				return(" >= ");
				
			default:
				assertCondition(false);
				return "";
		}
	}


	static void printOp(int op) {
		switch (op) {
			case sym.PLUS:
				System.out.print(" + ");
				break;
			case sym.MINUS:
				System.out.print(" - ");
				break;
			case sym.EQ:
				System.out.print(" == ");
				break;
			case sym.NOTEQ:
				System.out.print(" != ");
				break;
			case sym.INTLIT:
				System.out.print("integer literal");
				break;
			case sym.STRLIT:
				System.out.print("string literal");
				break;
			case sym.CHARLIT:			
				System.out.print("character literal");
				break;
			case sym.IDENTIFIER:
				System.out.print("identifier)");
				break;
			case sym.SEMI:
				System.out.print(";");
				break;
			case sym.LPAREN:
				System.out.print("(");
				break;
			case sym.RPAREN:
				System.out.print(")");
				break;
			case sym.LBRACKET:
				System.out.print("[");
				break;
			case sym.RBRACKET:
				System.out.print("]");
				break;
			case sym.ASG:
				System.out.print("=");
				break;
			case sym.TIMES:
				System.out.print(" * ");
				break;
			case sym.SLASH:
				System.out.print(" / ");
				break;
			case sym.CAND:
				System.out.print(" && ");
				break;
			case sym.COR:
				System.out.print(" || ");
				break;
			case sym.COMMA:
				System.out.print(",");
				break;
			case sym.NOT:
				System.out.print("!");
				break;
			case sym.LBRACE:
				System.out.print("{");
				break;
			case sym.RBRACE:
				System.out.print("}");
				break;
			case sym.COLON:
				System.out.print(":");
				break;
			case sym.INCREMENT:
				System.out.print("++");
				break;
			case sym.DECREMENT:
				System.out.print("--");
				break;
			case sym.LT:
				System.out.print(" < ");
				break;
			case sym.GT:
				System.out.print(" > ");
				break;
			case sym.LEQ:
				System.out.print(" <= ");
				break;
			case sym.GEQ:
				System.out.print(" >= ");
				break;
			case sym.rw_BOOL:
				System.out.print("BOOL");
				break;
			case sym.rw_BREAK:
				System.out.print("BREAK");
				break;
			case sym.rw_CHAR:
				System.out.print("CHAR");
				break;
			case sym.rw_CLASS:
				System.out.print("CLASS");
				break;
			case sym.rw_CONST:
				System.out.print("CONST");
				break;
			case sym.rw_CONTINUE:
				System.out.print("CONTINUE");
				break;
			case sym.rw_ELSE:
				System.out.print("ELSE");
				break;
			case sym.rw_FALSE:
				System.out.print("FALSE");
				break;
			case sym.rw_IF:
				System.out.print("IF");
				break;
			case sym.rw_INT:
				System.out.print("INT");
				break;
			case sym.rw_PRINT:
				System.out.print("PRINT");
				break;
			case sym.rw_READ:
				System.out.print("READ");
				break;
			case sym.rw_RETURN:
				System.out.print("RETURN");
				break;
			case sym.rw_TRUE:
				System.out.print("TRUE");
				break;
			case sym.rw_VOID:
				System.out.print("VOID");
				break;
			case sym.rw_WHILE:
				System.out.print("WHILE");
				break;
			case sym.error:
				System.out.println("**ERROR: invalid token ()");
				break;
			default:
				throw new Error();
		}
	}


	void visit(csxLiteNode n){
		this.visit(n.progDecls);
		this.visit(n.progStmts);
	}
	
	void visit(fieldDeclsNode n){
			this.visit(n.thisField);
			this.visit(n.moreFields);
	}
	void visit(nullFieldDeclsNode n){}

	void visit(stmtsNode n){
		  this.visit(n.thisStmt);
		  this.visit(n.moreStmts);

	}
	void visit(nullStmtsNode n){}

	// Extend varDeclNode's method to handle initialization
	void visit(varDeclNode n){

		
		SymbolInfo id = (SymbolInfo) st.localLookup(n.varName.idname);
		
		
		if (id != null) {
			System.out.println(error(n) + id.name()+ " is already declared.");
			typeErrors++;
			n.varName.type = ASTNode.Types.Error;

		} 
		else {
			id = new SymbolInfo(n.varName.idname, ASTNode.Kinds.Var, n.varType.type);

			
			n.varName.type = n.varType.type;
			

			try {
				st.insert(id);
			} catch (DuplicateException d) 
			{ /* can't happen */ }
			catch (EmptySTException e) 
			{ /* can't happen */ }
			n.varName.idinfo=id;
		}
		
		this.visit(n.varName);
		this.visit(n.initValue);
		this.visit(n.varType);
		
	

	};

	void visit(nullTypeNode n){}

	void visit(intTypeNode n){
		n.type = ASTNode.Types.Integer;
	}
	void visit(boolTypeNode n){
		//no type checking needed}
		n.type = ASTNode.Types.Boolean;
	}
	
	void visit(identNode n){
		
		SymbolInfo id =  (SymbolInfo) st.localLookup(n.idname);
		
		if(id == null){
			id =  (SymbolInfo) st.globalLookup(n.idname);
		}
		
		
		if (id == null) {
			System.out.println(error(n) +  n.idname + " is not declared.");
			typeErrors++;
			n.type = ASTNode.Types.Error;
		} 
		
		else {
			
		n.kind = id.kind;
			n.type = id.type; 
			n.idinfo = id; // Save ptr to correct symbol table entry
		}
	}

	void visit(nameNode n){
			
		SymbolInfo id = (SymbolInfo) st.localLookup(n.varName.idname);
		
		if(id == null){
			id = (SymbolInfo) st.globalLookup(n.varName.idname);
			
		}
	
		if(id != null){
			n.kind = id.kind;
			
			
			this.visit(n.varName); 
	        n.type=n.varName.type;
	        this.visit(n.subscriptVal);
	        
	        if(!n.subscriptVal.isNull()){
	        	if((n.subscriptVal.type != ASTNode.Types.Character) && (n.subscriptVal.type != ASTNode.Types.Integer)){
	        		System.out.println(error(n) + " can only access subscript of array with integers or characters");
	        	}
	        }
		}
		
		
        	

	}

	void visit(asgNode n){
		
		SymbolInfo id = (SymbolInfo) st.localLookup(n.target.varName.idname);
		
		if(id == null){
			
			 id = (SymbolInfo) st.globalLookup(n.target.varName.idname);
		}
		
	
		
		this.visit(n.target);
		this.visit(n.source);
		
		
		
		if(n.target.varName.kind == ASTNode.Kinds.Value){
			System.out.println(error(n) + "cannot assign a const");
		}
		
		
		else if(n.target.varName.kind == ASTNode.Kinds.Array && n.target.varName.type == ASTNode.Types.Character
				&& n.source.kind == ASTNode.Kinds.String){
			strLitNode a = (strLitNode) n.source;
			
			SymbolInfo i = (SymbolInfo) st.localLookup(n.target.varName.idname);
			
			if(i == null){
				i = (SymbolInfo) st.globalLookup(n.target.varName.idname);
			}
			
			if(a.strval.length()-2 != i.size){
				System.out.println(error(n) + "the string and the char array do not have the same length");
			}
			
		}
		
	

		else{
		typesMustBeEqual(n.source.type, n.target.varName.type,
				error(n) + "Both the left and right"
						+ " hand sides of an assignment must "
						+ "have the same type.");
		//System.out.println(n.source.type + " " +n.source.kind+ " " + n.target.varName.type + " " + n.target.varName.kind + " " + n.target.varName.idname);
		}
	}

	// Extend ifThenNode's method to handle else parts
	void visit(ifThenNode n){
		this.visit(n.condition);
		typeMustBe(n.condition.type, ASTNode.Types.Boolean,
				error(n) + "The control expression of an" +" if must be a bool.");

		
		st.openScope();
		this.visit(n.thenPart);
		// close this block's local scope
		try { 
			st.closeScope();
		}  
		catch (EmptySTException e) 
		{ /* can't happen */ 
			
		}

		if(!n.elsePart.isNull()){

			st.openScope();
			this.visit(n.elsePart);

			// close this block's local scope
			try { st.closeScope();
			}  catch (EmptySTException e) 
			{ /* can't happen */ }
		}

	}

	 void visit(printNode n){
		
        	
        	
        	if(!n.outputValue.isNull()){
        		this.visit(n.outputValue);
        		        		       		        
        		if(n.outputValue.getClass().getName().equals("fctCallNode")){
        			
        			System.out.println(error(n) + "Only int, char, char[], String Lits and bool values may be printed.");
        			typeErrors++;
        			return;
        			
        		}
        		
        		
        		else if(n.outputValue.type == ASTNode.Types.Error){
        			System.out.println(error(n) + "Only int, char, char[], String Lits and bool values may be printed." );
        			typeErrors++;
        			return;
        		}
        		
        		else if(n.outputValue.kind == ASTNode.Kinds.Method){
        			System.out.println(error(n) + "Only int, char, char[], String Lits and bool values may be printed." );
        			typeErrors++;
        			return;
        		}
        		
        		else if(n.outputValue.kind == ASTNode.Kinds.Array && n.outputValue.type != ASTNode.Types.Character){
        			System.out.println(error(n) + "Only int, char, char[], String Lits and bool values may be printed." );
        			typeErrors++;
        			return;
        		}
        		
        		
        		else if((n.outputValue.type == ASTNode.Types.Integer || n.outputValue.type == ASTNode.Types.Boolean )&& n.outputValue.kind == ASTNode.Kinds.Array){
        			System.out.println(error(n) + "Only int, char, char[], String Lits and bool values may be printed." );
        			typeErrors++;
        			return;
        		}
        		
        		else if(n.outputValue.type == ASTNode.Types.Unknown){
        			
        			System.out.println(error(n) + "Only int, char, char[], String Lits and bool values may be printed." );
        			typeErrors++;
        			return;
        			
        		}
        		
        		else if(n.outputValue.kind == ASTNode.Kinds.String){
        			//nothing to do
        		}
        		
        		
        		
        		else if(n.outputValue.type == ASTNode.Types.Character){
        			//nothing to do
        		}
        		
        		
        		else if (n.outputValue.kind == ASTNode.Kinds.Array && n.outputValue.type == ASTNode.Types.Character){
        		
        		//nothing to do
        		
        			
        		}
        		
        		else if((n.outputValue.type == ASTNode.Types.Integer || n.outputValue.type == ASTNode.Types.Boolean )&& n.outputValue.kind != ASTNode.Kinds.Array){
        			//nothing to do
        		}
        		
        		
        		
        		
        	}
        	this.visit(n.morePrints);
	  }
	  
	  void visit(blockNode n){
		// open a new local scope for the block body
			st.openScope();
			this.visit(n.decls);
			this.visit(n.stmts);
			// close this block's local scope
			try { st.closeScope();
			}  catch (EmptySTException e) 
	                      { /* can't happen */ }
	  }

	
	  void visit(binaryOpNode n){
		  
		assertCondition(n.operatorCode== sym.PLUS||n.operatorCode==sym.MINUS 
        			|| n.operatorCode== sym.EQ||n.operatorCode==sym.NOTEQ
        			||n.operatorCode==sym.COR||n.operatorCode==sym.CAND
        			||n.operatorCode==sym.GT||n.operatorCode==sym.LT
        			||n.operatorCode==sym.GEQ||n.operatorCode==sym.LEQ
        			||n.operatorCode==sym.TIMES||n.operatorCode==sym.SLASH);
		this.visit(n.leftOperand);
		this.visit(n.rightOperand);
		
		if (n.operatorCode== sym.PLUS || n.operatorCode==sym.MINUS 
				|| n.operatorCode== sym.TIMES || n.operatorCode==sym.SLASH){
			
			n.type = ASTNode.Types.Integer;



			if ((n.leftOperand.type != ASTNode.Types.Integer) && (n.leftOperand.type != ASTNode.Types.Character)) {
				System.out.println(error(n) + "Left operand of" + opToString(n.operatorCode) 
						+  "must be an int or char.");
				typeErrors++;
			}

			if ((n.rightOperand.type != ASTNode.Types.Integer) && (n.rightOperand.type != ASTNode.Types.Character)) {
				System.out.println(error(n) + "Right operand of" + opToString(n.operatorCode) 
						+  "must be an int or char.");
				typeErrors++;
			}
		}
		
		
		else if (n.operatorCode== sym.COR || n.operatorCode==sym.CAND ){
			
			n.type = ASTNode.Types.Boolean;
			
			
			if (n.leftOperand.type != ASTNode.Types.Boolean) {
				System.out.println(error(n) + "Left operand of" + opToString(n.operatorCode) 
						+  "must be a bool.");
				typeErrors++;
			}

			if (n.rightOperand.type != ASTNode.Types.Boolean) {
				System.out.println(error(n) + "Right operand of" + opToString(n.operatorCode) 
						+  "must be a bool.");
				typeErrors++;
			}
			
			

		
		}
		
		
		else { // Must be a comparison operator
			n.type = ASTNode.Types.Boolean;
			String errorMsg = error(n)+"Both operands of"+
					opToString(n.operatorCode)+"must have the same type.";
			typesMustBeEqual(n.leftOperand.type,n.rightOperand.type,errorMsg);

		}
	  }

	
	
	void visit(intLitNode n){
	//      All intLits are automatically type-correct
		
	}

	void visit(classNode n){
		
		SymbolInfo id =  (SymbolInfo) st.globalLookup(n.className.idname);
		if (id == null) {
			
			
			try{
			st.insert(n.className.idinfo);
		
			}
			catch(Exception e){
				//nothing to do
			}
			//n.className.idinfo.type = id.type; 
			n.className.idinfo = id; // Save ptr to correct symbol table entry
			
		} else {
			
			System.out.println(error(n) +  n.className.idname +" is already declared.");
			typeErrors++;
			n.className.idinfo.type = ASTNode.Types.Error;
		}

		this.visit(n.members);
		
//Check for methods that were forward referenced
		
		Iterator<String> itr = st.methodNotYetDeclared.iterator();
		
		while(itr.hasNext()){
			
			String next = itr.next();
			SymbolInfo i = (SymbolInfo) st.globalLookup(next);
			
			if(i == null){
				System.out.println("Error (line "+itr.next()+") The method " +next+ " was used but never declared");
				typeErrors++;
			}
			
			else{
				itr.next();
			}
			
			
		}
		
		
		
		
		
//Check for main method
		id =  (SymbolInfo) st.globalLookup("main");
		
		if(id == null){
			System.out.println(error(n) +  " there is no main method declared for class.");
			typeErrors++;
		}
		
		else{
			if(id.type != ASTNode.Types.Void){
				System.out.println(error(n) + " the return type of the main method should be void.");
				typeErrors++;
			}
			
			boolean noArgs = false;
			
			Iterator<String> iter = id.paramaterList.iterator();
			
			while (iter.hasNext()){
				if(iter.next().equals("none")){
					noArgs = true;
				}
			}
			
			if (!noArgs){
				System.out.println(error(n) + " the main method should have 0 arguments.");
				typeErrors++;
			}
		}
	}

	void  visit(memberDeclsNode n){
		
		if(!n.fields.isNull())
			this.visit(n.fields);
		if(n.mem !=null){
			if(!n.mem.isNull())
				this.visit(n.mem);
		}
		
		if(n.methods !=null){
			if(!n.methods.isNull())
				
				this.visit(n.methods);
			
		}

	}

	void  visit(methodDeclsNode n){
		if(n.thisDecl != null){
			if(!n.thisDecl.isNull()){
				this.visit(n.thisDecl);
			}
			
		}
		if(!n.moreDecls.isNull())
			this.visit(n.moreDecls);
	}

	void visit(nullStmtNode n){}

	void visit(nullReadNode n){}

	 void visit(nullPrintNode n){}

	 void visit(nullExprNode n){}

	 void visit(nullMethodDeclsNode n){}

	 void visit(methodDeclNode n){

		 this.visit(n.returnType);

		 SymbolInfo id =  (SymbolInfo) st.globalLookup(n.name.idname);

		 //Initial declaration of methods
		 if (id == null) {

			 id = new SymbolInfo(n.name.idname, ASTNode.Kinds.Method, n.returnType.type);
			 
			 try {
				 st.insert(id);
			 } catch (DuplicateException d) 
			 { /* can't happen */ }
			 catch (EmptySTException e) 
			 { /* can't happen */ }
			 n.name.idinfo=id;

			 st.openScope();



			 this.visit(n.args);
			 this.visit(n.decls);
			 this.visit(n.stmts);

			 
			 //Make teh list of paramaters for the method, save to id
			 if(!n.args.isNull()){
				 argDeclsNode argDecls = (argDeclsNode) n.args;
				 //create a string of the paramaters of the args 
				 String params = "";
				 while(true){

					 String classType = argDecls.thisDecl.getClass().getName();	 	 
					 //Figure out the types of node, and add it to the string of types.
					 if (classType.equals("valArgDeclNode")){
						 valArgDeclNode a = (valArgDeclNode) argDecls.thisDecl;
						 params = params + a.argType.type.toString() + "Var";

					 }
					 else{
						 arrayArgDeclNode a = (arrayArgDeclNode) argDecls.thisDecl;
						 params = params + a.elementType.type.toString()+"Array";
						 a.argName.kind = ASTNode.Kinds.Array;
						 
					 }

					 if (!argDecls.moreDecls.isNull()){
						 argDecls = (argDeclsNode) argDecls.moreDecls;
					 }

					 else{
						 break;
					 }					 
				 }

				 id.paramaterList.add(params);
			 }

			 else{
				 id.paramaterList.add("none");
			 }


			 //ensure correct return type from returnNode
			 stmtsNode bb = (stmtsNode) n.stmts;
			 
			 if(!bb.isNull()){
			 
			 while(true){
				 if(bb.thisStmt.getClass().equals("returnNode")){

					 returnNode cc = (returnNode) bb.thisStmt;

					 if(cc.returnVal.type != id.type){

						 System.out.println(error(n) + "return type in method does not match return type in method decleration");

					 }

					 break;
				 }


				 else{

					 if(!bb.moreStmts.isNull()){

						 bb = (stmtsNode) bb.moreStmts;
					 }

					 else{
						 break;
					 }


					
				 }
			 }
			 }
		 }

//Overloading Methods
		 else {

			 st.openScope();
			 this.visit(n.decls);
			 this.visit(n.stmts);
			 if(n.returnType.type == id.type){

				 //create a string of the paramaters of the args 
				 String params = "";

				 if(!n.args.isNull()){

					 argDeclsNode argDecls = (argDeclsNode) n.args;

					 while(true){

						 String classType = argDecls.thisDecl.getClass().getName();	 	 
						 //Figure out the types of node, and add it to the string of types.
						 if (classType.equals("valArgDeclNode")){
							 valArgDeclNode a = (valArgDeclNode) argDecls.thisDecl;
							 params = a.argType.type.toString();
						 }
						 else{
							 arrayArgDeclNode a = (arrayArgDeclNode) argDecls.thisDecl;
							 params = a.elementType.type.toString();
							 a.argName.kind = ASTNode.Kinds.Array;
						 }

						 if (!argDecls.moreDecls.isNull()){
							 argDecls = (argDeclsNode) argDecls.moreDecls;
						 }

						 else{
							 break;
						 }
					 }
				 }


				 else{
					 params = "none";
				 }

				 boolean alreadyDeclared = false;

				 Iterator<String> i = id.paramaterList.iterator();

				 while (i.hasNext()){

					 if(i.next().equals(params)){
						 alreadyDeclared= true;
					 }
				 }


				 if (alreadyDeclared){

					 System.out.println(error(n) +  n.name.idname +" has already been declared with these parameters.");
				 }


				 else{
					 id.paramaterList.add(params);
				 }
			 }


			 else{

				 System.out.println(error(n) +  n.name.idname +" does not have the right return type.");
				 typeErrors++;
			 }
			 // n.name.idinfo.type = ASTNode.Types.Error;

		 }


		 
		
		

		 
		 
		// close this block's local scope
			try { st.closeScope();
			}  catch (EmptySTException e) 
	                      { /* can't happen */ }
		 }
		 
	 

	 void visit(incrementNode n){

		 SymbolInfo    id;
		 
		 id =  (SymbolInfo) st.globalLookup(n.target.varName.idname);
		 if (id == null) {

			 System.out.println(error(n) +  n.target.varName.idname +" is not declared.");
			 typeErrors++;
			 n.target.type = ASTNode.Types.Error;
		 } else {
			 
			 if(!id.type.equals(ASTNode.Types.Character) && !id.type.equals(ASTNode.Types.Integer)){
				 System.out.println(error(n) +  n.target.varName.idname +" is not a type that can be incremented.");
				 typeErrors++;
				 n.target.type = ASTNode.Types.Error;

			 }
			 
			 else if(n.target.varName.kind == ASTNode.Kinds.Value){
				 System.out.println(error(n) +  n.target.varName.idname +" is a constanst and cannot be incremented.");
				 typeErrors++;
				 n.target.type = ASTNode.Types.Error;
				 
			 }
		 }

	 }
	 void visit(decrementNode n){
		 SymbolInfo    id;
		 assertCondition(n.target.kind == ASTNode.Kinds.Var); //In CSX-lite all IDs should be vars! 
		 id =  (SymbolInfo) st.globalLookup(n.target.varName.idname);
		 if (id == null) {

			 System.out.println(error(n) +  n.target.varName.idname +" is not declared.");
			 typeErrors++;
			 n.target.type = ASTNode.Types.Error;
		 } else {
			 
			 if(!id.type.equals(ASTNode.Types.Character) && !id.type.equals(ASTNode.Types.Integer)){
				 System.out.println(error(n) +  n.target.varName.idname +" is not a type that can be decremented.");
				 typeErrors++;
				 n.target.type = ASTNode.Types.Error;

			 }
			 
			 else if(n.target.varName.kind == ASTNode.Kinds.Value){
				 System.out.println(error(n) +  n.target.varName.idname +" is a constanst and cannot be incremented.");
				 typeErrors++;
				 n.target.type = ASTNode.Types.Error;
				 
			 }
		 }
		 }
	void visit(argDeclsNode n){
		
		 
		this.visit(n.thisDecl);
		if(!n.moreDecls.isNull()){
			this.visit(n.moreDecls);
		}
	}

	void visit(nullArgDeclsNode n){}

	
	void visit(valArgDeclNode n){
		
		this.visit(n.argType);	
		
		SymbolInfo id;
		//look up to see if 
		 id = (SymbolInfo) st.localLookup(n.argName.idname);
		if (id != null) {
			System.out.println(error(n) + id.name()+" is already declared.");
			typeErrors++;
			n.argName.type = ASTNode.Types.Error;

		} 

		else {
			id = new SymbolInfo(n.argName.idname, ASTNode.Kinds.ScalarParm, n.argType.type);
			n.argName.type = n.argType.type;



			try {
				st.insert(id);
			} catch (DuplicateException d) 
			{ /* can't happen */ }
			catch (EmptySTException e) 
			{ /* can't happen */ }

			n.argName.idinfo = id;
		}	


		
	}

	void visit(arrayArgDeclNode n){
		
		this.visit(n.elementType);	
		
		n.argName.kind = ASTNode.Kinds.Array;
		
		n.argName.type = n.elementType.type;
		
		
		
		SymbolInfo id = (SymbolInfo) st.localLookup(n.argName.idname);
		
		if (id != null) {
			System.out.println(error(n) + id.name()+" is already declared.");
			typeErrors++;
			n.argName.type = ASTNode.Types.Error;

		} 

		else {
			id = new SymbolInfo(n.argName.idname, ASTNode.Kinds.Array, n.elementType.type);
			n.argName.type = n.elementType.type;

			n.argName.kind = ASTNode.Kinds.Array;
			

			try {
				st.insert(id);
			} catch (DuplicateException d) 
			{ /* can't happen */ }
			catch (EmptySTException e) 
			{ /* can't happen */ }

			n.argName.idinfo = id;
			
			
		}	

		
	}
	
	void visit(constDeclNode n){
		
		SymbolInfo id = (SymbolInfo) st.globalLookup(n.constName.idname);
		if (id != null) {
			System.out.println(error(n) + id.name()+" is already declared.");
			typeErrors++;
			n.constName.type = ASTNode.Types.Error;

		} else {
			
			id = new SymbolInfo(n.constName.idname, ASTNode.Kinds.Value, n.constValue.type);
			n.constName.type = n.constValue.type;
			
			
			try {
				st.insert(id);
			} catch (DuplicateException d) 
			{ /* can't happen */ }
			catch (EmptySTException e) 
			{ /* can't happen */ }
			
			n.constName.idinfo = id;
			
			n.constName.kind = ASTNode.Kinds.Value;
		}
		
	 }
	 
	 void visit(arrayDeclNode n){
		 

		 SymbolInfo id = (SymbolInfo) st.localLookup(n.arrayName.idname);
		 
			 
		 if (id != null) {
			 System.out.println(error(n) + id.name()+
					 " is already declared.");
			 typeErrors++;
			 n.arrayName.type = ASTNode.Types.Error;

		} else {
			
			//ensure array is initialized wiht number
			if(n.arraySize.intval < 1){
				System.out.println(error(n) + n.arrayName.idname+
						" needs to be initiated with a value larger than 0.");
				typeErrors++;
				n.arrayName.type = ASTNode.Types.Error;
				
				return;
			}
			
			id = new SymbolInfo(n.arrayName.idname,n.arraySize.intval, n.elementType.type);	
			n.arrayName.type = n.elementType.type;
			n.arrayName.kind = ASTNode.Kinds.Array;
			
			try {
				st.insert(id);
			} catch (DuplicateException d) 
			{ /* can't happen */ }
			catch (EmptySTException e) 
			{ /* can't happen */ }
			n.arrayName.idinfo=id;
		}
	 }
	
	void visit(charTypeNode n){
		//System.out.println("Type checking for charTypeNode not yet implemented");
		n.type = ASTNode.Types.Character;
	}
	void visit(voidTypeNode n){
		n.type = ASTNode.Types.Void;	
	}

	void visit(whileNode n){

		//If there is a label, ensure it is used correctly
		if(!n.label.isNull()){
			
			
			
			identNode a = (identNode) n.label;
			SymbolInfo id = new SymbolInfo(a.idname, ASTNode.Kinds.HiddenLabel, ASTNode.Types.Unknown);
			
			
			//visit expression
			this.visit(n.condition);

			typeMustBe(n.condition.type, ASTNode.Types.Boolean, error(n) + "The control expression of an" +" if must be a bool.");

			
			
			
			st.openScope();
			
			
			try {
				st.insert(id);
			} catch (DuplicateException d) 
			{ /* can't happen */ }
			catch (EmptySTException e) 
			{ /* can't happen */ }
			
			this.visit(n.loopBody);
			
			// close this block's local scope
			try { st.closeScope();
			}  catch (EmptySTException e) 
	                      { /* can't happen */ }
		
			

		}

		else{
			//visit expression
			this.visit(n.condition);

			typeMustBe(n.condition.type, ASTNode.Types.Boolean, error(n) + "The control expression of a while" +" statement must be a bool.");

			
			
			
			st.openScope();
			this.visit(n.loopBody);
			// close this block's local scope
			try { st.closeScope();
			}  catch (EmptySTException e) 
	                      { /* can't happen */ }
		}

	}

	void visit(breakNode n){
		
		
		if(n.label!=null){
			
			SymbolInfo id = (SymbolInfo) st.localLookup(n.label.idname);
			
			if(id == null){
				
				id = (SymbolInfo) st.globalLookup(n.label.idname);
				
				
			}
			
			if(id == null){
				System.out.println(error(n) + "the label is not declared with a while loop");
			}
			
			else if(id.kind != ASTNode.Kinds.HiddenLabel){
				System.out.println(error(n) + "the label is not declared with a while loop");
			}
		}
		
	
	}
	void visit(continueNode n){
		if(n.label!=null){
			SymbolInfo id = (SymbolInfo) st.localLookup(n.label.idname);
			
			if(id == null){
				
				id = (SymbolInfo) st.globalLookup(n.label.idname);
				
			}
			
			if(id == null){
				System.out.println(error(n) + "the label is not declared with a while loop");
			}
			
			else if(id.kind != ASTNode.Kinds.HiddenLabel){
				System.out.println(error(n) + "the label is not declared with a while loop");
			}
		}
		
	}
	  
	void visit(callNode n){
		
		//Whenever there is a call to a function, put it on the back burner if it is not declared yet
		SymbolInfo id =  (SymbolInfo) st.globalLookup(n.methodName.idname);
		
		if (id != null) {
			this.visit(n.methodName);
			
		}
		
		
		else{
			
			//need to put it on the backburner
			//maybe add to symbol table
			
			
			//add to symbol table to check up on in classNode visit
			st.methodNotYetDeclared.add(n.methodName.idname);
			st.methodNotYetDeclared.add(Integer.toString(n.linenum));
		}
			
		
		
		this.visit(n.args);
		
		//create a string of the paramaters of the args 
		String params = "";

		if(!n.args.isNull()){
			argsNode argDecls = (argsNode) n.args;
			while(true){			 
				//Figure out the types of node, and add it to the string of types. 
				params = params + argDecls.argVal.type.name() + argDecls.argVal.kind.name();		 
				if (!argDecls.moreArgs.isNull()){
					argDecls = (argsNode) argDecls.moreArgs;
				}

				else{
					break;
				}
			}
		}

		else{
			params = "none";
		}
		

		boolean correctParam = false;
		id =  (SymbolInfo) st.globalLookup(n.methodName.idname);

		if(id != null){

			if(id.type != ASTNode.Types.Void){ 
				System.out.println(error(n) +  n.methodName.idname +" does not return void, only methods with a return type of void can be called in statements.");
				return;
			}

			Iterator<String> i = id.paramaterList.iterator();

			while (i.hasNext()){
				String s = i.next();
				
				if(s.equals(params)){
					correctParam= true;
					
				}
			}
			

			if (!correctParam){
				System.out.println(error(n) +  n.methodName.idname +" has the incorrect paramaters.");
				
			}
		}
	}






void visit(readNode n){

	if(n.targetVar != null){
		this.visit(n.targetVar);
		if ((n.targetVar.type != ASTNode.Types.Error) && (n.targetVar.type != ASTNode.Types.Integer) && (n.targetVar.type != ASTNode.Types.Character)) {
			System.out.println(error(n) + "Only int or char values may be read.");
			typeErrors++;
			return;
		}
		
		else if(n.targetVar.kind == ASTNode.Kinds.Method){
			System.out.println(error(n) + "Only int or char values may be read.");
			typeErrors++;
			return;
		}
	}
	this.visit(n.moreReads);

}


void visit(returnNode n){
	
	
	if(!n.returnVal.isNull()){
		this.visit(n.returnVal);
	}
}


void visit(argsNode n){
	




	this.visit(n.argVal);
	if(!n.moreArgs.isNull()){
		this.visit(n.moreArgs);
	}
}

void visit(nullArgsNode n){}

void visit(castNode n){

	this.visit(n.resultType);

	if(n.resultType.type == ASTNode.Types.Boolean || n.resultType.type == ASTNode.Types.Integer ||n.resultType.type == ASTNode.Types.Character){

		this.visit(n.operand);
		if(n.operand.type == ASTNode.Types.Boolean || n.operand.type == ASTNode.Types.Integer ||n.operand.type == ASTNode.Types.Character){

		}

		else{
			System.out.println(error(n)+"Not able to cast this variable");
			 }
		 }
		 
		 else{
		 
		 System.out.println(error(n)+"Not able to cast from this type");
		 }
		
	  }

	  void visit(fctCallNode n){
		  
		  
		//Whenever there is a call to a function, put it on the back burner if it is not declared yet
			SymbolInfo id =  (SymbolInfo) st.globalLookup(n.methodName.idname);
			
			if (id != null) {
				this.visit(n.methodName);
				
			}
			
			
			else{
				
				//need to put it on the backburner
				//maybe add to symbol table
				
				
				//add to symbol table to check up on in classNode visit
				st.methodNotYetDeclared.add(n.methodName.idname);
				st.methodNotYetDeclared.add(Integer.toString(n.linenum));
			}
		
		this.visit(n.methodArgs);
		
		//create a string of the paramaters of the args 
		String params = "";

		if(!n.methodArgs.isNull()){
			argsNode argDecls = (argsNode) n.methodArgs;
			while(true){			 
				//Figure out the types of node, and add it to the string of types. 
				params = params + argDecls.argVal.type.name() + argDecls.argVal.kind.name();;		 
				if (!argDecls.moreArgs.isNull()){
					argDecls = (argsNode) argDecls.moreArgs;
				}

				else{
					break;
				}

			}
		}

		else{
			params = "none";
		}

		boolean correctParam = false;
	  id =  (SymbolInfo) st.globalLookup(n.methodName.idname);
	
	 if(id != null){
		 
		 
		 if(id.type == ASTNode.Types.Void){ 
			 System.out.println(error(n) +  n.methodName.idname +" returns void, only methods with a return type can be called in expressions.");
			 return;
			}
		 
		 Iterator<String> i = id.paramaterList.iterator();

			while (i.hasNext()){

				if(i.next().equals(params)){
					correctParam= true;
				}
			}


			if (!correctParam){

				System.out.println(error(n) +  n.methodName.idname +" has the incorrect parameters.");
			}
		 
	 }
	  }

	  void visit(unaryOpNode n){
		
		this.visit(n.operand);
		
		if(n.operand.type != ASTNode.Types.Boolean){
			System.out.println(error(n) + " operand must be a bool.");
			typeErrors++;
		}
	  }

	
	void visit(charLitNode n){
		n.type = ASTNode.Types.Character;
		n.type = ASTNode.Types.Void;
		
	}
	  
	void visit(strLitNode n){
		n.kind = ASTNode.Kinds.String;
		n.type = ASTNode.Types.Void;
		
	}

	
	void visit(trueNode n){
		//System.out.println("Type checking for trueNode not yet implemented");
		n.type = ASTNode.Types.Boolean;
	}

	void visit(falseNode n){
		//System.out.println("Type checking for falseNode not yet implemented");
		n.type = ASTNode.Types.Boolean;
	}


}
