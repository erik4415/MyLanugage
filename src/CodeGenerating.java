import java.io.*;
/*
 *  This Visitor class generates JVM assembler code (using Jasmin's format)
 *  for CSX lite in the Printstream afile. You'll need to extend it to
 *  handle all of CSX. Note that for some AST nodes (like asgNode) code generation
 *  for CSX is more complex than that needed for CSX lite.
 *  All methods marked will have to be completed by you (for full CSX)
 */

public class CodeGenerating extends Visitor {
	
	PrintStream afile;	// File to generate JVM code into 

	int cgErrors =  0;       // Total number of code generation errors 

	int numberOfLocals =  0; // Total number of local CSX-lite vars

	int labelCnt = 0;	// counter used to generate unique labels
	
	methodDeclNode currentMethod;
	
	String CLASS;
	
	CodeGenerating(PrintStream f){
		afile=f;
	}

	static void assertCondition(boolean assertion){
		if (! assertion)
			 throw new RuntimeException();
	}

	String error(ASTNode n) {
		return "Error (line " + n.linenum + "): ";
        }

	// generate a comment
	 void  genComment(String text){
       	gen("; "+text);
	}
	 
	 // Generate a static method call:
	 void genCall(String methodDescriptor){
		 afile.println("\t"+"invokestatic " + methodDescriptor);

	 }




	// generate an instruction w/ 0 operands
	 void  gen(String opcode){
        	afile.println("\t"+opcode);
	}

        // generate an instruction w/ 1 operand
	void  gen(String opcode, String operand){
        	afile.println("\t"+opcode+"\t"+operand);
	}

        // generate an instruction w/ 1 integer operand
	void  gen(String opcode, int operand){
        	afile.println("\t"+opcode+"\t"+operand);
	}


	//  generate an instruction w/ 2 operands
	void  gen(String opcode, String operand1, String operand2){
        	afile.println("\t"+opcode+"\t"+ operand1+"  "+ operand2);
	}

	//  generate an instruction w/ 2 operands (String and int)
	void  gen(String opcode, String operand1, int operand2){
        	afile.println("\t"+opcode+"\t"+ operand1+"  "+operand2);
	}

	//      Generate a new label of form labeln (e.g., label7 or label123)
	String   genLab(){
                return "label"+labelCnt++;
	}

	//      Place a label in generated code
	void    defineLab(String label){
		afile.println("\t"+label+":");
	}

	void branch(String label){
		gen("goto",label);
	}

	void loadI(int val){
		gen("ldc",val);
	}
	
	void loadGlobalInt(String name){
		gen("getstatic", name, "I");
	}
	void loadLocalInt(int index){
		// Generate a load of an int local variable onto the stack: // 
		gen("iload", index);
	}
	
	
	void loadGlobalReference(String name, String typeCode){
		   // Generate a load of a reference to the stack from
		   // a static field:
		   //   getstatic CLASS/name typeCode
		afile.println("getStatic " + CLASS + "/"+name + " " + typeCode);
		
		}
	
	void loadLocalReference(int index){
		   // Generate a load of a reference to the stack from
		   // a local variable:
		afile.println("aload " + index);
		}


	void binOp(String op){
		// Generate a binary operation; // all operands are on the stack: // 
		gen(op);
	}

	void storeGlobalInt(String name){
		// Generate a store into an int static field from the stack: // 
		gen("putstatic",name, "I");
	}
	void storeLocalInt(int index){
		// Generate a store to an int local variable from the stack: // 
		gen("istore", index);
	}
	
	
	void computeAdr(nameNode name) {
		
		if (name.subscriptVal.isNull()) {
	        // Simple (unsubscripted) identifier
	        if (name.varName.idinfo.kind == ASTNode.Kinds.Var ||
	             name.varName.idinfo.kind ==  ASTNode.Kinds.ScalarParm) {
	             // id is a scalar variable
	             if (name.varName.idinfo.adr == ASTNode.AdrMode.global) {
	                   name.adr = ASTNode.AdrMode.global;
	                   name.label = name.varName.idinfo.label;
	             } else { // varName.idinfo.adr == Local
	                   name.adr = ASTNode.AdrMode.local;
	                   name.varIndex =
	                        name.varName.idinfo.varIndex;
	        }} else { // Must be an array
	             // Push ref to target array to check length
	             if (name.varName.idinfo.adr == ASTNode.AdrMode.global){
	            	 name.label = name.varName.idinfo.label;
	            	 loadGlobalReference(name.label,
	            			 arrayTypeCode(name.varName.idinfo.type));
	             } else { // (name.varName.idinfo.adr == local)
	            	 name.varIndex =
	            			 name.varName.idinfo.varIndex;
	            	 loadLocalReference(name.varIndex);
	             }
	        }
		} 
		else {
			
			// Push array reference first
			if (name.varName.idinfo.adr == ASTNode.AdrMode.global){
				name.label = name.varName.idinfo.label;
				loadGlobalReference(name.label,
						arrayTypeCode(name.varName.idinfo.type));
			} else { // (name.varName.idinfo.adr == local)
				name.varIndex = name.varName.idinfo.varIndex;
				loadLocalReference(name.varIndex);
			} // Next compute subscript expression
			this.visit(name.subscriptVal);
		}
	}


