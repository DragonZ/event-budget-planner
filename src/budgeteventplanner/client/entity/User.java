package budgeteventplanner.client.entity;

import java.io.Serializable;

import javax.persistence.Id;

import budgeteventplanner.shared.UUID;

import com.googlecode.objectify.annotation.Entity;

@SuppressWarnings("serial")
@Entity
public class User implements Serializable {
	private String userId;
	@Id
	private String email;
	private String password;
	private Integer role;
	private String name;
	private String company;
	private String address;

	public enum UserType {
		ORGANIZER(1, "Organizer"),
		VENDOR(2, "Vendor");

		private final Integer roleId;
		private final String roleText;

		UserType(int roleId, String roleText) {
			this.roleId = roleId;
			this.roleText = roleText;
		}

		public Integer getRoleId() {
			return roleId;
		}

		public String getRoleText() {
			return roleText;
		}

		public static Integer getRoleIdByText(String roleText) {
			for (UserType type : UserType.values()) {
				if (type.getRoleText().equals(roleText)) {
					return type.getRoleId();
				}
			}
			return 0;
		}
	}

	public final static Integer ORGANIZER = 1;
	public final static Integer VENDOR = 2;
	
	public User() {
	}

	public static class Builder {
		private User user;

		public Builder(User user) {
			this.user = user;
		}

		public Builder(String email, String password, Integer role) {
			this.user = new User();
			this.user.userId = UUID.randomUUID();
			this.user.email = email;
			this.user.password = password;
			this.user.role = role;
		}

		public Builder setName(String name) {
			this.user.name = name;
			return this;
		}

		public Builder setCompany(String company) {
			this.user.company = company;
			return this;
		}

		public Builder setAddress(String address) {
			this.user.address = address;
			return this;
		}

		public User build() {
			return this.user;
		}
	}

	public String getUserId() {
		return userId;
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
