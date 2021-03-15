package com.wb.simplerpggame.objects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class MapObj {
    private ArrayList<String> mapsArray = new ArrayList<>(Arrays.asList("FOREST", "RAVINE", "SEA", "ABYSS"));

    public String getMapDescription(String mapName) {
        switch (mapName) {
            case "FOREST":
                return "1 ~ 10";
            case "RAVINE":
                return "10 ~ 25";
            case "SEA":
                return "25 ~ 35";
            case "ABYSS":
                return "35 ~ 50";
            default:
                return null;
        }
    }

    public ArrayList<MonsterObj> getCurrentMapMonsters(String mapName) {
        switch (mapName) {
            case "FOREST":
                return initForest();
            case "RAVINE":
                return initRavine();
            case "SEA":
                return initSea();
            case "ABYSS":
                return initAbyss();
            default:
                return null;
        }
    }

    public ArrayList<String> getListOfMaps() {
        return mapsArray;
    }

    //return the list of monsters in forest map
    public ArrayList<MonsterObj> initForest() {
        ArrayList<MonsterObj> forestMonstersArray = new ArrayList<>();

        forestMonstersArray.add(MonsterObj.SLIME); //45
        forestMonstersArray.add(MonsterObj.CAVE_BAT); //25
        forestMonstersArray.add(MonsterObj.KOBOLD); //15
        forestMonstersArray.add(MonsterObj.GOBLIN); //10
        forestMonstersArray.add(MonsterObj.HIGH_ORC); //5

        Collections.reverse(forestMonstersArray);
        return forestMonstersArray;
    }

    //return the list of monsters in ravine map
    public ArrayList<MonsterObj> initRavine() {
        ArrayList<MonsterObj> ravineMonstersArray = new ArrayList<>();

        ravineMonstersArray.add(MonsterObj.PIXIE); //45
        ravineMonstersArray.add(MonsterObj.SALAMANDER); //25
        ravineMonstersArray.add(MonsterObj.LAMIA); //15
        ravineMonstersArray.add(MonsterObj.HARPY); //10
        ravineMonstersArray.add(MonsterObj.GRIFFIN); //5

        Collections.reverse(ravineMonstersArray);
        return ravineMonstersArray;
    }

    //return the list of monsters in sea map
    public ArrayList<MonsterObj> initSea() {
        ArrayList<MonsterObj> seaMonstersArray = new ArrayList<>();

        seaMonstersArray.add(MonsterObj.WATER_ELEMENTAL); //45
        seaMonstersArray.add(MonsterObj.SIREN); //25
        seaMonstersArray.add(MonsterObj.KELPIE); //15
        seaMonstersArray.add(MonsterObj.SERPENT); //10
        seaMonstersArray.add(MonsterObj.KRAKEN); //5

        Collections.reverse(seaMonstersArray);
        return seaMonstersArray;
    }

    //return the list of monsters in abyss map
    public ArrayList<MonsterObj> initAbyss() {
        ArrayList<MonsterObj> abyssMonstersArray = new ArrayList<>();

        abyssMonstersArray.add(MonsterObj.UNDEAD); //45
        abyssMonstersArray.add(MonsterObj.SKELETON); //25
        abyssMonstersArray.add(MonsterObj.GHOUL); //15
        abyssMonstersArray.add(MonsterObj.MINOTAUR); //10
        abyssMonstersArray.add(MonsterObj.MANTICORE); //5

        Collections.reverse(abyssMonstersArray);
        return abyssMonstersArray;
    }
}
