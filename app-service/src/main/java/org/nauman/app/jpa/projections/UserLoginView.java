package org.nauman.app.jpa.projections;

/**
 * To get specific columns from UserEntity 
 */
public interface UserLoginView {
	
	public Integer getId();
	
	public String getEmail();
	
	public String getPassword();
	
}
