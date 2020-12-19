build:
	mvn clean verify package -DsignSkip=false -DdocSkip=false

release:
	mvn clean verify release:prepare release:perform -DsignSkip=false -DdocSkip=false -Darguments="-DsignSkip=false -DdocSkip=false"
