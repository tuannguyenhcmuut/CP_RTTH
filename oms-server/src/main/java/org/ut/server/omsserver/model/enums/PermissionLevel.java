package org.ut.server.omsserver.model.enums;

public enum     PermissionLevel {
    VIEW_ONLY,
    CREATE_ORDER,
    UPDATE_ORDER,
    MANAGE_ORDER,  // manage = all order permission

    CREATE_PRODUCT,
    UPDATE_PRODUCT,
    // recevier
    CREATE_RECEIVER,
    UPDATE_RECEIVER,
    // store
    CREATE_STORE,
    UPDATE_STORE,

}
