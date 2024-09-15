package ru.antara.deleteTicket.postprocessors;

import us.abstracta.jmeter.javadsl.core.postprocessors.DslJsr223PostProcessor;

import java.util.Objects;

public class DeleteTicketCheck implements DslJsr223PostProcessor.PostProcessorScript{
    @Override
    public void runScript(DslJsr223PostProcessor.PostProcessorVars s) {
        String checkName = s.vars.get("Ticket_name");
        String checkNameDataBase = s.vars.get("Check_name_in_database");
        System.out.println(checkName +" "+checkNameDataBase);


        if (!Objects.equals(checkNameDataBase , "Name_not_found_in_database") || Objects.equals(checkName,checkNameDataBase)) {
            s.prev.setSuccessful(false);
            s.prev.setSampleLabel("/tickets/${Random_ticket}/delete/ERROR");
            s.prev.setResponseMessage(
                    "Error,delete ticket was some problem");
        }
    }
}
