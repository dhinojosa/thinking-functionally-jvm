package com.evolutionnext.sealed.functional;

public sealed interface Expression permits Constant, Sum, Subtract, Multiply {
}


