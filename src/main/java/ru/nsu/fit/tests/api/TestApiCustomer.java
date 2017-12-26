package ru.nsu.fit.tests.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.Assert;
import org.testng.annotations.Test;
import ru.nsu.fit.data.Customer;
import ru.nsu.fit.data.ResponseMessage;
import ru.nsu.fit.services.log.Logger;

public class TestApiCustomer extends BuildVerificationTest {
    @Test(description = "Create customer via API.")
    public void testCreateCustomerWithWrongBalance() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Logger.info("================================================================\n");
        Logger.info("Создание пользователя с ненулевым балансом\n");
        Customer customerBeforeCreateMethod = this.customerBeforeCreateMethod.clone();
        customerBeforeCreateMethod.setBalance(100);
        String json = mapper.writeValueAsString(customerBeforeCreateMethod);

        String responseText = makeJsonRequest("create_customer", "admin", "setup", json);
        ResponseMessage respMessage = mapper.readValue(responseText, ResponseMessage.class);
        Assert.assertEquals("Money not equals 0", respMessage.getMessage());
        Logger.info("Пользователь не создан.Получен верный код ошибки.\n");
    }

    @Test(description = "Create customer via API.")
    public void testCreateCustomer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Logger.info("================================================================\n");
        Logger.info("Создание пользователя\n");
        String json = mapper.writeValueAsString(customerBeforeCreateMethod);

        String responseText = makeJsonRequest("create_customer", "admin", "setup", json);
        Customer customerAfterCreateMethod = mapper.readValue(responseText, Customer.class);
        customerAfterCreateMethod.setId(customerBeforeCreateMethod.getId());

        Logger.debug("customerBeforeCreateMethod:\n"+customerBeforeCreateMethod.toString());
        Logger.debug("customerAfterCreateMethod:\n"+customerAfterCreateMethod.toString());
        Assert.assertTrue(customerAfterCreateMethod.equals(customerBeforeCreateMethod));
    }

    @Test(description = "Create customer via API second time.", dependsOnMethods = "testCreateCustomer")
    public void testCreateCustomerAlreadyExist() throws Exception {
        Logger.info("================================================================\n");
        ObjectMapper mapper = new ObjectMapper();
        Logger.info("Создание уже имеющегося пользователя\n");
        String json = mapper.writeValueAsString(customerBeforeCreateMethod);

        String responseText = makeJsonRequest("create_customer", "admin", "setup", json);
        ResponseMessage respMessage = mapper.readValue(responseText, ResponseMessage.class);
        Assert.assertEquals("Customer with such login already exists.", respMessage.getMessage());
        Logger.info("Пользователь не создан.Получен верный код ошибки.\n");
    }

    @Test(description = "Remove created customer.", dependsOnMethods = "testCreateCustomerAlreadyExist")
    public void testRemoveCustomer() throws Exception {
        Logger.info("================================================================\n");
        Logger.info("Удаление пользователя\n");
        String responseText = makeJsonRequest("remove_customer/"+customerBeforeCreateMethod.getLogin(),
                "admin","setup", "");

        ObjectMapper mapper = new ObjectMapper();
        ResponseMessage respMessage = mapper.readValue(responseText, ResponseMessage.class);

        Assert.assertEquals("Customer "+customerBeforeCreateMethod.getLogin()+" was removed.",
                respMessage.getMessage());
        Logger.info("Пользователь " + customerBeforeCreateMethod.getLogin() + " успешно удален \n");
    }
}
