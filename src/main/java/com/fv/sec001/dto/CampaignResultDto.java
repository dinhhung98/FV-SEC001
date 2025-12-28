package com.fv.sec001.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CampaignResultDto {
    String campaignId;
    Long totalImpressions;
    Long totalClicks;
    Double totalSpend;
    Long totalConversions;
    Double ctr;
    Double cpa;


}
