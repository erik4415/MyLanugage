	.class	public  p06csx
	.super	java/lang/Object
	.method public static	main([Ljava/lang/String;)V
	invokestatic	p06csx/main()V
	return
	.limit	stack  2
	.end	method
	.method public static	main()V
	ldc	"Testing program p06csx\n"
invokestatic CSXLib/printString(Ljava/lang/String;)V
	ldc	1
	ifeq	label0
	ldc	"if then stmt works\n"
invokestatic CSXLib/printString(Ljava/lang/String;)V
	goto	label1
label0:
label1:
	ldc	0
	ifeq	label2
	ldc	"if then stmt DOESN'T work\n"
invokestatic CSXLib/printString(Ljava/lang/String;)V
	goto	label3
label2:
label3:
	ldc	1
	ifeq	label4
	ldc	"if then else stmt works\n"
invokestatic CSXLib/printString(Ljava/lang/String;)V
	goto	label5
label4:
	ldc	"if then else stmt DOESN'T work\n"
invokestatic CSXLib/printString(Ljava/lang/String;)V
label5:
	ldc	0
	ifeq	label6
	ldc	"if then else stmt DOESN'T work\n"
invokestatic CSXLib/printString(Ljava/lang/String;)V
	goto	label7
label6:
	ldc	"if then else stmt works\n"
invokestatic CSXLib/printString(Ljava/lang/String;)V
label7:
	return
	.limit	stack  25
	.limit	locals  0
	.end	method
