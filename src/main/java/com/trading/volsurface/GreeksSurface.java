package com.trading.volsurface;

import java.util.List;

public class GreeksSurface {
    private final List<GreekPoint> surface;
    public GreeksSurface(List<GreekPoint> surface){
        this.surface=surface;
    }
    public List<GreekPoint> getRawSurface(){
        return surface;
    }
}
