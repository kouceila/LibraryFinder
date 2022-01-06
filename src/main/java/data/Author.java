package data;

import java.io.Serializable;
import java.util.Objects;

import org.json.JSONException;
import org.json.JSONObject;

public class Author implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private int birth_year;
	private int death_year;
	
	public Author(JSONObject author) throws JSONException	  {
		this.name = author.getString("name");
		if (author.get("birth_year") != JSONObject.NULL)
			this.birth_year = author.getInt("birth_year");
		if (author.get("death_year") != JSONObject.NULL)
			this.death_year = author.getInt("death_year");
		
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getBirth_year() {
		return birth_year;
	}
	public void setBirth_year(int birth_year) {
		this.birth_year = birth_year;
	}
	public int getDeath_year() {
		return death_year;
	}
	public void setDeath_year(int death_year) {
		this.death_year = death_year;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Author other = (Author) obj;
		return Objects.equals(name, other.name);
	}
	
	
}
