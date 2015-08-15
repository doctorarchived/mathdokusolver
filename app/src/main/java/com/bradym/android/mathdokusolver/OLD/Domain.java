//package com.bradym.android.mathdokusolver.OLD;
//
///**
// * Created by Michael on 7/13/2015.
// */
//public class Domain {
//
//    int domain;
//    int max;
//    int min;
//    int size;
//
//    public Domain(int domain, int max, int min, int size) {
//        this.domain = domain;
//        this.max = max;
//        this.min = min;
//        this.size = size;
//    }
//
//    public Domain(Domain domain) {
//        this.domain = domain.domain;
//        this.max = domain.max;
//        this.min = domain.min;
//        this.size = domain.size;
//    }
//
//    public Domain(int size, boolean type) {
//        if (type) {
//            this.domain = ~(~0 << size);
//            this.max = 32 - Integer.numberOfLeadingZeros(domain);
//            this.min = Integer.numberOfTrailingZeros(domain);
//            this.size = size;
//        } else {
//            this.domain = 1 << size;
//            this.max = size;
//            this.min = size;
//            this.size = 1;
//        }
//
//    }
//
//    public int removeIndex(int index) {
//        int maxMin = 0;
//        domain &= ~(1 << index);
//        if (index == max) {
//            max = 32 - Integer.numberOfLeadingZeros(domain);
//            maxMin += 1;
//        }
//        if (index == min) {
//            min = Integer.numberOfTrailingZeros(domain);
//            maxMin += 2;
//        }
//        size--;
//
//        return maxMin;
//    }
//
//    public void assign(int index) {
//        domain = 1 << index;
//        this.max = this.min = index;
//        size = 1;
//    }
//
//}
