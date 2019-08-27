
package com.gcloud.header.enums;

public enum ProviderType {

    GCLOUD(0),
    CINDER(1),
    GLANCE(2),
    NEUTRON(3);

    private int value;

    ProviderType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static ProviderType get(Integer value) {
        if (value != null) {
            for (ProviderType type : ProviderType.values()) {
                if (type.getValue() - value == 0) {
                    return type;
                }
            }
        }
        return null;
    }

}
