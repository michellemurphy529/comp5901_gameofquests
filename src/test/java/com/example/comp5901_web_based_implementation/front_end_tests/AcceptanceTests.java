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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

import com.example.comp5901_web_based_implementation.back_end_game_of_quests.FoeCard;
import com.example.comp5901_web_based_implementation.back_end_game_of_quests.Game;
import com.example.comp5901_web_based_implementation.back_end_game_of_quests.PlagueCard;
import com.example.comp5901_web_based_implementation.back_end_game_of_quests.ProsperityCard;
import com.example.comp5901_web_based_implementation.back_end_game_of_quests.QueensFavorCard;
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
        int offset = 28;

        for (int i = 0; i < numOfPlayers; i++) {
            multiDriver[i] = new ChromeDriver(options);
            if (i == 0 || i == 1) {
                multiDriver[i].manage().window().setSize(new org.openqa.selenium.Dimension(screenWidth / 2, (screenHeight / 2) + 4));
            } else {
                multiDriver[i].manage().window().setSize(new org.openqa.selenium.Dimension(screenWidth / 2, screenHeight / 2));
            }
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
        slowDown(1000);

        /*
        3) P1 draws a quest of 4 stages
         */
        //Player 1 clicks on 'Draw Event Card'
        WebElement drawEventCardPlayer1 = waitPlayer1.until(ExpectedConditions.visibilityOfElementLocated(By.id("draw")));
        drawEventCardPlayer1.click();
        slowDown(1000);

        /*
        4) P1 is asked but declines to sponsor
         */
        //Player 1 clicks 'No'
        WebElement declineSponsorPlayer1 = waitPlayer1.until(ExpectedConditions.visibilityOfElementLocated(By.id("noSponsor")));
        declineSponsorPlayer1.click();
        slowDown(1000);

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
        slowDown(1000);

        /* STAGE 1:
        CARDS - F5 H10
        VALUE = 15 */
        WebElement cardElement = selectCard(waitPlayer2, "P2", "F5");
        cardElement.click();
        slowDown(1000);

        cardElement = selectCard(waitPlayer2, "P2", "H10");
        cardElement.click();
        slowDown(1000);

        WebElement quitStagePlayer2 = waitPlayer2.until(ExpectedConditions.visibilityOfElementLocated(By.id("quitStage")));
        quitStagePlayer2.click();
        slowDown(1000);

        /* STAGE 2:
        CARDS - F15 S10
        VALUE = 25 */
        cardElement = selectCard(waitPlayer2, "P2", "F15");
        cardElement.click();
        slowDown(1000);

        cardElement = selectCard(waitPlayer2, "P2", "S10");
        cardElement.click();
        slowDown(1000);

        quitStagePlayer2.click();
        slowDown(1000);

        /* STAGE 3:
        CARDS - F15 D5 B15
        VALUE = 35 */
        cardElement = selectCard(waitPlayer2, "P2", "F15");
        cardElement.click();
        slowDown(1000);

        cardElement = selectCard(waitPlayer2, "P2", "D5");
        cardElement.click();
        slowDown(1000);

        cardElement = selectCard(waitPlayer2, "P2", "B15");
        cardElement.click();
        slowDown(1000);

        quitStagePlayer2.click();
        slowDown(1000);

        /* STAGE 4:
        CARDS - F40 B15
        VALUE = 55 */
        cardElement = selectCard(waitPlayer2, "P2", "F40");
        cardElement.click();
        slowDown(1000);

        cardElement = selectCard(waitPlayer2, "P2", "B15");
        cardElement.click();
        slowDown(1000);

        quitStagePlayer2.click();
        slowDown(1000);

        //Player 2 (Sponsor) clicks Begins Quest
        WebElement beginQuestPlayer2 = waitPlayer2.until(ExpectedConditions.visibilityOfElementLocated(By.id("sponsorDone")));
        beginQuestPlayer2.click();
        slowDown(1000);

        /* 6) Stage 1:
            a. P1 is asked and decides to participate – draws a F30 – discards a F5 
            (to trim down to 12 cards)
        */
        WebElement participatesInStagePlayer1 = waitPlayer1.until(ExpectedConditions.visibilityOfElementLocated(By.id("yesStage")));
        participatesInStagePlayer1.click();
        slowDown(1000);

        cardElement = selectCard(waitPlayer1, "P1", "F5");
        cardElement.click();
        slowDown(1000);

        WebElement okLeaveQuestDiscardPlayer1 = waitPlayer1.until(ExpectedConditions.visibilityOfElementLocated(By.id("leaveQuestDiscard")));
        okLeaveQuestDiscardPlayer1.click();
        slowDown(1000);

        /* b. P3 is asked and decides to participate – draws a Sword - discards a F5
            (to trim down to 12 cards)
        */
        WebElement participatesInStagePlayer3 = waitPlayer3.until(ExpectedConditions.visibilityOfElementLocated(By.id("yesStage")));
        participatesInStagePlayer3.click();
        slowDown(1000);

        cardElement = selectCard(waitPlayer3, "P3", "F5");
        cardElement.click();
        slowDown(1000);

        WebElement okLeaveQuestDiscardPlayer3 = waitPlayer3.until(ExpectedConditions.visibilityOfElementLocated(By.id("leaveQuestDiscard")));
        okLeaveQuestDiscardPlayer3.click();
        slowDown(1000);

        /* c. P4 is asked and decides to participate – draws an Axe - discards a F5 
            (to trim down to 12 cards)  
        */
        WebElement participatesInStagePlayer4 = waitPlayer4.until(ExpectedConditions.visibilityOfElementLocated(By.id("yesStage")));
        participatesInStagePlayer4.click();
        slowDown(1000);

        cardElement = selectCard(waitPlayer4, "P4", "F5");
        cardElement.click();
        slowDown(1000);

        WebElement okLeaveQuestDiscardPlayer4 = waitPlayer4.until(ExpectedConditions.visibilityOfElementLocated(By.id("leaveQuestDiscard")));
        okLeaveQuestDiscardPlayer4.click();
        slowDown(1000);

        /* e. P1 sees their hand and builds an attack: Dagger + Sword => value of 15 */
        cardElement = selectCard(waitPlayer1, "P1", "D5");
        cardElement.click();
        slowDown(1000);

        cardElement = selectCard(waitPlayer1, "P1", "S10");
        cardElement.click();
        slowDown(1000);

        WebElement finishAttackPlayer1 = waitPlayer1.until(ExpectedConditions.visibilityOfElementLocated(By.id("quitAttack")));
        finishAttackPlayer1.click();
        slowDown(1000);

        /* f. P3 sees their hand and builds an attack: Sword + Dagger => value of 15 */
        cardElement = selectCard(waitPlayer3, "P3", "S10");
        cardElement.click();
        slowDown(1000);

        cardElement = selectCard(waitPlayer3, "P3", "D5");
        cardElement.click();
        slowDown(1000);

        WebElement finishAttackPlayer3 = waitPlayer3.until(ExpectedConditions.visibilityOfElementLocated(By.id("quitAttack")));
        finishAttackPlayer3.click();
        slowDown(1000);

        /* g. P4 sees their hand and builds an attack: Dagger + Horse => value of 15 */
        cardElement = selectCard(waitPlayer4, "P4", "D5");
        cardElement.click();
        slowDown(1000);

        cardElement = selectCard(waitPlayer4, "P4", "H10");
        cardElement.click();
        slowDown(1000);

        WebElement finishAttackPlayer4 = waitPlayer4.until(ExpectedConditions.visibilityOfElementLocated(By.id("quitAttack")));
        finishAttackPlayer4.click();
        slowDown(1000);


        /* h. Resolution: all 3 attacks are sufficient thus all participants can go onto the next stage
        i. All 3 participants discard the cards used for their attacks.
        */

        /* 7) Stage 2:
        a. P1 is asked and decides to participate. 
        P1 draws a F10
        */
        participatesInStagePlayer1.click();
        slowDown(1000);

        okLeaveQuestDiscardPlayer1.click();
        slowDown(1000);

        /* b. P3 is asked and decides to participate. 
        P3 draws a Lance
        */
        participatesInStagePlayer3.click();
        slowDown(1000);

        okLeaveQuestDiscardPlayer3.click();
        slowDown(1000);

        /* c. P4 is asked and decides to participate. 
        P4 draws a Lance 
        */
        participatesInStagePlayer4.click();
        slowDown(1000);

        okLeaveQuestDiscardPlayer4.click();
        slowDown(1000);

        /* d. P1 sees their hand and builds an attack: 
        Horse + Sword => value of 20
        */
        cardElement = selectCard(waitPlayer1, "P1", "H10");
        cardElement.click();
        slowDown(1000);

        cardElement = selectCard(waitPlayer1, "P1", "S10");
        cardElement.click();
        slowDown(1000);

        finishAttackPlayer1.click();
        slowDown(1000);

        /* e. P3 sees their hand and builds an attack: 
        Axe + Sword => value of 25 
        */
        cardElement = selectCard(waitPlayer3, "P3", "B15");
        cardElement.click();
        slowDown(1000);

        cardElement = selectCard(waitPlayer3, "P3", "S10");
        cardElement.click();
        slowDown(1000);

        finishAttackPlayer3.click();
        slowDown(1000);

        /* f. P4 sees their hand and builds an attack: 
        Horse + Axe => value of 25 
        */
        cardElement = selectCard(waitPlayer4, "P4", "H10");
        cardElement.click();
        slowDown(1000);

        cardElement = selectCard(waitPlayer4, "P4", "B15");
        cardElement.click();
        slowDown(1000);

        finishAttackPlayer4.click();
        slowDown(1000);

        /* i. P1’s attack is insufficient – P1 loses and cannot go to the next stage.
        ii. P3’s and P4’s attack are sufficient go onto the next stage
        h. All 3 participants discard the cards used for their attacks */

        //Player 1 Shield Assert (should = 0)
        assertEquals(0, getShieldsCount("P1"));

        //Player 1 Hand Assert
        String actualHandPlayer1 = getStringPlayerHand("P1"); 
        slowDown(1000);
        String expectedHandPlayer1 = "F5 F10 F15 F15 F30 H10 B15 B15 L20";
        assertEquals(expectedHandPlayer1, actualHandPlayer1);
        slowDown(1000);

        /* 8) Stage 3:
        a. P3 is asked and decides to participate. 
        P3 draws an Axe.
        */
        participatesInStagePlayer3.click();
        slowDown(1000);

        okLeaveQuestDiscardPlayer3.click();
        slowDown(1000);

        /* b. P4 is asked and decides to participate. 
        P4 draws a Sword. 
        */
        participatesInStagePlayer4.click();
        slowDown(1000);

        okLeaveQuestDiscardPlayer4.click();
        slowDown(1000);

        /* c. P3 sees their hand and builds an attack: 
        Lance + Horse + Sword => value of 40.
        */
        cardElement = selectCard(waitPlayer3, "P3", "L20");
        cardElement.click();
        slowDown(1000);

        cardElement = selectCard(waitPlayer3, "P3", "H10");
        cardElement.click();
        slowDown(1000);

        cardElement = selectCard(waitPlayer3, "P3", "S10");
        cardElement.click();
        slowDown(1000);

        finishAttackPlayer3.click();
        slowDown(1000);

        /* d. P4 sees their hand and builds an attack: 
        Axe + Sword + Lance => value of 45
        */
        cardElement = selectCard(waitPlayer4, "P4", "B15");
        cardElement.click();
        slowDown(1000);

        cardElement = selectCard(waitPlayer4, "P4", "S10");
        cardElement.click();
        slowDown(1000);

        cardElement = selectCard(waitPlayer4, "P4", "L20");
        cardElement.click();
        slowDown(1000);

        finishAttackPlayer4.click();
        slowDown(1000);

        /* e. Resolution: P3’s and P4’s attack are sufficient go onto the next stage.
        f. All 2 participants discard the cards used for their attacks. 
        */

        /* 8) Stage 4:
        a. P3 is asked and decides to participate. 
        P3 draws an F30.
        */
        participatesInStagePlayer3.click();
        slowDown(1000);

        okLeaveQuestDiscardPlayer3.click();
        slowDown(1000);

        /* b. P4 is asked and decides to participate. 
        P4 draws a Lance. 
        */
        participatesInStagePlayer4.click();
        slowDown(1000);

        okLeaveQuestDiscardPlayer4.click();
        slowDown(1000);

        /* c. P3 sees their hand and builds an attack: 
        Axe + Horse + Lance => value of 45.
        */
        cardElement = selectCard(waitPlayer3, "P3", "B15");
        cardElement.click();
        slowDown(1000);

        cardElement = selectCard(waitPlayer3, "P3", "H10");
        cardElement.click();
        slowDown(1000);

        cardElement = selectCard(waitPlayer3, "P3", "L20");
        cardElement.click();
        slowDown(1000);

        finishAttackPlayer3.click();
        slowDown(1000);

        /* d. P4 sees their hand and builds an attack: 
        Dagger + Sword + Lance + Excalibur => value of 65.
        */
        cardElement = selectCard(waitPlayer4, "P4", "D5");
        cardElement.click();
        slowDown(1000);

        cardElement = selectCard(waitPlayer4, "P4", "S10");
        cardElement.click();
        slowDown(1000);

        cardElement = selectCard(waitPlayer4, "P4", "L20");
        cardElement.click();
        slowDown(1000);

        cardElement = selectCard(waitPlayer4, "P4", "E30");
        cardElement.click();
        slowDown(1000);

        finishAttackPlayer4.click();
        slowDown(1000);

        /* e. Resolution:
            i. P3’s attack is insufficient – P3 loses and receives no shields.
            ii. P4’s attack is sufficient – P4 receives 4 shields.
        
            f. All 2 participants discard the cards used for their attacks
            i. ASSERT P3 has no shields and has the 5 cards: F5 F5 F15 F30 Sword (see below)
            ii. ASSERT P4 has 4 shields and has the cards: F15 F15 F40 Lance (see below)
        */

        //Player 3 Shield Assert (should = 0)
        assertEquals(0, getShieldsCount("P3"));

        //Player 3 Hand Assert
        String actualHandPlayer3 = getStringPlayerHand("P3"); 
        slowDown(1000);
        String expectedHandPlayer3 = "F5 F5 F15 F30 S10";
        assertEquals(expectedHandPlayer3, actualHandPlayer3);
        slowDown(1000);

        //Player 4 Shield Assert (should = 0)
        assertEquals(4, getShieldsCount("P4"));

        //Player 4 Hand Assert
        String actualHandPlayer4 = getStringPlayerHand("P4"); 
        slowDown(1000);
        String expectedHandPlayer4 = "F15 F15 F40 L20";
        assertEquals(expectedHandPlayer4, actualHandPlayer4);
        slowDown(1000);
        
        /*
            10) P2 discards all 9 cards used in the quest and draws 9 + 4 = 13 random cards and then
            trims down to 12 cards. It does not matter which cards are selected to discard.
            • ASSERT P2 has 12 cards in hand (see below)
        */
        for (int i = 0; i < 4; i++) {
            String cardID = getFirstCardID("P2");
            cardElement = selectCard(waitPlayer2, "P2", cardID);
            cardElement.click();
            slowDown(3000);
        }

        //Player 2 Hand Count should = 12
        int actualPlayer2CardCount = getPlayerHandCardCount("P2");
        assertEquals(12, actualPlayer2CardCount);


        /* Correction Grid Reqs
        
        A1_Scenario:
        Row 21: The number of shields and the number of cards of each participant at the end 
                of the scenario is correct.

                P1: shields = 0, card count = 9
                P2: shields = 0, card count = 12
                P3: shields = 0, card count = 5
                P4: shields = 4, card count = 4
        */

        //Confirming asserts requried in the correction grid are made
        //Player 1
        assertEquals(0, getShieldsCount("P1"));
        assertEquals(9, getPlayerHandCardCount("P1"));
        //Player 2
        assertEquals(0, getShieldsCount("P2"));
        assertEquals(12, getPlayerHandCardCount("P2"));
        //Player 3
        assertEquals(0, getShieldsCount("P3"));
        assertEquals(5, getPlayerHandCardCount("P3"));
        //Player 4
        assertEquals(4, getShieldsCount("P4"));
        assertEquals(4, getPlayerHandCardCount("P4"));

        /* 
        P1 Lost in Stage 1
        P3 Lost in Stage 4
        P4 earns 4 shields and Won Quest
        */

        String expectedEndStage1LoserMessage = "Sorry, you LOST Stage 2!\nYou have been eliminated from participating in the Quest.";
        String expectedEndStage4LoserMessage = "Sorry, you LOST Stage 4!\nYou have been eliminated from participating in the Quest.";
        String expectedEndStageWinnerMessage = "Congratulations you WON the last stage of the Quest!\nYou have earned 4 shields!";
        //Player 1 screen displays they Lost in Stage 1
        assertEquals(expectedEndStage1LoserMessage, getEndQuestStageLoserMessage("P1"));
        //Player 3 screen displays they Lost in Stage 4
        assertEquals(expectedEndStage4LoserMessage, getEndQuestStageLoserMessage("P3"));
        //Player 4 screen displays they Lost
        assertEquals(expectedEndStageWinnerMessage, getEndQuestStageWinnerMessage("P4"));

        /*
        Row 22: The number of shields and the number of cards of each participant is correctly 
                updated throughout the scenario. YES

        Row 23: The final hand of each player is displayed and is correct at the end of the scenario.
                
                P1: F5 F10 F15 F15 F30 H10 B15 B15 L20
                P2: N/A
                P3: F5 F5 F15 F30 S10
                P4: F15 F15 F40 L20
        */

        //Confirming asserts requried in the correction grid are made
        //Player 1
        assertEquals("F5 F10 F15 F15 F30 H10 B15 B15 L20", getStringPlayerHand("P1"));
        //Player 3
        assertEquals("F5 F5 F15 F30 S10", getStringPlayerHand("P3"));
        //Player 4
        assertEquals("F15 F15 F40 L20", getStringPlayerHand("P4"));

        /*
        Also, ASSERT the winner(s) or absence of:
                None are Game Winner(s) (have >= 7 sheilds)
                The Message appears on all screens "There are no player's with 7 or more Shields<br>A Game of Quests Continues!"
        */

        WebElement okLeaveQuestDiscardPlayer2 = waitPlayer2.until(ExpectedConditions.visibilityOfElementLocated(By.id("leaveQuestDiscard")));
        okLeaveQuestDiscardPlayer2.click();
        slowDown(1000);

        String expectedMessage = "There are no player's with 7 or more Shields\nA Game of Quests Continues!";
        assertEquals(expectedMessage, finalQuestMessage("P1"));
        assertEquals(expectedMessage, finalQuestMessage("P2"));
        assertEquals(expectedMessage, finalQuestMessage("P3"));
        assertEquals(expectedMessage, finalQuestMessage("P4"));

        slowDown(10000);
    }

    @Test
    @DirtiesContext
    @DisplayName("2winner_game_2winner_quest")
    public void Scenario_2winner_game_2winner_quest() {
        //Navigate to webpage and register each player
        for (WebDriver driver : multiDriver) {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            driver.get("http://localhost:" + port);
            WebElement registerBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("registerBtn")));
            registerBtn.click();
            slowDown(1000);
        }

        //Rigging Adventure Deck for Proper Game Play
        rigAdventureDeckForGamePlay2winner_game_2winner_quest();
        slowDown(1000);
        //Rigging Players Hands
        rigAdventureDeckForPlayersHands2winner_game_2winner_quest();
        slowDown(1000);
        //Rigging Event Deck for Proper Game Play
        rigEventDeck2winner_game_2winner_quest();
        slowDown(1000);

        //Set all Players WebDriverWait
        WebDriverWait waitPlayer1 = new WebDriverWait(multiDriver[0], Duration.ofSeconds(10));
        WebDriverWait waitPlayer2 = new WebDriverWait(multiDriver[1], Duration.ofSeconds(10));
        WebDriverWait waitPlayer3 = new WebDriverWait(multiDriver[2], Duration.ofSeconds(10));
        WebDriverWait waitPlayer4 = new WebDriverWait(multiDriver[3], Duration.ofSeconds(10));

        //Player 1 clicks start button to start game
        WebElement startBtn = waitPlayer1.until(ExpectedConditions.visibilityOfElementLocated(By.id("startBtn")));
        startBtn.click();
        slowDown(1000);

        /*
        P1’s turn: P1 draws a 4-stage quest and decides to sponsor it.
         */
        //Player 1 clicks on 'Draw Event Card'
        WebElement drawEventCardPlayer1 = waitPlayer1.until(ExpectedConditions.visibilityOfElementLocated(By.id("draw")));
        drawEventCardPlayer1.click();
        slowDown(1000);

        WebElement acceptSponsorshipPlayer1 = waitPlayer1.until(ExpectedConditions.visibilityOfElementLocated(By.id("yesSponsor")));
        acceptSponsorshipPlayer1.click();
        slowDown(1000);

        /* P1 builds 4 stages:
            i. Stage 1: F5
        */
        
        WebElement cardElement = selectCard(waitPlayer1, "P1", "F5");
        cardElement.click();
        slowDown(1000);

        WebElement quitStagePlayer1 = waitPlayer1.until(ExpectedConditions.visibilityOfElementLocated(By.id("quitStage")));
        quitStagePlayer1.click();
        slowDown(1000);

        /* ii. Stage 2: F5 + dagger */
        cardElement = selectCard(waitPlayer1, "P1", "F5");
        cardElement.click();
        slowDown(1000);

        cardElement = selectCard(waitPlayer1, "P1", "D5");
        cardElement.click();
        slowDown(1000);

        quitStagePlayer1.click();
        slowDown(1000);

        /*
            iii. Stage 3: F10 + horse
        */
        cardElement = selectCard(waitPlayer1, "P1", "F10");
        cardElement.click();
        slowDown(1000);

        cardElement = selectCard(waitPlayer1, "P1", "H10");
        cardElement.click();
        slowDown(1000);

        quitStagePlayer1.click();
        slowDown(1000);

        /* iv. Stage 4: F10 + axe */
        cardElement = selectCard(waitPlayer1, "P1", "F10");
        cardElement.click();
        slowDown(1000);

        cardElement = selectCard(waitPlayer1, "P1", "B15");
        cardElement.click();
        slowDown(1000);

        quitStagePlayer1.click();
        slowDown(1000);

        //Player 1 (Sponsor) clicks Begins Quest
        WebElement beginQuestPlayer1 = waitPlayer1.until(ExpectedConditions.visibilityOfElementLocated(By.id("sponsorDone")));
        beginQuestPlayer1.click();
        slowDown(1000);

        /* Stage 1:
            P2, P3 and P4 participate in stage 1 and build their attack:
            i. P2 draws F5, discards F5
        */
        WebElement participatesInStagePlayer2 = waitPlayer2.until(ExpectedConditions.visibilityOfElementLocated(By.id("yesStage")));
        participatesInStagePlayer2.click();
        slowDown(1000);

        cardElement = selectCard(waitPlayer2, "P2", "F5");
        cardElement.click();
        slowDown(1000);

        WebElement okLeaveQuestDiscardPlayer2 = waitPlayer2.until(ExpectedConditions.visibilityOfElementLocated(By.id("leaveQuestDiscard")));
        okLeaveQuestDiscardPlayer2.click();
        slowDown(1000);

        /* 
            ii. P3 draws F40, discards F5 
        */
        WebElement participatesInStagePlayer3 = waitPlayer3.until(ExpectedConditions.visibilityOfElementLocated(By.id("yesStage")));
        participatesInStagePlayer3.click();
        slowDown(1000);

        cardElement = selectCard(waitPlayer3, "P3", "F5");
        cardElement.click();
        slowDown(1000);

        WebElement okLeaveQuestDiscardPlayer3 = waitPlayer3.until(ExpectedConditions.visibilityOfElementLocated(By.id("leaveQuestDiscard")));
        okLeaveQuestDiscardPlayer3.click();
        slowDown(1000);

        /* 
            iii. P4 draws F10, discards F10
        */
        WebElement participatesInStagePlayer4 = waitPlayer4.until(ExpectedConditions.visibilityOfElementLocated(By.id("yesStage")));
        participatesInStagePlayer4.click();
        slowDown(1000);

        cardElement = selectCard(waitPlayer4, "P4", "F10");
        cardElement.click();
        slowDown(1000);

        WebElement okLeaveQuestDiscardPlayer4 = waitPlayer4.until(ExpectedConditions.visibilityOfElementLocated(By.id("leaveQuestDiscard")));
        okLeaveQuestDiscardPlayer4.click();
        slowDown(1000);

        /* iv. P2 attack: 1 horse thus wins */
        cardElement = selectCard(waitPlayer2, "P2", "H10");
        cardElement.click();
        slowDown(1000);

        WebElement finishAttackPlayer2 = waitPlayer2.until(ExpectedConditions.visibilityOfElementLocated(By.id("quitAttack")));
        finishAttackPlayer2.click();
        slowDown(1000);

        /* v. P3 attack: nothing thus loses */
        WebElement finishAttackPlayer3 = waitPlayer3.until(ExpectedConditions.visibilityOfElementLocated(By.id("quitAttack")));
        finishAttackPlayer3.click();
        slowDown(1000);

        /* vi. P4 attack: 1 horse thus wins */
        cardElement = selectCard(waitPlayer4, "P4", "H10");
        cardElement.click();
        slowDown(1000);

        WebElement finishAttackPlayer4 = waitPlayer4.until(ExpectedConditions.visibilityOfElementLocated(By.id("quitAttack")));
        finishAttackPlayer4.click();
        slowDown(1000);


        /* P2 and P4 participate in stage 2:
            i.   P2 draws F10 (no need to discard) 
        */
        participatesInStagePlayer2.click();
        slowDown(1000);

        okLeaveQuestDiscardPlayer2.click();
        slowDown(1000);

        /* 
            ii.  P4 draws F30
        */
        participatesInStagePlayer4.click();
        slowDown(1000);

        okLeaveQuestDiscardPlayer4.click();
        slowDown(1000);

        /* 
            iii. P2 attack: sword thus wins
        */
        cardElement = selectCard(waitPlayer2, "P2", "S10");
        cardElement.click();
        slowDown(1000);

        finishAttackPlayer2.click();
        slowDown(1000);

        /* 
            iv.  P4 attack: sword thus wins
        */
        cardElement = selectCard(waitPlayer4, "P4", "S10");
        cardElement.click();
        slowDown(1000);

        finishAttackPlayer4.click();
        slowDown(1000);

        /* P2 and P4 participate in stage 3:
            i.   P2 draws F30 (no need to discard) 
        */
        participatesInStagePlayer2.click();
        slowDown(1000);

        okLeaveQuestDiscardPlayer2.click();
        slowDown(1000);

        /* 
            ii.  P4 draws F15
        */
        participatesInStagePlayer4.click();
        slowDown(1000);

        okLeaveQuestDiscardPlayer4.click();
        slowDown(1000);

        /* 
            iii. P2 attack: horse + sword, thus wins
        */
        cardElement = selectCard(waitPlayer2, "P2", "H10");
        cardElement.click();
        slowDown(1000);

        cardElement = selectCard(waitPlayer2, "P2", "S10");
        cardElement.click();
        slowDown(1000);

        finishAttackPlayer2.click();
        slowDown(1000);

        /* 
            iv.  P4 attack: horse + sword, thus wins
        */
        cardElement = selectCard(waitPlayer4, "P4", "H10");
        cardElement.click();
        slowDown(1000);

        cardElement = selectCard(waitPlayer4, "P4", "S10");
        cardElement.click();
        slowDown(1000);

        finishAttackPlayer4.click();
        slowDown(1000);

        /* P2 and P4 participate in stage 4:
            i.   P2 draws F15 (no need to discard) 
        */
        participatesInStagePlayer2.click();
        slowDown(1000);

        okLeaveQuestDiscardPlayer2.click();
        slowDown(1000);

        /* 
            ii.  P4 draws F20
        */
        participatesInStagePlayer4.click();
        slowDown(1000);

        okLeaveQuestDiscardPlayer4.click();
        slowDown(1000);

        /* 
            iii. P2 attack: sword + axe thus wins
        */
        cardElement = selectCard(waitPlayer2, "P2", "S10");
        cardElement.click();
        slowDown(1000);

        cardElement = selectCard(waitPlayer2, "P2", "B15");
        cardElement.click();
        slowDown(1000);

        finishAttackPlayer2.click();
        slowDown(1000);

        /* 
            iv.  P4 attack: sword + axe thus wins
        */
        cardElement = selectCard(waitPlayer4, "P4", "S10");
        cardElement.click();
        slowDown(1000);

        cardElement = selectCard(waitPlayer4, "P4", "B15");
        cardElement.click();
        slowDown(1000);

        finishAttackPlayer4.click();
        slowDown(1000);

        /* P2 and P4 each earn 4 shields (visible in UI)
        */
        //Player 1
        assertEquals(0, getShieldsCount("P1"));
        //Player 2
        assertEquals(4, getShieldsCount("P2"));
        //Player 3
        assertEquals(0, getShieldsCount("P3"));
        //Player 4
        assertEquals(4, getShieldsCount("P4"));

        
        /* P1 discards 7 cards used for quest,
            P1 draws 11 cards: 1xF5, 1xF10, 2xF15, 4xF20, 2xF25, 1xF30 
            P1 has 16 cards and discards: 1xF5, 1xF10, 2xF15
        */
        cardElement = selectCard(waitPlayer1, "P1", "F5");
        cardElement.click();
        slowDown(1000);

        cardElement = selectCard(waitPlayer1, "P1", "F10");
        cardElement.click();
        slowDown(1000);

        cardElement = selectCard(waitPlayer1, "P1", "F15");
        cardElement.click();
        slowDown(1000);

        cardElement = selectCard(waitPlayer1, "P1", "F15");
        cardElement.click();
        slowDown(1000);

        WebElement okLeaveQuestDiscardPlayer1 = waitPlayer1.until(ExpectedConditions.visibilityOfElementLocated(By.id("leaveQuestDiscard")));
        okLeaveQuestDiscardPlayer1.click();
        slowDown(1000);

        WebElement nextPlayerTurnPressPlayer1 = waitPlayer1.until(ExpectedConditions.visibilityOfElementLocated(By.id("leaveHotseat")));
        nextPlayerTurnPressPlayer1.click();
        slowDown(1000);

        /*
        P2’s turn: P2 draws a 3 stage quest and declines to sponsor it.
        */
        //Player 2 clicks on 'Draw Event Card'
        WebElement drawEventCardPlayer2 = waitPlayer2.until(ExpectedConditions.visibilityOfElementLocated(By.id("draw")));
        drawEventCardPlayer2.click();
        slowDown(1000);

        WebElement declinesSponsorshipPlayer2 = waitPlayer2.until(ExpectedConditions.visibilityOfElementLocated(By.id("noSponsor")));
        declinesSponsorshipPlayer2.click();
        slowDown(1000);

        /*
        P3 sponsors this quest and builds its stages:
        */
        WebElement acceptsSponsorshipPlayer3 = waitPlayer3.until(ExpectedConditions.visibilityOfElementLocated(By.id("yesSponsor")));
        acceptsSponsorshipPlayer3.click();
        slowDown(1000);

        /* i. Stage 1: F5 
        Stage 2: F5 + dagger 
        Stage 3: F5 + horse
        */
        cardElement = selectCard(waitPlayer3, "P3", "F5");
        cardElement.click();
        slowDown(1000);

        WebElement quitStagePlayer3 = waitPlayer3.until(ExpectedConditions.visibilityOfElementLocated(By.id("quitStage")));
        quitStagePlayer3.click();
        slowDown(1000);

        cardElement = selectCard(waitPlayer3, "P3", "F5");
        cardElement.click();
        slowDown(1000);

        cardElement = selectCard(waitPlayer3, "P3", "D5");
        cardElement.click();
        slowDown(1000);

        quitStagePlayer3.click();
        slowDown(1000);

        cardElement = selectCard(waitPlayer3, "P3", "F5");
        cardElement.click();
        slowDown(1000);

        cardElement = selectCard(waitPlayer3, "P3", "H10");
        cardElement.click();
        slowDown(1000);

        quitStagePlayer3.click();
        slowDown(1000);

        //Player 3 (Sponsor) clicks Begins Quest
        WebElement beginQuestPlayer3 = waitPlayer3.until(ExpectedConditions.visibilityOfElementLocated(By.id("sponsorDone")));
        beginQuestPlayer3.click();
        slowDown(1000);

        /*
            P1 declines to participate.
        */
        WebElement declineStagePlayer1 = waitPlayer1.until(ExpectedConditions.visibilityOfElementLocated(By.id("noStage")));
        declineStagePlayer1.click();
        slowDown(1000);

        /* P2 and P4 participate in stage 1:
            i. P2 draws dagger P4 draws dagger 
        */
        participatesInStagePlayer2.click();
        slowDown(1000);

        okLeaveQuestDiscardPlayer2.click();
        slowDown(1000);

        participatesInStagePlayer4.click();
        slowDown(1000);

        okLeaveQuestDiscardPlayer4.click();
        slowDown(1000);

        /* 
            ii. P2 attack: dagger thus wins
            iii. P4 attack: dagger thus wins
        */
        cardElement = selectCard(waitPlayer2, "P2", "D5");
        cardElement.click();
        slowDown(1000);

        finishAttackPlayer2.click();
        slowDown(1000);

        cardElement = selectCard(waitPlayer4, "P4", "D5");
        cardElement.click();
        slowDown(1000);

        finishAttackPlayer4.click();
        slowDown(1000);

        /* P2 and P4 participate in stage 2:
            i. P2 draws 1xF15 P4 draws 1xF15
        */
        participatesInStagePlayer2.click();
        slowDown(1000);

        okLeaveQuestDiscardPlayer2.click();
        slowDown(1000);

        participatesInStagePlayer4.click();
        slowDown(1000);

        okLeaveQuestDiscardPlayer4.click();
        slowDown(1000);

        /* 
            ii. P2 and P4 attacks are the same: 1 axe; both win
        */
        cardElement = selectCard(waitPlayer2, "P2", "B15");
        cardElement.click();
        slowDown(1000);

        finishAttackPlayer2.click();
        slowDown(1000);

        cardElement = selectCard(waitPlayer4, "P4", "B15");
        cardElement.click();
        slowDown(1000);

        finishAttackPlayer4.click();
        slowDown(1000);

        /* P2 and P4 participate in stage 3:
            i. P2 draws 1xF25 P4 draws 1xF25
        */
        participatesInStagePlayer2.click();
        slowDown(1000);

        okLeaveQuestDiscardPlayer2.click();
        slowDown(1000);

        participatesInStagePlayer4.click();
        slowDown(1000);

        okLeaveQuestDiscardPlayer4.click();
        slowDown(1000);

        /* 
            ii. P2 and P4 attack are the same: 1 excalibur; both win
        */
        cardElement = selectCard(waitPlayer2, "P2", "E30");
        cardElement.click();
        slowDown(1000);

        finishAttackPlayer2.click();
        slowDown(1000);

        cardElement = selectCard(waitPlayer4, "P4", "E30");
        cardElement.click();
        slowDown(1000);

        finishAttackPlayer4.click();
        slowDown(1000);

        /*  
            P3 discards 5 quest cards
            P3 draws 8 cards: 2xF20, 1xF25, 1xF30, sword, 2 axes, 1 lance 
            P3 trims their hand and discards 1xF20, 1xF25, 1xF30
        */
        cardElement = selectCard(waitPlayer3, "P3", "F20");
        cardElement.click();
        slowDown(1000);

        cardElement = selectCard(waitPlayer3, "P3", "F25");
        cardElement.click();
        slowDown(1000);

        cardElement = selectCard(waitPlayer3, "P3", "F30");
        cardElement.click();
        slowDown(1000);

        okLeaveQuestDiscardPlayer3.click();
        slowDown(1000);

        /* Correction Grid Reqs
        
        2winner_game_2winner_quest:
        Row 28: The number of shields and the number of cards of each participant at the end 
                of the scenario is correct.

                P1: shields = 0, card count = 12
                P2: shields = 7, card count = 9
                P3: shields = 0, card count = 12
                P4: shields = 7, card count = 9
        */

        /* P2 and P4 each earn 3 shields (visible in UI) */
        //Player 1
        assertEquals(0, getShieldsCount("P1"));
        assertEquals(12, getPlayerHandCardCount("P1"));
        //Player 2
        assertEquals(7, getShieldsCount("P2"));
        assertEquals(9, getPlayerHandCardCount("P2"));
        //Player 3
        assertEquals(0, getShieldsCount("P3"));
        assertEquals(12, getPlayerHandCardCount("P3"));
        //Player 4
        assertEquals(7, getShieldsCount("P4"));
        assertEquals(9, getPlayerHandCardCount("P4"));

        /* 
            P2 and P4 each earn 3 shields and both are declared (and asserted as) winners of the Quest. 
        */

        String expectedEndStageMessage = "Congratulations you WON the last stage of the Quest!\nYou have earned 3 shields!";
        //Player 2 screen displays it
        assertEquals(expectedEndStageMessage, getEndQuestStageWinnerMessage("P2"));
        //Player 4 screen displays it
        assertEquals(expectedEndStageMessage, getEndQuestStageWinnerMessage("P4"));

        /*
        Row 29: The number of shields and the number of cards of each participant is correctly 
                updated throughout the scenario. YES

        Row 30: The final hand of each player is displayed and is correct at the end of the scenario.
                
                P1: F15 F15 F20 F20 F20 F20 F25 F25 F30 H10 B15 L20
                P2: F10 F15 F15 F25 F30 F40 F50 L20 L20
                P3: F20 F40 D5 D5 S10 H10 H10 H10 H10 B15 B15 L20
                P4: F15 F15 F20 F25 F30 F50 F70 L20 L20
        */

        //Confirming asserts requried in the correction grid are made
        //Player 1
        assertEquals("F15 F15 F20 F20 F20 F20 F25 F25 F30 H10 B15 L20", getStringPlayerHand("P1"));
        //Player 2
        assertEquals("F10 F15 F15 F25 F30 F40 F50 L20 L20", getStringPlayerHand("P2"));
        //Player 3
        assertEquals("F20 F40 D5 D5 S10 H10 H10 H10 H10 B15 B15 L20", getStringPlayerHand("P3"));
        //Player 4
        assertEquals("F15 F15 F20 F25 F30 F50 F70 L20 L20", getStringPlayerHand("P4"));

        /*
        Also, ASSERT the winner(s) or absence of:
                None are Game Winner(s) (have >= 7 sheilds)
                The Message appears on all screens "A Game of Quests is Over!\nWinners are P2 & P4!"
        */

        String expectedMessage = "A Game of Quests is Over!\nWinner(s) are P2 & P4!";
        assertEquals(expectedMessage, finalQuestMessage("P1"));
        assertEquals(expectedMessage, finalQuestMessage("P2"));
        assertEquals(expectedMessage, finalQuestMessage("P3"));
        assertEquals(expectedMessage, finalQuestMessage("P4"));

        slowDown(10000);
    }

    @Test
    @DirtiesContext
    @DisplayName("")
    public void Scenario_1winner_game_with_events() {
        //Navigate to webpage and register each player
        for (WebDriver driver : multiDriver) {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            driver.get("http://localhost:" + port);
            WebElement registerBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("registerBtn")));
            registerBtn.click();
            slowDown(1000);
        }

        //Rigging Adventure Deck for Proper Game Play
        rigAdventureDeckForGamePlay1winner_game_with_events();
        slowDown(1000);
        //Rigging Players Hands
        rigAdventureDeckForPlayersHands1winner_game_with_events();
        slowDown(1000);
        //Rigging Event Deck for Proper Game Play
        rigEventDeck1winner_game_with_events();
        slowDown(1000);

        //Set all Players WebDriverWait
        WebDriverWait waitPlayer1 = new WebDriverWait(multiDriver[0], Duration.ofSeconds(10));
        WebDriverWait waitPlayer2 = new WebDriverWait(multiDriver[1], Duration.ofSeconds(10));
        WebDriverWait waitPlayer3 = new WebDriverWait(multiDriver[2], Duration.ofSeconds(10));
        WebDriverWait waitPlayer4 = new WebDriverWait(multiDriver[3], Duration.ofSeconds(10));

        //Player 1 clicks start button to start game
        WebElement startBtn = waitPlayer1.until(ExpectedConditions.visibilityOfElementLocated(By.id("startBtn")));
        startBtn.click();
        slowDown(1000);

        /*
        P1’s turn: P1 draws a 4-stage quest and decides to sponsor it.
         */
        //Player 1 clicks on 'Draw Event Card'
        WebElement drawEventCardPlayer1 = waitPlayer1.until(ExpectedConditions.visibilityOfElementLocated(By.id("draw")));
        drawEventCardPlayer1.click();
        slowDown(1000);

        WebElement acceptSponsorshipPlayer1 = waitPlayer1.until(ExpectedConditions.visibilityOfElementLocated(By.id("yesSponsor")));
        acceptSponsorshipPlayer1.click();
        slowDown(1000);

        /* P1 builds 4 stages:
            stage 1: F5 
            stage 2: F10 
            stage 3: F15 
            stage 4: F20
        */
        
        WebElement cardElement = selectCard(waitPlayer1, "P1", "F5");
        cardElement.click();
        slowDown(1000);

        WebElement quitStagePlayer1 = waitPlayer1.until(ExpectedConditions.visibilityOfElementLocated(By.id("quitStage")));
        quitStagePlayer1.click();
        slowDown(1000);

        cardElement = selectCard(waitPlayer1, "P1", "F10");
        cardElement.click();
        slowDown(1000);

        quitStagePlayer1.click();
        slowDown(1000);

        cardElement = selectCard(waitPlayer1, "P1", "F15");
        cardElement.click();
        slowDown(1000);

        quitStagePlayer1.click();
        slowDown(1000);

        cardElement = selectCard(waitPlayer1, "P1", "F20");
        cardElement.click();
        slowDown(1000);

        quitStagePlayer1.click();
        slowDown(1000);

        //Player 1 (Sponsor) clicks Begins Quest
        WebElement beginQuestPlayer1 = waitPlayer1.until(ExpectedConditions.visibilityOfElementLocated(By.id("sponsorDone")));
        beginQuestPlayer1.click();
        slowDown(1000);

        /* Stage 1:
            P2, P3 and P4 participate in stage 1 and build their attack:
            i. P2 draws F5, discards F5
        */
        WebElement participatesInStagePlayer2 = waitPlayer2.until(ExpectedConditions.visibilityOfElementLocated(By.id("yesStage")));
        participatesInStagePlayer2.click();
        slowDown(1000);

        cardElement = selectCard(waitPlayer2, "P2", "F5");
        cardElement.click();
        slowDown(1000);

        WebElement okLeaveQuestDiscardPlayer2 = waitPlayer2.until(ExpectedConditions.visibilityOfElementLocated(By.id("leaveQuestDiscard")));
        okLeaveQuestDiscardPlayer2.click();
        slowDown(1000);

        /* 
            ii. P3 draws F10, discards F10 
        */
        WebElement participatesInStagePlayer3 = waitPlayer3.until(ExpectedConditions.visibilityOfElementLocated(By.id("yesStage")));
        participatesInStagePlayer3.click();
        slowDown(1000);

        cardElement = selectCard(waitPlayer3, "P3", "F10");
        cardElement.click();
        slowDown(1000);

        WebElement okLeaveQuestDiscardPlayer3 = waitPlayer3.until(ExpectedConditions.visibilityOfElementLocated(By.id("leaveQuestDiscard")));
        okLeaveQuestDiscardPlayer3.click();
        slowDown(1000);

        /* 
            iii. P4 draws F20, discards F20
        */
        WebElement participatesInStagePlayer4 = waitPlayer4.until(ExpectedConditions.visibilityOfElementLocated(By.id("yesStage")));
        participatesInStagePlayer4.click();
        slowDown(1000);

        cardElement = selectCard(waitPlayer4, "P4", "F20");
        cardElement.click();
        slowDown(1000);

        WebElement okLeaveQuestDiscardPlayer4 = waitPlayer4.until(ExpectedConditions.visibilityOfElementLocated(By.id("leaveQuestDiscard")));
        okLeaveQuestDiscardPlayer4.click();
        slowDown(1000);

        /* P2, P3, P4 all use the same attack, a sword. All win and have 11 cards. */
        cardElement = selectCard(waitPlayer2, "P2", "S10");
        cardElement.click();
        slowDown(1000);

        WebElement finishAttackPlayer2 = waitPlayer2.until(ExpectedConditions.visibilityOfElementLocated(By.id("quitAttack")));
        finishAttackPlayer2.click();
        slowDown(1000);

        cardElement = selectCard(waitPlayer3, "P3", "S10");
        cardElement.click();
        slowDown(1000);

        WebElement finishAttackPlayer3 = waitPlayer3.until(ExpectedConditions.visibilityOfElementLocated(By.id("quitAttack")));
        finishAttackPlayer3.click();
        slowDown(1000);

        cardElement = selectCard(waitPlayer4, "P4", "S10");
        cardElement.click();
        slowDown(1000);

        WebElement finishAttackPlayer4 = waitPlayer4.until(ExpectedConditions.visibilityOfElementLocated(By.id("quitAttack")));
        finishAttackPlayer4.click();
        slowDown(1000);

        /* P2, P3 and P4 participate in stage 2:
            i.   P2 draws F15
        */
        participatesInStagePlayer2.click();
        slowDown(1000);

        okLeaveQuestDiscardPlayer2.click();
        slowDown(1000);

        /* 
            ii. P3 draws F5
        */
        participatesInStagePlayer3.click();
        slowDown(1000);

        okLeaveQuestDiscardPlayer3.click();
        slowDown(1000);

        /* 
            ii. P4 draws F25
        */
        participatesInStagePlayer4.click();
        slowDown(1000);

        okLeaveQuestDiscardPlayer4.click();
        slowDown(1000);

        /* P2, P3, P4 all use the same attack, a horse. All win and have 11 cards. */
        cardElement = selectCard(waitPlayer2, "P2", "H10");
        cardElement.click();
        slowDown(1000);

        finishAttackPlayer2.click();
        slowDown(1000);

        cardElement = selectCard(waitPlayer3, "P3", "H10");
        cardElement.click();
        slowDown(1000);

        finishAttackPlayer3.click();
        slowDown(1000);

        cardElement = selectCard(waitPlayer4, "P4", "H10");
        cardElement.click();
        slowDown(1000);

        finishAttackPlayer4.click();
        slowDown(1000);

        /* P2, P3 and P4 participate in stage 3:
            i.   P2 draws F5
        */
        participatesInStagePlayer2.click();
        slowDown(1000);

        okLeaveQuestDiscardPlayer2.click();
        slowDown(1000);

        /* 
            ii. P3 draws F10
        */
        participatesInStagePlayer3.click();
        slowDown(1000);

        okLeaveQuestDiscardPlayer3.click();
        slowDown(1000);

        /* 
            ii. P4 draws F20
        */
        participatesInStagePlayer4.click();
        slowDown(1000);

        okLeaveQuestDiscardPlayer4.click();
        slowDown(1000);

        /* P2, P3, P4 all use the same attack, an axe. All win and have 11 cards. */
        cardElement = selectCard(waitPlayer2, "P2", "B15");
        cardElement.click();
        slowDown(1000);

        finishAttackPlayer2.click();
        slowDown(1000);

        cardElement = selectCard(waitPlayer3, "P3", "B15");
        cardElement.click();
        slowDown(1000);

        finishAttackPlayer3.click();
        slowDown(1000);

        cardElement = selectCard(waitPlayer4, "P4", "B15");
        cardElement.click();
        slowDown(1000);

        finishAttackPlayer4.click();
        slowDown(1000);


        /* P2, P3 and P4 participate in stage 4:
            i.   P2 draws F5
        */
        participatesInStagePlayer2.click();
        slowDown(1000);

        okLeaveQuestDiscardPlayer2.click();
        slowDown(1000);

        /* 
            ii. P3 draws F10
        */
        participatesInStagePlayer3.click();
        slowDown(1000);

        okLeaveQuestDiscardPlayer3.click();
        slowDown(1000);

        /* 
            ii. P4 draws F20
        */
        participatesInStagePlayer4.click();
        slowDown(1000);

        okLeaveQuestDiscardPlayer4.click();
        slowDown(1000);

        /* P2, P3, P4 all use the same attack, a lance. All win and have 11 cards. */
        cardElement = selectCard(waitPlayer2, "P2", "L20");
        cardElement.click();
        slowDown(1000);

        finishAttackPlayer2.click();
        slowDown(1000);

        cardElement = selectCard(waitPlayer3, "P3", "L20");
        cardElement.click();
        slowDown(1000);

        finishAttackPlayer3.click();
        slowDown(1000);

        cardElement = selectCard(waitPlayer4, "P4", "L20");
        cardElement.click();
        slowDown(1000);

        finishAttackPlayer4.click();
        slowDown(1000);
        
        /* 
            P1 discards 4 cards used in quest and draws 8 cards: 2xF5, 2xF10, 4xF15
            P1 discards 2xF5, 2xF10 and has 12 cards.
        */
        cardElement = selectCard(waitPlayer1, "P1", "F5");
        cardElement.click();
        slowDown(1000);

        cardElement = selectCard(waitPlayer1, "P1", "F5");
        cardElement.click();
        slowDown(1000);

        cardElement = selectCard(waitPlayer1, "P1", "F10");
        cardElement.click();
        slowDown(1000);

        cardElement = selectCard(waitPlayer1, "P1", "F10");
        cardElement.click();
        slowDown(1000);

        WebElement okLeaveQuestDiscardPlayer1 = waitPlayer1.until(ExpectedConditions.visibilityOfElementLocated(By.id("leaveQuestDiscard")));
        okLeaveQuestDiscardPlayer1.click();
        slowDown(1000);

        WebElement nextPlayerTurnPressPlayer1 = waitPlayer1.until(ExpectedConditions.visibilityOfElementLocated(By.id("leaveHotseat")));
        nextPlayerTurnPressPlayer1.click();
        slowDown(1000);

        /* P2, P3 and P4 each earn 4 shields */
        //Player 1
        assertEquals(0, getShieldsCount("P1"));
        //Player 2
        assertEquals(4, getShieldsCount("P2"));
        //Player 3
        assertEquals(4, getShieldsCount("P3"));
        //Player 4
        assertEquals(4, getShieldsCount("P4"));

        /* 
            P2 draws ‘Plague’ and loses 2 shields (correctly updated and visible)
        */

        WebElement drawEventCardPlayer2 = waitPlayer2.until(ExpectedConditions.visibilityOfElementLocated(By.id("draw")));
        drawEventCardPlayer2.click();
        slowDown(1000);

        WebElement nextPlayerTurnPressPlayer2 = waitPlayer2.until(ExpectedConditions.visibilityOfElementLocated(By.id("leaveHotseat")));
        nextPlayerTurnPressPlayer2.click();
        slowDown(1000);

        //Player 1
        assertEquals(0, getShieldsCount("P1"));
        //Player 2
        assertEquals(2, getShieldsCount("P2"));
        //Player 3
        assertEquals(4, getShieldsCount("P3"));
        //Player 4
        assertEquals(4, getShieldsCount("P4"));

        /* P3 draws ‘Prosperity’: All 4 players receive 2 adventure cards */
        WebElement drawEventCardPlayer3 = waitPlayer3.until(ExpectedConditions.visibilityOfElementLocated(By.id("draw")));
        drawEventCardPlayer3.click();
        slowDown(1000);

        //Web app goes in order from the player who drew the Propsperity card
        /* 
            P3 draws 2 cards: 1 axe, 1xF40
            discards F5 (has 12 cards)
        */
        cardElement = selectCard(waitPlayer3, "P3", "F5");
        cardElement.click();
        slowDown(1000);

        WebElement nextPlayerProsperityOkPlayer3 = waitPlayer3.until(ExpectedConditions.visibilityOfElementLocated(By.id("prosperityOk")));
        nextPlayerProsperityOkPlayer3.click();
        slowDown(1000);

        /* 
            P4 draws 2 cards: 2 daggers
            discards F20 (has 12 cards)
        */
        cardElement = selectCard(waitPlayer4, "P4", "F20");
        cardElement.click();
        slowDown(1000);

        WebElement nextPlayerProsperityOkPlayer4 = waitPlayer4.until(ExpectedConditions.visibilityOfElementLocated(By.id("prosperityOk")));
        nextPlayerProsperityOkPlayer4.click();
        slowDown(1000);

        /* 
            P1 draws 2 cards: 2xF25 
            discards 1xF5, 1xF10 (has 12 cards)
        */
        cardElement = selectCard(waitPlayer1, "P1", "F5");
        cardElement.click();
        slowDown(1000);

        cardElement = selectCard(waitPlayer1, "P1", "F10");
        cardElement.click();
        slowDown(1000);

        WebElement nextPlayerProsperityOkPlayer1 = waitPlayer1.until(ExpectedConditions.visibilityOfElementLocated(By.id("prosperityOk")));
        nextPlayerProsperityOkPlayer1.click();
        slowDown(1000);

        /* 
            P2 draws 2 cards: horse, sword
            discards F5 (has 12 cards)
        */
        cardElement = selectCard(waitPlayer2, "P2", "F5");
        cardElement.click();
        slowDown(1000);

        WebElement nextPlayerProsperityOkPlayer2 = waitPlayer2.until(ExpectedConditions.visibilityOfElementLocated(By.id("prosperityOk")));
        nextPlayerProsperityOkPlayer2.click();
        slowDown(1000);

        /* P3 Leaves Hotseat */
        WebElement nextPlayerTurnPressPlayer3 = waitPlayer3.until(ExpectedConditions.visibilityOfElementLocated(By.id("leaveHotseat")));
        nextPlayerTurnPressPlayer3.click();
        slowDown(1000);

        //Player 1
        assertEquals(12, getPlayerHandCardCount("P1"));
        //Player 2
        assertEquals(12, getPlayerHandCardCount("P2"));
        //Player 3
        assertEquals(12, getPlayerHandCardCount("P3"));
        //Player 4
        assertEquals(12, getPlayerHandCardCount("P4"));

        /* 
            P4 draws ‘Queen’s favor’:
            i. Draws 1xF30, 1xF25 
            discards F25 and F30 
        */
        WebElement drawEventCardPlayer4 = waitPlayer4.until(ExpectedConditions.visibilityOfElementLocated(By.id("draw")));
        drawEventCardPlayer4.click();
        slowDown(1000);

        cardElement = selectCard(waitPlayer4, "P4", "F25");
        cardElement.click();
        slowDown(1000);

        cardElement = selectCard(waitPlayer4, "P4", "F30");
        cardElement.click();
        slowDown(1000);

        WebElement nextPlayerTurnPressPlayer4 = waitPlayer4.until(ExpectedConditions.visibilityOfElementLocated(By.id("leaveHotseat")));
        nextPlayerTurnPressPlayer4.click();
        slowDown(1000);

        /* 
            P1 draws a 3 stage quest and decides to sponsor it. 
            P1 builds 3 stages:

            Stage 1: F15 
            Stage 2: F15 + dagger 
            Stage3: F20 + dagger 
        */
        drawEventCardPlayer1.click();
        slowDown(1000);

        acceptSponsorshipPlayer1.click();
        slowDown(1000);
        
        cardElement = selectCard(waitPlayer1, "P1", "F15");
        cardElement.click();
        slowDown(1000);

        quitStagePlayer1.click();
        slowDown(1000);

        cardElement = selectCard(waitPlayer1, "P1", "F15");
        cardElement.click();
        slowDown(1000);

        cardElement = selectCard(waitPlayer1, "P1", "D5");
        cardElement.click();
        slowDown(1000);

        quitStagePlayer1.click();
        slowDown(1000);

        cardElement = selectCard(waitPlayer1, "P1", "F20");
        cardElement.click();
        slowDown(1000);

        cardElement = selectCard(waitPlayer1, "P1", "D5");
        cardElement.click();
        slowDown(1000);

        quitStagePlayer1.click();
        slowDown(1000);

        //Player 1 (Sponsor) clicks Begins Quest
        beginQuestPlayer1.click();
        slowDown(1000);

        /* 
        P2, P3 and P4 participate in stage 1

            i. P2 draws axe and discards F5
            ii. P3 draws horse and discards F10 
            iii. P4 draws F50 and discards F20
        */
        participatesInStagePlayer2.click();
        slowDown(1000);

        cardElement = selectCard(waitPlayer2, "P2", "F5");
        cardElement.click();
        slowDown(1000);

        okLeaveQuestDiscardPlayer2.click();
        slowDown(1000);

        participatesInStagePlayer3.click();
        slowDown(1000);

        cardElement = selectCard(waitPlayer3, "P3", "F10");
        cardElement.click();
        slowDown(1000);

        okLeaveQuestDiscardPlayer3.click();
        slowDown(1000);

        participatesInStagePlayer4.click();
        slowDown(1000);

        cardElement = selectCard(waitPlayer4, "P4", "F20");
        cardElement.click();
        slowDown(1000);

        okLeaveQuestDiscardPlayer4.click();
        slowDown(1000);

        /* P2, P3 attack with an axe, and win
        P4 attack with a horse and loses*/
        cardElement = selectCard(waitPlayer2, "P2", "B15");
        cardElement.click();
        slowDown(1000);

        finishAttackPlayer2.click();
        slowDown(1000);

        cardElement = selectCard(waitPlayer3, "P3", "B15");
        cardElement.click();
        slowDown(1000);

        finishAttackPlayer3.click();
        slowDown(1000);

        cardElement = selectCard(waitPlayer4, "P4", "H10");
        cardElement.click();
        slowDown(1000);

        finishAttackPlayer4.click();
        slowDown(1000);

        //Player 4
        assertEquals(11, getPlayerHandCardCount("P4"));

        /* 
        P2, and P3 participate in stage 2

            i. P2 draws sword
            ii. P3 draws sword
        */
        participatesInStagePlayer2.click();
        slowDown(1000);

        okLeaveQuestDiscardPlayer2.click();
        slowDown(1000);

        participatesInStagePlayer3.click();
        slowDown(1000);

        okLeaveQuestDiscardPlayer3.click();
        slowDown(1000);

        /* P2 attack: axe + horse thus wins (has 10 cards) 
        P3 attack: axe + sword thus wins (has 10 cards)*/
        cardElement = selectCard(waitPlayer2, "P2", "B15");
        cardElement.click();
        slowDown(1000);

        cardElement = selectCard(waitPlayer2, "P2", "H10");
        cardElement.click();
        slowDown(1000);

        finishAttackPlayer2.click();
        slowDown(1000);

        cardElement = selectCard(waitPlayer3, "P3", "B15");
        cardElement.click();
        slowDown(1000);

        cardElement = selectCard(waitPlayer3, "P3", "S10");
        cardElement.click();
        slowDown(1000);

        finishAttackPlayer3.click();
        slowDown(1000);

        
        //Player 2
        assertEquals(10, getPlayerHandCardCount("P2"));
        //Player 3
        assertEquals(10, getPlayerHandCardCount("P3"));

        /* 
        P2, and P3 participate in stage 3

            i. P2 draws F40
            ii. P3 draws F50
        */
        participatesInStagePlayer2.click();
        slowDown(1000);

        okLeaveQuestDiscardPlayer2.click();
        slowDown(1000);

        participatesInStagePlayer3.click();
        slowDown(1000);

        okLeaveQuestDiscardPlayer3.click();
        slowDown(1000);

        /* P2 attack: lance + sword thus wins (has 9 cards)
        P3 attack: excalibur thus wins (has 10 cards)*/
        cardElement = selectCard(waitPlayer2, "P2", "L20");
        cardElement.click();
        slowDown(1000);

        cardElement = selectCard(waitPlayer2, "P2", "S10");
        cardElement.click();
        slowDown(1000);

        finishAttackPlayer2.click();
        slowDown(1000);

        cardElement = selectCard(waitPlayer3, "P3", "E30");
        cardElement.click();
        slowDown(1000);

        finishAttackPlayer3.click();
        slowDown(1000);

        //Player 2
        assertEquals(9, getPlayerHandCardCount("P2"));
        //Player 3
        assertEquals(10, getPlayerHandCardCount("P3"));

        /*  
            P1 discards 5 quest cards, 
            draws 8 cards: 3 horses, 4 swords, 1 F35 (15 cards)
            P1 discards 3xF15
        */
        cardElement = selectCard(waitPlayer1, "P1", "F15");
        cardElement.click();
        slowDown(1000);

        cardElement = selectCard(waitPlayer1, "P1", "F15");
        cardElement.click();
        slowDown(1000);

        cardElement = selectCard(waitPlayer1, "P1", "F15");
        cardElement.click();
        slowDown(1000);

        /* 
        P2 and P3 earn 3 shields: P3 is declared (and asserted as) the winner
        */

        String expectedEndStageWinnerMessage = "Congratulations you WON the last stage of the Quest!\nYou have earned 3 shields!";
        String expectedEndStageLoserMessage = "Sorry, you LOST Stage 1!\nYou have been eliminated from participating in the Quest.";
        //Player 2 screen displays they Won
        assertEquals(expectedEndStageWinnerMessage, getEndQuestStageWinnerMessage("P2"));
        //Player 3 screen displays they Won
        assertEquals(expectedEndStageWinnerMessage, getEndQuestStageWinnerMessage("P3"));
        //Player 4 screen displays they Lost
        assertEquals(expectedEndStageLoserMessage, getEndQuestStageLoserMessage("P4"));

        /* P1 exits the Sponsor redraw */
        // okLeaveQuestDiscardPlayer1 = waitPlayer1.until(ExpectedConditions.visibilityOfElementLocated(By.id("leaveQuestDiscard")));
        okLeaveQuestDiscardPlayer1.click();
        slowDown(1000);

        /* Correction Grid Reqs
        
        1winner_game_with_events:
        Row 35: The number of shields and the number of cards of each participant at the end 
                of the scenario is correct.

                P1: shields = 0, card count = 12
                P2: shields = 5, card count = 9
                P3: shields = 7, card count = 10
                P4: shields = 4, card count = 11
        */

        //Player 1
        assertEquals(0, getShieldsCount("P1"));
        assertEquals(12, getPlayerHandCardCount("P1"));
        //Player 2
        assertEquals(5, getShieldsCount("P2"));
        assertEquals(9, getPlayerHandCardCount("P2"));
        //Player 3
        assertEquals(7, getShieldsCount("P3"));
        assertEquals(10, getPlayerHandCardCount("P3"));
        //Player 4
        assertEquals(4, getShieldsCount("P4"));
        assertEquals(11, getPlayerHandCardCount("P4"));

        /*
        Row 29: The number of shields and the number of cards of each participant is correctly 
                updated throughout the scenario. YES

        Row 30: The final hand of each player is displayed and is correct at the end of the scenario.
                
                P1: F25 F25 F35 D5 D5 S10 S10 S10 S10 H10 H10 H10
                P2: F15 F25 F30 F40 S10 S10 S10 H10 E30
                P3: F10 F25 F30 F40 F50 S10 S10 H10 H10 L20
                P4: F25 F25 F30 F50 F70 D5 D5 S10 S10 B15 L20
        */

        //Confirming asserts requried in the correction grid are made
        //Player 1
        assertEquals("F25 F25 F35 D5 D5 S10 S10 S10 S10 H10 H10 H10", getStringPlayerHand("P1"));
        //Player 2
        assertEquals("F15 F25 F30 F40 S10 S10 S10 H10 E30", getStringPlayerHand("P2"));
        //Player 3
        assertEquals("F10 F25 F30 F40 F50 S10 S10 H10 H10 L20", getStringPlayerHand("P3"));
        //Player 4
        assertEquals("F25 F25 F30 F50 F70 D5 D5 S10 S10 B15 L20", getStringPlayerHand("P4"));

        /*
        Also, ASSERT the winner(s) or absence of:
                None are Game Winner(s) (have >= 7 sheilds)
                The Message appears on all screens "A Game of Quests is Over!\nWinner(s) are P3!"
        */

        String expectedMessage = "A Game of Quests is Over!\nWinner(s) are P3!";
        assertEquals(expectedMessage, finalQuestMessage("P1"));
        assertEquals(expectedMessage, finalQuestMessage("P2"));
        assertEquals(expectedMessage, finalQuestMessage("P3"));
        assertEquals(expectedMessage, finalQuestMessage("P4"));

        slowDown(10000);
    }

    @Test
    @DirtiesContext
    @DisplayName("0_winner_quest")
    public void Scenario_0_winner_quest() {
        //Navigate to webpage and register each player
        for (WebDriver driver : multiDriver) {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            driver.get("http://localhost:" + port);
            WebElement registerBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("registerBtn")));
            registerBtn.click();
            slowDown(1000);
        }
        //Rigging Adventure Deck for Proper Game Play
        rigAdventureDeckForGamePlay0_winner_quest();
        slowDown(1000);
        //Rigging Players Hands
        rigAdventureDeckForPlayersHands0_winner_quest();
        slowDown(1000);
        //Rigging Event Deck for Proper Game Play
        rigEventDeck0_winner_quest();
        slowDown(1000);

        //Set all Players WebDriverWait
        WebDriverWait waitPlayer1 = new WebDriverWait(multiDriver[0], Duration.ofSeconds(10));
        WebDriverWait waitPlayer2 = new WebDriverWait(multiDriver[1], Duration.ofSeconds(10));
        WebDriverWait waitPlayer3 = new WebDriverWait(multiDriver[2], Duration.ofSeconds(10));
        WebDriverWait waitPlayer4 = new WebDriverWait(multiDriver[3], Duration.ofSeconds(10));

        //Player 1 clicks start button to start game
        WebElement startBtn = waitPlayer1.until(ExpectedConditions.visibilityOfElementLocated(By.id("startBtn")));
        startBtn.click();
        slowDown(1000);

        /*
        P1 draws a 2 stage quest and decides to sponsor it.
         */
        //Player 1 clicks on 'Draw Event Card'
        WebElement drawEventCardPlayer1 = waitPlayer1.until(ExpectedConditions.visibilityOfElementLocated(By.id("draw")));
        drawEventCardPlayer1.click();
        slowDown(1000);

        WebElement acceptSponsorshipPlayer1 = waitPlayer1.until(ExpectedConditions.visibilityOfElementLocated(By.id("yesSponsor")));
        acceptSponsorshipPlayer1.click();
        slowDown(1000);

        /* P1 builds 2 stages:
        i. Stage 1: F50 + dagger, sword, horse, axe, lance 
        */
        
        WebElement cardElement = selectCard(waitPlayer1, "P1", "F50");
        cardElement.click();
        slowDown(1000);

        cardElement = selectCard(waitPlayer1, "P1", "D5");
        cardElement.click();
        slowDown(1000);

        cardElement = selectCard(waitPlayer1, "P1", "S10");
        cardElement.click();
        slowDown(1000);

        cardElement = selectCard(waitPlayer1, "P1", "H10");
        cardElement.click();
        slowDown(1000);

        cardElement = selectCard(waitPlayer1, "P1", "B15");
        cardElement.click();
        slowDown(1000);

        cardElement = selectCard(waitPlayer1, "P1", "L20");
        cardElement.click();
        slowDown(1000);

        WebElement quitStagePlayer1 = waitPlayer1.until(ExpectedConditions.visibilityOfElementLocated(By.id("quitStage")));
        quitStagePlayer1.click();
        slowDown(1000);

        /* 
        ii. Stage 2: F70 + dagger, sword, horse, axe, lance
        */
        cardElement = selectCard(waitPlayer1, "P1", "F70");
        cardElement.click();
        slowDown(1000);

        cardElement = selectCard(waitPlayer1, "P1", "D5");
        cardElement.click();
        slowDown(1000);

        cardElement = selectCard(waitPlayer1, "P1", "S10");
        cardElement.click();
        slowDown(1000);

        cardElement = selectCard(waitPlayer1, "P1", "H10");
        cardElement.click();
        slowDown(1000);

        cardElement = selectCard(waitPlayer1, "P1", "B15");
        cardElement.click();
        slowDown(1000);

        cardElement = selectCard(waitPlayer1, "P1", "L20");
        cardElement.click();
        slowDown(1000);

        quitStagePlayer1.click();
        slowDown(1000);

        //Player 1 (Sponsor) clicks Begins Quest
        WebElement beginQuestPlayer1 = waitPlayer1.until(ExpectedConditions.visibilityOfElementLocated(By.id("sponsorDone")));
        beginQuestPlayer1.click();
        slowDown(1000);

        /*  P2, P3 and P4 participate in stage 1
            i. P2 participates, draws 1xF5, discards F5
        */
        WebElement participatesInStagePlayer2 = waitPlayer2.until(ExpectedConditions.visibilityOfElementLocated(By.id("yesStage")));
        participatesInStagePlayer2.click();
        slowDown(1000);

        cardElement = selectCard(waitPlayer2, "P2", "F5");
        cardElement.click();
        slowDown(1000);

        WebElement okLeaveQuestDiscardPlayer2 = waitPlayer2.until(ExpectedConditions.visibilityOfElementLocated(By.id("leaveQuestDiscard")));
        okLeaveQuestDiscardPlayer2.click();
        slowDown(1000);

        /* 
        ii. P3 participates, draws 1xF15, discards F15
        */
        WebElement participatesInStagePlayer3 = waitPlayer3.until(ExpectedConditions.visibilityOfElementLocated(By.id("yesStage")));
        participatesInStagePlayer3.click();
        slowDown(1000);

        cardElement = selectCard(waitPlayer3, "P3", "F15");
        cardElement.click();
        slowDown(1000);

        WebElement okLeaveQuestDiscardPlayer3 = waitPlayer3.until(ExpectedConditions.visibilityOfElementLocated(By.id("leaveQuestDiscard")));
        okLeaveQuestDiscardPlayer3.click();
        slowDown(1000);

        /* 
        iii. P4 participates, draws 1xF10, discards F10
        */
        WebElement participatesInStagePlayer4 = waitPlayer4.until(ExpectedConditions.visibilityOfElementLocated(By.id("yesStage")));
        participatesInStagePlayer4.click();
        slowDown(1000);

        cardElement = selectCard(waitPlayer4, "P4", "F10");
        cardElement.click();
        slowDown(1000);

        WebElement okLeaveQuestDiscardPlayer4 = waitPlayer4.until(ExpectedConditions.visibilityOfElementLocated(By.id("leaveQuestDiscard")));
        okLeaveQuestDiscardPlayer4.click();
        slowDown(1000);

        /* iv. P2 attack: Excalibur thus loses */
        cardElement = selectCard(waitPlayer2, "P2", "E30");
        cardElement.click();
        slowDown(1000);

        WebElement finishAttackPlayer2 = waitPlayer2.until(ExpectedConditions.visibilityOfElementLocated(By.id("quitAttack")));
        finishAttackPlayer2.click();
        slowDown(1000);

        /* v. P3 plays nothing as attack and thus loses */
        WebElement finishAttackPlayer3 = waitPlayer3.until(ExpectedConditions.visibilityOfElementLocated(By.id("quitAttack")));
        finishAttackPlayer3.click();
        slowDown(1000);

        /* g. vi. P4 plays nothing as attack and thus loses */
        WebElement finishAttackPlayer4 = waitPlayer4.until(ExpectedConditions.visibilityOfElementLocated(By.id("quitAttack")));
        finishAttackPlayer4.click();
        slowDown(1000);

        /* 
        The quest ends with no winner 
        P1 does discards 12 quests cards 
        P1 draws 14 cards: 1xF5, 1xF10, 1xF15, 4 daggers, 4 horses, 3 swords 
        P1 discards 1xF5, 1x10
        */
        cardElement = selectCard(waitPlayer1, "P1", "F5");
        cardElement.click();
        slowDown(1000);

        cardElement = selectCard(waitPlayer1, "P1", "F10");
        cardElement.click();
        slowDown(1000);

        WebElement okLeaveQuestDiscardPlayer1 = waitPlayer1.until(ExpectedConditions.visibilityOfElementLocated(By.id("leaveQuestDiscard")));
        okLeaveQuestDiscardPlayer1.click();
        slowDown(1000);

        /* 
            From A3 Specs:

            P1’s hand: 1xF15, 4 daggers, 4 horses, 3 swords
            P2 has 2xF5, 1xF10, 2xF15, 2xF20, 1xF25, 2xF30, 1xF40
            P3 and P4 have their initial hands
        */


        /* Correction Grid Reqs
        
        0_winner_quest:
        Row 42: the number of shields and the number of cards of each participant at the end 
                of the scenario is correct.

                P1: shields = 0, card count = 12
                P2: shields = 0, card count = 11
                P3: shields = 0, card count = 12
                P4: shields = 0, card count = 12
        */

        //Confirming asserts requried in the correction grid are made
        //Player 1
        assertEquals(0, getShieldsCount("P1"));
        assertEquals(12, getPlayerHandCardCount("P1"));
        //Player 2
        assertEquals(0, getShieldsCount("P2"));
        assertEquals(11, getPlayerHandCardCount("P2"));
        //Player 3
        assertEquals(0, getShieldsCount("P3"));
        assertEquals(12, getPlayerHandCardCount("P3"));
        //Player 4
        assertEquals(0, getShieldsCount("P4"));
        assertEquals(12, getPlayerHandCardCount("P4"));

        /*
        Row 43: The number of shields and the number of cards of each participant is correctly 
                updated throughout the scenario. YES

        Row 44: the final hand of each player is displayed and is correct at the end of the scenario
            
                P1: F15 D5 D5 D5 D5 S10 S10 S10 H10 H10 H10 H10
                P2: F5 F5 F10 F15 F15 F20 F20 F25 F30 F30 F40
                P3: F5 F5 F10 F15 F15 F20 F20 F25 F25 F30 F40 L20
                P4: F5 F5 F10 F15 F15 F20 F20 F25 F25 F30 F50 E30
        */

        //Confirming asserts requried in the correction grid are made
        //Player 1
        assertEquals("F15 D5 D5 D5 D5 S10 S10 S10 H10 H10 H10 H10", getStringPlayerHand("P1"));
        //Player 2
        assertEquals("F5 F5 F10 F15 F15 F20 F20 F25 F30 F30 F40", getStringPlayerHand("P2"));
        //Player 3
        assertEquals("F5 F5 F10 F15 F15 F20 F20 F25 F25 F30 F40 L20", getStringPlayerHand("P3"));
        //Player 4
        assertEquals("F5 F5 F10 F15 F15 F20 F20 F25 F25 F30 F50 E30", getStringPlayerHand("P4"));

        /*
        Also, ASSERT the winner(s) or absence of:
                None are Game Winner(s) (have >= 7 sheilds)
                The Message appears on all screens "There are no player's with 7 or more Shields<br>A Game of Quests Continues!"
        */

        String expectedMessage = "There are no player's with 7 or more Shields\nA Game of Quests Continues!";
        assertEquals(expectedMessage, finalQuestMessage("P1"));
        assertEquals(expectedMessage, finalQuestMessage("P2"));
        assertEquals(expectedMessage, finalQuestMessage("P3"));
        assertEquals(expectedMessage, finalQuestMessage("P4"));

        slowDown(10000);
    }

    //Helper Methods
    private String getEndQuestStageWinnerMessage(String playerID) {
        int webDriverIndex = Integer.valueOf(playerID.substring(1)) - 1;
        WebElement messageElement = new WebDriverWait(multiDriver[webDriverIndex], Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(By.id(playerID + "EndQuestStageMessage")));
        return messageElement.getText();
    }
    private String getEndQuestStageLoserMessage(String playerID) {
        int webDriverIndex = Integer.valueOf(playerID.substring(1)) - 1;
        WebElement messageElement = new WebDriverWait(multiDriver[webDriverIndex], Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(By.id(playerID + "EndStageMessage")));
        return messageElement.getText();
    }
    private String finalQuestMessage(String playerID) {
        String elementID = "finalQuestMessage";
        int webDriverIndex = Integer.valueOf(playerID.substring(1)) - 1;
        WebElement elementMessage = multiDriver[webDriverIndex].findElement(By.id(elementID));
        return elementMessage.getText();
    }
    private int getPlayerHandCardCount(String playerID) {
        int webDriverIndex = Integer.valueOf(playerID.substring(1)) - 1;
        WebElement playerHand = multiDriver[webDriverIndex].findElement(By.id(playerID + "hand"));
        List<WebElement> playerCards = playerHand.findElements(By.className("card"));
        return playerCards.size();
    }
    private String getFirstCardID(String playerID) {
        int webDriverIndex = Integer.valueOf(playerID.substring(1)) - 1;
        WebElement playerHand = multiDriver[webDriverIndex].findElement(By.id(playerID + "hand"));
        List<WebElement> playerCards = playerHand.findElements(By.className("card"));
        if (!playerCards.isEmpty()) {
            return playerCards.get(0).getAttribute("id").substring(2);
        }
        return null;
    }
    private int getShieldsCount(String id) {
        int webDriverIndex = Integer.valueOf(id.substring(1)) - 1;
        WebElement shieldCountList = multiDriver[webDriverIndex].findElement(By.id("shieldCountList"));
        List<WebElement> shieldCounts = shieldCountList.findElements(By.tagName("li"));
        int shields = 0;
        for (WebElement shieldCount : shieldCounts) {
            if (shieldCount.getText().startsWith(id + ":")) {
                String shieldText = shieldCount.getText().split(":")[1].trim();
                shields = Integer.parseInt(shieldText);
                break;
            }
        }
        return shields;
    }
    private String getStringPlayerHand(String id) {
        //Access player hand to get card id's in the order shown on the screen and return a string
        int webDriverIndex = Integer.valueOf(id.substring(1)) - 1;
        WebElement playerHand = multiDriver[webDriverIndex].findElement(By.id(id + "hand"));
        List<WebElement> playerCards = playerHand.findElements(By.className("card"));
        // Remove the player id prefix from all the cardIDs and join them by ", " to compare with the expected string
        List<String> cardIDs = new ArrayList<>();
        for (WebElement card : playerCards) {
            String cardID = card.getAttribute("id").substring(2);
            cardIDs.add(cardID);
        }
        return String.join(" ", cardIDs);
    }
    private WebElement selectCard(WebDriverWait wait, String id, String card) {
        String playerHandCardID = id + card;
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(playerHandCardID)));
    }
    private void slowDown(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //Rigging A1_Scenario Methods
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
    //Rigging 2_winner_game_2winner_quest
    private void rigAdventureDeckForPlayersHands2winner_game_2winner_quest() {

        /* A3 Specs - Initial Hands

        P1 - F5  F5  F10 F10 F15 F15  D5 H10 H10 B15 B15 L20
        P2 - F40 F50 S10 S10 S10 H10 H10 B15 B15 L20 L20 E30
        P3 - F5  F5  F5  F5  D5  D5  D5  H10 H10 H10 H10 H10
        P4 - F50 F70 S10 S10 S10 H10 H10 B15 B15 L20 L20 E30
        */

        //Rigging the Adventure Card Deck (backwards) to have the Players hand dealt in the right order

        //      P4   P3   P2   P1
        // 12 - E30, H10, E30, L20. 
        // 11 - L20, H10, L20, B15. 
        // 10 - L20, H10, L20, B15. 
        // 9 -  B15, H10, B15, H10. 
        // 8 -  B15, H10, B15, H10.
        // 7 -  H10, D5,  H10, D5.
        // 6 -  H10, D5,  H10, F15.
        // 5 -  S10, D5,  S10, F15.
        // 4 -  S10, F5,  S10, F10.
        // 3 -  S10, F5,  S10, F10.
        // 2 -  F70, F5,  F50, F5.
        // 1 -  F50, F5,  F40, F5.
        
        //Player hands P4 -> P3 -> P2 -> P1
        //12th Card
        // 12 - E30, H10, E30, L20.
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("E", 30));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("H", 10));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("E", 30));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("L", 20));

        //11th Card
        // 11 - L20, H10, L20, B15. 
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("L", 20));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("H", 10));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("L", 20));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("B", 15));

        //10th Card
        // 10 - L20, H10, L20, B15. 
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("L", 20));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("H", 10));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("L", 20));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("B", 15));

        //9th Card
        // 9 -  B15, H10, B15, H10. 
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("B", 15));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("H", 10));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("B", 15));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("H", 10));

        //8th Card
        // 8 -  B15, H10, B15, H10.
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("B", 15));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("H", 10));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("B", 15));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("H", 10));

        //7th Card
        // 7 -  H10, D5,  H10, D5.
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("H", 10));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("D", 5));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("H", 10));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("D", 5));

        //6th Card
        // 6 -  H10, D5,  H10, F15.
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("H", 10));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("D", 5));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("H", 10));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(15));

        //5th Card
        // 5 -  S10, D5,  S10, F15.
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("S", 10));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("D", 5));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("S", 10));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(15));

        //4th Card
        // 4 -  S10, F5,  S10, F10.
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("S", 10));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(5));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("S", 10));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(10));

        //3rd Card
        // 3 -  S10, F5,  S10, F10.
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("S", 10));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(5));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("S", 10));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(10));

        //2nd Card
        // 2 -  F70, F5,  F50, F5.
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(70));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(5));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(50));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(5));

        //1st Card
        // 1 -  F50, F5,  F40, F5.
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(50));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(5));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(40));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(5));
    }
    private void rigAdventureDeckForGamePlay2winner_game_2winner_quest() {
        //For game play
        //Q3 - P3 Sponsor redraw
        //P3 draws 8 cards: 2xF20, 1xF25, 1xF30, sword, 2 axes, 1 lance
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("L", 20));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("B", 15));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("B", 15));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("S", 10));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(30));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(25));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(20));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(20));

        //Cards drawn during Quest 3
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(25));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(25));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(15));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(15));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("D", 5));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("D", 5));

        //Q4 - P1 Sponsor redraw
        //P1 draws 11 cards: 1xF5, 1xF10, 2xF15, 4xF20, 2xF25, 1xF30
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(30));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(25));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(25));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(20));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(20));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(20));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(20));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(15));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(15));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(10));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(5));

        //Cards drawn during Quest 4
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(20));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(15));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(15));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(30));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(30));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(10));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(10));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(40));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(5));
    }
    private void rigEventDeck2winner_game_2winner_quest() {
        //Rigging the Event Deck to draw Quest 4 then Quest 3
        QuestCard q3 = new QuestCard(3);
        gameData.getEventDeck().getDeck().add(0, q3);

        QuestCard q4 = new QuestCard(4);
        gameData.getEventDeck().getDeck().add(0, q4);
    }
    //Rigging 1winner_game_with_events
    private void rigAdventureDeckForPlayersHands1winner_game_with_events() {

        /* A3 Specs - Initial Hands

        P1 - F5  F5  F10 F10 F15 F15 F20 F20  D5  D5  D5 D5
        P2 - F25 F30 S10 S10 S10 H10 H10 B15 B15 L20 L20 E30
        P3 - F25 F30 S10 S10 S10 H10 H10 B15 B15 L20 L20 E30
        P4 - F25 F30 F70 S10 S10 S10 H10 H10 B15 B15 L20 L20
        */

        //Rigging the Adventure Card Deck (backwards) to have the Players hand dealt in the right order

        //      P4   P3   P2   P1
        // 12 - L20, E30, E30, D5. 
        // 11 - L20, L20, L20, D5. 
        // 10 - B15, L20, L20, D5. 
        // 9 -  B15, B15, B15, D5. 
        // 8 -  H10, B15, B15, F20.
        // 7 -  H10, H10, H10, F20.
        // 6 -  S10, H10, H10, F15.
        // 5 -  S10, S10, S10, F15.
        // 4 -  S10, S10, S10, F10.
        // 3 -  F70, S10, S10, F10.
        // 2 -  F30, F30, F30, F5.
        // 1 -  F25, F25, F25, F5.
        
        //Player hands P4 -> P3 -> P2 -> P1
        //12th Card
        // 12 - L20, E30, E30, D5. 
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("L", 20));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("E", 30));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("E", 30));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("D", 5));

        //11th Card
        // 11 - L20, L20, L20, D5.
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("L", 20));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("L", 20));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("L", 20));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("D", 5));

        //10th Card
        // 10 - B15, L20, L20, D5. 
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("B", 15));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("L", 20));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("L", 20));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("D", 5));

        //9th Card
        // 9 -  B15, B15, B15, D5. 
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("B", 15));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("B", 15));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("B", 15));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("D", 5));

        //8th Card
        // 8 -  H10, B15, B15, F20.
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("H", 10));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("B", 15));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("B", 15));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(20));

        //7th Card
        // 7 -  H10, H10, H10, F20.
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("H", 10));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("H", 10));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("H", 10));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(20));

        //6th Card
        // 6 -  S10, H10, H10, F15.
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("S", 10));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("H", 10));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("H", 10));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(15));

        //5th Card
        // 5 -  S10, S10, S10, F15.
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("S", 10));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("S", 10));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("S", 10));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(15));

        //4th Card
        // 4 -  S10, S10, S10, F10.
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("S", 10));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("S", 10));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("S", 10));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(10));

        //3rd Card
        // 3 -  F70, S10, S10, F10.
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(70));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("S", 10));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("S", 10));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(10));

        //2nd Card
        // 2 -  F30, F30, F30, F5.
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(30));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(30));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(30));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(5));

        //1st Card
        // 1 -  F25, F25, F25, F5.
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(25));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(25));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(25));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(5));
    }
    private void rigAdventureDeckForGamePlay1winner_game_with_events() {
        //For game play
        //Q3 - P1 Sponsor redraw
        //P1 draws 8 cards: 3 horses, 4 swords, 1 F35
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(35));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("S", 10));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("S", 10));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("S", 10));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("S", 10));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("H", 10));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("H", 10));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("H", 10));

        //Cards drawn during Quest 3
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(50));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(40));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("S", 10));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("S", 10));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(50));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("H", 10));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("B", 15));

        //Cards drawn during Queen's Favor
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(25));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(30));

        //Cards drawn during Propsperity
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("D", 5));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("D", 5));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(40));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("B", 15));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("S", 10));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("H", 10));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(25));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(25));

        //Q4 - P1 Sponsor redraw
        //P1 discards 4 cards used in quest and draws 8 cards: 2xF5, 2xF10, 4xF15
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(15));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(15));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(15));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(15));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(10));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(10));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(5));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(5));

        //Cards drawn during Quest 4
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(20));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(10));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(5));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(20));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(10));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(5));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(25));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(5));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(15));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(20));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(10));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(5));
    }
    private void rigEventDeck1winner_game_with_events() {
        //Rigging the Event Deck to draw Quest 4, Plague, Prosperity, Queen's Favor, Quest 3
        QuestCard q3 = new QuestCard(3);
        gameData.getEventDeck().getDeck().add(0, q3);

        QueensFavorCard queenFavor = new QueensFavorCard();
        gameData.getEventDeck().getDeck().add(0, queenFavor);

        ProsperityCard prosperity = new ProsperityCard();
        gameData.getEventDeck().getDeck().add(0, prosperity);

        PlagueCard plague = new PlagueCard();
        gameData.getEventDeck().getDeck().add(0, plague);

        QuestCard q4 = new QuestCard(4);
        gameData.getEventDeck().getDeck().add(0, q4);
    }
    //Rigging 0_winner_quest
    private void rigAdventureDeckForPlayersHands0_winner_quest() {

        /* A3 Specs - Initial Hands

        P1 - F50 F70 D5 D5 S10 S10 H10 H10 B15 B15 L20 L20
        P2 - F5 F5 F10 F15 F15 F20 F20 F25 F30 F30 F40 E30
        P3 - F5 F5 F10 F15 F15 F20 F20 F25 F25 F30 F40 L20
        P4 - F5 F5 F10 F15 F15 F20 F20 F25 F25 F30 F50 E30
        */

        //Rigging the Adventure Card Deck (backwards) to have the Players hand dealt in the right order

        //      P4   P3   P2   P1
        // 12 - E30, L20, E30, L20. 
        // 11 - F50, F40, F40, L20. 
        // 10 - F30, F30, F30, B15. 
        // 9 -  F25, F25, F30, B15. 
        // 8 -  F25, F25, F25, H10.
        // 7 -  F20, F20, F20, H10.
        // 6 -  F20, F20, F20, S10.
        // 5 -  F15, F15, F15, S10.
        // 4 -  F15, F15, F15, D5.
        // 3 -  F10, F10, F10, D5.
        // 2 -  F5,  F5,  F5,  F70.
        // 1 -  F5,  F5,  F5,  F50.
        
        //Player hands P4 -> P3 -> P2 -> P1
        //12th Card
        // 12 - E30, L20, E30, L20. 
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("E", 30));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("L", 20));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("E", 30));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("L", 20));

        //11th Card
        // 11 - F50, F40, F40, L20. 
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(50));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(40));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(40));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("L", 20));

        //10th Card
        // 10 - F30, F30, F30, B15. 
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(30));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(30));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(30));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("B", 15));

        //9th Card
        // 9 -  F25, F25, F30, B15. 
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(25));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(25));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(30));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("B", 15));

        //8th Card
        // 8 -  F25, F25, F25, H10.
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(25));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(25));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(25));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("H", 10));

        //7th Card
        // 7 -  F20, F20, F20, H10.
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(20));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(20));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(20));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("H", 10));

        //6th Card
        // 6 -  F20, F20, F20, S10.
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(20));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(20));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(20));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("S", 10));

        //5th Card
        // 5 -  F15, F15, F15, S10.
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(15));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(15));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(15));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("S", 10));

        //4th Card
        // 4 -  F15, F15, F15, D5.
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(15));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(15));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(15));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("D", 5));

        //3rd Card
        // 3 -  F10, F10, F10, D5.
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(10));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(10));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(10));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("D", 5));

        //2nd Card
        // 2 -  F5,  F5,  F5,  F70.
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(5));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(5));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(5));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(70));

        //1st Card
        // 1 -  F5,  F5,  F5,  F50.
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(5));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(5));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(5));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(50));
    }
    private void rigAdventureDeckForGamePlay0_winner_quest() {
        //For game play
        //Sponsor redraw
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("S",10));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("S",10));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("S",10));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("H",10));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("H",10));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("H",10));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("H",10));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("D",5));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("D",5));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("D",5));
        gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("D",5));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(15));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(10));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(5));
        //Cards drawn during Quest
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(10));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(15));
        gameData.getAdventureDeck().getDeck().add(0, new FoeCard(5));
    }
    private void rigEventDeck0_winner_quest() {
        QuestCard q2 = new QuestCard(2);
        gameData.getEventDeck().getDeck().add(0, q2);
    }
}
