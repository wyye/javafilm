package wyye.javafilm.objects;

public class Production {
	private long id;
	private long film_id;
	private String production_name;
	private String country;
	public Production(long id, long film_id, String production_name, String country) {
		super();
		this.id = id;
		this.film_id = film_id;
		this.production_name = production_name;
		this.country = country;
	}
	@Override
	public String toString() {
		return "Production [id=" + id + ", film_id=" + film_id + ", production_name=" + production_name + ", country="
				+ country + "]";
	}
	public long getFilm_id() {
		return film_id;
	}
	public void setFilm_id(long film_id) {
		this.film_id = film_id;
	}
	public String getProduction_name() {
		return production_name;
	}
	public void setProduction_name(String production_name) {
		this.production_name = production_name;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
}
