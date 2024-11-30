package com.example.comp5901_web_based_implementation.front_end_tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

import java.time.Duration;
import java.util.logging.Level;

import com.example.comp5901_web_based_implementation.back_end_game_of_quests.GameLogic;
import com.example.comp5901_web_based_implementation.front_end_game_of_quests.GameData;
import com.example.comp5901_web_based_implementation.front_end_game_of_quests.Application;

@DirtiesContext
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = Application.class)
public class AcceptanceTests {

    @Autowired
    GameData gd;

    @Autowired
    GameLogic game;

    @LocalServerPort
    int port;

    private ConfigurableApplicationContext ctx;
    WebDriver[] multiDriver;
    int numOfPlayers;

    @BeforeEach
    public void startGame() {
        ctx = SpringApplication.run(Application.class);

        System.setProperty("webdriver.chrome.driver", "../chromedriver-mac-x64/chromedriver");

        java.util.logging.Logger.getLogger("org.openqa.selenium").setLevel(Level.SEVERE);

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("disable-infobars");
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        numOfPlayers = 4;
        multiDriver = new WebDriver[numOfPlayers];

        int screenWidth = (int) (3072 / 1.5);
        int screenHeight = (int) ((1920 - 45) / 1.5);
        int offset = 25;

        for (int i = 0; i < numOfPlayers; i++) {
            multiDriver[i] = new ChromeDriver(options);
            multiDriver[i].manage().window().setSize(new org.openqa.selenium.Dimension(screenWidth / 2, screenHeight / 2));
        }

        multiDriver[0].manage().window().setPosition(new org.openqa.selenium.Point(0, 0));
        multiDriver[1].manage().window().setPosition(new org.openqa.selenium.Point(screenWidth / 2, 0));
        multiDriver[2].manage().window().setPosition(new org.openqa.selenium.Point(0, screenHeight / 2 + offset));
        multiDriver[3].manage().window().setPosition(new org.openqa.selenium.Point(screenWidth / 2, screenHeight / 2 + offset));

        slowDown(1000);
    }

    @AfterEach
    public void tearDown() {
        for (WebDriver driver : multiDriver) {
            if (driver != null) {
                driver.quit();
            }
        }
        if (ctx != null) {
            ctx.close();
        }
    }

    @Test
    public void testGameFlow() {
        //Navigate to webpage and register each player
        for (WebDriver driver : multiDriver) {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            driver.get("http://localhost:" + port);
            WebElement registerBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("registerBtn")));
            registerBtn.click();
            slowDown(1000);
        }

        //Player 1 clicks start button to start game
        WebDriverWait wait = new WebDriverWait(multiDriver[0], Duration.ofSeconds(10));
        WebElement startBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("startBtn")));
        startBtn.click();
        slowDown(5000);
    }

    private void slowDown(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
