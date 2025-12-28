package com.fv.sec001.service;

import com.fv.sec001.dto.CampaignDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Slf4j
@Component
public class WriteFile {
    public void saveTop(Map<String, CampaignDto> data, int size) {
        Comparator<Map.Entry<String, CampaignDto>> byCpaAsc =
                Comparator.comparingDouble(e -> e.getValue().getCpa());

        Comparator<Map.Entry<String, CampaignDto>> byCpaDesc =
                byCpaAsc.reversed();

        Comparator<Map.Entry<String, CampaignDto>> byCtrAsc =
                Comparator.comparingDouble(e -> e.getValue().getCtr());

        PriorityQueue<Map.Entry<String, CampaignDto>> topHighCtr =
                new PriorityQueue<>(size, byCtrAsc);

        PriorityQueue<Map.Entry<String, CampaignDto>> topDownCpa =
                new PriorityQueue<>(size, byCpaDesc);


        for (Map.Entry<String, CampaignDto> entry : data.entrySet()) {
            offerTopCampaign(topDownCpa, entry, size, byCpaDesc);

            offerTopCampaign(topHighCtr, entry, size, byCtrAsc);
        }
        writeData("src/main/resources/data/top10_cpa.csv", topDownCpa);
        writeData("src/main/resources/data/top10_ctr.csv", topHighCtr);
    }

    private void offerTopCampaign(PriorityQueue<Map.Entry<String, CampaignDto>> pq,
                                         Map.Entry<String, CampaignDto> entry,
                                         int k,
                                         Comparator<Map.Entry<String, CampaignDto>> comparator
    ) {
        if (pq.size() < k) {
            pq.offer(entry);
        } else if (comparator.compare(entry, pq.peek()) > 0) {
            pq.poll();
            pq.offer(entry);
        }
    }

    private void writeData(String fileName, PriorityQueue<Map.Entry<String, CampaignDto>> data) {
        List<Map.Entry<String, CampaignDto>> topCampaigns = new ArrayList<>(data);
        try (BufferedWriter bw = Files.newBufferedWriter(Path.of(fileName))) {
            bw.write("campaign_id,total_impressions,total_clicks,total_spend,total_conversions,CTR,CPA");
            bw.newLine();

            for (Map.Entry<String, CampaignDto> e : topCampaigns) {
                CampaignDto campaign = e.getValue();

                bw.write(e.getKey());
                bw.write(",");
                bw.write(Long.toString(campaign.getImpressions()));
                bw.write(",");
                bw.write(Long.toString(campaign.getClicks()));
                bw.write(",");
                bw.write(Double.toString(campaign.getSpend()));
                bw.write(",");
                bw.write(Long.toString(campaign.getConversions()));
                bw.write(",");
                bw.write(Double.toString(campaign.getCtr()));
                bw.write(",");
                bw.write(Double.toString(campaign.getCpa()));
                bw.newLine();
            }
            log.info("Write success to: {}", fileName);
        } catch (IOException e) {
            log.error("Write file error: {}", e.getMessage());
        }
    }
}
