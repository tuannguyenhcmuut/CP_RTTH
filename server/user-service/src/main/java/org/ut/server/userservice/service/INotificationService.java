package org.ut.server.userservice.service;

import java.util.UUID;

public interface INotificationService {
    public void approveEmployeeRequest(UUID employeeId, Long requestId);
    public void rejectEmployeeRequest(UUID employeeId, Long requestId);
}
