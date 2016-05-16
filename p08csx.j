	.class	public  p08csx
	.super	java/lang/Object
	.method public static	main([Ljava/lang/String;)V
	invokestatic	p08csx/main()V
	return
	.limit	stack  2
	.end	method
	.method public static	main()V
	ldc	"Testing program p08csx\n"
	invokestatic CSXLib/printString(Ljava/lang/String;)V
	ldc	1
	istore	0
	ldc	0
	istore	1
	iload	0
	iload	0
	iand
	istore	2
	iload	1
	iload	1
	iand
	istore	3
	iload	0
	iload	1
	iand
	istore	4
	iload	1
	iload	0
	iand
	istore	5
	iload	0
	invokestatic CSXLib/printBool(Z)V
	ldc	"\t"
	invokestatic CSXLib/printString(Ljava/lang/String;)V
	iload	1
	invokestatic CSXLib/printBool(Z)V
	ldc	"\n"
	invokestatic CSXLib/printString(Ljava/lang/String;)V
	iload	2
	invokestatic CSXLib/printBool(Z)V
	ldc	"\t"
	invokestatic CSXLib/printString(Ljava/lang/String;)V
	iload	3
	invokestatic CSXLib/printBool(Z)V
	ldc	"\t"
	invokestatic CSXLib/printString(Ljava/lang/String;)V
	iload	4
	invokestatic CSXLib/printBool(Z)V
	ldc	"\t"
	invokestatic CSXLib/printString(Ljava/lang/String;)V
	iload	5
	invokestatic CSXLib/printBool(Z)V
	ldc	"\n"
	invokestatic CSXLib/printString(Ljava/lang/String;)V
	iload	0
	iload	0
	ior
	istore	2
	iload	1
	iload	1
	ior
	istore	3
	iload	0
	iload	1
	ior
	istore	4
	iload	1
	iload	0
	ior
	istore	5
	iload	0
	invokestatic CSXLib/printBool(Z)V
	ldc	"\t"
	invokestatic CSXLib/printString(Ljava/lang/String;)V
	iload	1
	invokestatic CSXLib/printBool(Z)V
	ldc	"\n"
	invokestatic CSXLib/printString(Ljava/lang/String;)V
	iload	2
	invokestatic CSXLib/printBool(Z)V
	ldc	"\t"
	invokestatic CSXLib/printString(Ljava/lang/String;)V
	iload	3
	invokestatic CSXLib/printBool(Z)V
	ldc	"\t"
	invokestatic CSXLib/printString(Ljava/lang/String;)V
	iload	4
	invokestatic CSXLib/printBool(Z)V
	ldc	"\t"
	invokestatic CSXLib/printString(Ljava/lang/String;)V
	iload	5
	invokestatic CSXLib/printBool(Z)V
	ldc	"\n"
	invokestatic CSXLib/printString(Ljava/lang/String;)V
	iload	0
	ifeq	label0
	ldc	0
	goto	label1
	label0:
	ldc	1
	label1:
	istore	2
	iload	1
	ifeq	label2
	ldc	0
	goto	label3
	label2:
	ldc	1
	label3:
	istore	3
	iload	0
	invokestatic CSXLib/printBool(Z)V
	ldc	"\t"
	invokestatic CSXLib/printString(Ljava/lang/String;)V
	iload	1
	invokestatic CSXLib/printBool(Z)V
	ldc	"\t"
	invokestatic CSXLib/printString(Ljava/lang/String;)V
	iload	2
	invokestatic CSXLib/printBool(Z)V
	ldc	"\t"
	invokestatic CSXLib/printString(Ljava/lang/String;)V
	iload	3
	invokestatic CSXLib/printBool(Z)V
	ldc	"\n"
	invokestatic CSXLib/printString(Ljava/lang/String;)V
	return
	.limit	stack  25
	.limit	locals  8
	.end	method
