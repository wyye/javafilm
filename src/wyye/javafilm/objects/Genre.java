package wyye.javafilm.objects;

public class Genre {
	private long id;
	private String name;
	private String description;
	public Genre(long id, String name, String description) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	@Override
	public String toString() {
		return "Genre [id=" + id + ", name=" + name + ", description=" + description + "]";
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
