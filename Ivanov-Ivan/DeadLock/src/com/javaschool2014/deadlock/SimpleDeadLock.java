package com.javaschool2014.deadlock;

public class SimpleDeadLock {

    static class LockedObject {

        private final String name;

        public LockedObject(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

        public synchronized void ping(LockedObject caller) {

            System.out.println(this.name + " 1 " + caller.getName());

            try {

                this.wait();
                caller.notify();

            } catch (InterruptedException e) {

                System.out.println(e.getMessage());

            }

            caller.pingBack(this);

        }

        public synchronized void pingBack(LockedObject caller) {

            System.out.println(this.name + " 2 " + caller.getName());

        }

    }

    public static void main(String[] args) {

        final LockedObject one = new LockedObject("One");
        final LockedObject two = new LockedObject("Two");

            new Thread(new Runnable() {

                public void run() {
                    one.ping(two);
                }

            }).start();

            new Thread(new Runnable() {

                public void run() {
                    two.ping(one);
                }

            }).start();

    }

}
