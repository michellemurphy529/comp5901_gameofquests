package com.example.comp5901_web_based_implementation.front_end_tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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

import com.example.comp5901_web_based_implementation.back_end_game_of_quests.FoeCard;
import com.example.comp5901_web_based_implementation.back_end_game_of_quests.Game;
import com.example.comp5901_web_based_implementation.back_end_game_of_quests.QuestCard;
import com.example.comp5901_web_based_implementation.back_end_game_of_quests.WeaponCard;
import com.example.comp5901_web_based_implementation.front_end_game_of_quests.GameData;
import com.example.comp5901_web_based_implementation.front_end_game_of_quests.Application;

@DirtiesContext
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = Application.class)
public class AcceptanceTests {

    @Autowired
    Game game;

    @Autowired
    GameData gameData;

    @LocalServerPort
    int port;

    private ConfigurableApplicationContext ctx;
    WebDriver[] multiDriver;
    int numOfPlayers;

    @BeforeEach
    public void setUp() {
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
    @DirtiesContext
    @DisplayName("A1_Scenario")
    public void A1_Scenario() {
        //Navigate to webpage and register each player
        for (WebDriver driver : multiDriver) {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            driver.get("http://localhost:" + port);
            WebElement registerBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("registerBtn")));
            registerBtn.click();
            slowDown(1000);
        }

        //Rigging Adventure Deck for Proper Game Play
        rigAdventureDeckForGamePlayA1_Scenario();
        slowDown(1000);
        //Rigging Players Hands
        rigAdventureDeckForPlayersHandsA1_Scenario();
        slowDown(1000);
        //Rigging Event Deck for Proper Game Play
        rigEventDeckA1_Scenario();
        slowDown(1000);

        //Set all Players WebDriverWait
        WebDriverWait waitPlayer1 = new WebDriverWait(multiDriver[0], Duration.ofSeconds(10));
        WebDriverWait waitPlayer2 = new WebDriverWait(multiDriver[1], Duration.ofSeconds(10));
        WebDriverWait waitPlayer3 = new WebDriverWait(multiDriver[2], Duration.ofSeconds(10));
        WebDriverWait waitPlayer4 = new WebDriverWait(multiDriver[3], Duration.ofSeconds(10));

        //Player 1 clicks start button to start game
        WebElement startBtn = waitPlayer1.until(ExpectedConditions.visibilityOfElementLocated(By.id("startBtn")));
        startBtn.click();
        slowDown(5000);

        /*
        3) P1 draws a quest of 4 stages
         */
        //Player 1 clicks on 'Draw Event Card'
        WebElement drawEventCardPlayer1 = waitPlayer1.until(ExpectedConditions.visibilityOfElementLocated(By.id("draw")));
        drawEventCardPlayer1.click();
        slowDown(5000);

        /*
        4) P1 is asked but declines to sponsor
         */
        //Player 1 clicks 'No'
        WebElement declineSponsorPlayer1 = waitPlayer1.until(ExpectedConditions.visibilityOfElementLocated(By.id("noSponsor")));
        declineSponsorPlayer1.click();
        slowDown(5000);

        /* 5) P2 is asked and sponsors and then builds the 4 stages of the quest as:
        
        P2 QUEST:
        STAGE 1:
        CARDS - F5 H10
        VALUE = 15

        STAGE 2:
        CARDS - F15 S10
        VALUE = 25

        STAGE 3:
        CARDS - F15 D5 B15
        VALUE = 35

        STAGE 4:
        CARDS - F40 B15
        VALUE = 55 */
        
        WebElement acceptsSponsorPlayer2 = waitPlayer2.until(ExpectedConditions.visibilityOfElementLocated(By.id("yesSponsor")));
        acceptsSponsorPlayer2.click();
        slowDown(5000);

        /* STAGE 1:
        CARDS - F5 H10
        VALUE = 15 */
        WebElement cardElement = selectCard(waitPlayer2, "P2", "F5");
        cardElement.click();
        slowDown(5000);

        cardElement = selectCard(waitPlayer2, "P2", "H10");
        cardElement.click();
        slowDown(5000);

        WebElement quitStagePlayer2 = waitPlayer2.until(ExpectedConditions.visibilityOfElementLocated(By.id("quitStage")));
        quitStagePlayer2.click();
        slowDown(5000);

        /* STAGE 2:
        CARDS - F15 S10
        VALUE = 25 */
        cardElement = selectCard(waitPlayer2, "P2", "F15");
        cardElement.click();
        slowDown(5000);

        cardElement = selectCard(waitPlayer2, "P2", "S10");
        cardElement.click();
        slowDown(5000);

