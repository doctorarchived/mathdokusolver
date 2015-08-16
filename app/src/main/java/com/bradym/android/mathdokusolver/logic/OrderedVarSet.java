package com.bradym.android.mathdokusolver.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Keeps a set of variables ordered according to a provided key. Allows fast updating of keys.
 */
class OrderedVarSet {

    private final Map<Variable, Integer> indexMap;
    private final List<HashSet<Variable>> map;

    private int size;
    private int max;

    public OrderedVarSet() {
        indexMap = new HashMap<>();
        map = new ArrayList<>();
        int MAX_SIZE = 16;
        for (int i = 0; i < MAX_SIZE; i++) {
            map.add(new HashSet<Variable>());
        }
    }

    private void resetMax() {
        for (int i = max; i >= 0; i--) {
            if (!map.get(i).isEmpty()) {
                max = i;
                break;
            }
        }
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public Variable pollMin() {
        for (int i = 0; i < map.size(); i++) {
            HashSet<Variable> hashSet = map.get(i);
            if (!hashSet.isEmpty()) {
                Iterator<Variable> iterator = hashSet.iterator();
                Variable var = iterator.next();
                iterator.remove();
                indexMap.remove(var);
                size--;
                return var;
            }
        }
        return null;
    }

    public void add(Variable var, int key) {
        if (key != -1) {
            indexMap.put(var, key);
            map.get(key).add(var);
            size++;
            max = key > max ? key : max;
        }
    }

    public void update(Variable var, int newKey) {
        int oldKey = indexMap.put(var, newKey);
        if (oldKey != -1) map.get(oldKey).remove(var);
        if (newKey != -1) map.get(newKey).add(var);

        if (newKey > max) {
            max = newKey;
        } else if (oldKey == max) {
            resetMax();
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = max; i >= 0; i--) {
            for (Variable v : map.get(i)) {
                sb.append(v.index).append(" ");
            }

        }
        return sb.toString().trim();
    }
}
