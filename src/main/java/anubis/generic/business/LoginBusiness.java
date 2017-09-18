package anubis.generic.business;

import org.springframework.stereotype.Service;

import anubis.generic.bean.SimpleGenericBean;
import anubis.generic.dao.GenericDAO;
import anubis.generic.security.AnubisUserSecurity;

@Service("loginBusiness")
public abstract class LoginBusiness<T extends SimpleGenericBean> extends GenericDAO<T> {
	
	public abstract AnubisUserSecurity login(String login);

}