        quitStagePlayer2.click();
        slowDown(5000);

        /* STAGE 3:
        CARDS - F15 D5 B15
        VALUE = 35 */
        cardElement = selectCard(waitPlayer2, "P2", "F15");
        cardElement.click();
        slowDown(5000);

        cardElement = selectCard(waitPlayer2, "P2", "D5");
        cardElement.click();
        slowDown(5000);

        cardElement = selectCard(waitPlayer2, "P2", "B15");
        cardElement.click();
        slowDown(5000);

        quitStagePlayer2.click();
        slowDown(5000);

        /* STAGE 4:
        CARDS - F40 B15
        VALUE = 55 */
        cardElement = selectCard(waitPlayer2, "P2", "F40");
        cardElement.click();
        slowDown(5000);

        cardElement = selectCard(waitPlayer2, "P2", "B15");
        cardElement.click();
        slowDown(5000);

        quitStagePlayer2.click();
        slowDown(5000);

        //Player 2 (Sponsor) clicks Begins Quest
        WebElement beginQuestPlayer2 = waitPlayer2.until(ExpectedConditions.visibilityOfElementLocated(By.id("sponsorDone")));
        beginQuestPlayer2.click();
        slowDown(5000);

        /* 6) Stage 1:
            a. P1 is asked and decides to participate – draws a F30 – discards a F5 
            (to trim down to 12 cards)
        */
        WebElement participatesInStagePlayer1 = waitPlayer1.until(ExpectedConditions.visibilityOfElementLocated(By.id("yesStage")));
        participatesInStagePlayer1.click();
        slowDown(5000);

        cardElement = selectCard(waitPlayer1, "P1", "F5");
        cardElement.click();
        slowDown(5000);

        WebElement okLeaveQuestDiscardPlayer1 = waitPlayer1.until(ExpectedConditions.visibilityOfElementLocated(By.id("leaveQuestDiscard")));
        okLeaveQuestDiscardPlayer1.click();
        slowDown(5000);

        /* b. P3 is asked and decides to participate – draws a Sword - discards a F5
            (to trim down to 12 cards)
        */
        WebElement participatesInStagePlayer3 = waitPlayer3.until(ExpectedConditions.visibilityOfElementLocated(By.id("yesStage")));
        participatesInStagePlayer3.click();
        slowDown(5000);

        cardElement = selectCard(waitPlayer3, "P3", "F5");
        cardElement.click();
        slowDown(5000);

        WebElement okLeaveQuestDiscardPlayer3 = waitPlayer3.until(ExpectedConditions.visibilityOfElementLocated(By.id("leaveQuestDiscard")));
        okLeaveQuestDiscardPlayer3.click();
        slowDown(5000);

        /* c. P4 is asked and decides to participate – draws an Axe - discards a F5 
            (to trim down to 12 cards)  
        */
        WebElement participatesInStagePlayer4 = waitPlayer4.until(ExpectedConditions.visibilityOfElementLocated(By.id("yesStage")));
        participatesInStagePlayer4.click();
        slowDown(5000);

        cardElement = selectCard(waitPlayer4, "P4", "F5");
        cardElement.click();
        slowDown(5000);

        WebElement okLeaveQuestDiscardPlayer4 = waitPlayer4.until(ExpectedConditions.visibilityOfElementLocated(By.id("leaveQuestDiscard")));
        okLeaveQuestDiscardPlayer4.click();
        slowDown(5000);

        /* e. P1 sees their hand and builds an attack: Dagger + Sword => value of 15 */
        cardElement = selectCard(waitPlayer1, "P1", "D5");
        cardElement.click();
        slowDown(5000);

        cardElement = selectCard(waitPlayer1, "P1", "S10");
        cardElement.click();
        slowDown(5000);

        WebElement finishAttackPlayer1 = waitPlayer1.until(ExpectedConditions.visibilityOfElementLocated(By.id("quitAttack")));
        finishAttackPlayer1.click();
        slowDown(5000);

        /* f. P3 sees their hand and builds an attack: Sword + Dagger => value of 15 */
        cardElement = selectCard(waitPlayer3, "P3", "S10");
        cardElement.click();
        slowDown(5000);

        cardElement = selectCard(waitPlayer3, "P3", "D5");
        cardElement.click();
        slowDown(5000);

        WebElement finishAttackPlayer3 = waitPlayer3.until(ExpectedConditions.visibilityOfElementLocated(By.id("quitAttack")));
        finishAttackPlayer3.click();
        slowDown(5000);

