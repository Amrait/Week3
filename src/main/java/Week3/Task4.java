package Week3;

import sun.security.krb5.internal.TGSRep;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Олексій on 08.02.2017.
 */
public class Task4 {
    public static void main(String[] args) {
        Task4 t4 = new Task4();
        t4.DoTask4();
    }
    public void DoTask4(){
        final Shop shop = new Shop();
        shop.start();
        final CyclicBarrier customerChecker = new CyclicBarrier(4,new Service(shop));
        final Semaphore semaphore = new Semaphore(5,true);
        Thread customersThread = new Thread(){
            public void run(){
                for (int i = 0; i < 20; i++) {
                    new Customer(customerChecker,semaphore,shop,i).start();
                    try {
                        Thread.sleep(200);
                    }
                    catch (InterruptedException e){}
                }
            }
        };
        customersThread.start();
    }
    public static class Shop extends Thread{
        private boolean Active;
        private int Customers;
        private boolean[] capacity;
        public Shop(){
            Active=false;
            Customers=0;
            capacity = new boolean[5];
        }
        public boolean isActive(){
            return this.Active;
        }
        public void setActive(boolean value)
        {
            this.Active = value;
        }
        @Override
        public void run() {
            try{
                while(true)
                {
                    if(Customers>3)
                    {
                        Active = true;
                    }
                    else {
                        Active = false;
                        System.out.println("Магазин на разі зачинено...");
                        Thread.sleep(10000);
                    }
                }
            }
            catch (Exception e){}
        }
    }
    public class Customer extends Thread{
        private CyclicBarrier Checker;
        private Semaphore Access;
        private Shop shop;
        private int Number;
        private boolean FirstAttempt;
        public Customer(CyclicBarrier cyclicBarrier, Semaphore semaphore,
                        Shop Father, int number){
            this.Checker = cyclicBarrier;
            this.Access = semaphore;
            this.Number = number;
            this.shop = Father;
            this.FirstAttempt = true;
        }
        public void run(){
            this.shop.Customers++;
            try{
                System.out.println("Thread " + this.Number + " sends " + shop.isActive());
                while(true){
                    System.out.println("Thread " + this.Number);
                    if(shop.isActive())
                    {
                        this.Access.acquire();
                        int a = -1;
                            for (int i = 0; i < 5; i++) {
                                if(shop.capacity[i]==false)
                                {
                                    shop.capacity[i]=true;
                                    a = i;
                                    System.out.printf("Покупець %d зайшов в магазин...\n",this.Number);
                                    Thread.sleep(ThreadLocalRandom.current().nextInt(1000,8001));
                                    System.out.printf("Покупець %d вийшов з магазину.\n",this.Number);
                                    this.shop.Customers--;
                                    break;
                                }
                            }
                            shop.capacity[a]=false;
                        this.Access.release();
                        Thread.currentThread().interrupt();
                    }
                    else {
                        if (FirstAttempt){
                            FirstAttempt=false;
                            this.Checker.await();
                        }
                    }
                }
            }
            catch (Exception e){}
        }
    }
    public static class Service implements Runnable{
        public Shop thread;
        public Service(){}
        public Service(Shop value){
            thread = value;
        }
        public void run(){
            thread.Active = true;
            System.out.println("Покупці погнали в магазин...");
        }
    }
}
