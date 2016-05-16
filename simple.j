	.class	public  simple
	.super	java/lang/Object
	.method public static	main([Ljava/lang/String;)V
	invokestatic	simple/main()V
	return
	.limit	stack  2
	.end	method
	.method public static	main()V
invokestatic CSXLib/readInt()I
	istore	0
	ldc	"Answer = "
invokestatic CSXLib/printString(Ljava/lang/String;)V
	ldc	2
	iload	0
	imul
	ldc	1
	iadd
invokestatic CSXLib/printInt(I)V
	ldc	10
	return
	.limit	stack  25
	.limit	locals  1
	.end	method
