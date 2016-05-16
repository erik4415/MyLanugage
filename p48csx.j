	.class	public  p48csx
	.super	java/lang/Object
	.method public static	main([Ljava/lang/String;)V
	invokestatic	p48csx/main()V
	return
	.limit	stack  2
	.end	method
	.method public static	main()V
	ldc	27
	newarray	char
	astore	0
aload 0
	ldc	"This is a string assignment"
	invokestatic CSXLib/convertString(Ljava/lang/String;)[C
	invokestatic CSXLib/checkCharArrayLength([C[C)[C
	astore	0
aload 0
	invokestatic CSXLib/printCharArray([C)V
	ldc	"\n"
	invokestatic CSXLib/printString(Ljava/lang/String;)V
	return
	.limit	stack  25
	.limit	locals  1
	.end	method
