package neatlogic.framework.process.exception.catalog;

import neatlogic.framework.exception.core.ApiRuntimeException;

public class CatalogNotFoundException extends ApiRuntimeException {

	private static final long serialVersionUID = 4478080139019340481L;

	public CatalogNotFoundException(String uuid) {
		super("exception.process.catalognotfoundexception", uuid);
	}
}