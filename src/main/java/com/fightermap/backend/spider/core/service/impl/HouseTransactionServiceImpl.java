package com.fightermap.backend.spider.core.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fightermap.backend.spider.common.enums.SourceType;
import com.fightermap.backend.spider.common.util.JsonUtil;
import com.fightermap.backend.spider.core.model.bo.spider.HouseDetailInfo;
import com.fightermap.backend.spider.core.model.entity.HouseTransaction;
import com.fightermap.backend.spider.core.repository.HouseTransactionRepository;
import com.fightermap.backend.spider.core.service.HouseTransactionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author zengqk
 */
@Slf4j
@Service
public class HouseTransactionServiceImpl implements HouseTransactionService {

    private final ObjectMapper objectMapper;

    private final HouseTransactionRepository houseTransactionRepository;

    public HouseTransactionServiceImpl(ObjectMapper objectMapper,
                                       HouseTransactionRepository houseTransactionRepository) {
        this.objectMapper = objectMapper;
        this.houseTransactionRepository = houseTransactionRepository;
    }

    @Override
    public List<HouseTransaction> findBySourceTypeAndHouseBaseIdIn(SourceType sourceType, List<Long> houseBaseIds) {
        if (CollectionUtils.isEmpty(houseBaseIds)) {
            return Collections.emptyList();
        }
        return houseTransactionRepository.findAllBySourceTypeAndHouseBaseIdInAndDeletedFalse(sourceType, houseBaseIds);
    }

    @Override
    public List<HouseTransaction> saveAll(List<HouseTransaction> houseTransactionList) {
        if (CollectionUtils.isEmpty(houseTransactionList)) {
            return Collections.emptyList();
        }
        return houseTransactionRepository.saveAll(houseTransactionList);
    }

    @Override
    public List<HouseTransaction> merge(List<HouseTransaction> rawList, List<HouseTransaction> dbList) {
        Map<Long, HouseTransaction> dbInfoMap = dbList.stream().collect(Collectors.toMap(HouseTransaction::getHouseBaseId, Function.identity()));
        return rawList.stream().filter(raw -> {
            Long houseBaseId = raw.getHouseBaseId();
            HouseTransaction db = dbInfoMap.get(houseBaseId);
            //取字段变更的
            return !raw.equals(db);
        }).collect(Collectors.toList())
                .stream()
                .peek(raw -> {
                    Long houseBaseId = raw.getHouseBaseId();
                    HouseTransaction db = dbInfoMap.get(houseBaseId);
                    if (db != null) {
                        raw.setId(db.getId());
                        raw.setCreatedBy(db.getCreatedBy());
                        raw.setCreatedAt(db.getCreatedAt());
                        raw.setVersion(db.getVersion());
                    }
                }).collect(Collectors.toList());
    }

    @Override
    public HouseTransaction save(HouseDetailInfo houseDetailInfo, Long houseBaseId) {
        HouseTransaction result;
        SourceType sourceType = houseDetailInfo.getSourceType();
        HouseDetailInfo.TransactionInfo transactionInfo = houseDetailInfo.getTransactionInfo();
        HouseTransaction houseTransaction = HouseTransaction.builder()
                .sourceType(sourceType)
                .houseBaseId(houseBaseId)
                .listingDate(transactionInfo.getListingDate())
                .lastTransactionDate(transactionInfo.getLastTransactionDate())
                .transactionHoldType(transactionInfo.getTransactionHoldType())
                .houseUsageType(transactionInfo.getHouseUsageType())
                .houseLimit(transactionInfo.getHouseLimit())
                .houseHoldType(transactionInfo.getHouseHoldType())
                .mortgageInfo(transactionInfo.getMortgageInfo())
                .hasMortgage(transactionInfo.getHasMortgage())
                .houseCredential(transactionInfo.getHouseCredential())
                .unknownField(JsonUtil.writeToJson(transactionInfo.getUnknownField(), objectMapper))
                .build();

        HouseTransaction dbInfo = houseTransactionRepository.findAllBySourceTypeAndHouseBaseIdAndDeletedFalse(sourceType, houseBaseId).orElse(null);

        if (dbInfo == null) {
            result = houseTransactionRepository.save(houseTransaction);
        } else if (!dbInfo.equals(houseTransaction)) {
            houseTransaction.setId(dbInfo.getId());
            houseTransaction.setCreatedBy(dbInfo.getCreatedBy());
            houseTransaction.setCreatedAt(dbInfo.getCreatedAt());
            houseTransaction.setVersion(dbInfo.getVersion());
            result = houseTransactionRepository.save(houseTransaction);
        } else {
            result = dbInfo;
        }
        return result;
    }
}
