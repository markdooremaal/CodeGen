package io.swagger.service;

import io.swagger.model.ArrayOfTransfers;
import io.swagger.model.Transfer;
import io.swagger.repository.ITransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.temporal.ChronoUnit;

@Service
public class TransferService {

    @Autowired
    private ITransferRepository transferRepository;

    public ArrayOfTransfers getAllTransfers() {
        return transferRepository.findAll();
    }

    public Transfer getTransferById(Integer id) {
        return transferRepository.findById(id).orElse(null);
    }

    public void storeTransfer(Transfer transfer) {
        OffsetDateTime dateTime = OffsetDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        transfer.setTimestamp(dateTime);
        transferRepository.save(transfer);
    }
}
