package com.ferroeduardo.cartorios_api_frontend.repository;

import com.ferroeduardo.cartorios_api_frontend.entity.User;
import com.ferroeduardo.cartorios_api_frontend.entity.UserSafeData;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends PagingAndSortingRepository<User, Long> {

    Optional<User> findFirstByUsername(String username);

    Optional<User> findFirstByEmail(String email);

    List<UserSafeData> findAllByApiAccessibleIsFalse();

    List<UserSafeData> findAllByApiAccessibleIsFalse(Pageable pageable);

    List<UserSafeData> findAllByApiAccessibleIsTrue();

    List<UserSafeData> findAllByApiAccessibleIsTrue(Pageable pageable);

}