        /* g. P4 sees their hand and builds an attack: Dagger + Horse => value of 15 */
        cardElement = selectCard(waitPlayer4, "P4", "D5");
        cardElement.click();
        slowDown(5000);

        cardElement = selectCard(waitPlayer4, "P4", "H10");
        cardElement.click();
        slowDown(5000);

        WebElement finishAttackPlayer4 = waitPlayer4.until(ExpectedConditions.visibilityOfElementLocated(By.id("quitAttack")));
        finishAttackPlayer4.click();
        slowDown(5000);
    }

    private void slowDown(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private void rigAdventureDeckForPlayersHandsA1_Scenario() {

        /* Posted Initial Hands:

        P1 - F5 F5 F15 F15 D5 S10 S10 H10 H10 B15 B15 L20
        P2 - F5 F5 F15 F15 F40 D5 S10 H10 H10 B15 B15 E30
        P3 - F5 F5 F5 F15 D5 S10 S10 S10 H10 H10 B15 L20
        P4 - F5 F15 F15 F40 D5 D5 S10 H10 H10 B15 L20 E30
        */

        //Rigging the Adventure Card Deck (backwards) to have the Players hand dealt in the right order

        //      P4   P3   P2   P1
        // 12 - E30, L20, E30, L20. 
        // 11 - L20, B15, B15, B15. 
        // 10 - B15, H10, B15, B15. 
        // 9 - H10, H10, H10, H10. 
        // 8 - H10, S10, H10, H10.
        // 7 - S10, S10, S10, S10.
        // 6 - D5, S10,  D5,  S10.
        // 5 - D5,  D5,  F40, D5.
        // 4 - F40, F15, F15, F15.
        // 3 - F15, F5,  F15, F15.
        // 2 - F15, F5,  F5,  F5.
        // 1 - F5,  F5,  F5,  F5.
        //Order E30, L20, E30, L20, L20, B15, B15, B15, B15, H10, B15, B15, H10, H10, H10, H10, H10, S10, H10, H10, S10, S10, S10, S10, D5, S10, D5, S10, D5, D5, F40, D5, F40, F15, F15, F15, F15, F5, F15, F15, F15, F5, F5, F5, F5, F5, F5, F5

        //Player hands P4 -> P3 -> P2 -> P1
        //12th Card
        // 12 - E30, L20, E30, L20. 
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("E", 30));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("L", 20));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("E", 30));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("L", 20));

        //11th Card
        // 11 - L20, B15, B15, B15. 
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("L", 20));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("B", 15));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("B", 15));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("B", 15));

        //10th Card
        // 10 - B15, H10, B15, B15. 
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("B", 15));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("H", 10));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("B", 15));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("B", 15));

        //9th Card
        // 9 - H10, H10, H10, H10. 
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("H", 10));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("H", 10));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("H", 10));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("H", 10));

        //8th Card
        // 8 - H10, S10, H10, H10.
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("H", 10));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("S", 10));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("H", 10));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("H", 10));

        //7th Card
        // 7 - S10, S10, S10, S10.
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("S", 10));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("S", 10));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("S", 10));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("S", 10));

        //6th Card
        // 6 - D5, S10, D5, S10.
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("D", 5));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("S", 10));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("D", 5));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("S", 10));

        //5th Card
        // 5 - D5, D5, F40, D5.
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("D", 5));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("D", 5));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(40));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("D", 5));

        //4th Card
        // 4 - F40, F15, F15, F15.
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(40));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(15));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(15));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(15));

        //3rd Card
        // 3 - F15, F5, F15, F15.
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(15));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(5));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(15));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(15));

        //2nd Card
        // 2 - F15, F5, F5, F5.
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(15));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(5));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(5));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(5));

        //1st Card
        // 1 - F5, F5, F5, F5.
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(5));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(5));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(5));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(5));
    }
    private void rigAdventureDeckForGamePlayA1_Scenario() {
        //Rig adventure deck to draw the right cards for game play
        //Cards in Adventure deck rigged for end Quest dsicard of the sponsor
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("H",10));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(40));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("S",10));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("B",15));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("L",20));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("S",10));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(5));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("B",15));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("S",10));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(10));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("L",20));

        //For game play
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("L",20));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(30));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("S",10));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("B",15));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("L",20));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("L",20));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(10));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("B",15));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("S",10));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(30));

    }
    private void rigEventDeckA1_Scenario() {
        QuestCard q4 = new QuestCard(4);
        gameData.getEventDeck().getDeck().add(0, q4);
    }
    private WebElement selectCard(WebDriverWait wait, String id, String card) {
        String playerHandCardID = id + card;
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(playerHandCardID)));
    }
}
