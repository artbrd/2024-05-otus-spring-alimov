package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.OtusRole;

public interface OtusRoleRepository extends JpaRepository<OtusRole, Long> {
}