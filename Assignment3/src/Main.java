import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;


abstract class Player{
    private boolean flag;
    private final String name;
    private double HP;
    private int votes;
    private final int id;
    private boolean flag2;
    private boolean flag3;
    Scanner sc=new Scanner(System.in);
    public Player(boolean flag,String name,int id){
        this.flag=flag;
        this.name=name;
        this.id=id;
    }
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public double getHP() {
        return HP;
    }
    public void setHP(double HP) {
        this.HP = HP;
    }
    public boolean isFlag2() {
        return flag2;
    }
    public void setFlag2(boolean flag2) {
        this.flag2 = flag2;
    }
    public int getVotes() {
        return votes;
    }
    public void setVotes(int votes) {
        this.votes = votes;
    }
    public boolean isFlag() {
        return flag;
    }
    public boolean isFlag3() {
        return flag3;
    }
    public void setFlag3(boolean flag3) {
        this.flag3 = flag3;
    }
    public abstract int setTarget(ArrayList<Player> arr);
    public abstract int chooseTarget(ArrayList<Player> arr,int f);
    public abstract boolean equals(Object o1);
    public int getRandomInteger(int maximum, int minimum)
    {
        return ((int) (Math.random()*(maximum - minimum))) + minimum;
    }

    public boolean checkAlive(ArrayList<Player> arr,int g){
        for(int i=1;i<arr.size();i++){
            if(arr.get(i).getId()==g){
                return true;
            }
        }
        return false;
    }}

class compare implements Comparator<Player>{

    @Override
    public int compare(Player o1, Player o2) {
        return o1.getVotes()-o2.getVotes();
    }
}

class Pair <T1, T2> {
    private final T1 key;
    private final T2 value;
    public Pair(T1 _k, T2 _v) {
        key = _k; value = _v;
    }
    public T1 getKey() { return key; }
    public T2 getValue() { return value; }

}
class Mafia extends Player{
    public Mafia(boolean flag, String name,int id) {
        super(flag,name,id);
    }
    public int chooseTarget(ArrayList<Player> arr,int f){
        int n=getRandomInteger(f-1,1);
        boolean flag=false;
        ArrayList m=checkMafia(arr);
        for(int i=1;i<arr.size();i++){
            if(!(m.contains(n)) && arr.get(i).getId()==n && !(arr.get(i) instanceof Mafia)){
                flag=true;
                return i;
            }
        }

        if(!flag){
            int n1=getRandomInteger(f-1,1);
            for(int i=1;i<arr.size();i++){
                if(!(m.contains(n1) && arr.get(i).getId()==n1 && !(arr.get(i) instanceof Mafia))){
                    flag=true;
                    return i;
                } }
        }
        if(!flag){
            int n3=getRandomInteger(f-1,1);
            for(int i=1;i<arr.size();i++){
                if(!(m.contains(n3) && arr.get(i).getId()==n3 && !(arr.get(i) instanceof Mafia))){
                    flag=true;
                    return i;
                } }
        }
        for(int z=1;z<arr.size();z++) {
            if(!(arr.get(z) instanceof Mafia)){
                return z;
            }
        }
        return 0;
    }

    public ArrayList<Integer> checkMafia(ArrayList<Player> arr){
        ArrayList<Integer> m=new ArrayList<>();
        for(int i=1;i<arr.size();i++){
            if(arr.get(i) instanceof Mafia){
                m.add(arr.get(i).getId());
            }
        }
        return m;
    }


    @Override
    public int setTarget(ArrayList<Player> arr){
        System.out.println("Choose player to target");
        int target=sc.nextInt();
        int rem=0;
        for(int i=1;i<arr.size();i++){
            if(arr.get(i).getId()==target){
                rem=i;
            }
        }
        if(equals(arr.get(rem))){
            System.out.println("You cannot target a mafia");
            int target2=sc.nextInt();
            int rem2=0;
            for(int i=1;i<arr.size();i++){
                if(arr.get(i).getId()==target){
                    rem2=i;
                }
            }
            return rem2;
        }
        return rem;
    }


