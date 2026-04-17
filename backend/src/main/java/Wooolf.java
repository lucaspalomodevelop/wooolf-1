package main.java;

import main.java.model.CharacterType;
import main.java.model.IdentityResolver;
import main.java.model.Character;

import java.util.ArrayList;

public class Wooolf {

    public  static void main(String[] args)
    {


        ArrayList<CharacterType> targets1 = new ArrayList<>();
        targets1.add(CharacterType.Sheep);
        Character c1 = new Character(CharacterType.Wolf,CharacterType.Wolf,5,targets1);


        ArrayList<CharacterType> targets2 = new ArrayList<>();
        targets2.add(CharacterType.Huntingdog);
        targets2.add(CharacterType.Hunter);
        Character c2 = new Character(CharacterType.Sheep,CharacterType.Sheep,1,targets2);



        System.out.println(c1);
        System.out.println(c2);
        System.out.println(IdentityResolver.resolveIdentity(c1,c2));
    }
}
