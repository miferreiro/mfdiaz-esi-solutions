package es.uvigo.esei.dgss.exercises.domain;

import java.util.Date;

import javax.persistence.Entity;

@Entity
public class Video extends Post{

	protected double duration;

	Video() {}
	
	public Video(String id, Date date, User user, double duration) {
		super(id, date, user);
		this.duration = duration;
	}
	
	public double getDuration() {
		return duration;
	}

	public void setDuration(double duration) {
		this.duration = duration;
	}
}