    public void kill(int target1, ArrayList<Player> arr) {
        int total=0;
        int Y=0;
        for(int i=1;i<arr.size();i++){
            if(equals(arr.get(i))){
                total+=arr.get(i).getHP();
                Y++;
            }
        }
        double red;
        int X=(int)arr.get(target1).getHP();
        if(Y!=0){
            red=X/(double)Y;
        }
        else{
            red=0;
        }
        if(total>=X){
            arr.get(target1).setHP(0);
        }
        else{
            arr.get(target1).setHP(arr.get(target1).getHP()-total);
        }
        if(arr.get(target1).isFlag3()){
            arr.get(target1).setHP(arr.get(target1).getHP()+500);
            System.out.println("No one died");
            for(int i=1;i<arr.size();i++){
                if(equals(arr.get(i))){
                    arr.get(i).setHP(arr.get(i).getHP()-red);
                }
            }
        }
        else {
            System.out.println(arr.get(target1).getName() + " has died");
            for(int i=1;i<arr.size();i++){
                if((arr.get(i)).isFlag3()){
                    arr.get(i).setHP(arr.get(i).getHP()+500);
                    break;
                }
                for(int i1=1;i1<arr.size();i1++){
                    if(equals(arr.get(i))){
                        arr.get(i1).setHP(arr.get(i1).getHP()-red);
                    }
                }
            }
            arr.remove(target1);
        }
    }

    @Override
    public boolean equals(Object o1) {
        return o1 instanceof Mafia;
    }
}

class Detective extends Player {
    public Detective(boolean flag, String name, int id) {
        super(flag, name, id);
    }

    @Override
    public int setTarget(ArrayList<Player> arr) {
        System.out.println("Choose player to test");
        int target = sc.nextInt();
        int rem = 0;
        for (int i = 1; i < arr.size(); i++) {
            if (arr.get(i).getId() == target) {
                rem = i;
            }
        }

        if (equals(arr.get(rem))) {
            System.out.println("You cannot test a detective");
            setTarget(arr);
        }
        if (arr.get(rem) instanceof Mafia) {
            System.out.println(arr.get(rem).getName() + " is a mafia");
            arr.get(rem).setFlag2(true);
        } else {
            System.out.println(arr.get(rem).getName() + " is not mafia");

        }
        return rem;
    }

    public int chooseTarget(ArrayList<Player> arr,int f) {
        int n = getRandomInteger(f - 1, 1);
        if (checkAlive(arr, n)) {
            for (int i = 1; i < arr.size(); i++) {
                if (arr.get(i).getId() == n && !equals(arr.get(i))) {
                    if(arr.get(i) instanceof Mafia){
                        arr.get(i).setFlag2(true);
                        return i;
                    }
                    else if(!(arr.get(i) instanceof Mafia) && !equals(arr.get(i))){
                        return i;
                    }

                    else{
                        redo(arr,f);
                    } }
            }
        } else {
            redo(arr,f);
        }
        return 1;
    }
    public void redo(ArrayList<Player> arr,int f){
        chooseTarget(arr,f);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Detective;
    }
}

class Healer extends Player {
    public Healer(boolean flag, String name, int id) {
        super(flag, name, id);
    }

    @Override
    public int setTarget(ArrayList<Player> arr) {
        System.out.println("Choose Player to heal");
        arr.forEach((n) -> n.setFlag3(false));
        int target = sc.nextInt();
        int index = 0;
        for (int i = 1; i < arr.size(); i++) {
            if (arr.get(i).getId() == target) {
                index = i;
            }
        }
        arr.get(index).setFlag3(true);
        return index;
    }

