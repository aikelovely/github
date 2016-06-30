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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

@Service
public class StatisticService {

    private static final Logger logger = Logger.getLogger(StatisticService.class);

    private ExecutorService executorService;

    @PostConstruct
    private void init() {
        final ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNameFormat("UserStatistic-%d")
                .setDaemon(true)
                .build();
        executorService = Executors.newFixedThreadPool(5, threadFactory);
    }

    public void serveStatistic(final String page) {
        final UserPrincipal user = UserContext.getUser();
        final DateTime currentDate = DateTime.now();

        executorService.submit(new Runnable() {
            @Override
            public void run() {
                logger.info("THREAD " + Thread.currentThread().getName() +
                        ", USER " + user.getDisplayName() +
                        ", PAGE " + page +
                        ", DATE " + currentDate.toString());
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
            logger.info("shutdown statistic is now");
            executorService.shutdownNow();
        }
    }
}
