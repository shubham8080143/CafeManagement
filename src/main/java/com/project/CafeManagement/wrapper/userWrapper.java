package com.project.CafeManagement.wrapper;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class userWrapper {
	private int id;

	private String email;

	private String contactnumber;

	private String password;

	private String status;

	private String role;

	public userWrapper(int id, String email, String contactnumber, String password, String status, String role) {
		super();
		this.id = id;
		this.email = email;
		this.contactnumber = contactnumber;
		this.password = password;
		this.status = status;
		this.role = role;
	}

}
