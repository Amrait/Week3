package Week3;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Олексій on 09.02.2017.
 */
public class Task5 {
    public static void main(String[] args) {
        Task5 task5 = new Task5();
        task5.DoTask5();
    }
    public void DoTask5(){
        boolean isRain = false;
        final Match football = new Match(isRain);
        football.start();
        final CyclicBarrier playerCount = new CyclicBarrier(10,new Task5.Service(football,isRain));
        Thread playersThread = new Thread(){
            public void run(){
                int number = 1;
                while (true){
                    if(!football.IsGoing){
                        new Thread(new Player(playerCount,number)).start();
                        number++;
                        try{
                            Thread.sleep(300);
                        }
                        catch(InterruptedException e){}
                    }
                    else {
                        try{
                            Thread.sleep(10200);
                            new Thread(new Player(playerCount,number)).start();
                            number++;
                                Thread.sleep(300);
                        }
                        catch(InterruptedException e){}
                    }
                }
            }
        };
        playersThread.start();


    }
    public class Match extends Thread{
        private volatile boolean IsGoing;
        private volatile boolean isRain;
        public Match(boolean forecast){
            this.IsGoing = false;
            this.isRain = forecast;
        }
        @Override
        public void run() {
            try{
                while (true){
                    if (this.IsGoing)
                    {
                        this.isRain = ThreadLocalRandom.current().nextBoolean();
                        if (!this.isRain){
                            System.out.println("Дощу немає, Футбольний матч розпочато.");
                            Thread.sleep(200);
                            System.out.println("Футбольний матч завершено, чекаємо наступного понеділка.");
                            Thread.sleep(10000);
                            this.IsGoing = false;
                        }
                        else{
                            System.out.println("Бодай йому, йде дощ. Матч переноситься на наступний понеділок.");
                            Thread.sleep(10200);
                            this.IsGoing = false;
                        }
                    }
                }
            }
            catch(InterruptedException e){

            }
        }
    }
    public class Player implements Runnable{
        private CyclicBarrier waitStart;
        private int Number;
        public Player(CyclicBarrier cyclicBarrierS, int number){
            this.waitStart = cyclicBarrierS;
            this.Number = number;
        }
        @Override
        public void run() {
            try{
                System.out.printf("Гравець №%d прибув на гру.\n",this.Number);
                this.waitStart.await();
                Thread.sleep(250);
                System.out.printf("Гравець №%d йде.\n",this.Number);
            }
            catch (Exception e){}
        }
    }
    public static class Service implements Runnable{
        public Match thread;
        public Service(){}
        public Service(Match value, boolean isRain){
            thread = value;
            isRain = ThreadLocalRandom.current().nextBoolean();
        }
        public void run(){
            thread.IsGoing = true;
        }
    }
}
