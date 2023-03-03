package neatlogic.framework.process.exception.catalog;

import neatlogic.framework.exception.core.ApiRuntimeException;

public class CatalogNameRepeatException extends ApiRuntimeException {

	private static final long serialVersionUID = -4617724920030245142L;

	public CatalogNameRepeatException(String name) {
		super("exception.process.catalognamerepeatexception", name);
	}
}
