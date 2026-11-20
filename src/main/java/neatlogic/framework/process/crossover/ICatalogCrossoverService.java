package neatlogic.framework.process.crossover;

import neatlogic.framework.crossover.ICrossoverService;
import neatlogic.framework.process.constvalue.CatalogChannelAuthorityAction;
import neatlogic.framework.process.dto.ChannelVo;

/**
 * @author longrf
 * @date 2022/1/14 4:27 下午
 */
public interface ICatalogCrossoverService extends ICrossoverService {
    /** 按目录顺序取得上报服务的展示路径；父目录缺失时返回 null。 */
    String getChannelPath(ChannelVo channel);

    /**
     * @param channelUuid
     * @param userUuid    用户uuid
     * @param action      授权类型
     * @return boolean
     * @Time:2020年7月7日
     * @Description: 判断当前用户是否有channelUuid服务的action权限，根据服务是否激活，服务是否授权，服务的所有上级目录是否都授权来判断
     */
    public boolean channelIsAuthority(String channelUuid, String userUuid, CatalogChannelAuthorityAction action);
}
