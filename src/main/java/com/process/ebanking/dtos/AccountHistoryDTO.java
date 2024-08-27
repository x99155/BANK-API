package com.process.ebanking.dtos;

import lombok.Data;

import java.util.List;

@Data
public class AccountHistoryDTO {
    private String acountId;
    private double balance;
    private int currentPage;
    private int pageSize;
    private int totalPages;
    private List<AccountOperationDTO> accountOperationDTOS;
}