	void storeId(identNode id) {
		if (id.idinfo.kind == ASTNode.Kinds.Var || id.idinfo.kind == ASTNode.Kinds.Value ) { 
			// id is a scalar variable
			if (id.idinfo.adr == ASTNode.AdrMode.global){ 
				// ident is a global 
				storeGlobalInt(id.idinfo.label);
			}
			else{ // (id.idinfo.adr == local) 
				storeLocalInt(id.idinfo.varIndex);
			}
		} 
		else{ 
			
			if (id.idinfo.adr == ASTNode.AdrMode.global){ 
				// ident is a global 
				String ty = arrayTypeCode(id.idinfo.type);
				storeGlobalReference(id.idinfo.label, ty);
			}
			else{ // (id.idinfo.adr == local) 
				storeLocalReference(id.idinfo.varIndex);
			}

		}

	}

	void storeName(nameNode name) {
		if (name.subscriptVal.isNull()) {
			// Simple (unsubscripted) identifier
			if (name.varName.idinfo.kind == ASTNode.Kinds.Var) {
				if (name.adr == ASTNode.AdrMode.global) {
					storeGlobalInt(name.label);
				}
				else{ // (name.adr == local) 
					storeLocalInt(name.varIndex);
				}
			} 
			else{ 

				// Check the lengths of source & target arrays
				switch(name.type){
				case Integer:
					genCall("CSXLib/checkIntArrayLength([I[I)[I");
					break;
				case Boolean:
					genCall(
							"CSXLib/checkBoolArrayLength([Z[Z)[Z");
					break;
				case Character:
					genCall("CSXLib/checkCharArrayLength([C[C)[C");
					break;
				default:
					break;

				}

				if (name.varName.idinfo.adr == ASTNode.AdrMode.global){
					name.label = name.varName.idinfo.label;
					storeGlobalReference(name.label,
							arrayTypeCode(name.varName.idinfo.type));
				} else { // (name.varName.idinfo.adr == local)
					name.varIndex =
							name.varName.idinfo.varIndex;
					storeLocalReference(name.varIndex);

				} 
			}
		}
		else{ 
			 
			switch(name.type){

			case Integer:
				//Generate: iastore
				gen("iastore");
				break;
			case Boolean:
				//Generate: bastore
				gen("bastore");
				break;
			case Character:
				//Generate: castore
				gen("castore");
				break;
			default:
				break;
			}
		}

	}





	static Boolean isRelationalOp(int op) {
		switch (op) {
		case sym.EQ:
			return true;
		case sym.NOTEQ:
			return true;
		case sym.LT:
			return true;
		case sym.LEQ:	
			return true;
		case sym.GT:
			return true;
		case sym.GEQ:
			return true;
		default:
			return false;
		}
	}
	static String relationCode(int op) {
		switch (op) {
		case sym.EQ:
			return "eq";
		case sym.NOTEQ:
			return "ne";
		case sym.LT:
			return "lt";
		case sym.LEQ:	
			return "le";
		case sym.GT:
			return "gt";
		case sym.GEQ:
			return "ge";
		default:
			return "";
		}
	}
	static String selectOpCode(int op) {
		switch (op) {
		case sym.PLUS:
			return("iadd");
		case sym.MINUS:
			return("isub");
		case sym.TIMES:
			return("imul");
		case sym.SLASH:
			return("idiv");
		case sym.CAND:
			return("iand");
		case sym.COR:
			return("ior");
		default:
			assertCondition(false);
			return "";
		}
	}
	
	
	void branchRelationalCompare(int tokenCode, String label){
		// Generate a conditional branch to label based on tokenCode:
		gen("if_icmp"+relationCode(tokenCode), label);
		}
	
