package com.ten_ball_bowling_game.app.frame;

import java.util.Arrays;
import java.util.Objects;

public class FrameWithBonus extends IFrame {
    private IFrame originalGame;

    public FrameWithBonus(IFrame frame, String additionalPlay) {
        super(additionalPlay);
        this.originalGame = frame;
    }

    public FrameWithBonus(IFrame frame, String addPlayOne, String addPlayTwo){
        super(addPlayOne, addPlayTwo);
        this.originalGame = frame;
    }

    public FrameWithBonus(IFrame originalGame, String[] bonusPlays) {
        super(bonusPlays);
        this.originalGame = originalGame;
    }

    public IFrame getOriginalGame() {
        return this.originalGame;
    }

    public String[] getBonusPlays() {
        return super.getPlays();
    }

    @Override
    public String[] getPlays() {
        String[] originalGameList = this.originalGame.getPlays();
        String[] superList = super.getPlays();

        if(this.originalGame.isStrikePlay()) {
            originalGameList = Arrays.stream(originalGameList)
                    .filter(stackPlay -> Objects.nonNull(stackPlay)).toArray(String[]::new);
        }

        int fal = originalGameList.length;
        int sal = superList.length;

        String[] newList = new String[fal + sal];

        System.arraycopy(originalGameList, 0, newList, 0, fal);
        System.arraycopy(superList, 0, newList, fal, sal);
        return newList;
    }

    @Override
    public void setPlay(String play) {
        if(!originalGame.isDone()) {
            originalGame.setPlay(play);
        } else {
            super.setPlay(play);
        }
    }

    @Override
    public String getPlayAt(int index) {
        String[] originalPlays = this.originalGame.getPlays();

        if(this.originalGame.isStrikePlay()) {
            originalPlays = Arrays.stream(this.originalGame.getPlays())
                    .filter(stackPlay -> Objects.nonNull(stackPlay)).toArray(String[]::new);
        }

        int originalPlaySize = originalPlays.length;

        if(index <= originalPlaySize) {
            return originalPlays[index - 1];
        }
        int newIndex = index - originalPlaySize;
        return super.getPlays()[newIndex - 1];
    }

    @Override
    public boolean isDone() {
        boolean isBonusFilled = Arrays.stream(this.getPlays()).noneMatch(play ->Objects.isNull(play) || play.equals(""));
        return this.originalGame.isDone() && isBonusFilled ;
    }

    @Override
    public boolean isSparePlay() {
        return this.originalGame.isSparePlay();
    }

    @Override
    public boolean isStrikePlay() {
        return this.originalGame.isStrikePlay();
    }
}
