package io.swagger.repository;

import io.swagger.model.ArrayOfTransfers;
import io.swagger.model.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ITransferRepository extends JpaRepository<Transfer, Integer> {
    ArrayOfTransfers findAll();
}
