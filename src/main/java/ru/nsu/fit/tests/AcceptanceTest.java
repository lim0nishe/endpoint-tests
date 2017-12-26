package ru.nsu.fit.tests;

import org.junit.Before;
import org.openqa.selenium.By;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.nsu.fit.services.browser.Browser;
import ru.nsu.fit.services.browser.BrowserService;
import ru.yandex.qatools.allure.annotations.Description;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Title;
import ru.yandex.qatools.allure.model.SeverityLevel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @author Timur Zolotuhin (tzolotuhin@gmail.com)
 */
public class AcceptanceTest {
    private Browser browser = null;

    protected void login(){
        browser = BrowserService.openNewBrowser();
        // login to admin cp
        browser.openPage("http://localhost:8080/endpoint");
        browser.waitForElement(By.id("email"));

        browser.getElement(By.id("email")).sendKeys("admin");
        browser.getElement(By.id("password")).sendKeys("setup");

        createReport( "try to login with username: admin, password: setup" );
        browser.getElement(By.id("login")).click();
    }

    protected void createReport(String message) {
        try {
            Reporter.log("<br><b>" + message + "</b><br>");
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
            File screenshot = new File(timeStamp + ".png");
            screenshot.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(screenshot);
            outputStream.write(browser.makeScreenshot());
            String path = "<br><img src=\"" + screenshot.getName() + "\" alt=\"455264 437702\"><br>";
            Reporter.log(path);
            Thread.sleep(1500);
        } catch (IOException e){
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Test
    @Title("Create customer")
    @Description("Create customer via UI API")
    @Severity(SeverityLevel.BLOCKER)
    @Features("Customer feature")
    public void createCustomer() {
        login();
        browser.waitForElement(By.id("customers_id"));
        createReport( "Try to open customer page" );
        browser.getElement(By.id("customers_id")).click();
        browser.waitForElement(By.id("add_new_customer"));
        createReport( "Customer page successfully opened" );

        // create customer
        browser.getElement(By.id("add_new_customer")).click();
        browser.getElement(By.id("first_name_id")).sendKeys("John");
        browser.getElement(By.id("last_name_id")).sendKeys("Weak");
        browser.getElement(By.id("email_id")).sendKeys("email@example.com");
        browser.getElement(By.id("password_id")).sendKeys("strongpass");
        createReport( "Try to create customer" );

        browser.getElement(By.id("create_customer_id")).click();
        browser.waitForElement(By.id("customer_list_id"));
        createReport( "Customer successfully created" );

        browser.close();
    }

    @Test(dependsOnMethods = "createCustomer")
    @Title("Remove customer")
    @Description("Remove customer via UI API")
    @Severity(SeverityLevel.BLOCKER)
    @Features("Customer feature")
    public void removeCustomer() throws IOException {
        login();
        browser.waitForElement(By.id("customers_id"));
        createReport("Trying to open customers page");
        browser.getElement(By.id("customers_id")).click();

        browser.waitForElement(By.id("delete_email@example.com"));
        createReport("Customers page successfully opened");
        // create customer

        createReport("Trying to delete a new customer");
        browser.getElement(By.id("delete_email@example.com")).click();
        browser.waitForElement(By.id("customer_list_id"));
        createReport("Customer successfully deleted");

        browser.close();
    }


    @Test
    @Title("Create plan")
    @Description("Create plan via UI API")
    @Severity(SeverityLevel.BLOCKER)
    @Features("Plan feature")
    public void createPlan() throws IOException {
        login();
        browser.waitForElement(By.id("plans_id"));
        createReport("Trying to open plans page");
        browser.getElement(By.id("plans_id")).click();

        browser.waitForElement(By.id("add_new_plan"));
        createReport("Plans page successfully opened");
        // create customer
        browser.getElement(By.id("add_new_plan")).click();
        browser.getElement(By.id("plan_name_id")).sendKeys("PlanName");
        browser.getElement(By.id("details_id")).sendKeys("Plan details");
        browser.getElement(By.id("fee_id")).sendKeys("100");

        createReport("Trying to create a new plan");
        browser.getElement(By.id("create_plan_id")).click();
        browser.waitForElement(By.id("plan_list_id"));
        createReport("New plan successfully created");

        browser.close();
    }

    @Test(dependsOnMethods = "createPlan")
    @Title("Update plan")
    @Description("Update plan via UI API")
    @Severity(SeverityLevel.BLOCKER)
    @Features("Plan feature")
    public void updatePlan() throws IOException {
        login();
        browser.waitForElement(By.id("plans_id"));
        createReport("Trying to open plans page");
        browser.getElement(By.id("plans_id")).click();

        browser.waitForElement(By.id("update_PlanName"));
        createReport("Plans page successfully opened");
        // create customer
        browser.getElement(By.id("update_PlanName")).click();
        browser.getElement(By.id("plan_name_id")).sendKeys("PlanName");
        browser.getElement(By.id("details_id")).sendKeys("Updated details");
        browser.getElement(By.id("fee_id")).sendKeys("50");

        createReport("Trying to update a 'PlanName'");
        browser.getElement(By.id("update_plan_id")).click();
        browser.waitForElement(By.id("plan_list_id"));
        createReport("Plan successfully updated");

        browser.close();
    }

    @Test(dependsOnMethods = "updatePlan")
    @Title("Remove plan")
    @Description("Remove plan via UI API")
    @Severity(SeverityLevel.BLOCKER)
    @Features("Plan feature")
    public void removePlan() throws IOException {
        login();
        browser.waitForElement(By.id("plans_id"));
        createReport("Trying to open plans page");
        browser.getElement(By.id("plans_id")).click();

        browser.waitForElement(By.id("delete_PlanName"));
        createReport("Plans page successfully opened");
        // create customer

        createReport("Trying to delete a new plan");
        browser.getElement(By.id("delete_PlanName")).click();
        browser.waitForElement(By.id("plan_list_id"));
        createReport("Plan successfully deleted");

        browser.close();
    }
}
