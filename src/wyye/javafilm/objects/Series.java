package wyye.javafilm.objects;

public class Series {
	private long id;
	private long film_id;
	private String name;
	private String description;
	private long year;
	private long season;
	private long episode;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getFilm_id() {
		return film_id;
	}
	public void setFilm_id(long film_id) {
		this.film_id = film_id;
	}
	public String getName() {
		return name;
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
	public long getYear() {
		return year;
	}
	public void setYear(long year) {
		this.year = year;
	}
	public long getSeason() {
		return season;
	}
	public void setSeason(long season) {
		this.season = season;
	}
	public long getEpisode() {
		return episode;
	}
	public void setEpisode(long episode) {
		this.episode = episode;
	}
	@Override
	public String toString() {
		return "Series [id=" + id + ", film_id=" + film_id + ", name=" + name + ", description=" + description
				+ ", year=" + year + ", season=" + season + ", episode=" + episode + "]";
	}
	public Series(long id, long film_id, String name, String description, long year, long season, long episode) {
		super();
		this.id = id;
		this.film_id = film_id;
		this.name = name;
		this.description = description;
		this.year = year;
		this.season = season;
		this.episode = episode;
	}
}
