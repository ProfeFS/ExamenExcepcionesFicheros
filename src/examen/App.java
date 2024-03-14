package examen;

public class App {

	public static void main(String[] args) {
	GestionInventarioLibros gestor = new GestionInventarioLibros();
	
	gestor.agregarLibro(new Libro(001, "harry Potter", "CF", 20));
	gestor.agregarLibro(new Libro(002, "el patito feo", "infantil", 15));
	gestor.agregarLibro(new Libro(001, "Harry Potter Y la piedra Filosofal", "CF", 10));
	gestor.eliminarLibro(6);
	gestor.eliminarLibro(2);

	gestor.guardarEnDisco();
		

	}

}
