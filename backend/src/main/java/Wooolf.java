package main.java;

import java.util.ArrayList;

public class Wooolf {

    public  static void main(String[] args)
    {


        ArrayList<CharacterType> targets1 = new ArrayList<>();
        targets1.add(CharacterType.Sheep);
        character c1 = new character(CharacterType.Wolf,CharacterType.Wolf,5,targets1);


        ArrayList<CharacterType> targets2 = new ArrayList<>();
        targets2.add(CharacterType.Huntingdog);
        targets2.add(CharacterType.Hunter);
        character c2 = new character(CharacterType.Sheep,CharacterType.Sheep,1,targets2);



        System.out.println(c1);
        System.out.println(c2);
        System.out.println(IdentityResolver.resolveIdentity(c1,c2));
    }
}
