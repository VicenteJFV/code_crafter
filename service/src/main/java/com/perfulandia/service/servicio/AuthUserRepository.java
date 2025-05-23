package com.perfulandia.service.servicio;

import com.perfulandia.service.model.AuthUser;

public record AuthUserRepository() {

    public Object findByUsername(String username) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByUsername'");
    }

    @SuppressWarnings("unused")
    AuthUser save(AuthUser authUser) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
