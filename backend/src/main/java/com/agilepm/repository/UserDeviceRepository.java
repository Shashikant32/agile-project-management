package com.agilepm.repository;

import com.agilepm.model.User;
import com.agilepm.model.UserDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserDeviceRepository extends JpaRepository<UserDevice, Long> {
    Optional<UserDevice> findByUserAndDeviceId(User user, String deviceId);
    List<UserDevice> findByUser(User user);
    Optional<UserDevice> findByIpAddress(String ipAddress);
    List<UserDevice> findByUserAndStatus(User user, UserDevice.DeviceStatus status);
}
