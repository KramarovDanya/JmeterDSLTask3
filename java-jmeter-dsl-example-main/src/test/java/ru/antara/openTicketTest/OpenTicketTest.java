package ru.antara.openTicketTest;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import ru.antara.admin_login.fragments.AdminLoginFragment;
import ru.antara.admin_login.helpers.AdminLoginPropertyHelper;
import ru.antara.changeTicket.ChangeTicketTest;
import ru.antara.logOutUser.fragments.LogOutUserFragment;
import ru.antara.logOutUser.samplers.LogOutThreadGroup;
import ru.antara.openTicketTest.fragments.OpenTicketTestFragment;
import ru.antara.openTicketTest.samplers.OpenTicketThreadGroup;
import us.abstracta.jmeter.javadsl.core.TestPlanStats;
import us.abstracta.jmeter.javadsl.core.engines.EmbeddedJmeterEngine;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeoutException;

import static ru.antara.common.helpers.ActionHelper.testAction;
import static ru.antara.common.helpers.CacheHelper.getCacheManager;
import static ru.antara.common.helpers.CookiesHelper.getCookiesClean;
import static ru.antara.common.helpers.HeadersHelper.getHeaders;
import static ru.antara.common.helpers.HttpHelper.getHttpDefaults;
import static ru.antara.common.helpers.HttpHelper.initializePort;
import static ru.antara.common.helpers.LogHelper.getTestResultString;
import static ru.antara.common.helpers.LogHelper.influxDbLog;
import static ru.antara.common.helpers.VisualizersHelper.*;
import static us.abstracta.jmeter.javadsl.JmeterDsl.*;
import static us.abstracta.jmeter.javadsl.JmeterDsl.transaction;

public class OpenTicketTest {
    boolean debugEnable;
    boolean errorLogEnable;
    boolean influxDbLogEnable;
    boolean resultTreeEnable;
    boolean resultDashboardEnable;
    boolean debugPostProcessorEnable;
    double throughputPerMinute;

    static final Logger logger = LogManager.getLogger(OpenTicketTest.class);
    EmbeddedJmeterEngine embeddedJmeterEngine = new EmbeddedJmeterEngine();
    Properties properties = new Properties();

    @BeforeTest
    private void init() throws IOException {
        properties = AdminLoginPropertyHelper.readAdminLoginProperties();
        AdminLoginPropertyHelper.setPropertiesToEngine(embeddedJmeterEngine, properties);

        debugEnable = Boolean.parseBoolean(properties.getProperty("DEBUG_ENABLE"));
        errorLogEnable = Boolean.parseBoolean(properties.getProperty("ERROR_LOG_ENABLE"));
        influxDbLogEnable = Boolean.parseBoolean(properties.getProperty("INFLUX_DB_LOG_ENABLE"));
        resultTreeEnable = Boolean.parseBoolean(properties.getProperty("RESULT_TREE_ENABLE"));
        resultDashboardEnable = Boolean.parseBoolean(properties.getProperty("RESULT_DASHBOARD_ENABLE"));
        debugPostProcessorEnable = Boolean.parseBoolean(properties.getProperty("DEBUG_POSTPROCESSOR_ENABLE"));
        throughputPerMinute = Double.parseDouble(properties.getProperty("THROUGHPUT"));

        int port = Integer.parseInt(properties.getProperty("PORT"));
        initializePort(port);
    }

    @SuppressWarnings("unused")
    @Test
    private void test() throws IOException, InterruptedException, TimeoutException {

        OpenTicketTestFragment openTicketTestFragment = new OpenTicketTestFragment();
        AdminLoginFragment adminLoginFragment = new AdminLoginFragment();

        TestPlanStats run = testPlan(
                getCookiesClean(),
                getCacheManager(),
                getHeaders(),
                getHttpDefaults(),
                OpenTicketThreadGroup.getThreadGroup("TG_Open_Ticket", debugEnable)
                        .children(
                                ifController(s -> !debugEnable,
                                        testAction(throughputTimer(throughputPerMinute).perThread())
                                ),
                                transaction("US_ADMIN_LOGIN",
                                        adminLoginFragment.get()
                                ),
                                transaction("UC_Open_Ticket",
                                        openTicketTestFragment.get()
                                )
                        ),


                influxDbLog(influxDbLogEnable),
                resultTree(resultTreeEnable),
                resultDashboard(resultDashboardEnable),
                debugPostPro(debugPostProcessorEnable)

        ).runIn(embeddedJmeterEngine);

        logger.info(getTestResultString(run));
    }
}
