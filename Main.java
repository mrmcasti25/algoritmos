import java.io.File;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Controlador ctr = new Controlador();
		int nroArgumentos = args.length;
		

		try {
			if (nroArgumentos == 0 || nroArgumentos == 1) {
				menu(1);
			}

			
			if (nroArgumentos == 3 || nroArgumentos==2)  {
				if (args[0].toString().equals("-mr") && args[1].toString().equals("-a")) {
					menu(2);
				} else if (args[0].toString().equals("-dc")) {
					if (args[1].toString().equals("-a"))
						menu(3);
					else {
						if (args[1].toString().equals("5") || args[1].toString().equals("6")
								|| args[1].toString().equals("7")) {
							
							if (args[2].toString().equals("it") || args[2].toString().equals("en")
									|| args[2].toString().equals("es")) {
								ctr.crearDiccionario(Integer.parseInt(args[1].toString()), args[2].toString());
							} else {
								System.out.println("Soporta solo idiomas en, it, es");
							}
							
						} else {
							menu(3);
						}

					}
				} else {
					menu(1);
				}
			}
			//if (nroArgumentos == 2 || (nroArgumentos == 3 && !args[0].toString().equals("-dc"))) {
			//	System.out.println("Error en la sintaxis de ejecucion.");

				//Thread.sleep(2000);
				//menu(2);
			//}
			if (nroArgumentos == 4) {
				// cifrar
				if (args[0].equals("-mr") && args[1].equals("-c")) {

					// verifica que exista el archivo a cifrar
					File info1 = new File(args[2]);
					if (!info1.exists()) {
						System.out.println("No existe el archivo " + args[2]);
						Thread.sleep(2000);
						menu(2);
						return;
					}

					// verificar que exista el diccionario
					info1 = new File(args[3]);
					if (!info1.exists()) {
						System.out.println("No existe el archivo " + args[3]);
						Thread.sleep(2000);
						menu(2);
						return;
					}
					long startTime = System.currentTimeMillis();
					ctr.ejecutarCifrado(args[2], args[3]);
					long totalSum = 0;
					;
					totalSum += (System.currentTimeMillis() - startTime);
					System.out.println("Tiempo de ejecucion: " + totalSum + " Milisegundos");

				} else if (args[0].equals("-mr") && args[1].equals("-d")) {

					// verificar que exista el archivo a descifrar
					File info1 = new File(args[2]);
					if (!info1.exists()) {
						System.out.println("No existe el archivo " + args[2]);
						Thread.sleep(2000);
						menu(2);
						return;
					}
					// verificar que exista la mascara
					info1 = new File(args[3]);
					if (!info1.exists()) {
						System.out.println("No existe el archivo " + args[3]);
						Thread.sleep(2000);
						menu(2);
						return;
					}

					long startTime = System.currentTimeMillis();
					ctr.ejecutarDescifrado(args[2], args[3]);
					long totalSum = 0;
					;
					totalSum += (System.currentTimeMillis() - startTime);
					System.out.println("Tiempo de ejecucion: " + totalSum + " Milisegundos");
				} else {
					menu(2);
				}
			}
			if (nroArgumentos > 4) {
				menu(1);
			}

		} catch (Exception e) {
			String[] g = new String[0];
			main(g);
		}

	}

	public static void menu(int imenu) {

		if (imenu == 1) {
			System.out.println("********** ALGORITMOS CRIPTOGRAFICOS ************");
			System.out.println("Sintaxis: java main <algoritmo>");
			System.out.println();
			System.out.println("<algoritmo>:");
			System.out.println("    -mr  Mascara Rotativa");
			System.out.println("    -dc  Deciware");
			System.out.println();
			System.out.println();
			System.out.println("Cosultar algoritmo especifico:");
			System.out.println("                            Sintaxis:  java Main <algoritmo> -a");

		}
		// menu mascara rotativa ayuda
		if (imenu == 2) {
			System.out.println("*********************** MASCARA ROTATIVA ***********************");
			System.out.println();
			System.out.println("Sintaxis: java Main -mr <modo> <archivoEntrada> <archivomascara>");
			System.out.println();
			System.out.println("<modo>:");
			System.out.println("       -c  cifrar archivo");
			System.out.println("       -d  descifrar archivo");
			System.out.println();
			System.out.println("       <archivoEntrada> : Nombre del archivo a cifrar o descifrar");
			System.out.println("       <archivoMascara> : Nombre del archivo que contiene la mascara");
			System.out.println("       Si <modo> -c , el archivo de salida es <archivoEntrada>.cifrado");
			System.out.println("       Si <modo> -d , el archivo de salida es <archivoEntrada>.descifrado");
			System.out.println();
			System.out.println("Ejemplo:");
			System.out.println("       Cifrar:      java Main -mr -c quijote.txt mascaraquijote.txt");
			System.out.println("       Descifrar:   java Main -mr -d quijote.cifrado mascaraquijote.txt");
		}
		if (imenu == 3) {
			System.out.println("********** CONTRASENHA DECIWARE  ************");
			System.out.println();
			System.out.println("Sintaxis: java Main -dc <numeroPalabras> <idioma>");
			System.out.println();
			System.out.println("<Nota>:");
			System.out.println("      Para ejercicio practico el numero de palabra valida esta entre 5, 6 y 7.");
			System.out.println("      Los idiomas soportados son Ingles y espanhol e Italiano");
			System.out.println("Ejemplo:");
			System.out.println("      para espanhol: java Main -dc 5 es");
			System.out.println("      para ingles:   java Main -dc 5 en");
			System.out.println("      para ingles:   java Main -dc 5 it");
		}
	}

}
