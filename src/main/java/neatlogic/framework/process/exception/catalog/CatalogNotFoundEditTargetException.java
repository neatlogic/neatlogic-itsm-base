package neatlogic.framework.process.exception.catalog;

import neatlogic.framework.exception.core.NotFoundEditTargetException;

public class CatalogNotFoundEditTargetException extends NotFoundEditTargetException {

	private static final long serialVersionUID = 4478080139019340480L;

	public CatalogNotFoundEditTargetException(String uuid) {
		super("nfpec.catalognotfoundedittargetexception.catalognotfoundedittargetexception", uuid);
	}
}
