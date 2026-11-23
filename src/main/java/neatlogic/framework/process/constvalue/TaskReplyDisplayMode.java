package neatlogic.framework.process.constvalue;

import org.apache.commons.lang3.StringUtils;

/** 子任务策略的回复展示方式；旧策略缺失配置时沿用分开显示。 */
public enum TaskReplyDisplayMode {
    SEPARATE("separate"), COMBINED("combined");

    private final String value;

    TaskReplyDisplayMode(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
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
