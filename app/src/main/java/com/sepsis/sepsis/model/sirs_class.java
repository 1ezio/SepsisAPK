package com.sepsis.sepsis.model;

public class sirs_class {
    private float mage;
    private float mresprate;

    public sirs_class() {

    }

    public float getMage() {
        return mage;
    }

    public void setMage(float mage) {
        this.mage = mage;
    }

    public float getMresprate() {
        return mresprate;
    }

    public void setMresprate(float mresprate) {
        this.mresprate = mresprate;
    }

    public float getSisbp() {
        return sisbp;
    }

    public void setSisbp(float sisbp) {
        this.sisbp = sisbp;
    }

    public float getMmap() {
        return mmap;
    }

    public void setMmap(float mmap) {
        this.mmap = mmap;
    }

    public float getMbody_temp() {
        return mbody_temp;
    }

    public void setMbody_temp(float mbody_temp) {
        this.mbody_temp = mbody_temp;
    }

    public float getMdiabp() {
        return mdiabp;
    }

    public void setMdiabp(float mdiabp) {
        this.mdiabp = mdiabp;
    }

    public float getMhr() {
        return mhr;
    }

    public void setMhr(float mhr) {
        this.mhr = mhr;
    }

    public sirs_class(float mage, float mresprate, float sisbp, float mmap, float mbody_temp, float mdiabp, float mhr) {
        this.mage = mage;
        this.mresprate = mresprate;
        this.sisbp = sisbp;
        this.mmap = mmap;
        this.mbody_temp = mbody_temp;
        this.mdiabp = mdiabp;
        this.mhr = mhr;
    }

    private float sisbp;
    private float mmap;
    private float mbody_temp;
    private float mdiabp;
    private float mhr;

}