    public int chooseTarget(ArrayList<Player> arr,int f) {
        int n = getRandomInteger(f - 1, 1);
        arr.forEach((b) -> b.setFlag3(false));
        if (checkAlive(arr, n)) {
            for (int i = 1; i < arr.size(); i++) {
                if (arr.get(i).getId() == n) {
                    arr.get(i).setFlag3(true);
                    return i;
                }
            }
        } else {
            redo(arr,f);
        }
        return n;
    }

    @Override
    public boolean equals(Object o1) {
        return o1 instanceof Healer;
    }

    public void redo(ArrayList<Player> arr,int f){
        chooseTarget(arr,f);
    }
}
class Commoner extends Player{
    public Commoner(boolean flag,String name,int id) {
        super(flag,name,id);
    }

    @Override
    public int setTarget(ArrayList<Player> arr) {

        return 0;
    }
    public int chooseTarget(ArrayList<Player> arr,int f){
        return getRandomInteger(f-1, 1); }

    @Override
    public boolean equals(Object o1) {
        return false;
    }
}

class Game implements Comparable {
    int count, count1, count2 = 0;
    boolean b1 = true;
    Scanner sc = new Scanner(System.in);

    public void random(int num, ArrayList<Player> arr) {
        while (b1) {
            int n = getRandomInteger(num + 1, 1);
            if (!arr.get(n).isFlag()) {
                if (count < num / 5) {
                    arr.set(n, new Mafia(true, "player" + n, n));
                    arr.get(n).setHP(2500);
                    count++;

                } else if (count1 < num / 5) {
                    arr.set(n, new Detective(true, "player" + n, n));
                    arr.get(n).setHP(800);
                    count1++;

                } else if (count2 < Math.max(1, num / 10)) {
                    arr.set(n, new Healer(true, "player" + n, n));
                    arr.get(n).setHP(800);
                    count2++;

                } else {
                    arr.set(n, new Commoner(true, "player" + n, n));
                    arr.get(n).setHP(1000);
                    if (flagVal(arr)) {
                        b1 = false;
                    }
                }
            }
        }
    }

    public int getRandomInteger(int maximum, int minimum) {
        return ((int) (Math.random() * (maximum - minimum))) + minimum;
    }

    public boolean flagVal(ArrayList<Player> arr) {
        for (int i = 1; i < arr.size(); i++) {
            if (!arr.get(i).isFlag()) {
                return false;
            }
        }
        return true;
    }

    public void vote(ArrayList<Player> arr, int userId, ArrayList<Mafia> mafias) {
        arr.forEach((n) -> n.setVotes(0));
        mafias.forEach((n) -> n.setVotes(0));
        int id = 0;
        for (int i = 1; i < arr.size(); i++) {
            if (arr.get(i).isFlag2()) {
                id = arr.get(i).getId();
                for (int i1 = 0; i1 < mafias.size(); i1++) {
                    if (mafias.get(i1).getId() == id) {
                        mafias.remove(i1);
                        System.out.println("player" + id + " has been voted out");
                    }
                }
                arr.remove(i);
                return;
            }
        }

        for (int i = 1; i < arr.size(); i++) {
            if (arr.get(i).getId() == userId) {
                System.out.println("Select person to vote ");
                int vote = sc.nextInt();
                if (checkAlive(arr, vote)) {
                    Votes(vote, arr);
                } else {
                    System.out.println("The player voted for has died. Please vote for a player still in the game");
                    int vote2 = sc.nextInt();
                    Votes(vote2, arr);
                }

            } else {
                int n = getRandomInteger(arr.size() - 1, 1);
                if (checkAlive(arr, n)) {
                    Votes(n, arr);
                } else {
                    int n1 = getRandomInteger(arr.size() - 1, 1);
                    if (checkAlive(arr, n1)) {
                        Votes(n1, arr);
                    }
                }
            }
        }
        compare z = new compare();
        int max = 0;
        int player = 0;
        for (int i = 1; i < arr.size(); i++) {
            if (i + 1 < arr.size() && z.compare(arr.get(i), arr.get(i + 1)) > 0) {
                max = i;
                player = arr.get(i).getId();
            }
        }
        if(max==0){
            System.out.println("player" +arr.get(1).getId() + " has been voted out");
            for (int i = 0; i < mafias.size(); i++) {
                if (mafias.get(i).getId() == arr.get(1).getId()) {
                    mafias.remove(i);
                }

                arr.remove(1);
            }
        }
        else{
            System.out.println("player" + player + " has been voted out");
            arr.remove(max);
            for (int i = 0; i < mafias.size(); i++) {
                if (mafias.get(i).getId() == player) {
                    mafias.remove(i);
                }
            }
        }
    }

