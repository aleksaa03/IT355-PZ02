package com.it355.movie_management.services;

import com.it355.movie_management.common.enums.UserLogType;
import com.it355.movie_management.dtos.Pagination;
import com.it355.movie_management.dtos.user.UserDto;
import com.it355.movie_management.dtos.user_log.UserLogDto;
import com.it355.movie_management.exceptions.NotFoundException;
import com.it355.movie_management.models.User;
import com.it355.movie_management.models.UserLog;
import com.it355.movie_management.repositories.UserLogRepository;
import com.it355.movie_management.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class UserLogService {
    private final UserLogRepository userLogRepository;
    private final UserRepository userRepository;

    public UserLogService(UserLogRepository userLogRepository, UserRepository userRepository) {
        this.userLogRepository = userLogRepository;
        this.userRepository = userRepository;
    }

    public void addUserLog(Long userId, UserLogType action, String description, String details) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new NotFoundException("User don't exists in the system."));

            UserLog userLog = new UserLog();
            userLog.setAction(action);
            userLog.setDescription(description);
            userLog.setDetails(details);
            userLog.setUser(user);

            userLogRepository.save(userLog);
        }
        catch (Exception ex) {}
    }

    public Page<UserLogDto> getList(Pagination pagination) {
        Sort sort;

        if ("username".equals(pagination.getSortExp())) {
            sort = Sort.by(Sort.Direction.fromString(pagination.getSortOrd()), "user.username");
        } else if ("roleId".equals(pagination.getSortExp())) {
            sort = Sort.by(Sort.Direction.fromString(pagination.getSortOrd()), "user.roleId");
        } else {
            sort = Sort.by(Sort.Direction.fromString(pagination.getSortOrd()), pagination.getSortExp());
        }

        Pageable pageable = PageRequest.of(pagination.getPage() - 1, pagination.getPageSize(), sort);

        return userLogRepository.findAll(pageable)
                .map((item) -> new UserLogDto(item.getId(),
                                                    item.getAction(),
                                                    item.getDescription(),
                                                    item.getDetails(),
                                                    item.getEventTime(),
                                                      new UserDto(item.getUser().getId(),
                                                              item.getUser().getUsername(),
                                                              item.getUser().getRoleId())));
    }
}