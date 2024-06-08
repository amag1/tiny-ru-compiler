/? 0, 1, 4hola, hola, holatrue, false, truea, a, a
start {
	Array Int x;
	Array Str y;
	Array Bool z;
	Array Char w;

	Int i;

	x = new Int[3];
	y = new Str[3];
	z = new Bool[3];
	w = new Char[3];
	while (i < x.length()) {
		x[i] = i*i;
		y[i] = "hola";
		z[i] = i%2 == 0;
		w[i] = 'a';
		(++i);
	}
	(IO.out_array_int(x));
	(IO.out_array_str(y));
	(IO.out_array_bool(z));
	(IO.out_array_char(w));
}