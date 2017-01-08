package com.ashishgoel.got.objects.kingDetails;

/**
 * Created by Ashish on 08/01/17.
 */

public class KingDetailsObject implements Comparable<KingDetailsObject> {

    int numberOfAttackWins;
    int numberOfDefenseWins;
    int numberOfBattlesLost;
    String name;
    String imageUrl;
    int numberOfDraws;

    Float currentRating;

    float defaultRating = 400f;

    public KingDetailsObject(String name) {
        this.name = name;
        if (currentRating == null) {
            currentRating = defaultRating;
        }
    }

    public int getNumberOfBattlesLost() {
        return numberOfBattlesLost;
    }

    public void setNumberOfBattlesLost(int numberOfBattlesLost) {
        this.numberOfBattlesLost = numberOfBattlesLost;
    }

    public int getNumberOfDraws() {
        return numberOfDraws;
    }

    public void setNumberOfDraws(int numberOfDraws) {
        this.numberOfDraws = numberOfDraws;
    }

    public int getNumberOfAttackWins() {
        return numberOfAttackWins;
    }

    public void setNumberOfAttackWins(int numberOfAttackWins) {
        this.numberOfAttackWins = numberOfAttackWins;
    }

    public int getNumberOfDefenseWins() {
        return numberOfDefenseWins;
    }

    public void setNumberOfDefenseWins(int numberOfDefenseWins) {
        this.numberOfDefenseWins = numberOfDefenseWins;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Float getCurrentRating() {
        if (currentRating == null) {
            currentRating = defaultRating;
        }
        return currentRating;
    }

    public void setCurrentRating(Float currentRating) {
        this.currentRating = currentRating;
    }

    @Override
    public int compareTo(KingDetailsObject obj) {
        return Float.compare(getCurrentRating(), obj.getCurrentRating());
    }
}
