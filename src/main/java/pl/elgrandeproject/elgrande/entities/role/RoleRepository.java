package pl.elgrandeproject.elgrande.entities.role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {

    @Query("SELECT r FROM Role r LEFT JOIN FETCH r.users WHERE r.name = :name")
    Optional<Role> findByName(String name);

    @Query("SELECT r FROM Role r LEFT JOIN FETCH  r.users")
    List<Role> findAllBy();

    @Query("SELECT r FROM Role r LEFT JOIN FETCH r.users WHERE r.id = :id")
    Optional<Role> findOneById(UUID id);



}
