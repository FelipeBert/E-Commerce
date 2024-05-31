package org.FelipeBert.ecommerce.service;

import org.FelipeBert.ecommerce.domain.model.User;

public interface UserService extends CrudService<Long, User>{
    User updatePassword(Long id, Long entityId,String currentPassword, String newPassword);
}
