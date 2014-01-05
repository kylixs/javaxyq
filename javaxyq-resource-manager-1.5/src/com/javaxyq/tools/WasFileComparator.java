package com.javaxyq.tools;

import java.util.Comparator;

import com.javaxyq.tools.WdfFileNode;

/**
 * 汉字拼音排序比较器
 */
public class WasFileComparator implements Comparator {

    /**
     * 比较类型
     */
    private static int sortType;

    public static final int SORT_ID = 0;

    public static final int SORT_NAME = 1;

    public static final int SORT_SIZE = 2;

    private static WasFileComparator instance = null;

    private WasFileComparator() {

    }

    /**
     * 设置排序的关键字<br>
     * SORT_ID id<BR>
     * SORT_SIZE 大小<BR>
     * SORT_NAME 名字<BR>
     * 
     * @param type
     */
    public static void setSortType(int type) {
        sortType = type;
    }

    public static WasFileComparator getInstance() {
        if (instance == null)
            instance = new WasFileComparator();
        return instance;
    }

    /**
     * WASFileNode 比较函数,根据不同的比较类型返回不同的值
     */
    public int compare(Object o1, Object o2) {
        try {
            WdfFileNode file1 = (WdfFileNode) o1;
            WdfFileNode file2 = (WdfFileNode) o2;
            switch (sortType) {
            case SORT_ID:
                return file1.getId() > file2.getId() ? 1
                        : (file1.getId() == file2.getId() ? 0 : -1);
            case SORT_SIZE:
                return file1.getSize() > file2.getSize() ? 1
                        : (file1.getSize() == file2.getSize() ? 0 : -1);
            case SORT_NAME:
                if (file1.getName() == null && file2.getName() != null)
                    return 1;
                else if (file1.getName() != null && file2.getName() == null)
                    return -1;
                else if (file1.getName() == null && file2.getName() == null)
                    return file1.getId() > file2.getId() ? 1 : (file1.getId() == file2.getId() ? 0
                            : -1);
                // 取得比较对象的汉字编码，并将其转换成字符串
                String s1 = new String(file1.getName().getBytes("GB2312"), "ISO-8859-1");
                String s2 = new String(file2.getName().getBytes("GB2312"), "ISO-8859-1");
                // 运用String类的 compareTo（）方法对两对象进行比较
                return s1.compareTo(s2);
            }
        } catch (Exception e) {
        }
        return 0;
    }

}
