nop # Inicializar variable i
	lw $t0, defaultValueInt
	push($t0)
	nop # Inicializar variable separator
	nop # AllocateMemory
	li $a0, 8
	li $v0, 9
	syscall
	move $a0, $v0
	la $t0, VT_Str
	sw $t0, 0($a0)
	la $t0, defaultValueStr
	sw $t0, 4($a0)
	la $t0, ($a0)
	push($t0)
	nop # Acceder a direccion del lado izquierdo
	la $a0, -8($fp)

	nop # Pushear la direccion obtenida
	push($a0)
	nop # Generar el lado derecho de la asignacion

.text
	nop # AllocateMemory
	li $a0, 8
	li $v0, 9
	syscall
	move $a0, $v0
	la $t0, VT_Str
	sw $t0, 0($a0)
	la $t0, literal_array_separator
	sw $t0, 4($a0)
	la $t0, ($a0)
	la $a0, ($t0)

	nop # Popear la direccion del lado izquierdo
	pop($t0)
	nop # Guardar el valor en la direccion obtenida
	sw $a0, 0($t0)
	nop # Generar codigo de bucle while
while_print_array_bool:
	nop # Generar codigo para condicion de bucle while
	nop # Generar codigo para operando izquierdo de operacion binaria
lw $a0, -4($fp)
	push($a0)
	nop # Generar codigo para operando derecho de operacion binaria
	nop # Generar codigo para operando izquierdo de operacion binaria
	nop # Generate code for the parent node
	lw $a0, 4($fp)

	nop # Check Nil pointer
	beq $a0, $zero, exception_nil_pointer
	nop # Start method call length
	push($fp)
	nop # Pushear self
	push($a0)
	nop # Pushear parametros
	nop # Obtener ref a metodo
	lw $a0, 4($sp)
	lw $a0, ($a0)
	lw $t1, 0($a0)
jalr $t1
	addiu $sp, $sp, 0
	pop($t0)
	pop($fp)


	push($a0)
	nop # Generar codigo para operando derecho de operacion binaria

.text
	la $a0, literal_one
	la $t0, ($a0)
	lw $a0, ($t0)

	pop($t0)
	nop # Generar codigo para la operacion binaria
sub $a0, $t0, $a0

	pop($t0)
	nop # Generar codigo para la operacion binaria
slt $a0, $t0, $a0

	nop # Si la condicion es falsa, saltar al final del bucle
beqz $a0, end_while_print_array_bool
	nop # Generar codigo para cuerpo del bucle while
	nop # Generar codigo de bloque
	nop # Start method call out_bool
	push($fp)
	nop # access to array
	lw $a0, 4($fp)

	nop # Check Nil pointer
	beq $a0, $zero, exception_nil_pointer
	lw $t0, 8($a0)
	push($t0)
	lw $t0, 4($a0)
	push($t0)
	nop # Calculate index expression
lw $a0, -4($fp)
slt $s0, $a0,  $zero
bne $s0, $zero, exception_index_out_of_bounds
	pop($t1)
slt  $s0, $t1, $a0
bne $s0, $zero, exception_index_out_of_bounds
	pop($t0)
mul $a0, $a0, 4
	add $t0, $t0, $a0

	lw $a0, ($t0)

	push($a0)
	la $t0, VT_IO
	lw $t1, 8($t0)
jalr $t1
	addiu $sp, $sp, 4
	pop($fp)

	nop # Start method call out_str
	push($fp)
lw $a0, -8($fp)
	push($a0)
	la $t0, VT_IO
	lw $t1, 0($t0)
jalr $t1
	addiu $sp, $sp, 4
	pop($fp)

	nop # Generar codigo para operando de expresion unaria
	la $a0, -4($fp)

	nop # Generar codigo para la operacion unaria
	lw $t1, 0($a0)
addi $t1, $t1, 1
	sw $t1, 0($a0)
	move $a0, $t1



	nop # Volver al inicio del bucle
j while_print_array_bool
end_while_print_array_bool:
	nop # Start method call out_bool
	push($fp)
	nop # access to array
	lw $a0, 4($fp)

	nop # Check Nil pointer
	beq $a0, $zero, exception_nil_pointer
	lw $t0, 8($a0)
	push($t0)
	lw $t0, 4($a0)
	push($t0)
	nop # Calculate index expression
lw $a0, -4($fp)
slt $s0, $a0,  $zero
bne $s0, $zero, exception_index_out_of_bounds
	pop($t1)
	slt  $s0, $t1, $a0
	bne $s0, $zero, exception_index_out_of_bounds
	pop($t0)
	mul $a0, $a0, 4
	add $t0, $t0, $a0

	lw $a0, ($t0)

	push($a0)
	la $t0, VT_IO
	lw $t1, 8($t0)
	jalr $t1
	addiu $sp, $sp, 4
	pop($fp)

	addiu $sp, $sp, 8
	pop($ra)
	jr $ra
.data