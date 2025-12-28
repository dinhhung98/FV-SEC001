package com.fv.sec001.service;

import com.fv.sec001.dto.CampaignDto;
import com.fv.sec001.process.ProcessTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Slf4j
@Component
public class ReadFile {
    public Map<String, CampaignDto> process() {
        Map<String, CampaignDto> result = new HashMap<>();
        StopWatch stopWatch = new StopWatch("Aggregate_process");
        Resource resource = new ClassPathResource("data/ad_data.csv");

        int processSize = 20000;
        int threads = Runtime.getRuntime().availableProcessors();

        ExecutorService pool = Executors.newFixedThreadPool(threads);
        List<Future<Map<String, CampaignDto>>> futures = new ArrayList<>();

        stopWatch.start("read_file_and_sum");
        try (BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            String header = br.readLine();
            if (header == null) {
                throw new IllegalStateException("CSV file is empty");
            }

            List<String> batch = new ArrayList<>(processSize);
            String line;

            while ((line = br.readLine()) != null) {
                batch.add(line);

                if (batch.size() == processSize) {
                    futures.add(pool.submit(new ProcessTask(batch)));
                    batch = new ArrayList<>(processSize);
                }
            }

            if (!batch.isEmpty()) {
                futures.add(pool.submit(new ProcessTask(batch)));
            }

            for (Future<Map<String, CampaignDto>> f : futures) {
                Map<String, CampaignDto> local = f.get();

                for (Map.Entry<String, CampaignDto> e : local.entrySet()) {
                    result.merge(
                            e.getKey(),
                            e.getValue(),
                            (campaign, orther) -> {
                                campaign.merge(orther);
                                return campaign;
                            }
                    );
                }
            }
            stopWatch.stop();
            log.info("CSV processing finished:\n{}", stopWatch.prettyPrint());
            return result;
        } catch (Exception ex) {
            log.error("Read file error: {}", ex.getMessage());
        } finally {
            pool.shutdown();
            return result;
        }
    }
}
