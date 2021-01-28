import java.util.ArrayList;
import java.util.Scanner;

interface Login{
    void print(ArrayList<Customer> costumers, ArrayList<Restaurant> restaurants, boolean flag);
    void choose(int q,ArrayList<Customer> costumers, ArrayList<Restaurant> restaurants);
    void printRewards(int q, ArrayList<Customer> costumers, ArrayList<Restaurant> restaurants);
}

class Menu {
    ArrayList<Customer> customers = new ArrayList<>();
    ArrayList<Restaurant> restaurants = new ArrayList<>();
    Scanner sc = new Scanner(System.in);
    public void printMenu(ArrayList<Customer> customers, ArrayList<Restaurant> restaurants, boolean flag) {
        System.out.println("Welcome to Zotato:");
        System.out.println("1) Enter as Restaurant Owner");
        System.out.println("2) Enter as Customer");
        System.out.println("3) Check User Details");
        System.out.println("4) Company Account details");
        System.out.println("5) Exit");
        System.out.println(" ");

        Scanner sc = new Scanner(System.in);
        int query = sc.nextInt();
        Login lg;
        if (query == 1) {
            lg = new Owner();
            lg.print(customers, restaurants, true);
        } else if (query == 2) {
            lg = new Customer("r", "", " 0",0);
            lg.print(customers, restaurants, true);
        } else if (query == 3) {
            System.out.println("1) Customer List");
            System.out.println("2) Restaurant List");
            int d = sc.nextInt();
            if (d == 1) {
                for (int i = 0; i < customers.size(); i++) {
                    System.out.println(i + 1 + ") " + customers.get(i).getName());
                }
                int m = sc.nextInt();
                System.out.println(customers.get(m - 1).getName() + "(" + customers.get(m - 1).getCategory() + ")" + " , "+customers.get(m-1).getAddress()+" " + customers.get(m - 1).getWallet());
                printMenu(customers, restaurants, true);

            } else if (d == 2) {
                for (int i = 0; i < restaurants.size(); i++) {
                    System.out.println(i + 1 + ") " + restaurants.get(i).getName());
                }
                int m = sc.nextInt();
                printMenu(customers, restaurants, true);
                System.out.println(restaurants.get(m - 1).getName() + "(" + restaurants.get(m - 1).getCategory() + ")" + " , "+restaurants.get(m-1).getAddress()+ " total orders " + restaurants.get(m-1).getTotalOrders());
            }
        } else if (query == 4) {
            int x=0;
            double y=0;
            for (int i = 0; i < restaurants.size(); i++) {
                if (restaurants.get(i).getTransfee() != 0) {
                    y+=restaurants.get(i).getTransfee();
                }
            }
            System.out.println("Total Company balance- INR " + y);

            for (int i = 0; i < customers.size(); i++) {
                if (customers.get(i).isFlag() == 1) {
                    x+=customers.get(i).getDelivery();
                }
            }
            System.out.println("Total Delivery Charges Collected - INR " + x );
            printMenu(customers, restaurants, true);
        } else if (query == 5) {
            System.exit(0);
        }
    }

}



class Customer extends Menu implements Login {
    class Cart{
        ArrayList<Restaurant> r=new ArrayList<>();
        ArrayList<FoodItem> f=new ArrayList<>();
        public ArrayList<Restaurant> getR() {
            return r;
        }
        public ArrayList<FoodItem> getF() {
            return f;
        }
        public double printcart(int q, ArrayList<Customer> customers, double price) {
            System.out.println("Items in cart -");
            Cart c = customers.get(q - 1).getCart();
            FoodItem fi = c.getF().get(0);
            System.out.println(fi.getId() + " " + c.getR().get(0).getName() + "-" + fi.getName() + "-" + fi.getPrice() + "-" + customers.get(q-1).getCart().getF().size() + "-" +fi.getQuantity()+" "+ fi.getOffer() + "% off");
            price += c.getF().size()*(fi.getPrice());
            price = price - (price * fi.getOffer() / 100);
            price = price - (c.getR().get(0).getDiscount() * price / 100);
            if(price>100 && c.getR().get(0).getCategory().equals("Authentic")){
                price=price-50;
            }
            else if(price>200 && c.getR().get(0).getCategory().equals("Special")){
                price=price-25;
            }
            return price; }}

