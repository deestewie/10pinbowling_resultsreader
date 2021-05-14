package com.bowlingGame.app.game;

import com.bowlingGame.app.frame.Frame;
import com.bowlingGame.app.frame.FrameWithBonus;
import com.bowlingGame.app.game.utils.GameUtils;
import com.bowlingGame.app.scoreboard.FrameSum;
import com.bowlingGame.app.exception.InvalidPlayInputException;
import com.bowlingGame.app.exception.InvalidRollAttemptException;
import com.bowlingGame.app.interfaces.IFrame;
import com.bowlingGame.app.interfaces.IGameFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.IntStream;

public class Game {

    private int gameTracker;
    private IGameFactory gameFactory;

    private IFrame[] frames;

    public Game(IGameFactory gameFactory) {
        this.gameFactory = gameFactory;
        this.gameReset();
    }

    public void gameReset() {
        this.gameTracker = 0;
        this.frames = new IFrame[10];
    }

    public void roll(String rollResult) throws InvalidPlayInputException, InvalidRollAttemptException, InvocationTargetException, IllegalAccessException {
        try {
            IFrame frame = null;

            if(this.gameTracker == 0){
                this.gameTracker =  this.gameTracker + 1;
                frame = gameFactory.generateFrameForGame(this.isLastFrame(), gameTracker, null, rollResult);
            }else {
                int currentIndex = this.gameTracker - 1;
                int nextFrame = this.gameTracker;

                if (!isLastFrame()) {
                    nextFrame =  this.frames[currentIndex].isDone() ? this.gameTracker + 1: this.gameTracker;
                }

                frame = gameFactory.generateFrameForGame(this.isLastFrame(), nextFrame, this.frames[currentIndex], rollResult);
                this.gameTracker = nextFrame;
            }

            this.frames[ this.gameTracker - 1 ] = frame;
        }catch(InvalidPlayInputException e) {
            System.err.println("Invalid Play");
        } catch (InvalidRollAttemptException e) {
            System.err.println("Invalid Roll");
        }

    }

    public IFrame[] getFramesStarted() {
        return Arrays.stream(this.frames).filter(frame -> frame != null).toArray(IFrame[]::new);
    }

    public boolean gameCompleted() {
        return this.getFramesStarted().length == 10;
    }

    public List<FrameSum> getFrameSum() {
        if(!this.hasGameStarted()) {
            return null;
        }

        List<FrameSum> listOfFrameAndSum = new ArrayList<>();
        int startedOrCompletedFramesCnt = this.getFramesStarted().length;
        IntStream.range(0, startedOrCompletedFramesCnt)
                .forEach(index -> {
                    IFrame frame = this.frames[index];
                    int previousScore = 0;

                    if(!listOfFrameAndSum.isEmpty()) {
                        previousScore = listOfFrameAndSum.get(index-1).getTotal();
                    }

                    int score = previousScore + frame.getScore();

                    if(frame.isSparePlay()) {
                        score = score + getSpareBonus(index);
                    } else if(frame.isStrikePlay()){
                        score = score + getStrikeBonus(index);
                    }

                    FrameSum frameSum = new FrameSum(index + 1, frame, score);
                    listOfFrameAndSum.add(frameSum);

                    System.out.println(String.format("%s => %s", frameSum.getFrame().getPlays(), score));
                });

        return listOfFrameAndSum;
    }

    public int getSpareBonus(int index){
        IFrame frame = this.frames[index + 1];
        return GameUtils.getNumericVersionOfPlay(frame.getPlayAt(1));
    }

    public boolean isLastFrame() {
        return this.gameTracker == 10;
    }

    public int getStrikeBonus(int index) {
//        System.out.println("Start Strike Bonus");
        return strikeBonusCalculator(0, index+1, 1);
    }

    private int strikeBonusCalculator(int strikeBonus, int index, int strikeCount){
        if(index == 10 || this.frames[index] == null){
            return strikeBonus + 0;
        }

        IFrame frame = this.frames[index];
        if(!frame.isStrikePlay()){
            int nextPlayOne = GameUtils.getNumericVersionOfPlay(this.frames[index ].getPlayAt(1));
            int nextPlayTwo = GameUtils.getNumericVersionOfPlay(this.frames[index ].getPlayAt(2));

            return strikeCount > 0 ? strikeBonus + nextPlayOne : strikeBonus + nextPlayOne + nextPlayTwo;
        }

        if(strikeCount == 2 && frame.isStrikePlay()) {
            return strikeBonus + frame.getScore();
        }

        strikeBonus += frame.getScore();
        return strikeBonusCalculator(strikeBonus, index + 1, strikeCount + 1);
    }

    public int getTotalScore() {
        if(!this.hasGameStarted()) {
            return 0;
        }

        List<FrameSum> frameSum = this.getFrameSum();
        return frameSum.get(frameSum.size() - 1).getTotal();
    }

    public boolean hasGameStarted() {
        return Arrays.stream(this.frames).anyMatch(iFrame -> iFrame != null);
    }

    public boolean isGutterGame() {
        return this.getTotalScore() == 0;
    }

    public boolean isPerfectGame() {
        return this.getTotalScore() == 300;
    }
}