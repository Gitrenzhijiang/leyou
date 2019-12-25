import lombok.val;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class FileSort {
    public static void main2(String[] args) {
        File file = new File("D://big.txt");
        split(file);
    }
    public static void main(String[] args) throws IOException {
        File file = new File("D://big.txt");

        // 如何对big.txt排序
        /*
        将big.txt, 拆分成小文件, 每一个文件1000行, 总共10000个小文件



        对这 1000 个数字排序, 存入相应文件.

        构建一个 10000 的堆, 每次读取最小元素, 然后最后一个

         生成一个 结果文件, 每次将最小堆中的最小元素写入结果文件.
         result.txt,
         */
        List<File> smallFileList = split(file);

        File targetFile = new File("D://test/target.txt");


        MinHeap<InnerStruct> minHeap = new MinHeap(smallFileList.size());


        // 1. 对每个小文件排序
        smallFileList.forEach((e) -> {sortFile(e);});
        // 2. 拿到每个小文件的读入流
        List<BufferedReader> bList = smallFileList.stream().map(e -> {
            try {
                return new BufferedReader(new FileReader(e));
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
            return null;
        }).collect(Collectors.toList());
        // 每个小文件第一行数字写入最小索引堆
        for (int i = 0; i < smallFileList.size(); i++) {

            BufferedReader br = bList.get(i);
            minHeap.insert(new InnerStruct(i, Integer.parseInt(br.readLine())));
        }

        BufferedWriter targetBw = new BufferedWriter(new FileWriter(targetFile));
        while (!minHeap.isEmpty()) {
            // 第几个文件当前最小
            InnerStruct struct = minHeap.extraction();
            targetBw.write(struct.data + "");
            targetBw.write(LINE_SEPARATOR);
            // 从获取最小元素文件, 在获取一次
            BufferedReader tempBr = bList.get(struct.i);
            String nextLine = tempBr.readLine();
            if (nextLine != null) {
                int willInsert = Integer.parseInt(nextLine);
                minHeap.insert(new InnerStruct(struct.i, willInsert));
            }

        }

        // 释放资源
        for (BufferedReader bufferedReader : bList) {
            if (bufferedReader != null){
                bufferedReader.close();
            }
        }
        targetBw.close();
    }
    static class InnerStruct implements Comparable<InnerStruct> {
        int i;
        Integer data;

        public InnerStruct(int i, Integer data) {
            this.i = i;
            this.data = data;
        }

        @Override
        public int compareTo(InnerStruct o) {
            return this.data - o.data;
        }
    }

    private static void sortFile(File file) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(file.getAbsolutePath()));
            List<Integer> nums = lines.stream().map(Integer::parseInt).collect(Collectors.toList());
            Integer[] cnum = new Integer[nums.size()];
            for (int i = 0; i < nums.size(); i++) {
                cnum[i] = nums.get(i);
            }
            Arrays.sort(cnum);
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            for (int i = 0; i < cnum.length; i++) {
                bw.write(cnum [i].intValue() + "");
                if (i != cnum.length - 1) {
                    bw.write(LINE_SEPARATOR);
                }
            }

            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    /**
     * 将file文件拆分成多个小文件, 每一个小文件是1000行
     * @param file
     */
    private static List<File> split(File file) {
        final int numOfFile = 1000;// 每一个文件1000行
        int tempNum = numOfFile;
        // 当前文件的读入
        BufferedReader br = null;
        // 小文件的写入
        BufferedWriter bw = null;
        long fileId = 1;
        List<File> fileList = new ArrayList<>();
        File tempFile = new File("D://test/" + fileId);
        fileList.add(tempFile);
        try {
            bw = new BufferedWriter(new FileWriter(tempFile));
            br = new BufferedReader(new FileReader(file));
            String line = null;
            while ((line = br.readLine()) != null) {
                if (tempNum == 0) {
                    fileId++;
                    bw.close();

                    tempFile = new File("D://test/" + fileId);
                    fileList.add(tempFile);
                    bw = new BufferedWriter(new FileWriter(tempFile));
                    tempNum = numOfFile;
                }
                tempNum--;
                bw.write(line);
                if (tempNum != 0) {
                    bw.write(LINE_SEPARATOR);
                }
            }
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileList;
    }

    private static void init(File file) {
        final long count = 1000000; //
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(file));
            for (int i = 1; i <= count; i++) {
                bw.write( ThreadLocalRandom.current().nextInt((int) count * 2 ) + LINE_SEPARATOR);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
