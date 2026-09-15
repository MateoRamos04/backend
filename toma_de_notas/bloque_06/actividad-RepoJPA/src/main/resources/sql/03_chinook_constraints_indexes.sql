-- Chinook (fuente: lerocha/chinook-database) adaptado a H2
-- 03: claves foráneas e índices

ALTER TABLE Album ADD CONSTRAINT FK_AlbumArtistId FOREIGN KEY (ArtistId) REFERENCES Artist (ArtistId);
ALTER TABLE Track ADD CONSTRAINT FK_TrackAlbumId FOREIGN KEY (AlbumId) REFERENCES Album (AlbumId);
ALTER TABLE Track ADD CONSTRAINT FK_TrackGenreId FOREIGN KEY (GenreId) REFERENCES Genre (GenreId);
ALTER TABLE Track ADD CONSTRAINT FK_TrackMediaTypeId FOREIGN KEY (MediaTypeId) REFERENCES MediaType (MediaTypeId);
ALTER TABLE Employee ADD CONSTRAINT FK_EmployeeReportsTo FOREIGN KEY (ReportsTo) REFERENCES Employee (EmployeeId);
ALTER TABLE Customer ADD CONSTRAINT FK_CustomerSupportRepId FOREIGN KEY (SupportRepId) REFERENCES Employee (EmployeeId);
ALTER TABLE Invoice ADD CONSTRAINT FK_InvoiceCustomerId FOREIGN KEY (CustomerId) REFERENCES Customer (CustomerId);
ALTER TABLE InvoiceLine ADD CONSTRAINT FK_InvoiceLineInvoiceId FOREIGN KEY (InvoiceId) REFERENCES Invoice (InvoiceId);
ALTER TABLE InvoiceLine ADD CONSTRAINT FK_InvoiceLineTrackId FOREIGN KEY (TrackId) REFERENCES Track (TrackId);
ALTER TABLE PlaylistTrack ADD CONSTRAINT FK_PlaylistTrackPlaylistId FOREIGN KEY (PlaylistId) REFERENCES Playlist (PlaylistId);
ALTER TABLE PlaylistTrack ADD CONSTRAINT FK_PlaylistTrackTrackId FOREIGN KEY (TrackId) REFERENCES Track (TrackId);

CREATE INDEX IFK_AlbumArtistId ON Album (ArtistId);
CREATE INDEX IFK_CustomerSupportRepId ON Customer (SupportRepId);
CREATE INDEX IFK_EmployeeReportsTo ON Employee (ReportsTo);
CREATE INDEX IFK_InvoiceCustomerId ON Invoice (CustomerId);
CREATE INDEX IFK_InvoiceLineInvoiceId ON InvoiceLine (InvoiceId);
CREATE INDEX IFK_InvoiceLineTrackId ON InvoiceLine (TrackId);
CREATE INDEX IFK_PlaylistTrackPlaylistId ON PlaylistTrack (PlaylistId);
CREATE INDEX IFK_PlaylistTrackTrackId ON PlaylistTrack (TrackId);
CREATE INDEX IFK_TrackAlbumId ON Track (AlbumId);
CREATE INDEX IFK_TrackGenreId ON Track (GenreId);
CREATE INDEX IFK_TrackMediaTypeId ON Track (MediaTypeId);
