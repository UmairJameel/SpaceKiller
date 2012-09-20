package com.plumtree.spacekiller;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

import org.anddev.andengine.audio.music.Music;
import org.anddev.andengine.audio.music.MusicFactory;
import org.anddev.andengine.audio.sound.Sound;
import org.anddev.andengine.audio.sound.SoundFactory;
import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.handler.IUpdateHandler;
import org.anddev.andengine.engine.handler.timer.ITimerCallback;
import org.anddev.andengine.engine.handler.timer.TimerHandler;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.modifier.MoveModifier;
import org.anddev.andengine.entity.modifier.MoveXModifier;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnSceneTouchListener;
import org.anddev.andengine.entity.scene.background.AutoParallaxBackground;
import org.anddev.andengine.entity.scene.background.ParallaxBackground.ParallaxEntity;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.ui.activity.BaseGameActivity;

import android.util.Log;
import android.view.Display;

public class MainActivity extends BaseGameActivity implements IOnSceneTouchListener{
	
	private Camera mCamera;
	private Scene mMainScene;
	private Sprite mBackground;
	private BitmapTextureAtlas mBitmapTextureAtlas, mBgTextureAtlas;
	private TextureRegion mPlayerTextureRegion, mProjectileTextureRegion, mTargetTextureRegion, mSceneBgTextureRegion;
	private Sprite player;
	private LinkedList targetLL, targetsToBeAdded, projectileLL, projectilesToBeAdded;
	private boolean hit ;
	private Sound shootingSound;
	private Music backgroundMusic;
	
