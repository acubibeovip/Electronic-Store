package com.example.demo.service;

import com.example.demo.domain.Deal;
import com.example.demo.dto.DealDto;
import com.example.demo.repository.DealRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DealService {
    private final DealRepository dealRepository;

    public Deal createDeal(DealDto dealDto) {
        List<Long> productIds = dealDto.getProductIds();
        Deal deal = new Deal();
        deal.setDiscount(dealDto.getDiscount());
        deal.setExpiration(dealDto.getExpiration());
        deal.setProductIds(productIds);

        return dealRepository.save(deal);
    }
}
