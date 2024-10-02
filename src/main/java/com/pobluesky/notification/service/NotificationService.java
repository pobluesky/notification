package com.pobluesky.notification.service;

import com.pobluesky.global.config.FirebaseConfig;
import com.pobluesky.global.error.CommonException;
import com.pobluesky.global.error.ErrorCode;
import com.pobluesky.notification.dto.request.CustomerNotificationCreateRequestDTO;
import com.pobluesky.notification.dto.request.CustomerNotificationUpdateRequestDTO;
import com.pobluesky.notification.dto.request.ManagerNotificationCreateRequestDTO;
import com.pobluesky.notification.dto.request.ManagerNotificationUpdateRequestDTO;
import com.pobluesky.feign.Customer;
import com.pobluesky.feign.Manager;
import com.pobluesky.notification.dto.response.CustomerNotificationResponseDTO;
import com.pobluesky.notification.dto.response.ManagerNotificationResponseDTO;
import com.pobluesky.notification.dto.response.MobileNotificationResponseDTO;
import com.pobluesky.notification.entity.CustomerNotification;
import com.pobluesky.notification.entity.ManagerNotification;
import com.pobluesky.feign.UserClient;
import com.pobluesky.notification.repository.CustomerNotificationRepository;
import com.pobluesky.notification.repository.ManagerNotificationRepository;

