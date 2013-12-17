package com.CornelCocioaba.Pixti.GameObject;

import aurelienribon.tweenengine.TweenAccessor;

public class GameObjectAccessor implements TweenAccessor<GameObject>{
	public static final int POSITION_X = 1;
    public static final int POSITION_Y = 2;
    public static final int POSITION_XY = 3;
    public static final int SCALE_X = 4;
    public static final int SCALE_Y = 5;
    public static final int SCALE_XY = 6;
    
	@Override
	public int getValues(GameObject target, int tweenType, float[] returnValues) {
		 switch (tweenType) {
         case POSITION_X: returnValues[0] = target.x; return 1;
         case POSITION_Y: returnValues[0] = target.y; return 1;
         case POSITION_XY:
             returnValues[0] = target.x;
             returnValues[1] = target.y;
             return 2;
         default: assert false; return -1;
     }
	}

	@Override
	public void setValues(GameObject target, int tweenType, float[] newValues) {
		switch (tweenType) {
        case POSITION_X: target.x = newValues[0]; break;
        case POSITION_Y: target.y = newValues[0]; break;
        case POSITION_XY:
            target.x = newValues[0];
            target.y = newValues[1];
            break;
        default: assert false; break;
    }
	}
	

}
