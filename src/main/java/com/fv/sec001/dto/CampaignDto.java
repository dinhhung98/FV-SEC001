package com.fv.sec001.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.util.StringUtils;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CampaignDto {
    String campaignId;
    String date;
    long impressions;
    long clicks;
    double spend;
    long conversions;
    double ctr;
    double cpa;

    public void add(String campaignId, long impressions, long clicks, double spend, long conversions) {
        this.campaignId = campaignId;
        this.impressions += impressions;
        this.clicks += clicks;
        this.spend += spend;
        this.conversions += conversions;
    }

    public void merge(CampaignDto orther) {
        if (!StringUtils.hasLength(orther.campaignId)) {
            return;
        }
        if (this.campaignId.equals(orther.campaignId)) {
            this.conversions += orther.conversions;
            this.impressions += orther.impressions;
            this.clicks += orther.clicks;
            this.spend += orther.spend;
            this.ctr = impressions == 0 ? 0 : (double) clicks / impressions;
            this.cpa = conversions == 0 ? 0 : spend / conversions;
        }
    }
}
