package neatlogic.framework.process.exception.catalog;

import neatlogic.framework.exception.core.ApiRuntimeException;

public class CatalogNotFoundException extends ApiRuntimeException {

	private static final long serialVersionUID = 4478080139019340481L;

	public CatalogNotFoundException(String uuid) {
		super("服务目录：“{0}”不存在", uuid);
	}
}