	void genRelationalOp(int operatorCode){
		   // Generate code to evaluate a relational operator
		     String trueLab = genLab();
		     String skip = genLab();
		     branchRelationalCompare(operatorCode, trueLab);
		     loadI(0); // Push false
		     branch(skip);
		     defineLab(trueLab);
		     loadI(1); // Push true
		     defineLab(skip);
		}


	
	//   startCodeGen translates the AST rooted by node n
  	//      into JVM code which is written in afile.
	//   If no errors occur during code generation,
	//    TRUE is returned, and afile should contain a
    //    complete and correct JVM program. 
	//   Otherwise, FALSE is returned and afile need not
	//    contain a valid program.
	
	boolean startCodeGen(csxLiteNode n) {// For CSX Lite
	    this.visit(n);
	    return (cgErrors == 0);
	}
	
	boolean startCodeGen(classNode n) {// For CSX
	    this.visit(n);
	    return (cgErrors == 0);
		}
	
 	void visit(csxLiteNode n) {
 		genComment("CSX Lite program translated into Java bytecodes ====(Jasmin format)");
		gen(".class","public","test");
    	gen(".super","java/lang/Object");
    	gen(".method"," public static","main([Ljava/lang/String;)V");
		this.visit(n.progDecls);
		if (numberOfLocals >0)
    		gen(".limit","locals",numberOfLocals);
		this.visit(n.progStmts);
    	gen("return");
    	gen(".limit","stack",10);
    	gen(".end","method");
	}

 	void visit(fieldDeclsNode n){
		this.visit(n.thisField);
		this.visit(n.moreFields);
	}

 	void visit(nullFieldDeclsNode n){}

 	void visit(stmtsNode n){
 		//System.out.println ("In stmtsNode\n");

 		if(n.thisStmt != null){
 			if(!n.thisStmt.isNull()){


 				this.visit(n.thisStmt);

 			}
 		}
 		if(n.thisStmt != null){
 			if(!n.thisStmt.isNull())
 				this.visit(n.moreStmts);
 		}
 	}


 	void visit(nullStmtsNode n){}

 	void visit(varDeclNode n){

 		if (currentMethod == null){
 			// A global field decl
 			if (n.varName.idinfo.adr == ASTNode.AdrMode.none){
 				// First pass; generate field declarations
 				declField(n);
 			}

 			else { // 2nd pass; do field initialization (if needed)
 				if (!n.initValue.isNull() && n.initValue !=null){
 					if (!isNumericLit(n.initValue)) {
 						// Compute init val onto stack; store in field this.visit(n.initValue);
 						storeId(n.varName);
 					} 
 				}
 			}
 		}
 		else {
 			n.varName.idinfo.varIndex =
 					currentMethod.name.idinfo.numberOfLocals;
 			n.varName.idinfo.adr = ASTNode.AdrMode.local;
 			//   Increment numberOfLocals used in this method
 			currentMethod.name.idinfo.numberOfLocals++;
 			// Do initialization (if necessary)
 			if (! n.initValue.isNull()  && n.initValue !=null){
 				this.visit(n.initValue);
 				storeId(n.varName);
 			}


 		}


 		n.varName.idinfo.varIndex = numberOfLocals;
 		numberOfLocals++;

 	}

 	void visit(nullTypeNode n) {
 	// No code generation needed
 		
 	}

 	void visit(intTypeNode n) {
 		// No code generation needed
 	}

 	void visit(boolTypeNode n) {
 		// No code generation needed
	}

	void visit(charTypeNode n) {
		// No code generation needed
	}

	void visit(voidTypeNode n) {
		// No code generation needed
	}
	
