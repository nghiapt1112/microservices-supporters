package demo.domain;

import java.io.Serializable;

public class AbstractObject implements Serializable {
    private static final String[] EXCLUDED_FIELD_NAMES_FROM_TOSTRING = new String[]{"password"};

    public AbstractObject() {
    }

    public String toString() {
//        return (new ReflectionToStringBuilder(this))
//                .setExcludeFieldNames(EXCLUDED_FIELD_NAMES_FROM_TOSTRING)
//                .toString();
        return JsonUtils.toJson(this);
    }
}
