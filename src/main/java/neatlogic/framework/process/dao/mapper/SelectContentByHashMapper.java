package neatlogic.framework.process.dao.mapper;

import neatlogic.framework.process.dto.ProcessTaskConfigVo;
import neatlogic.framework.process.dto.ProcessTaskContentVo;

import java.util.List;

public interface SelectContentByHashMapper {

    public String getProcessTaskStepConfigByHash(String hash);

    public ProcessTaskContentVo getProcessTaskContentByHash(String hash);

    List<ProcessTaskContentVo> getProcessTaskContentListByHashList(List<String> contentHashList);
    
    public String getProcessTaskContentStringByHash(String hash);

    public String getProcessTaskFromContentByHash(String hash);

    public String getProcessTaskFromContentByProcessTaskId(Long processTaskId);

    public int getProcessTaskFromContentCountByHash(String hash);

    public int checkProcessTaskScoreTempleteConfigIsExists(String hash);
    
    public String getProcessTaskScoreTempleteConfigStringIsByHash(String hash);

    public String getProcessTaskConfigStringByHash(String configHash);

    public List<ProcessTaskConfigVo> getProcessTaskConfigListByHashList(List<String> configHashList);
}
