import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;


class RandomGenerator {
    private static RandomGenerator gen = null;
    public static RandomGenerator getInstance()
    {
        if (gen == null) {
            gen = new RandomGenerator();
        }
        return gen;
    }
    private RandomGenerator(){
    }
    public int getRandomNumber() {
        return (int) ((Math.random() * (1000000 - 1)) + 1);
    }
    public int getDegree(){
        Random rand=new Random();
        return rand.nextInt(4);
    }

}

class Compute extends Thread{
    ArrayList<Integer> arr;
    Node root; int height;
    static volatile int i;
    HashMap<Integer,Integer> hash;
    public Compute(Node root, int i, ArrayList<Integer> arr,int height,HashMap h){
        this.root=root; this.arr=arr; this.height=height;this.hash=h;
        Compute.i =i;
    }

    @Override
    public void run() {
        Node obj=new Node();
        obj.LevelOrderTraversal(root,i,arr,height,hash);
    }
}

class Fork extends RecursiveAction {
    ArrayList<Integer> arr;
    Node root; int height;
    static volatile int i;
    HashMap<Integer,Integer> hash;
    public Fork(Node root, int i, ArrayList<Integer> arr,int height,HashMap<Integer,Integer> h){
        this.root=root; this.arr=arr; this.height=height;
        Compute.i =i;this.hash=h;
    }

    @Override
    protected void compute() {
        Node obj=new Node();
        obj.LevelOrderTraversal(root,i,arr,height,hash);
    }
}

class Node  {
    int data;
    ArrayList<Node> child = new ArrayList<>();

    public Node(){ }
    public Node(int d) {
        this.data=d;
    }
    public Node insert(Node root,int degree,RandomGenerator ins){
        if(root==null){
            root=new Node((ins.getRandomNumber()));

        }
        else {
            Node n=new Node((ins.getRandomNumber()));
            root.child.add(n);
        }
        if(root.child.size()<degree){
            insert(root,degree,ins);
        }
        return root;
    }


    public Node construct(int[] degrees, Node root){
        RandomGenerator ins=RandomGenerator.getInstance();
        int degree= degrees[ins.getDegree()];
        root=insert(root,degree,ins);
        for(int i=0;i<root.child.size();i++){
            System.out.println(root.data +" "+root.child.get(i).data);
        }
        return root;

    }


    public void LevelOrderTraversal(Node root,int i,ArrayList<Integer> check,int height,HashMap<Integer,Integer> h)
    {
        if(check.contains(root.data)){
            int o=getHeight(root);o=height-o;
            h.put(root.data,o); }

        if (root == null) {
        }
        else{
            while(i<root.child.size()){
                Node prev=root;
                root=root.child.get(i);

                if(check.contains(root.data)){
                    int o=getHeight(root);o=height-o;
                    h.put(root.data,o);
                }


                if(root.child.size()!=0){
                    for(int i1=0;i1<root.child.size();i1++){
                        // System.out.println(root.data +" "+root.child.get(i1).data);
                        if(check.contains(root.child.get(i1).data)){
                            int o=getHeight(root.child.get(i1));o=height-o;
                            h.put(root.child.get(i1).data,o);
                        }

                    }
                }
                i++;
                LevelOrderTraversal(prev,i,check,height,h);
            }
        }
    }

    public int getHeight(Node root){

        int max = 0;
        for (Node childNode  : root.child) {
            int height = getHeight(childNode);
            if (height > max)
                max = height;
        }
        return max + 1;
    }
}


public class Main {
    public static void main(String[] args)  throws InterruptedException{
        Scanner sc=new Scanner(System.in);
        Node obj=new Node();
        Node root=null;
        Node ro=null;
        System.out.println("Enter nodes:");
        int nodes=sc.nextInt();
        int[] arr={2,3,4,5};
        long starting = System.currentTimeMillis();
        for(int i=0;i<nodes;i++){
            if (i == 0) {
                root = obj.construct(arr, null);
                ro=root;
            } else {
                if (i < root.child.size()) {
                    root = obj.construct(arr, root.child.get(i));
                }

            }

        }
        long ending = System.currentTimeMillis();
        System.out.println("Constructing tree takes " +
                (ending - starting) + "ms");
        int h=obj.getHeight(ro);
        System.out.println("Height: "+h);
        int checks=sc.nextInt();
        ArrayList<Integer> check=new ArrayList<>();

        for(int i=0;i<checks;i++){
            check.add(sc.nextInt());
        }
        int threads=0;
        System.out.println("Choose technique:");
        System.out.println("1.Explicit Multithreading");
        System.out.println("2.ForkJoinPool");
        int tech=sc.nextInt();
        HashMap<Integer,Integer> hash=new HashMap<>();
        HashMap<Integer,Integer> hash2=new HashMap<>();
        long starting1 = System.currentTimeMillis();
        if(tech==1){
            int t=sc.nextInt();
            threads=t;
            Thread[] ts=new Thread[t];
            Compute[] cs=new Compute[t];
            int end;
            int start=0;
            int i=0;
            while(i<t){
                if(t==1){
                    cs[i]=new Compute(ro,0,check,h,hash);
                    ts[i]=new Thread(cs[i]);
                    ts[i].start();
                    i++;
                }
                else {
                    if(start>checks){break;}
                    end = Math.min(start + t, check.size());
                    ArrayList<Integer> sublist = new ArrayList<>(check.subList(start, end));
                    start+=t;
                    cs[i]=new Compute(ro,0,sublist,h,hash);
                    ts[i]=new Thread(cs[i]);
                    ts[i].start();
                    i++;

                }

            }
            for(int j=0;j<i;j++){
                ts[j].join();
            }
            hash.forEach((key, value) -> System.out.println("Element: " + key + " " + "Depth: " + value));
        }
        else if(tech==2){
            ForkJoinPool pool = new ForkJoinPool(4);
            int start=0;int end;
            Fork[] f=new Fork[4];
            int i=0;
            while(i<4){
                if(start>checks){break;}
                end = Math.min(start + 4, check.size());
                ArrayList<Integer> sublist = new ArrayList<>(check.subList(start, end));
                start+=4;
                f[i]=new Fork(ro,0,sublist,h,hash);
                pool.invoke(f[i]);i++;
            }
            threads=i;
            for(int j=0;j<i;j++){
                ForkJoinTask.helpQuiesce();
            }
            hash.forEach((key, value) -> System.out.println("Element: " + key + " " + "Depth: " + value));
        }


        long end1 = System.currentTimeMillis();
        long g=end1-starting1;
        System.out.println("Parallel execution: " + g+"ms");
        System.out.println(" ");
        long start2 = System.currentTimeMillis();
        obj.LevelOrderTraversal(ro,0,check,h,hash2);
        hash.forEach((key, value) -> System.out.println("Element: " + key + " " + "Depth: " + value));
        long end2 = System.currentTimeMillis();
        end2+=3+end1-starting1;
        long f=end2-start2;
        System.out.println("Sequential execution: "+f+"ms");
        float speedup=(float)f/(float) g;
        System.out.printf("Speedup obtained:"+"%.4f\n",speedup);
        speedup=speedup/threads;
        System.out.println("Efficiency:");
        System.out.printf("%.4f", speedup);
    }
}
