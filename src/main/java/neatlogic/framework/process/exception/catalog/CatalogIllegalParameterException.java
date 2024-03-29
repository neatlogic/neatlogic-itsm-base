package neatlogic.framework.process.exception.catalog;

import neatlogic.framework.exception.core.ApiRuntimeException;

public class CatalogIllegalParameterException extends ApiRuntimeException {

	private static final long serialVersionUID = 183789473205326514L;

	public CatalogIllegalParameterException(String name) {
		super("服务目录：“{0}”还存在子服务", name);
	}
}
