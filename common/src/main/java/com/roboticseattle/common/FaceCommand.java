package com.roboticseattle.common;

public class FaceCommand extends Command {
	private String expression;

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}
	
	public FaceExpression getFaceState() {
		return FaceExpression.valueOf(expression.toUpperCase());
	}
	
}
