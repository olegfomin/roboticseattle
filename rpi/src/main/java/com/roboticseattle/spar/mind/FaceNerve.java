package com.roboticseattle.spar.mind;

import com.roboticseattle.common.FaceExpression;

/**
 * Face Event Handler
 * @author robotic seattle
 *
 */
public interface FaceNerve extends Nerve {
	void onEpressionComplete(FaceExpression state);
}
