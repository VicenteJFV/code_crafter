package com.perfulandia.service.common;


public enum Role {
    ADMIN,
    USER, User;

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return super.toString();
    }

    public static Role fromString(String role) {
        for (Role r : Role.values()) {
            if (r.name().equalsIgnoreCase(role)) {
                return r;
            }
        }
        throw new IllegalArgumentException("No enum constant " + Role.class.getCanonicalName() + "." + role);
    }
}

