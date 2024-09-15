package ru.antara.pagination.postprocessors;

import us.abstracta.jmeter.javadsl.core.postprocessors.DslJsr223PostProcessor;

import java.util.Objects;

public class PaginationCheck implements DslJsr223PostProcessor.PostProcessorScript{
    @Override
    public void runScript(DslJsr223PostProcessor.PostProcessorVars s) {
        String check1 = s.vars.get("first_ticket_db1");
        String check2 = s.vars.get("first_ticket_db2");

        if (Objects.equals(check1, check2) ||
                Objects.equals(check1,"Error first ticket data base one") ||
                Objects.equals(check2,"Error first ticket data base two"))
        {
            s.prev.setSuccessful(false);
            s.prev.setSampleLabel("/Pagination//ERROR_pagination_database");
            s.prev.setResponseMessage(
                    "Error pagination database");
        }
    }
}