    private final String name;
    private final String category;
    private final String address;
    private double wallet=1000;
    private final int delivery;
    private int reward;
    private int flag;
    private Cart cart= new Cart();
    private Cart cart2 =new Cart();
    Customer(String name, String category, String address, int delivery) {
        this.name = name;
        this.category = category;
        this.address = address;
        this.delivery = delivery;
    }
    public String getName() {
        return name;
    }
    public String getCategory() {
        return category;
    }
    public double getWallet() {
        return wallet;
    }
    public Cart getCart() {
        return cart;
    }
    public String getAddress() {
        return address;
    }

    public void setCart2(Cart cart2) {
        this.cart2 = cart2;
    }

    public Cart getCart2() {
        return cart2;
    }

    public void setWallet(double wallet) {
        this.wallet = wallet;
    }
    public int getReward() {
        return reward;
    }
    public void setReward(int reward) {
        this.reward = reward;
    }
    public int getDelivery() {
        return delivery;
    }
    public int isFlag() {
        return flag; }
    public void setFlag(int flag) {
        this.flag = flag;
    }

    @Override
    public void print(ArrayList<Customer> customers, ArrayList<Restaurant> restaurants,boolean flag) {
        for (int i = 0; i < customers.size(); i++) {
            if (customers.get(i).getCategory().equals("")) {
                System.out.println(i + 1 + ")" + customers.get(i).getName());
            } else {
                System.out.println(i + 1 + ")" + customers.get(i).getName() + " " + "(" + customers.get(i).getCategory() + ")");
            }
        }
        int qx=sc.nextInt();
        choose(qx,customers,restaurants);
    }

    @Override
    public void choose(int q,ArrayList<Customer> customers, ArrayList<Restaurant> restaurants) {
        System.out.println("Welcome "+ customers.get(q-1).getName());
        System.out.println("Costumer Menu");
        System.out.println("1) Select Restaurant");
        System.out.println("2) checkout cart");
        System.out.println("3) Reward won");
        System.out.println("4) print recent orders");
        System.out.println("5) Exit");
        int z=sc.nextInt();

        int g=0;
        if(z==1){
            Login lg=new Owner();
            lg.print(customers,restaurants,false);
            g=sc.nextInt();
            System.out.println("Choose Item by code");
            for(int i=0;i<restaurants.get(g-1).getFoodl().size(); i++){
                System.out.println(restaurants.get(g-1).getFoodl().get(i).getId()+" "+restaurants.get(g-1).getFoodl().get(i).getName() +" "+restaurants.get(g-1).getFoodl().get(i).getPrice()+" "+ restaurants.get(g-1).getFoodl().get(i).getQuantity()+" "+restaurants.get(g-1).getFoodl().get(i).getOffer()+"% off"+" "+restaurants.get(g-1).getFoodl().get(i).getCategory());
            }
            int id=sc.nextInt();
            System.out.println("Enter Item quantity");
            int w=sc.nextInt();
            if (w != 0) {
                for (int i = 0; i < w; i++) {
                    for(int c=0;c<restaurants.get(g-1).getFoodl().size();c++){
                        if(restaurants.get(g-1).getFoodl().get(c).getId()==id){
                            customers.get(q - 1).getCart().getF().add(restaurants.get(g - 1).getFoodl().get(c));

                        }
                    }
                }
                for (int j = 0; j < restaurants.get(g - 1).getFoodl().size(); j++) {
                    int h = restaurants.get(g - 1).getFoodl().get(j).getQuantity();
                    restaurants.get(g - 1).getFoodl().get(j).setQuantity(h - w);
                }

                customers.get(q - 1).getCart().getR().add(restaurants.get(g - 1));
                System.out.println("Items added to cart");
            }
            choose(q,customers,restaurants);
        }
        else if(z==2){
            Cartcheckout(q,customers,restaurants);
            choose(q,customers,restaurants);
        }
        else if(z==3){
            printRewards(q,customers,restaurants);
            choose(q,customers,restaurants);
        }
        else if(z==4){
            printRecent(customers.get(q-1),customers.get(q-1).getCart2());
            choose(q,customers,restaurants);
        }
        else{
            super.printMenu(customers,restaurants,true);
        }
    }

