package org.example;

import java.io.PrintWriter;
import java.io.StringWriter;

public class GameDisplay {
    protected StringWriter stringWriter;
    protected PrintWriter output;
    protected PrintWriter consoleOutput;

    public GameDisplay() {
        this.stringWriter = new StringWriter();
        this.output = new PrintWriter(stringWriter);
        this.consoleOutput = new PrintWriter(System.out, true);
    }

    public String getOutput() {
        return "";
    }
}
