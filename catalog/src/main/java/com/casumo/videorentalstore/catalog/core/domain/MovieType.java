package com.casumo.videorentalstore.catalog.core.domain;

public enum MovieType {
	
	NEW_RELEASE {
		public boolean isNewRelease() {
			return true;
		}
	},
	
	REGULAR_FILM {
		public boolean isRegularFilm() {
			return true;
		}
	},
	
	OLD_FILM {
		public boolean isOldFilm() {
			return true;
		}
	};

	public boolean isNewRelease() {
		return false;
	}
	
	public boolean isRegularFilm() {
		return false;
	}
	
	public boolean isOldFilm() {
		return false;
	}
}
