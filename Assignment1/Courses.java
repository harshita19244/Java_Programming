package Covid;
import java.util.ArrayList;
import java.util.Scanner;

class Patients {
    static class Health {
        private String name;
        private String status;
        private int beds;
        private float tc;
        private float oc;
        private ArrayList<Patients> patients = new ArrayList<Patients>();

        public Health(String name, float tc, int oc, int beds, String status) {
            this.name = name;
            this.beds = beds;
            this.tc = tc;
            this.oc = oc;
            this.status = status;
        }

    }

    private String name;
    private int age;
    private int oxygen;
    private float temp;
    private int id;
    private String status = "not admitted";
    private String hospital;
    private int recovery;


    public Patients() {
        name = "x";

    }

    public Patients(String name, float temp, int oxygen, int age, int id) {
        this.name = name;
        this.temp = temp;
        this.oxygen = oxygen;
        this.age = age;
        this.id = id;

    }

    public void recoverDisplay(int recovery, int id) {
        System.out.println("Recovery days for admitted patient ID" + " " + id + "-" + recovery);
    }

    public int add(Health hospital, ArrayList<Patients> pat) {
        System.out.println(hospital.name);
        System.out.println("Temperature should be less <=" + hospital.tc); //102
        System.out.println("Oxygen levels should be >=" + hospital.oc);  //92
        System.out.println("Number of Available beds –" + hospital.beds);
        if (hospital.beds != 0) {
            System.out.println("Admission Status – OPEN");
        } else {
            System.out.println("Admission Status – CLOSED");
        }
        int x = hospital.beds;
        boolean flag = true;

        for (int i = 0; i < pat.size(); i++) {
            if (pat.get(i).oxygen >= hospital.oc && flag && pat.get(i).status.equals("not admitted")) {
                hospital.patients.add(pat.get(i));
                pat.get(i).status = "admitted";
                pat.get(i).hospital = hospital.name;
                //System.out.println(pat.get(i).id);

                hospital.beds--;
                if (hospital.beds == 0) {
                    flag = false;
                    break;
                }
            }
        }
        if (flag) {
            for (int i = 0; i < pat.size(); i++) {
                if (!hospital.patients.contains(pat.get(i))) {
                    // System.out.println(pat.get(i).temp);
                    if (pat.get(i).temp <= hospital.tc && flag && pat.get(i).status.equals("not admitted")) {
                        hospital.patients.add(pat.get(i));
                        pat.get(i).status = "admitted";
                        pat.get(i).hospital = hospital.name;
                        //System.out.println(pat.get(i).id);
                        hospital.beds--;
                        if (hospital.beds == 0) {
                            flag = false;
                            break;
                        }
                    }
                }

            }
        }

        if (hospital.beds == 0) {
            hospital.status = "CLOSED";
        }


        return hospital.patients.size();

    }

    public void display(ArrayList<Patients> pat) {
        for (Patients x : pat) {
            System.out.println(x.id + " " + x.name);

        }
    }

    public int calculate(ArrayList<Health> health, ArrayList<Patients> pat, boolean flag){
        int count=0;
        if(flag) {
            for (Health value : health) {
                count += value.patients.size();
            }
            count=pat.size()-count;
            if (health.size() != 0) {
                //System.out.println(count + " patients");
                return count;
            } else {
                return pat.size();
            }
        }
        else{
            return pat.size();
        }


    }

