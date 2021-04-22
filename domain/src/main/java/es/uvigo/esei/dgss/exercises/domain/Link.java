package es.uvigo.esei.dgss.exercises.domain;

import java.util.Date;

import javax.persistence.Entity;

@Entity
public class Link extends Post {
	
	protected String URL;

	Link() {}

	public Link(String id, Date date, User user, String URL) {
		super(id, date, user);
		this.URL = URL;
	}

	public String getURL() {
		return URL;
	}

	public void setURL(String URL) {
		this.URL = URL;
	}
}