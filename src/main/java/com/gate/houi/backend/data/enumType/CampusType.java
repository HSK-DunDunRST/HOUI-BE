package com.gate.houi.backend.data.enumType;

public enum CampusType {
    ASAN("아산캠퍼스"),
    CHEONAN("천안캠퍼스");
    
    private final String displayName;
    
    CampusType(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}
