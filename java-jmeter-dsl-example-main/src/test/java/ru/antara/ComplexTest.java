package ru.antara;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import ru.antara.admin_login.AdminLoginTest;
import ru.antara.admin_login.fragments.*;
import ru.antara.admin_login.helpers.AdminLoginPropertyHelper;
import ru.antara.admin_login.samplers.ComplexTestThreadGroup;
import ru.antara.changeTicket.fragments.ChangeTicketFragment;
import ru.antara.createNewTicket.fragments.CreateNewTicketFragment;
import ru.antara.createUser.fragments.CreateUserFragment;
import ru.antara.deleteTicket.fragments.DeleteTicketFragment;
import ru.antara.filtration.fragments.FiltrationFragment;
import ru.antara.logOutUser.fragments.LogOutUserFragment;
import ru.antara.openTicketTest.fragments.OpenTicketTestFragment;
import ru.antara.pagination.fragments.PaginationFragment;
import us.abstracta.jmeter.javadsl.core.TestPlanStats;
import us.abstracta.jmeter.javadsl.core.engines.EmbeddedJmeterEngine;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeoutException;

import static ru.antara.common.helpers.ActionHelper.jsr223Action;
import static ru.antara.common.helpers.ActionHelper.testAction;
import static ru.antara.common.helpers.CacheHelper.getCacheManager;
import static ru.antara.common.helpers.CookiesHelper.getCookiesClean;
import static ru.antara.common.helpers.HeadersHelper.getHeaders;
import static ru.antara.common.helpers.HttpHelper.getHttpDefaults;
import static ru.antara.common.helpers.LogHelper.getTestResultString;
import static ru.antara.common.helpers.LogHelper.influxDbLog;
import static ru.antara.common.helpers.VisualizersHelper.*;
import static ru.antara.common.helpers.HttpHelper.initializePort;
import static us.abstracta.jmeter.javadsl.JmeterDsl.*;

public class ComplexTest {
    boolean debugEnable;
    boolean errorLogEnable;
    boolean influxDbLogEnable;
    boolean resultTreeEnable;
    boolean resultDashboardEnable;
    boolean debugPostProcessorEnable;
    double throughputPerMinute;

    static final Logger logger = LogManager.getLogger(ComplexTest.class);
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
        AdminLoginFragment adminLoginFragment = new AdminLoginFragment();
        CreateUserFragment createUserFragment = new CreateUserFragment();
        ChangeTicketFragment changeTicketFragment = new ChangeTicketFragment();
        CreateNewTicketFragment createNewTicketFragment = new CreateNewTicketFragment();
        DeleteTicketFragment deleteTicketFragment = new DeleteTicketFragment();
        FiltrationFragment filtrationFragment = new FiltrationFragment();
        LogOutUserFragment logOutUserFragment = new LogOutUserFragment();
        OpenTicketTestFragment openTicketTestFragment = new OpenTicketTestFragment();
        PaginationFragment paginationFragment = new PaginationFragment();

        TestPlanStats run = testPlan(
                getCookiesClean(),
                getCacheManager(),
                getHeaders(),
                getHttpDefaults(),
                ComplexTestThreadGroup.getThreadGroup("TG_COMPLEX_TEST", debugEnable)
                        .children(
                                ifController(s -> !debugEnable,
                                        testAction(throughputTimer(throughputPerMinute).perThread())
                                ),
                                transaction("UC_ADMIN_LOGIN",
                                        adminLoginFragment.get()
                                ),
                                transaction("US_CREATE_USER",
                                        createUserFragment.get()
                                ),
                                transaction("US_Create_New_Ticket",
                                        createNewTicketFragment.get()
                                ),
                                transaction("US_Open_TIcket",
                                        openTicketTestFragment.get()
                                ),
                                transaction("US_Change_Ticket",
                                        changeTicketFragment.get()
                                ),
                                transaction("US_Logout_User",
                                        logOutUserFragment.get())
                        ),



                influxDbLog(influxDbLogEnable),
                resultTree(resultTreeEnable),
                resultDashboard(resultDashboardEnable),
                debugPostPro(debugPostProcessorEnable)

        ).runIn(embeddedJmeterEngine);

        logger.info(getTestResultString(run));

    }
}