import jakarta.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final CustomerNotificationRepository customerNotificationRepository;

    private final ManagerNotificationRepository managerNotificationRepository;

    private final UserClient userClient;

    private final FirebaseCloudMessageService firebaseCloudMessageService;

    private final FirebaseConfig firebaseConfig;

    public List<?> getNotificationsById(
        String token,
        Long id,
        NotificationType notificationType
    ) {
        Long userId = userClient.parseToken(token);

        switch (notificationType) {
            case CUSTOMER:
                Customer customer = userClient.getCustomerByIdWithoutToken(userId).getData();

                if(customer==null){
                    throw new CommonException(ErrorCode.USER_NOT_FOUND);
                }

                if(!Objects.equals(customer.getUserId(), userId))
                    throw new CommonException(ErrorCode.USER_NOT_MATCHED);

                List<CustomerNotification> customerNotifications =
                    customerNotificationRepository.findByUserIdAndIsReadFalseOrderByCreatedDateDesc(id);

                Long totalCustomerElements = customerNotificationRepository.countUnreadNotificationsByCustomer_UserId(id);

                return List.of(
                    customerNotifications.stream()
                        .map(CustomerNotificationResponseDTO::from)
                        .collect(Collectors.toList()),
                    totalCustomerElements
                );

            case MANAGER:
                Manager manager = userClient.getManagerByIdWithoutToken(userId).getData();

                if(manager==null){
                    throw new CommonException(ErrorCode.USER_NOT_FOUND);
                }

                if(!Objects.equals(manager.getUserId(), userId))
                    throw new CommonException(ErrorCode.USER_NOT_MATCHED);

                List<ManagerNotification> managerNotifications =
                    managerNotificationRepository.findByUserIdAndIsReadFalseOrderByCreatedDateDesc(id);

                Long totalManagerElements = managerNotificationRepository.countUnreadNotificationsByManager_UserId(id);

                return List.of(
                    managerNotifications.stream()
                        .map(ManagerNotificationResponseDTO::from)
                        .collect(Collectors.toList()),
                    totalManagerElements
                );

            default:
                throw new CommonException(ErrorCode.USER_NOT_FOUND);
        }
    }

    public List<?> getRecentNotifications(
        String token,
        Long id,
        NotificationType notificationType
    ) {
        Long userId = userClient.parseToken(token);

        Pageable pageable = PageRequest.of(0, 6);

        switch (notificationType) {
            case CUSTOMER:
                Customer customer = userClient.getCustomerByIdWithoutToken(userId).getData();

                if(customer==null){
                    throw new CommonException(ErrorCode.USER_NOT_FOUND);
                }

                if(!Objects.equals(customer.getUserId(), userId))
                    throw new CommonException(ErrorCode.USER_NOT_MATCHED);

                var customerPage =
                    customerNotificationRepository.findRecentNotificationsByuserIdAndIsRead(
                        id,
                        true,
                        pageable
                    );

                return customerPage.getContent().stream()
                    .map(CustomerNotificationResponseDTO::from)
                    .collect(Collectors.toList());

            case MANAGER:
                Manager manager = userClient.getManagerByIdWithoutToken(userId).getData();

                if(manager==null){
                    throw new CommonException(ErrorCode.USER_NOT_FOUND);
                }

                if(!Objects.equals(manager.getUserId(), userId))
                    throw new CommonException(ErrorCode.USER_NOT_MATCHED);

                var managerPage =
                    managerNotificationRepository.findRecentNotificationsByuserIdAndIsRead(
                        id,
                        true,
                        pageable
                    );

                return managerPage.getContent().stream()
                    .map(ManagerNotificationResponseDTO::from)
                    .collect(Collectors.toList());

            default:
                throw new CommonException(ErrorCode.USER_NOT_FOUND);
        }
    }

    public Object createNotification(
        String token,
        Object dto,
        Long id,
        NotificationType notificationType
    ) throws IOException {
        Long userId = userClient.parseToken(token);

        if(!Objects.equals(userId, id))
            throw new CommonException(ErrorCode.USER_NOT_MATCHED);

        switch (notificationType) {
            case CUSTOMER:
                Customer customer = userClient.getCustomerByIdWithoutToken(id).getData();

                if(customer==null){
                    throw new CommonException(ErrorCode.USER_NOT_FOUND);
                }

                if(!Objects.equals(customer.getUserId(), id))
                    throw new CommonException(ErrorCode.USER_NOT_MATCHED);

                CustomerNotification customerNotification =
                    ((CustomerNotificationCreateRequestDTO) dto)
                        .toCustomerNotificationEntity(customer);

                CustomerNotification savedCustomerNotification =
                    customerNotificationRepository.save(customerNotification);

                return CustomerNotificationResponseDTO.from(savedCustomerNotification);

            case MANAGER:
                Manager manager = userClient.getManagerByIdWithoutToken(id).getData();

                if(manager==null){
                    throw new CommonException(ErrorCode.USER_NOT_FOUND);
                }

                if(!Objects.equals(manager.getUserId(), id))
                    throw new CommonException(ErrorCode.USER_NOT_MATCHED);

                ManagerNotification managerNotification =
                    ((ManagerNotificationCreateRequestDTO) dto)
                        .toManagerNotificationEntity(manager);

                ManagerNotification savedManagerNotification =
                    managerNotificationRepository.save(managerNotification);

                firebaseCloudMessageService.sendMessageTo(
                        firebaseConfig.getFcmServerKey(),
                        managerNotification.getNotificationContents()
                );

                return ManagerNotificationResponseDTO.from(savedManagerNotification);

            default:
                throw new CommonException(ErrorCode.USER_NOT_FOUND);
        }
    }

    public Object updateNotificationIsRead(
        String token,
        Long notificationId,
        Object dto,
        NotificationType notificationType
    ) {
        Long userId = userClient.parseToken(token);

        switch (notificationType)
        {
            case CUSTOMER:
                Customer customer = userClient.getCustomerByIdWithoutToken(userId).getData();

                if(customer==null){
                    throw new CommonException(ErrorCode.USER_NOT_FOUND);
                }

                if(!Objects.equals(customer.getUserId(), userId))
                    throw new CommonException(ErrorCode.USER_NOT_MATCHED);

                CustomerNotification customerNotification =
                    customerNotificationRepository.findById(notificationId)
                        .orElseThrow(() -> new CommonException(ErrorCode.NOTIFICATION_NOT_FOUND));

                CustomerNotificationUpdateRequestDTO updateCustomerDTO =
                    (CustomerNotificationUpdateRequestDTO) dto;

                customerNotification.updateIsRead(updateCustomerDTO.isRead());
                customerNotificationRepository.save(customerNotification);

                return CustomerNotificationResponseDTO.from(customerNotification);

            case MANAGER:
                Manager manager = userClient.getManagerByIdWithoutToken(userId).getData();

                if(manager==null){
                    throw new CommonException(ErrorCode.USER_NOT_FOUND);
                }

                if(!Objects.equals(manager.getUserId(), userId))
                    throw new CommonException(ErrorCode.USER_NOT_MATCHED);

                ManagerNotification managerNotification =
                    managerNotificationRepository.findById(notificationId)
                        .orElseThrow(() -> new CommonException(ErrorCode.NOTIFICATION_NOT_FOUND));

                ManagerNotificationUpdateRequestDTO updateManagerDTO =
                    (ManagerNotificationUpdateRequestDTO) dto;

                managerNotification.updateIsRead(updateManagerDTO.isRead());
                managerNotificationRepository.save(managerNotification);

                return ManagerNotificationResponseDTO.from(managerNotification);

            default:
                throw new CommonException(ErrorCode.USER_NOT_FOUND);
        }
    }

    public List<MobileNotificationResponseDTO> getNotificationsById(Long userId) {
        Manager manager = validateManager(userId);

        if (!Objects.equals(manager.getUserId(), userId))
            throw new CommonException(ErrorCode.USER_NOT_MATCHED);

        List<ManagerNotification> managerNotifications =
            managerNotificationRepository.findByUserIdAndIsReadFalseOrderByCreatedDateDesc(userId);

        return managerNotifications.stream()
            .map(MobileNotificationResponseDTO::from)
            .collect(Collectors.toList());
    }

    public List<MobileNotificationResponseDTO> getRecentNotifications(Long userId) {
        Manager manager = validateManager(userId);

        if (!Objects.equals(manager.getUserId(), userId))
            throw new CommonException(ErrorCode.USER_NOT_MATCHED);

        List<ManagerNotification> recentNotificationsByuserIdAndIsReadForMobile =
            managerNotificationRepository.findRecentNotificationsByuserIdAndIsReadForMobile(userId, true);

        return recentNotificationsByuserIdAndIsReadForMobile.stream()
            .map(MobileNotificationResponseDTO::from)
            .collect(Collectors.toList());
    }

    @Transactional
    public void updateNotificationIsRead(Long notificationId) {
        ManagerNotification managerNotification =
            managerNotificationRepository.findById(notificationId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOTIFICATION_NOT_FOUND));

        managerNotification.updateIsRead(true);
    }

    private Manager validateManager(Long userId) {

        Manager manager = userClient.getManagerByIdWithoutToken(userId).getData();
        if(manager == null){
            throw new CommonException(ErrorCode.USER_NOT_FOUND);
        }

        return manager;
    }


}