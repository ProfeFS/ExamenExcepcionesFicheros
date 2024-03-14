package examen;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class GestionInventarioLibros {
	private List<Libro> libros;
	private final String RUTA_INVENTARIO = "Datos\\Libros\\InventarioGeneral.txt";
	private final String RUTA_LOGS = "Datos\\Logs.txt";
	private final String RUTA_RESPALDO = "Datos\\Libros\\Respaldo\\respaldo.txt";
	private Path rutaInventario;
	private Path rutaRespaldo;
	private Path rutalogs;

	public GestionInventarioLibros() {
		rutaRespaldo = Paths.get(RUTA_RESPALDO);
		rutaInventario = Paths.get(RUTA_INVENTARIO);
		rutalogs = Paths.get(RUTA_LOGS);
		libros = new ArrayList<>();
		gestionarRutas();
		leerInventarioDeDisco();
	}

	private List<Libro> leerInventarioDeDisco() {
		try {
			if (!Files.exists(rutaInventario)) {
				Files.createFile(rutaInventario);
			}
			
			List<String> inventario = Files.readAllLines(rutaInventario);
			for (String linea : inventario) {
				libros.add(lineaToLibro(linea));
			}
		} catch (IOException e) {
			System.out.println("Error al leer archivo de disco: " + e.getMessage());
		}

		return libros;

	}

	private Libro lineaToLibro(String linea) {
		String[] lineas = linea.split(",");
		return new Libro(Integer.parseInt(lineas[0]), lineas[1], lineas[2], Integer.parseInt(lineas[3]));
	}

	private void gestionarRutas() {
		try {
			verificarDirectorio(RUTA_INVENTARIO);
			verificarDirectorio(RUTA_LOGS);
			verificarDirectorio(RUTA_RESPALDO);
		} catch (IOException e) {
			System.out.println("Error al crea el directorio: " + e.getMessage());
			e.printStackTrace();
		}

	}

	private void verificarDirectorio(String ruta) throws IOException {
		Path dir = Paths.get(ruta).getParent();
		if (!Files.exists(dir)) {
			Files.createDirectories(dir);
		}

	}

	public void agregarLibro(Libro lib) {
		Libro libro = getLibro(lib.getId());
		try {
			if (libro == null) {
				libros.add(lib);
				crearTraza("Agregado Libro con id: " + lib.getId());

			} else {
				String msg = "El libro con id: " + lib.getId() + "ya existe, se actualizará la cantidad";
				System.out.println(msg);
				libro.actualizarCantidad(lib.getCantidad());
				crearTraza(msg);
			}

		} catch (Exception e) {
			System.out.println(e);
		}

	}

	private Libro getLibro(int id) {
		for (Libro l : libros) {
			if (l.getId() == id) {
				return l;
			}
		}
		return null;
	}

	public void eliminarLibro(int id) {
		Libro libroaEliminar = getLibro(id);
		String msgLogs = null;

		try {
			if (libroaEliminar != null) {
				libros.remove(libroaEliminar);
				msgLogs = "Eliminado libro con id: " + id;
			} else {
				msgLogs = "Error al eliminar libro: " + id + "No existe";
				throw new LibroNoEncontradoException(msgLogs);
			}

		} catch (LibroNoEncontradoException e) {
			System.out.println(e);
		} finally {
			try {
				crearTraza(msgLogs);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public void guardarEnDisco() {
		StringBuffer sb = new StringBuffer();

		for (Libro lib : libros) {
			sb.append(lib.toString() + System.lineSeparator());
		}
		try {
			Files.writeString(rutaInventario, sb, StandardOpenOption.TRUNCATE_EXISTING);
			crearTraza("Guardado en disco: ");
			Files.copy(rutaInventario, rutaRespaldo, StandardCopyOption.REPLACE_EXISTING);
			crearTraza("Realizada copia de respaldo: ");

		} catch (IOException e) {
			System.out.println("Error: " + e.getMessage());
		}

	}

	private void crearTraza(String action) throws IOException {
		String log = LocalDateTime.now() + "| " + action;
		Files.writeString(rutalogs, System.lineSeparator() + log, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
	}

}
