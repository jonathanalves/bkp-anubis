package anubis.generic.test;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;

public class UserSecurityTest extends UsernamePasswordAuthenticationToken {

	private static final long serialVersionUID = 1L;
	
	private User userSecurity;
	
	public UserSecurityTest(User userSecurity) {
		super(userSecurity.getUsername(), userSecurity.getPassword(), userSecurity.getAuthorities());
		this.userSecurity = userSecurity;
	}
	
	@Override
	public Object getPrincipal() {
		return userSecurity;
	}
	
}
