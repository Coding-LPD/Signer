
package com.scnu.zhou.signer.ui.widget.emoji;

import java.io.Serializable;

public class Emoji implements Serializable {
    private static final long serialVersionUID = 1L;
    private int icon;
    private char value;
    private String emoji;

    private Emoji() {
    }

    public static Emoji fromResource(int icon, int value) {
        Emoji emoji = new Emoji();
        emoji.icon = icon;
        emoji.value = (char) value;
        return emoji;
    }

    public static Emoji fromCodePoint(int codePoint) {
        Emoji emoji = new Emoji();
        emoji.emoji = newString(codePoint);
        return emoji;
    }

    public static Emoji fromChar(char ch) {
        Emoji emoji = new Emoji();
        emoji.emoji = Character.toString(ch);
        return emoji;
    }

    public static Emoji fromChars(String chars) {
        Emoji emoji = new Emoji();
        emoji.emoji = chars;
        return emoji;
    }

    public Emoji(String emoji) {
        this.emoji = emoji;
    }

    public char getValue() {
        return value;
    }

    public int getIcon() {
        return icon;
    }

    public String getEmoji() {
        return emoji;
    }

    public static final String newString(int codePoint) {
        if (Character.charCount(codePoint) == 1) {
            return String.valueOf(codePoint);
        } else {
            return new String(Character.toChars(codePoint));
        }
    }
}