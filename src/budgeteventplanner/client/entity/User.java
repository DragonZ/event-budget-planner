package budgeteventplanner.client.entity;

import javax.persistence.Id;

import com.googlecode.objectify.annotation.Entity;

@Entity
public class User {
	@Id
	private String email;
	private String name;
	private int role;
	private String password;
	private String company;
	private String address;

	public User() {}
	
	public static class Builder {
		private User user = new User();

		public Builder(User user) {
			this.user = user;
		}

		public Builder(String email, String password, int role) {
			this.user.email = email;
			this.user.role = role;
			this.user.password = password;
		}

		public Builder SetCompany(String company) {
			this.user.company = company;
			return this;
		}

		public Builder setAddress(String address) {
			this.user.address = address;
			return this;
		}

		public Builder name(String name) {
			this.user.name = name;
			return this;
		}

		public User build() {
			return this.user;
		}
	}

	public String getEmail() {
		return email;
	}

	public String getName() {
		return name;
	}

	public Integer getRole() {
		return role;
	}

	public String getPassword() {
		return password;
	}

	public String getPompany() {
		return company;
	}

	public String getAddress() {
		return address;
	}

}
