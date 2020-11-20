package codedriver.framework.process.dao.mapper;

import codedriver.framework.process.dto.ProcessTaskConfigVo;
import codedriver.framework.process.dto.ProcessTaskContentVo;

public interface SelectContentByHashMapper {

    public ProcessTaskConfigVo getProcessTaskConfigByHash(String hash);

    public String getProcessTaskStepConfigByHash(String hash);

    public ProcessTaskContentVo getProcessTaskContentByHash(String hash);
    
    public String getProcessTaskContentStringByHash(String hash);

    public String getProcessTaskFromContentByHash(String hash);

    public int checkProcessTaskScoreTempleteConfigIsExists(String hash);
    
    public String getProcessTaskScoreTempleteConfigStringIsByHash(String hash);

    public String getProcessTaskConfigStringByHash(String configHash);
}