	void visit(asgNode n) {
		// Compute address associated with LHS
	     computeAdr(n.target);
	     // Translate RHS (an expression)
	     this.visit(n.source);
	     // Check to see if source needs to be cloned or converted
	     if (n.source.kind == ASTNode.Kinds.Array ||
	         n.source.kind == ASTNode.Kinds.ArrayParm)
	           switch(n.source.type){
	                case Integer:
	                      genCall("CSXLib/cloneIntArray([I)[I");
	                      break;
	                case Boolean:
	                      genCall("CSXLib/cloneBoolArray([Z)[Z");
	                      break;
	                case Character:
	                      genCall("CSXLib/cloneCharArray([C)[C");
	                      break;
			default:
				break;
	           }
	     else if (n.source.kind == ASTNode.Kinds.String)
	           genCall("CSXLib/convertString(Ljava/lang/String;)[C");
	     // Value to be stored is now on the stack
	     // Store it into LHS
	     storeName(n.target);
	}
	
	
	void visit (ifThenNode n) { 
  
        	    	
        	 String endLab;   // label that will mark end of if stmt
             String elseLab;  // label that will mark start of else part
             // translate boolean condition, pushing it onto the stack
             this.visit(n.condition);
             elseLab = genLab();
             // generate conditional branch around then stmt
             branchZ(elseLab);
             // translate then part
             this.visit(n.thenPart);
             // branch around else part
             endLab = genLab();
             branch(endLab);
             // translate else part
             defineLab(elseLab);
             this.visit(n.elsePart);
             // generate label marking end of if stmt
             defineLab(endLab);
	}

	void visit(printNode n) {
		// compute value to be printed onto the stack
    
    // Call CSX library routine "printInt(int i)"
    	//gen("invokestatic"," CSXLib/printInt(I)V");
    	
		
		if(n.outputValue.isNull()){
			this.visit(n.morePrints);
			return;
		}

		// Compute value to be printed onto stack
		this.visit(n.outputValue);
		if (n.outputValue.kind == ASTNode.Kinds.Array ||
				n.outputValue.kind == ASTNode.Kinds.ArrayParm)
			genCall("CSXLib/printCharArray([C)V");
		else if (n.outputValue.kind == ASTNode.Kinds.String)
			genCall("CSXLib/printString(Ljava/lang/String;)V");
		else{
			switch(n.outputValue.type){
			case Integer:
				genCall("CSXLib/printInt(I)V");
				break;
			case Boolean:
				genCall("CSXLib/printBool(Z)V");
				break;
			case Character:
				genCall("CSXLib/printChar(C)V");
				break;
			default:
				break; 
				
			}
		}
		this.visit(n.morePrints);
	}

	void visit(nullPrintNode n) {}

	void visit(blockNode n) {
		this.visit(n.decls);
		this.visit(n.stmts);
	}
	
	void visit(binaryOpNode n) {
		  // First translate the left and right operands
	     this.visit(n.leftOperand);
	     this.visit(n.rightOperand);
	     // Now the values of the operands are on the stack
	     // Is this a relational operator?
	     if (relationCode(n.operatorCode) == ""){
	           gen(selectOpCode(n.operatorCode));
	     } else { // relational operator
	           genRelationalOp(n.operatorCode);
	     }
	     n.adr = ASTNode.AdrMode.stack;
	}
	
	
	
	
	void visit(identNode n) {
	
	}

	void visit(intLitNode n) {
		loadI(n.intval);
		n.adr = ASTNode.AdrMode.literal;
	}

