package pl.tk.phishingscanner.infra.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.tk.phishingscanner.infra.persistence.entity.PhishingScannerUser;

@Repository
public interface PhishingScannerUserRepository extends JpaRepository<PhishingScannerUser, Long> {

  boolean existsByPhoneNumber(String phoneNumber);
}
