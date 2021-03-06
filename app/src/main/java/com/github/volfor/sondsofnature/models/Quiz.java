package com.github.volfor.sondsofnature.models;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;

import com.github.volfor.sondsofnature.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static com.github.volfor.sondsofnature.root.MainActivity.ANIMALS;
import static com.github.volfor.sondsofnature.root.MainActivity.TRANSPORT;

/**
 * Created by Volfor on 04.01.2017.
 * http://github.com/Volfor
 */

public class Quiz {
    private static Quiz instance = null;

    public static final int TASK_COUNT = 20;

    public enum Difficulty {
        EASY,
        NORMAL,
        HARD,
        EXTRA
    }

    public int correctCount = 0;
    private GameCard correctCard;
    private List<GameCard> wrongCards;

    private MediaPlayer player;

    public static Quiz getInstance() {
        if (instance == null) {
            instance = new Quiz();
        }
        return instance;
    }

    public void createTask(Context context, int type) {
        List<GameCard> cards = new ArrayList<>();

        if (type == ANIMALS) {
            cards.addAll(Utils.getAnimalCards(context));
        } else if (type == TRANSPORT) {
            cards.addAll(Utils.getTransportCards(context));
        } else {
            cards.addAll(Utils.getAnimalCards(context));
            cards.addAll(Utils.getTransportCards(context));
        }

        Collections.shuffle(cards);

        correctCard = cards.get(0);
        wrongCards = new ArrayList<>();

        switch (getDifficulty()) {
            case EXTRA:
                wrongCards.add(cards.get(1));
                wrongCards.add(cards.get(2));
            case HARD:
                wrongCards.add(cards.get(3));
            case NORMAL:
                wrongCards.add(cards.get(4));
            case EASY:
                wrongCards.add(cards.get(5));
            default:
        }

        play(context);
    }

    public void play(final Context context) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                player = MediaPlayer.create(context, correctCard.getSounds()
                        .get(new Random().nextInt(correctCard.getSounds().size())));
                player.start();
            }
        }, 500);
    }

    public void replay() {
        if (player != null) {
            player.start();
        }
    }

    public boolean check(GameCard answer) {
        Utils.releasePlayer(player);
        return answer.equals(correctCard);
    }

    public GameCard getCorrectCard() {
        return correctCard;
    }

    public List<GameCard> getWrongCards() {
        return wrongCards;
    }

    public List<GameCard> getAllCards() {
        List<GameCard> cards = new ArrayList<>();
        cards.addAll(wrongCards);
        cards.add(new Random().nextInt(cards.size() + 1), correctCard);

        return cards;
    }

    public Difficulty getDifficulty() {
        if (correctCount < 5) {
            return Difficulty.EASY;
        } else if (correctCount < 10) {
            return Difficulty.NORMAL;
        } else if (correctCount < 15) {
            return Difficulty.HARD;
        } else {
            return Difficulty.EXTRA;
        }
    }

    public boolean isFinished() {
        return correctCount >= TASK_COUNT;
    }

    public void clear() {
        instance = null;
        Utils.releasePlayer(player);
    }

}
