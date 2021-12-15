import java.util.*;

public class Main {

    public static void main(String[] args) {
        task15();
    }

    static final boolean SOLVE_PART_2 = true;

    static void task15() {
        Scanner in = new Scanner(System.in);

        String s = in.nextLine();

        int n = s.length();

        Map<Integer, Map<Integer, Integer>> ijv = new HashMap<>();
        ijv.put(0, new HashMap<>());
        int zero = '0';
        for (int j = 0; j < n; j++) {
            ijv.get(0).put(j, s.charAt(j) - zero);
        }
        for (int i = 1; i < n; i++) {
            s = in.nextLine();
            ijv.put(i, new HashMap<>());
            for (int j = 0; j < n; j++) {
                ijv.get(i).put(j, s.charAt(j) - zero);
            }
        }
        if (SOLVE_PART_2) {
            int n5 = 5 * n;

            for (int i = 0; i < n; i++) {
                for (int j = n; j < n5; j++) {
                    int v = ijv.get(i).get(j - n);
                    v = v < 9 ? v + 1 : 1;
                    ijv.get(i).put(j, v);
                }
            }

            for (int i = n; i < n5; i++) {
                ijv.put(i, new HashMap<>());
                for (int j = 0; j < n5; j++) {
                    int v = ijv.get(i - n).get(j);
                    v = v < 9 ? v + 1 : 1;
                    ijv.get(i).put(j, v);
                }
            }
            n = n5;
        }

        Map<Integer, KeyValue<List<Integer>, Integer>> graph = new HashMap<>();
        List<Integer> l;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                l = new ArrayList<>();
                if (i > 0) l.add((i - 1) * n + j);
                if (i < n - 1) l.add((i + 1) * n + j);
                if (j > 0) l.add(i * n + j - 1);
                if (j < n - 1) l.add(i * n + j + 1);
                graph.put(i * n + j, new KeyValue<>(l, ijv.get(i).get(j)));
            }
        }
        int start = 0;
        int end = graph.size() - 1;

        boolean[] vd = new boolean[graph.size()];
        vd[start] = true;

        int[] sh = new int[graph.size()];
        Arrays.fill(sh, Integer.MAX_VALUE);
        sh[start] = 0;

        TreeMap<Integer, List<Integer>> tm = new TreeMap<>();
        for (int i : graph.get(start).k) {
            sh[i] = graph.get(i).v;
            if (!tm.containsKey(sh[i])) tm.put(sh[i], new ArrayList<>());
            tm.get(sh[i]).add(i);
        }

        int min, ind;
        for (int i = 1; i < sh.length; i++) {
            min = tm.ceilingKey(0);
            ind = tm.get(min).remove(0);
            if (tm.get(min).isEmpty()) tm.remove(min);

            vd[ind] = true;

            for (int j : graph.get(ind).k) {
                if (!vd[j]) {
                    if (sh[j] > min + graph.get(j).v) {
                        sh[j] = min + graph.get(j).v;
                        if (!tm.containsKey(sh[j])) tm.put(sh[j], new ArrayList<>());
                        tm.get(sh[j]).add(j);
                    }
                }
            }
        }
        System.out.println(sh[end]);
    }

    static class KeyValue<K, V> {
        K k;
        V v;

        public KeyValue(K k, V v) {
            this.k = k;
            this.v = v;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            KeyValue<?, ?> keyValue = (KeyValue<?, ?>) o;
            return Objects.equals(k, keyValue.k) && Objects.equals(v, keyValue.v);
        }

        @Override
        public int hashCode() {
            return Objects.hash(k, v);
        }
    }
}
