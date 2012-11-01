package com.plumtree.spacekiller;

import java.util.ArrayList;
 
import javax.microedition.khronos.opengles.GL10;
 
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.entity.scene.background.ParallaxBackground;
import org.anddev.andengine.entity.shape.Shape;
 
import android.util.Log;
 
   public class VerticalParallaxBackground extends ParallaxBackground {
                        public static int SCROLL_DOWN = -1;
                        public static int SCROLL_UP = 1;
                // ===========================================================
            // Constants
            // ===========================================================
 
                // ===========================================================
            // Fields
            // ===========================================================
 
                private final ArrayList<VerticalParallaxEntity> mParallaxEntities = new ArrayList<VerticalParallaxEntity>();
                private int mParallaxEntityCount;
 
                protected float mParallaxValue;
                private boolean paused;
 
                // ===========================================================
            // Constructors
            // ===========================================================
 
                        public VerticalParallaxBackground(float red, float green, float blue){
                                super(red, green, blue);
                                paused = false;
                                // TODO Auto-generated constructor stub
                        }
 
                // ===========================================================
            // Getter & Setter
            // ===========================================================
 
                public void setParallaxValue(final float pParallaxValue) {
                        this.mParallaxValue = pParallaxValue;
                }
               
                public void setPaused(boolean p){
                        paused = p;
                }
 
            // ===========================================================
            // Methods for/from SuperClass/Interfaces
            // ===========================================================
 
                @Override
                public void onDraw(final GL10 pGL, final Camera pCamera) {
                        super.onDraw(pGL, pCamera);
 
                        final float parallaxValue = this.mParallaxValue;
                        final ArrayList<VerticalParallaxEntity> parallaxEntities = this.mParallaxEntities;
                        Log.d("VAPB", "VAPB onDraw pre entity");
                        for(int i = 0; i < this.mParallaxEntityCount; i++) {
                                parallaxEntities.get(i).onDraw(pGL, parallaxValue, pCamera, paused);
                        }
                }
 
                // ===========================================================
            // Methods
            // ===========================================================
 
                public void attachParallaxEntity(final VerticalParallaxEntity pParallaxEntity) {
                        this.mParallaxEntities.add(pParallaxEntity);
                        this.mParallaxEntityCount++;
                }
 
                public boolean removeParallaxEntity(final VerticalParallaxEntity pParallaxEntity) {
                        this.mParallaxEntityCount--;
                        final boolean success = this.mParallaxEntities.remove(pParallaxEntity);
                        if(success == false) {
                                this.mParallaxEntityCount++;
                        }
                        return success;
                }
 
                // ===========================================================
            // Inner and Anonymous Classes
            // ===========================================================
 
                public static class VerticalParallaxEntity {
                        // ===========================================================
                    // Constants
                    // ===========================================================
 
                        // ===========================================================
                    // Fields
                    // ===========================================================
 
                        final float mParallaxFactor;
                        final Shape mShape;
                        private int direction;
                        // ===========================================================
                    // Constructors
                    // ===========================================================
 
                        public VerticalParallaxEntity(final float pParallaxFactor, final Shape pShape) {
                                this.mParallaxFactor = pParallaxFactor;
                                this.mShape = pShape;
                                this.direction = VerticalParallaxBackground.SCROLL_DOWN;
                        }
 
                        public VerticalParallaxEntity(final float pParallaxFactor, final Shape pShape, int direction) {
                        this.mParallaxFactor = pParallaxFactor;
                        this.mShape = pShape;
                        this.direction = direction;
                        }
 
                        // ===========================================================
                    // Getter & Setter
                    // ===========================================================
 
                        // ===========================================================
                    // Methods for/from SuperClass/Interfaces
                    // ===========================================================
 
                        // ===========================================================
                    // Methods
                    // ===========================================================
                       
                        public void onDraw(final GL10 pGL, final float pParallaxValue, final Camera pCamera, boolean paused) {
                                if(paused){
                            this.mShape.onDraw(pGL, pCamera);
                                }
                                else{
                                        pGL.glPushMatrix();
                                {
                                        final float cameraHeight = pCamera.getHeight();
                                        final float shapeHeightScaled = this.mShape.getHeightScaled();
                                        float baseOffset = (pParallaxValue * this.mParallaxFactor) % shapeHeightScaled;
                                        while(baseOffset > 0) {
                                                baseOffset -= shapeHeightScaled;
                                        }
                                        pGL.glTranslatef(0, (direction * baseOffset), 0);
                                        float currentMaxY = baseOffset;
                                        do {
                                                this.mShape.onDraw(pGL, pCamera);
                                                pGL.glTranslatef(0, (direction * shapeHeightScaled),0);
                                                currentMaxY += shapeHeightScaled;
                                        }while(currentMaxY < cameraHeight);
                                }
                                pGL.glPopMatrix();
                                }
                        }
 
                        // ===========================================================
                    // Inner and Anonymous Classes
                    // ===========================================================
                }
    }
 
