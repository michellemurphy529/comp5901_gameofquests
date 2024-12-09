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

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
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
        int offset = 28;

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
        participatesInStagePlayer1 = waitPlayer1.until(ExpectedConditions.visibilityOfElementLocated(By.id("yesStage")));
        participatesInStagePlayer1.click();
        slowDown(1000);

        okLeaveQuestDiscardPlayer1 = waitPlayer1.until(ExpectedConditions.visibilityOfElementLocated(By.id("leaveQuestDiscard")));
        okLeaveQuestDiscardPlayer1.click();
        slowDown(1000);

        /* b. P3 is asked and decides to participate. 
        P3 draws a Lance
        */
        participatesInStagePlayer3 = waitPlayer3.until(ExpectedConditions.visibilityOfElementLocated(By.id("yesStage")));
        participatesInStagePlayer3.click();
        slowDown(1000);

        okLeaveQuestDiscardPlayer3 = waitPlayer3.until(ExpectedConditions.visibilityOfElementLocated(By.id("leaveQuestDiscard")));
        okLeaveQuestDiscardPlayer3.click();
        slowDown(1000);

        /* c. P4 is asked and decides to participate. 
        P4 draws a Lance 
        */
        participatesInStagePlayer4 = waitPlayer4.until(ExpectedConditions.visibilityOfElementLocated(By.id("yesStage")));
        participatesInStagePlayer4.click();
        slowDown(1000);

        okLeaveQuestDiscardPlayer4 = waitPlayer4.until(ExpectedConditions.visibilityOfElementLocated(By.id("leaveQuestDiscard")));
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

        finishAttackPlayer1 = waitPlayer1.until(ExpectedConditions.visibilityOfElementLocated(By.id("quitAttack")));
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

        finishAttackPlayer3 = waitPlayer3.until(ExpectedConditions.visibilityOfElementLocated(By.id("quitAttack")));
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

        finishAttackPlayer4 = waitPlayer4.until(ExpectedConditions.visibilityOfElementLocated(By.id("quitAttack")));
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
        participatesInStagePlayer3 = waitPlayer3.until(ExpectedConditions.visibilityOfElementLocated(By.id("yesStage")));
        participatesInStagePlayer3.click();
        slowDown(1000);

        okLeaveQuestDiscardPlayer3 = waitPlayer3.until(ExpectedConditions.visibilityOfElementLocated(By.id("leaveQuestDiscard")));
        okLeaveQuestDiscardPlayer3.click();
        slowDown(1000);

        /* b. P4 is asked and decides to participate. 
        P4 draws a Sword. 
        */
        participatesInStagePlayer4 = waitPlayer4.until(ExpectedConditions.visibilityOfElementLocated(By.id("yesStage")));
        participatesInStagePlayer4.click();
        slowDown(1000);

        okLeaveQuestDiscardPlayer4 = waitPlayer4.until(ExpectedConditions.visibilityOfElementLocated(By.id("leaveQuestDiscard")));
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

        finishAttackPlayer3 = waitPlayer3.until(ExpectedConditions.visibilityOfElementLocated(By.id("quitAttack")));
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

        finishAttackPlayer4 = waitPlayer4.until(ExpectedConditions.visibilityOfElementLocated(By.id("quitAttack")));
        finishAttackPlayer4.click();
        slowDown(1000);

        /* e. Resolution: P3’s and P4’s attack are sufficient go onto the next stage.
        f. All 2 participants discard the cards used for their attacks. 
        */

        /* 8) Stage 4:
        a. P3 is asked and decides to participate. 
        P3 draws an F30.
        */
        participatesInStagePlayer3 = waitPlayer3.until(ExpectedConditions.visibilityOfElementLocated(By.id("yesStage")));
        participatesInStagePlayer3.click();
        slowDown(1000);

        okLeaveQuestDiscardPlayer3 = waitPlayer3.until(ExpectedConditions.visibilityOfElementLocated(By.id("leaveQuestDiscard")));
        okLeaveQuestDiscardPlayer3.click();
        slowDown(1000);

        /* b. P4 is asked and decides to participate. 
        P4 draws a Lance. 
        */
        participatesInStagePlayer4 = waitPlayer4.until(ExpectedConditions.visibilityOfElementLocated(By.id("yesStage")));
        participatesInStagePlayer4.click();
        slowDown(1000);

        okLeaveQuestDiscardPlayer4 = waitPlayer4.until(ExpectedConditions.visibilityOfElementLocated(By.id("leaveQuestDiscard")));
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

        finishAttackPlayer3 = waitPlayer3.until(ExpectedConditions.visibilityOfElementLocated(By.id("quitAttack")));
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

        finishAttackPlayer4 = waitPlayer4.until(ExpectedConditions.visibilityOfElementLocated(By.id("quitAttack")));
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


        Row 22: The number of shields and the number of cards of each participant is correctly 
                updated throughout the scenario. YES

        Row 23: The final hand of each player is displayed and is correct at the end of the scenario.
                
                P1: F5 F10 F15 F15 F30 H10 B15 B15 L20
                P2: NEED TO FIGURE OUT
                P3: F5 F5 F15 F30 S10
                P4: F15 F15 F40 L20
        
        Also, ASSERT the winner(s) or absence of:
                None are Game Winner(s) (have >= 7 sheilds)

        */

        slowDown(1000);
    }

    private int getPlayerHandCardCount(String playerID) {
        int webDriverIndex = Integer.valueOf(playerID.substring(1)) - 1;
        WebElement playerHand = multiDriver[webDriverIndex].findElement(By.id(playerID + "hand"));
        List<WebElement> playerCards = playerHand.findElements(By.className("card"));
        return playerCards.size();
    }
    private String getFirstCardID(String playerID) {
        int webDriverIndex = Integer.valueOf(playerID.substring(1)) - 1;
        System.out.println(webDriverIndex);
        WebElement playerHand = multiDriver[webDriverIndex].findElement(By.id(playerID + "hand"));
        System.out.println(playerHand);
        List<WebElement> playerCards = playerHand.findElements(By.className("card"));
        if (!playerCards.isEmpty()) {
            System.out.println(playerCards.get(0).getAttribute("id").substring(2));
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
        System.out.println(webDriverIndex);
        WebElement playerHand = multiDriver[webDriverIndex].findElement(By.id(id + "hand"));
        System.out.println(playerHand);
        List<WebElement> playerCards = playerHand.findElements(By.className("card"));
        System.out.println(playerCards);

        // Remove the player id prefix from all the cardIDs and join them by ", " to compare with the expected string
        List<String> cardIDs = new ArrayList<>();
        for (WebElement card : playerCards) {
            String cardID = card.getAttribute("id").substring(2);
            System.out.println(cardID);
            cardIDs.add(cardID);
        }
        return String.join(" ", cardIDs);
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
        // gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("H",10));
        // gameData.getAdventureDeck().getDeck().add(0, new FoeCard(40));
        // gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("S",10));
        // gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("B",15));
        // gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("L",20));
        // gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("S",10));
        // gameData.getAdventureDeck().getDeck().add(0, new FoeCard(5));
        // gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("B",15));
        // gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("S",10));
        // gameData.getAdventureDeck().getDeck().add(0, new FoeCard(10));
        // gameData.getAdventureDeck().getDeck().add(0, new WeaponCard("L",20));

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
