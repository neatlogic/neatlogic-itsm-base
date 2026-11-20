package neatlogic.framework.process.constvalue;

import neatlogic.framework.knowledge.dto.SyncSourceVo;
import neatlogic.framework.knowledge.source.ISyncSource;
import neatlogic.framework.util.$;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import neatlogic.framework.crossover.CrossoverServiceFactory;
import neatlogic.framework.knowledge.crossover.IKnowledgeProcessTaskAccess;

public enum KnowledgeSyncSource implements ISyncSource{
    PROCESSTASK("processtask", "nfpc.knowledgesyncsource.text.processtask");

    private String value;
    private String name;

    private KnowledgeSyncSource(String _value, String _name) {
        this.value = _value;
        this.name = _name;
    }

    /** 从工单详情查询时，由工单模块鉴权并收集各节点的来源记录。 */
    @Override
    public Map<String, List<Long>> listOrigins(String source, Long invokeId) {
        return CrossoverServiceFactory.<IKnowledgeProcessTaskAccess>getApi(IKnowledgeProcessTaskAccess.class).listOrigins(invokeId);
    }

    /** 手动转换的所有入口在同一个工单上串行化。 */
    @Override
    public void checkConversion(Long invokeId) {
        CrossoverServiceFactory.<IKnowledgeProcessTaskAccess>getApi(IKnowledgeProcessTaskAccess.class).checkConversion(invokeId);
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return $.t(name);
    }

    public static String getValue(String _value) {
        for (KnowledgeSyncSource s : KnowledgeSyncSource.values()) {
            if (s.getValue().equals(_value)) {
                return s.getValue();
            }
        }
        return null;
    }

    public static String getName(String _value) {
        for (KnowledgeSyncSource s : KnowledgeSyncSource.values()) {
            if (s.getValue().equals(_value)) {
                return s.getName();
            }
        }
        return "";
    }

    @Override
    public List<SyncSourceVo> getSyncSource() {
        List<SyncSourceVo> list = new ArrayList<>();
        for (KnowledgeSyncSource s : KnowledgeSyncSource.values()) {
           SyncSourceVo syncSource = new SyncSourceVo();
           syncSource.setSource(s.getValue());
           syncSource.setSourceName(s.getName());
           list.add(syncSource);
        }
        return list;
    }
    
}
