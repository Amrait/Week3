package Week3;

import java.util.concurrent.CyclicBarrier;

/**
 * Created by Олексій on 08.02.2017.
 */
public class Task3 {
    public static void main(String[] args) {
        new Task3().DoTask3();

    }
    public void DoTask3() {
        final CyclicBarrier global = new CyclicBarrier(2,new Thread(){
            public void run() {
                try
                {
                    System.out.println("Розпродаж розпочався!");
                    Thread.sleep(1000);
                    System.out.println("Розпродаж завершено.");
                }
                catch (InterruptedException e){}
            }
        });
        final CyclicBarrier forCars = new CyclicBarrier(5, new Thread() {
            public void run() {
                try {
                    global.await();
                }
                catch (Exception e){}
            }
        });
        final CyclicBarrier forCustomers = new CyclicBarrier(10, new Thread() {
            public void run() {
                try {
                    global.await();
                }
                catch (Exception e){}
            }
        });
        Thread first = new Thread(){
            public void run(){
                for (int i = 0; i < 10; i++) {
                    new Thread(new Car(forCars)).start();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
                }
            }
        };
        first.start();
        Thread second = new Thread(){
            public void run(){
                for (int i = 0; i < 20; i++) {
                    new Thread(new Customer(forCustomers)).start();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
                }
            }
        };
        second.start();
    }
    public class Car implements Runnable {
        CyclicBarrier Checker;

        public Car(CyclicBarrier checker) {
            this.Checker = checker;
        }

        @Override
        public void run() {
            try {
                System.out.println("Машина прибула до автосалону.");
                this.Checker.await();
            } catch (Exception e) {
            }
        }
    }
    public class Customer implements Runnable {
        CyclicBarrier Checker;

        public Customer(CyclicBarrier checker) {
            this.Checker = checker;
        }

        @Override
        public void run() {
            try {
                System.out.println("Покупець прибув до автосалону.");
                this.Checker.await();
            } catch (Exception e) {
            }
        }
    }
}
