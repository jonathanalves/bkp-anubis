package anubis.hibernate;

import java.sql.Types;

import org.hibernate.dialect.PostgreSQL94Dialect;
import org.hibernate.type.StandardBasicTypes;

public class AnubisDialect extends PostgreSQL94Dialect {

	public AnubisDialect() {
		registerColumnType(Types.JAVA_OBJECT, "json");
		registerHibernateType(Types.BIGINT, StandardBasicTypes.LONG.getName() );
	}

}
