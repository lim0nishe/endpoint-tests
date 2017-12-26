package ru.nsu.fit.tests.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.Assert;
import org.testng.annotations.Test;
import ru.nsu.fit.data.Plan;
import ru.nsu.fit.data.ResponseMessage;
import ru.nsu.fit.services.log.Logger;

public class TestApiPlan extends BuildVerificationTest {

    @Test(description = "Create plan via API second time.")
    public void testCreatePlanWithWrongName() throws Exception {
        Logger.info("================================================================\n");
        Logger.info("Создание плана с коротким именем\n");
        ObjectMapper mapper = new ObjectMapper();
        Plan plan = planBeforeCreateMethod.clone();
        plan.setName("s");
        String json = mapper.writeValueAsString(plan);

        String responseText = makeJsonRequest("create_plan", "admin", "setup", json);
        ResponseMessage respMessage = mapper.readValue(responseText, ResponseMessage.class);
        Assert.assertEquals("Wrong length of name.", respMessage.getMessage());
        Logger.info("Получен верный код ошибки. План не создан\n");
    }

    @Test(description = "Create plan via API.")
    public void testCreatePlan() throws Exception {
        Logger.info("================================================================\n");
        ObjectMapper mapper = new ObjectMapper();
        Logger.info("Содание плана\n");
        String json = mapper.writeValueAsString(planBeforeCreateMethod);

        String responseText = makeJsonRequest("create_plan", "admin", "setup", json);
        Plan planAfterCreateMethod = mapper.readValue(responseText, Plan.class);
        planAfterCreateMethod.setId(planBeforeCreateMethod.getId());

        Logger.debug("planBeforeCreateMethod:\n"+planBeforeCreateMethod.toString());
        Logger.debug("planAfterCreateMethod:\n"+planAfterCreateMethod.toString());
        Assert.assertTrue(planAfterCreateMethod.equals(planBeforeCreateMethod));
        Logger.info("План успешно создан\n");
    }

    @Test(description = "Create plan via API second time.", dependsOnMethods = "testCreatePlan")
    public void testCreatePlanAlreadyExist() throws Exception {
        Logger.info("================================================================\n");
        Logger.info("Создание уже имеющегося плана\n");
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(planBeforeCreateMethod);

        String responseText = makeJsonRequest("create_plan", "admin", "setup", json);
        ResponseMessage respMessage = mapper.readValue(responseText, ResponseMessage.class);
        Assert.assertEquals("Plan with such name already exists.", respMessage.getMessage());
        Logger.info("Получен верный код ошибки. План не создан\n");
    }

    @Test(description = "Remove created plan.", dependsOnMethods = "testCreatePlanAlreadyExist")
    public void testRemovePlan() throws Exception {
        Logger.info("================================================================\n");
        Logger.info("Удаление плана\n");
        String responseText = makeJsonRequest("remove_plan/"+planBeforeCreateMethod.getName(), "admin",
                "setup", "");

        ObjectMapper mapper = new ObjectMapper();
        ResponseMessage respMessage = mapper.readValue(responseText, ResponseMessage.class);

        Assert.assertEquals("Plan "+planBeforeCreateMethod.getName()+" was removed.", respMessage.getMessage());
        Logger.info("План успешно удален\n");
    }
}
