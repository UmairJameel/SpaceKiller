package com.plumtree.spacekiller; 

public final class Vector2 {

private final static Vector2 tmp = new Vector2();
public float x;
public float y;

public Vector2 () {

}

public Vector2 (float x, float y) {
this.x = x;
this.y = y;
}
public Vector2 (int x, float y){
	this.x = x;
	this.y = y;
}

public Vector2 (Vector2 v) {
set(v);
}

public Vector2 cpy () {
return new Vector2(this);
}

public float len () {
return (float)Math.sqrt(x * x + y * y);
}


public float len2 () {
return x * x + y * y;
}

public Vector2 set (Vector2 v) {
x = v.x;
y = v.y;
return this;
}

public Vector2 set (float x, float y) {
this.x = x;
this.y = y;
return this;
}

public Vector2 sub (Vector2 v) {
x -= v.x;
y -= v.y;
return this;
}


public Vector2 nor () {
float len = len();
if (len != 0) {
x /= len;
y /= len;
}
return this;
}


public Vector2 add (Vector2 v) {
x += v.x;
y += v.y;
return this;
}

public Vector2 add (float x, float y) {
this.x += x;
this.y += y;
return this;
}


public float dot (Vector2 v) {
return x * v.x + y * v.y;
}

public Vector2 mul (float scalar) {
x *= scalar;
y *= scalar;
return this;
}


public float dst (Vector2 v) {
float x_d = v.x - x;
float y_d = v.y - y;
return (float)Math.sqrt(x_d * x_d + y_d * y_d);
}


public float dst (float x, float y) {
float x_d = x - this.x;
float y_d = y - this.y;
return (float)Math.sqrt(x_d * x_d + y_d * y_d);
}


public float dst2 (Vector2 v) {
float x_d = v.x - x;
float y_d = v.y - y;
return x_d * x_d + y_d * y_d;
}

public String toString () {
return "[" + x + ":" + y + "]";
}


public Vector2 sub (float x, float y) {
this.x -= x;
this.y -= y;
return this;
}

public Vector2 tmp () {
return tmp.set(this);
}


public float cross(final Vector2 v) {
return this.x * v.y - v.x * this.y;
}

public float lenManhattan() {
return Math.abs(this.x) + Math.abs(this.y);
}
}