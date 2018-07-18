-- ----------------------------------------------------------------------------
-- Driver Model
-------------------------------------------------------------------------------
DROP TABLE Trip;
DROP TABLE Driver;
-----------------------------Driver---------------------------------------
CREATE TABLE Driver (driverId BIGINT NOT NULL AUTO_INCREMENT,
					 name VARCHAR(255) COLLATE latin1_bin NOT NULL,
					 city VARCHAR(255) COLLATE latin1_bin NOT NULL,
					 car VARCHAR(255) COLLATE latin1_bin NOT NULL,
					 startTime SMALLINT NOT NULL,
					 endTime SMALLINT NOT NULL,
					 creationDate DATETIME NOT NULL,
           			 puntuacionTotal BIGINT NOT NULL,
         			 numviajes BIGINT NOT NULL,
					 CONSTRAINT DriverPK PRIMARY KEY(driverId),
					 CONSTRAINT validstartDate CHECK ( startDate >= 0 AND startDate <= 23 ),
					 CONSTRAINT validstartDate CHECK ( endDate >= 0 AND endDate <= 23 )
					) ENGINE = InnoDB;

-----------------------------Trip---------------------------------------
CREATE TABLE Trip ( tripId BIGINT NOT NULL AUTO_INCREMENT,
				    driverId BIGINT NOT NULL,
				    origen VARCHAR(255) COLLATE latin1_bin NOT NULL,
				    destino VARCHAR(255) COLLATE latin1_bin NOT NULL,
				    user VARCHAR(255) COLLATE latin1_bin NOT NULL,
				    creditCardNumber VARCHAR(16),
				    valoracion BIGINT NOT NULL,
				    reservationDate DATETIME NOT NULL,
				    CONSTRAINT TripPK PRIMARY KEY(tripId),
				    CONSTRAINT TripDriverIdFK FOREIGN KEY(driverId)
				        REFERENCES Driver(driverId) ON DELETE CASCADE 
				   ) ENGINE = InnoDB;


