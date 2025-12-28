package com.fv.sec001;

import com.fv.sec001.dto.CampaignDto;
import com.fv.sec001.process.ProcessTask;
import com.fv.sec001.service.ReadFile;
import com.fv.sec001.service.WriteFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.StopWatch;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@SpringBootApplication
@Slf4j
@RequiredArgsConstructor
public class FvSec001Application implements CommandLineRunner {
    private final ReadFile readFile;
    private final WriteFile writeFile;

    public static void main(String[] args) {
        SpringApplication.run(FvSec001Application.class, args);
    }

    @Override
    public void run(String... args) {
        Map<String, CampaignDto> data = readFile.process();
        writeFile.saveTop(data, 10);
    }
}
