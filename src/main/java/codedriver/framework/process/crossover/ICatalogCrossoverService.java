package codedriver.framework.process.crossover;

import codedriver.framework.crossover.ICrossoverService;

/**
 * @author longrf
 * @date 2022/1/14 4:27 下午
 */
public interface ICatalogCrossoverService extends ICrossoverService {
    /**
     * @param channelUuid
     * @return boolean
     * @Time:2020年7月7日
     * @Description: 判断当前用户是否有channelUuid服务的上报权限，根据服务是否激活，服务是否授权，服务的所有上级目录是否都授权来判断
     */
    public boolean channelIsAuthority(String channelUuid, String userUuid);
}
