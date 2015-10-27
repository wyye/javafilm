package wyye.javafilm.objects;

public class Film {
	public boolean isSerial() {
		return isSerial;
	}
	public void setSerial(boolean isSerial) {
		this.isSerial = isSerial;
	}
	@Override
	public String toString() {
		return "Film [id=" + id + ", name=" + name + ", duration=" + duration + ", description=" + description
				+ ", year=" + year + ", status=" + status + ", isSerial=" + isSerial + "]";
	}
	private long id;
	private String name;
	private String duration;
	private String description;
	private long year;
	private String status;
	private boolean isSerial;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public boolean getIsSerial() {
		return isSerial;
	}
	public void setIsSerial(boolean isSerial) {
		this.isSerial = isSerial;
	}
	public Film(long id, String name, String duration, String description, long year, String status, boolean isSerial) {
		super();
		this.id = id;
		this.name = name;
		this.duration = duration;
		this.description = description;
		this.year = year;
		this.status = status;
		this.isSerial = isSerial;
	}
}
