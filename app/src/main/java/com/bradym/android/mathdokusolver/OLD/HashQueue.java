//package com.bradym.android.mathdokusolver.OLD;
//
//import com.bradym.android.mathdokusolver.OLD.TrueVariable;
//
//import java.util.ArrayList;
//import java.util.Comparator;
//import java.util.HashMap;
//import java.util.List;
//import java.util.TreeSet;
//
///**
// * Created by Michael on 6/14/2015.
// *
// * Queue backed by HashSet and HashMap
// */
//public class HashQueue {
//
//    List<TreeSet<TrueVariable>> queues;
//    HashMap<TrueVariable, Integer> indices;
//
//
//    Comparator<TrueVariable> comparator = new Comparator<TrueVariable>() {
//        @Override
//        public int compare(TrueVariable lhs, TrueVariable rhs) {
//            return lhs.weightedIndex - rhs.weightedIndex;
//        }
//    };
//
//    public HashQueue(TrueVariable[] tvs, int max) {
//        queues = new ArrayList<>();
//        indices = new HashMap<>();
//        for (int i = 0; i < max; i++) {
//            queues.add(i, new TreeSet<>(comparator));
//        }
//
//        for (TrueVariable tv : tvs) {
//            if (tv.constraints.get(2).scope.size() == 1) {
//                queues.get(0).add(tv);
//                indices.put(tv, 0);
//            } else {
//                int t = tv.domainSize;
//                indices.put(tv, t);
//            }
//        }
//    }
//
//
//
//    /*
//    Update this variable's position in the queues
//     */
//    public void update(TrueVariable tv) {
//        Integer i;
//        if ((i= indices.get(tv)) != null) {
//            queues.get(i).remove(tv);
//            queues.get(tv.domainSize).add(tv);
//
//            indices.remove(tv);
//            indices.put(tv, tv.domainSize);
//        }
//    }
//
//    /*
//    Dequeue the next variable in the queues
//     */
//    public TrueVariable dequeue() {
//        for (TreeSet<TrueVariable> ts : queues) {
//            if (!ts.isEmpty()) {
//                TrueVariable tv = ts.pollFirst();
//                indices.remove(tv);
//                return tv;
//            }
//        }
//        return null;
//    }
//
//    /*
//    Add the variable tv to the queues
//     */
//    public void enqueue(TrueVariable tv) {
//        queues.get(tv.domainSize).add(tv);
//        indices.put(tv, tv.domainSize);
//    }
//
//}