	void visit(nameNode n) {
		
		n.adr = ASTNode.AdrMode.stack;
		if (n.subscriptVal.isNull()) {
			// Simple (unsubscripted) identifier
			if (n.varName.idinfo.kind == ASTNode.Kinds.Var ||
					n.varName.idinfo.kind == ASTNode.Kinds.Value ||
					n.varName.idinfo.kind == ASTNode.Kinds.ScalarParm) {
				// id is a scalar variable, parameter or const
				if (n.varName.idinfo.adr == ASTNode.AdrMode.global){
					// id is a global
					String label = n.varName.idinfo.label;
					loadGlobalInt(label);
				} else { // (n.varName.idinfo.adr == Local)
					n.varIndex = n.varName.idinfo.varIndex;
					loadLocalInt(n.varIndex);
					
				} } else  { // varName is an array var or array parm
					if (n.varName.idinfo.adr == ASTNode.AdrMode.global){
						n.label = n.varName.idinfo.label;
						loadGlobalReference(n.label,
								arrayTypeCode(n.varName.idinfo.type));
					} else { // (n.varName.idinfo.adr == local)
						n.varIndex = n.varName.idinfo.varIndex;
						loadLocalReference(n.varIndex);
					}}
		} else { // This is a subscripted variable
			// Push array reference first
			if (n.varName.idinfo.adr == ASTNode.AdrMode.global){
				n.label = n.varName.idinfo.label;
				loadGlobalReference(n.label,
						arrayTypeCode(n.varName.idinfo.type));
			} else { // (n.varName.idinfo.adr == local)
				n.varIndex = n.varName.idinfo.varIndex;
				loadLocalReference(n.varIndex);
			} // Next compute subscript expression
			this.visit(n.subscriptVal);
			// Now load the array element onto the stack
			switch(n.type){

			case Integer:
				// Generate: iaload
				gen("iaload");
				break;
			case Boolean:
				// Generate: baload
				gen("baload");
				break;
			case Character:
				// Generate: caload
				gen("caload");
				break;
			default:
				break;
			}
		}
	}
	
	
	boolean isNumericLit(exprNodeOption e){
		return (e instanceof intLitNode) || (e instanceof charLitNode) ||
		(e instanceof trueNode) ||
		(e instanceof falseNode); 
		}
	
int getLitValue(exprNode e){
	
		if (e instanceof intLitNode)
			return e.intVal;
		
		else if (e instanceof charLitNode)
		return ((charLitNode) e).charval; 
		
		else if (e instanceof trueNode)
		return 1;
		
		else if (e instanceof falseNode)
		return 0; 
		
		else
			return 0;
		
}


void declGlobalInt(String name, exprNodeOption initValue){ 
	if (isNumericLit(initValue)){
		exprNode a = (exprNode) initValue;
		int numValue = getLitValue(a); 
		// Generate a field declaration with initial value: 
		//.field public static name I = numValue
		String field = ".field public static";
		
		gen(field, name, numValue );
	}
	else{

		// Gen a field declaration without an initial value: 
		//.field public static name I
		String field = ".field public static";
		gen(field,name,"I");
	}

}


String typeCode(typeNode type){
    // Return type code
    if (type instanceof intTypeNode)
          return "I";
    else if (type instanceof charTypeNode)
          return "C";
    else if (type instanceof boolTypeNode)
          return "Z";
    else // (type instanceof voidTypeNode)
          return "V";
}


String typeCode(ASTNode.Types type){
	// Return type code
	switch (type){
	case Integer:
		return "I";
	case Character:
		return "C";
	case Boolean:
		return "Z";
	case Void:
		return "V";
	default: 
		return "";

	} 
	}


String arrayTypeCode(typeNode type){ // Return array type code
	if (type instanceof intTypeNode)
		return "[I";
	else if (type instanceof charTypeNode)
		return "[C";
	else // (type instanceof boolTypeNode)
		return "[Z";
}

String arrayTypeCode(ASTNode.Types type){
    // Return array type code
    switch(type){
         case Integer: return "[I";
         case Character: return "[C";
         case Boolean: return "[Z"; 
         default: return "";
         }
}



String buildTypeCode(argDeclNode n) {
	if (n instanceof valArgDeclNode){
		return typeCode(((valArgDeclNode) n).argType);
	}
	else{ // must be an arrayArgDeclNode

		return arrayTypeCode(((arrayArgDeclNode) n).elementType);
	}
}


String buildTypeCode(argDeclsNode n){
    if (n.moreDecls.isNull())
          return buildTypeCode(n.thisDecl);
    else
          return buildTypeCode(n.thisDecl)+
                 buildTypeCode((argDeclsNode) n.moreDecls);
}


String buildTypeCode(exprNode n){
    if (n.kind == ASTNode.Kinds.Array)
          return arrayTypeCode(n.type);
    else return typeCode(n.type);
}
String buildTypeCode(argsNode n){
    if (n.moreArgs.isNull())
          return buildTypeCode(n.argVal);
    else return buildTypeCode(n.argVal) +
                buildTypeCode((argsNode) n.moreArgs);
}
String buildTypeCode(String methodName,
                    argsNodeOption args, String returnCode){
    String newTypeCode = methodName;
    if (args.isNull())
          newTypeCode = newTypeCode + "()";
    else newTypeCode = newTypeCode + "(" +
                        buildTypeCode((argsNode) args) + ")";
    return newTypeCode + returnCode;
}


void declGlobalArray(String name, typeNode type){ 
	
	
	// Generate a field declaration for an array:
	// .field public static name arrayTypeCode(type)
	String code = ".field public static";
	gen(code,name,arrayTypeCode(type));
}


void allocateArray(typeNode type){ 
	
	if (type instanceof intTypeNode){
		//
		gen("newarray","int");
	
	}
	else if (type instanceof charTypeNode){
		
		gen("newarray","char");
	}
		else{
			// (type instanceof boolTypeNode)
			// Gen a newarray instruction for a boolean array: // newarray boolean
			gen("newarray","boolean");

		}
}
void storeGlobalReference(String name, String typeCode){ // Generate a store of a reference from the stack into // a static field:
	// putstatic CLASS/name typeCode
		afile.println("putstatic " + CLASS + "/"+name + " " + typeCode);

}
void storeLocalReference(int index){

	//Generate a store of a reference from the stack into a local variable:
	
	gen("astore",index);
}



void declField(varDeclNode n){
String varLabel = n.varName.idname +"$"; 
declGlobalInt(varLabel,n.initValue); 
n.varName.idinfo.label = varLabel; 
n.varName.idinfo.adr = ASTNode.AdrMode.global;
}
void declField(constDeclNode n){
String constLabel = n.constName.idname +"$"; 
declGlobalInt(constLabel,n.constValue); n.constName.idinfo.label = constLabel; 
n.constName.idinfo.adr = ASTNode.AdrMode.global;
}
void declField(arrayDeclNode n){
String arrayLabel = n.arrayName.idname +"$"; 
declGlobalArray(arrayLabel,n.elementType); 
n.arrayName.idinfo.label = arrayLabel; 
n.arrayName.idinfo.adr = ASTNode.AdrMode.global;
}







void visit(classNode n) {

	 currentMethod = null; // We're not in any method body (yet)
     CLASS = n.className.idname;
     
     // generate:
     gen(".class","public",CLASS);
     gen(".super","java/lang/Object");
     
     // Generate field declarations for the class
     this.visit(n.members.fields);

     String info = ".method public static";
     gen(info,"main([Ljava/lang/String;)V");
     
     // Generate non-trivial field initializations
     
     this.visit(n.members.fields);
     // generate:
     gen("invokestatic",CLASS+"/main()V");
     gen("return");
     gen(".limit","stack",2);
     gen(".end","method");
    
     this.visit(n.members.methods);
 
}

void visit(memberDeclsNode n) {


	//do fields then methods
	if(n.fields != null){
		this.visit(n.fields);
	}
	if(!n.mem.isNull()){
		this.visit(n.mem);
	}

	if(!n.mem.methods.isNull()){
		this.visit(n.methods);
	}

}

	
	void visit(valArgDeclNode n) {
		 // Label method argument with its address info
	     n.argName.idinfo.adr = ASTNode.AdrMode.local;
	     n.argName.idinfo.varIndex = currentMethod.name.idinfo.numberOfLocals++;

	}