    public void Votes(int b, ArrayList<Player> arr) {
        for (int i = 1; i < arr.size(); i++) {
            if (arr.get(i).getId() == b) {
                arr.get(i).setVotes(arr.get(i).getVotes() + 1);
                break;
            }
        }
    }

    public Pair login(int num, ArrayList<Player> arr) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Choose a Character");
        System.out.println("1) Mafia");
        System.out.println("2) Detective");
        System.out.println("3) Healer");
        System.out.println("4) Commoner");
        System.out.println("5) Assign Randomly");
        int role = sc.nextInt();
        int d = 0;
        boolean c=true,c1 = true;
        if (role == 1) {
            random(num, arr);
            for (Player player : arr) {
                if (c && player instanceof Mafia) {
                    d = player.getId();
                    System.out.println("You are " + player.getName());
                    System.out.println("You are a mafia. Other mafias are ");
                    c = false;
                } else if (!c && player instanceof Mafia) {
                    System.out.println(player.getName());
                    c1=false;
                }
            }
            if(c1){
                System.out.println("[]");
            }
        } else if (role == 2) {
            random(num, arr);
            for (Player player : arr) {
                if (c && player instanceof Detective) {
                    d = player.getId();
                    System.out.println("You are " + player.getName());
                    System.out.println("You are a Detective. Other detectives are ");
                    c = false;
                } else if (!c && player instanceof Detective) {
                    System.out.println(player.getName());
                    c1=false;
                }
            }
            if(c1){
                System.out.println("[]");
            }
        } else if (role == 3) {
            random(num, arr);
            for (Player player : arr) {
                if (c && player instanceof Healer) {
                    d = player.getId();
                    System.out.println("You are " + player.getName());
                    System.out.println("You are a healer. Other healers are ");
                    c = false;
                } else if (!c && player instanceof Healer) {
                    System.out.println(player.getName());
                    c1=false;
                }
            }
            if(c1){
                System.out.println("[]");
            }
        } else if (role == 4) {
            random(num, arr);
            for (Player player : arr) {
                if (player instanceof Commoner) {
                    d = player.getId();
                    System.out.println("You are " + player.getName());
                    System.out.println("You are a commoner");
                    break;
                }
            }
        } else if (role == 5) {
            random(num, arr);
            System.out.println("You are " + arr.get(1).getName());
            d = arr.get(1).getId();
            int a = compareTo(arr.get(1));
            if (a == 0) {
                role = 1;
                System.out.println("You are a Mafia.Other mafias are");
            } else if (a == 0) {
                role = 2;
                System.out.println("You are a detective.Other detectives are");
            } else if (a == -1) {
                role = 3;
                System.out.println("You are a healer.Other healers are");
            } else {
                role = 4;
                System.out.println("You are a commoner.");
                return new Pair<>(role,d);
            }
            for (int i = 2; i < arr.size(); i++) {
                if (a == 0 && arr.get(i) instanceof Mafia) {
                    System.out.println(arr.get(i).getName());
                    c1=false;
                } else if (a == 1 && arr.get(i) instanceof Detective) {
                    System.out.println(arr.get(i).getName());c1=false;
                } else if (a == -1 && arr.get(i) instanceof Healer) {
                    System.out.println(arr.get(i).getName());c1=false;
                }
            }
            if(c1){
                System.out.println("[]"); }

        } else {
            System.out.println("Please enter a value between 1 to 5 to decide role");
            System.exit(0);
        }
        return new Pair<>(role, d);
    }


    public void rounds(int round, ArrayList<Player> arr) {
        System.out.println("Round" + round);
        System.out.println(arr.size() - 1 + " players remaining");
        for (int i = 1; i < arr.size(); i++) {
            System.out.print(arr.get(i).getName() + ",");
        }

        System.out.println("are alive");
    }

    public boolean checkAlive(ArrayList<Player> arr, int g) {
        for (int i = 1; i < arr.size(); i++) {
            if (arr.get(i).getId() == g) {
                return true;
            }
        }
        return false;
    }

    protected Pair check_specific(ArrayList<Player> arr) {
        boolean a = false;
        boolean b = false;
        for (int i = 1; i < arr.size(); i++) {
            if (arr.get(i) instanceof Detective) {
                a = true;
                break;
            }
        }
        for (int i = 1; i < arr.size(); i++) {
            if (arr.get(i) instanceof Healer) {
                b = true;
                break;
            }
        }
        Pair<Boolean, Boolean> p = new Pair<>(a, b);
        return p;
    }

    public Pair compute(ArrayList<Player> arr, ArrayList<Mafia> mafia) {
        int msize = mafia.size();
        int rsize = 0;
        for (int i = 1; i < arr.size(); i++) {
            if (!(arr.get(i) instanceof Mafia)) {
                rsize++;
            }
        }
        if (msize > 0) {
            double s=(double) rsize / (double) msize;
            double p=(double) msize/ (double) rsize;
            return new Pair(s,p);
        } else {
            return null;
        }
    }

    @Override
    public int compareTo(Object o) {
        Class c = o.getClass();
        return switch (c.getName()) {
            case "Mafia" -> 0;
            case "Detective" -> 1;
            case "Healer" -> -1;
            default -> 2;
        };
    }
}


