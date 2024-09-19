package com.pobluesky.notification.repository;


import com.pobluesky.notification.entity.CustomerNotification;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerNotificationRepository extends JpaRepository<CustomerNotification, Long> {

    List<CustomerNotification> findByUserIdAndIsReadFalseOrderByCreatedDateDesc(Long userId);

    @Query("SELECT cn FROM CustomerNotification cn WHERE cn.userId = :userId AND cn.isRead = :isRead ORDER BY cn.createdDate DESC")
    Page<CustomerNotification> findRecentNotificationsByuserIdAndIsRead(
        @Param("userId") Long userId,
        @Param("isRead") Boolean isRead,
        Pageable pageable
    );

    @Query("SELECT COUNT(cn) FROM CustomerNotification cn WHERE cn.userId = :userId AND cn.isRead = false")
    long countUnreadNotificationsByCustomer_UserId(@Param("userId") Long userId);

}
