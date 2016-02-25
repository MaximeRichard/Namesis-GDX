package com.gameleonevents.namesis;

/**
 * Created by Pierre on 25/02/2016.
 */

enum MiniJeuChoisi{
    CONTREATTAQUE,
    PIEGE,
    CACHECACHE
}

enum DirectionCacheCache{
    CENTRE,
    GAUCHE,
    DROITE
}

public class IA {

    private static int GenerateScore(int probability, int medianNummber){
        //The probability (%) represents the chance the function has to generate
        //a number above the medianNumber
        //First simulation -> it returns the probability interval
        int prob = (int)(Math.random() * 100);
        int score;
        //If the probability is in the "winner" interval.
        if(prob < (probability)){
            //Generate a number between the medianNumber and the Max
            score = medianNummber + (int)(Math.random() * (12 - medianNummber));
        }
        else{
            //else, generate an number below
            score = (int)(Math.random() * medianNummber);
        }
        return score;
    }

    private static boolean GenerateGameResult(int probabilty){
        //The probability indicates the chances the AI has to generate
        //a boolean equal to true (meaning victory in our case)
        int prob = (int)(Math.random() * 100);
        if(prob < (probabilty)){
            return true;
        }
        else
            return false;
    }

    public static int SimulerAttaque(){
        return GenerateScore(30, 10);
    }

    public static int SimulerDefense(){
        return GenerateScore(70, 9);
    }

    public static boolean SimulerPosePiege(){
        return GenerateGameResult(70);
    }

    public static boolean SimulerContreAttaque(){
        return GenerateGameResult(25);
    }

    public static PlayerMode ChoisirMode(){
        int prob = (int)(Math.random() * 10);
        if(prob < 4)
            return PlayerMode.predateur;
        else
            return PlayerMode.defenseur;
    }

    public static DirectionCacheCache ChoisirDirectionCacheCache(){
        int proba = (int)(Math.random() * 100);
        if(proba < 33)
            return DirectionCacheCache.CENTRE;
        else if(proba >= 33 && proba <= 66)
            return DirectionCacheCache.DROITE;
        else
            return DirectionCacheCache.GAUCHE;
    }

    public static boolean DesamorcerPiege(){
        return GenerateGameResult(70);
    }

    public static MiniJeuChoisi ChoisirMiniJeu(){
        int proba = (int)(Math.random() * 10);
        if(proba < 4)
            return MiniJeuChoisi.CONTREATTAQUE;
        else if(proba >= 4 && proba <= 6)
            return MiniJeuChoisi.PIEGE;
        else
            return MiniJeuChoisi.CACHECACHE;
    }

}
