import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Vector;

public class Controlador {

	public Controlador() {

	}

	String[][] MatrizA;
	String[][] MatrizB;

	String textoCrifrar = "";
	String textoPosicionesPerforaciones = "";
	int tamanoMatriz = 0;
	int numeroPerforaciones = 0;

	public void ejecutarCifrado(String rutaArchivo, String rutaMascaras) {

		try {
			String[] abecedario = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q",
					"R", "S", "T", "U", "V", "W", "X", "Y", "Z" };

			// capturar texto a cifrar
			textoCrifrar = leerArchivo(rutaArchivo);
			numeroPerforaciones = (int) (textoCrifrar.length() / 4.0);
			int tamanoTextoAcifrar = textoCrifrar.length();
			tamanoMatriz = (int) Math.ceil(Math.sqrt(tamanoTextoAcifrar));

			textoPosicionesPerforaciones = leerArchivo(rutaMascaras);
			// calcular tamanho de la matriz

			// tamanoMatriz = 990;
			// declaro el tamanho de las matrices de acuerdo al valor calculado

			// asignar las perforaciones en la matriz B
			crearMatrizPerforacion(rutaMascaras);

			int auxLetra = 0;
			StringBuilder errores = new StringBuilder();
			for (int k = 1; k <= 4; k++) {
				System.out.println("Inicio matriz " + k);

				StringBuilder lines = new StringBuilder();

				// Recorrer la matriz B que contiene las posiciones con las perforaciones, y
				// asignar esas posiciones a la matriz A
				// se realiza proceso de validacion entre las matrices, que no se intercepten al
				// rotar y no sobreescriba una posicion
				for (int i = 0; i < tamanoMatriz; i++) {
					for (int j = 0; j < tamanoMatriz; j++) {
						if (MatrizB[i][j] == null) {
							lines.append("-");
						} else {
							lines.append(MatrizB[i][j].toString());

							if (MatrizA[i][j] != null) {
								errores.append("La rotacion " + k + " de la matriz se intercepta en la posicion " + i
										+ " , " + j + " Revisar la mascara" + "\n");
							}
							MatrizA[i][j] = MatrizB[i][j].toString();
						}
						lines.append("");
					}
					lines.append("\n");
				}

				if (!errores.toString().equals("")) {
					System.out.println("Se genero la siguiente incidencia:");
					System.out.println(errores.toString());
					return;
				}

				// imprimir en archivo txt la matriz X como se ve con las posiciones de las
				// perforaciones
				// PrintWriter writer = new PrintWriter("matriz_" + k + ".cifrado", "UTF-8");
				// writer.print(lines);
				// writer.close();

				lines = new StringBuilder();

				// escritura
				// En la matriz A, voy asignando los caracteres del texto a encriptar
				for (int i = 0; i < tamanoMatriz; i++) {
					for (int j = 0; j < tamanoMatriz; j++) {
						if (MatrizB[i][j] == null) {
							lines.append("-");
						} else {

							if (MatrizB[i][j].toString().equals("X")) {
								if (auxLetra < (textoCrifrar.length())) {
									String letra = textoCrifrar.charAt(auxLetra) + "";
									MatrizA[i][j] = letra;
									lines.append(MatrizA[i][j].toString());
									auxLetra++;
								} else {
									MatrizA[i][j] = "$";
									if (MatrizA[i][j] == null)
										lines.append("-");
								}
							}
						}
						lines.append("");
					}
					lines.append("\n");
				}

				// Imprimo en txt como se genero la matriz X con los valores del texto
				// writer = new PrintWriter("Ormatriz_" + k + ".cifrado", "ISO-8859-1");
				// writer.print(lines);
				// writer.close();

				// rotar la matriz B para obtener las nuevas posiciones y reanudar al ciclo de
				// las 4 matrices
				MatrizB = rotarMatriz90Grados(MatrizB);

				System.out.println("Finaliza matriz " + k);
			}

			System.out.println("Inicia Matriz Finalizada ");
			StringBuilder textCifrado = new StringBuilder();
			// Matriz finalizada
			StringBuilder lines = new StringBuilder();
			for (int i = 0; i < tamanoMatriz; i++) {
				for (int j = 0; j < tamanoMatriz; j++) {
					if (MatrizA[i][j] == null) {
						int numRandon = (int) Math.round(Math.random() * 25);
						lines.append(abecedario[numRandon]);
						textCifrado.append(abecedario[numRandon]);
					} else {
						lines.append(MatrizA[i][j].toString());
						textCifrado.append(MatrizA[i][j].toString());
					}

					lines.append("");
				}
				lines.append("\n");
			}
			System.out.println("Finaliza Matriz Finalizada ");
			File file = new File(rutaArchivo);
			System.out.println(file.getName());
			// PrintWriter writer = new PrintWriter(file.getName().replace(".txt", "") +
			// ".cifrado","UTF-8");
			// writer.print(textCifrado.toString());
			// writer.close();

			OutputStream fout = new FileOutputStream(file.getName().replace(".txt", "") + ".cif");
			OutputStream bout = new BufferedOutputStream(fout);
			OutputStreamWriter out = new OutputStreamWriter(bout, "8859_1");
			out.write(textCifrado.toString());

			out.flush(); // Don't forget to flush!
			out.close();

		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	public void ejecutarDescifrado(String rutaArchivo, String rutaMascaras) {
		try {
			// capturar texto a cifrar
			textoCrifrar = leerArchivo(rutaArchivo);
			numeroPerforaciones = (int) (textoCrifrar.length() / 4.0);
			int tamanoTextoAcifrar = textoCrifrar.length();
			// tamanoMatriz = (int) Math.ceil(Math.sqrt(tamanoTextoAcifrar));

			textoPosicionesPerforaciones = leerArchivo(rutaMascaras);

			// asignar las perforaciones en la matriz B y verificar que no se interpolen
			// declaro el tamanho de las matrices de acuerdo al valor calculado
			crearMatrizPerforacion(rutaMascaras);

			// ciclo de 4 rotaciones
			String textoDecrifrar = leerArchivo(rutaArchivo);
			textoPosicionesPerforaciones = leerArchivo(rutaMascaras);
			int auxLe = 0;
			StringBuilder linesDes = new StringBuilder();
			for (int k = 1; k <= 1; k++) {

				for (int i = 0; i < tamanoMatriz; i++) {
					for (int j = 0; j < tamanoMatriz; j++) {
						String letra = textoDecrifrar.charAt(auxLe) + "";
						if (!letra.equals("$"))
							MatrizA[i][j] = letra;
						else
							MatrizA[i][j] = "";
						auxLe++;
					}

				}
			}

			// Matriz finalizada

			for (int i = 0; i < tamanoMatriz; i++) {
				for (int j = 0; j < tamanoMatriz; j++) {
					linesDes.append(MatrizA[i][j].toString());
				}
				linesDes.append("\n");
			}

			// ciclo de 4 matrices para obtener el mensaje en claro.
			StringBuilder textoDescifrado = new StringBuilder();
			for (int k = 1; k <= 4; k++) {
				// recorrer las matrices
				StringBuilder dlines = new StringBuilder();

				for (int i = 0; i < tamanoMatriz; i++) {
					for (int j = 0; j < tamanoMatriz; j++) {

						if (i == 1280 && j == 0) {

							String x = ";";
						}

						if (MatrizB[i][j] == null) {
							dlines.append("-");
						} else {
							if (MatrizB[i][j].toString().equals("X")) {
								textoDescifrado.append(MatrizA[i][j].toString());
							}

						}

					}
				}

				MatrizB = rotarMatriz90Grados(MatrizB);
				File file = new File(rutaArchivo);
				PrintWriter writer = new PrintWriter(file.getName().replace(".cif", "") + ".des",
						"ISO-8859-1");
				writer.print(textoDescifrado.toString());
				writer.close();
			}

			PrintWriter writer = new PrintWriter("matrizLlenaCifrada" + ".cif", "ISO-8859-1");
			writer.print(linesDes.toString());
			writer.close();

			System.out.println("Finalizo");

		} catch (Exception e) {
			System.out.print("Se genero una exepcion no controlada en el sistema. por favor ejecutar nuevamente.");
		}

	}

	public String[][] rotarMatriz90Grados(String[][] matOriginal) {
		int tamanho = matOriginal.length;
		String[][] novaMatrix = new String[tamanho][tamanho];

		for (int i = 0, j = tamanho - 1; i < tamanho && j >= 0; i++, j--) {
			for (int k = 0; k < tamanho; k++) {
				novaMatrix[k][j] = matOriginal[i][k];
			}
		}
		return novaMatrix;
	}

	public String leerArchivo(String archivo) throws IOException {
		String cadena;
		StringBuilder mensaje = new StringBuilder();
		// lectura del archivo
		FileReader f = new FileReader(archivo);

		File fi = new File(archivo);
		InputStreamReader fr = new InputStreamReader(new FileInputStream(fi), "8859_1");
		BufferedReader b = new BufferedReader(fr);

		// BufferedReader b = new BufferedReader(f);
		while ((cadena = b.readLine()) != null) {

			// valorN = (int) Math.ceil(Math.sqrt(tamanoCadena));
			mensaje.append(cadena);
		}
		b.close();
		return mensaje.toString();
	}

	public void crearMatrizPerforacion(String rutaMascara) throws Exception {

		String cadena;
		String mensaje = "";
		Vector vPosicionPerfo = new Vector();
		// lectura del archivo
		FileReader f = new FileReader(rutaMascara);
		BufferedReader b = new BufferedReader(f);
		while ((cadena = b.readLine()) != null) {
			vPosicionPerfo.add(cadena);
		}
		b.close();
		System.out.println("Tamanho de la matriz: " + vPosicionPerfo.elementAt(0));
		tamanoMatriz = Integer.parseInt(vPosicionPerfo.elementAt(0).toString());
		MatrizA = new String[tamanoMatriz][tamanoMatriz];
		MatrizB = new String[tamanoMatriz][tamanoMatriz];
		// recorrer vector Mascara 1
		for (int i = 1; i < vPosicionPerfo.size(); i++) {
			String[] aux = vPosicionPerfo.elementAt(i).toString().split(",");
			MatrizB[Integer.parseInt(aux[0])][Integer.parseInt(aux[1])] = "X";
		}
	}

	// DECIWARE
	// LEER EL ARCHIVO Y ALMACENAR EN UNA MATRIZ
	public void crearDiccionario(int nroPalabras, String idioma) {
		String txtDiccionario;
		try {
			String cadena;
			HashMap<String, String> keys = new HashMap<String, String>();
			
			
			String nombreDiccionario="";
			if(idioma.equals("es") || idioma.equals("ES") || idioma.equals("Es") ||  idioma.equals("eS"))
				nombreDiccionario="diccionario.txt";
			if(idioma.equals("en") || idioma.equals("EN") || idioma.equals("En") ||  idioma.equals("eN"))
				nombreDiccionario="diccionarioingles.txt";
			if(idioma.equals("it") || idioma.equals("It") || idioma.equals("iT") ||  idioma.equals("IT"))
				nombreDiccionario="diccionarioitaliano.txt";
			
			
			// lectura del archivo diccionarioy mapeo
			File fi = new File(nombreDiccionario);
			InputStreamReader fr = new InputStreamReader(new FileInputStream(fi), "8859_1");
			BufferedReader b = new BufferedReader(fr);

			// BufferedReader b = new BufferedReader(f);
			String[] auxiliar ;
			while ((cadena = b.readLine()) != null) {
				auxiliar = new String[2];
				auxiliar = cadena.split(" ");
				keys.put(auxiliar[0], auxiliar[1]);
			}
			b.close();
			 
			String nro;
			StringBuilder hashWord = new StringBuilder();
			System.out.println("El numero de palabras es "+nroPalabras); 
			for(int i=1; i<=nroPalabras;i++) {
			
				//ciclo de 5 lanzamientos por cada palabra
				nro="";
				for(int d=0;d<5;d++) {
					nro += ((int) (Math.random() * 6) + 1)+"";
				}
				hashWord.append(keys.get(nro));
				nro+=" "+keys.get(nro);
				System.out.println(nro);
			}
			System.out.println("Palabra secreta: "+hashWord.toString());
			
			
		}catch(

	IOException e)
	{
		System.out.println("Error en la lectura del archivo diccionario");
	}

}

}
