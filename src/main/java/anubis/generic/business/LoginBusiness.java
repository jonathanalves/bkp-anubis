package anubis.generic.business;

import anubis.generic.bean.SimpleGenericBean;
import anubis.generic.dao.GenericDAO;
import anubis.generic.security.AnubisUserSecurity;
import org.springframework.stereotype.Service;

@Service("loginBusiness")
public abstract class LoginBusiness<T extends SimpleGenericBean> extends GenericDAO<T> {
	
	public abstract AnubisUserSecurity login(String login);

}