    public void Cartcheckout(int q,ArrayList<Customer> customers, ArrayList<Restaurant> restaurants){
        double price=customers.get(q-1).getCart().printcart(q,customers,0.0);
        System.out.println("Delivery charge -"+customers.get(q-1).getDelivery()+" /-");
        double x=price+customers.get(q-1).getDelivery();
        System.out.println("Total order value- INR  "+ x);
        System.out.println("1) Proceed to checkout ");
        setFlag(1);
        if(sc.nextInt()==1){
            x=x-customers.get(q-1).getReward();
            if(customers.get(q-1).getWallet()-x>=0){
                System.out.println(customers.get(q-1).getCart().getF().size() +" items successfully brought for INR " + x +"/-");
                customers.get(q-1).setWallet(getWallet()-x);
                for(int i=0;i<customers.get(q-1).getCart().getR().size();i++){
                    int c=customers.get(q-1).getCart().getR().get(i).getId();
                    restaurants.get(c).setTransfee(restaurants.get(c).getTransfee()+ 0.01*price);
                    restaurants.get(c).setTotalOrders(restaurants.get(c).getTotalOrders()+1);
                    customers.get(q-1).setFlag(1);

                    String c1=restaurants.get(c).getCategory();
                    if(c1.equals("Fast Food") && (price/150>0)){
                        restaurants.get(c).setRewards((int)(price/150)*10);
                        customers.get(q-1).setReward((int)(price/150)*10);
                    }
                    else if(c1.equals("Authentic") && (price/200>0)){
                        int g=(int)price/200;
                        restaurants.get(c).setRewards(g*25);
                        customers.get(q-1).setReward(g*25);
                    }
                    else if(price/100>0){
                        restaurants.get(c).setRewards((int)(price/100)*5);
                        customers.get(q-1).setReward((int)(price/100)*5);
                    }
                }
                for(int i=0;i<customers.get(q-1).getCart().getR().size();i++){
                    customers.get(q-1).getCart2().getR().add(customers.get(q-1).getCart().getR().get(i));
                }
                for(int i=0;i<customers.get(q-1).getCart().getF().size();i++){
                    customers.get(q-1).getCart2().getF().add(customers.get(q-1).getCart().getF().get(i));
                }
                customers.get(q-1).getCart().getR().clear();
                customers.get(q-1).getCart().getF().clear();

            }
            else{
                System.out.println("Insufficient wallet balance");
                System.out.println("Remove Item by id");
                for(int i=0;i<customers.get(q-1).getCart().getF().size();i++){
                    FoodItem x1=customers.get(q-1).getCart().getF().get(i);
                    System.out.println(x1.getId()+" "+ x1.getName()+ " "+x1.getPrice());
                }
                int rem=sc.nextInt();
                for(int i=0;i<customers.get(q-1).getCart().getF().size();i++) {
                    if (customers.get(q - 1).getCart().getF().get(i).getId()==rem)
                        customers.get(q - 1).getCart().getF().remove(i);
                    break;
                }
                Cartcheckout(q,customers,restaurants);
            } }}
    @Override
    public void printRewards(int q, ArrayList<Customer> costumers, ArrayList<Restaurant> restaurants) {
        System.out.println("Reward points "+costumers.get(q-1).getReward());
    }

    public void printRecent(Customer customer,Cart c) {

        ArrayList<FoodItem> fi=new ArrayList<>();
        ArrayList<Integer> b=new ArrayList<>();
        for(int i=0;i<c.getR().size();i++){
            int qty=1;
            for(int j=0;j<c.getF().size();j++){
                if(b.size()>0) {
                    if (!fi.contains(c.getF().get(b.get(0)))) {
                        fi.add(c.getF().get(b.get(0)));
                    }
                    else{
                        qty++;

                    }
                }
                else{
                    if (!fi.contains(c.getF().get(j))) {
                        fi.add(c.getF().get(j));
                    }
                    else{
                        qty++;

                    }
                }

            }
            b.add(qty); }

        for(int i=0;i<c.getR().size();i++){
            if(i==1){
                System.out.println("Item name: " + fi.get(i).getName() + ",quantity " + b.get(i)+ " for Rs " + fi.get(i).getPrice() + " from restaurant " + c.getR().get(i).getName()+" and delivery charge " + customer.getDelivery());

            }
            else if(i==2){
                System.out.println("Item name: " + fi.get(i).getName() + ",quantity " + b.get(i) + " for Rs " + fi.get(i).getPrice() + " from restaurant " + c.getR().get(i).getName()+" and delivery charge " + customer.getDelivery());
            }
            else {
                System.out.println("Item name: " + fi.get(i).getName() + ",quantity " + b.get(i) + " for Rs " + fi.get(i).getPrice() + " from restaurant " + c.getR().get(i).getName() + " and delivery charge " + customer.getDelivery());
            }
        }

    }
}

