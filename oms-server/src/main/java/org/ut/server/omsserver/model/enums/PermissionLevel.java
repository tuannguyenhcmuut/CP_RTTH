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
    ;
    public String convertPermissionLevelStr() {
        switch (this) {
            case VIEW_ONLY:
                return "Chỉ xem đơn hàng";
            case CREATE_ORDER:
                return "Tạo đơn hàng";
            case UPDATE_ORDER:
                return "Cập nhật đơn hàng";
            case MANAGE_ORDER:
                return "Quản lý đơn hàng";
            case CREATE_PRODUCT:
                return "Tạo sản phẩm";
            case UPDATE_PRODUCT:
                return "Cập nhật sản phẩm";
            case CREATE_RECEIVER:
                return "Tạo người nhận";
            case UPDATE_RECEIVER:
                return "Cập nhật người nhận";
            case CREATE_STORE:
                return "Tạo cửa hàng";
            case UPDATE_STORE:
                return "Cập nhật cửa hàng";
            default:
                return "Unknown";
        }
    }
}
