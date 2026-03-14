package br.com.starosky.entrepreneur.repository;

import br.com.starosky.entrepreneur.entity.Entrepreneur;
import br.com.starosky.entrepreneur.enums.OperatingSegment;
import br.com.starosky.entrepreneur.enums.Status;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface EntrepreneurRepository extends JpaRepository<Entrepreneur, Long> {

    Page<Entrepreneur> findByStatus(Status status, Pageable pageable);

    Page<Entrepreneur> findByOperatingSegment(OperatingSegment operatingSegment, Pageable pageable);

    Page<Entrepreneur> findByCityContainingIgnoreCase(String city, Pageable pageable);

    Page<Entrepreneur> findByEnterpriseNameContainingIgnoreCase(String enterpriseName, Pageable pageable);
    
    Optional<Entrepreneur> findByIdAndStatus(Long id, Status status);
}
