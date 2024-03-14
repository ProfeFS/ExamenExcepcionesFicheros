package examen;

public class Libro {
	private int id;
	private String titulo;
	private String categoria;
	private int cantidad;
	
	public Libro(int id, String titulo, String categoria, int cantidad) {
		super();
		this.id = id;
		this.titulo = titulo;
		this.categoria = categoria;
		this.cantidad = cantidad;
	}
	
	public String toString() {
		return id + "," + titulo + "," + categoria + "," + cantidad;
	}
	
	public int getId() {
		return id;
	}
	
	public void actualizarCantidad(int cantidad) {
		this.cantidad+= cantidad;
	}
	
	public int getCantidad() {
		return cantidad;
	}
	

}
