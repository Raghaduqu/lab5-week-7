
public class Deadlock {
    static class Friend {
        private final String name;
        public Friend(String name) {
            this.name = name;
        }
        public String getName() {
            return this.name;
        }
        /**
          * locks aquired: this + bower, ordered alphabeticaly by name
          * deadlock happend because thread1 holds alphonse waits for gaston,
          * thread2 holds gaston waits for alphonse -- neither moves foward
          * fix: always grab the alphabeticaly smaller friend first, breaks the cycle 
         */
        public void bow(Friend bower) {
   
            Friend first, second;
            if (this.name.compareTo(bower.name) <= 0) {
                first  = this;
               second = bower;
             } else {
                first  = bower;
                second = this;
                   }

            synchronized (first) {
                synchronized (second) {
                    System.out.format("%s: %s has bowed to me!%n",
                            this.name, bower.getName());
                    bower.bowBack(this);
                }
            }
        }
        /**
        *no locks aquired here -- already inside the 2 locks held by bow()    
        * Deadlock (original): Thread 1 couldn't enter gaston.bowBack() because
        *and thread2 couldnt get alphonse either, so both just stuck
        *fix: removed synchronized, the ordering in bow() handles it
         */
        public void bowBack(Friend bower) {
            System.out.format("%s: %s has bowed back to me!%n",
                    this.name, bower.getName());
        }
    }

    public static void main(String[] args) {
        final Friend alphonse =
            new Friend("Alphonse");
        final Friend gaston =
            new Friend("Gaston");
        new Thread(new Runnable() {
            public void run() { alphonse.bow(gaston); }
        }).start();
        new Thread(new Runnable() {
            public void run() { gaston.bow(alphonse); }
        }).start();
    }
}