	void visit(arrayArgDeclNode n) {
		 // Label method argument with its address info
	     n.argName.idinfo.adr = ASTNode.AdrMode.local;
	     n.argName.idinfo.varIndex =
	           currentMethod.name.idinfo.numberOfLocals++;

	}

	void visit(argDeclsNode n) {
		// Label each method argument with its address info
	     this.visit(n.thisDecl);
	     this.visit(n.moreDecls);

	}


	void visit(nullArgDeclsNode n) {}


	void visit(methodDeclsNode n) {


		if(n.thisDecl != null){
			if(!n.thisDecl.isNull()){
				this.visit(n.thisDecl);
			}
		}
		if(n.moreDecls != null){
			if(!n.moreDecls.isNull()){
				this.visit(n.moreDecls);
			}
		}

	}

	void visit(nullMethodDeclsNode n) {}

	void visit(methodDeclNode n) {
		currentMethod = n; // We’re in a method now!
	     n.name.idinfo.numberOfLocals = 0;
	     String newTypeCode = n.name.idname;
	     if (n.args.isNull())
	           newTypeCode = newTypeCode + "()";
	     else newTypeCode = newTypeCode + "(" +
	              buildTypeCode((argDeclsNode) n.args) + ")";
	     newTypeCode = newTypeCode + typeCode(n.returnType);
	     n.name.idinfo.methodReturnCode = typeCode(n.returnType);
	    
	     String info =".method public static";
	     gen(info,newTypeCode);
	     this.visit(n.args); // Assign local variable indices to args
	     
	     // Generate code for local decls and method body
	     this.visit(n.decls);
	     
	     this.visit(n.stmts);
	     // generate default return at end of method body
	     if (n.returnType instanceof voidTypeNode){
	    	 gen("return");
	     }
	     else { // Push a default return value of 0
	    	 loadI(0);
	    	 gen("ireturn");

	     }
	     // Generate end of method data;
	     // we’ll guestimate stack depth needed at 25
	     //  (almost certainly way too big!)
	     gen(".limit","stack",25);
	     gen(".limit","locals",n.name.idinfo.numberOfLocals);
	     gen(".end","method");
	}