public class Main {

    public static void main(String[] args) throws CloneNotSupportedException {
        Scanner sc = new Scanner(System.in);
        Game game = new Game();
        System.out.println("Welcome to Mafia");
        System.out.println("Number of players:");
        int num = sc.nextInt();
        if(num<6){
            System.out.println("The numbers of players must be greater than 6. Please choose again:");
            num=sc.nextInt();
        }
        ArrayList<Player> arr = new ArrayList<Player>();
        Mafia m = new Mafia(true, "", 0);
        Healer h = new Healer(true, "", 0);
        Detective d = new Detective(true, "", 0);
        int target1, target2=0, target3=0;
        Player b = new Player(false, "", 0) {
            @Override
            public int setTarget(ArrayList<Player> arr) {
                return 0;
            }
            @Override
            public int chooseTarget(ArrayList<Player> arr,int f) {
                return 0;
            }
            @Override
            public boolean equals(Object o1) { return false; }};

        for (int i = 0; i < num + 1; i++) {
            arr.add(b);
        }

        ArrayList<Mafia> mafia = new ArrayList<>();
        Pair z = game.login(num, arr);
        ArrayList<Player> clonedLis = (ArrayList<Player>) arr.clone();
        for (int i = 1; i < arr.size(); i++) {
            if (arr.get(i) instanceof Mafia) {
                mafia.add((Mafia) arr.get(i));
            }
        }
        int count = 1;
        Pair s = game.compute(arr, mafia);
        int id = (int) z.getValue();
        while (mafia.size() > 0 && ((double)s.getKey() > 1 || (double)s.getValue()>1)){
            game.rounds(count, arr);
            if ((int) z.getKey() == 1) {
                if (game.checkAlive(arr, id)) {
                    target1 = m.setTarget(arr);
                } else {
                    target1 = m.chooseTarget(arr,clonedLis.size());
                }
                Pair c=game.check_specific(arr);
                if((boolean)c.getKey()){
                    target2=d.chooseTarget(arr,clonedLis.size());
                }
                System.out.println("Detectives have chosen a player to test.");
                if((boolean)c.getValue()){
                    target3=h.chooseTarget(arr,clonedLis.size());
                }
                System.out.println("Healers have chosen someone to heal.");
                System.out.println("--End of Actions--");
                //System.out.println(target1);
                //System.out.println(target2);
                //System.out.println(target3);
                m.kill(target1, arr);
                Pair t=game.compute(arr,mafia);
                if((double)t.getValue()==1){
                    break;
                }
                game.vote(arr, (int) z.getValue(), mafia);

            } else if ((int) z.getKey() == 2) {
                target1 = m.chooseTarget(arr, clonedLis.size());
                System.out.println("Mafias have decided the target");
                Pair c=game.check_specific(arr);
                if((boolean)c.getKey()){
                    if (game.checkAlive(arr, id)) {
                        d.setTarget(arr);
                    } else {
                        d.chooseTarget(arr,clonedLis.size());
                    } }
                if((boolean)c.getValue()){
                    h.chooseTarget(arr,clonedLis.size());
                }
                System.out.println("Healers have chosen someone to heal.");
                System.out.println("--End of Actions--");
                //System.out.println("Target of mafia: " + target1);
                m.kill(target1, arr);
                Pair t=game.compute(arr,mafia);
                if((double)t.getValue()==1){
                    break;
                }
                game.vote(arr, (int) z.getValue(), mafia);

            } else if ((int) z.getKey() == 3) {
                target1 = m.chooseTarget(arr, clonedLis.size());
                System.out.println("Mafias have decided the target");
                Pair c=game.check_specific(arr);
                if((boolean)c.getKey()){
                    d.chooseTarget(arr,clonedLis.size());
                }
                System.out.println("Detectives have chosen a player to test.");
                if((boolean)c.getValue()){
                    if (game.checkAlive(arr, id)) {
                        h.setTarget(arr);
                    } else {
                        h.chooseTarget(arr,clonedLis.size());
                    }
                }
                System.out.println("--End of Actions--");
                m.kill(target1, arr);
                Pair t=game.compute(arr,mafia);
                if((double)t.getValue()==1){
                    break;
                }
                game.vote(arr, (int) z.getValue(), mafia);

            } else if ((int) z.getKey() == 4) {
                Pair c=game.check_specific(arr);
                target1 = m.chooseTarget(arr, clonedLis.size());
                System.out.println("Mafias have decided the target");
                if((boolean)c.getKey()){
                    d.chooseTarget(arr,clonedLis.size());
                }
                System.out.println("Detectives have chosen a player to test.");
                if((boolean)c.getValue()){
                    h.chooseTarget(arr,clonedLis.size());
                }
                System.out.println("Healers have chosen someone to heal.");
                System.out.println("--End of Actions--");
                m.kill(target1, arr);
                Pair t=game.compute(arr,mafia);
                if((double)t.getValue()==1){
                    break;
                }
                game.vote(arr, (int) z.getValue(), mafia);
            }

            System.out.println("--End of Round" + count + "--");
            System.out.println(" ");
            count++;
            arr.forEach((n) ->n.setFlag3(false));
            s = game.compute(arr, mafia);
            //System.out.println((double)s.getKey());
            //System.out.println((double)s.getValue());
            //System.out.println("Mafia size:" + mafia.size());
        }
        System.out.println("Game over.");
        if(mafia.size()==0){
            System.out.println("The Mafias have lost");
        }
        else {
            System.out.println("The Mafias have won");
        }
        for (int i = 1; i < clonedLis.size(); i++) {
            System.out.println(clonedLis.get(i).getName() + " was a " + clonedLis.get(i).getClass().getName());
        }
    }
}