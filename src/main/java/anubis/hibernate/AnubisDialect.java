package anubis.hibernate;

import org.hibernate.dialect.PostgreSQL94Dialect;
import org.hibernate.type.StandardBasicTypes;

import java.sql.Types;

public class AnubisDialect extends PostgreSQL94Dialect {

	public AnubisDialect() {
		registerColumnType(Types.JAVA_OBJECT, "json");
		registerHibernateType(Types.BIGINT, StandardBasicTypes.LONG.getName() );
	}

}
