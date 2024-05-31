package org.FelipeBert.ecommerce.controller.dto;

public record UserUpdatePasswordDTO(Long entityId, String currentPassword, String newPassword) {
}
