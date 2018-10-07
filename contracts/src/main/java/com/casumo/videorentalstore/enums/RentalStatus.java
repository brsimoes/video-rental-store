package com.casumo.videorentalstore.enums;

public enum RentalStatus {
	
	OPEN {
		public boolean isOpen() {
			return true;
		}
	},
	
	SUBMITED {
		public boolean isSubmited() {
			return true;
		}
	},
	
	CLOSED {
		public boolean isClosed() {
			return true;
		}
	},

	OVERDUE {
		public boolean isOverdue() {
			return true;
		}
	}, 
	
	CANCELED{
		public boolean isCanceled() {
			return true;
		}
	};

	public boolean isOpen() {
		return false;
	}
	
	public boolean isSubmited() {
		return false;
	}
	
	public boolean isClosed() {
		return false;
	}
	
	public boolean isOverdue() {
		return false;
	}
	
	public boolean isCanceled() {
		return false;
	}
}
