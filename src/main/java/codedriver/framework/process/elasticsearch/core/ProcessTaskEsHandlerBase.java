package codedriver.framework.process.elasticsearch.core;

import com.techsure.multiattrsearch.MultiAttrsObjectPool;

import codedriver.framework.elasticsearch.core.ElasticSearchPoolManager;
import codedriver.framework.elasticsearch.core.IElasticSearchHandler;

public abstract class ProcessTaskEsHandlerBase implements IElasticSearchHandler {
	public final static String POOL_NAME = "processtask";
	protected MultiAttrsObjectPool objectPool;
	
	protected MultiAttrsObjectPool getObjectPool() {
		if(objectPool == null) {
			return ElasticSearchPoolManager.getObjectPool(POOL_NAME);
		}
		return objectPool;
	}
}
