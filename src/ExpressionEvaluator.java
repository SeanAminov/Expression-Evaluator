import java.util.Arrays;

/**
 * The Class ExpressionEvaluator.
 */
// TODO: Auto-generated Javadoc
public class ExpressionEvaluator {
	// These are the required error strings for that MUST be returned on the
	// appropriate error
	/** The Constant PAREN_ERROR. */
	// for the JUnit tests to pass
	private static final String PAREN_ERROR = "Paren Error: ";

	/** The Constant OP_ERROR. */
	private static final String OP_ERROR = "Op Error: ";

	/** The Constant DATA_ERROR. */
	private static final String DATA_ERROR = "Data Error: ";

	/** The Constant DIV0_ERROR. */
	private static final String DIV0_ERROR = "Div0 Error: ";

	/** The start string. */
	// The placeholders for the two stacks
	private String startString;

	/** The expressions. */
	private RegularExpressions expressions;

	/** The data stack. */
	private GenericStack<Double> dataStack;

	/** The oper stack. */
	private GenericStack<String> operStack;

	/** The array stack. */
	private GenericStack<Boolean> arrayStack;

	/** The str array. */
	private String[] strArray;

	/** The val. */
//	private String operTop = "";
//	private boolean rightBracket = false;
	/** The answer. */
//	private boolean leftBracket = false;
	private String answer;

	/** The top value. */
	private Double topValue = 0.0;

	/** The bottom value. */
	private Double bottomValue = 0.0;

	/** The error string. */
	private String errorString;

	/** The skip paren. */
	private int skipParen;

	/** The negative implicit. */
	private boolean negativeImplicit;

//	private String prevOperator;

	/** The right paren. */
	int rightParen = 0;

	/** The left paren. */
	int leftParen = 0;

	/**
	 * Negative implicit.
	 *
	 * @param str the str
	 * @return the string
	 */
	private String negativeImplicit(String str) {
		for (int i = 0; i < str.length(); i++) {
			if (i + 5 < str.length()) {
				if (str.substring(i, i + 1).equals("(")) {
					if (str.substring(i, i + 5).equals("(-1*(")) {
						arrayStack.push(true);
						negativeImplicit = true;
					} else {
						arrayStack.push(false);
					}
				}
			}
			if (str.substring(i, i + 1).equals(")") && negativeImplicit && !arrayStack.empty()) {
				if (!(arrayStack.size() == 1))
					arrayStack.pop();
				if (arrayStack.peek() == true) {
					arrayStack.pop();
					str = str.substring(0, i) + ")" + str.substring(i, str.length());
					i++;
				}
			}
		}
		return str;
	}

	/**
	 * Convert to tokens. Takes a string and splits it into tokens that are either
	 * operators or data. This is where you should convert implicit multiplication
	 * to explict multiplication. It is also a candidate for recognizing negative
	 * numbers, and then including that negative sign as part of the appropriate
	 * data token.
	 *
	 * @param str the str
	 * @return the string[]
	 */
	private String[] convertToTokens(String str) {
		arrayStack = new GenericStack();
		negativeImplicit = false;
		skipParen = 0;
		rightParen = 0;
		leftParen = 0;

		str = str.replaceAll("(\\))\\s*(\\()", "$1*$2");
		str = str.replaceAll("(\\d)\\s*(\\()", "$1*$2");
		str = str.replaceAll("(\\))\\s*(\\d)", "$1*$2");
		str = str.replaceAll("^\\-\\(", "(-1*("); // Start of string negative implicit multiplication
		str = str.replaceAll("([\\+\\-\\/\\(\\*])\\-\\(", "$1(-1*(");

		str = negativeImplicit(str); // method to reduce amount of code in this method
		str = str.replaceAll("([\\+\\-\\/\\(\\)\\*])", " $1 ");
		str = str.replaceAll("^\\s*()", "");
		startString = str;
		if (str.substring(0, 2).equals("- ")) {
			str = str.replaceFirst(" ", "");
		}
		str = str.replaceAll("([\\+\\-\\/\\(\\)\\*])\\s+\\-\\s(\\d)", "$1 -$2");
		String[] strArray = str.split("[\\s]+");
		return strArray;
	}

	/**
	 * Presedence.
	 *
	 * @param arrayToken     the array token
	 * @param topOfOperStack the top of oper stack
	 * @return true, if successful
	 */
	public boolean presedence(String arrayToken, String topOfOperStack) {
		int arrayTokenValue = 0;
		int topOfOperStackValue = 0;
		if (arrayToken.equals("*") || arrayToken.equals("/")) { // basic code to set values
			arrayTokenValue = 2;
		} else if (arrayToken.equals("-") || arrayToken.equals("+")) {
			arrayTokenValue = 1;
		} else if (arrayToken.equals("(")) {
			arrayTokenValue = 3;
		} else {
			arrayTokenValue = 4;
		}
		if (topOfOperStack.equals("*") || topOfOperStack.equals("/")) {
			topOfOperStackValue = 2;
		} else if (topOfOperStack.equals("-") || topOfOperStack.equals("+")) {
			topOfOperStackValue = 1;
		} else if (topOfOperStack.equals("(")) {
			topOfOperStackValue = 3;
		} else {
			topOfOperStackValue = 4;
		}
		if (topOfOperStackValue == 3 && arrayTokenValue != 4) {
			return false;
		}
		if (topOfOperStackValue >= arrayTokenValue) { // If the top of the operStack is greater than the latest value in
														// the array
			return true;
		}
		return false;
	}

