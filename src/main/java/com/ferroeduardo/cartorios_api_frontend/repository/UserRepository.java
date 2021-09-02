package com.ferroeduardo.cartorios_api_frontend.repository;

import com.ferroeduardo.cartorios_api_frontend.entity.User;
import com.ferroeduardo.cartorios_api_frontend.entity.UserDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends PagingAndSortingRepository<User, Long> {

    Optional<User> findFirstByUsername(String username);

    Optional<User> findFirstByEmail(String email);

    List<UserDTO> findAllByApiAccessibleIsFalse();

    List<UserDTO> findAllByApiAccessibleIsFalse(Pageable pageable);

    List<UserDTO> findAllByApiAccessibleIsFalseAndIdNot(Pageable pageable, long id);

    List<UserDTO> findAllByApiAccessibleIsTrue();

    List<UserDTO> findAllByApiAccessibleIsTrue(Pageable pageable);

    List<UserDTO> findAllByApiAccessibleIsTrueAndIdNot(Pageable pageable, long id);

}
