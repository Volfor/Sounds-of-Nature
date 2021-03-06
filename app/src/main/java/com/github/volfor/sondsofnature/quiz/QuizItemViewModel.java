package com.github.volfor.sondsofnature.quiz;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableBoolean;
import android.view.View;

import com.github.volfor.sondsofnature.events.AnswerEvent;
import com.github.volfor.sondsofnature.models.GameCard;
import com.github.volfor.sondsofnature.models.Quiz;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Volfor on 04.01.2017.
 * http://github.com/Volfor
 */

public class QuizItemViewModel extends BaseObservable {

    public ObservableBoolean correctAnswerVisibility = new ObservableBoolean(false);

    private Quiz quiz;
    private GameCard card;

    public QuizItemViewModel(Quiz quiz, GameCard card) {
        this.quiz = quiz;
        this.card = card;
    }

    @Bindable
    public int getImage() {
        return card.getImage();
    }

    public void onItemClick(View v) {
        if (quiz.check(card)) {
            quiz.correctCount++;
        }

        correctAnswerVisibility.set(quiz.check(card));
        EventBus.getDefault().post(new AnswerEvent(quiz.check(card), quiz.getCorrectCard()));
    }

}
