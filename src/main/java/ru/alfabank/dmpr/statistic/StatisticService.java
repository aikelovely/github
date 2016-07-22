package ru.alfabank.dmpr.statistic;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.spring.security.UserContext;
import ru.alfabank.dmpr.infrastructure.spring.security.UserPrincipal;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

@Service
public class StatisticService {

    private static final Logger logger = Logger.getLogger(StatisticService.class);

    private ExecutorService executorService;

    private BatchBlockingQueue<Statistic> batchBlockingQueue;

    @PostConstruct
    private void init() {
        final ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNameFormat("UserStatistic-%d")
                .setDaemon(true)
                .build();
        executorService = Executors.newFixedThreadPool(5, threadFactory);
        batchBlockingQueue = new BatchBlockingQueue<>(5);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        List<Statistic> statistics = batchBlockingQueue.poll();
                        StringBuilder csv = new StringBuilder();
                        for (Statistic statistic : statistics) {
                            csv.append(statistic.getUser() + "," + statistic.getPage() + "," + statistic.getLocalDateTime() + "\n");
                        }
                        Files.write(Paths.get("/home/wert/stat.csv"), csv.toString().getBytes(), StandardOpenOption.APPEND,StandardOpenOption.CREATE);
                    } catch (InterruptedException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void serveStatistic(final String page) {
        final UserPrincipal user = UserContext.getUser();
        final DateTime currentDate = DateTime.now();

        executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    batchBlockingQueue.put(new Statistic(user.getDisplayName(), page, currentDate));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @PreDestroy
    private void destroy() throws InterruptedException {
        logger.info("shutdown statistic");
        executorService.shutdown();
        boolean success = executorService.awaitTermination(1, TimeUnit.MINUTES);
        logger.info("shutdown statistic is " + success);
        if (!success) {
            logger.info("shutdown statistic now");
            executorService.shutdownNow();
        }
    }
}
