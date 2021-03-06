// Thid test tries to expose C at : benchmarks/instrumented/java17/util/Vector.java#411
// with R at : benchmarks/instrumented/java17/util/Vector.java#269
// accessing fied: [I1, elementData]
import java.util.*;
import java.io.*; 
import intruder.util.*;
public class TestDriver2{ 

    public static List<List<Parameter>> col0;
    public static List<List<Parameter>> col1;
    public static List<Parameter> racyParameters0;
    public static List<Parameter> racyParameters1;

    /* Impose the constraints on the collected objects*/
    public static void imposeConstraint() { 

        Parameter racy = racyParameters0.get(1);
        racyParameters1.set(0, racy);
    }

    /*
    * Test for the race.
     */

    public static void main(String args[]) { 

        Initializer.initialize(2);


        /* Objects collected from run : 1
         Invocation of method : removeAll
        Line : 57 in VectorTest*/
        racyParameters0 = Initializer.getRacyObjects(0);	

        /* Objects collected from run : 2
         Invocation of method : addAll
        Line : 27 in VectorTest*/
        racyParameters1 = Initializer.getRacyObjects(1);	


        imposeConstraint();


        Thread t0 = new Thread0(racyParameters0);
        Thread t1 = new Thread1(racyParameters1);

        t0.start();
        t1.start();

        try { 
            t0.join();
            t1.join();
        }
        catch(Exception e) { }
    }
}

class Thread0 extends Thread {
    List<Parameter> parameters;

    public Thread0(List<Parameter> paramList) {
        this.parameters = paramList;
    }
    public void run() {
        List<Parameter> paramList = parameters;
        benchmarks.instrumented.java17.util.Vector par1 = (benchmarks.instrumented.java17.util.Vector)paramList.get(0).returnStored();
        benchmarks.instrumented.java17.util.Collection par2 = (benchmarks.instrumented.java17.util.Collection)paramList.get(1).returnStored();
        try {
             Thread.sleep(0* 10 * 1000);
             // Invocation leading to atomicity violation
            par1.removeAll(par2);
        } catch (Exception e) {            System.out.println("TestDriver Error");
            System.err.println("TestDriver Error");
            e.printStackTrace();
        }
    } 
}


class Thread1 extends Thread {
    List<Parameter> parameters;

    public Thread1(List<Parameter> paramList) {
        this.parameters = paramList;
    }
    public void run() {
        List<Parameter> paramList = parameters;
        benchmarks.instrumented.java17.util.Vector par1 = (benchmarks.instrumented.java17.util.Vector)paramList.get(0).returnStored();
        benchmarks.instrumented.java17.util.Collection par2 = (benchmarks.instrumented.java17.util.Collection)paramList.get(1).returnStored();
        try {
             Thread.sleep(1* 10 * 1000);
             // Invocation leading to atomicity violation
            par1.addAll(par2);
        } catch (Exception e) {            System.out.println("TestDriver Error");
            System.err.println("TestDriver Error");
            e.printStackTrace();
        }
    } 
}