class Restaurant extends Menu {
    private final String name;
    private final String category;
    private int rewards;
    private int discount;
    private int id;
    private double transfee;
    private int totalOrders;
    private final String address;
    private ArrayList<FoodItem> foodl=new ArrayList<>();
    Restaurant(String name, String category, String address) {
        this.name = name;
        this.category = category;
        this.address = address;
    }

    public int getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(int totalOrders) {
        this.totalOrders = totalOrders;
    }

    public String getName() {
        return name;
    }
    public int getDiscount() {
        return discount;
    }
    public void setDiscount(int discount) {
        this.discount = discount;
    }
    public int getRewards() {
        return rewards;
    }
    public int getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setTransfee(double transfee) {
        this.transfee = transfee;
    }
    public double getTransfee() {
        return transfee;
    }
    public void setRewards(int rewards) {
        this.rewards = rewards;
    }
    public ArrayList<FoodItem> getFoodl() {
        return foodl;
    }
    public void setFoodl(ArrayList<FoodItem> foodl) {
        this.foodl = foodl;
    }
    public String getCategory() {
        return category;
    }
    public void printRewards(int q, ArrayList<Restaurant> res, ArrayList<Customer> cus){
        int h=res.get(q-1).getRewards();
        for(int i=0;i< cus.size();i++){
            h-=cus.get(i).getReward();
        }
        System.out.println("Reward points "+h);
    }

}
class Owner extends Menu implements Login{

    private ArrayList<FoodItem> fi = new ArrayList<>();
    public void setFi(ArrayList<FoodItem> fi) {
        this.fi = fi;
    }
    public ArrayList<FoodItem> getFi() {
        return fi;
    }

    @Override
    public void print(ArrayList<Customer> costumers, ArrayList<Restaurant> restaurants, boolean flag) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Choose Restaurant");
        for (int i = 0; i < restaurants.size(); i++) {
            if (restaurants.get(i).getCategory().equals("")) {
                System.out.println(i + 1 + ")" + restaurants.get(i).getName());
                restaurants.get(i).setId(i);
            } else {
                System.out.println(i + 1 + ")" + restaurants.get(i).getName() + " " + "(" + restaurants.get(i).getCategory() + ")");
                restaurants.get(i).setId(i);
            }
        }
        if (flag) {
            int q = sc.nextInt();
            choose(q, costumers, restaurants);
        }
    }

    @Override
    public void choose(int q, ArrayList<Customer> costumers, ArrayList<Restaurant> restaurants) {
        System.out.println("Welcome " + restaurants.get(q - 1).getName());
        System.out.println("1) Add item");
        System.out.println("2) Edit item");
        System.out.println("3) Print Rewards");
        System.out.println("4) Discount on bill value");
        System.out.println("5) Exit");
        int z = sc.nextInt();
        if (z == 1) {
            add(q,restaurants);
            choose(q, costumers, restaurants);
        } else if (z == 2) {
            edit(q, restaurants);
            choose(q, costumers, restaurants);

        } else if (z == 3) {
            printRewards(q,costumers,restaurants);
            choose(q, costumers, restaurants);
        } else if (z == 4) {
            if (restaurants.get(q - 1).getCategory().equals("Authentic") || restaurants.get(q - 1).getCategory().equals("Fast Food")) {
                System.out.println("Enter discount on bill value: ");
                int billv = sc.nextInt();
                restaurants.get(q - 1).setDiscount(billv);
                System.out.println("Offer on bill value- " + billv);
            } else {
                System.out.println("Inapplicable");
            }
            choose(q, costumers, restaurants);
        } else {
            super.printMenu(costumers, restaurants, true);
        }
    }
    public void add(int q, ArrayList<Restaurant> restaurants){
        int c=0;
        for(int i=0;i<restaurants.size();i++){
            c+=restaurants.get(i).getFoodl().size();
        }
        FoodItem fi = new FoodItem(c+1);
        System.out.println("Food name");
        fi.setName(sc.next());
        System.out.println("Item price:");
        fi.setPrice(sc.nextInt());
        System.out.println("Item quantity:");
        fi.setQuantity(sc.nextInt());
        System.out.println("Item category:");
        fi.setCategory(sc.next());
        System.out.println("Offer:");
        fi.setOffer(sc.nextInt());
        getFi().add(fi);
        restaurants.get(q-1).setFoodl(getFi());
        System.out.println(fi.getId() + " " + fi.getName() + " " + fi.getPrice() + " " + fi.getQuantity() +" "+ fi.getOffer() + "% off" + " " + fi.getCategory());

    }

    @Override
    public void printRewards(int q, ArrayList<Customer> costumers, ArrayList<Restaurant> restaurants) {
        restaurants.get(q-1).printRewards(q,restaurants,costumers);
    }
    public void printinline2(int id, Restaurant res, ArrayList<FoodItem> fi) {
        System.out.println(fi.get(id - 1).getId() + " " + res.getName() + " " + "-" + " " + fi.get(id - 1).getName() + " " + fi.get(id - 1).getPrice() + " " + fi.get(id - 1).getQuantity() +" "+ fi.get(id - 1).getOffer() + "% off" + " " + fi.get(id - 1).getCategory());
    }

    public void edit(int q, ArrayList<Restaurant> restaurants) {
        System.out.println("Choose item by code");
        for (int i = 0; i < getFi().size(); i++) {
            printinline2(i + 1, restaurants.get(q - 1), getFi());
        }
        int id = sc.nextInt();
        System.out.println("Choose Attribute to edit:");
        System.out.println("1) Name");
        System.out.println("2) Price");
        System.out.println("3) Quantity");
        System.out.println("4) Category");
        System.out.println("5) Offer");
        int e = sc.nextInt();
        if (e == 1) {
            System.out.println("Enter new name: ");
            getFi().get(id-1).setName(sc.next());
            printinline2(id, restaurants.get(q - 1), getFi());
        } else if (e == 2) {
            System.out.println("Enter new price: ");
            getFi().get(id-1).setPrice(sc.nextInt());
            printinline2(id, restaurants.get(q - 1), getFi());
        } else if (e == 3) {
            System.out.println("Enter new quantity: ");
            getFi().get(id-1).setQuantity(sc.nextInt());
            printinline2(id, restaurants.get(q - 1), getFi());
        } else if (e == 4) {
            System.out.println("Enter new category: ");
            getFi().get(id-1).setCategory(sc.next());
            printinline2(id, restaurants.get(q - 1), getFi());
        } else if (e == 5) {
            System.out.println("Enter new offer: ");
            getFi().get(id-1).setOffer(sc.nextInt());
            printinline2(id, restaurants.get(q - 1), getFi());
        }


    }
}