	void visit(trueNode n) {
		loadI(1);
		n.adr = ASTNode.AdrMode.literal; 
		n.intVal = 1;

	}

	void visit(falseNode n) {
		loadI(0);
		n.adr = ASTNode.AdrMode.literal; 
		n.intVal = 0;

	}

	void visit(constDeclNode n) {
		if (currentMethod == null){
		
			if (n.constName.idinfo.adr == ASTNode.AdrMode.none){
				// First pass; generate field declarations
				declField(n);
			}
			else { // 2nd pass; do field initialization (if needed)
				if (! isNumericLit(n.constValue)) {
					// Compute const val onto stack and store in field this.visit(n.constValue);
					storeId(n.constName);
				} 
			}
		}
		else {
			n.constName.idinfo.varIndex =
					currentMethod.name.idinfo.numberOfLocals;
			n.constName.idinfo.adr = ASTNode.AdrMode.local;
			//   Increment numberOfLocals used in this method
			currentMethod.name.idinfo.numberOfLocals++;
			// compute and store const value
			this.visit(n.constValue);
			storeId(n.constName);
		}

	}

	void visit(arrayDeclNode n) {
		if (currentMethod == null){
			if (n.arrayName.idinfo.adr == ASTNode.AdrMode.none) {
				// First pass; generate field declarations
				declField(n);
				return;
			}
		}
		else {
			n.arrayName.idinfo.varIndex =
					currentMethod.name.idinfo.numberOfLocals;
			n.arrayName.idinfo.adr = ASTNode.AdrMode.local;
			//   Increment numberOfLocals used in this method
			currentMethod.name.idinfo.numberOfLocals++;
		}
		// Now create the array & store a reference to it 
		loadI(n.arraySize.intval); 
		//Push number of array elements 
		allocateArray(n.elementType);

		if (n.arrayName.idinfo.adr == ASTNode.AdrMode.global){
			storeGlobalReference(n.arrayName.idinfo.label, arrayTypeCode(n.elementType));
		}
		else{ 
			storeLocalReference(n.arrayName.idinfo.varIndex);
		}

	}
	
	

	void visit(readNode n) {
		// Compute address associated with target variable
		if(n.targetVar == null){
			this.visit(n.moreReads);
			return;
		}
		
		
		// Call library routine to do the read
		if (n.targetVar.varName.idinfo.type == ASTNode.Types.Integer){
			genCall("CSXLib/readInt()I");
		}
		else {// targetVar.varName.idinfo.type == Character
			genCall("CSXLib/readChar()C");
		}
		storeName(n.targetVar);
		this.visit(n.moreReads);

	}


	void visit(nullReadNode n) {}


	void visit(charLitNode n) {
		loadI(n.charval); 
		n.adr = ASTNode.AdrMode.literal; 
		n.intVal = n.charval;

	}

	void visit(strLitNode n) {
		gen("ldc", n.strval);
		

	}

	void visit(argsNode n) {
		// Evaluate arguments and load them onto stack
	     this.visit(n.argVal);
	     this.visit(n.moreArgs);

	}


	void visit(nullArgsNode n) {}

	
	void visit(unaryOpNode n) {
		
		this.visit(n.operand);
		String falseVal = genLab();
		String trueVal = genLab();
		
		
		branchZ(falseVal);
		//gen("iconst_0");
		loadI(0);
		branch(trueVal);
		defineLab(falseVal);
		loadI(1);
		//gen("iconst_1");
		defineLab(trueVal);
		
		/**
		String a = n.operand.getClass().getName();
		
		if(a.equals("nameNode")){
			
			nameNode b = (nameNode) n.operand;
			storeName(b);
		}
		*/
		n.adr = ASTNode.AdrMode.stack;
	}

