package wyye.javafilm.objects;

public class Person {
	private long id;
	private String name;
	private String gender; 
	private long birthdate;
	private String biography;
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
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public long getBirthdate() {
		return birthdate;
	}
	public void setBirthdate(long birthdate) {
		this.birthdate = birthdate;
	}
	public String getBiography() {
		return biography;
	}
	public void setBiography(String biography) {
		this.biography = biography;
	}
	@Override
	public String toString() {
		return "Person [id=" + id + ", name=" + name + ", gender=" + gender + ", birthdate=" + birthdate
				+ ", biography=" + biography + "]";
	}
	public Person(long id, String name, String gender, long birthdate, String biography) {
		super();
		this.id = id;
		this.name = name;
		this.gender = gender;
		this.birthdate = birthdate;
		this.biography = biography;
	}
}