class FoodItem {
    private String name;
    private final int id;
    private int price;
    private int quantity;
    private String category;
    private int offer;
    ArrayList<FoodItem> fi=new ArrayList<>();
    FoodItem(int id) {
        this.id = id;
    }
    public String getCategory() {
        return category;
    }
    public int getId() {
        return id;
    }
    public int getQuantity() {
        return quantity;
    }
    public String getName() {
        return name;
    }
    public int getOffer() {
        return offer;
    }
    public int getPrice() {
        return price;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public void setOffer(int offer) {
        this.offer = offer;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public void setPrice(int price) {
        this.price = price;
    }
}

class Main {
    public static void main(String[] args) {
        Menu appl = new Menu();
        Customer c1 = new Customer("Ram", "Elite", "Delhi", 0);
        appl.customers.add(c1);
        Customer c2 = new Customer("Sam", "Elite", "Mumbai", 0);
        appl.customers.add(c2);
        Customer c3 = new Customer("Tim", "Special","Bangalore" , 20);
        appl.customers.add(c3);
        Customer c4 = new Customer("Kim", "", "Hyderabad", 40);
        appl.customers.add(c4);
        Customer c5 = new Customer("Jim", "", "Delhi", 40);
        appl.customers.add(c5);
        Restaurant r1 = new Restaurant("Shah", "Authentic", "Delhi");
        appl.restaurants.add(r1);
        Restaurant r2 = new Restaurant("Ravi's", "","Mumbai" );
        appl.restaurants.add(r2);
        Restaurant r3 = new Restaurant("The Chinese", "Authentic", "Hyderabad");
        appl.restaurants.add(r3);
        Restaurant r4 = new Restaurant("Wang's", "Fast Food", "Pune");
        appl.restaurants.add(r4);
        Restaurant r5 = new Restaurant("Paradise", "", "Delhi");
        appl.restaurants.add(r5);
        appl.printMenu(appl.customers,appl.restaurants,true);
    }
}

