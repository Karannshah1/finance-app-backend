package com.karan.finance.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class TicketTransferRequest {
    private String newOwnerEmail;
    private List<UUID> ticketIds;
}