	void visit(nullStmtNode n) {}


	void visit(nullExprNode n) {}


	void branchZ(String label){
		// Generate branch to label if stack top contains 0:
		//    ifeq label
		gen("ifeq",label);
	}


	void visit(whileNode n) {
		String top = genLab();
		String bottom = genLab();
	     if (! n.label.isNull()){
	           ((identNode) n.label).idinfo.topLabel = top;
	           ((identNode) n.label).idinfo.bottomLabel = bottom;
	           }
	     defineLab(top);
	     this.visit(n.condition);
	     branchZ(bottom);
	     this.visit(n.loopBody);
	     branch(top);
	     defineLab(bottom);
		
		
		

	}

	void visit(callNode n) {
		 // Evaluate args and push them onto the stack
	     this.visit(n.args);
	     // Generate call to method, using its type code
	     String typeCode =
	         buildTypeCode(n.methodName.idname, n.args,
	                       n.methodName.idinfo.methodReturnCode);
	     genCall(CLASS+ "/" + typeCode);

	}


	void visit(fctCallNode n) {
		// TODO Auto-generated method stub


		// Evaluate args and push them onto the stack
		this.visit(n.methodArgs);
		// Generate call to method, using its type code
		String typeCode =
				buildTypeCode(n.methodName.idname, n.methodArgs, n.methodName.idinfo.methodReturnCode);
		genCall(CLASS+ "/" + typeCode);

		
		//storeName(n.)
		//TODO look at adding loadI(the value), then  gen("ireturn")

	}


	void visit(returnNode n) {
		if (n.returnVal.isNull())
			gen("return");
	     else { // Evaluate return value
	           this.visit(n.returnVal);
	           gen("ireturn");
	     }

	}

	void visit(breakNode n) {
		
		 branch(n.label.idinfo.bottomLabel);

	}

	void visit(continueNode n) {
		
		 branch(n.label.idinfo.topLabel);

	}


	void visit(castNode n) {
		// First translate the operand
		this.visit(n.operand);
		// Is the operand an int or char and the resultType a bool?
		if ( ((n.operand.type == ASTNode.Types.Integer) ||
				(n.operand.type ==  ASTNode.Types.Character)) &&
				(n.resultType instanceof boolTypeNode)){
			loadI(0);
			genRelationalOp(sym.NOTEQ);
		} 
		else if ((n.operand.type == ASTNode.Types.Integer) &&(n.resultType instanceof charTypeNode)){
			loadI(127); // Equal to 1111111B
			gen("iand");

		}
	}

	void visit(incrementNode n){
		if (n.target.subscriptVal.isNull()){
			// Simple (unsubscripted) identifier
			this.visit(n.target); //Evaluate ident onto stack
			loadI(1);
			gen("iadd"); //incremented ident now on stack
			computeAdr(n.target);
			storeName(n.target);
		} else { // Subscripted array element
			computeAdr(n.target); //Push array ref and index
			gen("dup2"); // Duplicate array ref and index
			// (one pair for load, 2nd pair for store)
			// Now load the array element onto the stack
			switch(n.target.type){
			case Integer:
				gen("iaload");
				break;
			case Boolean:
				gen("baload");
				break;
			case Character:
				gen("caload");
				break;
			default:
				break;
			}
			loadI(1);
			gen("iadd"); // incremented identifier now on stack
			storeName(n.target);
		}
	}
	void visit(decrementNode n){
		if (n.target.subscriptVal.isNull()){
			// Simple (unsubscripted) identifier
			this.visit(n.target); //Evaluate ident onto stack
			loadI(1);
			gen("iadd"); //incremented ident now on stack
			computeAdr(n.target);
			storeName(n.target);
		} else { // Subscripted array element
			computeAdr(n.target); //Push array ref and index
			gen("dup2"); // Duplicate array ref and index
			// (one pair for load, 2nd pair for store)
			// Now load the array element onto the stack
			switch(n.target.type){
			case Integer:
				gen("iaload");
				break;
			case Boolean:
				gen("baload");
				break;
			case Character:
				gen("caload");
				break;
			default:
				break;
			}
			loadI(1);
			gen("isub"); // decremnt identifier now on stack
			storeName(n.target);
		}
		 }

}
