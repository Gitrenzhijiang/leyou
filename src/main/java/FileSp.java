import java.io.*;

public class FileSp {
    public static void main(String[] args) throws InterruptedException {
        File file = new File("D://big.txt");
        for (int i = 1; i <= 4; i++){
            Thread t1 = new Thread(new MyRunner(file, i));
            t1.start();
//            t1.join();
        }

    }

    /**
     * 这个线程将给入的文件  截取指定部分, 然后分成两部分.
     * 目标文件默认存储D盘,
     */
    static class MyRunner implements  Runnable {
        /**
         *
         * @param file 目标文件
         * @param index 第几部分 [1 - 4]
         */
        public MyRunner(File file, int index) {
            this.target = file;
            this.index = index;
        }
        private int index;
        private final File target;
        @Override
        public void run() {
            RandomAccessFile randomAccessFile = null;
            File d1 = new File("D://" + index + "_sp01");
            File d2 = new File("D://" + index + "_sp02");

            BufferedOutputStream fw1 = null;
            BufferedOutputStream fw2 = null;
            try {
                if (!d1.exists()) {
                    d1.createNewFile();
                }
                if (!d2.exists()) {
                    d2.createNewFile();
                }
                fw1 = new BufferedOutputStream(new FileOutputStream(d1));
                fw2 = new BufferedOutputStream(new FileOutputStream(d2));

                randomAccessFile = new RandomAccessFile(target, "r");
                final long total = randomAccessFile.length();
                // 1002 / 4 = 250 余2
                // 0 - 249   index=1
                // 250 -     index=2
                long p = total / 4;
                long start = p * (index - 1);
                long end = index == 4? total - 1 : start + (p - 1);
                System.out.println("start=" + start + ", " + end + "");

                randomAccessFile.seek(start);
                long mid = start + (end - start) / 2;
                byte[] buff = new byte[2];
                while (randomAccessFile.getFilePointer() <= end) {
                    int len = randomAccessFile.read(buff);
                    if (randomAccessFile.getFilePointer() <= mid) {
                        fw1.write(buff, 0, len);
                    } else {
                        fw2.write(buff, 0, len);
                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    fw1.close();
                    fw2.close();
                } catch (IOException e) {
                }
            }
        }
    }
}
