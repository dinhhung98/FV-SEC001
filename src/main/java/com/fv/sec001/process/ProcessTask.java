package com.fv.sec001.process;

import com.fv.sec001.dto.CampaignDto;
import com.fv.sec001.util.ParseUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

@Slf4j
public class ProcessTask implements Callable<Map<String, CampaignDto>> {
    private final List<String> lines;

    public ProcessTask(List<String> lines) {
        this.lines = lines;
    }

    @Override
    public Map<String, CampaignDto> call() {
        Map<String, CampaignDto> local = new HashMap<>();

        for (String line : lines) {
            String[] f = line.split(",", -1);

            String campaignId = f[0];
            long impressions = ParseUtil.parseLong(f[2]);
            long clicks = ParseUtil.parseLong(f[3]);
            double spend = ParseUtil.parseDouble(f[4]);
            long conversions = ParseUtil.parseLong(f[5]);

            local.computeIfAbsent(campaignId, k -> new CampaignDto())
                    .add(campaignId, impressions, clicks, spend, conversions);
        }
        return local;
    }
}
