package Week3;


import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Олексій on 06.02.2017.
 */
public class Task1 {
    private static volatile CyclicBarrier cyclicBarrier = new CyclicBarrier(2);

    public static void main(String[] args) {
        Task1 homeWork = new Task1();
        homeWork.DoTask1();
    }
    public void DoTask1() {
        final CountDownLatch checker = new CountDownLatch(2);
        try {
            Thread first = new Thread() {
                public void run() {
                    DoThreadThing(1);
                }
            };
            first.start();
            first.join();
            Thread fourth = new Thread() {
                public void run() {
                    try {
                        checker.await();
                        DoThreadThing(4);
                    } catch (InterruptedException e) {
                    }

                }
            };
            fourth.start();
            for (int i = 2; i < 4; i++) {
                new Thread(new InnerThread(checker, cyclicBarrier, i)).start();
            }
        } catch (Exception e) {
        }
    }
    public void DoThreadThing(int threadNumber) {
        System.out.printf("Потік №%d розпочав роботу...\n", threadNumber);
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(0, 1501));
            System.out.printf("Потік №%d завершив роботу!\n", threadNumber);
        } catch (InterruptedException e) {

        }
    }
    public class InnerThread implements Runnable {
        private CountDownLatch countDownLatch;
        private CyclicBarrier cyclicBarrier;
        private int number;

        public InnerThread(CountDownLatch cdl, CyclicBarrier cb, int n) {
            this.countDownLatch = cdl;
            this.cyclicBarrier = cb;
            this.number = n;
        }

        @Override
        public void run() {
            try {
                this.cyclicBarrier.await();
                DoThreadThing(this.number);
                this.countDownLatch.countDown();
            } catch (Exception e) {

            }
        }
    }

}
