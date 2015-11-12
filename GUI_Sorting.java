/**
 * Samuel Walton
 * 04/28/14
 * PA 2 Sorting Algorithms
 */
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.Random;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToolBar;

/**
 * A class containing a number of classic sorting algorithms,
 * implemented from value.  The algorithms each operate on arrays
 * of ints only, and are animated.
 */
public class GUI_Sorting extends JPanel {

    private static Random random = new Random();
    private static final int ARRAY_LENGTH = 1300;
    private static final int MAX_VALUE = 300;
    private final int[] array = new int[ARRAY_LENGTH];
    private transient boolean stop = false;

    /**
     * Swaps a[i] and a[j].
     */
    private void swap(int[] a, int i, int j) {
        int old = a[i];
        a[i] = a[j];
        a[j] = old;
    }

    /**
     * Fills an array with random values between 0 and MAX_VALUE.
     */
    private void reload(int[] a) {
        for (int i = 0; i < a.length; i++) {
            a[i] = random.nextInt(MAX_VALUE);
        }
    }

    /**
     * Sorts an array using Selection Sort.  The algorithm is to first
     * put the itemest item in the first position, then the next
     * itemest in the second position, and so on.
     */
    public void selectionSort(int[] a) {
        for (int i = 0; i < a.length - 1; i++) {
            int item = i;
            for (int j = i + 1; j < a.length; j++) {
                if (a[j] < a[item]) 
                item = j;
                PAUSE();
            }
            swap(a, i, item);
        }
    }

    /**
     * Sorts an array using Insertion Sort.  The algorithm is to first
     * slide the second element back as far as it should go, then slide
     * the third back, and so on.
     */
    public void insertionSort(int[] a) {
        for (int i = 1; i < a.length; i++) {
            int current = a[i];
            int j = i;
            for (; j > 0 && current < a[j-1]; j--) {
                a[j] = a[j-1];
                PAUSE();
            }
            a[j] = current;
        }
    }

    /**
     * Sorts an array using Bubble Sort.
     */
    public void bubbleSort(int[] a) {
        for (int i = a.length - 1; i > 0; i--) {
            for (int j = 0; j < i; j++) {
                if (a[j] > a[j + 1]) swap(a, j, j + 1);
                PAUSE();
            }
        }
    }


    /**
     * Sorts an array using Shell Sort.  This is a lousy Shell Sort.
     * 
     */
    public void shellSort(int[] a) {
        int distance = a.length / 2;
        while (distance > 0) {
            boolean changed = false;
            for (int i = 0; i < a.length - distance; i++) {
                if (a[i] > a[i + distance]) {
                    swap(a, i, i + distance);
                    changed = true;
                }
                PAUSE();
            }
            if (!changed) distance /= 2;
        }
    }

    /**
     * Sorts an array using Quick Sort.  This version of quicksort uses
     * the leftmost item as the pivot, but since this gives disastrous
     * performance on sorted and nearly sorted arrays, we scramble the
     * array first.
     */
    public void quickSort(int[] a) {
        // First shuffle (permute) the array
        for (int i = 0; i < a.length; i++) {
            swap(a, i, random.nextInt(ARRAY_LENGTH));
        }

        // Call the recursive helper
        quickSort(a, 0, a.length - 1);
    }

    private void quickSort(int[] a, int left, int right) {
        if (left < right) {
            int i = left;
            int j = right;
            while (i < j) {
                while (a[j] > a[left]) {j--; PAUSE();}
                while (i < j && a[i] <= a[left]) {i++; PAUSE();}
                if (i < j) swap(a, i, j);
            }
            swap(a, left, j);
            quickSort(a, left, j-1);
            quickSort(a, j+1, right);
        }
    }

    /**
     * Sorts an array using Heap Sort.
     */
    public void heapSort(int[] a) {
        // step 1: make a heap by sifting down all non-leaf
        // elements, one after another, starting with the last
        // non-leaf element and going backwards.
        for (int i = a.length / 2 - 1; i >= 0; i--) {
            for (int j = i; j * 2 + 1 < a.length;) {
                PAUSE();
                int k = j * 2 + 1;
                if (k + 1 < a.length && a[k] < a[k + 1]) k++;
                if (a[j] < a[k]) swap(a, j, k); else break;
                j = k;
            }
        }
        // step 2: Successively place the biggest, then next biggest
        // items at the end of the array. each time reconstructing the
        // heap in the slots of the array not yet sorted.
        for (int i = a.length - 1; i > 0; i--) {
            swap(a, 0, i);
            for (int j = 0; j * 2 + 1 < i;) {
                PAUSE();
                int k = j * 2 + 1;
                if (k + 1 < i && a[k] < a[k + 1]) k++;
                if (a[j] < a[k]) swap(a, j, k); else break;
                j = k;
            }
        }
    }