	/**
	 * Solver.
	 *
	 * @param operation the operation
	 */
	public void solver(String operation) {
		if (!operation.equals("(") && !operation.equals(")")) { // prevent dataStack isn't large enough yet
			bottomValue = dataStack.pop();
			topValue = dataStack.pop();
		}
		switch (operation.charAt(0)) {
		case '+':
			dataStack.push(topValue + bottomValue);
			break;
		case '-':
			dataStack.push(topValue - bottomValue);
			break;
		case '*':
			dataStack.push(topValue * bottomValue);
			break;
		case '/':
			dataStack.push(topValue / bottomValue);
			break;
		case '(':
			operStack.push("(");
			break;
		case ')': // special code for ')' because of its properties in a calculator
			solver(operStack.pop());
			if (operStack.peek().equals("(")) {
				operStack.pop();
			} else {
				solver(operStack.pop());
			}
			break;
		}
	}

	/**
	 * Paren errors.
	 *
	 * @return true, if successful
	 */
	public boolean ParenErrors() {
		errorString = PAREN_ERROR;
		for (int i = 0; i < strArray.length; i++) {
			if (strArray[i].equals("(")) {
				rightParen++;
			}
			if (strArray[i].equals(")")) {
				leftParen++;
			}
			if (leftParen > rightParen) { // ')' > '('
				errorString = PAREN_ERROR;
				return true;
			}
			if (i >= 1) {
				if (strArray[i].equals(")") && strArray[i - 1].equals("(")) { // Empty parens
					errorString = PAREN_ERROR;
					return true;
				}
			}
		}
		if (leftParen != rightParen) { // they're not equal
			errorString = PAREN_ERROR;
			return true;
		}
		return false;
	}

	/**
	 * Error checking.
	 *
	 * @return true, if successful
	 */
	public boolean errorChecking() {
		if (ParenErrors())
			return true;
		for (int i = 0; i < strArray.length; i++) {
			errorString = DATA_ERROR;
			if (i >= 1) {
				if (strArray[i].matches("([\\+\\-\\/\\*\\)])") && strArray[i - 1].matches("([\\+\\-\\/\\*])")) { // matching
					errorString = OP_ERROR;
					return true;
				}

				if ((strArray[i].matches("-?(\\d+)") || strArray[i].matches("-?((\\d+)\\.(\\d+))")) && (strArray[i - 1].matches("-?(\\d+)") || strArray[i - 1].matches("-?((\\d+)\\.(\\d+))")))
					return true;
			}
			if (!strArray[i].matches("([\\+\\-\\/\\(\\)\\*])")) { // match operators
				if (!strArray[i].matches("-?((\\d+)\\.(\\d+))") && !strArray[i].matches("-?(\\d+)"))
					return true;
				try {
					Double.parseDouble(strArray[i]); // error checking for bad values like a letter
				} catch (NumberFormatException e) {
					return true;
				}
			}
		}
		errorString = OP_ERROR;
		if (!strArray[0].equals("(") && !strArray[0].equals("-") && !strArray[0].matches("-?(\\d+)") && !strArray[0].matches("-?((\\d+)\\.(\\d+))"))
			return true;
		if (!strArray[strArray.length - 1].equals(")") && !strArray[strArray.length - 1].matches("-?(\\d+)") && !strArray[strArray.length - 1].matches("-?((\\d+)\\.(\\d+))"))
			return true;
		return false;
	}

	/**
	 * Evaluate expression. This is it, the big Kahuna.... It is going to be called
	 * by the GUI (or the JUnit tester), and: a) convert the string to tokens b) if
	 * conversion successful, perform static error checking - Paren Errors - Op
	 * Errors - Data Errors c) if static error checking is successful: - evaluate
	 * the expression, catching any runtime errors. For the purpose of this project,
	 * the only runtime errors are divide-by-0 errors.
	 *
	 * @param str the str
	 * @return the string
	 */
	protected String evaluateExpression(String str) {
		dataStack = new GenericStack<Double>(); // need to reset these values
		operStack = new GenericStack<String>();
		strArray = convertToTokens(str);
		if (errorChecking())
			return errorString;
		for (int i = 0; i < strArray.length; i++) {
			if (strArray[i].matches("-?(\\d+)") || strArray[i].matches("-?((\\d+)\\.(\\d+))")) {
				dataStack.push(Double.parseDouble(strArray[i]));
			} else {
				if (operStack.empty()) { // empty operStack means to push value if operator
					operStack.push(strArray[i]);
				} else if (presedence(strArray[i], operStack.peek())) { // If operStack has more presedence than
																		// topOfArray
					if ((operStack.peek().equals(")") && strArray[i].equals(")"))
							|| (!strArray[i].equals("(") && !strArray[i].equals(")"))) {
						i--;
					}
					solver(operStack.pop());
				} else {
					operStack.push(strArray[i]);
				}
			}
		}
		while (dataStack.size() >= 2) // solve at the very beginning
			solver(operStack.pop());
		answer = dataStack.pop() + "";
		if (answer.equals("Infinity")) // Infinity is what you get when dividing by 0
			return DIV0_ERROR;
		return (str + " = " + answer); // answer!!! woo!
	}
}
