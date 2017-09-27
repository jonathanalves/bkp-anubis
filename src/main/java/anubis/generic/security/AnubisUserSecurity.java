package anubis.generic.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import anubis.generic.bean.SimpleGenericBean;

public abstract class AnubisUserSecurity extends User {

	private static final long serialVersionUID = 6573467542591854060L;

	private SimpleGenericBean bean;
	
	public SimpleGenericBean getBean() {
		return bean;
	}
	
	//FIXME CRIAR UMA INTERFACE PARA FAZER O LOGIN E INJETAR A MESMA PARA REMOVER O CONFIGURATION GENERIC BEAN
	public AnubisUserSecurity(SimpleGenericBean bean, String username, String password, boolean enabled, boolean accountNonExpired,
			boolean credentialsNonExpired, boolean accountNonLocked,
			Collection<? extends GrantedAuthority> authorities) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
		this.bean = bean;
	}

	public Long getId() {
		return bean.getId();
	}
	
}