    public static void main(String args[]) {
        Scanner sc = new Scanner(System.in);
        int no = sc.nextInt();
        Patients p = new Patients();
        int id = 0;

        ArrayList<Patients> pat = new ArrayList<>();
        ArrayList<Health> health = new ArrayList<>();

        for (int i = 0; i < no; i++) {
            String name = sc.next();
            float temp = sc.nextFloat();
            int oxygen = sc.nextInt();
            int age = sc.nextInt();
            id++;
            Patients pat1 = new Patients(name, temp, oxygen, age, id);
            if (!pat.contains(pat1)) {
                pat.add(pat1);
            }

        }

        boolean FLAG=true;
        int q=0;
        for(int i=0;i< Integer.MAX_VALUE;i++){
            int c=p.calculate(health,pat,FLAG);
            if(c!=0){
                q=sc.nextInt();

                if (q == 1) {
                    FLAG=true;
                    String ins = sc.next();
                    int t = sc.nextInt();
                    int o = sc.nextInt();
                    int beds = sc.nextInt();
                    Health hosp = new Health(ins, t, o, beds, "OPEN");
                    if (!health.contains(hosp)) {
                        health.add(hosp);
                        //System.out.println("Yes");
                    }
                    int c1 = p.add(hosp, pat);
                    System.out.println(c1+ " patients were admitted");
                    for (int i1 = 0; i1 < c1; i1++) {
                        int s = sc.nextInt();
                        pat.get(i1).recovery = s;
                        p.recoverDisplay(s, hosp.patients.get(i1).id);
                    }


                } else if (q == 2) {
                    System.out.println("Account ID removed of admitted patients");
                    for (Health value : health) {
                        for (int i1 = 0; i1 < value.patients.size(); i1++) {
                            Health n = value;
                            if (n.patients.get(i1).status.equals("admitted")) {
                                System.out.println(n.patients.get(i1).id);
                                int x = n.patients.get(i1).id;
                                pat.removeIf(e -> e.id == x);

                            }
                        }
                    }
                    //System.out.println(pat.size());
                    FLAG=false;


                } else if (q == 3) {
                    System.out.println("Accounts removed of Institute whose admission is closed");
                    if(health.size()!=0) {
                        for (int b=0;b<health.size();b++) {
                            if (health.get(b).status.equals("CLOSED")) {
                                System.out.println(health.get(b).name);

                                health.remove(health.get(b));
                            }

                        }
                    }
                    //System.out.println(health.size());

                } else if (q == 4) {
                    int v=p.calculate(health,pat,FLAG);
                    System.out.println(v+ " patients");


                } else if (q == 5) {
                    int count = 0;
                    for (Health value : health) {
                        if (value.status.equals("OPEN")) {
                            count++;
                        }
                    }

                    System.out.println(count + " " + "institute/institutes are admitting patients currently");


                } else if (q == 6) {
                    String institute = sc.next();
                    boolean flag = false;
                    for (Health value : health) {
                        if (value.name.equals(institute)) {
                            flag = true;
                            System.out.println(value.name);
                            System.out.println("Temperature should be <= " + value.tc);
                            System.out.println("Oxygen levels should be >= " + value.oc);
                            System.out.println("Number of Available beds –" + value.beds);
                            System.out.println("Admission Status – " + value.status);

                        }

                    }

                    if (!flag) {
                        System.out.println("Health Institute could not be found in records");
                    }


                } else if (q == 7) {
                    int ID = sc.nextInt();
                    boolean flag = false;
                    for (Patients patients : pat) {
                        if (patients.id == ID) {
                            flag = true;
                            System.out.println(patients.name);
                            System.out.println("Temperature is " + patients.temp);
                            System.out.println("Oxygen levels is " + patients.oxygen);
                            System.out.println("Admission Status - " + patients.status);
                            if (patients.status.equals("admitted")) {
                                System.out.println("Admitting Institute - " + patients.hospital);
                            }
                        }
                    }

                    if (!flag) {
                        System.out.println("Patient with ID " + ID + " does not exist");
                    }


                } else if (q == 8) {
                    p.display(pat);


                } else if (q == 9) {
                    String institute = sc.next();
                    boolean flag = false;
                    for (Health value : health) {
                        if (value.name.equals(institute)) {
                            flag = true;
                            for (int k = 0; k < value.patients.size(); k++) {
                                System.out.println(value.patients.get(k).name + ", recovery time is  " + value.patients.get(k).recovery + " days");
                            }
                        }
                    }
                    if (!flag) {
                        System.out.println("Health Institute could not be found in records");
                    }


                }
            }
            else{
                System.out.println("All patients admitted!");
                break;
            }
        }
    }
}