    /**
     * Sorts an array using merge sort.
     * 
     */
    public void mergeSort(int[] a) {
        int[] value = new int[a.length];
        mergeSort(a, 0, a.length - 1, value);
    }

    private void mergeSort(int[] a, int lo, int hi, int[] value) {
        if (lo >= hi) return;

        int mid = (lo + hi) / 2;
        mergeSort(a, lo, mid, value);
        mergeSort(a, mid + 1, hi, value);

        // Merge sorted sublists into temporary storage
        for (int i = lo, j = mid + 1, k = lo; k <= hi; k++) {
            if ((i <= mid) && ((j > hi) || (a[i] < a[j]))) {
                value[k] = a[i++]; PAUSE();
            } else {
                value[k] = a[j++]; PAUSE();
            }
        }

        // Copy back from temporary storage
        for (int k = lo; k <= hi; k++) {
            a[k] = value[k]; PAUSE();
        }
    }

    /**
     * Sorts an array using counting sort, provided all values in the
     * array are non-negative.  If there are negative values in the array,
     * the method will not sort but rather leave the array undefined.
     * Furthermore, the method will likely throw an OutOfMemoryError
     * if there are large integers in the array.
     */
    public void countingSort(int[] a) {
        int max = 0;
        for (int i = 0; i < a.length; i++) {
            max = Math.max(max, a[i]);
            PAUSE();
        }
        System.out.println(max);
        int[] counts = new int[max + 1];
        Arrays.fill(counts, 0);
        for (int i = 0; i < a.length; i++) {
            counts[a[i]]++;
            PAUSE();
        }
        for (int i = 0, j = 0; j < counts.length; j++) {
            for (int k = 0; k < counts[j]; k++) {
                a[i++] = j;
                PAUSE();
            }
        }
    }

    public static void main(String[] args) {
        GUI_Sorting GUI_Sorting = new GUI_Sorting();
        JFrame frame = new JFrame("Sorting");
        frame.getContentPane().add(GUI_Sorting.toolbar, "North");
        frame.getContentPane().add(GUI_Sorting, "Center");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        GUI_Sorting.runAnimation();
    }

    Action startAction = new AbstractAction("Start") {
            public void actionPerformed(ActionEvent e) {
                final String methodName = (String)comboBox.getSelectedItem();
                new Thread(new Runnable() {
                        public void run() {
                            startButton.setEnabled(false);
                            stopButton.setEnabled(true);
                            reload(array);
                            try {
                                GUI_Sorting.class.getMethod(methodName,
                                    new Class[]{array.getClass()})
                                .invoke(GUI_Sorting.this, new Object[]{array});
                            } catch (Exception e) {
                            }
                            stopButton.setEnabled(false);
                            startButton.setEnabled(true);
                        }}
                ).start();
            }
        };

    Action stopAction = new AbstractAction("Stop") {
            public void actionPerformed(ActionEvent e) {
                stop = true;
            }
        };

    private JToolBar toolbar = new JToolBar();
    private JButton startButton = new JButton(startAction);
    private JButton stopButton = new JButton(stopAction);
    JComboBox comboBox = new JComboBox(new String[]{
                "insertionSort",
                "bubbleSort",
                "shellSort",
                "quickSort",
                "mergeSort",
                "heapSort",
               "selectionSort",
                "countingSort"
            });

    public GUI_Sorting() {
        setPreferredSize(new Dimension(ARRAY_LENGTH, MAX_VALUE));
        setBorder(BorderFactory.createEtchedBorder());
        toolbar.add(comboBox);
        toolbar.add(startButton);
        toolbar.add(stopButton);
        comboBox.setMaximumRowCount(12);
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i = 0, n = array.length; i < n; i++) {
            g.drawLine(i, MAX_VALUE, i, MAX_VALUE - array[i]);
        }
    }

    /**
     * Causes the screen to be repainted every 20 ms or so.
     */
    private void runAnimation() {
        while (true) {
            repaint();
            try {Thread.sleep(20);} catch (InterruptedException e) {}
        }
    }

    /**
     * Something to call periodically during sorting.
     */
    private void PAUSE() {
        try {
            Thread.sleep(1);
            if (stop) {
                stop = false;
                // Can't think of a better way to stop than this
                throw new RuntimeException();
            }
        } catch (InterruptedException e) {
        }
    }
}