	private static final int CAMERA_WIDTH = 720;
    private static final int CAMERA_HEIGHT = 480;

	
	@Override
	public void onLoadComplete() {
		// TODO Auto-generated method stub
		try {
			backgroundMusic = MusicFactory.createMusicFromAsset(mEngine.getMusicManager()
					, this, "backgroung_music.wav");
			backgroundMusic.setLooping(true);
			backgroundMusic.play();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public Engine onLoadEngine() {
		// TODO Auto-generated method stub
		final Display display = getWindowManager().getDefaultDisplay();
		int cameraWidth = display.getWidth();
		int cameraHeight = display.getHeight();
		
		this.mCamera = new Camera(0, 0, cameraWidth, cameraHeight);
		
		return new Engine(new EngineOptions(true, ScreenOrientation.PORTRAIT,
				new RatioResolutionPolicy(cameraWidth, cameraHeight), this.mCamera)
		        .setNeedsMusic(true).setNeedsSound(true));  
	}

	@Override
	public void onLoadResources() {
		// TODO Auto-generated method stub
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		this.mBitmapTextureAtlas = new BitmapTextureAtlas(1024, 1024, 
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);

    	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		this.mBgTextureAtlas = new BitmapTextureAtlas(1024 ,1024 , 
					TextureOptions.DEFAULT);

		
		mSceneBgTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mBgTextureAtlas, this, "background.png",0,0);
		this.mEngine.getTextureManager().loadTextures(mBgTextureAtlas);
		mPlayerTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mBitmapTextureAtlas, this, "click.png",0,0);
		mTargetTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mBitmapTextureAtlas, this, "Target.png", 128, 0);
		mEngine.getTextureManager().loadTexture(mBitmapTextureAtlas);
		mProjectileTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mBitmapTextureAtlas, this, "Projectile.png",64,0);
		SoundFactory.setAssetBasePath("mfx/");
		MusicFactory.setAssetBasePath("mfx/");
		try{
			
			shootingSound = SoundFactory.createSoundFromAsset(mEngine.getSoundManager()
					,this, "pew_pew_lei.wav");
			backgroundMusic = MusicFactory.createMusicFromAsset(mEngine.getMusicManager()
					, this, "backgroung_music.wav");
			backgroundMusic.setLooping(true);
		}catch(IllegalStateException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();	
		}
	}

	
	@Override
	public Scene onLoadScene() {
		// TODO Auto-generated method stub
		mEngine.registerUpdateHandler(new FPSLogger());
		
	/*	final int centerX = (CAMERA_WIDTH -
				mSceneBgTextureRegion.getWidth()) / 2; 
		final int centerY = (CAMERA_HEIGHT -
						mSceneBgTextureRegion.getHeight()) / 2;
		this.mMainScene = new Scene();
		SpriteBackground bg = new SpriteBackground(new Sprite(centerX, centerY, mSceneBgTextureRegion));
		mMainScene.setBackground(bg);*/
	//	mMainScene.setBackground(new ColorBackground(0,0,0,1));
		this.mMainScene= new Scene();
		final AutoParallaxBackground autoParallaxBackground = 
				new AutoParallaxBackground(0, 0, 0, 10);
		autoParallaxBackground.attachParallaxEntity(new ParallaxEntity(-25.0f,
				new Sprite(0,mCamera.getHeight() - this.mSceneBgTextureRegion.getHeight(),
						this.mSceneBgTextureRegion)));
		mMainScene.setBackground(autoParallaxBackground);
		/*mSceneBG = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mBitmapTextureAtlas, this, "click.png",0,0);
		
		mMainScene.setBackground(new Sprite(mMainScene.getInitialX(), mMainScene.getInitialY(), mSceneBG)));
		*/
		
	//	this.mBackground = new Sprite(0,0,this.mSceneBgTextureRegion);
		//mMainScene.getFirstChild().attachChild(this.mBackground);
		final int PlayerX = this.mPlayerTextureRegion.getWidth() / 2;
		final int PlayerY = (int) ((mCamera.getHeight()-mPlayerTextureRegion.getHeight())/2);
		player = new Sprite(PlayerX,PlayerY,mPlayerTextureRegion);
		player.setScale(2);
		mMainScene.attachChild(player);
		targetLL = new LinkedList();
		targetsToBeAdded = new LinkedList();
		spritSprawnTimeHandler();
		mMainScene.registerUpdateHandler(detect);
		projectileLL = new LinkedList();
		projectilesToBeAdded = new LinkedList();
		mMainScene.setOnSceneTouchListener(this);
		
		
		return this.mMainScene;
	}
	
	public void addTarget(){
		Random ran = new Random();
		
		int x = (int) mCamera.getWidth() + mTargetTextureRegion.getWidth();
		int minY = mTargetTextureRegion.getHeight();
		int maxY =(int) (mCamera.getWidth() - mTargetTextureRegion.getWidth());
		int rangeY = maxY - minY ;
		int y = ran.nextInt(rangeY) + minY;
		
		Sprite target = new Sprite(x, y, mTargetTextureRegion.clone());
		mMainScene.attachChild(target);
		
		int minDuration = 2;
		int maxDuration = 4;
		int rangeDuration = maxDuration - minDuration;
		int actualDuration = ran.nextInt(rangeDuration) + minDuration ;
		
		MoveXModifier mod = new MoveXModifier(actualDuration, target.getX(),-target.getWidth());
		target.registerEntityModifier(mod.clone());
		targetsToBeAdded.add(target);
	}
	
	private void spritSprawnTimeHandler(){
		TimerHandler spritTimeHandler;
		float mEffectSpawnDelay = 1f ;
		
		spritTimeHandler = new TimerHandler(mEffectSpawnDelay, true,
				new ITimerCallback() {
			
			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				// TODO Auto-generated method stub
				addTarget();
				
			}
		});
		getEngine().registerUpdateHandler(spritTimeHandler);
	}
	
	public void removeSprite(final Sprite _sprite, Iterator it){
		runOnUpdateThread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				mMainScene.detachChild(_sprite);
			}
			
		});
		it.remove();
		
	}
	
	IUpdateHandler detect = new IUpdateHandler(){
		
		@Override
		public void reset() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onUpdate(float pSecondsElapsed) {
			// TODO Auto-generated method stub
			try {
				Iterator<Sprite> targets = targetLL.iterator();
				Sprite _target;
				boolean hit = false;
				
				while (targets.hasNext()){
					_target = targets.next();
					if (_target.getX() <= -_target.getWidth()){
						removeSprite(_target, targets);
						break;
					}
					Iterator<Sprite> projectiles = projectileLL.iterator();
					Sprite _projectile;
					while (projectiles.hasNext()){
						_projectile = projectiles.next();
						if (_projectile.getX() >= mCamera.getWidth()
								|| _projectile.getY() >= mCamera.getHeight()
								+ _projectile.getHeight()
								|| _projectile.getY() <= -_projectile.getHeight()){
							removeSprite (_projectile,projectiles);
							continue;		
								}
						if (_target.collidesWith(_projectile)){
							removeSprite(_projectile,projectiles);
							hit=true ;
							break;
						}
							
						
					}
					if(hit){
						removeSprite(_target,targets);
						hit=false;						
					}
				}
				projectileLL.addAll(projectilesToBeAdded);
				projectilesToBeAdded.clear();
				
				targetLL.addAll(targetsToBeAdded);
				targetsToBeAdded.clear();
				
			} catch(Exception ex) {
				Log.v("ex at onUpdate", ex.toString());
			}
		}
		
	};
	
	private void shootProjectile(final float pX, final float pY){
		int offX = (int) (pX-player.getX());
		int offY = (int) (pY-player.getY());
		if (offX <= 0)
			return ;
		final Sprite projectile;
		projectile = new Sprite(player.getX(), player.getY(),
				mProjectileTextureRegion.clone());
		mMainScene.attachChild(projectile,1);
		
		int realX = (int) (mCamera.getWidth()+ projectile.getWidth()/2.0f);
		float ratio = (float) offY / (float) offX;
		int realY = (int) ((realX*ratio) + projectile.getY());
		
		int offRealX = (int) (realX- projectile.getX());
		int offRealY = (int) (realY- projectile.getY());
		float length = (float) Math.sqrt((offRealX*offRealX)+(offRealY*offRealY));
		float velocity = (float) 480.0f/1.0f;
		float realMoveDuration = length/velocity;
		  
		MoveModifier modifier = new MoveModifier(realMoveDuration,
				projectile.getX(), realX, projectile.getY(), realY);
		projectile.registerEntityModifier(modifier.clone());
		projectilesToBeAdded.add(projectile);
		shootingSound.play();
	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		// TODO Auto-generated method stub
		if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN){
			final float touchX = pSceneTouchEvent.getX();
			final float touchY = pSceneTouchEvent.getY();
			shootProjectile(touchX, touchY);
			return  true;
		}
		
		return false;
	}
}
