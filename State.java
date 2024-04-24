package assignment04;

public enum State {
	WAITING {
		@Override
		public State enter_key(int a, GarageDoorOpener opener) {
			// if a is equal to the code[0] in opener then
			// set the valid in opener to true
			// in any case, return ONE_KEY
		}
	}, ONE_KEY {
		@Override
		public State enter_key(int b, GarageDoorOpener opener) {
			// if b is NOT equal to the code[1] in opener then
			// set the valid in opener to false
			// in any case, return TWO_KEY
		}
	}, TWO_KEY {
		@Override
		public State enter_key(int c, GarageDoorOpener opener) {
			// if c is NOT equal to the code[2] in opener then
			// set the valid in opener to false
			// in any case, return THREE_KEY
		}
	}, THREE_KEY {
		@Override
		public State enter_key(int d, GarageDoorOpener opener) {
			// if d is NOT equal to the code[4] in opener then
			// set the valid in opener to false
			// in any case, return FINAL
		}
	}, FINAL {
		@Override
		public State enter_key(int k, GarageDoorOpener opener) {
			// For any k, set the valid in opener to false
			// in any case, return FINAL
		}
		public State press_open(GarageDoorOpener opener) {
			// If the valid in opener is true--use isValid()--then
			// call call the method that runs the door Motor in opener
			// In any case, set the valid in opener to false and return WAITING
		}
	};
	
	abstract State enter_key(int k, GarageDoorOpener opener);
	
	public State press_open(GarageDoorOpener opener) {
		// Set the valid in opener to false and return WAITING
	}
}
