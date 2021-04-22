package es.uvigo.esei.dgss.exercises.domain;

import java.util.Date;

import javax.persistence.Entity;

@Entity
public class Photo extends Post {
	
	protected String content;

	Photo() {}
	
	public Photo(String id, Date date, User user, String content) {
		super(id, date, user);
		this.content = content;
	}
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}