package com.example.comp5901_web_based_implementation.front_end_game_of_quests;

public class ConnectionMessage {

    private String message;
  
    public ConnectionMessage() {
    }
  
    public ConnectionMessage(String message) {
      this.message = message;
    }
  
    public String getMessage() {
      return message;
    }
  
    public void setMessage(String message) {
      this.message = message;
    }
  }