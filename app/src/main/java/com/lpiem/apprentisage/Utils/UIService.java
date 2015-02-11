/******************************************************************************************************************
*
* Apprenti-Sage Android
*
* UIService.java
* 
* Michael Breton - Clement Bretin
* LP IEM - 2014
*
*******************************************************************************************************************/

package com.lpiem.apprentisage.Utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * Contain useful function to make IHM
 */
public class UIService
{
	/**
	 * Convert <b>dp</b> to <b>px</b>
	 * @param context Context
	 * @param dp int
	 * @return the amount of <b>pixel</b> equivalent to <b>dp</b>
	 */
	public static int getPx(Context context, int dp)
	{
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
	    return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics);
	}
}