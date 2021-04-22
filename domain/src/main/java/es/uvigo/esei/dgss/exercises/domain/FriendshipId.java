package es.uvigo.esei.dgss.exercises.domain;

import java.io.Serializable;

public class FriendshipId implements Serializable{

	private String user1;
	private String user2;
	
	public FriendshipId() { }
	
	public FriendshipId(String user1, String user2) {
		this.user1 = user1;
		this.user2 = user2;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((user1 == null) ? 0 : user1.hashCode());
		result = prime * result + ((user2 == null) ? 0 : user2.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FriendshipId other = (FriendshipId) obj;
		if (user1 == null) {
			if (other.user1 != null)
				return false;
		} else if (!user1.equals(other.user1))
			return false;
		if (user2 == null) {
			if (other.user2 != null)
				return false;
		} else if (!user2.equals(other.user2))
			return false;
		return true;
	}
}
