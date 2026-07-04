package com.evolutionnext.sealed.shapes;

public sealed abstract class Shape permits Circle, Triangle, Rectangle {
    public abstract int area();
}
