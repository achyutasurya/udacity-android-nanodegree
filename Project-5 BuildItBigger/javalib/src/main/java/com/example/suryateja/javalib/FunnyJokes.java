package com.example.suryateja.javalib;

import java.util.Random;

public class FunnyJokes {
    String[] jokearr;
    public String getJoke(){
        jokearr = new String[6];
        jokearr[0]= "Java is like Alzheimer, it starts off slow, but eventually, your memory is gone.";
        jokearr[1]= "Genders are a lot like booleans. There're only two of them.";
        jokearr[2]= "Debugging: Removing the needles from the haystack.";
        jokearr[3]= "Hey Girl,\nRoses are #ff0000,\nViolets are #0000ff,\nI use hex codes,\nBut I'd use RGB for you.";
        jokearr[4]= "If Bill Gates had a dime for every time Windows crashed ... Oh, he does.";
        jokearr[5]= "A dying programmer's last program is Goodbye World!";

        Random rand = new Random();
        int i = rand.nextInt(jokearr.length);
        return jokearr[i];
    }
}
