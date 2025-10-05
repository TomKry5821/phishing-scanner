package pl.tk.phishingscanner.infra.persistence.service;

import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import pl.tk.phishingscanner.application.port.PhishingScannerUserService;
import pl.tk.phishingscanner.domain.model.PhoneNumber;
import pl.tk.phishingscanner.infra.mapping.PhishingScannerUserMapper;
import pl.tk.phishingscanner.infra.persistence.repository.PhishingScannerUserRepository;

@Slf4j
@Component
@Transactional
@RequiredArgsConstructor
public class PhishingScannerUserServiceImpl implements PhishingScannerUserService {

  private final PhishingScannerUserRepository repository;

  @Override
  public Optional<PhoneNumber> addUserByPhoneNumber(PhoneNumber phoneNumber) {
    try {
      return saveUser(phoneNumber);
    } catch (DataAccessException | IllegalArgumentException e) {
      log.warn(
          "There was problem while saving phone number: {} in database: {}, {}",
          phoneNumber,
          e.getClass(),
          e.getMessage());
      return Optional.empty();
    }
  }

  @Override
  public void removeUserByPhoneNumber(PhoneNumber phoneNumber) {
    try {
      repository.deleteByPhoneNumber(phoneNumber.getNumberAsString());
    } catch (DataAccessException | IllegalArgumentException e) {
      log.warn(
          "There was problem while deleting phone number: {} from database: {}, {}",
          phoneNumber,
          e.getClass(),
          e.getMessage());
    }
  }

  private Optional<PhoneNumber> saveUser(PhoneNumber phoneNumber) {
    if (repository.existsByPhoneNumber(phoneNumber.getNumberAsString())) {
      log.debug("User with number already exists: {}", phoneNumber);
      return Optional.of(phoneNumber);
    }
    repository.save(PhishingScannerUserMapper.map(phoneNumber));
    return Optional.of(phoneNumber);
  }
}
