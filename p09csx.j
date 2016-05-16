	.class	public  p09csx
	.super	java/lang/Object
	.method public static	main([Ljava/lang/String;)V
	invokestatic	p09csx/main()V
	return
	.limit	stack  2
	.end	method
	.method public static	main()V
	ldc	"Testing program p09csx\n"
	invokestatic CSXLib/printString(Ljava/lang/String;)V
	ldc	10
	istore	0
	ldc	-10
	istore	1
	iload	0
	iload	0
	if_icmpeq	label0
	ldc	0
	goto	label1
	label0:
	ldc	1
	label1:
	istore	2
	iload	0
	iload	0
	if_icmpne	label2
	ldc	0
	goto	label3
	label2:
	ldc	1
	label3:
	istore	3
	iload	0
	iload	0
	if_icmple	label4
	ldc	0
	goto	label5
	label4:
	ldc	1
	label5:
	istore	4
	iload	0
	iload	0
	if_icmplt	label6
	ldc	0
	goto	label7
	label6:
	ldc	1
	label7:
	istore	5
	iload	0
	iload	0
	if_icmpge	label8
	ldc	0
	goto	label9
	label8:
	ldc	1
	label9:
	istore	6
	iload	0
	iload	0
	if_icmpgt	label10
	ldc	0
	goto	label11
	label10:
	ldc	1
	label11:
	istore	7
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
	iload	6
	invokestatic CSXLib/printBool(Z)V
	ldc	"\t"
	invokestatic CSXLib/printString(Ljava/lang/String;)V
	iload	7
	invokestatic CSXLib/printBool(Z)V
	ldc	"\n"
	invokestatic CSXLib/printString(Ljava/lang/String;)V
	iload	0
	iload	1
	if_icmpeq	label12
	ldc	0
	goto	label13
	label12:
	ldc	1
	label13:
	istore	2
	iload	0
	iload	1
	if_icmpne	label14
	ldc	0
	goto	label15
	label14:
	ldc	1
	label15:
	istore	3
	iload	0
	iload	1
	if_icmple	label16
	ldc	0
	goto	label17
	label16:
	ldc	1
	label17:
	istore	4
	iload	0
	iload	1
	if_icmplt	label18
	ldc	0
	goto	label19
	label18:
	ldc	1
	label19:
	istore	5
	iload	0
	iload	1
	if_icmpge	label20
	ldc	0
	goto	label21
	label20:
	ldc	1
	label21:
	istore	6
	iload	0
	iload	1
	if_icmpgt	label22
	ldc	0
	goto	label23
	label22:
	ldc	1
	label23:
	istore	7
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
	iload	6
	invokestatic CSXLib/printBool(Z)V
	ldc	"\t"
	invokestatic CSXLib/printString(Ljava/lang/String;)V
	iload	7
	invokestatic CSXLib/printBool(Z)V
	ldc	"\n"
	invokestatic CSXLib/printString(Ljava/lang/String;)V
	return
	.limit	stack  25
	.limit	locals  8
	.end	method
