package anubis.generic.security;

import anubis.generic.business.LoginBusiness;
import anubis.response.ResponseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Actus Description: Classe responsável pela autenticação no sistema
 *         (operação de login).
 */
@Service("anubisUserDetailsService")
@Transactional(rollbackFor = {Exception.class, Throwable.class}, timeout = 120)
public class AnubisUserDetailsService implements UserDetailsService {

	@SuppressWarnings("rawtypes")
	@Autowired
	private LoginBusiness loginBusiness;
	
	/**
	 * Description: Responsável por buscar o usuário através do seu login para
	 * realizar a autenticação no sistema.
	 */
	@Override
	public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
		try {
			return loginBusiness.login(login);
		} catch ( Exception e) {
			throw new ResponseException(e.getMessage());
		}
	}

}
