package uk.co.cadetscricket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uk.co.cadetscricket.domain.Player;

import java.util.List;

/**
 * Repository interface for managing Player entities.
 * This interface extends JpaRepository, providing CRUD operations and additional query methods for Player entities.
 */
@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

    List<Player> findByFirstNameIgnoreCaseAndSurnameIgnoreCase(
            String firstName,
            String surname);

    @Query("""
            SELECT p FROM Player p
            WHERE LOWER(p.firstName) LIKE LOWER(CONCAT(:query, '%'))
               OR LOWER(p.surname) LIKE LOWER(CONCAT(:query, '%'))
                                        OR ((p.surname IS NULL OR TRIM(p.surname) = '')
                                                 AND LOWER(p.initial) LIKE LOWER(CONCAT(:query, '%')))
            ORDER BY p.surname, p.firstName
            """)
    List<Player> findSuggestions(@Param("query") String query, Pageable pageable);
}