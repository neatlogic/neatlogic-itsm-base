package neatlogic.framework.process.constvalue;

import com.alibaba.fastjson.JSONObject;
import neatlogic.framework.common.constvalue.IEnum;
import neatlogic.framework.util.$;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/** 子任务策略的回复展示方式；旧策略缺失配置时沿用分开显示。 */
public enum TaskReplyDisplayMode implements IEnum<JSONObject> {
    // 保存稳定配置值，选项文案在请求时按当前语言翻译。
    SEPARATE("separate", "nfpc.taskreplydisplaymode.text.separate"),
    COMBINED("combined", "nfpc.taskreplydisplaymode.text.combined");

    private final String value;
    private final String text;

    TaskReplyDisplayMode(String value, String text) {
        this.value = value;
        this.text = text;
    }

    @Override
    public String getValue() {
        return value;
    }

    /** 延迟翻译，避免枚举初始化时缓存某一次请求的语言。 */
    public String getText() {
        return $.t(text);
    }

    /** 供通用枚举接口返回 value/text 选项，按声明顺序保持分开在前、拼接在后。 */
    @Override
    public List<JSONObject> getValueTextList() {
        List<JSONObject> list = new ArrayList<>();
        for (TaskReplyDisplayMode mode : TaskReplyDisplayMode.values()) {
            JSONObject option = new JSONObject();
            option.put("value", mode.getValue());
            option.put("text", mode.getText());
            list.add(option);
        }
        return list;
    }

    /** 读取策略配置时容错，只有明确的 combined 才采用拼接显示。 */
    public static String normalize(String value) {
        return COMBINED.value.equals(value) ? COMBINED.value : SEPARATE.value;
    }

    /** 保存配置时允许缺省值，但拒绝未知的非空枚举值。 */
    public static boolean isValid(String value) {
        return StringUtils.isBlank(value) || SEPARATE.value.equals(value) || COMBINED.value.equals(value);
    }
}
